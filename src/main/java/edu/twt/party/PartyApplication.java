package edu.twt.party;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
// import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;





@EnableTransactionManagement
@MapperScan(basePackages = "edu.twt.party.dao")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class PartyApplication {
    public static void main(String[] args) {
        SpringApplication.run(PartyApplication.class, args);
    }

}
