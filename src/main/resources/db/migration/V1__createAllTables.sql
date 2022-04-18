create table roles
(
    id   bigint not null
        primary key,
    name varchar(20)
);

create table audio_book
(
    audio_book_id          bigint not null
        primary key,
    duration               time,
    fragment               varchar(255),
    url_of_book_from_cloud varchar(255)
);

create table electronic_book
(
    ebook_id               bigint not null
        primary key,
    fragment_of_book       varchar(255),
    number_of_pages        integer,
    publishing_house       varchar(255),
    url_of_book_from_cloud varchar(255)
);

create table paper_book
(
    paper_book_id      bigint not null
        primary key,
    fragment_of_book   varchar(255),
    number_of_pages    integer,
    number_of_selected integer,
    publishing_house   varchar(255)
);

create table book
(
    book_id          bigint  not null
        primary key,
    about_book       varchar(255),
    author_full_name varchar(255),
    baskets          integer not null,
    book_type        varchar(255),
    discount         integer,
    genre            varchar(255),
    is_best_seller   boolean,
    language         varchar(255),
    likes            integer not null,
    price            numeric(19, 2),
    title            varchar(255),
    year_of_issue    date,
    audiobook_id     bigint
        constraint fkdprixgudcimk0y74sm1890i7h
            references audio_book,
    ebook_id         bigint
        constraint fknvpltwyl30wwcq07t660h7yi7
            references electronic_book,
    paperbook_id     bigint
        constraint fk4wqd1fquypqmo4ea5ah910bq
            references paper_book

);

create table basket
(
    basket_id bigint not null
        primary key
);

create table basket_books
(
    basket_id bigint not null
        constraint fkkhldx2fv5feh03e92xe9hojmo
            references basket,
    book_id    bigint not null
        constraint uk_fmems7vdq93o1cbiggi2i0sn0
            unique
        constraint fksn8qc2gvdkkbvoj4tjred26vk
            references book
);


create table users
(
    user_id    bigint not null
        primary key,
    email      varchar(50)
        constraint uk6dotkott2kjsp8vw4d0m25fb7
            unique,
    first_name varchar(255),
    last_name  varchar(255),
    number     varchar(255),
    password   varchar(120),
    basket_id  bigint
        constraint fk7uvaosre9anv0okiwm4mx82bm
            references basket,
    role_id    bigint
        constraint fkp56c1712k691lhsyewcssf40f
            references roles
);

create table favorites
(
    favorite_id bigint not null
        primary key,
    user_id     bigint
        constraint fkk7du8b8ewipawnnpg76d55fus
            references users,
    book_id     bigint
        constraint fk273xtfi5rey9ay0bek4b6mey5
            references book
);

create table file_sources
(
    id      bigint not null
        primary key,
    images  varchar(255),
    book_id bigint
        constraint fkl53nj97s95ra5emnodnaddjau
            references book
);

create table user_liked_books
(
    user_id     bigint not null
        constraint fkiu3i8u002917juq9cb7taywiu
            references users,
    favorite_id bigint not null
        constraint uk_o2kigultwf9c9r0ycoeholngq
            unique
        constraint fkidjywp9mhhhvfr6a28yxcbebo
            references favorites
);

create table vendor_books
(
    user_id bigint not null
        constraint fkp048a2rkyn4gfxlikbq0d0xiu
            references users,
    book_id bigint not null
        constraint uk_3a9oermdmttxoxmmdm7d1xqqp
            unique
        constraint fkl3v27c2q3c3sv76ge4juv8j2y
            references book
);

create sequence audiobook_seq;
create sequence basket_seq;
create sequence book_seq;
create sequence ebook_seq;
create sequence favorites_seq;
create sequence file_sources_seq;
create sequence paperbook_seq;
create sequence role_seq;
create sequence user_seq;

insert into roles(id, name)
values(1, 'ROLE_CLIENT');
insert into roles(id, name)
values(2, 'ROLE_VENDOR');
insert into roles(id, name)
values(3, 'ROLE_ADMIN');

insert into users(user_id, email, password, role_id)
values(1,'admin@gmail.com',
       '$2a$10$Zz/PNXKsnLOSPmHtMP1zZ.WK31Bs69gv3a2N5mKAcn3/qP8Lq1Od.',
       3);