package dev.erikmota.desafiounikamain;

import dev.erikmota.desafiounikamain.view.DashboardPage;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DesafioUnikaMainApplication{
    public static void main(String[] args) {
        SpringApplication.run(DesafioUnikaMainApplication.class, args);
    }
}
