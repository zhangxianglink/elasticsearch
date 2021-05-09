package com.example.demo.config;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexResponse;

public class InsertDataListener implements ActionListener<IndexResponse> {
    @Override
    public void onResponse(IndexResponse indexResponse) {
        System.out.println(indexResponse.getId());
        System.out.println("插入成功");
    }

    @Override
    public void onFailure(Exception e) {
        System.out.println(e);
        System.out.println("插入失败");
    }
}
