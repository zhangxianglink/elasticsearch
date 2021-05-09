/*package com.example.demo;

import com.alibaba.fastjson.JSONObject;
import com.webapp.MainApplication;
import com.webapp.common.Constants;
import com.webapp.dao.model.DbManager;
import com.webapp.elasticsearch.ElasticsearchTemplateImpl;
import com.webapp.service.DbManagerService;
import com.webapp.utils.FileUtils;
import com.webapp.utils.JDBCElasticSearchUtil;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequest;
import org.elasticsearch.script.mustache.SearchTemplateResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class Elastic7Test {

	@Autowired
	private DbManagerService dbService;

	private static RestHighLevelClient buildClient = null;

    @Before
    public void init() throws Exception {
		DbManager dataSource = dbService.getDataSource(3L);
		 buildClient = JDBCElasticSearchUtil.buildClient(dataSource);
	}

    @After
    public void destory() throws Exception {
       JDBCElasticSearchUtil.close(buildClient);
    }

    @Test
    public void getAllTypes() throws IOException {
    	GetIndexResponse respons = buildClient.indices().get(new GetIndexRequest(Constants.ELS_INDEX_ALL),RequestOptions.DEFAULT );//org.elasticsearch.client.RequestOption.
        for (String indexName : respons.getIndices()) {
           if(!indexName.startsWith(Constants.ELS_SEPARATOR_DOT) && !indexName.startsWith(Constants.ELS_INDEX_KIBANA)) {
        	   System.out.println(indexName);
           }
        }
    }


	@Test
    public void getMapping() throws IOException {
        org.elasticsearch.client.indices.GetMappingsRequest request = new org.elasticsearch.client.indices.GetMappingsRequest();
    	request.indices("els_152");
    	request.setMasterTimeout(TimeValue.timeValueMinutes(1));
    	org.elasticsearch.client.indices.GetMappingsResponse response = buildClient.indices().getMapping(request, RequestOptions.DEFAULT);
    	Map<String, MappingMetaData> allMappings = response.mappings();
    	MappingMetaData indexMapping = allMappings.get("library.zzr");
    	Map<String, Object> mapping = indexMapping.sourceAsMap();
    	Map<String, Map<String, Object>> properties = (Map<String, Map<String, Object>>) mapping.get("properties");
    	 for (Entry<String, Map<String, Object>> entry : properties.entrySet()) {
             System.out.println("key:value = " + entry.getKey() + ":" + entry.getValue().get("type"));
         }
    }

	@Test
	public void getMapping2() {
		ElasticsearchTemplateImpl templateImpl = new ElasticsearchTemplateImpl(dbService.getDataSource(3L));
		List<String> mappingfields2 = templateImpl.getMappingfields("hive_to_els_0608");
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (String string : mappingfields2) {
			sb.append("\""+string+"\",");
		}
		sb.append("\"es_to_hive_version\"]");
		templateImpl.close();
		System.out.println(sb.toString());

	}

	@Test
	public void putToEs() throws IOException {
		Path path = Paths.get("D:\\img\\20200310\\fakemsg.txt");
		List<String> lines = Files.readAllLines(path);
		BulkRequest bulkRequest = new BulkRequest();
		int count =0;
		for (String value : lines) {
			System.out.println(count);
			Map<String,Object> map = JSONObject.parseObject(value,  Map.class);
			bulkRequest.add(new IndexRequest("quarter_table").id(null).source(map));
			count ++;
		}
		BulkResponse bulkResponse = buildClient.bulk(bulkRequest, RequestOptions.DEFAULT);
		boolean hasFailures = bulkResponse.hasFailures();
		System.out.println(hasFailures);
	}

	@Test
	public void createIndex() {
		HashMap<String, String> map = new HashMap<>();
		map.put("es_id", "long");
		map.put("es_REGION", "text");
		map.put("es_REGION_CODE", "keyword");
		map.put("es_EMAIL", "keyword");
		map.put("es_UPDATE_DATE", "date");
		map.put("es_version", "keyword");
		try {
			//判断indexName是否已经存在, mapping只能新增字段，如果修改需要新建索引
			 GetIndexRequest exist=new GetIndexRequest("test-1");
		     boolean exists = buildClient.indices().exists(exist, RequestOptions.DEFAULT);
			if (!exists) {
				CreateIndexRequest requestIndex = new CreateIndexRequest("test-1");
				requestIndex.settings(
						Settings.builder().put("index.number_of_shards", 1).put("index.number_of_replicas", 0));
				XContentBuilder builder = XContentFactory.jsonBuilder();
				builder.startObject();
				{
					builder.startObject("properties");
					{
						for (Entry<String, String> entry : map.entrySet()) {
							builder.startObject(entry.getKey());
							{
								builder.field("type", entry.getValue());
							}
							builder.endObject();
						}
					}
					builder.endObject();
				}
				builder.endObject();
				requestIndex.mapping(builder);
				buildClient.indices().create(requestIndex, RequestOptions.DEFAULT);
		}} catch (Exception e) {
			System.out.println(FileUtils.collectExceptionStackMsg(e));
		}

	}


	@Test
	 public void query1() throws IOException, InterruptedException {
	  //限定索引
	  SearchRequest searchRequest = new SearchRequest("standards");
	  // IndicesOptions控制如何解决不可用的索引以及如何扩展通配符表达式
	  searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());
	  SearchSourceBuilder buider = new SearchSourceBuilder();
	  // 配置查询
	  BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
	  boolQuery.must(QueryBuilders.termQuery("topic.keyword", "客户"));
	  boolQuery.must(QueryBuilders.rangeQuery("createDate").lte("2020-04-27 13:32:46").gte("2020-04-27 13:32:40"));
	  buider.query(boolQuery);
	  buider.from(0);
	  buider.size(10);
	  buider.timeout(new TimeValue(60, TimeUnit.SECONDS));
	  searchRequest.source(buider);

	  SearchResponse searchResponse = buildClient.search(searchRequest, RequestOptions.DEFAULT);
	  readSearchResult(searchResponse);
	 }

	private void readSearchResult(SearchResponse searchResponse) {
		SearchHits hits = searchResponse.getHits();
		 TotalHits totalHits = hits.getTotalHits();
		 long numHits = totalHits.value;
		 System.out.println("命中数量"+numHits);
		   for (SearchHit hit : hits.getHits()) {
		    String sourceAsString = hit.getSourceAsString();
		    System.out.println(sourceAsString);
		    Map<String, Object> sourceAsMap = hit.getSourceAsMap();
		    System.out.println(sourceAsMap);
		    String documentTitle = String.valueOf(sourceAsMap.get("businessDefinition"));
		    System.out.println(documentTitle);
//	    List<Object> users = (List<Object>) sourceAsMap.get("topic");
//	    Map<String, Object> innerObject =
//	            (Map<String, Object>) sourceAsMap.get("innerObject");
		   }
	}


	@Test
	 public void query2() throws IOException, InterruptedException {
		SearchTemplateRequest request = new SearchTemplateRequest();
		request.setRequest(new SearchRequest("standards"));
		request.setScriptType(ScriptType.INLINE);
		request.setScript("{\n" +
				"  \"from\": \"{{from}}\",\n" +
				"  \"size\": \"{{size}}\",\n" +
				"  \"timeout\": \"60s\",\n" +
				"  \"query\": {\n" +
				"    \"bool\": {\n" +
				"      \"must\": [\n" +
				"        {\n" +
				"          \"term\": {\n" +
				"            \"dataType\": {\n" +
				"              \"value\": \"{{dataType}}\",\n" +
				"              \"boost\": 1\n" +
				"            }\n" +
				"          }\n" +
				"        },\n" +
				"        {\n" +
				"          \"range\": {\n" +
				"            \"createDate\": {\n" +
				"              \"gte\": \"{{gte}}\",\n" +
				"              \"lte\": \"{{lte}}\"\n" +
				"            }\n" +
				"          }\n" +
				"        }\n" +
				"      ],\n" +
				"      \"adjust_pure_negative\": true,\n" +
				"      \"boost\": 1\n" +
				"    }\n" +
				"  }\n" +
				"}");
		Map<String, Object> scriptParams = new HashMap<>();
		scriptParams.put("from", 0);
		scriptParams.put("dataType", "n1");
		scriptParams.put("size", 5);
		scriptParams.put("gte", "2020-04-27 13:32:40");
		scriptParams.put("lte", "2020-07-27 13:32:46");
		request.setScriptParams(scriptParams);
		SearchTemplateResponse response = buildClient.searchTemplate(request, RequestOptions.DEFAULT);
		SearchResponse searchResponse = response.getResponse();
		readSearchResult(searchResponse);
	 }


}
*/