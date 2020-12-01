package com.jbc.demoa.util;
import com.*;
import com.table.bloodTable;
import com.table.fatherTable;

import java.io.IOException;
import java.util.Vector;

public class test {
    public static void main(String[] args) throws IOException {
        //    secret.getSecureDataIndex(2.0);
        //  secret.getSecureDataIndex(2.0);
        //  System.out.println(secret.getSecureDataIndex(2.0));
        // System.out.println(secret.getSecureDataIndex(2.0));

        bloodTable bt=new bloodTable();
       // String s=AES.encrypt(2.0);
       // System.out.println(AES.decrypt(s));
        Vector<fatherTable> ftV= csv.csvRead("C:\\Users\\78240\\Desktop\\test.csv","ѪҺ��");
        //  bloodTable bt=new bloodTable();
     //  System.out.println(1111);
        //System.out.println(DBH.search(jsonHelper.TestJson()));
//        DBH.search(jsonHelper.JsonToJson())
//          System.out.println(DBH.search(jsonHelper.JsonToJson()));
         // System.out.println(jsonHelper.TestJson());
        //    System.out.println(jsonHelper.BtalbeToJson(bt));
        //  com.DBH.search();

        //   System.out.println(ft.elementAt(0).m_name);
        //  System.out.println(jsonHelper.talbeToJson(ft.elementAt(1)));
        //��ȡ��Կ
        //     SecretKey sk = Gen.GenKey(64);
        //   System.out.println(sk);
        //     double data = -113.0;
        //    double queryLeft = 2.0;
        //  double queryRight = 4.0;
        // String str= AES.encrypt(data);
        //    System.out.println(str+" "+com.AES.decrypt(str));
        // ��������
        //  splitedMatrix dataIndex1 = getSecureDataIndex(data, sk);

        //     ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("test"));

        // com.DBH.Add("bloodTalbe",str,dataIndex1);
        //    DBH.search("bloodTable",sk);


        //   splitedMatrix queryIndexLeft = getSecureQueryIndex(queryLeft, sk);
        // splitedMatrix queryIndexRight = getSecureQueryIndex(queryRight, sk);

        // ��ѯ ���Ϊ0�����㷶Χ��ѯ����
        // �����䷶Χ��ѯ
        //   System.out.println(""+dataIndex1.toString());
        //     System.out.println(new Query().Search(queryIndexLeft, queryIndexRight, dataIndex1));


    }



}


