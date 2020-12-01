package com.jbc.demoa.controller;

import com.DBH;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.jbc.demoa.mapper.UserMapper;
import com.jbc.demoa.pojo.*;
import com.jsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @CrossOrigin
    @GetMapping("/getAdmin")  //测试，获取管理员列表
    public List<admin_account> getAllAdminList() {
        List<admin_account> admin_accounts = userMapper.getAllAdminList();
        for (admin_account adminAccount : admin_accounts) {
            System.out.println(admin_accounts);

        }
        return admin_accounts;
    }

    @CrossOrigin
    @GetMapping("/getAllDocAndPatient")  //获取所有医生和病人账号列表
    public List<Map<Object, Object>> getAllDocAndPatient() {
        List<Map<Object, Object>> mapList = new ArrayList<>();
        mapList.addAll(userMapper.getAllDocAccount());
        mapList.addAll(userMapper.getAllPatientAccount());
        return mapList;
    }

    @CrossOrigin  //删除医生或者病人
    @RequestMapping(value = "/deleteDocOrPatient", method = RequestMethod.POST, consumes = "application/json")
    public Boolean deleteDocOrPatient(@RequestBody String jsonParamStr){
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String phone = jsonObject.getString("phone");
        String tag = jsonObject.getString("tag");
        if (tag.equals("病患")) {
            userMapper.deletePatientAccountByPhone(phone);
            userMapper.deletePatientByPhone(phone);
            return true;
        } else if (tag.equals("医师")) {
            userMapper.deleteDocAccountByPhone(phone);
            userMapper.deleteDocByPhone(phone);
            return true;
        }
        return false;
    }

    @CrossOrigin
    //获取医生或病人详细信息
    @RequestMapping(value = "/getInformationDocOrPatient", method = RequestMethod.POST, consumes = "application/json")
    public Map<Object, Object> getInformationDocOrPatient(@RequestBody String jsonParamStr) {
        Map<Object, Object> map = new HashMap<Object, Object>();
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String phone = jsonObject.getString("phone");
        String tag = jsonObject.getString("tag");
        if (tag.equals("病患")) {
            map = userMapper.getPatientDetailInformationByPhone(phone);
        } else if (tag.equals("医师")) {
            map = userMapper.getDocDetailInformationByPhone(phone);
        }
        return map;
    }

    @CrossOrigin
    @RequestMapping(value = "/addUser", method = RequestMethod.POST, consumes = "application/json")
    public String addUser(@RequestBody String jsonParamStr) {
        boolean isRegister = false;
        int check_phone = -1;
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        String phone = jsonObject.getString("phone");
        int value = Integer.parseInt(jsonObject.getString("value"));
        switch (value) {
            case 1:  //管理员
                if (userMapper.checkAdminPhone(phone) == 1) {
                    return "手机号已被注册!";
                }
                userMapper.addAdmin(new admin_account(username, password, phone));
                isRegister = true;
                break;
            case 2:   //医生
                if (userMapper.checkDoctorAccountPhone(phone) == 1) {
                    return "手机号已被注册!";
                }
                userMapper.addDocAccount(new doc_account(username, password, phone));
                int DocId = userMapper.getDocIdByPhoneNo(phone);
                userMapper.addDocBase(new doctor(DocId, phone));
                isRegister = true;
                break;
            case 3: //病人
                if (userMapper.checkPatientAccountPhone(phone) == 1) {
                    return "手机号已被注册!";
                }
                userMapper.addPatientAccount(new patient_account(username, password, phone));
                userMapper.addPatient(new patient(username, phone));
                int patientId = userMapper.getPatientIdByPhone(phone);
                int patientAccountNo = userMapper.getPatientAccountNoByPhone(phone);
                userMapper.updatePatientAccountPatientIdByPhone(phone, patientId);
                userMapper.updatePatientPatientAccountByPhone(phone, patientAccountNo); //这个的问题
                isRegister = true;
                break;
            default:
                break;
        }
        return String.valueOf(isRegister);
    }

    @CrossOrigin
    @RequestMapping(value = "/checkUser", method = RequestMethod.POST, consumes = "application/json")
    public Boolean checkUser(@RequestBody String jsonParamStr) {
        boolean isLogin = false;
        int check = -1;
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String phone = jsonObject.getString("phone");
        String password = jsonObject.getString("password");
        int value = Integer.parseInt(jsonObject.getString("value"));
        switch (value) {
            case 1:  //管理员
                check = userMapper.checkAdminAccount(phone, password);
                if (check == 1) {
                    isLogin = true;
                }
                break;
            case 3:   //医生
                check = userMapper.checkDoctorAccount(phone, password);
                if (check == 1) {
                    isLogin = true;
                }
                break;
            case 2:  //病人
                check = userMapper.checkPatientAccount(phone, password);
                if (check == 1) {
                    isLogin = true;
                }
                break;
            default:
                break;
        }
        return isLogin;
    }

//    之后是病人界面
    @CrossOrigin
    @GetMapping("/getAllDoc")  //获取所有医生账号列表
    public List<Map<Object, Object>> getAllDoc() {
        return new ArrayList<>(userMapper.getAllDoc());
    }


