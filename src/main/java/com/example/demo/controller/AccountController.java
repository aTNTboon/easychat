package com.example.demo.controller;

import com.example.demo.model.Dto.LoginReqDTO;
import com.example.demo.model.Dto.UserRespDTO;
import com.example.demo.model.Dto.RegisterReqDTO;
import com.example.demo.model.R;
import com.example.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final AccountService accountService;


    @PostMapping("/login")
    public R<UserRespDTO> login(@RequestBody LoginReqDTO loginReqDTO) {
        UserRespDTO UserRespDTO = accountService.login(loginReqDTO);
        return R.success(UserRespDTO);
    }
    @PostMapping("/register")
    public R<String> register(@RequestBody RegisterReqDTO registerReqDTO) {
        accountService.register(registerReqDTO);
        return R.success("注册成功");
    }
}
