package com.teamfam.file.springscp.transmit;

import com.teamfam.file.springscp.config.ScpAutoConfiguration;
import com.teamfam.file.springscp.config.ScpConfigurationProperties;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Validate that the JSch bean is properly tested when configured.
 * Given that we cannot test utlizing the keyfile, we should ensure
 * that when the bean is created, that the keys are validated.
 * 
 * @author teamfam
 */
@ExtendWith(MockitoExtension.class)
public class ScpAutoConfigurationTest {
    
    @InjectMocks
    private ScpAutoConfiguration ScpAutoConfiguration;

    @Mock
    private ScpConfigurationProperties scpConfigurationProperties;
}