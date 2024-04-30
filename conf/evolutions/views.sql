create view module_view as
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
      group by m.id) as data;

create view study_program_view as
select coalesce(json_agg(jsonb_build_object(
        'id', study_program.id,
        'deLabel', study_program.de_label,
        'enLabel', study_program.en_label,
        'poId', study_program.po_id,
        'poNumber', study_program.po_number,
        'teachingUnit', study_program.teaching_unit,
        'degree', jsonb_build_object('id', degree.id, 'label', degree.de_label),
        'specialization', case
                              when specialization.id is null then null
                              else jsonb_build_object('id', specialization.id, 'label', specialization.label) end
                         )), '[]' ::json) as study_programs
from study_program
         join degree on study_program.degree = degree.id
         left join specialization on study_program.specialization_id = specialization.id;