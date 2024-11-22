package com.duckbill.cine_list;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CineListApplication {

	public static void main(String[] args) {
		// Carregar as variáveis do .env, se disponível
		Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing() // Ignora se o arquivo .env não estiver presente
				.load();

		// Configurar as variáveis do .env no System Properties, sem sobrescrever existentes
		dotenv.entries().stream()
				.filter(entry -> System.getProperty(entry.getKey()) == null) // Evita sobrescrever variáveis existentes
				.forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		// Inicializar a aplicação Spring Boot
		SpringApplication.run(CineListApplication.class, args);
	}
}