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
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.Container.ExecResult;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Application behaviour test that will test out sending the file to a
 * destination through SCP.
 */
@Testcontainers
@SpringBootTest(classes = { SpringBootDummyApp.class })
@ActiveProfiles("test")
public class ScpFileTransmitterAppBehaviourTest {

    private static final Logger LOG = LoggerFactory.getLogger(ScpFileTransmitterAppBehaviourTest.class);

    @Container
    private GenericContainer<?> scpRemoteContainer = new GenericContainer<>(
            new ImageFromDockerfile()
                                     .withFileFromClasspath("ssh_host_rsa_key", "/docker/ssh_host_rsa_key")
                                     .withFileFromClasspath("ssh_host_rsa_key.pub", "/docker/ssh_host_rsa_key.pub")
                                     .withFileFromClasspath("sshd_config", "/docker/sshd_config")
                                     .withFileFromClasspath("authorized_keys", "/docker/authorized_keys")
                                     .withFileFromClasspath("Dockerfile", "/docker/Dockerfile")
            )
                                                                            .withExposedPorts(22)
                                                                            .waitingFor(
                                                                                Wait.forLogMessage(".*Ready to accept connections.*\\n", 1)
                                                                            )
                                                                            .withLogConsumer(new Slf4jLogConsumer(LOG));
                                                                                                

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
        ExecResult lsResult = scpRemoteContainer.execInContainer("ls", "-al", "/tmp");
        String stdOut = lsResult.getStdout();
        assertTrue(stdOut.contains(localTxtFile));
    }
}