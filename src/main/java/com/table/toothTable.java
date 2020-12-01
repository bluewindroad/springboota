package com.table;

import com.encrypty.splitedMatrix;

import java.util.Vector;

public class toothTable extends fatherTable{
    public String m_type,m_name,m_date;
    public int m_fieldNum,m_patientID,m_tableNum;
    public Vector<Double> num;
    public Vector<splitedMatrix> dataIndex;
    public Vector<String> AESString;
    public Vector<String> incluStr;
    public String getM_type(){
        return m_type;
    }
}
