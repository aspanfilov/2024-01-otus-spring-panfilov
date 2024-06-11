insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3'),
       ('Author_4'), ('Author_5'), ('Author_6'),
       ('Author_7'), ('Author_8'), ('Author_9'),
       ('Author_10'), ('Author_11'), ('Author_12'),
       ('Author_13'), ('Author_14'), ('Author_15'),
       ('Author_16'), ('Author_17'), ('Author_18'),
       ('Author_19'), ('Author_20'), ('Author_21'),
       ('Author_22'), ('Author_23'), ('Author_24'),
       ('Author_25'), ('Author_26'), ('Author_27'),
       ('Author_28'), ('Author_29'), ('Author_30'),
       ('Author_31'), ('Author_32'), ('Author_33'),
       ('Author_34'), ('Author_35'), ('Author_36'),
       ('Author_37'), ('Author_38'), ('Author_39'),
       ('Author_40'), ('Author_41'), ('Author_42'),
       ('Author_43'), ('Author_44'), ('Author_45'),
       ('Author_46'), ('Author_47'), ('Author_48'),
       ('Author_49'), ('Author_50'), ('Author_51'),
       ('Author_52'), ('Author_53'), ('Author_54'),
       ('Author_55'), ('Author_56'), ('Author_57'),
       ('Author_58'), ('Author_59'), ('Author_60');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6'),
       ('Genre_7'), ('Genre_8'), ('Genre_9'),
       ('Genre_10'), ('Genre_11'), ('Genre_12'),
       ('Genre_13'), ('Genre_14'), ('Genre_15'),
       ('Genre_16'), ('Genre_17'), ('Genre_18'),
       ('Genre_19'), ('Genre_20'), ('Genre_21'),
       ('Genre_22'), ('Genre_23'), ('Genre_24'),
       ('Genre_25'), ('Genre_26'), ('Genre_27'),
       ('Genre_28'), ('Genre_29'), ('Genre_30');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);
