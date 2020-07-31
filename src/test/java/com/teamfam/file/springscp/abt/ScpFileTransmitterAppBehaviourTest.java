package com.teamfam.file.springscp.abt;

import com.teamfam.file.springscp.SpringBootDummyApp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Application behaviour test that will test out sending the file to a
 * destination through SCP.
 */
@SpringBootTest(classes={SpringBootDummyApp.class})
@ActiveProfiles("test")
public class ScpFileTransmitterAppBehaviourTest {
   
    @Test
    public void init(){
        
    }
}