package com.freelancehub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class FreelanceHubApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(FreelanceHubApplication.class, args);
        Environment env = context.getEnvironment();
        String port = env.getProperty("server.port", "8080");

        System.out.println("\n----------------------------------------------------------");
        System.out.println("  🚀 FreelanceHub Backend is running!");
        System.out.println("  🔗 Local URL:   http://localhost:" + port);
        System.out.println("  🔗 API Test:    http://localhost:" + port + "/api/projects");
        System.out.println("  💻 Frontend:    http://localhost:5173");
        System.out.println("----------------------------------------------------------\n");
    }

}