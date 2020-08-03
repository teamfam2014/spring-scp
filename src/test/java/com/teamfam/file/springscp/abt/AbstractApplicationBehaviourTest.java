package com.teamfam.file.springscp.abt;

import com.teamfam.app.SpringBootDummyApp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Abstract application behaviour test which initializes the docker container
 * and sets the properties to use.
 */
@Testcontainers
@SpringBootTest(classes = { SpringBootDummyApp.class })
@ContextConfiguration(initializers = AbstractApplicationBehaviourTest.Initializer.class)
@ActiveProfiles("test")
public abstract class AbstractApplicationBehaviourTest {
    
    private static final Logger LOG = LoggerFactory.getLogger(AbstractApplicationBehaviourTest.class);

    @Container
    protected GenericContainer<?> scpRemoteContainer = new GenericContainer<>(
            new ImageFromDockerfile()
                                     .withFileFromClasspath("ssh_host_rsa_key", "/docker/ssh_host_rsa_key")
                                     .withFileFromClasspath("ssh_host_rsa_key.pub", "/docker/ssh_host_rsa_key.pub")
                                     .withFileFromClasspath("sshd_config", "/docker/sshd_config")
                                     .withFileFromClasspath("authorized_keys", "/docker/authorized_keys")
                                     .withFileFromClasspath("Dockerfile", "/docker/Dockerfile")
            )
                                                                            .withExposedPorts(22)
                                                                            .withLogConsumer(new Slf4jLogConsumer(LOG));

    public class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    "scp.host=" + scpRemoteContainer.getHost(),
                    "scp.port=" + scpRemoteContainer.getFirstMappedPort()
            );
            values.applyTo(configurableApplicationContext);
        }
    }
}