/**
package com.example.demo;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.util.StringUtils;

import com.webapp.common.AppException;
import com.webapp.common.enums.ErrorCode;
import com.webapp.dao.model.DataIntegrationTable;
import com.webapp.dao.model.DataIntegrationTableField;
import com.webapp.dao.model.DbManager;
import com.webapp.dao.model.DbServer;

import lombok.extern.slf4j.Slf4j;
import scala.collection.mutable.StringBuilder;

@Slf4j
public class JDBCElasticSearchUtil {

    

    public static RestHighLevelClient buildClient(DbManager dataSource) {
        RestHighLevelClient restHighLevelClient;
        try {
            HttpHost[] httpHosts = new HttpHost[dataSource.getServers().size()];
            for (int i = 0; i < httpHosts.length; i++) {
                DbServer server = dataSource.getServers().get(i);
                httpHosts[i] = new HttpHost(server.getHost(), Integer.parseInt(server.getPort()), "http");
            }
            if(!StringUtils.isEmpty(dataSource.getUsername())) {
                final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(AuthScope.ANY,new UsernamePasswordCredentials(dataSource.getUsername(), dataSource.getPassword()));  
                restHighLevelClient = new RestHighLevelClient(
                        RestClient.builder(httpHosts).setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                            @Override
                            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                                httpClientBuilder.disableAuthCaching();
                                return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                            }
                        })
                );
            }else{
                restHighLevelClient = new RestHighLevelClient(RestClient.builder(httpHosts));
            }
        } catch (Exception e) {
            log.error("buildClient RestHighLevelClient error {} ",e.getMessage());
            return null;
        }
        return restHighLevelClient;
    }
    
    public static String getElasticSearchServer(DbManager dbManager) {
    	List<DbServer> servers = dbManager.getServers();
    	StringBuilder sb = new StringBuilder();
    	for (int i = 0; i < servers.size(); i++) {
    		DbServer dbServer = servers.get(i);
    		if ((i+1) == servers.size()) {
    			sb.append(dbServer.getHost()).append(":").append(dbServer.getPort());
			}else {
				sb.append(dbServer.getHost()).append(":").append(dbServer.getPort()).append(",");
			}
		}
    	return sb.toString();
    }
    
    // ["127.0.0.1:9200","127.0.0.2:9200"]
    public static String getLogstashHosts(DbManager dbManager) {
    	List<DbServer> servers = dbManager.getServers();
    	StringBuilder sb = new StringBuilder();
    	sb.append("[");
    	for (int i = 0; i < servers.size(); i++) {
    		DbServer dbServer = servers.get(i);
    		if ((i+1) == servers.size()) {
    			sb.append("\""+dbServer.getHost()).append(":").append(dbServer.getPort()+"\"");
			}else {
				sb.append("\""+dbServer.getHost()).append(":").append(dbServer.getPort()+"\"").append(",");
			}
		}
    	sb.append("]");
    	return sb.toString();
    }
    
    public static void close(RestHighLevelClient client) {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                log.info("elasticsearch RestHighLevelClient close error: {}",e.getMessage());
            }
        }
        log.info("elasticsearch RestHighLevelClient close");
    }
    
    public static boolean isValid(RestHighLevelClient client) {
        return client != null;
    }

// 创建索引
	public static void createIndex(String indexName, RestHighLevelClient client) throws IOException {
		CreateIndexRequest requestIndex = new CreateIndexRequest(indexName.toLowerCase());
		// 注： 设置副本数为0，索引刷新时间为-1对大批量索引数据效率的提升有不小的帮助
		requestIndex.settings(Settings.builder().put("index.number_of_shards", 1)
				.put("index.number_of_replicas", 0)
				.put("index.refresh_interval", "120s"));
		client.indices().create(requestIndex, RequestOptions.DEFAULT);
	}
	


//创建mapping索引
	public static String createIndexMapping(String indexName, DataIntegrationTable dTable,RestHighLevelClient client){
		String result = indexName + "索引创建完成。";
		try {
			//判断indexName是否已经存在, mapping只能新增字段，如果修改需要新建索引
			 GetIndexRequest exist=new GetIndexRequest(indexName);
		     boolean exists=client.indices().exists(exist, RequestOptions.DEFAULT);
			if (!exists) {
				List<DataIntegrationTableField> tableFields = dTable.getDataIntegrationTableFields();
				CreateIndexRequest requestIndex = new CreateIndexRequest(indexName.toLowerCase());
				requestIndex.settings(
						Settings.builder().put("index.number_of_shards", 1).put("index.number_of_replicas", 0));
				XContentBuilder builder = XContentFactory.jsonBuilder();
				builder.startObject();
				{
					builder.startObject("properties");
					{
						for (DataIntegrationTableField dataIntegrationTableField : tableFields) {
							builder.startObject(dataIntegrationTableField.getTargetFieldName());
							{
								builder.field("type", dataIntegrationTableField.getTargetFieldType());
							}
							builder.endObject();
						}
					}
					builder.endObject();
				}
				builder.endObject();
				requestIndex.mapping(builder);
				client.indices().create(requestIndex, RequestOptions.DEFAULT);
			}else {
				result = indexName + "索引已经存在了。";
			}
		} catch (Exception e) {
			result =  FileUtils.collectExceptionStackMsg(e);
		}
		return result;
	}
	
//创建bulkProcessor并初始化
	private static BulkProcessor getBulkProcessor(RestHighLevelClient client) {

		BulkProcessor bulkProcessor = null;
		try {

			BulkProcessor.Listener listener = new BulkProcessor.Listener() {
				@Override
				public void beforeBulk(long executionId, BulkRequest request) {
					log.info("Try to insert data number : " + request.numberOfActions());
				}

				@Override
				public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
					log.info("************** Success insert data number : " + request.numberOfActions() + " , id: "
							+ executionId);
				}

				@Override
				public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
					log.error("Bulk is unsuccess : " + failure + ", executionId: " + executionId);
				}
			};

			BiConsumer<BulkRequest, ActionListener<BulkResponse>> bulkConsumer = (request, bulkListener) -> client
					.bulkAsync(request, RequestOptions.DEFAULT, bulkListener);

			BulkProcessor.Builder builder = BulkProcessor.builder(bulkConsumer, listener);
//			根据当前添加的操作数设置何时刷新新的批量请求（默认为1000，使用-1禁用它）
//			根据当前添加的操作大小设置何时刷新新的批量请求（默认为5Mb，使用-1禁用它）
//			设置允许执行的并发请求数（默认为1，使用0仅允许执行单个请求）
//			设置刷新间隔，BulkRequest如果间隔过去，则刷新所有未决（默认值未设置）
//			设置一个恒定的退避策略，该策略最初等待1秒，然后重试3次。请参阅BackoffPolicy.noBackoff()， BackoffPolicy.constantBackoff()以及BackoffPolicy.exponentialBackoff() 更多选项。
			builder.setBulkActions(1000);
			builder.setBulkSize(new ByteSizeValue(2L, ByteSizeUnit.MB));
			builder.setConcurrentRequests(3);
			builder.setFlushInterval(TimeValue.timeValueSeconds(60L));
			builder.setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1L), 3));

			bulkProcessor = builder.build();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				bulkProcessor.awaitClose(100L, TimeUnit.SECONDS);
				client.close();
			} catch (Exception e1) {
				log.error(e1.getMessage());
			}
		}
		return bulkProcessor;
	}

	
	// 将mysql 数据查出组装成es需要的map格式，通过批量写入es中
	public static void writeMysqlDataToES(String tableName, RestHighLevelClient client, ArrayList<HashMap<String, String>>  list) {
		BulkProcessor bulkProcessor = getBulkProcessor(client);
		try {
			int count = 0;
			ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
			for (HashMap<String, String> map : list) {
				count++;
				dataList.add(map);
				if (count % 1000 == 0) {
					log.info("local file data number : " + count);
					for (HashMap<String, String> hashMap2 : dataList) {
						bulkProcessor.add(new IndexRequest(tableName.toLowerCase()).id(null).source(hashMap2));
					}
					dataList.clear();
				}
			}

			//处理未提交的数据
			for (HashMap<String, String> hashMap2 : dataList) {
				bulkProcessor.add(new IndexRequest(tableName.toLowerCase()).id(null).source(hashMap2));
			}

			log.info("-------------------------- Finally insert number total : " + count);
            // 将数据刷新到es, 注意这一步执行后并不会立即生效，取决于bulkProcessor设置的刷新时间
			bulkProcessor.flush();
			
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			try {
				boolean terminatedFlag = bulkProcessor.awaitClose(150L, TimeUnit.SECONDS);
				log.info(terminatedFlag+": 批量请求结果。");
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
	}

}

 */