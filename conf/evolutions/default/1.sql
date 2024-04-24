-- !Ups

create table faculty
(
    "id"       text PRIMARY KEY,
    "de_label" text not null,
    "en_label" text not null
);

create table teaching_unit
(
    "id"       uuid PRIMARY KEY,
    "faculty"  text not null,
    "de_label" text not null,
    "en_label" text not null,
    FOREIGN KEY (faculty) REFERENCES faculty (id)
);

create table degree
(
    "id"       text PRIMARY KEY,
    "de_label" text not null,
    "de_desc"  text not null,
    "en_label" text not null,
    "en_desc"  text not null
);

create table specialization
(
    "id"    text PRIMARY KEY,
    "label" text not null
);

create table study_program
(
    "id"                uuid PRIMARY KEY,
    "teaching_unit"     uuid    not null,
    "degree"            text    not null,
    "de_label"          text    not null,
    "en_label"          text    not null,
    "abbrev"            text    not null,
    "po_id"             text    not null,
    "po_number"         integer not null,
    "specialization_id" text null,
    FOREIGN KEY (teaching_unit) REFERENCES teaching_unit (id),
    FOREIGN KEY (degree) REFERENCES degree (id),
    FOREIGN KEY (specialization_id) REFERENCES specialization (id)
);

create table language
(
    "id"       text PRIMARY KEY,
    "de_label" text not null,
    "en_label" text not null
);

create table season
(
    "id"       text PRIMARY KEY,
    "de_label" text not null,
    "en_label" text not null
);

create table module
(
    "id"       uuid PRIMARY KEY,
    "label"    text not null,
    "abbrev"   text not null,
    "language" text not null,
    "season"   text not null,
    "parts"    text not null,
    FOREIGN KEY (language) REFERENCES language (id),
    FOREIGN KEY (season) REFERENCES season (id)
);

create table module_relation
(
    "parent" uuid not null,
    "child"  uuid not null,
    PRIMARY KEY (parent, child),
    FOREIGN KEY (parent) REFERENCES module (id),
    FOREIGN KEY (child) REFERENCES module (id)
);

create table module_in_study_program
(
    "id"                   uuid PRIMARY KEY,
    "module"               uuid    not null,
    "study_program"        uuid    not null,
    "mandatory"            boolean not null,
    "focus"                boolean not null,
    "recommended_semester" text    not null,
    FOREIGN KEY (module) REFERENCES module (id),
    FOREIGN KEY (study_program) REFERENCES study_program (id)
);

create table identity
(
    "id"        text PRIMARY KEY,
    "firstname" text not null,
    "lastname"  text not null,
    "title"     text not null,
    "abbrev"    text not null,
    "kind"      text not null,
    "campus_id" text not null
);

create table module_supervisor
(
    "module"     uuid not null,
    "supervisor" text not null,
    PRIMARY KEY (module, supervisor),
    FOREIGN KEY (module) REFERENCES module (id),
    FOREIGN KEY (supervisor) REFERENCES identity (id)
);

create table semester
(
    "id"            uuid PRIMARY KEY,
    "label"         text not null,
    "abbrev"        text not null,
    "start"         date not null,
    "end"           date not null,
    "lecture_start" date not null,
    "lecture_end"   date not null
);

create table course
(
    "id"       uuid PRIMARY KEY,
    "semester" uuid not null,
    "module"   uuid not null,
    "part"     text not null,
    FOREIGN KEY (semester) REFERENCES semester (id),
    FOREIGN KEY (module) REFERENCES module (id)
);

create table course_lecturer
(
    "lecturer" text not null,
    "course"   uuid not null,
    PRIMARY KEY (lecturer, course),
    FOREIGN KEY (lecturer) REFERENCES identity (id),
    FOREIGN KEY (course) REFERENCES course (id)
);

create table campus
(
    "id"     uuid PRIMARY KEY,
    "label"  text not null,
    "abbrev" text not null
);

create table room
(
    "id"         uuid PRIMARY KEY,
    "campus"     uuid not null,
    "label"      text not null,
    "identifier" text not null,
    "type"       text not null,
    "capacity"   int  not null,
    FOREIGN KEY (campus) REFERENCES campus (id)
);

create table schedule_entry
(
    "id"     uuid PRIMARY KEY,
    "course" uuid not null,
    "room"   uuid not null,
    "date"   date not null,
    "start"  time without time zone not null,
    "end"    time without time zone not null,
    FOREIGN KEY (course) REFERENCES course (id),
    FOREIGN KEY (room) REFERENCES room (id)
);

create table module_in_study_program_placed_in_schedule_entry
(
    "module_in_study_program" uuid not null,
    "schedule_entry"          uuid not null,
    PRIMARY KEY (module_in_study_program, schedule_entry),
    FOREIGN KEY (module_in_study_program) REFERENCES module_in_study_program (id),
    FOREIGN KEY (schedule_entry) REFERENCES schedule_entry (id)
);

create table schedule_entry_lecturer
(
    "lecturer"       text not null,
    "schedule_entry" uuid not null,
    PRIMARY KEY (lecturer, schedule_entry),
    FOREIGN KEY (lecturer) REFERENCES identity (id),
    FOREIGN KEY (schedule_entry) REFERENCES schedule_entry (id)
);

