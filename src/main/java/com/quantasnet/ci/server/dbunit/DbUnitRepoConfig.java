package com.quantasnet.ci.server.dbunit;

import com.quantasnet.ci.server.ExecConfiguration;
import com.quantasnet.ci.server.exec.CommandExecutor;
import com.quantasnet.ci.server.scm.git.GitService;
import com.quantasnet.ci.server.scm.git.RepoConfig;
import org.eclipse.jgit.lib.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Component
public class DbUnitRepoConfig {

    private final Logger logger = LoggerFactory.getLogger(DbUnitRepoConfig.class);

    @Value("${repoLocation}")
    private String repoLocation;

    @Autowired
    private GitService gitService;

    @Autowired
    private List<ExecConfiguration.ExecConfig> execCommands;

    @Autowired
    private CommandExecutor executor;

    @PostConstruct
    void postConstruct() throws IOException {

        final RepoConfig repoConfig = new RepoConfig();
        repoConfig.setGitFolder(repoLocation + "\\.git");

        final Repository repo = gitService.getRepo(repoConfig);

        // Execute
        gitService.startRepoWatcher(repo, () -> {
            logger.info("new commit!");

            execCommands.forEach(config -> {
                try {
                    executor.execute(config.getName(), config.getCommand(), repoLocation);
                } catch (final InterruptedException | IOException e) {
                    logger.error("Error running command!", e);
                }
            });

            return null;
        });
    }
}
