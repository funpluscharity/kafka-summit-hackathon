package org.aravind.oss.kafka.connector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by p0c on 4/25/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Job {
    String name;
    String url;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String color;

}
