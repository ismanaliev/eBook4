drop table if exists audio_book cascade;
drop table if exists basket cascade;
drop table if exists basket_books cascade;
drop table if exists book cascade;
drop table if exists client_operation_books cascade;
drop table if exists client_operations cascade;
drop table if exists electronic_book cascade;
drop table if exists favorites cascade;
drop table if exists file_sources cascade;
drop table if exists paper_book cascade;
drop table if exists promocode cascade;
drop table if exists roles cascade;
drop table if exists user_liked_books cascade;
drop table if exists users cascade;
drop table if exists vendor_books cascade;
drop sequence if exists audiobook_seq;
drop sequence if exists basket_seq;
drop sequence if exists book_seq;
drop sequence if exists ebook_seq;
drop sequence if exists favorites_seq;
drop sequence if exists file_sources_seq;
drop sequence if exists operation_seq;
drop sequence if exists paperbook_seq;
drop sequence if exists promo_seq;
drop sequence if exists role_seq;
drop sequence if exists user_seq;
create sequence audiobook_seq start 1 increment 1;
create sequence basket_seq start 1 increment 1;
create sequence book_seq start 1 increment 1;
create sequence ebook_seq start 1 increment 1;
create sequence favorites_seq start 1 increment 1;
create sequence file_sources_seq start 1 increment 1;
create sequence operation_seq start 1 increment 1;
create sequence paperbook_seq start 1 increment 1;
create sequence promo_seq start 1 increment 1;
create sequence role_seq start 1 increment 1;
create sequence user_seq start 2 increment 1;

create table audio_book
(
    audio_book_id          int8 not null,
    duration               time,
    fragment               varchar(255),
    url_of_book_from_cloud varchar(255),
    primary key (audio_book_id)
);

create table basket
(
    basket_id int8 not null,
    user_id   int8,
    primary key (basket_id)
);

create table basket_books
(
    basket_id int8 not null,
    book_id   int8 not null
);

create table book
(
    book_id             int8    not null,
    about_book          varchar(255),
    author_full_name    varchar(255),
    baskets             int4    not null,
    book_type           varchar(255),
    discount            int4,
    discount_from_promo int4,
    genre               varchar(255),
    is_active           boolean not null,
    is_best_seller      boolean,
    language            varchar(255),
    likes               int4    not null,
    price               numeric(19, 2),
    publishing_house    varchar(255),
    title               varchar(255),
    year_of_issue       date,
    audiobook_id        int8,
    ebook_id            int8,
    paperbook_id        int8,
    primary key (book_id)
);

create table client_operation_books
(
    operation_id int8 not null,
    book_id      int8 not null
);

create table client_operations
(
    operation_id int8 not null,
    user_id      int8,
    primary key (operation_id)
);

create table electronic_book
(
    ebook_id               int8 not null,
    fragment_of_book       varchar(255),
    number_of_pages        int4,
    url_of_book_from_cloud varchar(255),
    primary key (ebook_id)
);

create table favorites
(
    favorite_id int8 not null,
    user_id     int8,
    book_id     int8,
    primary key (favorite_id)
);

create table file_sources
(
    id      int8 not null,
    images  varchar(255),
    book_id int8,
    primary key (id)
);

create table paper_book
(
    paper_book_id      int8 not null,
    fragment_of_book   varchar(255),
    number_of_pages    int4,
    number_of_selected int4,
    primary key (paper_book_id)
);

create table promocode
(
    id            int8 not null,
    beginning_day date,
    discount      int4 not null,
    end_day       date,
    promocode     varchar(255),
    user_id       int8,
    primary key (id)
);

create table roles
(
    id   int8 not null,
    name varchar(20),
    primary key (id)
);

create table user_liked_books
(
    user_id     int8 not null,
    favorite_id int8 not null
);

create table users
(
    user_id      int8 not null,
    email        varchar(50),
    first_name   varchar(255),
    last_name    varchar(255),
    number       varchar(255),
    password     varchar(64),
    operation_id int8,
    role_id      int8,
    primary key (user_id)
);

create table vendor_books
(
    user_id int8 not null,
    book_id int8 not null
);

alter table if exists basket_books
    add constraint UK_fmems7vdq93o1cbiggi2i0sn0 unique (book_id);
alter table if exists client_operation_books
    add constraint UK_42ea7c9u1ae2v0qhxlnug7vib unique (book_id);
alter table if exists user_liked_books
    add constraint UK_o2kigultwf9c9r0ycoeholngq unique (favorite_id);
alter table if exists users
    add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email);
alter table if exists vendor_books
    add constraint UK_3a9oermdmttxoxmmdm7d1xqqp unique (book_id);
alter table if exists basket
    add constraint FK810a8gq30miyp6j1eub97qm6k foreign key (user_id) references users;
alter table if exists basket_books
    add constraint FKsn8qc2gvdkkbvoj4tjred26vk foreign key (book_id) references book;
alter table if exists basket_books
    add constraint FK8irmqww621kbknimho5v1834o foreign key (basket_id) references basket;
alter table if exists book
    add constraint FKdprixgudcimk0y74sm1890i7h foreign key (audiobook_id) references audio_book;
alter table if exists book
    add constraint FKnvpltwyl30wwcq07t660h7yi7 foreign key (ebook_id) references electronic_book;
alter table if exists book
    add constraint FK4wqd1fquypqmo4ea5ah910bq foreign key (paperbook_id) references paper_book;
alter table if exists client_operation_books
    add constraint FK8k1ho69b9re3mglp1b2seqwgo foreign key (book_id) references book;
alter table if exists client_operation_books
    add constraint FKt1d91xt8u2olymh2lrntg1r4f foreign key (operation_id) references client_operations;
alter table if exists client_operations
    add constraint FKrfs0frm6itndxftmko8vaiegb foreign key (user_id) references users;
alter table if exists favorites
    add constraint FKk7du8b8ewipawnnpg76d55fus foreign key (user_id) references users;
alter table if exists favorites
    add constraint FK273xtfi5rey9ay0bek4b6mey5 foreign key (book_id) references book;
alter table if exists file_sources
    add constraint FKl53nj97s95ra5emnodnaddjau foreign key (book_id) references book;
alter table if exists promocode
    add constraint FKhukhmhwiowi738x9trd153grn foreign key (user_id) references users;
alter table if exists user_liked_books
    add constraint FKidjywp9mhhhvfr6a28yxcbebo foreign key (favorite_id) references favorites;
alter table if exists user_liked_books
    add constraint FKiu3i8u002917juq9cb7taywiu foreign key (user_id) references users;
alter table if exists users
    add constraint FKl62y3jbqrl3banos0r5pp7a9u foreign key (operation_id) references client_operations;
alter table if exists users
    add constraint FKp56c1712k691lhsyewcssf40f foreign key (role_id) references roles;
alter table if exists vendor_books
    add constraint FKl3v27c2q3c3sv76ge4juv8j2y foreign key (book_id) references book;
alter table if exists vendor_books
    add constraint FKp048a2rkyn4gfxlikbq0d0xiu foreign key (user_id) references users;

insert into roles(id, name)
values (1, 'ROLE_CLIENT'),
       (2, 'ROLE_VENDOR'),
       (3, 'ROLE_ADMIN');

insert into users(user_id, email, password, role_id)
values (1, 'admin@gmail.com', '$2a$10$Zz/PNXKsnLOSPmHtMP1zZ.WK31Bs69gv3a2N5mKAcn3/qP8Lq1Od.', 3);