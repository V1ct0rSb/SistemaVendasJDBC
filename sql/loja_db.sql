-- sql/loja_db.sql
CREATE DATABASE loja_db;
USE loja_db;

CREATE TABLE category (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(100) NOT NULL
);

CREATE TABLE product (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(100) NOT NULL,
    Price DOUBLE,
    CategoryId INT,
    FOREIGN KEY (CategoryId) REFERENCES category(Id)
);

INSERT INTO category (Name) VALUES ('Eletrônicos'), ('Roupas'), ('Alimentos');

INSERT INTO product (Name, Price, CategoryId) VALUES
('Smartphone', 2500.00, 1),
('Camisa Polo', 120.00, 2),
('Chocolate', 8.50, 3);