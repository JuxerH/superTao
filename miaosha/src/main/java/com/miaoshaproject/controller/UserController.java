package com.miaoshaproject.controller;

import com.miaoshaproject.controller.viewobject.UserVO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.mysql.cj.util.Base64Decoder;
import com.mysql.cj.util.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;

import javax.servlet.http.HttpServletRequest;
import java.beans.Encoder;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

@Controller(value = "user")
@RequestMapping(value = "/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")//跨越全域
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping(value = "/login", method = {RequestMethod.POST},
            consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody//用户登录接口
public CommonReturnType login(@RequestParam(name = "telphone")String telphone,@RequestParam(name="password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if(org.apache.commons.lang3.StringUtils.isEmpty(telphone)|| StringUtils.isNullOrEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        UserModel userModel= userService.validateLogin(telphone,this.EncodeByMd5(password));
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);
        this.httpServletRequest.getSession().setAttribute("username",userModel.getName());
        this.httpServletRequest.getSession().setAttribute("userId",userModel.getId());
        return CommonReturnType.create(null);
    }
    @RequestMapping(value = "/register",
            method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody//用户注册接口
    @Transactional
    public CommonReturnType register(@RequestParam(name = "telphone") String telphone,
                                     @RequestParam(name = "otpCode") String otpCode,
                                     @RequestParam(name = "name") String name,
                                     @RequestParam(name = "gender") int gender,
                                     @RequestParam(name = "age") Integer age,
                                     @RequestParam(name = "password") String password,
                                     @RequestParam(name = "userType") int userType
                                     ) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telphone);
        if (!com.alibaba.druid.util.StringUtils.equals(otpCode, inSessionOtpCode)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "短信验证码不符合");
        }
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(gender);
        userModel.setAge(age);
        userModel.setTelphone(telphone);
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(this.EncodeByMd5(password));
        userModel.setUserType(userType);
        userService.register(userModel);
        return CommonReturnType.create(null);
    }
public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest md5=MessageDigest.getInstance("MD5");
    Base64.Encoder encoder = Base64.getEncoder();
    String newstr=encoder.encodeToString(md5.digest(str.getBytes("utf-8")));
    return newstr;
}
    @RequestMapping(value = "/getotp", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name = "telphone") String telphone) {
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        if (randomInt < 10000) {//保证生成5位数随机数
            randomInt += 10000;
        }
        String otpCode = String.valueOf(randomInt);
        httpServletRequest.getSession().setAttribute(telphone, otpCode);
        System.out.println("telphone=" + telphone + "&otpCode=" + otpCode);
        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        UserModel userModel = userService.getUserById(id);
        if (userModel == null) {

            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        UserVO userVO = convertFromModel(userModel);
        return CommonReturnType.create(userVO);
    }

    @RequestMapping(value = "/outlogin")
    @ResponseBody
    @Transactional
    public CommonReturnType logOut(){
        Enumeration em = httpServletRequest.getSession().getAttributeNames();
        while(em.hasMoreElements()){
            httpServletRequest.getSession().removeAttribute(em.nextElement().toString());
        }
        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/listAllUser")
    @ResponseBody
    public CommonReturnType listAllUser(){
        List<UserModel> userModelList=userService.listAllUser();
        return CommonReturnType.create(userModelList);
    }

    @RequestMapping(value = "/userType")
    @ResponseBody
    public CommonReturnType userType() throws BusinessException {
        if (httpServletRequest.getSession().getAttribute("userId")==null)return CommonReturnType.create(null);;
        Integer nowLoginId=Integer.valueOf(httpServletRequest.getSession().getAttribute("userId").toString());
        UserModel userModel=userService.getUserById(nowLoginId);
        return CommonReturnType.create(userModel.getUserType());
    }

    private UserVO convertFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }
    @RequestMapping(value = "/loginStatus")
    @ResponseBody
    public CommonReturnType loginStatus(){
        if(this.httpServletRequest.getSession().getAttribute("username")==null){
            return CommonReturnType.create("unlogin");
        }
        return CommonReturnType.create(this.httpServletRequest.getSession().getAttribute("username").toString());
    }

    @RequestMapping(value = "/deleteUser")
    @ResponseBody
    public CommonReturnType deleteUser(@RequestParam(name = "userId")Integer userId){
        return CommonReturnType.create(userService.deleteUserById(userId));
    }


    @RequestMapping(value = "/loginId")
    @ResponseBody
    public Integer loginId(){
        Integer userId=Integer.valueOf( this.httpServletRequest.getSession().getAttribute("userId").toString());
        return userId;
    }



}