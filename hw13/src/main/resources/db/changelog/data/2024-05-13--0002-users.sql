--liquibase formatted sql

--changeset Panfilov Artur:2024-05-13-0001-users
insert into users(username, password)
values ('user1', '$2a$10$Im0p6fFnhr9Aw7YdOF.Fq.MTqGHtNDwAG/.PRcy9sjye6KzqYZxkW'), --user1, password1
       ('user2', '$2a$10$BgpumrdObDeOZzkXn.fsPeZzPjsRF6WcJ7W9G0g28VqZr3y/vpXEq'); --user2, password2
