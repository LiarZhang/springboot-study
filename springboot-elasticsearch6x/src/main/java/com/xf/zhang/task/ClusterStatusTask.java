package com.xf.zhang.task;


import com.xf.zhang.common.EnumStatus;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.elasticsearch.cluster.health.ClusterIndexHealth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 监控ES状态变化
 */
@EnableScheduling
@Component
public class ClusterStatusTask {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TransportClient transportClient;

    @Scheduled(fixedDelay = 6000)
    public void healthCheck() {

        ClusterAdminClient clusterAdminClient=transportClient.admin().cluster();//集群管理对象
        ClusterHealthResponse clusterHealthResponse=clusterAdminClient.prepareHealth().get();

        for (ClusterIndexHealth health : clusterHealthResponse.getIndices().values()) {
            String index = health.getIndex();
            int numberOfShards = health.getNumberOfShards();
            int numberOfReplicas = health.getNumberOfReplicas();
            System.out.printf("index=%s,numberOfShards=%d,numberOfReplicas=%d\n",index,numberOfShards,numberOfReplicas);
            ClusterHealthStatus status = health.getStatus();
            if((EnumStatus.GREEN.toString()).equals(status.toString())){
                //do nothing
            }else {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("2532466427@qq.com");
                message.setTo("mr_zxf0105@163.com");
                message.setSubject("主题：ElasticSearch集群报警");
                message.setText("你好：elasticsearch集群状态为"+status.toString()+",请及时处理");
                mailSender.send(message);
                System.out.println("已发送");
                System.out.println(status.toString());
            }
        }
    }
}