//  添加医师和开放所有权限
    @CrossOrigin
    @RequestMapping(value = "/addDoctorAndPermission", method = RequestMethod.POST, consumes = "application/json")
    public String addDoctorAndPermission(@RequestBody String jsonParamStr){
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String PatientPhone=jsonObject.getString("PatientPhone");
        JSONArray Doctor = jsonObject.getJSONArray("Doctor");
        for (int i = 0; i < Doctor.size(); i++) {
            String DoctorPhone= (String) Doctor.getJSONObject(i).get("tel");
            int DoctorId=userMapper.getDoctorIdByPhone(DoctorPhone);
            int PatientId=userMapper.getPatientIdByPhone(PatientPhone);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            userMapper.insertRelationship(PatientId,DoctorId,df.format(new Date()).toString());
            userMapper.insertRestriction(PatientId,DoctorId);
        }


        return "true";
    }

    //获取病人选择的医生的列表
    @CrossOrigin
    @RequestMapping(value = "/getRelationship", method = RequestMethod.POST, consumes = "application/json")
    public List<Object>getRelationship(@RequestBody String jsonParamStr){
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String PatientPhone=jsonObject.getString("PatientPhone");
        int PatientId=userMapper.getPatientIdByPhone(PatientPhone);
        List<Map<Object, Object>> mapList =new ArrayList<>();
        List<Object>DocList=new ArrayList<>();
        mapList=userMapper.getRelationshipByPatientId(PatientId);
        for (Map<Object, Object> objectObjectMap : mapList) {
            int DocoterId = (int) objectObjectMap.get("doctorID");
            DocList.add(userMapper.getDocListByDocId(DocoterId));
        }
        return DocList;

    }

    //修改病人选择的医生的权限
    @CrossOrigin
    @RequestMapping(value = "/updateRelationship", method = RequestMethod.POST, consumes = "application/json")
    public String updateRelationship(@RequestBody String jsonParamStr){
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String PatientPhone=jsonObject.getString("PatientPhone");
        int PatientId=userMapper.getPatientIdByPhone(PatientPhone);
        String DoctorPhone=jsonObject.getString("DoctorPhone");
        int DoctorId=userMapper.getDoctorIdByPhone(DoctorPhone);
        JSONArray checkList = jsonObject.getJSONArray("checkList");
        switch (checkList.size()){
            case 0:
                userMapper.updateRelationshipByPatientId(PatientId,DoctorId,0,0);
                break;
            case 1:
                if (checkList.get(0).equals("口腔科")){
                    userMapper.updateRelationshipByPatientId(PatientId,DoctorId,0,1);
                }
                if (checkList.get(0).equals("血液科")){
                    userMapper.updateRelationshipByPatientId(PatientId,DoctorId,1,0);
                }
                break;
        }
        return "true";
    }

    //删除病人所选择的医生
    @CrossOrigin
    @RequestMapping(value = "/deleteRelationship", method = RequestMethod.POST, consumes = "application/json")
    public String deleteRelationship(@RequestBody String jsonParamStr){
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String PatientPhone=jsonObject.getString("PatientPhone");
        int PatientId=userMapper.getPatientIdByPhone(PatientPhone);
        String DoctorPhone=jsonObject.getString("DoctorPhone");
        int DoctorId=userMapper.getDoctorIdByPhone(DoctorPhone);
        userMapper.deleteRelationshipByPatientId(PatientId,DoctorId);
        userMapper.deleteRestrictionByPatientId(PatientId,DoctorId);
        return "true";
    }


    //查询数据
    @CrossOrigin
    @RequestMapping(value = "/selectPatientCase", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8")
    public List<JSONObject>  selectPatientCase(@RequestBody String jsonParamStr){

        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        JSONArray value = jsonObject.getJSONArray("value");
//        System.out.println(jsonObject);
        int PatientId=userMapper.getPatientIdByPhone(jsonObject.getString("phone"));
        jsonObject.put("patientId",PatientId);
        return DBH.search(jsonHelper.JsonToJson(jsonObject));
    }


//    医生
    //医生个人界面
    @CrossOrigin
    @RequestMapping(value = "/getDocDetail", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8")
    public Map<Object,Object>getDocDetail(@RequestBody String jsonParamStr){
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        String DoctorPhone=jsonObject.getString("DoctorPhone");
        return userMapper.getDocDetailInformationByPhone(DoctorPhone);
    }

    //修改医生个人信息
    @CrossOrigin
    @RequestMapping(value = "/updateDocDetail", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8")
    public String updateDocDetail(@RequestBody String jsonParamStr){
        JSONObject jsonObject = JSONObject.parseObject(jsonParamStr);
        Map<String,Object>map=new HashMap<>();
        String DoctorPhone=jsonObject.getString("DoctorPhone");
        String sex=jsonObject.getString("sex");
        if (sex.length()==2){
            if (sex.equals("男性")){
                map.put("sex","f");
            }else if (sex.equals("女性")){
                map.put("sex","m");
            }
        }else {
            map.put("sex",jsonObject.getString("sex"));
        }
        map.put("phoneNo",DoctorPhone);
        map.put("birthday",jsonObject.getString("birthday").substring(0,10));
        map.put("nationality",jsonObject.getString("nationality"));
        map.put("nation",jsonObject.getString("nation"));
        map.put("college",jsonObject.getString("college"));
        map.put("address",jsonObject.getString("address"));
        map.put("expertise",jsonObject.getString("expertise"));
        map.put("works",jsonObject.getString("works"));
        map.put("introduction",jsonObject.getString("introduction"));
        map.put("achievements",jsonObject.getString("achievements"));
        map.put("evaluation",jsonObject.getString("evaluation"));
        map.put("nativePlace",jsonObject.getString("nativePlace"));
//        System.out.println("birthday:"+jsonObject.getString("birthday").substring(0,10));
        System.out.println("长度:"+jsonObject.getString("sex").length());
        userMapper.updateDocDetail(map);

        return "修改成功!";
    }
}
