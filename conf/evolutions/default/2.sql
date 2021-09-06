# --- !Ups
create table schedule_wip
(
    "id"                            uuid PRIMARY KEY,
    "last_modified"                 timestamp not null,
    "course"                        uuid      not null,
    "room"                          uuid      not null,
    "module_examination_regulation" uuid      not null,
    "weekday"                       number    not null,
    "start"                         time without time zone not null,
    "end"                           time without time zone not null,
    "notes"                         text      not null,
    "history"                       text      not null,
    "priority"                      integer   not null,
    FOREIGN KEY (course) REFERENCES course (id),
    FOREIGN KEY (room) REFERENCES room (id),
    FOREIGN KEY (module_examination_regulation) REFERENCES module_examination_regulation (id)
);

# --- !Downs
drop table schedule_wip if exists;