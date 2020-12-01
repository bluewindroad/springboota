package com;
import com.encrypty.*;
import com.table.bloodTable;
import com.table.fatherTable;
import com.table.toothTable;

import java.util.Vector;

public class secret {
    //公用密码
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
        // 生成明文索引
        IndexEncrypty indexEncrypty = new IndexEncrypty();
        double[] index = indexEncrypty.BuildIndexVector(data, sk);

        // 索引加密
        splitedMatrix splitedMatrix = indexEncrypty.splitIndexVector(index, sk);
        secureDataIndex = indexEncrypty.EncIndexVector(splitedMatrix, sk);

        return secureDataIndex;
    }

    /**
     * 获取安全查询索引
     *
     * @param data 查询边界值
     *
     * @return 密钥
     */
    public static splitedMatrix getSecureQueryIndex(double data) {
        splitedMatrix secureQueryIndex = null;

        SecretKey sk=getKey(); //Gen.GenKey(64);
        // 生成明文索引
        QueryEncrypty queryEncrypty = new QueryEncrypty();
        double[] index = queryEncrypty.BuildQueryVector(data, sk);

        // 索引加密
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
        //对bloodtalbe中的数据行加密，返回加密后的bt
        for(int i=0;i<bt.num.size();++i){

            bt.AESString.add(AES.encrypt(bt.num.elementAt(i)));
            bt.dataIndex.add(getSecureDataIndex(bt.num.elementAt(i)));

        }
        return  bt;
    }


    private static toothTable SecretForTooth(toothTable tt)
    {
        //对toothtalbe中的数据行加密，返回加密后的bt
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
        //分类对ft中的数据进行加密，返回加密后的ft
        if(((bloodTable)ft).m_type=="bloodTable")ft=SecretForBlood((bloodTable) ft);

        if(ft.m_type=="toothTable")ft=SecretForTooth((toothTable) ft);

        return  ft;
    }


}
