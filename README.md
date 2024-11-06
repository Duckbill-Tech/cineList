# moviesList
A3 - Sitemas distriuídos e Mobile

Aplicação Full stack para gerenciar listas de filmes.

Comando para inicializar o MySQL service via docker:

```sh
docker run -d \
    --rm \
    --name cinelist_db \
    -e MYSQL_ROOT_PASSWORD=cinelist \
    -e MYSQL_USER=cinelist \
    -e MYSQL_PASSWORD=cinelist \
    -e MYSQL_DATABASE=cinelist \
    -v cineList_data:/var/lib/mysql \
    -p 3306:3306 \
    mysql:latest
```

## Migrations 

```
mvn clean
mvn compile
```

Rodar as migrations gerando tabelas (com o warning de versão):
```
mvn flyway:migrate -Dflyway.url=jdbc:mysql://localhost:3306/cinelist -Dflyway.user=cinelist -Dflyway.password=cinelist -Dflyway.locations=classpath:/db/migration
```
Usando o -q para silenciar warnings:
```
mvn -q  flyway:migrate -Dflyway.url=jdbc:mysql://localhost:3306/cinelist -Dflyway.user=cinelist -Dflyway.password=cinelist -Dflyway.locations=classpath:/db/migration
```

# Testes

Comando para rodar teste:
```
mvn test        
```

# Servidor HTTP (rodar a aplicação)

```
mvn spring-boot:run
```
