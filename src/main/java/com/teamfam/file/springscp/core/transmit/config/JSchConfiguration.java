package com.teamfam.file.springscp.core.transmit.config;

import com.jcraft.jsch.JSch;

/**
 * Configure the JSch for file transmission.
 */
public interface JSchConfiguration {
   
    /**
     * Configure the JSch prior to use. Can be used to configure
     * the JSch with private keys for example.
     * 
     * @param jsch The JSch instance to configure.
     */
    public void configure(JSch jsch);
}