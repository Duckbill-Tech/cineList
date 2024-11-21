package com.duckbill.cine_list;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.duckbill.cine_list")
public class CineListApplication {

	public static void main(String[] args) {
		// Carregar as variáveis do .env, ignorando se o arquivo não estiver presente
		Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing()
				.load();

		// Carregar as variáveis do .env no System Properties (se ainda não estiverem definidas)
		dotenv.entries().forEach(entry -> {
			if (System.getProperty(entry.getKey()) == null) {
				System.setProperty(entry.getKey(), entry.getValue());
			}
		});
		SpringApplication.run(CineListApplication.class, args);
	}
}