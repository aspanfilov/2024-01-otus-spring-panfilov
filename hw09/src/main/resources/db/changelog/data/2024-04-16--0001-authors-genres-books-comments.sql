--liquibase formatted sql

--changeset Panfilov Artur:2024-04-16-0001-authors
insert into authors(full_name)
values ('Федор Михайлович Достоевский'), ('Теодор Драйзер'), ('Брюс Эккель');

--changeset Panfilov Artur:2024-04-16-0001-genres
insert into genres(name)
values ('Классика'), ('Учебная'), ('Драма'),
('Психология'), ('Политика'), ('Экономика');

--changeset Panfilov Artur:2024-04-16-0001-books
insert into books(title, author_id)
values ('Бесы', 1), ('Братья Карамазовы', 1),
       ('Финансист', 2), ('Американская трагедия', 2),
       ('Философия Java', 3);

--changeset Panfilov Artur:2024-04-16-0001-books_genres
insert into books_genres(book_id, genre_id)
values (1, 1), (1, 3), (1, 4), (1, 5),
       (2, 1), (2, 3), (2, 4),
       (3, 1), (3, 3), (3, 4), (3, 6),
       (4, 1), (4, 3), (4, 4),
       (5, 2), (5, 3);

