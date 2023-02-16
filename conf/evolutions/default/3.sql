-- !Ups

alter table study_program
    add parent uuid null;
alter table study_program
    add constraint studyProgram foreign key (parent) REFERENCES study_program (id);

-- !Downs

alter table study_program
    drop constraint studyProgram cascade;
alter table study_program
    drop column parent;