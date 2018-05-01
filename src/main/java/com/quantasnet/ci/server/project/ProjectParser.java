package com.quantasnet.ci.server.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;

@Component
public class ProjectParser {

    private Logger logger = LoggerFactory.getLogger(ProjectParser.class);

    public Project parseProject(final String configLocation) throws IOException {
        return parseProject(configLocation, false);
    }

    public Project parseProject(final String configLocation, final boolean classPath) throws IOException {
        final Yaml yaml = new Yaml();
        final Resource resource = classPath ? new ClassPathResource(configLocation) : new FileSystemResource(configLocation);
        final Project project = yaml.loadAs(resource.getInputStream(), Project.class);
        logger.info("Parsed Project = {}", project);
        return project;
    }
}
