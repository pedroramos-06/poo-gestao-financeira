# 💰 WalletFlow
Sistema de gestão financeira pessoal desenvolvido com Spring Boot.

---

## ⚙️ Spring Boot

### 🛠️ Configurações Initializr

| Campo | Valor |
|---|---|
| Project | Maven |
| Language | Java |
| Spring Boot | 4.0.7 |
| Packaging | Jar |
| Java | 17 |

### 📦 Dependências

| Dependência | Para quê serve |
|---|---|
| Spring Web | Construção de aplicações web/RESTful com Spring MVC |
| Spring Data JPA | Persistência de dados com JPA/Hibernate |
| Thymeleaf | Template engine para renderização das páginas |
| H2 Database | Banco de dados em memória para desenvolvimento |
| Validation | Validação de dados com Hibernate Validator |

---

## 🎨 Frontend

- Bootstrap

---

## 🗄️ Banco de dados

- **H2** — banco de dados em memória

---

## 🧱 Estrutura

- **Controllers** — recebem as requisições e retornam as respostas
- **DTO** — objetos de transferência de dados entre camadas
- **Service** — regras de negócio da aplicação
- **Repository** — acesso e persistência dos dados (Spring Data JPA)
- **Models** — entidades que representam as tabelas do banco
