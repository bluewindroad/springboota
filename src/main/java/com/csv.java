package com;
import com.csvreader.CsvReader;
import  com.table.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Vector;
import java.io.*;
public class csv {
   // static int count=0;
    //用于读取/处理csv
    private static Vector<fatherTable>ReadForBlood(CsvReader reader) throws IOException {
        //从csv中生成bloodtable对象
        //System.out.println("1111");
    //    System.out.println(csvReader.getValues());
        Vector<fatherTable> ftVec=new Vector<>(3);
      //  System.out.println(csvReader.readRecord());
        String inString = "";
        String tmpString = "";
        ArrayList<String []> List = new ArrayList<String[]>();

        reader.readHeaders();
        while(reader.readRecord()) {
            List.add(reader.getValues());
        }
        reader.close();
        for (int row = 0;row < List.size(); row++) {
            int Length=List.get(row).length;
            bloodTable bt=new bloodTable();
            if(Length > 0){
               //for(int i=0;i<Length;i++){
                bt.m_type="bloodTable";
                    bt.m_patientID=Integer.parseInt((List.get(row)[0]));
                    bt.m_date=List.get(row)[1];

                 //  System.out.println(List.get(row)[2]);
                   bt.num=new Vector<>();
                    bt.num.add(Double.parseDouble(List.get(row)[2]));
                    bt.num.add(Double.parseDouble(List.get(row)[3]));
                      bt.num.add(Double.parseDouble(List.get(row)[4]));
              //      System.out.println(bt.m_name);
                    ftVec.add(bt);

            }//if
        //    System.out.println("");
        //fo
        }
        bloodTable bt=new bloodTable();

        for(int i=0;i<ftVec.size();++i){
            bt= (bloodTable) secret.Secret(ftVec.elementAt(i));
           // System.out.println(111);
            //     System.out.println(bt.num);
            DBH.Add("bloodTable",bt.m_patientID,bt.m_date,bt.AESString,bt.dataIndex);
        }
        return  ftVec;
    }


    private static Vector<fatherTable>ReadForTooth(CsvReader reader) throws IOException {
        //从csv中生成toothtable对象
        Vector<fatherTable> ftVec=new Vector<>(3);
        ArrayList<String []> List = new ArrayList<String[]>();

        reader.readHeaders();
        while(reader.readRecord()) {
            List.add(reader.getValues());
        }
        reader.close();
        for (int row = 0;row < List.size(); row++) {
            int Length=List.get(row).length;
            toothTable tt=new toothTable();
            if(Length > 0){
                //for(int i=0;i<Length;i++){
                tt.m_type="toothTable";
                tt.m_patientID=Integer.parseInt((List.get(row)[0]));
                tt.m_date=List.get(row)[1];

                //  System.out.println(List.get(row)[2]);
                tt.num=new Vector<>();
                tt.num.add(Double.parseDouble(List.get(row)[2]));
                tt.num.add(Double.parseDouble(List.get(row)[3]));
                tt.num.add(Double.parseDouble(List.get(row)[4]));
                //      System.out.println(bt.m_name);
                ftVec.add(tt);
               // bloodTable bt2=(bloodTable) ftVec.elementAt(0);
                //     System.out.println(bt2.m_date);
                //     System.out.println(bt2.m_date);
                //    System.out.println(((bloodTable)ftVec.elementAt(0)).m_name);
                //   System.out.print(List.get(row)[i]+",");
                // }//for

            }//if
            //    System.out.println("");
            //fo
        }


        return  ftVec;
    }


    public static Vector<fatherTable> csvRead(String filename, String type){
        //从选定的csv文件中读取数据，生成fatherTalbe的子类对象，存储信息
        Vector<fatherTable> ftVec=new Vector<>(3);
      //  bloodTable bt=new bloodTable();
       // ftVec.add(bt);

     //   fatherTable ft = new fatherTable();
        try {
            File fileIN= new File(filename);
            BufferedReader reader=new BufferedReader(new FileReader(fileIN));
      //      System.out.println(reader.toString(
            CsvReader csvReader = new CsvReader(reader);
          //  CsvReader csvReader2 = new CsvReader(filename,',', Charset.forName("GBK"));
      //      csvReader.readHeaders();


            if (type == "bloodTable") {
                ftVec = ReadForBlood(csvReader);
          //      System.out.println("1111");
              //  ftVec = ReadForBlood(csvReader2);
           //     System.out.println(111);
                bloodTable bt=new bloodTable();
                for(int i=0;i<ftVec.size();++i){
                    bt= (bloodTable) secret.Secret(ftVec.elementAt(i));
                     //    System.out.println(bt.num);
                    DBH.Add("bloodTable",bt.m_patientID,bt.m_date,bt.AESString,bt.dataIndex);
                }
            }
            if (type == "toothTable") {
                ftVec = ReadForTooth(csvReader);
                toothTable tt=new toothTable();
                for(int i=0;i<ftVec.size();++i){
                    tt= (toothTable) com.secret.Secret(ftVec.elementAt(i));
                    //     System.out.println(bt.num);
                    com.DBH.Add("toothTable",tt.m_patientID,tt.m_date,tt.AESString,tt.dataIndex);
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
   //     System.out.println(ftVec);

        return  ftVec;
    }
}
