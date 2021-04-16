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

```java
org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor.writeWithMessageConverters(T, MethodParameter, ServletServerHttpRequest, ServletServerHttpResponse) 
返回Controller结果前选择合适的 HttpMessageConverter
    
//     答案都在源码，删除StringHttpMessageConverter selectedMediaType对象首选项就是application/json，不删除就是默认的text/html

//     https://blog.csdn.net/qq_26472621/article/details/102684266?utm_medium=distribute.pc_relevant.none-task-blog-baidujs_title-1&spm=1001.2101.3001.4242
 
```



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

**工具类设计**

```java
/* 通常是以public static方法的形式，向外提供功能. 例如密码，时间，id计算等等。
但是有些工具类需要使用到Spring bean， 例如redisUtils -> redisTemplate
*/

// 传统写法
public  class RedisUtil {
    private static RedisTemplate<String, Object> redisTemplate;

    public static setRedisTemplate(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }
}    还有一些优化，静态单例，类加载优化等 https://blog.csdn.net/u013885298/article/details/106436446/ 

// 借助spring
@Component
public final class RedisUtil {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;
    
    // other methods
}
```


**zookeeper 集群搭建，Api使用**


