<h1 align="center">Spring Boot Security Project</h1>

<p align="center">
  <img src="https://img.shields.io/badge/Spring--Boot--Security--Project-brightgreen" alt="Spring Boot Security Project">
  <img src="https://img.shields.io/badge/Using--Docker-blue" alt="Docker">
  <img src="https://img.shields.io/badge/Java-21-blueviolet" alt="Java 21">
  <img src="https://img.shields.io/badge/Database-MySQL-4479A1" alt="MySQL">
  <img src="https://img.shields.io/badge/Auth-OAuth2-orange" alt="OAuth2">
</p>

<p align="center">Este é um projeto de demonstração para implementar segurança usando Spring Boot, com funcionalidades de login, registro de usuários, criação e remoção de tweets, e autenticação via JWT (JSON Web Token). O projeto utiliza o Spring Security, OAuth2 Resource Server, JPA e MySQL.</p>

## Funcionalidades

- **Registro de usuário:** Permite que novos usuários se registrem, armazenando suas credenciais de forma segura com a codificação da senha utilizando BCrypt.
- **Login de usuário:** Usuários podem fazer login fornecendo suas credenciais, e um JWT é gerado para autenticação futura.
- **Autenticação JWT:** O sistema utiliza JWT para proteger endpoints e garantir a comunicação segura.
- **Gestão de usuários:** Apenas administradores autenticados podem visualizar todos os usuários registrados.
- **Tweets:** Usuários autenticados podem criar, listar e excluir tweets.

## Tecnologias

- **Spring Boot 3.4.4**: Framework utilizado para o desenvolvimento da aplicação.
- **Spring Security**: Utilizado para a implementação de autenticação e autorização.
- **Spring Data JPA**: Para comunicação com o banco de dados.
- **JWT (JSON Web Token)**: Utilizado para autenticação.
- **MySQL**: Banco de dados utilizado para armazenar as informações de usuários.
- **Docker**: Utilizado para facilitar a execução do ambiente do projeto.

## Pré-requisitos

- **Java 21**: Certifique-se de que o Java 21 esteja instalado.
- **MySQL**: Instalar e configurar um banco de dados MySQL, caso não queira usar o Docker.
- **Docker e Docker Compose**: Para rodar o projeto com os containers configurados.

## Como rodar o projeto

### Passo 1: Clone o repositório

```bash
git clone https://github.com/leandrordg/authentication-spring repositorio
cd repositorio
```

### Passo 2: Configure o banco de dados MySQL

Se for utilizar o MySQL localmente, configure o banco de dados no arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/seubancodedados
spring.datasource.username=exemplo
spring.datasource.password=exemplo
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

Caso queira usar o Docker para rodar o MySQL, pule este passo.

### Passo 3: Usando Docker

Execute os containers do Docker para o ambiente de desenvolvimento:

```bash
docker compose up
```

Isso irá rodar a aplicação Spring Boot e o MySQL dentro de containers Docker, sem precisar de uma configuração manual no seu sistema.

### Passo 4: Execute o projeto

#### Usando Maven:

```bash
mvn spring-boot:run
```

#### Usando IDE (Ex: IntelliJ, Eclipse):

1. Abra o projeto na IDE.
2. Execute a classe `SecurityApplication.java`.

## Endpoints

- **POST /auth/register**: Registrar um novo usuário.

  **Requisição:**
    ```json
    {
      "username": "johndoe",
      "password": "johndoe123"
    }
    ```

- **POST /auth/login**: Fazer login com um usuário registrado.

  **Requisição:**
    ```json
    {
      "username": "johndoe",
      "password": "johndoe123"
    }
    ```

  **Resposta:**
    ```json
    {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
      "expiresIn": 300
    }
    ```

- **GET /users**: Listar todos os usuários (somente para administradores).

  **Autenticação:** Bearer token (`{{accessToken}}`)

- **GET /tweets**: Listar tweets (pode ser filtrado por `pageSize` e `page`).

  **Requisição:**
    ```
    GET {{localhost}}/tweets?pageSize=10&page=0
    ```

  **Autenticação:** Bearer token (`{{accessToken}}`)

- **POST /tweets**: Criar um novo tweet.

  **Requisição:**
    ```json
    {
      "content": "meu primeiro tweet da conta lbertalhia"
    }
    ```

  **Autenticação:** Bearer token (`{{accessToken}}`)

- **DELETE /tweets/{tweetId}**: Deletar um tweet pelo seu ID.

  **Requisição:**
    ```
    DELETE {{localhost}}/tweets/{id}
    ```

  **Autenticação:** Bearer token (`{{accessToken}}`)

## Imagens do Projeto

### Controllers

![Controllers](./images/controllers.png)

### Services

![Services](./images/services.png)

### Infrastructure

![Infrastructure](./images/infra.png)

## Como contribuir

1. Faça um fork do repositório.
2. Crie uma branch para a sua feature: `git checkout -b minha-feature`.
3. Faça suas alterações e commit: `git commit -am 'Adiciona nova feature'`.
4. Envie para o repositório remoto: `git push origin minha-feature`.
5. Crie um Pull Request.

## Licença

Este projeto é licenciado sob a [MIT License](./LICENSE).