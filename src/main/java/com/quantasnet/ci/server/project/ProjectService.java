package com.quantasnet.ci.server.project;

import com.quantasnet.ci.server.dependencies.docker.ContainerConfig;
import com.quantasnet.ci.server.dependencies.docker.DockerConnector;
import com.quantasnet.ci.server.exec.CommandExecutor;
import com.quantasnet.ci.server.scm.git.GitService;
import org.eclipse.jgit.lib.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ProjectService {

    private final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    private final DockerConnector dockerConnector;
    private final CommandExecutor commandExecutor;
    private final ProjectParser projectParser;
    private final GitService gitService;

    public ProjectService(final DockerConnector dockerConnector, final CommandExecutor commandExecutor, final ProjectParser projectParser, final GitService gitService) {
        this.dockerConnector = dockerConnector;
        this.commandExecutor = commandExecutor;
        this.projectParser = projectParser;
        this.gitService = gitService;
    }

    public void startProject(final String configLocation) {
        startProject(configLocation, false);
    }

    public void startProject(final String configLocation, final boolean classPath) {
        try {
            final Project project = projectParser.parseProject(configLocation, classPath);
            final Repository repo = gitService.getRepo(project.getRepoLocation() + "\\.git");

            gitService.startRepoWatcher(repo, () -> {
                logger.info("new commit!");

                project.getCommands().forEach(config -> {
                    try {
                        logger.info("Checking Docker Dependency!");
                        final ContainerConfig dockerContainerConfig = config.getDockerDependency();
                        if (null != dockerContainerConfig) {
                            dockerConnector.createOrGetContainer(dockerContainerConfig);
                        }

                        commandExecutor.execute(config.getName(), config.getCommand(), project.getRepoLocation());

                        if (null != dockerContainerConfig) {
                            logger.info("Stopping Docker Containers!");
                            dockerConnector.stopContainer(dockerContainerConfig);
                        }

                    } catch (final InterruptedException | IOException e) {
                        logger.error("Error running command!", e);
                    }
                });

                return null;
            });

        } catch (final IOException ioe) {
            logger.error("Error loading project file", ioe);
        }
    }

}
