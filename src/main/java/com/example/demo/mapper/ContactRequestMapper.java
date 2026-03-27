package com.example.demo.mapper;

import com.example.demo.model.ContactRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ContactRequestMapper {
    ContactRequest selectById(Long id);

    int insert(ContactRequest contactRequest);

    int update(ContactRequest contactRequest);

    int delete(Long id);


    List<ContactRequest> selectByFromUserId(Long fromUserId);
    @Select("select id from contact_requests where from_user_id = #{fromUserId} and  to_user_id=#{toUserId}")
    Long selectIdsByFromUserId(Long fromUserId,Long toUserId);


    List<ContactRequest> selectByToUserId(Long toUserId);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    int updateByFromAndToUserId(ContactRequest contactRequest);
}