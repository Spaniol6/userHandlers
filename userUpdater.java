package userupdater;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
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

                stmt = (PreparedStatement)con.prepareStatement("SELECT * FROM Player");
                rs = stmt.executeQuery();
                while(rs.next()){
                    String hold = rs.getString("username");
                    double upp = rs.getDouble("e_points") + rand.nextDouble() * 300;
                    
                    stmt2 = (PreparedStatement) con.prepareStatement("UPDATE Player SET e_points="+upp+" WHERE username='"+hold+"'");
                    stmt2.executeUpdate();
                    
                    stmt1 = (PreparedStatement)con.prepareStatement("SELECT * FROM Lives_in WHERE p_username='"+hold+"'");
                    rs1 = stmt1.executeQuery();
                    rs1.next();

                    hold = rs1.getString("s_name");
                    int sf = 0;
                    switch (hold) {
                        case "DC":
                            sf = 7;
                            break;
                        case "Delaware":
                            sf = 8;
                            break;
                        default:
                            while(!hold.equals(states[sf])){
                                sf++;
                            }
                            break;
                    }
                    s_points[sf] = s_points[sf] + upp;
                }

                for(int sf = 0; sf < 51; sf++){
                    int hold;
                    switch(sf){
                        case 7:
                            hold = 8;
                            break;
                        case 8:
                            hold = 7;
                            break;
                        default:
                            hold = sf;
                            break;
                    }
                    stmt2 = (PreparedStatement) con.prepareStatement("UPDATE Location SET generalpoints="+s_points[hold]+" WHERE state_name='"+states[hold]+"'");
                    stmt2.executeUpdate();
                }
                
                stmt = (PreparedStatement)con.prepareStatement("SELECT * FROM Location ORDER BY generalpoints DESC");
                rs = stmt.executeQuery();
                int rankc = 0;
                while(rs.next()){
                    String hold = rs.getString("state_name");
                    stmt1 = (PreparedStatement)con.prepareStatement("UPDATE Location SET rank="+rankc+" WHERE state_name='"+hold+"'");
                    stmt1.executeUpdate();
                    rankc++;
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
