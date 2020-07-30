package com.teamfam.file.springscp.transmit;

import com.jcraft.jsch.JSch;
import com.teamfam.file.springscp.config.ScpConfigurationProperties;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit test the SCP file transmitter.
 */
@ExtendWith(MockitoExtension.class)
public class ScpFileTransmitterTest {
    
    @InjectMocks
    private ScpFileTransmitter scpFileTransmitter;

    @Mock
    private JSch jsch;
    
    @Mock
    private ScpConfigurationProperties scpConfigProps;

    /**
     * If there is an issue obtaining the session, then the 
     * file is not transmitted.
     */

    /**
     * If there is an issue connecting to the session, then
     * the file is not transmitted.
     */

    /**
     * If there is an issue opening the channel, then the
     * file is not transmitted.
     */

    /**
     * If there is an issue opening the output stream, then the 
     * file is not transmitted.
     */

    /**
     * If there is an issue opening the input stream, then the
     * file is not transmitted.
     */

    /**
     * If there is an issue connecting to the channel, then the
     * file is not transmitted.
     */

    /**
     * If there is an issue writing the file name, then the file 
     * is not transmitted.
     */

    /**
     * If there is an issue while sending the file contents, then
     * the file is not transmitted.
     */

    /**
     * If the file is not found while sending the file contents, then
     * the file is not transmitted.
     */

    /**
     * If the file is proper, and all connections established, then
     * the file is transmitted.
     */
}