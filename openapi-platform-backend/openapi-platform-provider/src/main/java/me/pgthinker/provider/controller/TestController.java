package me.pgthinker.provider.controller;

import me.pgthinker.provider.model.dto.TestPingDto;
import org.springframework.web.bind.annotation.*;

/**
 * @Project: me.pgthinker.controller
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/7/24 19:56
 * @Description: 测试接口
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/ping-get")
    public String pingGet() {
        return "pong!";
    }

    @PostMapping("/ping-post-1")
    public String pingPost(@RequestParam String param) {
        System.out.println(param);
        return "pong! " + param;
    }

    @PostMapping("/ping-post-2")
    public String pingPost(@RequestBody TestPingDto testPingDto) {
        return "pong! " + testPingDto.getParam();
    }
}
