package org.aravind.oss.kafka.connector;

import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceConnector;
import org.apache.kafka.connect.util.ConnectorUtils;

import java.io.IOException;
import java.util.*;

/**
 * A Kafka Connect {@link SourceConnector} implementation that watches a Jenkins Instance (through its REST API) and
 * generates tasks to ingest Job Build details.
 * <p>
 * One connector instance is expected to be started for each Jenkins instance.
 *
 * @author Aravind R Yarram
 * @since 4/25/16
 * <p>
 * <p>
 * <p>
 * <p>
 * 1 Jenkins instance       = 1 Kafka Topic
 * 1 Jenkins Project/com.aravind.connector.Job    = 1 Partition (sourcePartition)
 * 1 com.aravind.connector.Job com.aravind.connector.Build              = 1 offset (sourceOffset)
 */
public class JenkinsSourceConnector extends SourceConnector {

    private String baseUrl;
    private String jobsResource;
    private String buildsResource;
    private JenkinsSourceConfig jenkinsCfg;

    public String version() {
        return "0.5";
    }

    public void start(Map<String, String> cfg) {

        System.setProperty("http.proxyHost", "172.18.100.15");
        System.setProperty("http.proxyPort", "18717");
        System.setProperty("https.proxyHost", "172.18.100.15");
        System.setProperty("https.proxyPort", "18717");
        jenkinsCfg = new JenkinsSourceConfig(cfg);
        buildsResource = baseUrl + "/job/Abdera-trunk/api/json";
    }

    public Class<? extends Task> taskClass() {
        return JenkinsSourceTask.class;
    }

    /**
     * Returns a set of configurations for {@link JenkinsSourceTask} based on the current configuration, producing at most {@code numTasks} configurations.
     *
     * @param numTasks maximum number of configurations to generate
     * @return configurations for Tasks
     */
    public List<Map<String, String>> taskConfigs(int numTasks) {
        //get all current jobs
        String json = null;
        try {
            json = Main.get(jenkinsCfg.getJobsResource());
        } catch (IOException e) {
            throw new ConnectException("Error while retrieving jobs from " + jenkinsCfg.getJobsResource(), e);
        }
        Job[] jobs = Main.getJobs(json);

        //create job groups
        int numGroups = Math.min(jobs.length, numTasks);
        List<List<Job>> jobGroups = ConnectorUtils.groupPartitions(Arrays.asList(jobs), numGroups);

        //create task configs for each group
        List<Map<String, String>> taskConfigs = new ArrayList<>(jobGroups.size());

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
