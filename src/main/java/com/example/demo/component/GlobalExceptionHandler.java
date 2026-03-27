package com.example.demo.component;

import com.example.demo.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Object handleRuntimeException(RuntimeException ex) {
        log.error("运行时异常: ", ex);
            return new R(400,ex.getMessage(), ex.getMessage());
    }
}