create table student_schedule_entry
(
    "id"             uuid PRIMARY KEY,
    "student"        text not null,
    "schedule_entry" uuid not null,
    FOREIGN KEY (schedule_entry) REFERENCES schedule_entry (id)
);

create
materialized view schedule_entry_view as
select schedule_entry.id                                 as s_id,
       schedule_entry.date                               as s_date,
       schedule_entry.start                              as s_start,
       schedule_entry.end                                as s_end,
       room_q.id                                         as room_id,
       room_q.identifier                                 as room_identifier,
       room_q.campus_id                                  as campus_id,
       room_q.campus_label                               as campus_label,
       course.part                                       as course_part,
       module.id                                         as module_id,
       module.label                                      as module_label,
       module.abbrev                                     as module_abbrev,
       module.language                                   as module_language,
       module_supervisor_q.identity_id                   as module_lecturer_id,
       module_supervisor_q.kind                          as module_lecturer_kind,
       module_supervisor_q.firstname                     as module_lecturer_firstname,
       module_supervisor_q.lastname                      as module_lecturer_lastname,
       module_supervisor_q.title                         as module_lecturer_title,
       module_in_study_program_q.mandatory               as mandatory,
       module_in_study_program_q.focus                   as focus,
       module_in_study_program_q.study_program_po_id     as po_id,
       module_in_study_program_q.study_program_po_number as po_number,
       module_in_study_program_q.study_program_id        as sp_id,
       module_in_study_program_q.study_program_de_label  as sp_de_label,
       module_in_study_program_q.study_program_en_label  as sp_en_label,
       module_in_study_program_q.degree_id               as degree_id,
       module_in_study_program_q.degree_de_label         as degree_label,
       module_in_study_program_q.teaching_unit_id        as teaching_unit_id,
       module_in_study_program_q.teaching_unit_de_label  as teaching_unit_de_label,
       module_in_study_program_q.teaching_unit_en_label  as teaching_unit_en_label,
       module_in_study_program_q.recommended_semester    as recommended_semester
from schedule_entry
         join (select room.id         as id,
                      room.label      as label,
                      room.identifier as identifier,
                      campus.id       as campus_id,
                      campus.label    as campus_label
               from room
                        join campus on room.campus = campus.id) room_q on room_q.id = schedule_entry.room
         join course on schedule_entry.course = course.id
         left join (select course_lecturer.course as course_id,
                           identity.id            as identity_id,
                           identity.kind          as kind,
                           identity.firstname     as firstname,
                           identity.lastname      as lastname,
                           identity.title         as title
                    from course_lecturer
                             join identity on identity.id = course_lecturer.lecturer) course_lecturer_q
                   on course_lecturer_q.course_id = course.id
         join module on course.module = module.id
         join module_in_study_program_placed_in_schedule_entry
              on module_in_study_program_placed_in_schedule_entry.schedule_entry = schedule_entry.id
         join (select module_in_study_program.id                   as id,
                      module_in_study_program.module               as module_id,
                      module_in_study_program.mandatory            as mandatory,
                      module_in_study_program.focus                as focus,
                      module_in_study_program.recommended_semester as recommended_semester,
                      study_program.id                             as study_program_id,
                      study_program.de_label                       as study_program_de_label,
                      study_program.en_label                       as study_program_en_label,
                      study_program.po_id                          as study_program_po_id,
                      study_program.po_number                      as study_program_po_number,
                      degree.id                                    as degree_id,
                      degree.de_label                              as degree_de_label,
                      teaching_unit.id                             as teaching_unit_id,
                      teaching_unit.de_label                       as teaching_unit_de_label,
                      teaching_unit.en_label                       as teaching_unit_en_label
               from module_in_study_program
                        join study_program on module_in_study_program.study_program = study_program.id
                        join degree on study_program.degree = degree.id
                        join teaching_unit on study_program.teaching_unit = teaching_unit.id) module_in_study_program_q
              on module_in_study_program_q.id = module_in_study_program_placed_in_schedule_entry.module_in_study_program
         join (select module_supervisor.module as module_id,
                      identity.id              as identity_id,
                      identity.kind            as kind,
                      identity.firstname       as firstname,
                      identity.lastname        as lastname,
                      identity.title           as title
               from module_supervisor
                        join identity on identity.id = module_supervisor.supervisor) module_supervisor_q
              on module_supervisor_q.module_id = module.id;

create view module_view as
select coalesce(json_agg(result), '[]'::json) as modules
from (select m as module,
             json_agg(json_build_object('id', sp.id, 'mandatory', msp.mandatory, 'focus', msp.focus)) as study_programs
      from module as m
          join module_in_study_program as msp
      on m.id = msp.module
          join study_program sp on sp.id = msp.study_program
      group by m.id) as result

-- !Downs

drop view schedule_entry_view if exists;
drop table student_schedule_entry if exists;
drop table schedule_entry_lecturer if exists;
drop table module_in_study_program_placed_in_schedule_entry if exists;
drop table schedule_entry if exists;
drop table room if exists;
drop table campus if exists;
drop table course_lecturer if exists;
drop table course if exists;
drop table semester if exists;
drop table module_supervisor if exists;
drop table identity if exists;
drop table module_in_study_program if exists;
drop table module_relation if exists;
drop table module if exists;
drop table season if exists;
drop table language if exists;
drop table study_program if exists;
drop table specialization if exists;
drop table degree if exists;
drop table teaching_unit if exists;
drop table faculty if exists;