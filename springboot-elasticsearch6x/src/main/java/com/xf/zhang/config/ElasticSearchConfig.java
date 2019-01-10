package com.xf.zhang.config;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;

@Configuration
public class ElasticSearchConfig {

    @Value("spring.data.elasticsearch.cluster-nodes")
    private String nodes;

    @Value("${spring.data.elasticsearch.cluster-name}")
    private String clusterName;

    @Bean
    public TransportClient transportClient() throws Exception {
        Settings settings = Settings.builder()
                .put("cluster.name", this.clusterName)
                .put("client.transport.sniff", true)
                .put("thread_pool.search.size", 6)//增加线程池个数，暂时设为5
                .build();

        TransportAddress master0 = new TransportAddress(
                InetAddress.getByName("127.0.0.1"), 9300);
        TransportAddress master1 = new TransportAddress(
                InetAddress.getByName("127.0.0.1"), 9301);
        TransportAddress master2 = new TransportAddress(
                InetAddress.getByName("127.0.0.1"), 9302);
        TransportClient transportClient = new PreBuiltTransportClient(settings)
                .addTransportAddress(master0)
                .addTransportAddress(master1)
                .addTransportAddress(master2);

        return transportClient;
    }

}
