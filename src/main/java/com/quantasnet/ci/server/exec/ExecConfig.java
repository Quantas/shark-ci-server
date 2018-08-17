package com.quantasnet.ci.server.exec;

import com.quantasnet.ci.server.dependencies.docker.ContainerConfig;

public class ExecConfig {
    private String name;
    private String command;
    private ContainerConfig dockerDependency;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public ContainerConfig getDockerDependency() {
        return dockerDependency;
    }

    public void setDockerDependency(ContainerConfig dockerDependency) {
        this.dockerDependency = dockerDependency;
    }

    @Override
    public String toString() {
        return "ExecConfig{" +
                "name='" + name + '\'' +
                ", command='" + command + '\'' +
                '}';
    }
}
