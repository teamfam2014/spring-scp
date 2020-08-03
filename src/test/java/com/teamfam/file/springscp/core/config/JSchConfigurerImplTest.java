package com.teamfam.file.springscp.core.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
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
     * If the public and private key file path and the key password are empty, then do not populate the
     * JSch identity
     * 
     * @throws JSchException
     */
    @DisplayName("Pub and Priv Key File Path, and Key Password empty")
    @Test
    public void allEmpty() throws JSchException {
        //ARRANGE
        JSch jsch = mock(JSch.class);
        //ACT
        jschConfigurerImpl.configure(jsch);
        //ASSERT
        verify(jsch,times(0)).addIdentity(anyString(),anyString(),any(byte[].class));
        verify(jsch,times(0)).addIdentity(anyString(),anyString());
    }

    /**
     * If private key is populated, but public key is not populated, then do not populate
     * anything.
     */
    @DisplayName("Priv Key File Path populated only")
    @Test
    public void prvPopulated() throws JSchException {
        //ARRANGE
        JSch jsch = mock(JSch.class);
        when(scpConfigurationProperties.getPrvKeyFilePath())
        .thenReturn("docker/ssh_host_rsa_key");          
        //ACT
        jschConfigurerImpl.configure(jsch);
        //ASSERT
        verify(jsch,times(0)).addIdentity(anyString(),anyString(),any(byte[].class));
        verify(jsch,times(0)).addIdentity(anyString(),anyString());
    }    
    
    /**
     * If public key is populated, but private key is not populated, then do not populate
     * anything.
     */
    @DisplayName("Pub Key File Path populated only")
    @Test
    public void pubPopulated() throws JSchException {
        //ARRANGE
        JSch jsch = mock(JSch.class);
        lenient().when(scpConfigurationProperties.getPubKeyFilePath())
        .thenReturn("docker/ssh_host_rsa_key.pub");          
        //ACT
        jschConfigurerImpl.configure(jsch);
        //ASSERT
        verify(jsch,times(0)).addIdentity(anyString(),anyString(),any(byte[].class));
        verify(jsch,times(0)).addIdentity(anyString(),anyString());
    }    

    /**
     * If the private and public key file path is populated but the key password is empty
     * then only populate the JSch identity with the key file path.
     */
    @DisplayName("Public and Private Key File Path populated only")
    @Test
    public void keyFilePathsPopulated() throws JSchException {
        //ARRANGE
        JSch jsch = mock(JSch.class);
        when(scpConfigurationProperties.getPrvKeyFilePath())
        .thenReturn("docker/ssh_host_rsa_key");        
        when(scpConfigurationProperties.getPubKeyFilePath())
        .thenReturn("docker/ssh_host_rsa_key.pub");
        //ACT
        jschConfigurerImpl.configure(jsch);
        //ASSERT
        verify(jsch,times(0)).addIdentity(anyString(),anyString(),any(byte[].class));
        verify(jsch,times(1)).addIdentity(anyString(),anyString());
    }    

    /**
     * If the public and private key file path and the key password are populated then
     * populate both in the JSch identify.
     */
    @DisplayName("All Paths and Pwd populated")
    @Test
    public void allPopulated() throws JSchException {
        //ARRANGE
        JSch jsch = mock(JSch.class);
        when(scpConfigurationProperties.getPrvKeyFilePath())
        .thenReturn("docker/ssh_host_rsa_key");        
        when(scpConfigurationProperties.getPubKeyFilePath())
        .thenReturn("docker/ssh_host_rsa_key.pub");
        when(scpConfigurationProperties.getKeyPassword())
        .thenReturn("test-Pwd");        
        //ACT
        jschConfigurerImpl.configure(jsch);
        //ASSERT
        verify(jsch,times(1)).addIdentity(anyString(),anyString(),any(byte[].class));
        verify(jsch,times(0)).addIdentity(anyString(),anyString());
    }    
}