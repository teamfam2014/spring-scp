package com.teamfam.file.springscp.abt;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import com.teamfam.file.springscp.core.service.ScpService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.containers.Container.ExecResult;

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