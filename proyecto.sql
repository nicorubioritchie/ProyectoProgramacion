drop database if exists proyecto;
create database proyecto;

use proyecto;

create table usuario(
pass varchar(10),
edad numeric,
ubicacion varchar(50),
mail varchar(40),
preferencias varchar(150),
telefono int
);