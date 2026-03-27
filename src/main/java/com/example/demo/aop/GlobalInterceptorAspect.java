package com.example.demo.aop;

import com.example.demo.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class GlobalInterceptorAspect {
    private final JwtUtil jwtUtil;

    /**
     * 从方法参数中获取指定字段的值
     * @param args 方法参数数组
     * @param fieldName 要获取的字段名
     * @param fieldType 字段类型
     * @return 字段值，未找到返回null
     */
    private <T> T getFieldValueFromArgs(Object[] args, String fieldName, Class<T> fieldType) {
        for (Object arg : args) {
            if (arg == null) continue;
            try {
                Field field = arg.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(arg);
                if (fieldType.isInstance(value)) {
                    return fieldType.cast(value);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                continue;
            }
        }
        return null;
    }
    @Before("@annotation(globalInterceptor)")
    public void before(JoinPoint joinPoint, GlobalInterceptor globalInterceptor) throws Throwable {

        if (!globalInterceptor.checkLogin()) {
            log.info("注解配置为不校验，跳过逻辑");
            return; // 这里 return 仅仅是不执行当前 AOP 校验，业务方法仍会继续
        }
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder
                        .getRequestAttributes()).getRequest();
        String token = (String) request.getAttribute("token");
        // 3. 校验逻辑
        if (token == null || token.isEmpty()) {
            log.error("filter异常");
            throw new RuntimeException("Token 不能为空，访问拒绝！");
        }
        if(!jwtUtil.validateToken(token)){
            throw new RuntimeException("Token过期 访问拒绝！");
        };
        log.info("用户{}调用方法{}", jwtUtil.getUserIdFromToken(token), joinPoint.getSignature().getName());
        }
        // 这里可以继续调用你的 jwtUtils.validateToken(token) ...

    }
