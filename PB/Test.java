import java.sql.*;
public class Test{
public static void main(String[] args) {
try {
String sourceURL;
Connection databaseConnection;
if (!true) { // Oracle
Class.forName("oracle.jdbc.driver.OracleDriver");
// Create a connection through the DriverManager
sourceURL =
"jdbc:oracle:thin:@todo.fri.uni-lj.si:1521:vaje";
} else { // MySQL
Class.forName("com.mysql.jdbc.Driver");
// Create a connection through the DriverManager
sourceURL = "jdbc:mysql://localhost:3306/vaje";
}
databaseConnection =
DriverManager.getConnection
(sourceURL, "pb", "pbvaje");
System.out.println("Connection is: "+databaseConnection);
Statement statement = DatabaseConnection.createStatement();
ResultSet result = statement.executeQuery
("SELECT * FROM jadralec");
// Meta podatki
ResultSetMetaData md = result.getMetaData();
int count = md.getColumnCount();
for (int i=1; i<=count; i++)
System.out.print(md.getColumnLabel(i)+"\t");
System.out.println();
// Izpis v tekstovni obliki
while (result.next()) {
for (int i=1; i<=count; i++)
System.out.print(result.getString(i)+"\t");
System.out.println();
}
// Zapri ResultSet , Statement, Connection
result.close(); statement.close(); databaseConnection.close();
} catch(ClassNotFoundException cnfe) {
System.err.println(cnfe);
} catch(SQLException sqle) {
System.err.println(sqle);
}
}
}