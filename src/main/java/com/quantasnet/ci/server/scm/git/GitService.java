package com.quantasnet.ci.server.scm.git;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

@Component
public class GitService {

    private Logger logger = LoggerFactory.getLogger(GitService.class);

    public Repository getRepo(final String gitFolder) throws IOException {
        final FileRepositoryBuilder repoBuilder = new FileRepositoryBuilder();
        repoBuilder.setMustExist(true);
        repoBuilder.setGitDir(new File(gitFolder));
        return repoBuilder.build();
    }

    public void startRepoWatcher(final Repository repo, final Callable<Void> callback) {
        new Thread(() -> {
            String headHash = "";
            boolean running = true;

            while (running) {
                try {
                    final Ref headRef = repo.findRef(repo.getFullBranch());
                    final String newHeadHash = headRef.getObjectId().name();
                    if (!newHeadHash.equals(headHash)) {
                        headHash = newHeadHash;
                        logger.info("HEAD changed -> {}", newHeadHash);
                        callback.call();
                    } else {
                        logger.info("HEAD did not change -> {}", newHeadHash);
                    }

                    Thread.sleep(10000);
                } catch (final InterruptedException ie) {
                    logger.info("Shutting Down");
                    running = false;
                } catch (final Exception e) {
                    logger.error("Exception! ", e);
                }
            }
        }).start();
    }
}
