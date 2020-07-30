package com.teamfam.file.springscp.transmit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.teamfam.file.springscp.config.ScpConfigurationProperties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
        doThrow(JSchException.class)
        .when(mockSession).openChannel(anyString());
        //ACT
        boolean transmitted = scpFileTransmitter.transmit(fileToTransmit);
        //ASSERT
        assertFalse(transmitted);
    }    

    /**
     * If there is an issue opening the output stream, then the file is not
     * transmitted.
     * 
     * @throws IOException
     */
    @DisplayName("Issue getting Output Stream")
    @Test
    public void openingOSIssue() throws NumberFormatException, JSchException, IOException {
        //ARRANGE
        File fileToTransmit = mock(File.class);
        mockBaseScpProperties();
        Session mockSession = mock(Session.class);
        mockSessionCreation(mockSession);
        ChannelExec mockChannel = mock(ChannelExec.class);
        mockChannelCreation(mockChannel, mockSession);
        doThrow(IOException.class)
        .when(mockChannel).getOutputStream();
        //ACT
        boolean transmitted = scpFileTransmitter.transmit(fileToTransmit);
        //ASSERT
        assertFalse(transmitted);
    }

    /**
     * If there is an issue opening the input stream, then the
     * file is not transmitted.
     */
    @DisplayName("Issue getting Input Stream")
    @Test
    public void openingISIssue() throws NumberFormatException, JSchException, IOException {
        //ARRANGE
        File fileToTransmit = mock(File.class);
        mockBaseScpProperties();
        Session mockSession = mock(Session.class);
        mockSessionCreation(mockSession);
        ChannelExec mockChannel = mock(ChannelExec.class);
        mockChannelCreation(mockChannel, mockSession);
        doThrow(IOException.class)
        .when(mockChannel).getInputStream();
        //ACT
        boolean transmitted = scpFileTransmitter.transmit(fileToTransmit);
        //ASSERT
        assertFalse(transmitted);
    }    

    /**
     * If there is an issue connecting to the channel, then the
     * file is not transmitted.
     */
    @DisplayName("Issue Connecting to Channel")
    @Test
    public void connectChannelIssue() throws NumberFormatException, JSchException {
        //ARRANGE
        File fileToTransmit = mock(File.class);
        mockBaseScpProperties();
        Session mockSession = mock(Session.class);
        mockSessionCreation(mockSession);
        ChannelExec mockChannel = mock(ChannelExec.class);
        mockChannelCreation(mockChannel, mockSession);
        doThrow(JSchException.class)
        .when(mockChannel).connect();
        //ACT
        boolean transmitted = scpFileTransmitter.transmit(fileToTransmit);
        //ASSERT
        assertFalse(transmitted);
    }     

    /**
     * If there is an issue writing the file name, then the file is not transmitted.
     * 
     * @throws IOException
     */
    @DisplayName("Issue Writing Filename")
    @Test
    public void writingFileNameIssue() throws NumberFormatException, JSchException, IOException {
        //ARRANGE
        File fileToTransmit = mock(File.class, Mockito.RETURNS_DEEP_STUBS);
        mockFilePath(fileToTransmit);
        mockBaseScpProperties();
        Session mockSession = mock(Session.class);
        mockSessionCreation(mockSession);
        ChannelExec mockChannel = mock(ChannelExec.class);
        mockChannelCreation(mockChannel, mockSession);
        OutputStream out = mock(OutputStream.class);
        InputStream in = mock(InputStream.class);
        mockStreams(in,out,mockChannel);
        doThrow(IOException.class)
        .when(out).write(any());
        //ACT
        boolean transmitted = scpFileTransmitter.transmit(fileToTransmit);
        //ASSERT
        assertFalse(transmitted);
    }    

    /**
     * If the file is not found while sending the file contents, then
     * the file is not transmitted.
     */
    @DisplayName("Issue because File Not Found")
    @Test
    public void fileNotFoundIssue() throws NumberFormatException, JSchException, IOException {
        //ARRANGE
        File fileToTransmit = mockFileStubInvalid();
        mockBaseScpProperties();
        Session mockSession = mock(Session.class);
        mockSessionCreation(mockSession);
        ChannelExec mockChannel = mock(ChannelExec.class);
        mockChannelCreation(mockChannel, mockSession);
        OutputStream out = mock(OutputStream.class);
        InputStream in = mock(InputStream.class);
        mockStreams(in,out,mockChannel);
        mockAckFailOnContentSend(in);
        //ACT
        boolean transmitted = scpFileTransmitter.transmit(fileToTransmit);
        //ASSERT
        assertFalse(transmitted);
    }     

    /**
     * If there is an issue while sending the file contents, then the file is not
     * transmitted.
     */
    @DisplayName("Issue Sending File Contents")
    @Test
    public void writingFileContentsIssue() throws NumberFormatException, JSchException, IOException {
        //ARRANGE
        File fileToTransmit = mockFileStubValid();
        mockBaseScpProperties();
        Session mockSession = mock(Session.class);
        mockSessionCreation(mockSession);
        ChannelExec mockChannel = mock(ChannelExec.class);
        mockChannelCreation(mockChannel, mockSession);
        OutputStream out = mock(OutputStream.class);
        InputStream in = mock(InputStream.class);
        mockStreams(in,out,mockChannel);
        doNothing()
        .when(out).write(any(byte[].class));
        doThrow(IOException.class)
        .when(out).write(any(byte[].class),anyInt(),anyInt());
        //ACT
        boolean transmitted = scpFileTransmitter.transmit(fileToTransmit);
        //ASSERT
        assertFalse(transmitted);
        verify(out,times(1)).write(any(byte[].class),anyInt(),anyInt());
        verify(out,times(1)).write(any(byte[].class));
    }    

    /**
     * If the file is proper, and all connections established, then
     * the file is transmitted.
     */
    @DisplayName("Successful File Send")
    @Test
    public void scpFile() throws NumberFormatException, JSchException, IOException {
        //ARRANGE
        File fileToTransmit = mockFileStubValid();
        mockBaseScpProperties();
        Session mockSession = mock(Session.class);
        mockSessionCreation(mockSession);
        ChannelExec mockChannel = mock(ChannelExec.class);
        mockChannelCreation(mockChannel, mockSession);
        OutputStream out = mock(OutputStream.class);
        InputStream in = mock(InputStream.class);
        mockStreams(in,out,mockChannel);
        //ACT
        boolean transmitted = scpFileTransmitter.transmit(fileToTransmit);
        //ASSERT
        assertTrue(transmitted);
        verify(out,times(1)).write(any(byte[].class),anyInt(),anyInt());
        verify(out,times(1)).write(any(byte[].class));
        verify(in,times(2)).read();
    }

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

    private void mockStreams(InputStream in, OutputStream out, Channel channel) throws IOException {
        when(channel.getInputStream())
        .thenReturn(in);
        when(channel.getOutputStream())
        .thenReturn(out);
    }

    private void mockFilePath(File mockFile){
        when(mockFile.getAbsolutePath())
        .thenReturn("/some/path/myFile.txt");
    }

    private File mockFileStubInvalid(){
        return new File(this.getClass().getName(),Long.toString(System.currentTimeMillis()));
    }

    private File mockFileStubValid() throws IOException {
        return File.createTempFile("stub", ".txt");

    }

    private void mockAckFailOnContentSend(InputStream in) throws IOException {
        when(in.read())
        .thenReturn(0)
        .thenReturn(2)
        .thenReturn(10); //10 represents the `\n` character
    }    
}