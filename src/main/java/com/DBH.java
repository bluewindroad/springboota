package com;

import com.encrypty.splitedMatrix;
import com.encrypty.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
//import com.google.gson.JsonObject;
import com.table.bloodTable;
import com.table.toothTable;

import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.ArrayList;
public class DBH {
    private static Connection Conn; // 数据库连接对象

    // 数据库连接地址
    private static String URL = "jdbc:mysql://localhost:3306/healthCare?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";

    // 数据库的用户名
    private static String UserName = "root";
    // 数据库的密码
    private static String Password = "111111";

    /**
     * * @Description: TODO 获取访问数据库的Connection对象
     * @param @return
     * @return Connection 连接数据的对象
     * @author 情绪i
     */
    public static Connection getConnection() {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver"); // 加载驱动

        //    System.out.println("加载驱动成功!!!");
        } catch (ClassNotFoundException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        try {

            //通过DriverManager类的getConenction方法指定三个参数,连接数据库
            Conn = DriverManager.getConnection(URL, UserName, Password);
     //       System.out.println("连接数据库成功!!!");
            //返回连接对象
            return Conn;

        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }

    private static ArrayList<JSONObject> searchForBlood(JSONObject jo) throws SQLException, IOException, ClassNotFoundException {
   //     System.out.println(11111);
   //     if (Conn == null)
     //       return;
        ArrayList<JSONObject> JAL=new ArrayList<>();
     //   List<JsonObject> Lj=new List<>();
        Statement stt;
        int patientID=jo.getInteger("patientId");
         patientID=11;//测试！！！！
        String dateFrom=jo.getString("startDate");
        String dateTo=jo.getString("endDate");
        String Sql = "select ";
     //   System.out.println(jo.get("红细胞"));
        if(jo.get("rbc")!=null&&jo.getBoolean("rbc"))Sql+="rbc,rbc_index,";
        if(jo.get("wbc")!=null&&jo.getBoolean("wbc"))Sql+="wbc,wbc_index,";
        if(jo.get("plt")!=null&&jo.getBoolean("plt"))Sql+="plt,plt_index,";
        Sql+="date,bloodNo from blood_routine where date_format(date,'%Y-%m-%d') between \""+dateFrom+"\" and \""+dateTo+
        "\" and patientID="+patientID+";";
    //    System.out.println(Sql);
        stt = Conn.createStatement();
        ResultSet set=null;
        // 返回结果集
 //       System.out.println(Sql);
        set = stt.executeQuery(Sql);
        // 获取数据
        splitedMatrix rbcUp=new splitedMatrix(),rbcDown=new splitedMatrix(),
                wbcUp=new splitedMatrix(),wbcDown=new splitedMatrix(),
                pltDown=new splitedMatrix(),pltUp=new splitedMatrix();


   //     secret.getSecureQueryIndex()
        if(jo.get("rbc")!=null&&jo.getBoolean("rbc")){
            rbcDown=secret.getSecureQueryIndex(jo.getDouble("rbcDown"));
            rbcUp=secret.getSecureQueryIndex(jo.getDouble("rbcUp"));

           // rbcDown=secret.getSecureQueryIndex(15);
           // rbcUp=secret.getSecureQueryIndex(500);
        }
        if(jo.get("wbc")!=null&&jo.getBoolean("wbc")){
        //    System.out.println(jo.get("红细胞下限").getAsDouble());
            wbcDown=secret.getSecureQueryIndex(jo.getDouble("wbcDown"));
            wbcUp=secret.getSecureQueryIndex(jo.getDouble("wbcUp"));

         //   wbcDown=secret.getSecureQueryIndex(15);
          //  wbcUp=secret.getSecureQueryIndex(500);
        }
        if(jo.get("plt")!=null&&jo.getBoolean("plt")){
            pltDown=secret.getSecureQueryIndex(jo.getDouble("pltDown"));
            pltUp=secret.getSecureQueryIndex(jo.getDouble("pltUp"));
        }


       // System.out.println(set.getString(1));
        while (set.next()) {

            bloodTable bt=new bloodTable();
            bt.incluStr=new Vector<>();
            bt.num=new Vector<>();
            int count=0;
      //      System.out.println(1111);
            if(jo.getBoolean("rbc")!=null){
           //     System.out.println(111);
                Blob bb= set.getBlob(count+2);
                InputStream is=bb.getBinaryStream();                //获取二进制流对象
                BufferedInputStream bis=new BufferedInputStream(is);    //带缓冲区的流对象

                byte[] buff=new byte[(int) bb.length()];
                bis.read(buff, 0, buff.length);           //一次性全部读到buff中
                ObjectInputStream in=new ObjectInputStream(new ByteArrayInputStream(buff));
                splitedMatrix sMarix=(splitedMatrix)in.readObject();

              //  System.out.println(new Query().Search(rbcDown,rbcUp,sMarix));
                if(new Query().Search(rbcDown,rbcUp,sMarix)==0){
                  //  System.out.println(set.getString(count+1));
                    bt.num.add(Double.parseDouble(AES.decrypt(set.getString(count+1))));
               //     bt.num.add(Double.parseDouble(set.getString(count+1)));
                //    System.out.println(AES.decrypt(set.getString(count+1)));
                    count+=2;
                    bt.incluStr.add("rbc");
                }
                else continue;
            }

            if(jo.getBoolean("wbc")!=null){

                Blob bb= set.getBlob(count+2);
                InputStream is=bb.getBinaryStream();                //获取二进制流对象
                BufferedInputStream bis=new BufferedInputStream(is);    //带缓冲区的流对象

                byte[] buff=new byte[(int) bb.length()];
                bis.read(buff, 0, buff.length);           //一次性全部读到buff中
                ObjectInputStream in=new ObjectInputStream(new ByteArrayInputStream(buff));
                splitedMatrix sMarix=(splitedMatrix)in.readObject();

          //      System.out.println(new Query().Search(rbcDown,rbcUp,sMarix));
                if(new Query().Search(wbcDown,wbcUp,sMarix)==0){
                    bt.num.add(Double.parseDouble(AES.decrypt(set.getString(count+1))));
                 //   bt.num.add(Double.parseDouble(set.getString(count+1)));
                //    System.out.println(set.getString(count+1));
                    count+=2;
                    bt.incluStr.add("wbc");
                }
                else continue;
            }

            if(jo.getBoolean("plt")!=null&&jo.getBoolean("plt")){

                Blob bb= set.getBlob(count+2);
                InputStream is=bb.getBinaryStream();                //获取二进制流对象
                BufferedInputStream bis=new BufferedInputStream(is);    //带缓冲区的流对象

                byte[] buff=new byte[(int) bb.length()];
                bis.read(buff, 0, buff.length);           //一次性全部读到buff中
                ObjectInputStream in=new ObjectInputStream(new ByteArrayInputStream(buff));
                splitedMatrix sMarix=(splitedMatrix)in.readObject();


            //    System.out.println(new Query().Search(pltDown,pltUp,sMarix));
                if(new Query().Search(pltDown,pltUp,sMarix)==0){
                    bt.num.add(Double.parseDouble(AES.decrypt(set.getNString(count+1))));
                 //   System.out.println(set.getString(count+1));
                //    bt.num.add(Double.parseDouble(set.getString(count+1)));
                    count+=2;
                    bt.incluStr.add("plt");
                }
                else continue;
            }
            bt.m_tableNum=set.getInt(count+2);
            bt.m_type="bloodTable";
            bt.m_date=set.getDate(count+1).toString();
           // System.out.println(bt.m_date);
      //      System.out.println(bt.num);
        //    System.out.println(jsonHelper.talbeToJson(bt));
            JAL.add(jsonHelper.talbeToJson(bt));
   //     System.out.println(patientID);
        //jo.get("病人id");
         //   System.out.println(bt.num);

    }
//        System.out.println(JAL);
        return JAL;
    }

    private static ArrayList<JSONObject> searchForTooth(JSONObject jo) throws SQLException, IOException, ClassNotFoundException {
       //未完成


        ArrayList<JSONObject> JAL=new ArrayList<>();
        Statement stt;
        int patientID=jo.getInteger("patientId");
        String dateFrom=jo.getString("startDate");
        String dateTo=jo.getString("endDate");
        String Sql = "select ";
        if(jo.get("pain")!=null&&jo.getBoolean("pain"))Sql+="rbc,rbc_index,";
        if(jo.get("mobility")!=null&&jo.getBoolean("mobility"))Sql+="wbc,wbc_index,";
        if(jo.get("tartar")!=null&&jo.getBoolean("tartar"))Sql+="plt,plt_index,";
        Sql+="date,toothNo from tooth_routine where date_format(date,'%Y-%m-%d') between \""+dateFrom+"\" and \""+dateTo+
                "\" and patientID="+patientID+";";
    //    System.out.println(Sql);
        stt = Conn.createStatement();
        ResultSet set=null;
        // 返回结果集
        //       System.out.println(Sql);
        set = stt.executeQuery(Sql);
        // 获取数据
        splitedMatrix painUp=new splitedMatrix(),painDown=new splitedMatrix(),
                mobilityUp=new splitedMatrix(),mobilityDown=new splitedMatrix(),
                tartarDown=new splitedMatrix(),tartarUp=new splitedMatrix();


        //     secret.getSecureQueryIndex()
        if(jo.get("pain")!=null&&jo.getBoolean("pain")){
            painDown=secret.getSecureQueryIndex(jo.getDouble("painDown"));
            painUp=secret.getSecureQueryIndex(jo.getDouble("painUp"));
        }
        if(jo.get("mobility")!=null&&jo.getBoolean("mobility")){
            mobilityDown=secret.getSecureQueryIndex(jo.getDouble("mobilityDown"));
            mobilityUp=secret.getSecureQueryIndex(jo.getDouble("mobilityUp"));
        }
        if(jo.get("tartar")!=null&&jo.getBoolean("tartar")){
            tartarDown=secret.getSecureQueryIndex(jo.getDouble("tartarDown"));
            tartarUp=secret.getSecureQueryIndex(jo.getDouble("tartarUp"));
        }


        // System.out.println(set.getString(1));
        while (set.next()) {

            toothTable tt=new toothTable();
            tt.incluStr=new Vector<>();
            tt.num=new Vector<>();
            int count=0;
            //      System.out.println(1111);
            if(jo.get("pain")!=null&&jo.getBoolean("pain")){
                //     System.out.println(111);
                Blob bb= set.getBlob(count+2);
                InputStream is=bb.getBinaryStream();                //获取二进制流对象
                BufferedInputStream bis=new BufferedInputStream(is);    //带缓冲区的流对象

                byte[] buff=new byte[(int) bb.length()];
                bis.read(buff, 0, buff.length);           //一次性全部读到buff中
                ObjectInputStream in=new ObjectInputStream(new ByteArrayInputStream(buff));
                splitedMatrix sMarix=(splitedMatrix)in.readObject();

                //System.out.println(new Query().Search(rbcDown,rbcUp,sMarix));
                if(new Query().Search(painDown,painUp,sMarix)==0){
                       tt.num.add(Double.parseDouble(AES.decrypt(set.getString(count+1))));
                //    bt.num.add(Double.parseDouble(set.getString(count+1)));
               //     System.out.println(set.getString(count+1));
                    count+=2;
                    tt.incluStr.add("pain");
                }
                else continue;
            }

            if(jo.get("mobility")!=null&&jo.getBoolean("mobility")){

                Blob bb= set.getBlob(count+2);
                InputStream is=bb.getBinaryStream();                //获取二进制流对象
                BufferedInputStream bis=new BufferedInputStream(is);    //带缓冲区的流对象

                byte[] buff=new byte[(int) bb.length()];
                bis.read(buff, 0, buff.length);           //一次性全部读到buff中
                ObjectInputStream in=new ObjectInputStream(new ByteArrayInputStream(buff));
                splitedMatrix sMarix=(splitedMatrix)in.readObject();

                //System.out.println(new Query().Search(rbcDown,rbcUp,sMarix));
                if(new Query().Search(mobilityDown,mobilityUp,sMarix)==0){
                      tt.num.add(Double.parseDouble(AES.decrypt(set.getString(count+1))));
                   // bt.num.add(Double.parseDouble(set.getString(count+1)));
                   // System.out.println(set.getString(count+1));
                    count+=2;
                    tt.incluStr.add("mobility");
                }
                else continue;
            }

            if(jo.get("tartar")!=null&&jo.getBoolean("tartar")){

                Blob bb= set.getBlob(count+2);
                InputStream is=bb.getBinaryStream();                //获取二进制流对象
                BufferedInputStream bis=new BufferedInputStream(is);    //带缓冲区的流对象

                byte[] buff=new byte[(int) bb.length()];
                bis.read(buff, 0, buff.length);           //一次性全部读到buff中
                ObjectInputStream in=new ObjectInputStream(new ByteArrayInputStream(buff));
                splitedMatrix sMarix=(splitedMatrix)in.readObject();


                if(new Query().Search(tartarDown,tartarUp,sMarix)==0){
                        tt.num.add(Double.parseDouble(AES.decrypt(set.getNString(count+1))));

                    count+=2;
                    tt.incluStr.add("tartar");
                }
                else continue;
            }
            tt.m_tableNum=set.getInt(count+2);
            tt.m_type="toothTable";
            tt.m_date=set.getDate(count+1).toString();
            JAL.add(jsonHelper.talbeToJson(tt));

        }
        return JAL;
    }

    public static ArrayList<JSONObject> search(JSONObject jo){//String tablename,SecretKey sk){
        Connection conn=null;
        ResultSet set=null;
        String tablename=jo.getString("tableName");

        try {
            // 获取连接
            Conn = getConnection();




            if(tablename.equals("blood")){

                return searchForBlood(jo);}
            if(tablename.equals("tooth"))return searchForTooth(jo);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            try {
                set.close();
                conn.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }

        }
        return null;
    }

    public static void Add(String tablename, int patientID,  String date, Vector<String> num,
                           Vector<splitedMatrix> dataIndex){
        Connection conn = DBH.getConnection();
        try {
            //获取连接
        //    System.out.println(patientID);
            if(conn==null)
                return;

            String sql = "insert into ";

            if(tablename=="bloodTable"){
               sql+= "blood_routine(patientID,date,rbc,rbc_index,wbc,wbc_index,plt,plt_index)"
                       +" VALUES(?,?,?,?,?,?,?,?);";
            }
            else if(tablename=="toothTable"){
                sql+= "tooth_routine(patientID,date,pain,pain_index,mobility,mobility_index,tartar,tartar_index)"
                        +" VALUES(?,?,?,?,?,?,?,?);";
            }
         //   System.out.println(sql);
            //获取用户输入的账号和密码
            //定义sql语句
        //    String sql = "insert into "+tablename+" VALUES (?,?,?);";

            //获取Statement对象
            PreparedStatement stt = conn.prepareStatement(sql);
            //执行sql语句

            stt.setInt(1,patientID);
            stt.setDate(2, Date.valueOf(date));
          //  stt.setDate(3,sql.);
        //    System.out.println(num);
            for(int i=0;i<num.size();++i){
                stt.setString(3+i*2,num.elementAt(i));
                stt.setObject(3+i*2+1,dataIndex.elementAt(i));
            }

       //     System.out.println(stt.toString());
            stt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //释放资源
            try {

                conn.close();

            } catch (Exception e2) {}

        }


    }

}
