package com.quantasnet.ci.server.status;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BuildStatusService {

    private Map<String, BuildStatus> statusMap = new HashMap<>();

    public void updateStatus(final String name, final BuildStatus status) {
        statusMap.put(name, status);
    }

    Map<String, BuildStatus> currentStatus() {
        return statusMap;
    }
}
