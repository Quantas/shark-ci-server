package com.quantasnet.ci.server.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/status")
public class BuildStatusController {

    @Autowired
    private BuildStatusService service;

    @GetMapping()
    public Map<String, BuildStatus> getStatus() {
        return service.currentStatus();
    }
}
