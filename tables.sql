create table tb_cliente (
	id bigint,
	nome varchar(20) not null,
	sobrenome varchar(30) not null,
	cpf varchar(16) not null,
	telefone varchar(16) not null,
	endereco varchar(50) default('NC'),
	numero_endereco integer default(0),
	cidade varchar(30) default('NC'),
	estado varchar(2) default('NC'),
	constraint pk_id_cliente primary key (id),
	constraint uq_cpf_cliente unique (cpf)
);

create table tb_produto (
	id bigint,
	codigo varchar(10) not null,
	nome varchar(50) not null,
	descricao text default('NC'),
	preco numeric(10,2) default(0),
	estoque int not null,
	constraint pk_id_produto primary key (id),
	constraint uq_codigo_produto unique (codigo)
);

create table tb_venda (
	id bigint,
	codigo varchar(10) not null,
	id_cliente_fk bigint not null,
	valor_total numeric(10,2) not null,
	data_venda timestamptz not null,
	status_venda varchar(50) not null,
	constraint pk_id_venda primary key (id),
	constraint uq_codigo_venda unique (codigo),
	constraint fk_id_cliente_venda foreign key (id_cliente_fk) references tb_cliente(id)
);

create table tb_produto_quantidade (
	id bigint,
	id_produto_fk bigint not null,
	id_venda_fk bigint not null,
	quantidade int not null,
	valor_total numeric(10,2) not null,
	constraint pk_id_prodquant primary key (id),
	constraint fk_id_prodquant_produto foreign key (id_produto_fk) references tb_produto(id),
	constraint fk_id_prodquant_venda foreign key (id_venda_fk) references tb_venda(id)
);

create table tb_estoque (
	id bigint,
	id_produto_fk bigint not null,
	estoque int not null,
	constraint pk_id_prodest primary key (id),
	constraint fk_id_prodest_produto foreign key (id_produto_fk) references tb_produto(id),
);

create sequence sq_cliente
start 1
increment 1
owned by tb_cliente.id;

create sequence sq_produto
start 1
increment 1
owned by tb_produto.id;

create sequence sq_venda
start 1
increment 1
owned by tb_venda.id;

create sequence sq_produto_quantidade
start 1
increment 1
owned by tb_produto_quantidade.id;

create sequence sq_estoque
start 1
increment 1
owned by tb_estoque.id;

select v.id as id_venda, v.codigo, v.id_cliente_fk, v.valor_total, v.data_venda, v.status_venda,
c.id as id_cliente, c.nome, c.cpf, c.telefone, c.endereco, c.numero_endereco, c.cidade, c.estado,
p.id as id_prod_qtd, p.quantidade, p.valor_total as prod_qtd_valor_total
from tb_venda v
inner join tb_cliente c on v.id_cliente_fk = c.id
inner join tb_produto_quantidade p on p.id_venda_fk = v.id
where v.codigo = 'A1';

select pq.id, pq.quantidade, pq.valor_total,
p.id as id_produto, p.codigo, p.nome, p.descricao, p.preco
from tb_produto_quantidade pq
inner join tb_produto p on p.id = pq.id_produto_fk;

select pe.id, pe.estoque,
p.id as id_produto, p.codigo, p.nome, p.descricao, p.preco
from tb_estoque pe
inner join tb_produto p on p.id = pe.id_produto_fk;