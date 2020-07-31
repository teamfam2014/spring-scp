package com.teamfam.file.springscp.abt;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.teamfam.app.SpringBootDummyApp;
import com.teamfam.file.springscp.core.service.ScpService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Application behaviour test that will test out sending the file to a
 * destination through SCP.
 */
@Testcontainers
@SpringBootTest(classes={SpringBootDummyApp.class})
@ActiveProfiles("test")
public class ScpFileTransmitterAppBehaviourTest {

    @Container
    private GenericContainer<?> scpRemote = new GenericContainer<>(
        new ImageFromDockerfile()
            .withFileFromClasspath("Dockerfile", "images/Dockerfile")
    ).withExposedPorts(22);

    @Autowired
    private ScpService scpService;

    @Test
    public void init(){
        assertNotNull(scpService);
    }
}