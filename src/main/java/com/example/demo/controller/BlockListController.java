package com.example.demo.controller;

import com.example.demo.model.BlockList;
import com.example.demo.model.Dto.BlockListDTO;
import com.example.demo.model.R;
import com.example.demo.service.BlockListService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/block-list")
public class BlockListController {
    private final BlockListService blockListService;

    public BlockListController(BlockListService blockListService) {
        this.blockListService = blockListService;
    }

    @GetMapping("/{id}")
    public R<BlockList> getBlockList(@PathVariable Long id) {
        return R.success(blockListService.getBlockListById(id));
    }

    @PostMapping
    public R<Long> addBlockList(@RequestBody BlockListDTO blockListDTO) {
        return R.success(blockListService.createBlockList(blockListDTO));
    }
//    @PutMapping
//    public R<Void> updateBlockList(@RequestBody BlockListDTO blockListDTO) {
//        blockListService.updateBlockList(blockListDTO);
//        return R.success();
//    }

//    @DeleteMapping("/{id}")
//    public R<Void> deleteBlockList(@PathVariable Long id) {
//        blockListService.deleteBlockList(id);
//        return R.success();
//    }

    @GetMapping("/user/{userId}")
    public R<List<BlockList>> getAllBlockListsByUser(@PathVariable Long userId) {
        return R.success(blockListService.getAllBlockListsByUserId(userId));
    }
}