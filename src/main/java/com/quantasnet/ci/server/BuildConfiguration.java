package com.quantasnet.ci.server;

import com.quantasnet.ci.server.dependencies.docker.ContainerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "docker")
public class BuildConfiguration {

    private List<ContainerConfig> containers;

    @Bean
    public List<ContainerConfig> containers() {
        return getContainers();
    }

    public List<ContainerConfig> getContainers() {
        return containers;
    }

    public void setContainers(List<ContainerConfig> containers) {
        this.containers = containers;
    }
}
