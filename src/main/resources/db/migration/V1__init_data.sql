create table categories (id bigserial primary key, title varchar(255));
insert into categories (title)
values
('Food'),
('Electronic');

create table products (id bigserial primary key, title varchar(255), price int, category_id bigint references categories (id));
insert into products (title, price, category_id)
values
('Milk', 95, 1),
('Bread', 25, 1),
('Cheese', 360, 1);

create table authors (id bigserial primary key, name varchar(255));
insert into authors (name)
values
('Chief'),
('Programmer'),
('Reporter');

create table books (id bigserial primary key, title varchar(256), pages int, author_id bigint references authors(id));
insert into books (title, pages, author_id)
values
('Cooking manual', 25, 1),
('Java manual', 100, 2),
('C++ manual', 100, 2),
('Newspaper', 10, 3);