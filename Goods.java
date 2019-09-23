import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

class Goods extends JFrame {

  private DataBaseConnection conn;

  private static JTextField name = new JTextField();
  private static JTextField priority = new JTextField();
  private static JTextField id = new JTextField();
  private static JTextArea error = new JTextArea(5, 10);
  private static JTextField idForUpd = new JTextField();
  private static JTextField whChange = new JTextField();
  private static JTextField value = new JTextField();

  Goods (DataBaseConnection conn) {
    super("Goods");
    this.conn = conn;
    this.setBounds(500, 200, 600, 400);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    Container container = this.getContentPane();
    container.setLayout(null);

    JLabel lname = new JLabel("Name: ");
    JLabel lprior = new JLabel("Priority: ");
    JLabel lid = new JLabel("Id: ");
    JLabel lidForUpd = new JLabel("Id for update: ");
    JLabel lwhChange = new JLabel("What change: ");
    JLabel lvalue = new JLabel("Value: ");
    container.add(lidForUpd);
    container.add(lwhChange);
    container.add(lvalue);
    container.add(lname);
    container.add(lid);
    container.add(lprior);
    container.add(idForUpd);
    container.add(whChange);
    container.add(value);
    container.add(name);
    container.add(priority);
    container.add(id);

    JButton buttonAdd = new JButton("Add good");
    JButton buttonDel = new JButton("Delete good");
    JButton buttonUpd = new JButton("Update good");
    JButton buttonPred = new JButton("Previous window");
    JButton buttonShowAll = new JButton("All goods");

    buttonAdd.addActionListener(new ButtonAdd());
    buttonDel.addActionListener(new ButtonDel());
    buttonUpd.addActionListener(new ButtonUpd());
    buttonPred.addActionListener(new ButtonPred());
    buttonShowAll.addActionListener(new ShowAll());
    container.add(buttonAdd);
    container.add(buttonDel);
    container.add(buttonUpd);
    container.add(buttonPred);
    container.add(buttonShowAll);
    error.setLineWrap(true);
    error.setWrapStyleWord(true);
    JScrollPane scroll = new JScrollPane(error,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    container.add(scroll);

    buttonAdd.setBounds(15, 20, 150, 20);
    lname.setBounds(180, 20, 100, 20);
    name.setBounds(290, 20, 150, 20);
    lprior.setBounds(180, 50, 100, 20);
    priority.setBounds(290, 50, 150, 20);

    buttonDel.setBounds(15, 80, 150, 20);
    lid.setBounds(180, 80, 100, 20);
    id.setBounds(290, 80, 150, 20);

    buttonUpd.setBounds(15, 110, 150, 20);
    lidForUpd.setBounds(180, 110, 100, 20);
    idForUpd.setBounds(290, 110, 150, 20);
    lwhChange.setBounds(180, 140, 100, 20);
    whChange.setBounds(290, 140, 150, 20);
    lvalue.setBounds(180, 170, 100, 20);
    value.setBounds(290, 170, 150, 20);

    buttonPred.setBounds(15, 200, 150, 20);
    buttonShowAll.setBounds(200, 200, 150, 20);
    scroll.setBounds(40, 230, 500, 120);
  }

  class ButtonAdd implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if (name.getText().length() == 0 || priority.getText().length() == 0) {
        error.setText("    Invalid parameters!!!");
      } else {
        try {
          conn.addGood(name.getText(), parseFloat(priority.getText()));
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
          conn.deleteGood(parseInt(id.getText()));
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
            case "priority":
              conn.updatePrGood(parseInt(idForUpd.getText()), parseFloat(value.getText()));
              error.setText("Success");
              break;
            case "name":
              conn.updateNameGood(parseInt(idForUpd.getText()), value.getText());
              error.setText("Success");
              break;
            default:
              error.setText(" Values in the scope 'what change' can be: name, priority");
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
        error.setText("id | name | priority\n");
        ResultSet rs = conn.getAllGoods();
        while( rs.next() ) {
          error.append(rs.getInt("id") + "  " +
                  rs.getString("name") + "  " +
                  rs.getFloat("priority") + "\n");
        }
      } catch (Exception e1) {
        error.setText(e1.getMessage());
      }
    }
  }

}
