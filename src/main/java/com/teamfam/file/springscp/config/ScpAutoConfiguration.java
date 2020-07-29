package com.teamfam.file.springscp.config;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto configure the SCP service library.
 * 
 * @author teamfam
 */
@Configuration
public class ScpAutoConfiguration {
    
    private static final Logger LOG = LoggerFactory.getLogger(ScpAutoConfiguration.class);

    @Autowired
    private ScpConfigurationProperties scpConfigurationProperties;

    @Bean
    public JSch jsch(){
        JSch jsch = new JSch();
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
        return jsch;
    }
}