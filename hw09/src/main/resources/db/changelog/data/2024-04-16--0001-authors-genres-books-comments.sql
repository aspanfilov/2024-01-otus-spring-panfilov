--liquibase formatted sql

--changeset Panfilov Artur:2024-04-16-0001-authors
insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3'), ('Author_4');

--changeset Panfilov Artur:2024-04-16-0001-genres
insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
('Genre_4'), ('Genre_5'), ('Genre_6');

--changeset Panfilov Artur:2024-04-16-0001-books
insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

--changeset Panfilov Artur:2024-04-16-0001-books_genres
insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
(2, 3),   (2, 4),
(3, 5),   (3, 6);

--changeset Panfilov Artur:2024-04-16-0001-book_comments
insert into book_comments(comment_text, book_id)
values ('comment_1', 1), ('comment_2', 1),
('comment_3', 2), ('comment_4', 2),
('comment_5', 3), ('comment_6', 3);