package com.aravind.connector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by p0c on 4/25/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JenkinsInstance {


    Job jobs[];
    public Job[] getJobs() {
        return jobs;
    }

    public void setJobs(Job[] jobs) {
        this.jobs = jobs;
    }

}
