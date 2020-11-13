package com.jbc.demoa.controller;

import com.alibaba.fastjson.JSONObject;
import com.jbc.demoa.mapper.UserMapper;
import com.jbc.demoa.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @CrossOrigin
    @GetMapping("/getAdmin")
    public List<admin_account> getAllAdminList(){
        List<admin_account>admin_accounts= userMapper.getAllAdminList();
        for (admin_account adminAccount:admin_accounts) {
            System.out.println(admin_accounts);

        }
        return admin_accounts;
    }

    @CrossOrigin
    @RequestMapping(value = "/addUser",method = RequestMethod.POST,consumes = "application/json")
    public Boolean addUser(@RequestBody String jsonParamStr){
        Boolean isRegister=false;
        JSONObject jsonObject =  JSONObject.parseObject(jsonParamStr);
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        String phone = jsonObject.getString("phone");
        int value = Integer.parseInt(jsonObject.getString("value"));
        switch (value){
            case 1:  //管理员
                userMapper.addAdmin(new admin_account(username,password,phone));
                isRegister=true;
                break;
            case 2:   //医生
                userMapper.addDocAccount(new doc_account(username,password,phone));
                int DocId =  userMapper.getDocIdByPhoneNo(phone);
                userMapper.addDocBase(new doctor(DocId,phone));
                isRegister=true;
                break;
            case 3: //病人
                userMapper.addPatientAccount(new patient_account(username,password,phone));
                userMapper.addPatient(new patient(username,phone));
                int patientId = userMapper.getPatientIdByPhone(phone);
                int patientAccountNo =userMapper.getPatientAccountNoByPhone(phone);
                userMapper.updatePatientAccountPatientIdByPhone(phone,patientId);
                userMapper.updatePatientPatientAccountByPhone(phone,patientAccountNo); //这个的问题
                isRegister=true;
                break;
            default:
                break;
        }
        return isRegister;
    }

    @CrossOrigin
    @RequestMapping(value = "/checkUser",method = RequestMethod.POST,consumes = "application/json")
    public Boolean checkUser(@RequestBody String jsonParamStr){
        Boolean isLogin=false;
        int check=-1;
        JSONObject jsonObject =  JSONObject.parseObject(jsonParamStr);
        String phone = jsonObject.getString("phone");
        String password = jsonObject.getString("password");
        int value = Integer.parseInt(jsonObject.getString("value"));
        switch (value){
            case 1:  //管理员
                check=userMapper.checkAdminAccount(phone,password);
                System.out.println("check:"+check);
                if (check==1){
                    isLogin=true;
                }
                break;
            case 2:   //医生
                check=userMapper.checkDoctorAccount(phone,password);
                if (check==1){
                    isLogin=true;
                }
                break;
            case 3:  //病人
                check=userMapper.checkPatientAccount(phone,password);
                if (check==1){
                    isLogin=true;
                }
                break;
            default:
                break;
        }
        return isLogin;
    }
}
