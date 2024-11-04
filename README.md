# moviesList
A3 - Sitemas distriuídos e Mobile

Full stack aplicação para gerenciar listas de filmes.

Command line for MySQL service via docker:

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
Comando para rodar teste:

````
mvn test        
```