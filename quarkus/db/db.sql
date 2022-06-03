create table users(
	id bigserial not null primary key,
	nome varchar(100) not null,
	email varchar(50) not null,
	senha varchar(20) not null,
	idade integer not null
);