package com.jbc.demoa.controller;

import com.alibaba.fastjson.JSONObject;
import com.jbc.demoa.mapper.UserMapper;
import com.jbc.demoa.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            case 2:   //医生
                check = userMapper.checkDoctorAccount(phone, password);
                if (check == 1) {
                    isLogin = true;
                }
                break;
            case 3:  //病人
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
}
