package k2dparser.data;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

/**
 *
 * @author trusty
 */
public class OrgunitsUpdater {
    static Connection conn;
    static PreparedStatement pstmt;
    static final String url = "jdbc:mysql://localhost/k2ddummy";

    
        //fetches moh711 dataelement IDs and returns a string array
    public static LinkedHashMap<String, String> fetchOrgunits() {
    	LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        //establish connection to DB
        try {
            //register jdbc driver
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(url, "root", "password");
            conn.setAutoCommit(false);

            String sql = "SELECT orgunitid,facilityName FROM orgunits";
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            //determine number of rows in in the resultset
            rs.last(); //move cursor to the last row
            //int rowcount = rs.getRow();
            //array = new String[rowcount - 1];
            rs.first();//return cursor to the first row
            int count = 0;
            while(rs.next()){
                //retrieve by column name
                String orgunitid = rs.getString("orgunitid");
                String orgunitname = rs.getString("facilityName");
                
                map.put(orgunitid, orgunitname);
                //array[count] = id;
                count++;
            }
            rs.close();
            conn.close();
            pstmt.close();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
       return map; 
    }

}
