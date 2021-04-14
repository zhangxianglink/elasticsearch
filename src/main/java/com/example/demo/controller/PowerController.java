package com.example.demo.controller;

import com.example.demo.service.PowerService;
import com.example.demo.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/power")
public class PowerController {

    /**
     * 对于下单等存在唯一主键的，可以使用 “唯一主键方案” 的方式实现。
     *
     * 对于更新订单状态等相关的更新场景操作，使用 “乐观锁方案” 实现更为简单。
     *
     * 对于上下游这种，下游请求上游，上游服务可以使用 “下游传递唯一序列号方案” 更为合理。
     *
     * 类似于前端重复提交、重复下单、没有唯一 ID 号的场景，可以通过 Token 与 Redis 配合的 “防重 Token 方案” 实现更为快捷。
     */

    @Autowired
    PowerService powerService;

    @GetMapping("/token")
    public String token(String userId) {
        // 根据信息生产token
        // 将token存入redis
        return  powerService.generateToken(userId);
    }

    @PostMapping("/one")
    public String one(@RequestHeader("token") String token, String userId) {
        // 根据 Token 和与用户相关的信息到 Redis 验证是否存在对应的信息
        boolean result = powerService.validToken(token, userId);
        // 根据验证结果响应不同信息
        return result ? "正常调用" : "重复调用";
    }
}
