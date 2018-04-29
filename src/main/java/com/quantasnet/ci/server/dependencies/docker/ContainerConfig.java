package com.quantasnet.ci.server.dependencies.docker;

import java.util.ArrayList;
import java.util.List;

public class ContainerConfig {
    private String name;
    private String image;
    private List<String> cmds;
    private int internalPort;
    private String externalPort;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getCmds() {
        return cmds;
    }

    public void setCmds(List<String> cmds) {
        this.cmds = cmds;
    }

    public int getInternalPort() {
        return internalPort;
    }

    public void setInternalPort(int internalPort) {
        this.internalPort = internalPort;
    }

    public String getExternalPort() {
        return externalPort;
    }

    public void setExternalPort(String externalPort) {
        this.externalPort = externalPort;
    }

    @Override
    public String toString() {
        return "ContainerConfig{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", cmds=" + cmds +
                ", internalPort=" + internalPort +
                ", externalPort='" + externalPort + '\'' +
                '}';
    }
}
