package com.teamfam.file.springscp.core.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Properties of the SCP transmission such as the key file location, the keyfile password etc.
 * 
 * @author teamfam
 */
@Configuration
@ConfigurationProperties(prefix = "scp")
public class ScpConfigurationProperties {
    private String prvKeyFilePath;
    private String pubKeyFilePath;
    private String keyPassword;
    private String userName;
    private String host;
    private String port;
    private Map<String,String> sessionConfigs;
    private String localFilePath;
    private String remoteFilePath;

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

    public String getPrvKeyFilePath() {
        return prvKeyFilePath;
    }

    public void setPrvKeyFilePath(String prvKeyFilePath) {
        this.prvKeyFilePath = prvKeyFilePath;
    }

    public String getPubKeyFilePath() {
        return pubKeyFilePath;
    }

    public void setPubKeyFilePath(String pubKeyFilePath) {
        this.pubKeyFilePath = pubKeyFilePath;
    }
    
}