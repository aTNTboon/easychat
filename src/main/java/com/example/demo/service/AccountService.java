package com.example.demo.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.RandomUtil;
import com.example.demo.model.Dto.LoginReqDTO;
import com.example.demo.model.Dto.UserRespDTO;
import com.example.demo.model.Dto.RegisterReqDTO;
import com.example.demo.model.R;
import com.example.demo.model.UserInfo;
import com.example.demo.util.IdComponent;
import com.example.demo.util.JwtUtil;
import com.example.demo.util.PasswordEncoderUtil;
import com.example.demo.util.RedisUtil;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {

    private final UserInfoService userInfoService;
    private final RedisUtil redisUtil;
    private final PasswordEncoderUtil passwordEncoderUtil;
    private final JwtUtil jwtUtil;




    public UserRespDTO login(LoginReqDTO loginReqDTO) {

         String rightCode= redisUtil.get(loginReqDTO.getCaptchaId()).toString();
         if(!rightCode.equals(loginReqDTO.getCaptchaCode())) {
             throw new RuntimeException("验证码错误");
         }

         UserInfo user = userInfoService.getUserByEmail(loginReqDTO.getEmail());
         if (user == null) {
             throw new RuntimeException("用户不存在");
         }
         if (user.getPassword()==null) {
             throw new RuntimeException("密码为空");
         }
        UserRespDTO userRespDTO=new UserRespDTO();
        UserInfo selectedUser= userInfoService.getUserByEmail(loginReqDTO.getEmail());
         if(passwordEncoderUtil.verifyPassword( loginReqDTO.getPassword(),selectedUser.getPassword())){
             log.info("登录成功{}",selectedUser.getEmail());
             userRespDTO.setToken(jwtUtil.generateToken(String.valueOf(user.getUserId())));
             userRespDTO.setEmail(user.getEmail());
             userRespDTO.setNickName(user.getNickName());
             userRespDTO.setUserId(user.getUserId());
             return userRespDTO;
         }else{
             throw new RuntimeException("邮箱或密码错误");
         }
    }

    private boolean checkCode(String captchaId, String captchaCode) {
        Object rightCode = redisUtil.get(captchaId);
        if (rightCode == null) {
            return false;
        }
        String code = (String) redisUtil.get(captchaId);
        code.equals(captchaCode);
        redisUtil.delete(captchaId);
        return true;
    }

    public UserRespDTO register(RegisterReqDTO registerReqDTO) {
        if(registerReqDTO.getCaptchaId()==null) {
            throw new RuntimeException("验证码ID为空");
        }
        if (!checkCode(registerReqDTO.getCaptchaId(), registerReqDTO.getCaptchaCode())) {
            throw new RuntimeException("验证码错误");
        }
        UserInfo existingUser = userInfoService.getUserByEmail(registerReqDTO.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("邮箱已被注册");
        }
        String encodedPassword = passwordEncoderUtil.encryptPassword(registerReqDTO.getPassword());
        UserInfo newUser = new UserInfo();
        newUser.setEmail(registerReqDTO.getEmail());
        newUser.setPassword(encodedPassword);
        newUser.setUserId(IdComponent.generateMeetingNo());  // 生成类似 "f47ac10b-58cc-4372-a567-0e02b2c3d479"
        if(newUser.getNickName()==null||newUser.getNickName()=="") {
            newUser.setNickName("用户"+ RandomUtil.randomString(6));
        }else{
            newUser.setNickName(registerReqDTO.getNickName());
        }

        userInfoService.saveUser(newUser);
        log.info("注册成功{}", newUser.getEmail());
        UserRespDTO respDTO = new UserRespDTO();
        return respDTO;
    }


}
