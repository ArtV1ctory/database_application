import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Timestamp;

import static java.lang.Integer.parseInt;

class Sales extends JFrame {

  private DataBaseConnection conn;

  private static JTextField id = new JTextField();
  private static JTextField good_id = new JTextField();
  private static JTextField good_count = new JTextField();
  private static JTextField create_date = new JTextField();
  private static JTextArea error = new JTextArea(5, 10);
  private static JTextField idForUpd = new JTextField();
  private static JTextField whChange = new JTextField();
  private static JTextField value = new JTextField();

  Sales(DataBaseConnection conn) {
    super("Sales");
    this.conn = conn;
    this.setBounds(500, 200, 600, 400);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container container = this.getContentPane();
    container.setLayout(null);

    JLabel lgood_id = new JLabel("Good id: ");
    JLabel lgood_count = new JLabel("Good count: ");
    JLabel lcreate_date = new JLabel("Create date: ");
    JLabel lid = new JLabel("Id: ");
    JLabel lidForUpd = new JLabel("Id for update: ");
    JLabel lwhChange = new JLabel("What change: ");
    JLabel lvalue = new JLabel("Value: ");
    container.add(lidForUpd);
    container.add(lwhChange);
    container.add(lvalue);
    container.add(lgood_id);
    container.add(lid);
    container.add(lgood_count);
    container.add(lcreate_date);
    container.add(idForUpd);
    container.add(whChange);
    container.add(value);
    container.add(create_date);
    container.add(good_id);
    container.add(good_count);
    container.add(id);

    JButton buttonAdd = new JButton("Add sale");
    JButton buttonDel = new JButton("Delete sale");
    JButton buttonUpd = new JButton("Update sale");
    JButton buttonPred = new JButton("Previous window");
    JButton buttonShowAll = new JButton("All sales");
    buttonAdd.addActionListener(new Sales.ButtonAdd());
    buttonDel.addActionListener(new Sales.ButtonDel());
    buttonUpd.addActionListener(new Sales.ButtonUpd());
    buttonPred.addActionListener(new Sales.ButtonPred());
    buttonShowAll.addActionListener(new Sales.ShowAll());
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
    lcreate_date.setBounds(180, 80, 100, 20);
    create_date.setBounds(290, 80, 150, 20);

    buttonDel.setBounds(15, 110, 150, 20);
    lid.setBounds(180, 110, 100, 20);
    id.setBounds(290, 110, 150, 20);

    buttonUpd.setBounds(15, 140, 150, 20);
    lidForUpd.setBounds(180, 140, 100, 20);
    idForUpd.setBounds(290, 140, 150, 20);
    lwhChange.setBounds(180, 170, 100, 20);
    whChange.setBounds(290, 170, 150, 20);
    lvalue.setBounds(180, 200, 100, 20);
    value.setBounds(290, 200, 150, 20);

    buttonPred.setBounds(15, 230, 150, 20);

    buttonShowAll.setBounds(200, 230, 150, 20);

    error.setLineWrap(true);
    error.setWrapStyleWord(true);
    JScrollPane scroll = new JScrollPane(error,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    container.add(scroll);
    scroll.setBounds(40, 260, 500, 90);
  }

  class ButtonAdd implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if (good_id.getText().length() == 0 || good_count.getText().length() == 0
              || create_date.getText().length() == 0) {
        error.setText("    Invalid parameters!!!");
      } else {
        try {
          conn.addSale(parseInt(good_id.getText()), parseInt(good_count.getText()),
                  Timestamp.valueOf(create_date.getText() + " 00:00:00"));
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
          conn.deleteSaleById(parseInt(id.getText()));
          error.setText("Success");
        } catch (Exception e1) {
          error.setText(e1.getMessage());
        }
      }
    }
  }

  class ButtonUpd implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if (whChange.getText().length() == 0 || idForUpd.getText().length() == 0 || value.getText().length() == 0) {
        error.setText("    Invalid parameters!!!");
      }
      else {
        try {
          switch (whChange.getText()) {
            case "good_count":
              conn.updateSaleGoodCount(parseInt(idForUpd.getText()), parseInt(value.getText()));
              error.setText("Success");
              break;
            case "create_date":
              conn.updateSaleCrDate(parseInt(idForUpd.getText()),
                      Timestamp.valueOf(value.getText() + " 00:00:00"));
              error.setText("Success");
              break;
            case "good_id":
              conn.updateSaleGoodId(parseInt(idForUpd.getText()), parseInt(value.getText()));
              error.setText("Success");
              break;
            default:
              error.setText(" Values in the scope 'what change' can be: good_count, create_date, good_id");
              break;
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

  class ShowAll implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      try {
        error.setText("id | good_id | good_count | create_date\n");
        ResultSet rs = conn.getAllSales();
        while( rs.next() ) {
          error.append(rs.getInt("id") + "  " +
                  rs.getInt("good_id") + "  " +
                  rs.getInt("good_count") + "  " +
                  rs.getTimestamp("create_date") + "\n");
        }
      } catch (Exception e1) {
        error.setText(e1.getMessage());
      }
    }
  }

}
