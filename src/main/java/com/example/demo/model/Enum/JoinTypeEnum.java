package com.example.demo.model.Enum;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 消息类型枚举
 */
public enum JoinTypeEnum {
    /**
     * 开放加入
     */
    OPEN(0, "开放加入"),

    /**
     * 需要密码
     */
    PASSWORD(1, "需要密码");

    private final int code;
    private final String desc;
    private static final Map<Integer, JoinTypeEnum> CODE_MAP = Arrays.stream(values())
            .collect(Collectors.toUnmodifiableMap(JoinTypeEnum::getCode, Function.identity()));

    JoinTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据code获取枚举
     */
    public static JoinTypeEnum getByCode(int code) {
        return CODE_MAP.get(code);
    }
}
