## 练手

参考文章：【悟空聊架构】

官方资料：

[Elasticsearch Guide文档](https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html)

[Java High Level REST Client](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high.html#java-rest-high)

```json
GET kibana_sample_data_ecommerce/_search
{
  "query": {
   "bool": {
     "must": [
       {
         "match": {
           "customer_last_name": "Underwood"
         }
       },
       {
         "match": {
           "day_of_week":  "Monday"
         }
       }
     ]
   }
  },
  "aggs": {
    "ageAgg": {
      "avg": {
        "field": "products.price"
      }
    },
    "genderAgg":{
      "terms": {
        "field": "customer_gender",
        "size": 2
      }
    }
  }
}



GET _cat/indices

GET kibana_sample_data_ecommerce/_doc/L78HxHgBBs0nH61MIMMe

GET pass_user/_doc/1

# 覆盖更新
POST pass_user/_doc/1?if_seq_no=1&&if_primary_term=1
{
  "id": "30",
  "name": "jacklove",
  "nickname": "captain"
}

# 对比更新
POST pass_user/_update/1?if_seq_no=2&&if_primary_term=1
{
  "doc": {
    "id":"1",
    "age":"25"
  }
}

```

**统一格式返回处理**

**接口安全设计**

**BUG1** [解决](https://stackoverflow.com/questions/23349180/java-config-for-spring-interceptor-where-interceptor-is-using-autowired-spring-b)

```java
# Interceptor 如果不配置成为spring bean 将无法使用自动注入功能，
    public class LocaleInterceptor extends HandlerInterceptorAdaptor {

    @Autowired
    ISomeService someService;  // null

    ...
}

解决方案：
@EnableWebMvc
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    LocaleInterceptor localInterceptor() {
         return new LocalInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeInterceptor());
    }

}
```





