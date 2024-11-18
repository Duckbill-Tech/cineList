## Relatório de Aplicação - Sistema de lista de filmes

O CineList é uma aplicação full stack desenvolvida para gerenciar listas de filmes, permitindo que os usuários cadastrem, 
atualizem, visualizem e removam filmes de suas listas. O sistema foi desenvolvido com uma arquitetura moderna utilizando 
Java e Spring Boot no backend e React no frontend, com um banco de dados MySQL. A interface inicial é oferecida através 
de uma API documentada via Swagger.

## Desenvolvimento 

O desenvolvimento do CineList foi realizado seguindo os princípios da programação orientada a objetos e de boas práticas 
para APIs RESTful. O projeto foi estruturado em pacotes distintos para facilitar a organização, manutenção e extensão do código. 
O backend gerencia toda a lógica de negócio e persistência de dados, enquanto o frontend (React) lida com a interface do usuário. 

As principais pastas são:

- backend: contém os controladores, serviços e repositórios da API.
- frontend (SPA): contém os componentes e lógica de apresentação da interface de usuário.
- docker: configurações para iniciar o banco de dados MySQL via Docker.

## Tecnologias e Ferramentas

Foram utilizadas as seguintes tecnologias e ferramentas:
- MySQL: Banco de dados relacional utilizado para armazenar informações dos filmes.
- Docker: Configuração do serviço MySQL em containers, facilitando a criação e gerenciamento do ambiente.
- JPA (Java Persistence API): Utilizada para realizar operações de persistência no banco de dados de forma simplificada e automatizada.
- Swagger: Documentação da API, permitindo que os endpoints sejam explorados e testados.
- JUnit e Mockito: Frameworks de teste para validar o funcionamento dos métodos de serviço.

## Estrutura do Banco de Dados

O banco de dados MySQL é composto por tabelas que armazenam informações dos filmes e usuários:
- Filmes: Armazena dados sobre cada filme, incluindo título, descrição, e status de visualização.
- Usuários: Contém informações sobre os usuários, incluindo identificadores e preferências.

Na pasta `doc` contém o driagrama do banco de dados, para mais informações.

## Funcionalidades

O CineList inclui algumas dessas funcionalidades para:

Gerenciamento de Filmes:
- Cadastrar um novo filme: `create()`.
- Atualizar dados de um filme existente: `update()`.
- Excluir um filme: `delete()`.
- Visualizar lista completa de filmes: `getAll()`.
- Visualizar por ID: `getById()`.

Gerenciamento de Usuários:
- Cadastro de novos usuários: `create()`.
- Atualização de dados dos usuários: `update()`.
- Excluir um usuário: `delete()`.
- Visualizar lista completa de usuários: `getAll()`.
- Visualizar por ID: `getById()`.
- Validar CPF: `isValidCPF()` e `calcularDigito()`.

Na pasta `doc` contém um relatório com mais informações mais profundas sobre todas as funcionalidades. 

## Classes Principais e Organização

O sistema foi organizado em várias classes, cada uma responsável por um aspecto do sistema:
- Filme (db): Classe responsável por definir atributos e métodos relacionados ao gerenciamento de filmes.
- Usuário (db): Classe que armazena as informações dos usuários do sistema.

- Controller: Define os endpoints da API e gerencia as interações com o cliente.
- Service: Contém a lógica de negócio do sistema.
- Repository: Interface para operações de banco de dados usando JPA.
- DTO: Responsável pela transmição de dados. 
- Security: Classes responsáveis pela segurança da aplicação.
- Mapper: Responsável pela converção entre os objetos Filme (entidade de domínio) e FilmeDTO (objeto de transferência de dados).

## Database e Conexão com Docker

Para configurar o banco de dados, utiliza-se o seguinte comando Docker para iniciar o serviço MySQL:

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

## Frontend (React)

O frontend do CineList é desenvolvido com React e é responsável pela interface interativa da aplicação. Para configurar e iniciar o frontend, siga os passos abaixo:
1.	Navegue até o diretório do frontend (chamado spa):

``` sh
cd spa
```

2.	Instale as dependências do React:

``` sh
npm install
```

3.	Inicie o servidor de desenvolvimento:

``` sh
npm run dev
```

## Migrations e Testes

- Migrations usando JPA: As migrações são gerenciadas automaticamente pelo JPA, mantendo o esquema atualizado 
conforme o código evolui. Para rodar as migrations, use:

```sh
mvn clean
mvn compile
mvn spring-boot:run
```

- Testes: Os testes são realizados com JUnit e Mockito. Para executar todos os testes, rode o comando:

```sh
mvn test        
```

## Uso do Sistema - Backend

```sh
mvn spring-boot:run
```
Com esses passos, o CineList está pronto para uso.

## Estrutura do Projeto

O projeto foi estruturado para separar responsabilidades, com pastas para:
•	backend: código Java (controladores, serviços, repositórios e configuração de segurança).
•	frontend (SPA): código React para a interface do usuário.
•	docker: scripts para configurar o ambiente de banco de dados.

## Documentação API - Swagger

A API está documentada com Swagger, permitindo fácil visualização e teste dos endpoints:
- Swagger UI: http://localhost:8081/swagger-ui/index.html
- Documentação JSON: http://localhost:8081/v3/api-docs
