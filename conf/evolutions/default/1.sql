-- !Ups
CREATE TABLE faculty
(
    "id"       text PRIMARY KEY,
    "de_label" text NOT NULL,
    "en_label" text NOT NULL
);
CREATE TABLE teaching_unit
(
    "id"       uuid PRIMARY KEY,
    "faculty"  text NOT NULL,
    "de_label" text NOT NULL,
    "en_label" text NOT NULL,
    FOREIGN KEY (faculty) REFERENCES FACULTY (id)
);
CREATE TABLE degree
(
    "id"       text PRIMARY KEY,
    "de_label" text NOT NULL,
    "de_desc"  text NOT NULL,
    "en_label" text NOT NULL,
    "en_desc"  text NOT NULL
);
CREATE TABLE specialization
(
    "id"    text PRIMARY KEY,
    "label" text NOT NULL
);
CREATE TABLE study_program
(
    "id"                uuid PRIMARY KEY,
    "teaching_unit"     uuid    NOT NULL,
    "degree"            text    NOT NULL,
    "de_label"          text    NOT NULL,
    "en_label"          text    NOT NULL,
    "abbrev"            text    NOT NULL,
    "po_id"             text    NOT NULL,
    "po_number"         integer NOT NULL,
    "specialization_id" text    NULL,
    FOREIGN KEY (teaching_unit) REFERENCES TEACHING_UNIT (id),
    FOREIGN KEY (degree) REFERENCES DEGREE (id),
    FOREIGN KEY (specialization_id) REFERENCES SPECIALIZATION (id)
);
CREATE TABLE language
(
    "id"       text PRIMARY KEY,
    "de_label" text NOT NULL,
    "en_label" text NOT NULL
);
CREATE TABLE season
(
    "id"       text PRIMARY KEY,
    "de_label" text NOT NULL,
    "en_label" text NOT NULL
);
CREATE TABLE module
(
    "id"       uuid PRIMARY KEY,
    "label"    text NOT NULL,
    "abbrev"   text NOT NULL,
    "language" text NOT NULL,
    "season"   text NOT NULL,
    "parts"    text NOT NULL,
    FOREIGN KEY (language) REFERENCES LANGUAGE (id),
    FOREIGN KEY (season) REFERENCES SEASON (id)
);
CREATE TABLE module_relation
(
    "parent" uuid NOT NULL,
    "child"  uuid NOT NULL,
    PRIMARY KEY (parent, child),
    FOREIGN KEY (parent) REFERENCES MODULE (id),
    FOREIGN KEY (child) REFERENCES MODULE (id)
);
CREATE TABLE module_in_study_program
(
    "id"                   uuid PRIMARY KEY,
    "module"               uuid    NOT NULL,
    "study_program"        uuid    NOT NULL,
    "mandatory"            boolean NOT NULL,
    "focus"                boolean NOT NULL,
    "recommended_semester" text    NOT NULL,
    FOREIGN KEY (module) REFERENCES MODULE (id),
    FOREIGN KEY (study_program) REFERENCES STUDY_PROGRAM (id)
);
CREATE TABLE identity
(
    "id"        text PRIMARY KEY,
    "firstname" text NOT NULL,
    "lastname"  text NOT NULL,
    "title"     text NOT NULL,
    "abbrev"    text NOT NULL,
    "kind"      text NOT NULL,
    "campus_id" text NOT NULL
);
CREATE TABLE module_supervisor
(
    "module"     uuid NOT NULL,
    "supervisor" text NOT NULL,
    PRIMARY KEY (module, supervisor),
    FOREIGN KEY (module) REFERENCES MODULE (id),
    FOREIGN KEY (supervisor) REFERENCES IDENTITY (id)
);
CREATE TABLE semester
(
    "id"            uuid PRIMARY KEY,
    "de_label"      text NOT NULL,
    "en_label"      text NOT NULL,
    "abbrev"        text NOT NULL,
    "start"         date NOT NULL,
    "end"           date NOT NULL,
    "lecture_start" date NOT NULL,
    "lecture_end"   date NOT NULL
);
CREATE TYPE course_id AS ENUM ('lecture','seminar','practical','exercise','tutorial');
CREATE CAST (character varying AS course_id) WITH INOUT AS ASSIGNMENT;
CREATE TABLE course_type
(
    "id"       course_id PRIMARY KEY,
    "de_label" text NOT NULL,
    "en_label" text NOT NULL
);
CREATE TABLE course
(
    "id"        uuid PRIMARY KEY,
    "semester"  uuid      NOT NULL,
    "module"    uuid      NOT NULL,
    "course_id" course_id NOT NULL,
    FOREIGN KEY (semester) REFERENCES SEMESTER (id),
    FOREIGN KEY (module) REFERENCES MODULE (id)
);
CREATE TABLE course_lecturer
(
    "lecturer" text NOT NULL,
    "course"   uuid NOT NULL,
    PRIMARY KEY (lecturer, course),
    FOREIGN KEY (lecturer) REFERENCES IDENTITY (id),
    FOREIGN KEY (course) REFERENCES COURSE (id)
);
CREATE TABLE campus
(
    "id"     uuid PRIMARY KEY,
    "label"  text NOT NULL,
    "abbrev" text NOT NULL
);
CREATE TABLE room
(
    "id"         uuid PRIMARY KEY,
    "campus"     uuid NOT NULL,
    "label"      text NOT NULL,
    "identifier" text NOT NULL,
    "type"       text NOT NULL,
    "capacity"   int  NOT NULL,
    FOREIGN KEY (campus) REFERENCES CAMPUS (id)
);
CREATE TABLE schedule_entry
(
    "id"     uuid PRIMARY KEY,
    "course" uuid                        NOT NULL,
    "start"  timestamp without time zone NOT NULL,
    "end"    timestamp without time zone NOT NULL,
    FOREIGN KEY (course) REFERENCES COURSE (id)
);
CREATE TABLE schedule_entry_room
(
    "schedule_entry" uuid NOT NULL,
    "room"           uuid NOT NULL,
    PRIMARY KEY (schedule_entry, room),
    FOREIGN KEY (schedule_entry) REFERENCES SCHEDULE_ENTRY (id),
    FOREIGN KEY (room) REFERENCES ROOM (id)
);
CREATE TABLE module_in_study_program_placed_in_schedule_entry
(
    "module_in_study_program" uuid NOT NULL,
    "schedule_entry"          uuid NOT NULL,
    PRIMARY KEY (module_in_study_program, schedule_entry),
    FOREIGN KEY (module_in_study_program) REFERENCES MODULE_IN_STUDY_PROGRAM (id),
    FOREIGN KEY (schedule_entry) REFERENCES SCHEDULE_ENTRY (id)
);
CREATE TABLE schedule_entry_lecturer
(
    "lecturer"       text NOT NULL,
    "schedule_entry" uuid NOT NULL,
    PRIMARY KEY (lecturer, schedule_entry),
    FOREIGN KEY (lecturer) REFERENCES IDENTITY (id),
    FOREIGN KEY (schedule_entry) REFERENCES SCHEDULE_ENTRY (id)
);
CREATE TABLE student_schedule_entry
(
    "id"             uuid PRIMARY KEY,
    "student"        text NOT NULL,
    "schedule_entry" uuid NOT NULL,
    FOREIGN KEY (schedule_entry) REFERENCES SCHEDULE_ENTRY (id)
);
CREATE TYPE semester_plan_type_id AS ENUM ('exam','lecture','block','project','closed_building');
CREATE CAST (character varying AS semester_plan_type_id) WITH INOUT AS ASSIGNMENT;
CREATE TABLE semester_plan_type
(
    "id"       semester_plan_type_id PRIMARY KEY,
    "de_label" text NOT NULL,
    "en_label" text NOT NULL
);
create table semester_plan_entry
(
    "id"             uuid PRIMARY KEY,
    "start"          timestamp without time zone NOT NULL,
    "end"            timestamp without time zone NOT NULL,
    "type"           semester_plan_type_id       not null,
    "semester"       uuid                        not null,
    "teaching_unit"  uuid                        null,
    "semester_index" text                        null,
    FOREIGN KEY (semester) REFERENCES semester (id),
    FOREIGN KEY (teaching_unit) REFERENCES teaching_unit (id)
);
create table legal_holiday
(
    "date"  date     NOT NULL,
    "label" text     not null,
    "year"  smallint not null,
    PRIMARY KEY ("date", "label")
);
create table campus_event
(
    "start" timestamp without time zone NOT NULL,
    "end"   timestamp without time zone NOT NULL,
    "label" text                        not null,
    PRIMARY KEY ("start", "end", "label")
);
create table school_holiday
(
    "start" timestamp without time zone NOT NULL,
    "end"   timestamp without time zone NOT NULL,
    "label" text                        not null,
    PRIMARY KEY ("start", "end", "label")
);
--  semester_plan_entry = start, end, type, teaching unit, semester
--     type = prüfungswoche | vorlesungszeit | blockwoche | projektwoche | gebäude geschlossen
--     teaching unit & semester = prüfungswoche | blockwoche | projektwoche
--     null & null = vorlesungszeit | gebäude geschlossen
--
--  legal_holiday = label, start, end
--  campus_event = label, start, end
--  school_holiday = label, start, end
CREATE VIEW module_view AS
SELECT COALESCE(
               JSON_AGG(
                       JSON_BUILD_OBJECT(
                               'id',
                               data.module -> 'id',
                               'label',
                               data.module -> 'label',
                               'parts',
                               data.module -> 'parts',
                               'abbrev',
                               data.module -> 'abbrev',
                               'season',
                               data.module -> 'season',
                               'language',
                               data.module -> 'language',
                               'studyPrograms',
                               data.study_programs
                       )
               ),
               '[]' :: json
       ) AS modules
