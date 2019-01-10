package com.xf.zhang.controller;

import org.apache.catalina.connector.Response;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 */
@RestController
public class ClusterStatusController {

    private static final String HEALTH_CHECK_API = "http://127.0.0.1:8888/_cluster/health";

    private static final String GREEN = "green";
    private static final String YELLOW = "yellow";
    private static final String RED = "red";

    //    @Scheduled(fixedDelay = 60 * 1000)
    @RequestMapping("/health")
    public int healthCheck() {
        HttpClient httpClient = HttpClients.createDefault();

        HttpGet get = new HttpGet(HEALTH_CHECK_API);

        try {
            HttpResponse response = httpClient.execute(get);
            return response.getStatusLine().getStatusCode();
        }catch (Exception e){

        }
        return 9999;
    }


}
