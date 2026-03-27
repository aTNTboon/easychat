package com.example.demo.mapper;

import com.example.demo.model.BlockList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface BlockListMapper {
    BlockList selectById(Long id);

    int insert(BlockList blockList);

    int update(BlockList blockList);

    int delete(Long id);

    List<BlockList> selectAllByUserId(Long userId);
}