FROM (SELECT TO_JSON(m)                                                                               AS module,
             JSON_AGG(JSON_BUILD_OBJECT('id', sp.id, 'mandatory', msp.mandatory, 'focus', msp.focus)) AS study_programs
      FROM module AS m
               JOIN module_in_study_program AS msp ON m.id = msp.module
               JOIN study_program sp ON sp.id = msp.study_program
      GROUP BY m.id) AS data;
CREATE VIEW study_program_view AS
SELECT JSONB_BUILD_OBJECT(
               'id',
               study_program.id,
               'deLabel',
               study_program.de_label,
               'enLabel',
               study_program.en_label,
               'poId',
               study_program.po_id,
               'poNumber',
               study_program.po_number,
               'teachingUnit',
               study_program.teaching_unit,
               'degree',
               JSONB_BUILD_OBJECT('id', degree.id, 'label', degree.de_label),
               'specialization',
               CASE
                   WHEN specialization.id IS NULL THEN NULL
                   ELSE JSONB_BUILD_OBJECT('id', specialization.id, 'label', specialization.label)
                   END
       ) AS study_programs
FROM study_program
         JOIN degree ON study_program.degree = degree.id
         LEFT JOIN specialization ON study_program.specialization_id = specialization.id;
CREATE VIEW study_program_view_en AS
SELECT COALESCE(
               JSON_AGG(TO_JSONB(study_programs) - 'deLabel' - 'enLabel' ||
                        JSONB_BUILD_OBJECT('label', study_programs -> 'enLabel')),
               '[]' :: json
       ) AS study_programs
