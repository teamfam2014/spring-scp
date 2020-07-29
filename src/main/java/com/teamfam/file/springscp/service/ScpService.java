package com.teamfam.file.springscp.service;

/**
 * Send a file over using the SCP command.
 */
public interface ScpService {

    /**
     * Transmit a file to a remote system utlizing SCP.
     */
    public String scp();
}