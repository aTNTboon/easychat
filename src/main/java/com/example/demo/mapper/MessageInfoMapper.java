package com.example.demo.mapper;

import com.example.demo.model.MessageInfo;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface MessageInfoMapper {

    /**
     * 插入一条消息记录
     * @param message 消息对象（id 自增，created_at 由数据库自动填充）
     * @return 影响行数
     */
    @Insert("INSERT INTO message_info (sender_id, receiver_id, message_send_type, type, status, data, date,msg_id) " +
            "VALUES (#{senderId}, #{receiverId}, #{messageSendType}, #{type}, " +
            "COALESCE(#{status}, 0), #{data}, #{date},#{msgId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(MessageInfo message);

    /**
     * 根据接收者ID查询消息（分页）
     * @param receiverId 接收者ID
     * @param offset     偏移量
     * @param limit      每页大小
     * @return 消息列表
     */
    @Select("SELECT id, sender_id, receiver_id, message_send_type, type, status, data, date, created_at,msg_id " +
            "FROM message_info WHERE receiver_id = #{receiverId} " +
            "ORDER BY date DESC LIMIT #{offset}, #{limit}")
    List<MessageInfo> selectByReceiverId(@Param("receiverId") String receiverId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

    /**
     * 根据ID查询消息
     */
    @Select("SELECT id, sender_id, receiver_id, message_send_type, type, status, data, date, created_at,msg_id " +
            "FROM message_info WHERE id = #{id}")
    MessageInfo selectById(@Param("id") Long id);

    /**
     * 更新消息状态
     * @param id     消息ID
     * @param status 新状态
     * @return 影响行数
     */
    @Update("UPDATE message_info SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);



    /**
     * 根据接收者ID和时间范围查询消息（示例）
     */
    @Select("SELECT id, sender_id, receiver_id, message_send_type, type, status, data, date, created_at,msg_id " +
            "FROM message_info WHERE receiver_id = #{receiverId} " +
            "AND date BETWEEN #{startTime} AND #{endTime} ORDER BY date ASC")
    List<MessageInfo> selectByReceiverIdAndTimeRange(@Param("receiverId") String receiverId,
                                                     @Param("startTime") Date startTime,
                                                     @Param("endTime") Date endTime);

    /**
     * 软删除（将状态改为3）
     */
    @Update("UPDATE message_info SET status = 3 WHERE id = #{id}")
    int softDelete(@Param("id") Long id);

    /**
     * 根据接收者ID查询某个时间点之后的所有消息（包含该时间点）
     * @param receiverId 接收者ID
     * @param startTime  起始时间（包含）
     * @return 消息列表，按时间升序
     */
    @Select("SELECT id, sender_id, receiver_id, message_send_type, type, status, data, date, created_at,msg_id " +
            "FROM message_info WHERE receiver_id = #{receiverId} " +
            "AND date >= #{startTime} AND status=1 ORDER BY date ASC")
    List<MessageInfo> selectByReceiverIdAfterTime(@Param("receiverId") String receiverId,
                                                  @Param("startTime") Date startTime);




    int batchUpdateStatusToSended(@Param("list") List<String> list);

    int batchUpdateStatusToAccecped(@Param("list") List<String> list);
}