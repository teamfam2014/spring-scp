package com.teamfam.file.springscp.abt;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import com.teamfam.app.SpringBootDummyApp;
import com.teamfam.file.springscp.core.service.ScpService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.Container.ExecResult;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Application behaviour test that will test out sending the file to a
 * destination through SCP.
 */
public class ScpFileTransmitterAppBehaviourTest extends AbstractApplicationBehaviourTest{

    @Autowired
    private ScpService scpService;

    /**
     * Send a local text file to the folder in the container.
     * 
     * @throws InterruptedException
     * @throws IOException
     * @throws UnsupportedOperationException
     */
    @DisplayName("Send Text File to Container")
    @Test
    public void sendTextFile() throws UnsupportedOperationException, IOException, InterruptedException {
        // ARRANGE
        String localTxtFile = "localfile.txt";
        // ACT
        boolean transmitted = scpService.scpFile(localTxtFile);
        // ASSERT
        assertTrue(transmitted);
        ExecResult lsResult = super.scpRemoteContainer.execInContainer("ls", "-al", "/tmp");
        String stdOut = lsResult.getStdout();
        assertTrue(stdOut.contains(localTxtFile));
    }    
}