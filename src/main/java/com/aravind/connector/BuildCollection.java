package com.aravind.connector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by p0c on 4/25/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuildCollection {
    public Build[] getBuilds() {
        return builds;
    }

    public void setBuilds(Build[] builds) {
        this.builds = builds;
    }

    Build[] builds;

}
