//package com.duckbill.cine_list;
//
//import io.github.cdimascio.dotenv.Dotenv;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class CineListApplicationTests {
//
//	@BeforeAll
//	static void setUp() {
//		// Carregar variáveis do .env no sistema
//		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
//		dotenv.entries().forEach(entry -> {
//			if (System.getProperty(entry.getKey()) == null) {
//				System.setProperty(entry.getKey(), entry.getValue());
//			}
//		});
//	}
//
//	@Test
//	void contextLoads() {
//		// Teste básico para verificar se o contexto carrega corretamente
//	}
//}