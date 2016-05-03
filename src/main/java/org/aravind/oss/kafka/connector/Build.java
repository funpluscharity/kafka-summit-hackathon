package org.aravind.oss.kafka.connector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by p0c on 4/25/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Build {
    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    Long number;
    String url;

}
