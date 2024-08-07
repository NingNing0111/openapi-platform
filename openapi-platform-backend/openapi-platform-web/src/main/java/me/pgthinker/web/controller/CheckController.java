package me.pgthinker.web.controller;

import me.pgthinker.model.dto.CheckDto;
import org.springframework.web.bind.annotation.*;

/**
 * @Project: com.pgthinker.backend.controller
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/7/27 00:06
 * @Description:
 */
@RequestMapping("/check")
@RestController
public class CheckController {
    @GetMapping("/get-param")
    public CheckDto getParam(@RequestParam String name){
        CheckDto checkDto = new CheckDto();
        checkDto.setName(name);
        return checkDto;
    }

    @GetMapping("/post-body")
    public CheckDto postBody(@RequestBody CheckDto  dto){
        System.out.println(dto);
        return dto;
    }
}

