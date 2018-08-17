package com.quantasnet.ci.server.project;

import com.quantasnet.ci.server.exec.ExecConfig;

import java.util.List;

public class Project {
    private String name;
    private String repoLocation;
    private List<ExecConfig> commands;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRepoLocation() {
        return repoLocation;
    }

    public void setRepoLocation(String repoLocation) {
        this.repoLocation = repoLocation;
    }

    public List<ExecConfig> getCommands() {
        return commands;
    }

    public void setCommands(List<ExecConfig> commands) {
        this.commands = commands;
    }

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", repoLocation='" + repoLocation + '\'' +
                ", commands=" + commands +
                '}';
    }
}
