--liquibase formatted sql

--changeset Panfilov Artur:2024-05-13-0001-users
insert into users(username, password)
values  ('admin', '$2a$10$6yWetu/cAlk.1bO6NOpoXOtQoUmGWFWUj6guuW6Yd/uVJry1NCs0S'), --admin, admin
        ('user1', '$2a$10$S/1TxBk8sre6omde3HvPHOmO7GFpAfdiOoZEK5Q4SVZkFxxTDez9u'), --user1, user1
        ('user2', '$2a$10$xKrOs2jnGr/jmADau0CxpOMxo.9UyOnZddYNIp0Ea6D9CHq0/ItKW'), --user2, user2
        ('guest', '$2a$10$.yiLvIB17fl2jVziVZ9mV.xDSuOGvjOkeEkvZyFHaLGsV1AFANtOW'); --guest, guest

--changeset Panfilov Artur:2024-05-21-0002-authority_groups
insert into authority_groups (name)
values ('ADMIN'), ('USER'), ('GUEST');

--changeset Panfilov Artur:2024-05-23-0003-users_authority_groups
insert into users_authority_groups (user_id, authority_group_id)
values (1, 1), (2, 2), (3, 2), (4, 3);

--changeset Panfilov Artur:2024-05-21-0004-authorities
insert into authorities (name)
values ('BOOK_READ'), ('BOOK_WRITE'), ('BOOK_DELETE'),
       ('AUTHOR_READ'), ('AUTHOR_WRITE'), ('AUTHOR_DELETE'),
       ('GENRE_READ'), ('GENRE_WRITE'), ('GENRE_DELETE'),
       ('COMMENT_READ'), ('COMMENT_WRITE'), ('COMMENT_DELETE');

--changeset Panfilov Artur:2024-05-21-0005-authority_groups_authorities
insert into authority_groups_authorities (authority_group_id, authority_id)
values  (1, 1), (1, 2), (1, 3),
        (1, 4), (1, 5), (1, 6),
        (1, 7), (1, 8), (1, 9),
        (1, 10), (1, 11), (1, 12),

        (2, 1),
        (2, 4),
        (2, 7),
        (2, 10), (2, 11), (2, 12),

        (3, 1),
        (3, 4),
        (3, 7),
        (3, 10);

--changeset Panfilov Artur:2024-04-16-0006-authors
delete from authors;
insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

--changeset Panfilov Artur:2024-04-16-0007-genres
delete from genres;
insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

--changeset Panfilov Artur:2024-04-16-0008-books
insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

--changeset Panfilov Artur:2024-04-16-0009-books_genres
insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);

--changeset Panfilov Artur:2024-04-16-0010-book_comments
insert into book_comments(comment_text, book_id, user_id)
values ('comment_1', 1, 2), ('comment_2', 1, 3),
       ('comment_3', 2, 2), ('comment_4', 2, 2),
       ('comment_5', 3, 2), ('comment_6', 3, 2),
       ('comment_7', 3, 2), ('comment_8', 3, 2);