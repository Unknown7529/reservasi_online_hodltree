package hodltree;
import java.sql.*;
import javax.swing.*;

public class koneksi {
    private static Connection KoneksiDatabse;
    public static Connection koneksiDB() throws SQLException {
        try {
            String DB ="jdbc:mysql://localhost/hodltree";
            String user="root";
            String pass="";
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            KoneksiDatabse = (Connection) DriverManager.getConnection(DB,user,pass);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Tidak ada koneksi","Error",
            JOptionPane.INFORMATION_MESSAGE);
            System.err.println(e.getMessage());
            System.exit(0);
        }
         return KoneksiDatabse;
    }
}