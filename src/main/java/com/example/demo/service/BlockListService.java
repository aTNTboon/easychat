package com.example.demo.service;

import com.example.demo.mapper.BlockListMapper;
import com.example.demo.model.BlockList;
import com.example.demo.model.Dto.BlockListDTO;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BlockListService {
    private final BlockListMapper blockListMapper;

    public BlockListService(BlockListMapper blockListMapper) {
        this.blockListMapper = blockListMapper;
    }

    public BlockList getBlockListById(Long id) {
        return blockListMapper.selectById(id);
    }

    public Long createBlockList(BlockListDTO blockListDTO) {
        BlockList blockList = new BlockList();

        blockList.setUserId(blockListDTO.getUserId());
        blockList.setBlockedId(blockListDTO.getBlockedId());
        blockList.setCreatedAt(LocalDateTime.now());
        blockList.setUpdatedAt(LocalDateTime.now());

        blockListMapper.insert(blockList);
        return blockList.getId();
    }

    public void updateBlockList(BlockListDTO blockListDTO) {
        BlockList blockList = new BlockList();
        blockList.setId(blockListDTO.getId());
        blockList.setUserId(blockListDTO.getUserId());
        blockList.setBlockedId(blockListDTO.getBlockedId());
        blockList.setUpdatedAt(LocalDateTime.now());

        blockListMapper.update(blockList);
    }

    public void deleteBlockList(Long id) {
        blockListMapper.delete(id);
    }

    public List<BlockList> getAllBlockListsByUserId(Long userId) {
        return blockListMapper.selectAllByUserId(userId);
    }
}