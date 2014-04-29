package usermaker;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Mike
 */
public class UserMaker {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        
        String url = "jdbc:mysql://128.6.29.222:3306/nhcgame?user=root&password=TheoMensah";
        PreparedStatement stmt;
        PreparedStatement stmt1;
        PreparedStatement stmt2;
        Connection con;
        int[] mpop = new int[51];
        int[] fpop = new int[51];
        Random rand = new Random();
        String[] states = {"Alabama","Alaska","Arizona","Arkansas","California","Colorado","Connecticut","Delaware","DC","Florida","Georgia","Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas","Kentucky","Louisiana","Maine","Maryland","Massachusetts","Michigan","Minnesota","Mississippi","Missouri","Montana","Nebraska","Nevada","New Hampshire","New Jersey","New Mexico","New York","North Carolina","North Dakota","Ohio","Oklahoma","Oregon","Pennsylvania","Rhode Island","South Carolina","South Dakota","Tennesse","Texas","Utah","Vermont","Virginia","Washington","West Virginia","Wisconsin","Wyoming"};
        String[] state_pop = {"4822023","731449","6553255","2949131","38041430","5187582","3590347","917092","632323","19317568","9919945","1392313","1595728","12875255","6537334","3074186","2885905","4380415","4601893","1329192","5884563","6646144","9883360","5379139","2984926","6021988","1005141","1855525","2758931","1320718","8864590","2085538","19570261","9752073","699628","11544225","3814820","3899353","12763536","1050292","4723723","833354","6456243","26059203","2855287","626011","8185867","6897012","1855413","5726398","576412"};
        double[] state_per = {32.2,24.5,24.3,30.1,24.0,21.0,22.5,28,22.2,26.6,29.6,22.7,26.5,28.2,29.6,28.4,29.4,31.3,31.0,26.8,27.1,23.0,30.9,24.8,34.0,30.5,23.0,26.9,22.4,25.0,23.8,25.1,23.9,27.8,27.2,29.2,30.4,26.8,28.6,25.5,31.5,27.3,30.8,31.0,22.5,23.2,26.0,25.5,32.5,26.3,25.1};
        int[] fakepop = new int[51];
        char alphabet[] = {'1','2','3','4','5','6','7','8','9','0'};
        String mnames[] = {"andy","bob","carl","david","ethan","frank","gavin","homer","ian","jack","kent"};
        String fnames[] = {"aly","belle","carla","dorathy","elizabeth","fionna","gina","isabelle","johanna","kate"};
        int slen = alphabet.length;
        int uname_len;
        ResultSet rs;
        ResultSet rs1;
        ResultSet rs2;
        
        File file = new File("names.txt");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(UserMaker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        for(int i=0; i<51; i++){
            fakepop[i] = (int)((Integer.parseInt(state_pop[i])/100000) * (1-state_per[i]/100));
        }
        
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex){
            System.out.println(ex.getMessage());
            System.exit(1);
        }
        
        try {
            FileWriter fw = new FileWriter("names.txt",true);
            for(int y = 0; y < 1; y++){
                double[] s_points = new double[51];
                con = (Connection)DriverManager.getConnection(url);
                //Start Zombie Creation

                String usernames;
                double epoints;
                int k;
                int h;
                for(h=0;h<51;h++){
                    int l = fakepop[h];
                    for(k=0;k<l;k++){
                        epoints = rand.nextDouble() * 300;
                        uname_len = 1 + rand.nextInt(10);
                        
                        int statenum = h;
                        String sex;
                        if(rand.nextInt(2) == 0){
                            sex = "Male";
                            mpop[statenum]++;
                            usernames = mnames[rand.nextInt(10)];
                        }
                        else{
                            sex = "Female";
                            fpop[statenum]++;
                            usernames = fnames[rand.nextInt(10)];
                        }
                        int j;

                        for(j = 0; j < uname_len; j++){
                            usernames += alphabet[rand.nextInt(slen)];
                        }
                       
                        fw.write(usernames + "\n");
                        fw.flush();

                        stmt = (PreparedStatement)con.prepareStatement("SELECT * FROM Player");
                        rs = stmt.executeQuery();
                        
                        try{
                            while(rs.next()){
                                if(rs.getString("username").equals(usernames)){
                                    usernames += alphabet[rand.nextInt(slen)];
                                    rs.first();
                                }
                            }
                        }catch(SQLException sqlex){
                            
                        }
                        stmt = (PreparedStatement) con.prepareStatement("INSERT INTO Player (username, e_points, phone_number, password, sex) VALUES ('"+usernames+"', "+epoints+", '555-555-5555', 'test', '"+sex+"')");
                        stmt.executeUpdate();
                        stmt = (PreparedStatement) con.prepareStatement("INSERT INTO Lives_in (s_name, p_username) VALUES ('"+states[statenum]+"', '"+usernames+"')");
                        stmt.executeUpdate();
                    }
                }
                fw.close();
                //End Zombie Creation

                stmt = (PreparedStatement)con.prepareStatement("SELECT * FROM Player");
                rs = stmt.executeQuery();
                
                while(rs.next()){
                    String hold = rs.getString("username");
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
                    s_points[sf] = s_points[sf] + rs.getDouble("e_points");
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
                    stmt2 = (PreparedStatement) con.prepareStatement("UPDATE Location SET female_pop="+fpop[hold]+" WHERE state_name='"+states[hold]+"'");
                    stmt2.executeUpdate();
                    stmt2 = (PreparedStatement) con.prepareStatement("UPDATE Location SET male_pop="+mpop[hold]+" WHERE state_name='"+states[hold]+"'");
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
                    Logger.getLogger(UserMaker.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            }
            System.out.println("success");
        } catch (SQLException ex) {
            Logger.getLogger(UserMaker.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        } catch (IOException ex) {
            Logger.getLogger(UserMaker.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
