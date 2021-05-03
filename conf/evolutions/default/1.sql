# --- !Ups

create table faculty
(
    "id"           uuid PRIMARY KEY,
    "label"        text not null,
    "abbreviation" text not null
);

create table teaching_unit
(
    "id"           uuid PRIMARY KEY,
    "label"        text    not null,
    "abbreviation" text    not null,
    "number"       integer not null
);

create table teaching_unit_association
(
    "id"            uuid PRIMARY KEY,
    "faculty"       uuid not null,
    "teaching_unit" uuid not null,
    FOREIGN KEY (faculty) REFERENCES faculty (id),
    FOREIGN KEY (teaching_unit) REFERENCES teaching_unit (id)
);

create table study_program
(
    "id"           uuid PRIMARY KEY,
    "label"        text not null,
    "abbreviation" text not null,
    "graduation"   text not null
);

create table study_program_association
(
    "id"            uuid PRIMARY KEY,
    "teaching_unit" uuid not null,
    "study_program" uuid not null,
    FOREIGN KEY (teaching_unit) REFERENCES teaching_unit (id),
    FOREIGN KEY (study_program) REFERENCES study_program (id)
);

create table examination_regulation
(
    "id"                 uuid PRIMARY KEY,
    "study_program"      uuid not null,
    "label"              text not null,
    "abbreviation"       text not null,
    "accreditation_date" date not null,
    "activation_date"    date not null,
    "expiring_date"      date,
    FOREIGN KEY (study_program) REFERENCES study_program (id)
);

create table module
(
    "id"                     uuid PRIMARY KEY,
    "examination_regulation" uuid    not null,
    "course_manager"         uuid    not null,
    "label"                  text    not null,
    "abbreviation"           text    not null,
    "ects"                   decimal not null,
    "description_file_url"   text    not null,
    FOREIGN KEY (examination_regulation) REFERENCES examination_regulation (id),
    FOREIGN KEY (course_manager) REFERENCES user (id)
);

create table submodule
(
    "id"                   uuid PRIMARY KEY,
    "module"               uuid    not null,
    "label"                text    not null,
    "abbreviation"         text    not null,
    "recommended_semester" integer not null,
    "description_file_url" text    not null,
    "ects"                 decimal not null,
    FOREIGN KEY (module) REFERENCES module (id)
);

create table semester
(
    "id"           uuid PRIMARY KEY,
    "label"        text not null,
    "abbreviation" text not null,
    "start"        date not null,
    "end"          date not null,
    "exam_start"   date not null
);

create table user
(
    "id"        uuid PRIMARY KEY,
    "firstname" text not null,
    "lastname"  text not null,
    "status"    text not null,
    "email"     text not null,
    "title"     text,
    "initials"  text
);

create table course
(
    "id"          uuid PRIMARY KEY,
    "lecturer"    uuid not null,
    "semester"    uuid not null,
    "submodule"   uuid not null,
    "intervall"   text not null,
    "course_type" text not null,
    FOREIGN KEY (lecturer) REFERENCES user (id),
    FOREIGN KEY (semester) REFERENCES semester (id),
    FOREIGN KEY (submodule) REFERENCES submodule (id)
);

create table room
(
    "id"     uuid PRIMARY KEY,
    "label"  text    not null,
    "number" text    not null,
    "seats"  integer not null,
    "type"   text    not null
);

create table schedule
(
    "id"         uuid PRIMARY KEY,
    "course"     uuid not null,
    "room"       uuid not null,
    "date"       date not null,
    "start_time" time without time zone not null,
    "end_time"   time without time zone not null,
    FOREIGN KEY (course) REFERENCES course (id),
    FOREIGN KEY (room) REFERENCES room (id)
);

create table student_schedule
(
    "id"       uuid PRIMARY KEY,
    "student"  uuid not null,
    "schedule" uuid not null,
    FOREIGN KEY (student) REFERENCES user (id),
    FOREIGN KEY (schedule) REFERENCES schedule (id)
);

# --- !Downs
drop table student_schedule if exists;
drop table schedule if exists;
drop table room if exists;
drop table course if exists;
drop table user if exists;
drop table semester if exists;
drop table submodule if exists
drop table module if exists;
drop table examination_regulation if exists;
drop table study_program_association if exists;
drop table study_program if exists;
drop table teaching_unit_association if exists;
drop table teaching_unit if exists;
drop table faculty if exists;