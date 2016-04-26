package com.aravind.connector;

import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by p0c on 4/25/16.
 * <p>
 * 1 Jenkins instance       = 1 Kafka Topic
 * 1 Jenkins Project/com.aravind.connector.Job    = 1 Partition (sourcePartition)
 * 1 com.aravind.connector.Job com.aravind.connector.Build              = 1 offset (sourceOffset)
 */
public class JenkinsSourceConnector extends SourceConnector {

    private String baseUrl;
    private String jobsResource;
    private String buildsResource;

    public String version() {
        return "0.1";
    }

    public void start(Map<String, String> cfg) {

        System.setProperty("http.proxyHost", "172.18.100.15");
        System.setProperty("http.proxyPort", "18717");
        System.setProperty("https.proxyHost", "172.18.100.15");
        System.setProperty("https.proxyPort", "18717");

        baseUrl = "http://builds.apache.org";
        jobsResource = baseUrl + "/api/json";
        buildsResource = baseUrl + "/job/Abdera-trunk/api/json";
    }

    public Class<? extends Task> taskClass() {
        return JenkinsSourceTask.class;
    }

    public List<Map<String, String>> taskConfigs(int maxTasks) {
        //split into multiple task configs (max is maxTasks)
        String json = Main.get("https://builds.apache.org/api/json");
        Job[] jobs = Main.getJobs(json);

        List<Map<String, String>> taskConfigs = new ArrayList<>(jobs.length);

        for (Job j : jobs) {
            Map<String, String> taskProps = new HashMap<>();
            taskProps.put("jenkinsInstance", "jenkins-test");
            taskProps.put("jobName", j.getName());
            taskProps.put("jobUrl", j.getUrl());
            taskProps.put("jobColor", j.getColor());

            taskConfigs.add(taskProps);
        }
        return taskConfigs;
    }

    public void stop() {

    }
}
