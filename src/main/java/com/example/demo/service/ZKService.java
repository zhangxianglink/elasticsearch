package com.example.demo.service;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.util.List;

public interface ZKService {

    /**
     * 判断指定节点是否存在
     * @param path
     * @param needWatch  指定是否复用zookeeper中默认的Watcher
     * @return
     */
    Stat exists(String path, boolean needWatch);

    /**
     *  检测结点是否存在 并设置监听事件
     *      三种监听类型： 创建，删除，更新
     *
     * @param path
     * @param watcher  传入指定的监听类
     * @return
     */
    Stat exists(String path, Watcher watcher );

    /**
     * 创建持久化节点
     * @param path
     * @param data
     */
    boolean createNode(String path, String data);

    /**
     * 修改持久化节点
     * @param path
     * @param data
     */
    boolean updateNode(String path, String data);

    /**
     * 删除持久化节点
     * @param path
     */
    boolean deleteNode(String path);

    /**
     * 获取当前节点的子节点(不包含孙子节点)
     * @param path 父节点path
     */
    List<String> getChildren(String path) throws KeeperException, InterruptedException;

    /**
     * 获取指定节点的值
     * @param path
     * @return
     */
    String getData(String path,Watcher watcher);


}
