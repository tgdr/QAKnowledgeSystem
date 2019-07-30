package DataBase;

import net.sf.json.JSONObject;
import request.HttpGetRequest;
import request.HttpThread;

import javax.sound.midi.Soundbank;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/27.
 */
public class DbUtils {
 static int guankatishu=10;

    public static boolean getuserexist(String openid) {
        ResultSet rs = null;
        Connection conn = DbConn.getConnSql();
        PreparedStatement ps = null;

        boolean exist=false;

        if (conn != null) {
            try {
                String connstr = "select wx_openid from user_info where wx_openid ='" +
                        openid+"';";
                ps = conn.prepareStatement(connstr);
                rs = ps.executeQuery();
                if (rs.next()) {
                    exist = true;
                }

                //System.out.println(status + "ddddddddd");
            } catch (Exception e) {
                // TODO: handle exception
                exist = false;
                e.printStackTrace();
            } finally {
                try {
                    conn.close();
                    ps.close();

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
        return exist;
    }

    public static int getguankatotal(String openid) {
        ResultSet rs = null;
        Connection conn = DbConn.getConnSql();
        PreparedStatement ps = null;
        int count=-1;


        if (conn != null) {
            try {
                String connstr = "SELECT MAX(guanka_flag) FROM question_info;";
                ps = conn.prepareStatement(connstr);
                rs = ps.executeQuery();
                if (rs.next()) {
                    count = rs.getInt("MAX(guanka_flag)");
                }

                System.out.println(count + "ddddddddd");
            } catch (Exception e) {
                // TODO: handle exception
              //  exist = false;
                e.printStackTrace();
            } finally {
                try {
                    conn.close();
                    ps.close();

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
        return count;
    }




    public static Map getknowledge() {
        ResultSet rs = null;
        Connection conn = DbConn.getConnSql();
        PreparedStatement ps = null;
        JSONObject resjson = new JSONObject();
       String knowledge;
        Map<String,ArrayList> map = new HashMap();
        ArrayList msgid = new ArrayList();
        ArrayList msg_content = new ArrayList();


        if (conn != null) {
            try {
                String connstr = "select msg_id,msg_content  from irr_msg;";
                ps = conn.prepareStatement(connstr);
                rs = ps.executeQuery();
                while (rs.next()) {
                    msgid.add(rs.getInt("msg_id"));
                    msg_content.add(rs.getString("msg_content"));
                }
                map.put("id",msgid);
                map.put("content",msg_content);

                //System.out.println(status + "ddddddddd");
            } catch (Exception e) {
                // TODO: handle exception

                e.printStackTrace();
            } finally {
                try {
                    conn.close();
                    ps.close();

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
        return map;
    }





    public static int Login(String username, String password) {

        ResultSet rs = null;
        String resultpassword = "";
        int resultauth = -1;
        Connection conn = DbConn.getConnSql();

        PreparedStatement ps = null;

        if (conn != null) {
            try {

                String connstr = "select Password,auth  from user_reg where Username = '" + username + "';";
                ps = conn.prepareStatement(connstr);


                rs = ps.executeQuery();

                if (rs.next()) {
                    resultpassword = rs.getString("Password");
                    if (resultpassword.equals(password)) {
                        resultauth = rs.getInt("auth");
                    } else
                        resultauth = -1;
                } else {
                    resultauth = -1;
                }

                //System.out.println(status + "ddddddddd");
            } catch (Exception e) {
                // TODO: handle exception
                resultauth = -1;
                e.printStackTrace();
            } finally {
                try {
                    conn.close();
                    ps.close();

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
        return resultauth;
    }

    public static String getsession(String jscode){
        HttpThread thread = new HttpThread();
        thread.setUrl("https://api.weixin.qq.com/sns/jscode2session?appid=wx100a8e6e98585110&secret=4774a6faf2a316e80a8936470dc3cb8f&js_code=" +
                jscode+ "&grant_type=authorization_code");
        thread.start();
        int count=0;
        while (count<=10){
            if(thread.getRes() ==1){
                return  thread.getWebContent();
            }
            else{
                count++;
                try {
                    Thread.sleep(90);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

       return "failed";
    }

    public static String[] getuserinfo(String openid){
        String result[] = new String[3];
        ResultSet rs = null;

        Connection conn = DbConn.getConnSql();
        PreparedStatement ps = null;

        if (conn != null) {
            try {

                String connstr = "select * from user_info where wx_openid = '" + openid + "';";
                ps = conn.prepareStatement(connstr);


                rs = ps.executeQuery();
                if (rs.next()) {
                   result[0]= rs.getString("user_stuname");
                    result[1]= rs.getString("user_stunum");
                    result[2]= String.valueOf(rs.getInt("user_guanka"));
                }

                //System.out.println(status + "ddddddddd");
            } catch (Exception e) {
                // TODO: handle exception
                result = null;
                e.printStackTrace();
            } finally {
                try {
                    conn.close();
                    ps.close();

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
        return result;
    }

    public static String[][] getranklist(String openid){

        String result[][] = new String[5][10];   //显示前面10个人排行  数组长度固定为10
        ResultSet rs = null;

        Connection conn = DbConn.getConnSql();
        PreparedStatement ps = null;

        if (conn != null) {
            try {
                String connstr = "select user_stuname,user_stunum,user_guanka,user_photo from user_info order by user_guanka desc limit 10;";
                ps = conn.prepareStatement(connstr);
                rs = ps.executeQuery();
                int i=0;
                while (rs.next()) {

                    result[0][i]= rs.getString("user_stuname");
                    result[1][i]= rs.getString("user_stunum");
                    result[2][i]= String.valueOf(rs.getInt("user_guanka"));
                    result[3][i]= rs.getString("user_photo");
                    i++;
                }
                result[4][0]= i+"";

                //System.out.println(status + "ddddddddd");
            } catch (Exception e) {
                // TODO: handle exception
                result = null;
                e.printStackTrace();
            } finally {
                try {
                    conn.close();
                    ps.close();

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
        return result;
    }



    public static boolean adduser(String openid,String username,String usernum,String photourl){
        boolean status = false;
        Connection conn = null;
        Statement stmt = null;
        int result=-1;

   String execsql = "insert into user_info(wx_openid,user_stuname,user_stunum,user_guanka,user_photo) values ('"+openid+"','"+username+"','"+usernum+"',0,'" +photourl+
           "')";
        System.out.println(execsql);
        try {
            conn = DbConn.getConnSql();

            stmt = conn.createStatement();
           result  =stmt.executeUpdate(execsql);
           if(result>0){
               status= true;
           }
           else
               status = false;

        } catch (SQLException e) {
           // e.printStackTrace();
           // System.out.print("result"+result+"66666666666");

            status=false;
        }
        catch (Exception e1){
            status=false;
        }
        finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                status = false;
            }
        }
        return status;
    }

    public static int[] randomCommon(int min, int max, int n){
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        int[] result = new int[n];
        int count = 0;
        while(count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if(num == result[j]){
                    flag = false;
                    break;
                }
            }
            if(flag){
                result[count] = num;
                count++;
            }
        }
        return result;
    }

    public static String [][] getqabyguanka(String openid,int guanka){
        String result[][] = new String[7][10];
        ResultSet rs = null;
        int tihao[] = randomCommon(1+guankatishu*(guanka-1),11+guankatishu*(guanka-1),5);
        Connection conn = DbConn.getConnSql();
        PreparedStatement ps = null;

        if (conn != null) {
            try {

                String connstr = "select * from question_info where question_id = "+tihao[0]+" or question_id="+tihao[1]+" or question_id ="+tihao[2]+" or question_id="+tihao[3]+" or question_id ="+tihao[4];
                ps = conn.prepareStatement(connstr);
                //    System.out.println(connstr);
                rs = ps.executeQuery();
                int index = 0;
                while (rs.next()) {

                    result[0][index] = rs.getString("question_describe");   //result0  描述
                    result[1][index] = rs.getString("answer_A");  //result 1 a选项
                    result[2][index] = rs.getString("answer_B");
                    result[3][index] = rs.getString("answer_C");
                    result[4][index] = rs.getString("answer_D");
                    result[5][index] = rs.getString("answer_right");   //正确选项
                    result[6][index] = rs.getString("answer_hint");   //正确选项
                    //       System.out.println(result[1][index]);
                    index++;
                }

                //System.out.println(status + "ddddddddd");
            } catch (Exception e) {
                // TODO: handle exception
                result = null;
                e.printStackTrace();
            } finally {
                try {
                    conn.close();
                    ps.close();

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
        return result;
    }


    /**
     * 生成题目方法2
     * @return
     */

    public static String [][] getqabyguanka2(String openid,int guanka,int tishu){
        String result[][] = new String[7][tishu];
        ResultSet rs = null;
        Connection conn = DbConn.getConnSql();
        PreparedStatement ps = null;

        if (conn != null) {
            try {
                String connstr = "select * from question_info where guanka_flag = "+guanka+" ORDER BY rand() LIMIT "+tishu;

              //  String connstr = "select * from question_info where question_id<"+(11+guankatishu*(guanka-1))+ " and question_id>="+(1+guankatishu*(guanka-1))+" ORDER BY rand() LIMIT "+tishu;
                System.out.println(connstr);
                ps = conn.prepareStatement(connstr);
                //    System.out.println(connstr);
                rs = ps.executeQuery();
                int index = 0;
                while (rs.next()) {

                    result[0][index] = rs.getString("question_describe");   //result0  描述
                    result[1][index] = rs.getString("answer_A");  //result 1 a选项
                    result[2][index] = rs.getString("answer_B");
                    result[3][index] = rs.getString("answer_C");
                    result[4][index] = rs.getString("answer_D");
                    result[5][index] = rs.getString("answer_right");   //正确选项
                    result[6][index] = rs.getString("answer_hint");   //正确选项
                    //       System.out.println(result[1][index]);
                    index++;
                }

                System.err.println("                                        "+result[0].length);
                //System.out.println(status + "ddddddddd");
            } catch (Exception e) {
                // TODO: handle exception
                result = null;
                e.printStackTrace();
            } finally {
                try {
                    conn.close();
                    ps.close();

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
        return result;
    }


    public static   Map getalluserlocation() {
        List<String> getdbusername;
        List<Float> getdblocationx;
        List<Float> getdblocationy;
        List<String> getdblogtime;
        getdbusername = new ArrayList<String>();
        getdblocationx = new ArrayList<Float>();
        getdblocationy = new ArrayList<Float>();
        getdblogtime = new ArrayList<String>();
        Map<String,Object> dbdata = new HashMap();




        ResultSet rs = null;
        Connection conn = DbConn.getConnSql();

        PreparedStatement ps = null;
        if (conn != null) {
            try {
                String connstr = "select * from SqlHelpers order by LogTime desc ;";
                ps = conn.prepareStatement(connstr);
                rs = ps.executeQuery();
                while (rs.next()) {
                    getdbusername.add(rs.getString("Username"));
                    getdblocationx.add(rs.getFloat("Locationx"));
                    getdblocationy.add(rs.getFloat("Locationy"));
                    getdblogtime.add(rs.getString("LogTime"));
                }

                dbdata.put("dbusername",getdbusername);
                dbdata.put("dblocationx",getdblocationx);
                dbdata.put("dblocationy",getdblocationy);
                dbdata.put("dblogtime",getdblogtime);
               // System.out.print(dbdata.get("dbusername")+"ddddddddddddddddd");
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                try {
                    rs.close();
                    ps.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return dbdata;
    }


public static boolean updateuserguanka(String openid,int guanka){
    boolean status = false;
    Connection conn = null;
    PreparedStatement pstmt = null;
    int result=-1;
    String execsql = "update user_info set user_guanka = "+guanka+" where wx_openid='" +openid+"';";
    try {
        conn = DbConn.getConnSql();
        pstmt = (PreparedStatement) conn.prepareStatement(execsql);
        result  =pstmt.executeUpdate();
        if(result>0){
            status= true;
        }
        else
            status = false;

    } catch (SQLException e) {
        // e.printStackTrace();
        // System.out.print("result"+result+"66666666666");

        status=false;
    }
    catch (Exception e1){
        status=false;
    }
    finally {
        try {
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            status = false;
        }
    }
    return status;
}



}
