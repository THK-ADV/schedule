-- !Ups

create table faculty
(
    "id"            uuid PRIMARY KEY,
    "last_modified" timestamp not null,
    "label"         text      not null,
    "abbreviation"  text      not null,
    "number"        integer   not null
);

create table teaching_unit
(
    "id"            uuid PRIMARY KEY,
    "last_modified" timestamp not null,
    "faculty"       uuid      not null,
    "label"         text      not null,
    "abbreviation"  text      not null,
    "number"        integer   not null,
    FOREIGN KEY (faculty) REFERENCES faculty (id)
);

create table graduation
(
    "id"            uuid PRIMARY KEY,
    "last_modified" timestamp not null,
    "label"         text      not null,
    "abbreviation"  text      not null
);

create table people
(
    "id"            uuid PRIMARY KEY,
    "last_modified" timestamp not null,
    "username"      text      not null,
    "firstname"     text      not null,
    "lastname"      text      not null,
    "status"        text      not null,
    "email"         text      not null,
    "title"         text null,
    "initials"      text null
);

create table semester
(
    "id"            uuid PRIMARY KEY,
    "last_modified" timestamp not null,
    "label"         text      not null,
    "abbreviation"  text      not null,
    "start"         date      not null,
    "end"           date      not null,
    "lecture_start" date      not null,
    "lecture_end"   date      not null
);

create table campus
(
    "id"            uuid PRIMARY KEY,
    "last_modified" timestamp not null,
    "label"         text      not null,
    "abbreviation"  text      not null
);

create table room
(
    "id"            uuid PRIMARY KEY,
    "last_modified" timestamp not null,
    "campus"        uuid      not null,
    "label"         text      not null,
    "abbreviation"  text      not null,
    FOREIGN KEY (campus) REFERENCES campus (id)
);

create table reservation
(
    "id"    uuid PRIMARY KEY,
    "room"  uuid not null,
    "date"  date not null,
    "start" time without time zone not null,
    "end"   time without time zone not null,
    "type"  text not null,
    "cause" text not null,
    FOREIGN KEY (room) REFERENCES room (id)
);

create table study_program
(
    "id"            uuid PRIMARY KEY,
    "last_modified" timestamp not null,
    "teaching_unit" uuid      not null,
    "graduation"    uuid      not null,
    "label"         text      not null,
    "abbreviation"  text      not null,
    "po_number"     integer   not null,
    "start"         date      not null,
    "end"           date null,
    FOREIGN KEY (teaching_unit) REFERENCES teaching_unit (id),
    FOREIGN KEY (graduation) REFERENCES graduation (id)
);

create table study_program_relation
(
    "parent" uuid not null,
    "child"  uuid not null,
    PRIMARY KEY (parent, child),
    FOREIGN KEY (parent) REFERENCES study_program (id),
    FOREIGN KEY (child) REFERENCES study_program (id)
);

create table study_program_supervisor
(
    "id"            uuid PRIMARY KEY,
    "study_program" uuid not null,
    "supervisor"    uuid not null,
    "role"          text not null,
    "start"         date not null,
    "end"           date null,
    FOREIGN KEY (study_program) REFERENCES study_program (id),
    FOREIGN KEY (supervisor) REFERENCES people (id)
);

create table module
(
    "id"                   uuid PRIMARY KEY,
    "last_modified"        timestamp not null,
    "label"                text      not null,
    "abbreviation"         text      not null,
    "ects"                 decimal   not null,
    "compendium_url"       text      not null,
    "recommended_semester" integer   not null,
    "language"             text      not null,
    "season"               text      not null,
    "type"                 text      not null
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
    "id"            uuid PRIMARY KEY,
    "last_modified" timestamp not null,
    "module"        uuid      not null,
    "study_program" uuid      not null,
    "mandatory"     boolean   not null,
    FOREIGN KEY (module) REFERENCES module (id),
    FOREIGN KEY (study_program) REFERENCES study_program (id)
);

create table module_supervisor
(
    "module"     uuid not null,
    "supervisor" uuid not null,
    "role"       text not null,
    PRIMARY KEY (module, supervisor, role),
    FOREIGN KEY (module) REFERENCES module (id),
    FOREIGN KEY (supervisor) REFERENCES people (id)
);

create table course
(
    "id"            uuid PRIMARY KEY,
    "last_modified" timestamp not null,
    "semester"      uuid      not null,
    "module"        uuid      not null,
    "interval"      text      not null,
    "type"          text      not null,
    FOREIGN KEY (semester) REFERENCES semester (id),
    FOREIGN KEY (module) REFERENCES module (id)
);

create table course_lecturer
(
    "lecturer" uuid not null,
    "course"   uuid not null,
    PRIMARY KEY (lecturer, course),
    FOREIGN KEY (lecturer) REFERENCES people (id),
    FOREIGN KEY (course) REFERENCES course (id)
);

create table schedule_entry
(
    "id"                      uuid PRIMARY KEY,
    "last_modified"           timestamp not null,
    "course"                  uuid      not null,
    "module_in_study_program" uuid      not null,
    "reservation"             uuid      not null,
    FOREIGN KEY (course) REFERENCES course (id),
    FOREIGN KEY (module_in_study_program) REFERENCES module_in_study_program (id),
    FOREIGN KEY (reservation) REFERENCES reservation (id)
);

create table schedule_entry_lecturer
(
    "lecturer"       uuid not null,
    "schedule_entry" uuid not null,
    PRIMARY KEY (lecturer, schedule_entry),
    FOREIGN KEY (lecturer) REFERENCES people (id),
    FOREIGN KEY (schedule_entry) REFERENCES schedule_entry (id)
);

create table student_schedule_entry
(
    "id"             uuid PRIMARY KEY,
    "last_modified"  timestamp not null,
    "student"        uuid      not null,
    "schedule_entry" uuid      not null,
    FOREIGN KEY (student) REFERENCES people (id),
    FOREIGN KEY (schedule_entry) REFERENCES schedule_entry (id)
);

-- !Downs
drop table student_schedule_entry if exists;
drop table schedule_entry_lecturer if exists;
drop table schedule_entry if exists;
drop table course_lecturer if exists;
drop table course if exists;
drop table module_supervisor if exists;
drop table module_in_study_program if exists;
drop table module_relation if exists;
drop table module if exists;
drop table study_program_supervisor if exists;
drop table study_program_relation if exists;
drop table study_program if exists;
drop table reservation if exists;
drop table room if exists;
drop table campus if exists;
drop table semester if exists;
drop table people if exists;
drop table graduation if exists;
drop table teaching_unit if exists;
drop table faculty if exists;