drop materialized view if exists schedule_entry_view;
drop view if exists module_view;

create materialized view schedule_entry_view as
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
(
select coalesce(json_agg(json_build_object('id', data.module -> 'id', 'label', data.module -> 'label', 'parts',
                                           data.module -> 'parts', 'abbrev', data.module -> 'abbrev', 'season',
                                           data.module -> 'season', 'language', data.module -> 'language',
                                           'studyPrograms', data.study_programs)), '[]' ::json) as modules
from (select to_json(m)                             as module,
             json_agg(json_build_object('id', sp.id, 'mandatory', msp.mandatory, 'focus',
                                        msp.focus)) as study_programs
      from module as m
               join module_in_study_program as msp
                    on m.id = msp.module
               join study_program sp on sp.id = msp.study_program
      group by m.id) as data
    );