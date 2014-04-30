package userupdater;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mike
 */
public class UserUpdater {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        
        double perc;
        
        Scanner in = new Scanner(System.in);
        
        System.out.println("Enter percent of people to update (0.0 - 1.0)");
        perc = in.nextDouble();
        
        if(perc < 0){
            perc = -perc;
            System.out.println("Input can't be negative.  Converted to absolute value.");
        }
        
        if(perc > 1){
            System.out.println("Input too big, resized to 1");
            perc = 1;
        }
        
        PreparedStatement stmt;
        PreparedStatement stmt1;
        PreparedStatement stmt2;
        ResultSet rs;
        ResultSet rs1;
        String url = "jdbc:mysql://128.6.29.222:3306/nhcgame?user=root&password=TheoMensah";
        Connection con;
        Random rand = new Random();
        String[] states = {"Alabama","Alaska","Arizona","Arkansas","California","Colorado","Connecticut","Delaware","DC","Florida","Georgia","Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas","Kentucky","Louisiana","Maine","Maryland","Massachusetts","Michigan","Minnesota","Mississippi","Missouri","Montana","Nebraska","Nevada","New Hampshire","New Jersey","New Mexico","New York","North Carolina","North Dakota","Ohio","Oklahoma","Oregon","Pennsylvania","Rhode Island","South Carolina","South Dakota","Tennesse","Texas","Utah","Vermont","Virginia","Washington","West Virginia","Wisconsin","Wyoming"};
        double[] s_points = new double[51];
        
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex){
            System.out.println(ex.getMessage());
            System.exit(1);
        }
        try{
                con = (Connection)DriverManager.getConnection(url);
                int count = 0;
                
                stmt = (PreparedStatement)con.prepareStatement("SELECT * FROM Player");
                rs = stmt.executeQuery();
                while(rs.next()){
                    count++;
                }
                rs.first();
                
                count = (int) (count * perc);
                System.out.println(count);
                int inc = 0;
                while(inc < count){
                    String hold = rs.getString("username");
                    double upp = rand.nextDouble() * 5;
                    
                    stmt1 = (PreparedStatement) con.prepareStatement("INSERT INTO Upload(distance, p_username, eType,upload_date,upload_time) VALUES ("+upp+",'"+hold+"','walking', NOW(),CURTIME())");
                    stmt1.executeUpdate();
                    inc++;
                    rs.next();
                }
                try {
                    System.out.println("waiting");
                    con.close();
                    TimeUnit.MINUTES.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(UserUpdater.class.getName()).log(Level.SEVERE, null, ex);
                }
            System.out.println("success");
        }catch (SQLException ex) {
                Logger.getLogger(UserUpdater.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1);
        }
        
    }
}
