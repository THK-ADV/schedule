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

-- !Downs

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