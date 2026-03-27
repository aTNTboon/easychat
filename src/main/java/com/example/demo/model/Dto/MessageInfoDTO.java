package com.example.demo.model.Dto;

import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 消息传输对象
 */
@Data
public class MessageInfoDTO {
    /**
     * 消息ID
     */
    private String id;

    /**
     * 发送者ID
     */
    private String senderId;

    /**
     * 接收者ID
     */
    private String receiverId;

    private Integer messageSendType;
    private String msgId;
    /**
     * 消息类型（数字标识）
     * 0: 文本消息
     * 1: 图片消息
     * 2: 文件消息
     * 3: 系统通知
     * 4: 状态更新
     * 后续可扩展
     */
    private Integer type;

    /**
     * 消息内容
     */
    private Object data;

    /**
     * 消息时间戳（毫秒）
     */
    private Long timestamp;

    /**
     * 消息状态
     * 0: 未发送
     * 1: 已发送
     * 2: 已送达
     * 3: 已读
     */
    private Integer status;

    /**
     * 额外扩展字段
     */
    private Object extra;

    // 无参构造
}