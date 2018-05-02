package com.quantasnet.ci.server.dependencies.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class DockerConnector {

    private final Logger logger = LoggerFactory.getLogger(DockerConnector.class);

    @Value("${docker.host}")
    private String dockerHost;

    @Value("${docker.tls-verify}")
    private boolean tlsVerify;

    @Value("${docker.sleep-time}")
    private long sleepTime;

    private DockerClient dockerClient;

    @PostConstruct
    void postConstruct() {
        final DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(dockerHost)
                .withDockerTlsVerify(tlsVerify)
                .build();

        dockerClient = DockerClientBuilder.getInstance(config).build();
    }

    public void stopContainer(final ContainerConfig config) {
        logger.info("Stopping - {}", config.getName());
        dockerClient.stopContainerCmd(config.getName()).exec();
        logger.info("Removing - {}", config.getName());
        dockerClient.removeContainerCmd(config.getName()).exec();
    }

    public Container createOrGetContainer(final ContainerConfig config) {
        final List<Container> containersBefore = dockerClient.listContainersCmd().withShowAll(true).exec();
        final Optional<Container> theContainer = containersBefore.stream().filter(container -> Arrays.asList(container.getNames()).contains("/" + config.getName())).findFirst();
        if (!theContainer.isPresent()) {
            logger.info("Starting new container - {}", config.getName());

            ExposedPort port = null;
            Ports portBindings = null;

            if (null != config.getExternalPort()) {
                port = ExposedPort.tcp(config.getInternalPort());
                portBindings = new Ports();
                portBindings.bind(port, new Ports.Binding(null, config.getExternalPort()));
            }

            CreateContainerCmd container = dockerClient.createContainerCmd(config.getImage())
                .withExposedPorts(port)
                .withPortBindings(portBindings)
                .withName(config.getName())
                .withTty(true);

            if (config.getCmds() != null) {
                container = container.withCmd(config.getCmds());
            }

            final CreateContainerResponse response = container.exec();

            dockerClient.startContainerCmd(response.getId()).exec();
            sleepSilently();

            logger.info("Started Container - {}", config.getName());
            return dockerClient.listContainersCmd().exec().stream().filter(newContainer -> newContainer.getId().equals(response.getId())).findFirst().get();
        }

        logger.info("Found Container - {}", config.getName());
        final Container existingContainer = theContainer.get();
        logger.info("Existing Container is - {}", existingContainer.getStatus());

        if (!existingContainer.getStatus().startsWith("Up ")) {
            logger.info("Starting existing container - {}", config.getName());
            dockerClient.startContainerCmd(existingContainer.getId()).exec();
            sleepSilently();

        }
        return existingContainer;
    }

    private void sleepSilently() {
        try {
            Thread.sleep(sleepTime);
        } catch (final InterruptedException e) {
        }
    }

}
