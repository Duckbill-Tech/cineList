package com.duckbill.cine_list;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CineListApplication {

	public static void main(String[] args) {
		// Carrega as variáveis do arquivo .env
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

		// Adiciona cada variável do .env no System para o Spring poder utilizá-las
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		SpringApplication.run(CineListApplication.class, args);
	}
}