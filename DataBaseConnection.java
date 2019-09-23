import java.sql.*;
import static java.lang.Class.forName;

class DataBaseConnection {

  private Connection connection = null;

  void connect() {
    try {
      forName("oracle.jdbc.driver.OracleDriver");
      connection = DriverManager.getConnection(
              "jdbc:oracle:thin:@localhost:1521:xe",
              "C##Victory", "oracle");

      System.out.println("Success");
    } catch (ClassNotFoundException ex){
      System.out.println(ex.getMessage());
    } catch (SQLException ex){
      System.out.println("SQLException caught");
      System.out.println("---");
      while (ex != null){
        System.out.println("Message : " + ex.getMessage());
        System.out.println("SQLState : " + ex.getSQLState());
        System.out.println("ErrorCode : " + ex.getErrorCode());
        System.out.println("---");
        ex = ex.getNextException();
      }
    }
  }

  void addGoodOnWh(String wh, int good_id, int good_count) throws SQLException {
    int n = getGoodCountWh(wh, good_id);
    if (n == 0) {
      PreparedStatement s = connection.prepareStatement("insert into " + wh +
              " (good_id, good_count) values (?, ?)");
      s.setInt(1, good_id);
      s.setInt(2, good_count);
      s.executeUpdate();
    } else {
      updateWarehouse(wh, good_id, n + good_count);
    }
  }

  void updateWarehouse(String wh, int good_id, int good_count) throws SQLException {
      PreparedStatement s = connection.prepareStatement("update " + wh +
              " set good_count = " + good_count +
              "where good_id = " + good_id);
      s.executeUpdate();
  }

  private int getGoodCountWh(String wh, int good_id) throws SQLException {
    PreparedStatement s = connection.prepareStatement("select sum(good_count)" +
            " from " + wh +
            " where good_id = " + good_id);
    ResultSet rs = s.executeQuery();
    int sum = 0;
    while(rs.next()) {
      sum = rs.getInt("sum(good_count)");
    }
    return sum;
  }

  ResultSet get5PopularGoods() throws SQLException {
      PreparedStatement s = connection.prepareStatement("select sales.good_id, goods.name, count(good_id) "
            + "from goods, sales "
            + "where goods.id = sales.good_id "
            + "group by sales.good_id, goods.name "
            + "order by count(sales.good_id) desc");
      return s.executeQuery();
  }

  ResultSet getAllGoods() throws SQLException {
    PreparedStatement s = connection.prepareStatement("select id, name, priority from goods");
    return s.executeQuery();
  }

  ResultSet getAllGoodsWh(String wh) throws SQLException {
    PreparedStatement s = connection.prepareStatement("select id, good_id, good_count from " + wh);
    return s.executeQuery();
  }

  ResultSet getAllSales() throws SQLException {
    PreparedStatement s = connection.prepareStatement("select id, good_id, good_count, create_date from sales");
    return s.executeQuery();
  }

  ResultSet getStatOfGood(int good_id, Timestamp start, Timestamp end) throws SQLException {
      PreparedStatement s = connection.prepareStatement("select sales.good_id, goods.name,"
              + " sales.good_count, sales.create_date"
              + " from goods, sales"
              + " where goods.id = sales.good_id and sales.good_id = ?"
              + " and sales.create_date between ? and ?"
              + " order by sales.create_date");
      s.setInt(1, good_id);
      s.setTimestamp(2, start);
      s.setTimestamp(3, end);
      return s.executeQuery();
  }

  void delGoodFromWh(String wh, int good_id) throws SQLException {
      PreparedStatement s = connection.prepareStatement("delete" +
              " from " + wh +
              " where good_id = " + good_id);
      s.executeQuery();
  }

  void addGood(String name, float priority) throws SQLException {
      PreparedStatement s = connection.prepareStatement("insert into goods (name, priority)" +
              " values (?, ?)");
      s.setString(1, name);
      s.setFloat(2, priority);
      s.executeUpdate();
  }

  void updateNameGood(int id, String name) throws SQLException {
      PreparedStatement s = connection.prepareStatement("update goods set name = ?" +
              "where id = ?");
      s.setString(1, name);
      s.setInt(2, id);
      s.executeUpdate();
  }

  void updatePrGood(int id, float priority) throws SQLException {
      PreparedStatement s = connection.prepareStatement("update goods set priority = ?" +
            "where id = ?");
      s.setFloat(1, priority);
      s.setInt(2, id);
      s.executeUpdate();
  }

  void deleteGood(int id) throws SQLException {
      PreparedStatement s = connection.prepareStatement("delete from goods" +
              " where id = ?");
      s.setInt(1, id);
      s.executeUpdate();
  }

  void addSale(int good_id, int good_count,  Timestamp create_date) throws SQLException {
    PreparedStatement s = connection.prepareStatement("INSERT INTO sales " +
            "(good_id, good_count, create_date) " +
            "VALUES (?, ?, ?)");
    s.setInt(1, good_id);
    s.setInt(2, good_count);
    s.setTimestamp(3, create_date);
    s.executeUpdate();
    int n1 = getGoodCountWh("warehouse1", good_id);
    int n2 = getGoodCountWh("warehouse2", good_id);
    if (n1 == good_count) {
      delGoodFromWh("warehouse1", good_id);
    }
    else if (n1 < good_count) {
      int sub = good_count - n1;
      delGoodFromWh("warehouse1", good_id);
      updateWarehouse("warehouse2", good_id, n2 - sub);
      if (getGoodCountWh("warehouse2", good_id) <= 0) {
        delGoodFromWh("warehouse2", good_id);
      }
    }
    else {
      updateWarehouse("warehouse1", good_id, n1 - good_count);
    }
  }

  void updateSaleGoodCount(int id, int good_count) throws SQLException {
      PreparedStatement s = connection.prepareStatement("update sales set good_count = ?" +
              "where id = ?");
      s.setInt(1, good_count);
      s.setInt(2, id);
      s.executeUpdate();
  }

  void updateSaleGoodId(int id, int good_id) throws SQLException {
    PreparedStatement s = connection.prepareStatement("update sales set good_id = ?" +
            "where id = ?");
    s.setInt(1, good_id);
    s.setInt(2, id);
    s.executeUpdate();
  }

  void updateSaleCrDate(int id, Timestamp create_date) throws SQLException {
      PreparedStatement s = connection.prepareStatement("update sales set create_date = ?" +
            "where id = ?");
      s.setTimestamp(1, create_date);
      s.setInt(2, id);
      s.executeUpdate();
  }

  void deleteSaleById(int id) throws SQLException {
      PreparedStatement s = connection.prepareStatement("delete from sales" +
            " where id = ?");
      s.setInt(1, id);
      s.executeUpdate();
  }
}
