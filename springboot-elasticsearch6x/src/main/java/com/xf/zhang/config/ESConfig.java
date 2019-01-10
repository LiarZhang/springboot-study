package com.xf.zhang.config;//package com.xf.zhang.config;
//
//import org.apache.http.HttpHost;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class ESConfig {
//
//    @Value("spring.data.elasticsearch.cluster-nodes")
//    private String nodes;
//
//    @Bean
//    public RestHighLevelClient client() {
//        //相比于Transport Client，此处不需要配置cluster.name、client.transport.sniff
//        RestHighLevelClient highLevelClient = new RestHighLevelClient(
//                RestClient.builder(
//                        new HttpHost("127.0.0.1", 9200, "http"),
//                        new HttpHost("127.0.0.1", 9201, "http"),
//                        new HttpHost("127.0.0.1", 9202, "http")
//                )
//        );
//        System.out.println("******->"+highLevelClient.cluster());
//        return highLevelClient;
//    }
//}
