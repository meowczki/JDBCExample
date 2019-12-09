import java.sql.*;
public class TestMySQL {
    // przydatna strona -> http://www.mysqltutorial.org/mysql-jdbc-tutorial/
    public static void main(String[] args) {
        try {
            // ladowanie klasy sterownika, wymaga wyjatku ClassNotFoundException
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder urlSB = new StringBuilder("jdbc:mysql://"); // polaczenie z MySQL
        urlSB.append("localhost:3306/"); // numer portu
        urlSB.append("bazaWin2?"); // nazwa bazy (bazaWin)
        urlSB.append("useUnicode=true&characterEncoding=utf"); // kodowanie
        urlSB.append("-8&user=student"); // nazwa uzytkownika (root)
        urlSB.append("&password=BazyDanych_015s"); // haslo uzytkownika
        urlSB.append("&serverTimezone=CET"); // strefa czasowa (CET)
        String connectionUrl = urlSB.toString();
        try {
            // utworzenie polaczenia z baza wymaga wyjatku SQLException
            Connection conn = DriverManager.getConnection(connectionUrl);
            // ***************** wyswietlenie tabel w bazie danych

            PreparedStatement showTablesST = conn.prepareStatement("SHOW TABLES");
// utworzenie zapytania

            ResultSet rs1 = showTablesST.executeQuery(); // wykonanie zapytania
            System.out.println("Nazwy kolumn:");
            while (rs1.next()) { // iterowanie po krotkach wyniku (next() to kursor do konkretnej krotki)
                String s = rs1.getString(1);
                System.out.print(s + ", ");
            }
            System.out.println("");
            // ***************** WYSWIETLENIE TABELI *****************
            PreparedStatement selectAllSt = conn.prepareStatement("SELECT * FROM wina;");
            ResultSet rsAllSt = selectAllSt.executeQuery(); // wykonanie zapytania
            printResultSet(rsAllSt);
            // ***************** INSERT *****************
            // dodanie wina tylko z polem nazwy
            PreparedStatement insertTokaj = conn.prepareStatement("INSERT INTO wina(id,nazwa) VALUES (9,'Tokaj');");
            // liczba rzedow, ktore zostaly zmienione (zmienna typu int)
            int rowAffected = insertTokaj.executeUpdate(); // wykonanie zapytania
            System.out.println(String.format("INSERT: Row affected %d", rowAffected));
            rsAllSt = selectAllSt.executeQuery(); // sprawdzenie tabeli powykonaniu INSERT
            printResultSet(rsAllSt);
            // ***************** UPDATE *****************
            PreparedStatement updateST = conn.prepareStatement("UPDATE wina SET nazwa = 'Beaujolais' WHERE id = 9;");
            rowAffected = updateST.executeUpdate();
            System.out.println(String.format("UPDATE: Row affected %d", rowAffected));
            rsAllSt = selectAllSt.executeQuery(); // sprawdzenie tabeli po wykonaniu UPDATE
            printResultSet(rsAllSt);
            // ***************** DELETE *****************
            PreparedStatement deleteST = conn.prepareStatement("DELETE FROM wina WHERE nazwa = 'Beaujolais';");
            rowAffected = deleteST.executeUpdate();
            System.out.println(String.format("DELETE: Row affected %d", rowAffected));
            rsAllSt = selectAllSt.executeQuery(); // sprawdzenie tabeli po wykonaniu DELETE
            printResultSet(rsAllSt);
            // ***************** SELECT + WHERE *****************
            PreparedStatement selectST = conn.prepareStatement("SELECT distinct kraj_pochodzenia AS kraje FROM wina;");
            ResultSet rsSelect = selectST.executeQuery();
            System.out.println("Kraje pochodzenia win");
            printResultSet(rsSelect);
            // ***************** JOIN *****************
            PreparedStatement selectJoin = conn.prepareStatement("SELECT w.nazwa, z.potrawa FROM wina w " +
                    "JOIN zastosowania z ON w.id = z.id_wina;");
            ResultSet rsJoin = selectJoin.executeQuery();
            System.out.println("Wynik zlaczenia");
            printResultSet(rsJoin);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void printResultSet(ResultSet resultSet) throws SQLException {
        ResultSetMetaData rsmd = resultSet.getMetaData(); // metadane o zapytaniu
        int columnsNumber = rsmd.getColumnCount(); // liczba kolumn
        while (resultSet.next()) { // wyswietlenie nazw kolumn i wartosci w rzedach
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1)
                    System.out.print(", ");
                String columnValue = resultSet.getString(i);
                System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
            }
            System.out.println("");
        }
        System.out.println("");
    }
}