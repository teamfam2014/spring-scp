package com.teamfam.file.springscp.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties of the SCP transmission such as the key file location, the keyfile password etc.
 * 
 * @author teamfam
 */
@ConfigurationProperties(prefix = "scp")
public class ScpConfigurationProperties {
    private String keyFilePath;
    private String keyPassword;
    private String userName;
    private String host;
    private String port;
    private Map<String,String> sessionConfigs;
    private String localFilePath;
    private String remoteFilePath;

    public String getKeyFilePath() {
        return keyFilePath;
    }

    public void setKeyFilePath(String keyFilePath) {
        this.keyFilePath = keyFilePath;
    }

    public String getKeyPassword() {
        return keyPassword;
    }

    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Map<String, String> getSessionConfigs() {
        return sessionConfigs;
    }

    public void setSessionConfigs(Map<String, String> sessionConfigs) {
        this.sessionConfigs = sessionConfigs;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String getRemoteFilePath() {
        return remoteFilePath;
    }

    public void setRemoteFilePath(String remoteFilePath) {
        this.remoteFilePath = remoteFilePath;
    }
    
}