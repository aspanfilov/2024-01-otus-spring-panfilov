--liquibase formatted sql

--changeset Panfilov Artur:2024-05-13-0001-users
insert into users(username, password)
values  ('admin', '$2a$10$6yWetu/cAlk.1bO6NOpoXOtQoUmGWFWUj6guuW6Yd/uVJry1NCs0S'), --admin, admin
        ('user1', '$2a$10$S/1TxBk8sre6omde3HvPHOmO7GFpAfdiOoZEK5Q4SVZkFxxTDez9u'), --user1, user1
        ('user2', '$2a$10$xKrOs2jnGr/jmADau0CxpOMxo.9UyOnZddYNIp0Ea6D9CHq0/ItKW'), --user2, user2
        ('guest', '$2a$10$.yiLvIB17fl2jVziVZ9mV.xDSuOGvjOkeEkvZyFHaLGsV1AFANtOW'); --guest, guest

