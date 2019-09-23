import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import static java.lang.Integer.parseInt;

class Warehouse extends JFrame {

    private DataBaseConnection conn;
    private String wh;

    private static JTextField id = new JTextField();
    private static JTextField good_id = new JTextField();
    private static JTextField good_count = new JTextField();
    private static JTextArea error = new JTextArea(5, 10);
    private static JTextField idForUpd = new JTextField();
    private static JTextField value = new JTextField();

    Warehouse(DataBaseConnection conn, int n) {
      super("Warehouse " + n);
      this.conn = conn;

      if (n == 1) { wh = "warehouse1"; }
      else if (n == 2) { wh = "warehouse2"; }

      this.setBounds(500, 200, 600, 400);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      Container container = this.getContentPane();
      container.setLayout(null);

      JLabel lgood_id = new JLabel("Good id: ");
      JLabel lgood_count = new JLabel("Good count: ");
      JLabel lid = new JLabel("Good id: ");
      JLabel lidForUpd = new JLabel("Good id for update: ");
      JLabel lvalue = new JLabel("Value: ");
      container.add(lidForUpd);
      container.add(lvalue);
      container.add(lgood_id);
      container.add(lid);
      container.add(lgood_count);
      container.add(idForUpd);
      container.add(value);
      container.add(good_id);
      container.add(id);
      container.add(good_count);

      JButton buttonAdd = new JButton("Add good");
      JButton buttonDel = new JButton("Delete good");
      JButton buttonUpd = new JButton("Update good");
      JButton buttonPred = new JButton("Previous window");
      JButton buttonShowAll = new JButton("All goods");
      buttonAdd.addActionListener(new Warehouse.ButtonAdd());
      buttonDel.addActionListener(new Warehouse.ButtonDel());
      buttonUpd.addActionListener(new Warehouse.ButtonUpd());
      buttonPred.addActionListener(new Warehouse.ButtonPred());
      buttonShowAll.addActionListener(new Warehouse.ShowAll());
      container.add(buttonAdd);
      container.add(buttonDel);
      container.add(buttonUpd);
      container.add(buttonPred);
      container.add(buttonShowAll);

      buttonAdd.setBounds(15, 20, 150, 20);
      lgood_id.setBounds(180, 20, 100, 20);
      good_id.setBounds(290, 20, 150, 20);
      lgood_count.setBounds(180, 50, 100, 20);
      good_count.setBounds(290, 50, 150, 20);

      buttonDel.setBounds(15, 80, 150, 20);
      lid.setBounds(180, 80, 100, 20);
      id.setBounds(290, 80, 150, 20);

      buttonUpd.setBounds(15, 110, 150, 20);
      lidForUpd.setBounds(180, 110, 150, 20);
      idForUpd.setBounds(290, 110, 150, 20);
      lvalue.setBounds(180, 140, 100, 20);
      value.setBounds(290, 140, 150, 20);

      buttonPred.setBounds(15, 200, 150, 20);

      buttonShowAll.setBounds(200, 200, 150, 20);

      error.setLineWrap(true);
      error.setWrapStyleWord(true);
      JScrollPane scroll = new JScrollPane(error,
              JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
              JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      container.add(scroll);
      scroll.setBounds(40, 230, 500, 120);
    }

    class ButtonAdd implements ActionListener {
      public void actionPerformed(ActionEvent e) {
        if (good_id.getText().length() == 0 || good_count.getText().length() == 0) {
          error.setText("    Invalid parameters!!!");
        } else {
          try {
            conn.addGoodOnWh(wh, parseInt(good_id.getText()), parseInt(good_count.getText()));
            error.setText("Success");
          } catch (Exception e1) {
            error.setText(e1.getMessage());
          }
        }
      }
    }

    class ButtonDel implements ActionListener {
      public void actionPerformed(ActionEvent e) {
        if (id.getText().length() == 0) {
          error.setText("    Invalid parameter!!!");
        } else {
          try {
            conn.delGoodFromWh(wh, parseInt(id.getText()));
            error.setText("Success");
          } catch (Exception e1) {
            error.setText(e1.getMessage());
          }
        }
      }
    }

    class ButtonUpd implements ActionListener {
      public void actionPerformed(ActionEvent e) {
        if (idForUpd.getText().length() == 0 || value.getText().length() == 0) {
          error.setText("    Invalid parameters!!!");
        } else {
          try {
            conn.updateWarehouse(wh, parseInt(idForUpd.getText()), parseInt(value.getText()));
            error.setText("Success");
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

    class ShowAll implements ActionListener {
      public void actionPerformed(ActionEvent e) {
        try {
          error.setText("good_id | good_count\n");
          ResultSet rs = conn.getAllGoodsWh(wh);
          while( rs.next() ) {
            error.append(rs.getInt("good_id") + "  " +
                    rs.getInt("good_count") + "\n");
          }
        } catch (Exception e1) {
          error.setText(e1.getMessage());
        }
      }
    }
  }