FROM study_program_view AS study_programs;
CREATE VIEW study_program_view_de AS
SELECT COALESCE(
               JSON_AGG(TO_JSONB(study_programs) - 'deLabel' - 'enLabel' ||
                        JSONB_BUILD_OBJECT('label', study_programs -> 'deLabel')),
               '[]' :: json
       ) AS study_programs
FROM study_program_view AS study_programs;
CREATE VIEW course_view AS
SELECT c.id        AS id,
       c.semester  AS semester,
       c.module    AS module,
       c.course_id AS course_id,
       ct.de_label AS course_de_label,
       ct.en_label AS course_en_label
FROM course c
         JOIN course_type ct ON c.course_id = ct.id;
CREATE materialized VIEW schedule_entry_view AS
SELECT schedule_entry.id                                 AS s_id,
       schedule_entry.start                              AS s_start,
       schedule_entry.end                                AS s_end,
       room.id                                           AS room_id,
       room.identifier                                   AS room_identifier,
       campus.id                                         AS campus_id,
       campus.label                                      AS campus_label,
       course_view.course_de_label                       AS course_de_label,
       course_view.course_en_label                       AS course_en_label,
       module.id                                         AS module_id,
       module.label                                      AS module_label,
       module.abbrev                                     AS module_abbrev,
       module.language                                   AS module_language,
       module_supervisor_q.identity_id                   AS module_lecturer_id,
       module_supervisor_q.kind                          AS module_lecturer_kind,
       module_supervisor_q.firstname                     AS module_lecturer_firstname,
       module_supervisor_q.lastname                      AS module_lecturer_lastname,
       module_supervisor_q.title                         AS module_lecturer_title,
       module_in_study_program_q.mandatory               AS mandatory,
       module_in_study_program_q.focus                   AS focus,
       module_in_study_program_q.study_program_po_id     AS po_id,
       module_in_study_program_q.study_program_po_number AS po_number,
       module_in_study_program_q.study_program_id        AS sp_id,
       module_in_study_program_q.study_program_de_label  AS sp_de_label,
       module_in_study_program_q.study_program_en_label  AS sp_en_label,
       module_in_study_program_q.degree_id               AS degree_id,
       module_in_study_program_q.degree_de_label         AS degree_label,
       module_in_study_program_q.teaching_unit_id        AS teaching_unit_id,
       module_in_study_program_q.teaching_unit_de_label  AS teaching_unit_de_label,
       module_in_study_program_q.teaching_unit_en_label  AS teaching_unit_en_label,
       module_in_study_program_q.recommended_semester    AS recommended_semester
