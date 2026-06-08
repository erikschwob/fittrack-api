-- A starter catalogue of common compound and accessory lifts so the API is
-- usable immediately after a fresh deploy.

INSERT INTO exercise (name, muscle_group, category, description) VALUES
    ('Back Squat',        'LEGS',      'STRENGTH', 'Barbell squat, high or low bar'),
    ('Front Squat',       'LEGS',      'STRENGTH', 'Barbell squat with anterior rack position'),
    ('Deadlift',          'BACK',      'STRENGTH', 'Conventional barbell deadlift'),
    ('Romanian Deadlift', 'LEGS',      'STRENGTH', 'Hip-hinge with minimal knee bend'),
    ('Bench Press',       'CHEST',     'STRENGTH', 'Flat barbell bench press'),
    ('Incline Bench Press','CHEST',    'STRENGTH', 'Barbell bench on an inclined bench'),
    ('Overhead Press',    'SHOULDERS', 'STRENGTH', 'Standing barbell shoulder press'),
    ('Pull-up',           'BACK',      'STRENGTH', 'Bodyweight or weighted vertical pull'),
    ('Barbell Row',       'BACK',      'STRENGTH', 'Bent-over barbell row'),
    ('Barbell Curl',      'ARMS',      'STRENGTH', 'Standing biceps curl'),
    ('Triceps Pushdown',  'ARMS',      'STRENGTH', 'Cable triceps extension'),
    ('Plank',             'CORE',      'MOBILITY', 'Isometric core hold'),
    ('Running',           'CARDIO',    'CARDIO',   'Steady-state or interval running'),
    ('Rowing Ergometer',  'FULL_BODY', 'CARDIO',   'Concept2-style rowing machine');
