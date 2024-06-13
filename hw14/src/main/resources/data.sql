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
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3),
       ('BookTitle_4', 4), ('BookTitle_5', 5), ('BookTitle_6', 6),
       ('BookTitle_7', 7), ('BookTitle_8', 8), ('BookTitle_9', 9),
       ('BookTitle_10', 10), ('BookTitle_11', 11), ('BookTitle_12', 12),
       ('BookTitle_13', 13), ('BookTitle_14', 14), ('BookTitle_15', 15),
       ('BookTitle_16', 16), ('BookTitle_17', 17), ('BookTitle_18', 18),
       ('BookTitle_19', 19), ('BookTitle_20', 20), ('BookTitle_21', 21);

insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6),
       (4, 7), (4, 8),
       (5, 9), (5, 10),
       (6, 11), (6, 12),
       (7, 13), (7, 14),
       (8, 15), (8, 16),
       (9, 17), (9, 18),
       (10, 19), (10, 20),
       (11, 21), (11, 22),
       (12, 23), (12, 24),
       (13, 25), (13, 26),
       (14, 27), (14, 28),
       (15, 29), (15, 30);

insert into book_comments(comment_text, book_id)
values ('Comment_1', 1), ('Comment_2', 2), ('Comment_3', 3),
       ('Comment_4', 4), ('Comment_5', 5), ('Comment_6', 6),
       ('Comment_7', 7), ('Comment_8', 8), ('Comment_9', 9),
       ('Comment_10', 10), ('Comment_11', 11), ('Comment_12', 12),
       ('Comment_13', 13), ('Comment_14', 13), ('Comment_15', 13),
       ('Comment_16', 14), ('Comment_17', 14), ('Comment_18', 14),
       ('Comment_19', 15), ('Comment_20', 15), ('Comment_21', 15),
       ('Comment_22', 16), ('Comment_23', 16), ('Comment_24', 16),
       ('Comment_25', 17), ('Comment_26', 17), ('Comment_27', 17),
       ('Comment_28', 18), ('Comment_29', 18), ('Comment_30', 18);
