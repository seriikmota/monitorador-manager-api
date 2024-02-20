package dev.erikmota.desafiounika;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "API Unika", version = "1", description = "API desenvolvida para controle de monitoradores e seus endereços"))
public class DesafioUnikaMainApplication{
    public static void main(String[] args) {
        SpringApplication.run(DesafioUnikaMainApplication.class, args);
    }
}
