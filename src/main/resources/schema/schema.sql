
create database invoice_td;
create type invoice_status as enum ('DRAFT','CONFIRMED','PAID');

create table invoice(
    id serial primary key ,
    customer_name varchar (250) not null ,
    status invoice_status
);

create table invoice_line(
    id serial primary key ,
    invoice_id int not null references  invoice(id),
    label varchar (250) not null ,
    quantity int  not null ,
    unit_price numeric(10,2) not null
);

insert into invoice(id,customer_name,status) values (1,'Alice','CONFIRMED'),(2,'BOB','PAID'),(3,'Charlie','DRAFT');

insert into invoice_line ( invoice_id, label, quantity, unit_price) values (1,'Produit A',2,100),
                                                                              (1,'Produit B',1,50),
                                                                              (2,'Produit A',5,100),
                                                                              (2,'Service C',1,200),
                                                                              (3, 'Produit B',3,50);

create table tax_config(
    id serial primary key,
    label varchar not null,
    rate numeric(5,2) not null
);

insert into tax_config(label,rate)values ('TVA STANDARD',20);