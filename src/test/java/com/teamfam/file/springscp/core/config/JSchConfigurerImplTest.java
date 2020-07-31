package com.teamfam.file.springscp.core.config;

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
    private JSchConfigurer jschConfigurer;

    @Mock
    private ScpConfigurationProperties scpConfigurationProperties;

    /**
     * If the key file path and the key password are empty, then
     * do not populate the JSch identity
     */

    /**
     * If the key file path is populated but the key password is empty
     * then only populate the JSch identity with the key file path.
     */

    /**
     * If the key file path and the key password are populated then
     * populate both in the JSch identify.
     */
}