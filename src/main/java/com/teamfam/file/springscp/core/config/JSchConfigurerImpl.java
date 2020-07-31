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
            if (StringUtils.isNotBlank(scpConfigurationProperties.getKeyFilePath())){
                if (StringUtils.isNotBlank(scpConfigurationProperties.getKeyPassword())){
                    LOG.debug("Configuring JSch with key file and password. keyFilePath={} | keyPassword=****",scpConfigurationProperties.getKeyFilePath());
                    jsch.addIdentity(scpConfigurationProperties.getKeyFilePath(),scpConfigurationProperties.getKeyPassword());
                }else{
                    LOG.debug("Configuring JSch with key file only. keyFilePath={}",scpConfigurationProperties.getKeyFilePath());
                    jsch.addIdentity(scpConfigurationProperties.getKeyFilePath());
                }
            }
            LOG.info("JSch Configuration Complete.");
        }catch(JSchException je){
            LOG.warn("There was an exception adding the identity to JSCH.",je);
        }
    }
    
}