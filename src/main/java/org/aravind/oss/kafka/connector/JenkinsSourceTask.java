package org.aravind.oss.kafka.connector;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by p0c on 4/25/16.
 */
public class JenkinsSourceTask extends SourceTask {

    private String jobName;
    private String jobUrl;
    private String jenkinsInstance;

    public String version() {
        return null;
    }

    public void start(Map<String, String> taskProps) {
        jobName = taskProps.get("jobName");
        jobUrl = taskProps.get("jobUrl");
        jenkinsInstance = taskProps.get("jenkinsInstance");
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        System.out.println("getting builds from url: "+jobUrl + "api/json");
        String json = Main.get(jobUrl + "api/json");
        Build[] builds = Main.getBuilds(json);
        System.out.println("builds length: " + builds.length);

        ArrayList<SourceRecord> records = new ArrayList<>();

        for (Build b : builds) {
            Map sourcePartition = Collections.singletonMap("jobName", jobName);
            Map sourceOffset = Collections.singletonMap("buildNumber", b.getNumber());

            records.add(new SourceRecord(sourcePartition, sourceOffset, jenkinsInstance, Schema.STRING_SCHEMA, "" + b.getNumber() + "," + b.getUrl()));
        }
        return records;
    }

    public void stop() {

    }

}
