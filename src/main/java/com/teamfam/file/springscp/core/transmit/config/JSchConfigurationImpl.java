package com.teamfam.file.springscp.core.transmit.config;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.teamfam.file.springscp.core.config.ScpConfigurationProperties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JSchConfigurationImpl implements JSchConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(JSchConfigurationImpl.class);

    @Autowired
    private ScpConfigurationProperties scpConfigurationProperties;

    @Override
    public void configure(JSch jsch) {
        try{
            if (StringUtils.isNotBlank(scpConfigurationProperties.getKeyFilePath())){
                if (StringUtils.isNotBlank(scpConfigurationProperties.getKeyPassword())){
                    jsch.addIdentity(scpConfigurationProperties.getKeyFilePath(),scpConfigurationProperties.getKeyPassword());
                }else{
                    jsch.addIdentity(scpConfigurationProperties.getKeyFilePath());
                }
            }
        }catch(JSchException je){
            LOG.warn("There was an exception adding the identity to JSCH.",je);
        }
    }
    
}