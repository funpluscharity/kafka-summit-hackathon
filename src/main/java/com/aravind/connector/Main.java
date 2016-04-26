package com.aravind.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;

/**
 * Created by p0c on 4/25/16.
 */
public class Main {
    static {
        System.setProperty("http.proxyHost", "172.18.100.15");
        System.setProperty("http.proxyPort", "18717");
        System.setProperty("https.proxyHost", "172.18.100.15");
        System.setProperty("https.proxyPort", "18717");
        Authenticator.setDefault(new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                if (getRequestorType() == Authenticator.RequestorType.PROXY)
                    return new PasswordAuthentication("p0c", "T6356yr^".toCharArray());
                else
                    return super.getPasswordAuthentication();
            }
        });
    }

    public static String get(String url) {
        Authenticator.setDefault(new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                if (getRequestorType() == Authenticator.RequestorType.PROXY)
                    return new PasswordAuthentication("p0c", "T6356yr^".toCharArray());
                else
                    return super.getPasswordAuthentication();
            }
        });
        InputStream response = null;

        String s = null;
        try {
            response = new URL(url).openStream();
            s = IOUtils.toString(response, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(s);
        return s;
    }

    public static Job[] getJobs(String json) {
        JenkinsInstance ji = null;
        try {

            ObjectMapper mapper = new ObjectMapper();
            ji = mapper.readValue(json, JenkinsInstance.class);

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return ji.getJobs();
    }

    public static Build[] getBuilds(String json) {
        BuildCollection bl = null;
        try {

            ObjectMapper mapper = new ObjectMapper();
            bl = mapper.readValue(json, BuildCollection.class);

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return bl.getBuilds();
    }

    public static void main(String args[]) {

        Authenticator.setDefault(new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                if (getRequestorType() == Authenticator.RequestorType.PROXY)
                    return new PasswordAuthentication("p0c", "T6356yr^".toCharArray());
                else
                    return super.getPasswordAuthentication();
            }
        });

        String json = get("https://builds.apache.org/api/json");
        Job[] jobs = getJobs(json);
        System.out.println(jobs.length);

        for (Job j : jobs) {
            String r = get(j.getUrl() + "api/json");
            Build[] builds = getBuilds(r);
            System.out.println("builds.length: "+builds.length);
        }
    }
}
