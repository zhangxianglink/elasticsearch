package com.example.demo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.example.demo.config.InsertDataListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Cancellable;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class PassSearchApplicationTests {
	
	private static  Logger log = LoggerFactory.getLogger(PassSearchApplicationTests.class);
	
	@Resource(name = "passSearchClient")
    private RestHighLevelClient client;
	
	@Test
	void contextLoads() {
		System.out.println(client);
	}

	@Test 
	void insertUserIndex() throws IOException {
		IndexRequest request = new IndexRequest("pass_user");
		User user = new User();
		user.setAge(25);
		user.setId("1");
		user.setNickname("jacklove");
		user.setName("柴犬");
		ObjectMapper ob = new ObjectMapper();
		String writeValueAsString = ob.writeValueAsString(user);
		request.source(writeValueAsString, XContentType.JSON); 
		
		IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
		String index = indexResponse.getIndex();
		String id = indexResponse.getId();
		log.info("index is :{} id : {} ", index, id);
	}

	@Test
	void insertAsync() throws JsonProcessingException, InterruptedException {
		IndexRequest request = new IndexRequest("pass_user");
		User user = new User();
		user.setAge(25);
		user.setId("1");
		user.setNickname("async");
		user.setName("dog");
		ObjectMapper ob = new ObjectMapper();
		String writeValueAsString = ob.writeValueAsString(user);
		request.source(writeValueAsString, XContentType.JSON);
		client.indexAsync(request, RequestOptions.DEFAULT, new InsertDataListener());
		System.out.println("异步操作");
		Thread.sleep(2000);
	}
	
	@Test
	public void genderAggs() throws IOException {
		SearchRequest sq = new SearchRequest();
		sq.indices("kibana_sample_data_ecommerce");
		
		SearchSourceBuilder builder = new SearchSourceBuilder();
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		BoolQueryBuilder must = boolQuery.must(QueryBuilders.matchQuery("customer_last_name", "Underwood")).must(QueryBuilders.matchQuery("day_of_week", "Monday"));
		builder.query(must);
		
		TermsAggregationBuilder gender = AggregationBuilders.terms("genderAgg").field("customer_gender");
		builder.aggregation(gender);
		AvgAggregationBuilder price = AggregationBuilders.avg("priceAvg").field("products.price");
		builder.aggregation(price);
		
		log.info("检索参数：{}", builder.toString());
		
		sq.source(builder);
		
		SearchResponse response = client.search(sq, RequestOptions.DEFAULT);
		
		SearchHits hits = response.getHits();
		TotalHits totalHits = hits.getTotalHits();
		long number = totalHits.value;
		log.info("总数： {}",number);
		
		SearchHit[] searchHits = hits.getHits();
		for (SearchHit searchHit : searchHits) {
			String hitStr = searchHit.getSourceAsString();
			System.out.println(hitStr);
		}
		
		Aggregations aggregations = response.getAggregations();
		Terms genderAgg = aggregations.get("genderAgg");
		
		List<? extends Bucket> buckets = genderAgg.getBuckets();
		buckets.forEach(e -> log.info("性别：{}，数量： {}", e.getKeyAsString(), e.getDocCount()));
	
		Avg balanceAvg1 = aggregations.get("priceAvg");
		log.info("平均购物价格：{}" , balanceAvg1.getValue());
	}
	
	//https://mp.weixin.qq.com/s/9dmKmqk8YA6ZPfpH7paqpQ
	//https://mp.weixin.qq.com/s/ojH-X3emovz2YOzoqNYPDQ	
}
