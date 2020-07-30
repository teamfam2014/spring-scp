package com.teamfam.file.springscp.transmit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.teamfam.file.springscp.config.ScpConfigurationProperties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
     * If there is an issue obtaining the session, then the file is not transmitted.
     * 
     * @throws JSchException
     * @throws NumberFormatException
     */
    @DisplayName("Issue Obtaining Session")
    @Test
    public void obtainSessionIssue() throws NumberFormatException, JSchException {
        //ARRANGE
        File fileToTransmit = mock(File.class);
        mockBaseScpProperties();
        when(jsch.getSession(scpConfigProps.getUserName(),scpConfigProps.getHost(),Integer.parseInt(scpConfigProps.getPort())))
        .thenThrow(JSchException.class);
        //ACT
        boolean transmitted = scpFileTransmitter.transmit(fileToTransmit);
        //ASSERT
        assertFalse(transmitted);
    }

    /**
     * If there is an issue connecting to the session, then
     * the file is not transmitted.
     */
    @DisplayName("Issue Connecting to Session")
    @Test
    public void connectSessionIssue() throws NumberFormatException, JSchException {
        //ARRANGE
        File fileToTransmit = mock(File.class);
        mockBaseScpProperties();
        Session mockSession = mock(Session.class);
        mockSessionCreation(mockSession);
        doThrow(JSchException.class)
        .when(mockSession).connect();
        //ACT
        boolean transmitted = scpFileTransmitter.transmit(fileToTransmit);
        //ASSERT
        assertFalse(transmitted);
    }

    /**
     * If there is an issue opening the channel, then the
     * file is not transmitted.
     */
    @DisplayName("Issue Opening Channel")
    @Test
    public void openingChannelIssue() throws NumberFormatException, JSchException {
        //ARRANGE
        File fileToTransmit = mock(File.class);
        mockBaseScpProperties();
        Session mockSession = mock(Session.class);
        mockSessionCreation(mockSession);
        Channel mockChannel = mock(Channel.class);
        mockChannelCreation(mockChannel, mockSession);
        doThrow(JSchException.class)
        .when(mockSession).openChannel(anyString());
        //ACT
        boolean transmitted = scpFileTransmitter.transmit(fileToTransmit);
        //ASSERT
        assertFalse(transmitted);
    }    

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

    private void mockBaseScpProperties(){
        Map<String,String> sessionProps = new HashMap<String,String>();
        sessionProps.put("StrictHostKeyChecking", "no");
        String userName = "test";
        String host = "localhost";
        String port = "22";
        
        when(scpConfigProps.getHost())
        .thenReturn(host);
        when(scpConfigProps.getUserName())
        .thenReturn(userName);
        when(scpConfigProps.getPort())
        .thenReturn(port);
        when(scpConfigProps.getSessionConfigs())
        .thenReturn(sessionProps);
    }

    private void mockSessionCreation(Session mockSession) throws NumberFormatException, JSchException {
        when(jsch.getSession(scpConfigProps.getUserName(),scpConfigProps.getHost(),Integer.parseInt(scpConfigProps.getPort())))
        .thenReturn(mockSession);
    }

    private void mockChannelCreation(Channel mockChannel, Session mockSession) throws JSchException {
        when(mockSession.openChannel(anyString()))
        .thenReturn(mockChannel);
    }
}