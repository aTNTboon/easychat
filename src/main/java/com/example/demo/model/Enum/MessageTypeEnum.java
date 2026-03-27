package com.example.demo.model.Enum;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 消息类型枚举
 */
public enum MessageTypeEnum {
    /**
     * 个人消息
     */
    PRIVATE(0, "个人消息"),

    /**
     * 群组消息
     */
    GROUP(1, "群组消息");

    private final int code;
    private final String desc;
    private static final Map<Integer, MessageTypeEnum> CODE_MAP = Arrays.stream(values())
            .collect(Collectors.toUnmodifiableMap(MessageTypeEnum::getCode, Function.identity()));

    MessageTypeEnum(int code, String desc) {
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
    public static MessageTypeEnum getByCode(int code) {
        return CODE_MAP.get(code);
    }
}
