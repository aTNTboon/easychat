package com.example.demo.model.Enum;

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
        for (JoinTypeEnum value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }
}