package com.jbc.demoa.mapper;

import com.jbc.demoa.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface UserMapper {
    List<admin_account> getAllAdminList();  //获取所有管理员列表

    admin_account getAdminByName(String username);

    void addAdmin(admin_account admin);

    List<doc_account> getAllDocList();     //获取所有医生列表

    void addDocAccount(doc_account docAccount);   //增加医生账户

    int getDocIdByPhoneNo(String phone);   //通过手机号查询医生账户的NO

    void addDocBase(doctor doctor); //增加医生个人信息

    doctor getDocByPhone(String phone);  //通过电话查询医生个人信息

    void addPatientAccount(patient_account patientAccount);  //新增病人账户

    void addPatient(patient patient);  //新增病人信息;

    int getPatientAccountNoByPhone(String phone); //通过电话查询病人账户编号

    int getPatientIdByPhone(String phone); //通过电话查询病人id

    void updatePatientAccountPatientIdByPhone(@Param("phone") String phone, @Param("patientId") int patientId);  //在病人账户中加入病人编号

    void updatePatientPatientAccountByPhone(@Param("phone") String phone, @Param("patientAccountNo") int patientAccountNo); //在病人中添加病人账户编号

    //接下来三条是通过电话和密码查询验证管理员、病人、医生账户信息是否存在，1表示存在，0表示不存在
    int checkAdminAccount(@Param("phone") String phone, @Param("password") String password);   //返回管理员账户编号

    int checkDoctorAccount(@Param("phone") String phone, @Param("password") String password);   //返回医生账户编号

    int checkPatientAccount(@Param("phone") String phone, @Param("password") String password);   //返回病人账户编号

    //接下来3条是验证手机号有没有被注册，1表示存在，0表示不存在
    int checkAdminPhone(@Param("phone") String phone);  //验证管理员手机号

    int checkDoctorAccountPhone(@Param("phone") String phone); //验证医生手机号

    int checkPatientAccountPhone(@Param("phone") String phone); //验证病人手机号

//    接下来2条是获取所有病人账号和医生账号的信息
    List<Map<Object,Object>> getAllPatientAccount();   //获取所有病人账号

    List<Map<Object,Object>>   getAllDocAccount();    //获取所有医生账号


}