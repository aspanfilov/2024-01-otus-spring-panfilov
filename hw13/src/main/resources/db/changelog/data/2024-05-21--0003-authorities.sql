--liquibase formatted sql

--changeset Panfilov Artur:2024-05-21-0001-authorities
insert into authorities (name)
values ('BOOK_READ'), ('BOOK_WRITE'), ('BOOK_DELETE'),
       ('AUTHOR_READ'), ('AUTHOR_WRITE'), ('AUTHOR_DELETE'),
       ('GENRE_READ'), ('GENRE_WRITE'), ('GENRE_DELETE'),
       ('COMMENT_READ'), ('COMMENT_WRITE'), ('COMMENT_DELETE');

--changeset Panfilov Artur:2024-05-21-0002-authority_groups
insert into authority_groups (name)
values ('GUEST'), ('USER'), ('ADMIN');

--changeset Panfilov Artur:2024-05-21-0003-authority_groups_authorities
insert into authority_groups_authorities (authority_group_id, authority_id)
values (1, 1), (1, 4), (1, 7), (1, 10),

       (2, 1), (2, 4), (2, 7), (2, 10),
       (2, 11), (2, 12),

       (3, 1), (3, 2), (3, 3), (3, 4),
       (3, 5), (3, 6), (3, 7), (3, 8),
       (3, 9), (3, 10), (3, 11), (3, 12);