package com;
import com.encrypty.*;
import com.table.bloodTable;
import com.table.fatherTable;
import com.table.toothTable;

import java.util.Vector;

public class secret {
    //��������
    SecretKey sk=Gen.GenKey(64);
    public static SecretKey getKey(){
        FileHelper fi=new FileHelper("E:\\programming\\java\\demoa\\src\\secretKey");
        SecretKey sk=fi.getObjFromFile();
        //     System.out.println(sk.toString());
        if(sk!=null)return sk;
      //  System.out.println(1111111222);
        sk=Gen.GenKey(64);
        fi.saveObjToFile(sk);
        return sk;
    }

    public static splitedMatrix getSecureDataIndex(double data) {
        splitedMatrix secureDataIndex = null;
        SecretKey sk=getKey();//= Gen.GenKey(64);
        // ������������
        IndexEncrypty indexEncrypty = new IndexEncrypty();
        double[] index = indexEncrypty.BuildIndexVector(data, sk);

        // ��������
        splitedMatrix splitedMatrix = indexEncrypty.splitIndexVector(index, sk);
        secureDataIndex = indexEncrypty.EncIndexVector(splitedMatrix, sk);

        return secureDataIndex;
    }

    /**
     * ��ȡ��ȫ��ѯ����
     *
     * @param data ��ѯ�߽�ֵ
     *
     * @return ��Կ
     */
    public static splitedMatrix getSecureQueryIndex(double data) {
        splitedMatrix secureQueryIndex = null;

        SecretKey sk=getKey(); //Gen.GenKey(64);
        // ������������
        QueryEncrypty queryEncrypty = new QueryEncrypty();
        double[] index = queryEncrypty.BuildQueryVector(data, sk);

        // ��������
        splitedMatrix splitedMatrix = queryEncrypty.splitQueryVector(index, sk);
        secureQueryIndex = queryEncrypty.EncQueryVector(splitedMatrix, sk);

        return secureQueryIndex;
    }

    private static bloodTable SecretForBlood(bloodTable bt)
    {
    //    System.out.println("111111111");
        bt.AESString=new Vector<>();
        bt.dataIndex=new Vector<>();
        //   System.out.println("1111");
        //��bloodtalbe�е������м��ܣ����ؼ��ܺ��bt
        for(int i=0;i<bt.num.size();++i){

            bt.AESString.add(AES.encrypt(bt.num.elementAt(i)));
            bt.dataIndex.add(getSecureDataIndex(bt.num.elementAt(i)));

        }
        return  bt;
    }


    private static toothTable SecretForTooth(toothTable tt)
    {
        //��toothtalbe�е������м��ܣ����ؼ��ܺ��bt
        tt.AESString=new Vector<>();
        tt.dataIndex=new Vector<>();
        for(int i=0;i<tt.num.size();++i){
            tt.AESString.add(AES.encrypt(tt.num.elementAt(i)));
            tt.dataIndex.add(getSecureDataIndex(tt.num.elementAt(i)));
        }
        return  tt;
    }

    public static fatherTable Secret(fatherTable ft)
    {
        //�����ft�е����ݽ��м��ܣ����ؼ��ܺ��ft
        if(ft.getM_type().equals("toothTable"))ft=SecretForTooth((toothTable) ft);;
        if(ft.getM_type().equals("bloodTable"))ft=SecretForBlood((bloodTable) ft);;
   //     if(((toothTable)ft).m_type=="toothTable")ft=SecretForTooth((toothTable) ft);
//        if(((bloodTable)ft).m_type=="bloodTable")ft=SecretForBlood((bloodTable) ft);



        return  ft;
    }


}
