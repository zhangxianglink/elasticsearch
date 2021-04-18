package com.example.demo.controller;

import com.example.demo.service.ZKService;
import com.example.demo.service.impl.WaterServiceImpl;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/zk")
public class ZKController {

    @Autowired
    private ZKService zkServiceImpl;

    /**
     * 创建持久化节点
     * path
     * data
     */
    @GetMapping("create")
    private String Create(String path, String data)
    {
        path="/"+path;
        //查看节点是否存在
        Stat stat= zkServiceImpl.exists(path,new WaterServiceImpl());
        if(null != stat)
        {
            return "该节点以存在";
        }
        if(zkServiceImpl.createNode(path,data))
        {
            return "创建成功";
        }

        return "创建失败";
    }

    /**
     * 修改持久化节点
     * @param path
     * @param data
     */
    @GetMapping("updateNode")
    public String updateNode(String path,String data){
        path="/"+path;
        //查看节点是否存在
        Stat stat= zkServiceImpl.exists(path,new WaterServiceImpl());
        if(null == stat)
        {
            return "该节点不存在！";
        }

        if(zkServiceImpl.updateNode(path,data))
        {
            return "修改成功";
        }

        return "修改失败";
    }

    /**
     * 获取当前节点的值(不包含孙子节点)
     *
     */
    @GetMapping("getdata")
    private String getData(String path)
    {
        path="/"+path;
        Stat stat= zkServiceImpl.exists(path,new WaterServiceImpl());
        if(null == stat)
        {
            return "节点不存在！";
        }

        return zkServiceImpl.getData(path,new WaterServiceImpl());
    }


}
