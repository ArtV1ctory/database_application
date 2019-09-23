class Main {

  public static void main(String[] args) {
    DataBaseConnection conn = new DataBaseConnection();
    conn.connect();
    Interface app = new Interface(conn);
    app.setVisible(true);
  }
}