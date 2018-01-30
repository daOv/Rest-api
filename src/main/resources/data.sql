insert into book_category(name) values('roman');
insert into book_category(name) values('novela');

insert into book(description,title,category_id_fk) values('good ','Book1',1);
insert into book(description,title,category_id_fk) values('bad','Book2',1);
insert into book(description,title,category_id_fk) values('boring','Book3',2);