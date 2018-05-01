package com.quantasnet.ci.server.exec;

public class ExecConfig {
    private String name;
    private String command;

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

    @Override
    public String toString() {
        return "ExecConfig{" +
                "name='" + name + '\'' +
                ", command='" + command + '\'' +
                '}';
    }
}
