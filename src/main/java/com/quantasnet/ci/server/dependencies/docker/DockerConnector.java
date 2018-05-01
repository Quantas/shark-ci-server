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

@Component
public class DockerConnector {

    private final Logger logger = LoggerFactory.getLogger(DockerConnector.class);

    @Value("${docker.host}")
    private String dockerHost;

    @Value("${docker.tls-verify}")
    private boolean tlsVerify;

    private DockerClient dockerClient;

    @PostConstruct
    void postConstruct() {
        final DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(dockerHost)
                .withDockerTlsVerify(tlsVerify)
                .build();

        dockerClient = DockerClientBuilder.getInstance(config).build();
    }

    public void shutdown(final ContainerConfig config) {
        logger.info("Stopping {}", config.getName());
        dockerClient.stopContainerCmd(config.getName()).exec();
        logger.info("Removing {}", config.getName());
        dockerClient.removeContainerCmd(config.getName()).exec();
    }

    public Container createOrGetContainer(final ContainerConfig config) {
        final List<Container> containersBefore = dockerClient.listContainersCmd().withShowAll(true).exec();

        if (containersBefore.stream().filter(container -> Arrays.asList(container.getNames()).contains("/" + config.getName())).count() < 1) {
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

            try {
                Thread.sleep(10000);
            } catch (final InterruptedException e) {

            }

            logger.info("Started Container - {}", config.getName());
        }

        final List<Container> containers = dockerClient.listContainersCmd().withShowAll(true).exec();
        logger.info("Found Container - {}", config.getName());
        return containers.stream().filter(container -> Arrays.asList(container.getNames()).contains("/" + config.getName())).findFirst().get();
    }
}
