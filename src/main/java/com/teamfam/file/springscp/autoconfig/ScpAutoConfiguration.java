package com.teamfam.file.springscp.autoconfig;

import com.jcraft.jsch.JSch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Auto configure the SCP service library.
 * 
 * @author teamfam
 */
@Configuration
@ComponentScan("com.teamfam.file.springscp.core")
public class ScpAutoConfiguration {

    @Bean
    public JSch jsch(){
        JSch jsch = new JSch();
        return jsch;
    }
}