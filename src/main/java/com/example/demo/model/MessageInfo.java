package com.example.demo.model;

import lombok.Data;

import java.util.Date;
@Data
public class MessageInfo {
    private Long id;
    private String senderId;
    private String receiverId;
    private Integer messageSendType;  // 0 私聊, 1 群组
    private Integer type;              // 0文本 1图片 2文件 3系统 4状态
    private Integer status;            // 0正常 1已读 2撤回 3删除
    private String data;                // JSON格式的消息内容
    private Date date;                  // 消息时间
    private Date createdAt;              // 创建时间
    private String msgId;
    // getters and setters (略，请自行生成)
}