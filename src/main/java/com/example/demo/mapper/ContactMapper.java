package com.example.demo.mapper;

import com.example.demo.model.Contact;
import com.example.demo.model.Dto.ContactDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ContactMapper {

    @Select("SELECT * FROM contacts  WHERE id = #{id}")
    Contact selectById(Long id);

    int insert(Contact contact);

    int update(Contact contact);

    int delete(Long id);


    @Select("""
SELECT 
  c.id,
  c.user_id,
  c.contact_id,
  u.email,
  u.nick_name
FROM contacts c
LEFT JOIN user_info u
  ON c.contact_id = u.user_id
WHERE c.user_id = #{userId}
""")
    List<ContactDTO> selectAllByUserId(Long userId);

    List<Contact> searchByNickname(@Param("userId") Long userId, @Param("nickname") String nickname);
    @Select("SELECT user_id FROM contacts WHERE contact_id = #{contactId} AND user_id = #{userId}")
    Long selectUserIdByContactIdAndUserId (Long contactId,Long userId);
}