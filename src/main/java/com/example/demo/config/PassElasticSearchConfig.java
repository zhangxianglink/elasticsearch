package com.example.demo.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class PassElasticSearchConfig {
	
	private static  Logger log = LoggerFactory.getLogger(PassElasticSearchConfig.class);

	@Bean("passSearchClient")
	public RestHighLevelClient passSearchClient() {
		RestClientBuilder builder = RestClient.builder(
		        new HttpHost("1xxxx", 9200, "http"));
		builder.setFailureListener(new RestClient.FailureListener() {
		    @Override
		    public void onFailure(Node node) {
		        log.error("node error: {}", node.getHost());
		    }
		});
		
		return new RestHighLevelClient(builder);
	}
}
