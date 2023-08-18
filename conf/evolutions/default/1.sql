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

create table grade
(
    "id"       text PRIMARY KEY,
    "de_label" text not null,
    "de_desc"  text not null,
    "en_label" text not null,
    "en_desc"  text not null
);

create table study_program
(
    "id"            text PRIMARY KEY,
    "teaching_unit" uuid    not null,
    "grade"         text    not null,
    "de_label"      text    not null,
    "en_label"      text    not null,
    "abbrev"        text    not null,
    "po_number"     integer not null,
    "start"         date    not null,
    "end"           date null,
    FOREIGN KEY (teaching_unit) REFERENCES teaching_unit (id),
    FOREIGN KEY (grade) REFERENCES grade (id)
);

create table study_program_relation
(
    "parent" text not null,
    "child"  text not null,
    PRIMARY KEY (parent, child),
    FOREIGN KEY (parent) REFERENCES study_program (id),
    FOREIGN KEY (child) REFERENCES study_program (id)
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
    "label"    text    not null,
    "abbrev"   text    not null,
    "language" text    not null,
    "season"   text    not null,
    "type"     text    not null,
    "active"   boolean not null,
    "parts"    text    not null,
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
    "study_program"        text    not null,
    "mandatory"            boolean not null,
    "recommended_semester" text    not null,
    FOREIGN KEY (module) REFERENCES module (id),
    FOREIGN KEY (study_program) REFERENCES study_program (id)
);

create table people
(
    "id"        text PRIMARY KEY,
    "username"  text    not null,
    "firstname" text    not null,
    "lastname"  text    not null,
    "active"    boolean not null,
    "kind"      text    not null,
    "abbrev"    text    not null,
    "title"     text null
);

create table module_supervisor
(
    "module"     uuid not null,
    "supervisor" text not null,
    PRIMARY KEY (module, supervisor),
    FOREIGN KEY (module) REFERENCES module (id),
    FOREIGN KEY (supervisor) REFERENCES people (id)
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
    "id"            uuid PRIMARY KEY,
    "semester"      uuid not null,
    "module"        uuid not null,
    "study_program" text not null,
    "part"          text not null,
    FOREIGN KEY (semester) REFERENCES semester (id),
    FOREIGN KEY (module) REFERENCES module (id),
    FOREIGN KEY (study_program) REFERENCES study_program (id)
);

create table course_lecturer
(
    "lecturer" text not null,
    "course"   uuid not null,
    PRIMARY KEY (lecturer, course),
    FOREIGN KEY (lecturer) REFERENCES people (id),
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

create table reservation
(
    "id"          uuid PRIMARY KEY,
    "room"        uuid not null,
    "date"        date not null,
    "start"       time without time zone not null,
    "end"         time without time zone not null,
    "source"      text not null,
    "description" text not null,
    FOREIGN KEY (room) REFERENCES room (id)
);

create table schedule_entry
(
    "id"          uuid PRIMARY KEY,
    "course"      uuid not null,
    "reservation" uuid not null,
    FOREIGN KEY (course) REFERENCES course (id),
    FOREIGN KEY (reservation) REFERENCES reservation (id)
);

create table schedule_entry_lecturer
(
    "lecturer"       text not null,
    "schedule_entry" uuid not null,
    PRIMARY KEY (lecturer, schedule_entry),
    FOREIGN KEY (lecturer) REFERENCES people (id),
    FOREIGN KEY (schedule_entry) REFERENCES schedule_entry (id)
);

create table student_schedule_entry
(
    "id"             uuid PRIMARY KEY,
    "student"        text not null,
    "schedule_entry" uuid not null,
    FOREIGN KEY (schedule_entry) REFERENCES schedule_entry (id)
);

-- !Downs

drop table student_schedule_entry if exists;
drop table schedule_entry_lecturer if exists;
drop table schedule_entry if exists;
drop table reservation if exists;
drop table room if exists;
drop table campus if exists;
drop table course_lecturer if exists;
drop table course if exists;
drop table semester if exists;
drop table module_supervisor if exists;
drop table people if exists;
drop table module_in_study_program if exists;
drop table module_relation if exists;
drop table module if exists;
drop table season if exists;
drop table language if exists;
drop table study_program_relation if exists;
drop table study_program if exists;
drop table grade if exists;
drop table teaching_unit if exists;
drop table faculty if exists;