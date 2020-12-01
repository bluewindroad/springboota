package com;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
import com.table.*;

public class jsonHelper {
    //����ִ��json���ͺ��������ת��
    public static String request(JSONObject jo)
    {
        //����������json����������serverlet����
      //  JsonParser jp=new JsonParser();
        String str=new String();
        return str;
    }

    public  static JSONObject TtalbeToJson(toothTable tt)
    {
        //�����ݱ�ת��Ϊjson�ļ�
        JSONObject jo=new JSONObject();
        jo.put("office","口腔科");
        jo.put("tableId",tt.m_tableNum);
        jo.put("date",tt.m_date);

        for(int i=0;i<tt.incluStr.size();++i)
            jo.put(tt.incluStr.elementAt(i),tt.num.elementAt(i));
        return jo;
    }

    public  static JSONObject BtalbeToJson(bloodTable bt)
    {


        JSONObject jo=new JSONObject();
        jo.put("office","血液科");
        jo.put("tableId",bt.m_tableNum);
        jo.put("date",bt.m_date);

        for(int i=0;i<bt.incluStr.size();++i)
            jo.put(bt.incluStr.elementAt(i),bt.num.elementAt(i));
        // jo.addProperty();
        return jo;
    }
    public  static JSONObject JsonToJson(JSONObject jo){

        JSONObject joNew=new JSONObject();
        joNew.put("tableName",jo.getString("tableName"));
        joNew.put("patientId",jo.getString("patientId"));
        joNew.put("startDate",jo.getString("startDate").split("T")[0]);
        joNew.put("endDate",jo.getString("endDate").split("T")[0]);

        JSONArray joA=jo.getJSONArray("value");

        for(int i=0;i<joA.size();++i){
            joNew.put(joA.getJSONObject(i).getString("values"),true);//ԭɸѡ��Ŀ
            joNew.put(joA.getJSONObject(i).getString("values")+"Down",joA.getJSONObject(i).getDoubleValue("down"));
            joNew.put(joA.getJSONObject(i).getString("values")+"Up",joA.getJSONObject(i).getDoubleValue("up"));
        }

        return joNew;
    }


    public  static JSONObject TestJson()
    {
        //��ѪҺ��ת��Ϊjson�ļ�
        JSONObject jo=new JSONObject();
        jo.put("tableName","bloodTable");
        jo.put("patientId",11);
        //     jo.addProperty("����","2019-01-02");
        jo.put("rbc",true);
        jo.put("wbc",true);
        //   jo.addProperty("ѪС��",false);
        jo.put("startDate","2019-03-05");
        jo.put("endDate","2020-06-08");
        jo.put("rbcDown",15);
        jo.put("rbcUp",500);
        jo.put("wbcDown",15);
        jo.put("wbcUp",500);
        // jo.addProperty();

        return jo;
    }

    public  static JSONObject TestJson2()
    {
        //��ѪҺ��ת��Ϊjson�ļ�
        JSONObject jo=new JSONObject();
        jo.put("talbeName","bloodTable");
        jo.put("office","blood");
        jo.put("tableId",11);
        //     jo.addProperty("����","2019-01-02");
        jo.put("startDate","2019-03-05T06:09:49.618Z");
        jo.put("endDate","2020-06-08T06:09:49.618Z");
//        jo.addProperty("��������","2020-06-08");
        jo.put("rbc",15);
        jo.put("wbc",15);
        // jo.addProperty();
        return jo;
    }


    public  static JSONObject talbeToJson(fatherTable ft)
    {
        //��ϵͳ�еı�ת��Ϊjson�ļ�
        JSONObject jo=new JSONObject();
        if(ft.getM_type().equals("bloodTable"))return BtalbeToJson((bloodTable) ft);
        if(ft.getM_type().equals("toothTable"))return TtalbeToJson((toothTable) ft);
      //  if(((bloodTable)ft).m_type=="bloodTable")return BtalbeToJson((bloodTable) ft);
       // if(((toothTable)ft).m_type=="toothTable")return TtalbeToJson((toothTable) ft);
        return jo;
    }

}