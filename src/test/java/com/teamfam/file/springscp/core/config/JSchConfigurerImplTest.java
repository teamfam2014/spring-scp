package com.teamfam.file.springscp.core.config;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * We need to validate that the JSch configurations work as expected.
 */
@ExtendWith(MockitoExtension.class)
public class JSchConfigurerImplTest {

    @InjectMocks
    private JSchConfigurerImpl jschConfigurerImpl;

    @Mock
    private ScpConfigurationProperties scpConfigurationProperties;

    /**
     * If the key file path and the key password are empty, then do not populate the
     * JSch identity
     * 
     * @throws JSchException
     */
    @DisplayName("Key File Path and Key Password empty")
    @Test
    public void bothEmpty() throws JSchException {
        //ARRANGE
        JSch jsch = mock(JSch.class);
        //ACT
        jschConfigurerImpl.configure(jsch);
        //ASSERT
        verify(jsch,times(0)).addIdentity(anyString(),anyString());
        verify(jsch,times(0)).addIdentity(anyString());
    }

    /**
     * If the key file path is populated but the key password is empty
     * then only populate the JSch identity with the key file path.
     */
    @DisplayName("Key File Path populated only")
    @Test
    public void keyFilePathPopulated() throws JSchException {
        //ARRANGE
        JSch jsch = mock(JSch.class);
        when(scpConfigurationProperties.getKeyFilePath())
        .thenReturn("test-path");
        //ACT
        jschConfigurerImpl.configure(jsch);
        //ASSERT
        verify(jsch,times(0)).addIdentity(anyString(),anyString());
        verify(jsch,times(1)).addIdentity(anyString());
    }    

    /**
     * If the key file path and the key password are populated then
     * populate both in the JSch identify.
     */
    @DisplayName("Key File Path and Key Password populated")
    @Test
    public void bothPopulated() throws JSchException {
        //ARRANGE
        JSch jsch = mock(JSch.class);
        when(scpConfigurationProperties.getKeyFilePath())
        .thenReturn("test-path");
        when(scpConfigurationProperties.getKeyPassword())
        .thenReturn("test-Pwd");        
        //ACT
        jschConfigurerImpl.configure(jsch);
        //ASSERT
        verify(jsch,times(1)).addIdentity(anyString(),anyString());
        verify(jsch,times(0)).addIdentity(anyString());
    }    
}