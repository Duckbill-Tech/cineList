# Desenvolvimento de REST em java

## Usando o spring boot:

springboot: https://www.youtube.com/watch?v=GEvpC0DZSL4
https://dev.to/iamjose/como-iniciar-um-aplicativo-spring-boot-jpa-mysql-2nl4

## Rodar migrations:

migrations: https://medium.com/version-1/db-migrations-in-spring-boot-7d48e5e18738

https://medium.com/version-1/db-migrations-in-spring-boot-7d48e5e18738


```
mvn clean
mvn compile
```

---
com o warning de versão:
```
mvn flyway:migrate -Dflyway.url=jdbc:mysql://localhost:3306/cinelist -Dflyway.user=cinelist -Dflyway.password=cinelist -Dflyway.locations=classpath:/db/migration

```

Usando o -q para silenciar warnings:
```
mvn -q  flyway:migrate -Dflyway.url=jdbc:mysql://localhost:3306/cinelist -Dflyway.user=cinelist -Dflyway.password=cinelist -Dflyway.locations=classpath:/db/migration


```

## Service:

O FilmeService é responsável por realizar operações CRUD para a entidade Filme. Ele utiliza o repositório FilmeRepository para interagir com o banco de dados.


### Método create
Este método salva um novo objeto Filme no banco de dados:

Recebe um objeto Filme como parâmetro.
Usa filmeRepository.save(filme) para salvar o filme no banco de dados e retorna o objeto salvo (incluindo o id gerado pelo banco).


### Método getById

Este método busca um filme pelo id:

Recebe o id (UUID) do filme que queremos buscar.
filmeRepository.findById(id) retorna um Optional<Filme>, que representa o filme encontrado ou vazio, caso o filme não exista.


Método getAll

Este método retorna todos os filmes da base de dados:

Usa filmeRepository.findAll() para buscar todos os filmes.
Retorna uma List<Filme> contendo todos os registros da tabela filme.


### SEGURANÇA


- UUID:
  O uso de UUIDs (Universally Unique Identifiers) em uma aplicação traz várias vantagens, especialmente em contextos onde a escalabilidade, a segurança e a integridade dos dados são fundamentais. Aqui estão algumas das principais vantagens:

Unicidade em Sistemas Distribuídos:

UUIDs são projetados para serem únicos em sistemas distribuídos, garantindo que, mesmo em diferentes instâncias de banco de dados ou servidores, não haja conflito de IDs.
Isso é especialmente útil em microsserviços ou sistemas com múltiplos bancos de dados, onde IDs numéricos simples (autoincrementais) podem colidir se sincronizados entre diferentes bancos de dados.
Escalabilidade e Independência de Banco de Dados:

Por serem gerados no nível da aplicação, UUIDs permitem que os dados sejam transferidos entre bancos de dados e ambientes sem preocupação com conflitos de IDs.
Ao contrário dos IDs sequenciais, UUIDs podem ser gerados fora do banco, o que permite maior escalabilidade e independência em sistemas onde o banco não precisa controlar a geração do identificador.
Maior Segurança e Dificuldade de Previsão:

IDs sequenciais, como 1, 2, 3, ..., facilitam a inferência de dados, pois podem revelar a quantidade de registros ou ser previsíveis. Isso pode ser um problema em APIs ou URLs públicas, onde um usuário pode tentar acessar registros que não deveria.
Com UUIDs, é muito mais difícil para um atacante prever ou inferir outros registros, aumentando a segurança do sistema.
Facilidade de Integração e Sincronização de Dados:

Em cenários onde dados precisam ser sincronizados entre diferentes sistemas (ex: replicação entre bancos de dados ou backup), UUIDs tornam a integração mais fácil e evitam conflitos.
Persistência Independente do Banco de Dados:

UUIDs são independentes de um banco de dados específico, permitindo migrações mais fáceis entre diferentes tipos de banco (como SQL para NoSQL) sem a necessidade de alterar os identificadores.

- Variáveis de ambiente (application.properties, informações de banco de dados)

Usar um arquivo .env para armazenar variáveis de ambiente em uma aplicação traz várias vantagens, especialmente para o gerenciamento de configurações em diferentes ambientes (desenvolvimento, teste, produção).

Informações sensíveis, como credenciais de bancos de dados, chaves de API e tokens de autenticação, podem ser armazenadas em um arquivo .env, mantendo-as fora do código. Isso reduz o risco de que informações confidenciais sejam expostas em repositórios de código, especialmente se o .env estiver incluído no .gitignore.



## Testes

Fazemos testes que simulam erros para garantir que a aplicação lide adequadamente com situações inesperadas e com entradas de dados incorretas. Esses testes, conhecidos como testes de erro ou testes de cenários negativos, têm várias finalidades importantes:
1.	Verificar Tratamento de Exceções: Testes de erro ajudam a confirmar que o código captura exceções corretamente e responde com a mensagem de erro ou status HTTP apropriado, sem deixar a aplicação falhar de forma descontrolada.
2.	Garantir Experiência do Usuário Consistente: Em situações de erro, queremos que a aplicação retorne mensagens informativas, como “Usuário não encontrado” ou “Erro de validação”, para que o usuário ou cliente do serviço entenda o que deu errado e, se possível, possa corrigir.
3.	Prevenir Falhas em Produção: Simulando esses cenários, conseguimos antecipar problemas antes que a aplicação esteja em produção, evitando que falhas conhecidas cheguem aos usuários finais.
4.	Cobrir Casos de Uso Extremos e Inputs Inválidos: Esses testes ajudam a validar como a aplicação se comporta com dados inesperados (como IDs inválidos ou campos obrigatórios ausentes), e se ela está pronta para lidar com esses casos sem comprometer a estabilidade.
5.	Aumentar a Confiabilidade do Código: Ao garantir que cada cenário de erro é tratado, temos confiança de que a aplicação é resiliente e capaz de lidar com uma variedade de inputs e condições, tornando o sistema mais robusto e confiável.

Esses testes de erro são fundamentais para sistemas seguros e confiáveis, especialmente em APIs e serviços que devem lidar com diversas interações e dados variados.


## Documentação da API


Usamos isso?
https://swagger.io/resources/open-api/


