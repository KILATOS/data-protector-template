package org.masterleonardo.usersapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UsersApiApplication {
    public static void main(String[] args) {
        //TODO make log file rotatable
        SpringApplication.run(UsersApiApplication.class, args);
    }

}
