import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Interface extends JFrame {

  private DataBaseConnection conn;

  Interface (DataBaseConnection conn) {
    super("DataBase");
    this.conn = conn;
    JButton goods = new JButton("Update goods");
    JButton sales = new JButton("Update sales");
    JButton wh1 = new JButton("Update goods on warehouse1");
    JButton wh2 = new JButton("Update goods on warehouse2");
    JButton stat = new JButton("Statistics about goods");

    this.setBounds(500, 200, 400, 400);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    Container container = this.getContentPane();
    container.setLayout (null);

    goods.addActionListener(new UpdGoods());
    sales.addActionListener(new UpdSales());
    wh1.addActionListener(new Wh1());
    wh2.addActionListener(new Wh2());
    stat.addActionListener(new Statistic());
    container.add(goods);
    container.add(sales);
    container.add(wh1);
    container.add(wh2);
    container.add(stat);

    goods.setBounds(80, 50, 210, 30);
    sales.setBounds(80,110, 210, 30);
    wh1.setBounds(80,170, 210, 30);
    wh2.setBounds(80,230,210,30);
    stat.setBounds(80, 290, 210, 30);
  }

  class UpdGoods implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      setVisible(false);
      Goods goods = new Goods(conn);
      goods.setVisible(true);
    }
  }

  class UpdSales implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      setVisible(false);
      Sales sales = new Sales(conn);
      sales.setVisible(true);
    }
  }

  class Wh1 implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      setVisible(false);
      Warehouse wh1 = new Warehouse(conn, 1);
      wh1.setVisible(true);
    }
  }

  class Wh2 implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      setVisible(false);
      Warehouse wh2 = new Warehouse(conn, 2);
      wh2.setVisible(true);
    }
  }

  class Statistic implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      setVisible(false);
      Stat stat = new Stat(conn);
      stat.setVisible(true);
    }
  }
}
