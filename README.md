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

## Migrations usando JPA

```
mvn clean
mvn compile
mvn spring-boot:run
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
