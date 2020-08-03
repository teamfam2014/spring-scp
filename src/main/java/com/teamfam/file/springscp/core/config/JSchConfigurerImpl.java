package com.teamfam.file.springscp.core.config;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JSchConfigurerImpl implements JSchConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(JSchConfigurerImpl.class);

    @Autowired
    private ScpConfigurationProperties scpConfigurationProperties;

    @Override
    public void configure(JSch jsch) {
        try{
            if (StringUtils.isNotBlank(scpConfigurationProperties.getPrvKeyFilePath()) && StringUtils.isNotBlank(scpConfigurationProperties.getPubKeyFilePath())){
                if (StringUtils.isNotBlank(scpConfigurationProperties.getKeyPassword())){
                    LOG.debug("Configuring JSch with private key and public key files and password. prvKeyFilePath={} | pubKeyFilePath={} | keyPassword=****",scpConfigurationProperties.getPrvKeyFilePath(),scpConfigurationProperties.getPubKeyFilePath());
                    jsch.addIdentity(getResourceFile(scpConfigurationProperties.getPrvKeyFilePath()),getResourceFile(scpConfigurationProperties.getPrvKeyFilePath()),scpConfigurationProperties.getKeyPassword().getBytes());
                }else{
                    LOG.debug("Configuring JSch with private key and public key files only. pubKeyFilePath={} | prvKeyFilePath={}",scpConfigurationProperties.getPrvKeyFilePath(),scpConfigurationProperties.getPubKeyFilePath());
                    jsch.addIdentity(getResourceFile(scpConfigurationProperties.getPrvKeyFilePath()),getResourceFile(scpConfigurationProperties.getPubKeyFilePath()));
                }
            }else{
                LOG.warn("Public and Private Keys not configured.");
            }
            LOG.info("JSch Configuration Complete.");
        }catch(JSchException je){
            LOG.warn("There was an exception adding the identity to JSCH.",je);
        }
    }
    
    private String getResourceFile(String fileName) {
        return this.getClass().getClassLoader().getResource(fileName).getPath();
    }

}