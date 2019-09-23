import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Timestamp;

import static java.lang.Integer.parseInt;

class Stat extends JFrame {

  private DataBaseConnection conn;
  private static JTextField good_id = new JTextField();
  private static JTextField start_date = new JTextField();
  private static JTextField end_date = new JTextField();
  private static JTextArea error = new JTextArea(5, 10);

  Stat(DataBaseConnection conn) {
    super("Statistics");
    this.conn = conn;
    this.setBounds(500, 200, 600, 400);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container container = this.getContentPane();
    container.setLayout(null);

    JLabel lgood_id = new JLabel("Good id: ");
    JLabel lstartdate = new JLabel("Start date: ");
    JLabel lenddate = new JLabel("End date: ");
    container.add(good_id);
    container.add(start_date);
    container.add(end_date);
    container.add(lenddate);
    container.add(lgood_id);
    container.add(lstartdate);

    JButton stat = new JButton("Statistics");
    JButton mostpop = new JButton("5 most popular goods");
    JButton graph = new JButton("Graphics");
    JButton buttonPred = new JButton("Previous window");
    stat.addActionListener(new Stat.getStat());
    buttonPred.addActionListener(new Stat.ButtonPred());
    mostpop.addActionListener(new Stat.Pop());
    graph.addActionListener(new Stat.GetGraph());
    container.add(stat);
    container.add(graph);
    container.add(buttonPred);
    container.add(mostpop);

    stat.setBounds(15, 20, 150, 20);
    lgood_id.setBounds(180, 20, 100, 20);
    good_id.setBounds(290, 20, 150, 20);
    lstartdate.setBounds(180, 50, 100, 20);
    start_date.setBounds(290, 50, 150, 20);
    lenddate.setBounds(180, 80, 100, 20);
    end_date.setBounds(290, 80, 150, 20);

    mostpop.setBounds(180, 110, 200, 20);
    graph.setBounds(15, 110, 150, 20);
    buttonPred.setBounds(15, 140, 150, 20);

    error.setLineWrap(true);
    error.setWrapStyleWord(true);
    JScrollPane scroll = new JScrollPane(error,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    container.add(scroll);
    scroll.setBounds(40, 170, 500, 180);
  }

  class getStat implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if (good_id.getText().length() == 0 || start_date.getText().length() == 0
              || end_date.getText().length() == 0) {
        error.setText("    Invalid parameters!!!");
      } else {
        try {
          error.setText("");
          ResultSet rs = conn.getStatOfGood(parseInt(good_id.getText()), Timestamp.valueOf(start_date.getText() + " 00:00:00"),
                  Timestamp.valueOf(end_date.getText() + " 00:00:00"));
          while( rs.next() ) {
            error.append(rs.getInt("good_id") + "  " +
                    rs.getString("name") + "  " +
                    rs.getInt("good_count") + "  " +
                    rs.getTimestamp("create_date") + "\n");
          }
        } catch (Exception e1) {
          error.setText(e1.getMessage());
        }
      }
    }
  }

  class ButtonPred implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      error.setText("");
      setVisible(false);
      Interface app = new Interface(conn);
      app.setVisible(true);
    }
  }

  class Pop implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      try {
        error.setText("good_id | name | count\n");
        ResultSet rs = conn.get5PopularGoods();
        int count = 0;
        while( rs.next() && count < 5 ) {
          error.append(rs.getInt("good_id") + "  " +
                    rs.getString("name") + "  " +
                    rs.getInt("count(good_id)") + "\n");
          count++;
        }
      } catch (Exception e1) {
        error.setText(e1.getMessage());
      }
    }
  }

  class GetGraph implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if (good_id.getText().length() == 0 || start_date.getText().length() == 0
              || end_date.getText().length() == 0) {
        error.setText("    Invalid parameters!!!");
      } else {
        try {
          error.setText("");
          ResultSet rs = conn.getStatOfGood(parseInt(good_id.getText()), Timestamp.valueOf(start_date.getText()
                          + " 00:00:00"),
                  Timestamp.valueOf(end_date.getText() + " 00:00:00"));
          int count = 0;
          while (rs.next()) {
            error.append(count+1 + "  " +
                    rs.getString("name") + "  " +
                    rs.getInt("good_count") + "  " +
                    rs.getTimestamp("create_date") + "\n");
            count++;
          }
          if (count != 0) {
            ResultSet rs1 = conn.getStatOfGood(parseInt(good_id.getText()), Timestamp.valueOf(start_date.getText()
                            + " 00:00:00"),
                    Timestamp.valueOf(end_date.getText() + " 00:00:00"));
            double y[] = new double[count];
            double x[] = new double[count];
            double i = 0;
            while (rs1.next()) {
              y[(int) i] = rs1.getDouble("good_count");
              x[(int) i] = i;
              i += 1;
            }
            SwingUtilities.invokeLater(() -> {
              JFrame frame = new JFrame("Chart");
              frame.setSize(600, 400);
              frame.setVisible(true);
              XYDataset ds = createDataset(x, y);
              JFreeChart chart = ChartFactory.createXYLineChart("Change in the demand of the good", "",
                      "count", ds, PlotOrientation.VERTICAL, true, true, false);
              ChartPanel cp = new ChartPanel(chart);
              frame.getContentPane().add(cp);
            });
          }
          else {
            error.setText("No data for graph...");
          }
        } catch (Exception e1) {
          error.setText(e1.getMessage());
        }
      }
    }

    private XYDataset createDataset(double x[], double y[]) {
      DefaultXYDataset ds = new DefaultXYDataset();
      double[][] data = { x, y };
      ds.addSeries("series1", data);
      return ds;
    }

  }
}
