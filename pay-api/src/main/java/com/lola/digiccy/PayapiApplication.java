package com.lola.digiccy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author MECHREVO
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class PayapiApplication {
    public static void main(String[] args) {
            SpringApplication.run(PayapiApplication.class, args);

    }
}
