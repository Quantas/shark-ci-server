package com.quantasnet.ci.server.exec;

import com.quantasnet.ci.server.status.BuildStatus;
import com.quantasnet.ci.server.status.BuildStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class CommandExecutor {

    private final Logger logger = LoggerFactory.getLogger(CommandExecutor.class);

    @Autowired
    private BuildStatusService buildStatusService;

    public BuildStatus execute(final String name, final String command, final String workDir) throws IOException, InterruptedException {
        final Process process = Runtime.getRuntime().exec(command, null, new File(workDir));
        final BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String s;
        while ((s = stdInput.readLine()) != null) {
            logger.info("{} - {}", name, s);
        }

        final int returnValue = process.waitFor();

        final BuildStatus status;
        if (returnValue == 0) {
            status = BuildStatus.SUCCESS;
            logger.info("SUCCESS - {}", name);
        } else {
            status = BuildStatus.ERROR;
            logger.error("ERROR - {}", name);
        }

        buildStatusService.updateStatus(name, status);

        return status;
    }

}
