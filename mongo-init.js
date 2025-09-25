// Script de inicialização do MongoDB
db = db.getSiblingDB('assessmentdb');

// Criar usuário para a aplicação
db.createUser({
  user: 'appuser',
  pwd: 'apppassword',
  roles: [
    {
      role: 'readWrite',
      db: 'assessmentdb'
    }
  ]
});

// Criar algumas coleções iniciais se necessário
db.createCollection('students');
db.createCollection('professors');
db.createCollection('disciplines');
db.createCollection('enrollments');

print('Database initialized successfully!');
