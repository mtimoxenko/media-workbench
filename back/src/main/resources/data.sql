-- Insert users with roles
INSERT INTO usr (name, surname, email, password, is_admin, role, shift)
VALUES
    ('Rick', 'Deckard', 'deckard@bladerunner.com', 'voightkampff', false, 'BLADE_RUNNER', 'NIGHT'),
    ('K', 'Joe', 'k@bladerunner2049.com', 'baseline', false, 'BLADE_RUNNER', 'NIGHT'),
    ('Roy', 'Batty', 'batty@nexus6.com', 'morelife', false, 'REPLICANT', 'DAY'),
    ('Pris', 'Stratton', 'pris@nexus6.com', 'basicpleasure', false, 'REPLICANT', 'DAY'),
    ('J.F.', 'Sebastian', 'sebastian@tyrell.com', 'chessmaster', false, 'ENGINEER', 'EVENING'),
    ('Eldon', 'Tyrell', 'tyrell@tyrell.com', 'replicantcreator', false, 'ENGINEER', 'EVENING');



-- Insert specific tasks
INSERT INTO task (name, description, creator_id, creation_date, status, category, shift_status)
VALUES
    ('Replicant Identification', 'Deckard must use the Voight-Kampff test to identify possible replicants in the city.', 1, '2024-01-20 09:00:00', 'IN_PROGRESS', 'CUSTOM', 'NIGHT'),
    ('Baseline Test Administration', 'K is tasked with administering baseline tests to ensure no replicants are deviating from their designed emotional responses.', 2, '2024-01-21 10:00:00', 'IN_PROGRESS', 'CUSTOM', 'NIGHT'),
    ('Replicant Freedom Movement', 'Roy Batty to lead a movement for replicant rights and freedom.', 3, '2024-01-22 11:00:00', 'IN_PROGRESS', 'CUSTOM', 'DAY'),
    ('Replicant Integration Study', 'Pris Stratton to analyze and report on replicant integration within human society.', 4, '2024-01-23 12:00:00', 'IN_PROGRESS', 'CUSTOM', 'DAY'),
    ('Replicant Design Improvement', 'J.F. Sebastian to work on enhancements in replicant design for increased longevity.', 5, '2024-01-24 13:00:00', 'IN_PROGRESS', 'CUSTOM', 'EVENING'),
    ('Tyrell Corporation Strategy', 'Eldon Tyrell to develop new strategies for the Tyrell Corporation’s future replicant models.', 6, '2024-01-25 14:00:00', 'IN_PROGRESS', 'CUSTOM', 'EVENING');


-- Insert assignments of tasks to users (UserTask)
INSERT INTO user_task (user_id, task_id, user_task_status, assignment_date, assigned_by)
VALUES
    (1, 1, 'ASSIGNED', '2024-01-20 14:00:00', 2), -- Deckard assigned Replicant Identification
    (2, 2, 'ASSIGNED', '2024-01-21 15:00:00', 1), -- K assigned Baseline Test Administration
    (3, 3, 'ASSIGNED', '2024-01-22 09:30:00', 4), -- Roy Batty assigned Replicant Freedom Movement
    (4, 4, 'ASSIGNED', '2024-01-23 16:45:00', 3), -- Pris Stratton assigned Replicant Integration Study
    (5, 5, 'ASSIGNED', '2024-01-24 17:00:00', 6), -- J.F. Sebastian assigned Replicant Design Improvement
    (6, 6, 'ASSIGNED', '2024-01-25 10:00:00', 5); -- Eldon Tyrell assigned Tyrell Corporation Strategy


-- Insert specific comments
INSERT INTO comment (user_id, task_id, text, timestamp)
VALUES
    (1, 1, 'Administering Voight-Kampff test to suspected replicants. Awaiting results.', '2024-01-21 09:15:00'),
    (2, 2, 'Baseline tests conducted. No deviations detected so far.', '2024-01-22 10:30:00'),
    (3, 3, 'Organizing replicant rights movement. Gathering support.', '2024-01-23 11:45:00'),
    (4, 4, 'Studying human-replicant social dynamics. Compiling data.', '2024-01-24 14:30:00'),
    (5, 5, 'Working on next-gen replicant design. Encountering complex challenges.', '2024-01-25 15:45:00'),
    (6, 6, 'Strategizing for Tyrell Corp’s future. New model concepts underway.', '2024-01-26 09:20:00'),
    (1, 1, 'Several suspects cleared. Continuing the search.', '2024-01-27 10:35:00'),
    (2, 2, 'Re-evaluating baseline test parameters for improved accuracy.', '2024-01-28 12:00:00'),
    (3, 3, 'Movement gaining traction. Planning next steps.', '2024-01-29 13:30:00'),
    (4, 4, 'Discovering intriguing patterns in replicant integration.', '2024-01-30 15:00:00'),
    (5, 5, 'Prototype development in progress. Testing advanced AI integration.', '2024-01-31 16:45:00');

