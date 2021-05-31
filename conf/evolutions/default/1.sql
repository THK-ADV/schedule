# --- !Ups

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

create table study_program
(
    "id"            uuid PRIMARY KEY,
    "last_modified" timestamp not null,
    "teaching_unit" uuid      not null,
    "label"         text      not null,
    "abbreviation"  text      not null,
    "graduation"    uuid      not null,
    FOREIGN KEY (teaching_unit) REFERENCES teaching_unit (id),
    FOREIGN KEY (graduation) REFERENCES graduation (id)
);

create table examination_regulation
(
    "id"            uuid PRIMARY KEY,
    "last_modified" timestamp not null,
    "study_program" uuid      not null,
    "number"        integer   not null,
    "start"         date      not null,
    "end"           date null,
    FOREIGN KEY (study_program) REFERENCES study_program (id)
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

create table module
(
    "id"                   uuid PRIMARY KEY,
    "last_modified"        timestamp not null,
    "course_manager"       uuid      not null,
    "label"                text      not null,
    "abbreviation"         text      not null,
    "ects"                 decimal   not null,
    "description_file_url" text      not null,
    FOREIGN KEY (course_manager) REFERENCES people (id)
);

create table module_examination_regulation
(
    "id"                     uuid PRIMARY KEY,
    "last_modified"          timestamp not null,
    "module"                 uuid      not null,
    "examination_regulation" uuid      not null,
    "mandatory"              boolean   not null,
    FOREIGN KEY (examination_regulation) REFERENCES examination_regulation (id),
    FOREIGN KEY (module) REFERENCES module (id)
);

create table submodule
(
    "id"                   uuid PRIMARY KEY,
    "last_modified"        timestamp not null,
    "module"               uuid      not null,
    "label"                text      not null,
    "abbreviation"         text      not null,
    "recommended_semester" integer   not null,
    "description_file_url" text      not null,
    "ects"                 decimal   not null,
    "language"             text      not null,
    "season"               text      not null,
    FOREIGN KEY (module) REFERENCES module (id)
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

create table course
(
    "id"            uuid PRIMARY KEY,
    "last_modified" timestamp not null,
    "lecturer"      uuid      not null,
    "semester"      uuid      not null,
    "submodule"     uuid      not null,
    "interval"      text      not null,
    "course_type"   text      not null,
    FOREIGN KEY (lecturer) REFERENCES people (id),
    FOREIGN KEY (semester) REFERENCES semester (id),
    FOREIGN KEY (submodule) REFERENCES submodule (id)
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

create table schedule
(
    "id"                            uuid PRIMARY KEY,
    "last_modified"                 timestamp not null,
    "course"                        uuid      not null,
    "room"                          uuid      not null,
    "module_examination_regulation" uuid      not null,
    "date"                          date      not null,
    "start"                         time without time zone not null,
    "end"                           time without time zone not null,
    FOREIGN KEY (course) REFERENCES course (id),
    FOREIGN KEY (room) REFERENCES room (id),
    FOREIGN KEY (module_examination_regulation) REFERENCES module_examination_regulation (id)
);

create table student_schedule
(
    "id"            uuid PRIMARY KEY,
    "last_modified" timestamp not null,
    "student"       uuid      not null,
    "schedule"      uuid      not null,
    FOREIGN KEY (student) REFERENCES people (id),
    FOREIGN KEY (schedule) REFERENCES schedule (id)
);

# --- !Downs
drop table student_schedule if exists;
drop table schedule if exists;
drop table room if exists;
drop table campus if exists;
drop table course if exists;
drop table semester if exists;
drop table submodule if exists
drop table module_examination_regulation if exists
drop table module if exists;
drop table people if exists;
drop table examination_regulation if exists;
drop table study_program if exists;
drop table graduation if exists;
drop table teaching_unit if exists;
drop table faculty if exists;