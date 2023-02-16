-- !Ups

alter table schedule add status text not null default 'active';
alter table schedule alter column status drop default;

-- !Downs

alter table schedule drop column status;