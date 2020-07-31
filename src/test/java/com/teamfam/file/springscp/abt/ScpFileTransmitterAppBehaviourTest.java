package com.teamfam.file.springscp.abt;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.teamfam.app.SpringBootDummyApp;
import com.teamfam.file.springscp.core.service.ScpService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Application behaviour test that will test out sending the file to a
 * destination through SCP.
 */
@SpringBootTest(classes={SpringBootDummyApp.class})
@ActiveProfiles("test")
public class ScpFileTransmitterAppBehaviourTest {

    @Autowired
    private ScpService scpService;

    @Test
    public void init(){
        assertNotNull(scpService);
    }
}