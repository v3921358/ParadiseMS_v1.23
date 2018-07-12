package database;

import constants.GameConstants;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

public class DatabaseConnection {
    
    private static final ThreadLocal<Connection> con = new ThreadLocalConnection();
    public static final int CLOSE_CURRENT_RESULT = 1;
    public static final int KEEP_CURRENT_RESULT = 2;
    public static final int CLOSE_ALL_RESULTS = 3;
    public static final int SUCCESS_NO_INFO = -2;
    public static final int EXECUTE_FAILED = -3;
    public static final int RETURN_GENERATED_KEYS = 1;
    public static final int NO_GENERATED_KEYS = 2;

    public static final Connection getConnection() {
        return con.get();
    }

    public static final void closeAll() throws SQLException {
        for (final Connection con : ThreadLocalConnection.allConnections) {
	    if (con != null) {
            	con.close();
	    }
        }
    }

    private static final class ThreadLocalConnection extends ThreadLocal<Connection> {
        public static final Collection<Connection> allConnections = new LinkedList<Connection>();
        @Override
        protected final Connection initialValue() {
            try {
                Class.forName("com.mysql.jdbc.Driver"); // touch the mysql driver
            } catch (final ClassNotFoundException e) {
                System.err.println("ERROR" + e);
            }
            try {
                final Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/odin_" + (GameConstants.GMS ? "gms" : "sea") + "?autoReconnect=true", "root", "");
                allConnections.add(con);
                return con;
            } catch (SQLException e) {
                System.err.println("ERROR" + e);
                return null;
            }
        }
    }
}
