--liquibase formatted sql

--changeset Panfilov Artur:2024-05-21-0001-authority_groups
insert into authority_groups (name)
values ('ADMIN'), ('USER'), ('GUEST');

--changeset Panfilov Artur:2024-05-23-0002-users_authority_groups
insert into users_authority_groups (user_id, authority_group_id)
values (1, 1), (2, 2), (3, 2), (4, 3);

--changeset Panfilov Artur:2024-05-21-0003-authorities
insert into authorities (name)
values ('BOOK_READ'), ('BOOK_WRITE'), ('BOOK_DELETE'),
       ('AUTHOR_READ'), ('AUTHOR_WRITE'), ('AUTHOR_DELETE'),
       ('GENRE_READ'), ('GENRE_WRITE'), ('GENRE_DELETE'),
       ('COMMENT_READ'), ('COMMENT_WRITE'), ('COMMENT_DELETE');

--changeset Panfilov Artur:2024-05-21-0004-authority_groups_authorities
insert into authority_groups_authorities (authority_group_id, authority_id)
values  (1, 1), (1, 2), (1, 3), (1, 4),
        (1, 5), (1, 6), (1, 7), (1, 8),
        (1, 9), (1, 10), (1, 11), (1, 12),

        (2, 1), (2, 4), (2, 7), (2, 10),
        (2, 11), (2, 12),

        (3, 1), (3, 4), (3, 7), (3, 10);


