package com.quantasnet.ci.server;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "exec")
public class ExecConfiguration {

    private List<ExecConfig> commands;

    public List<ExecConfig> getCommands() {
        return commands;
    }

    public void setCommands(List<ExecConfig> commands) {
        this.commands = commands;
    }

    @Bean
    public List<ExecConfig> execConfig() {
        return commands;
    }

    public static class ExecConfig {
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
    }
}
