package com.quantasnet.ci.server;

import com.quantasnet.ci.server.project.ProjectService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SharkCiServerApplication {

	public static void main(String[] args) {
		final ConfigurableApplicationContext context = SpringApplication.run(SharkCiServerApplication.class, args);
		context.registerShutdownHook();

		context.getBean(ProjectService.class).startProject("sharkci.yml", true);

	}
}
