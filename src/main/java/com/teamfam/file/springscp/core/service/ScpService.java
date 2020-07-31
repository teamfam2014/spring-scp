package com.teamfam.file.springscp.core.service;

/**
 * Send a file over using the SCP command.
 */
public interface ScpService {

    /**
     * Transmit a file to a remote system utlizing SCP.
     */
    public Boolean scpFile(String fileName);
}