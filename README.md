# School Management System

Sistema de gerenciamento escolar desenvolvido em Spring Boot com MongoDB.

## Tecnologias

- **Backend**: Spring Boot 3.5.6
- **Database**: MongoDB 7.0
- **Security**: JWT Authentication
- **Containerização**: Docker & Docker Compose
   
### Acesso
- **API**: http://localhost:8080
- **MongoDB**: localhost:27017 

##  Comandos úteis

```bash
# Clonar o repositório
git clone https://github.com/SEU_USUARIO/school-management.git
cd school-management

# Executar com Docker Compose
docker compose up --build
```

```bash
# Parar containers
docker compose down

# Ver logs
docker compose logs

# Executar em background
docker compose up -d --build
```

## 📁 Estrutura

```
src/
├── main/java/com/example/school_management/
│   ├── controller/     # REST Controllers
│   ├── model/         # Entidades JPA
│   ├── service/       # Lógica de negócio
│   ├── repository/    # Repositórios MongoDB
│   └── security/      # Configurações de segurança
└── main/resources/
    └── application.properties
```

## 🔐 Configuração

- **MongoDB**: admin/password123
- **Database**: assessmentdb
- **JWT Secret**: configurado no docker-compose.yml
 
