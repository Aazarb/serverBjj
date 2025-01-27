CREATE DATABASE bjj_db;


-- Créer l'utilisateur "admin" avec le mot de passe "password"
CREATE USER 'user' IDENTIFIED BY 'password';

-- Donner tous les privilèges sur la base de données "bjj_db"
GRANT ALL PRIVILEGES ON bjj_db.* TO 'user';

-- Appliquer les changements
FLUSH PRIVILEGES;
