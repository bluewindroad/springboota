package com.table;

import com.AES;
import com.encrypty.IndexEncrypty;
import com.encrypty.SecretKey;
import com.encrypty.splitedMatrix;

import java.util.Vector;

public class bloodTable extends fatherTable  {
    public String m_type,m_name,m_date;
    public int m_tableNum,m_patientID;
    public Vector<Double> num;
    public  Vector<String> incluStr;
    public  Vector<splitedMatrix> dataIndex;
    public  Vector<String> AESString;
    public String getM_type(){
        return m_type;
    }

    void addDBH()
    {
        Vector<String> nums = new Vector<String>(6);
        Vector<splitedMatrix> dataIndex= new Vector<splitedMatrix>(6);;

    }

}