FROM schedule_entry
         JOIN schedule_entry_room ON schedule_entry_room.schedule_entry = schedule_entry.id
         JOIN room ON room.id = schedule_entry_room.room
         JOIN campus ON room.campus = campus.id
         JOIN course_view ON schedule_entry.course = course_view.id
         LEFT JOIN (SELECT course_lecturer.course AS course_id,
                           identity.id            AS identity_id,
                           identity.kind          AS kind,
                           identity.firstname     AS firstname,
                           identity.lastname      AS lastname,
                           identity.title         AS title
                    FROM course_lecturer
                             JOIN identity ON identity.id = course_lecturer.lecturer) course_lecturer_q
                   ON course_lecturer_q.course_id = course_view.id
         JOIN module ON course_view.module = module.id
         JOIN module_in_study_program_placed_in_schedule_entry
              ON module_in_study_program_placed_in_schedule_entry.schedule_entry = schedule_entry.id
         JOIN (SELECT module_in_study_program.id                   AS id,
                      module_in_study_program.module               AS module_id,
                      module_in_study_program.mandatory            AS mandatory,
                      module_in_study_program.focus                AS focus,
                      module_in_study_program.recommended_semester AS recommended_semester,
                      study_program.id                             AS study_program_id,
                      study_program.de_label                       AS study_program_de_label,
                      study_program.en_label                       AS study_program_en_label,
                      study_program.po_id                          AS study_program_po_id,
                      study_program.po_number                      AS study_program_po_number,
                      degree.id                                    AS degree_id,
                      degree.de_label                              AS degree_de_label,
                      teaching_unit.id                             AS teaching_unit_id,
                      teaching_unit.de_label                       AS teaching_unit_de_label,
                      teaching_unit.en_label                       AS teaching_unit_en_label
               FROM module_in_study_program
                        JOIN study_program ON module_in_study_program.study_program = study_program.id
                        JOIN degree ON study_program.degree = degree.id
                        JOIN teaching_unit ON study_program.teaching_unit = teaching_unit.id) module_in_study_program_q
              ON module_in_study_program_q.id = module_in_study_program_placed_in_schedule_entry.module_in_study_program
         JOIN (SELECT module_supervisor.module AS module_id,
                      identity.id              AS identity_id,
                      identity.kind            AS kind,
                      identity.firstname       AS firstname,
                      identity.lastname        AS lastname,
                      identity.title           AS title
               FROM module_supervisor
                        JOIN identity ON identity.id = module_supervisor.supervisor) module_supervisor_q
              ON module_supervisor_q.module_id = module.id;
INSERT INTO course_type
VALUES ('lecture',
        'Vorlesung',
        'Lecture')
        ,
       ('seminar',
        'Seminar',
        'Seminar')
        ,
       ('practical',
        'Praktikum',
        'Lab')
        ,
       ('exercise',
        'Übung',
        'Exercise')
        ,
       ('tutorial',
        'Tutorium',
        'Tutorial');
INSERT INTO semester_plan_type
VALUES ('exam',
        'Prüfungszeitraum',
        'Exam period')
        ,
       ('lecture',
        'Vorlesungszeit',
        'Lecture period')
        ,
       ('block',
        'Blockveranstaltung',
        'Block period')
        ,
       ('project',
        'Projektwoche',
        'Project period')
        ,
       ('closed_building',
        'Gebäude geschlossen',
        'Building closed');
-- !Downs
DROP materialized VIEW schedule_entry_view;
DROP TABLE student_schedule_entry;
DROP TABLE schedule_entry_lecturer;
DROP TABLE module_in_study_program_placed_in_schedule_entry;
DROP TABLE schedule_entry_room;
DROP TABLE schedule_entry;
DROP TABLE room;
DROP TABLE campus;
DROP TABLE course_lecturer;
DROP TABLE course_type;
DROP TABLE course;
DROP TABLE semester;
DROP TABLE module_supervisor;
DROP TABLE identity;
DROP TABLE module_in_study_program;
DROP TABLE module_relation;
DROP TABLE module;
DROP TABLE season;
DROP TABLE language;
DROP TABLE study_program;
DROP TABLE specialization;
DROP TABLE degree;
DROP TABLE teaching_unit;
DROP TABLE faculty