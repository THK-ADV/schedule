# --- !Ups

create table "semesters"
(
    "id"   uuid    not null primary key,
    "name" varchar not null
);

# --- !Downs

drop table "semesters" if exists;