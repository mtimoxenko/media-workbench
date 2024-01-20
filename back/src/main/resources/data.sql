-- Insert users with roles
INSERT INTO usr (name, surname, email, password, is_admin, role, shift)
VALUES
    ('Rick', 'Deckard', 'deckard@bladerunner.com', 'replicant', false, 'REPLICANT_HUNTER', 'NIGHT'),
    ('Paul', 'Atreides', 'atreides@dune.com', 'spiceflow', false, 'SPICE_OVERSEER', 'NIGHT'),
    ('Kris', 'Kelvin', 'kelvin@solaris.com', 'oceanplanet', false, 'COSMIC_RESEARCHER', 'NIGHT'),
    ('Roy', 'Batty', 'batty@bladerunner.com', 'nexus6', false, 'REVOLUTIONARY_LEADER', 'DAY'),
    ('Duncan', 'Idaho', 'idaho@dune.com', 'fremen', false, 'DUNE_STRATEGIST', 'DAY'),
    ('Hari', 'Seldon', 'seldon@solaris.com', 'station', false, 'PSYCHOHISTORIAN', 'DAY'),
    ('Stalker', 'Guide', 'guide@stalker.com', 'zonevisit', false, 'ZONE_EXPLORER', 'EVENING'),
    ('Gurney', 'Halleck', 'halleck@dune.com', 'baliset', false, 'BALISET_ARTIST', 'EVENING'),
    ('Dr.', 'Snaut', 'snaut@solaris.com', 'cosmonaut', false, 'STATION_SCIENTIST', 'EVENING');
 

-- Insert specific tasks
INSERT INTO task (name, description, creator_id, creation_date, status, category, shift_status)
VALUES
    ('Replicant Identification', 'Deckard must use the Voight-Kampff test to identify possible replicants in the city.', 1, '2023-08-01 09:00:00', 'IN_PROGRESS', 'CUSTOM', 'NIGHT'),
    ('Arrakis Spice Monitoring', 'Paul Atreides to oversee spice production levels on Arrakis and report any anomalies.', 2, '2023-08-02 10:00:00', 'IN_PROGRESS', 'CUSTOM', 'NIGHT'),
    ('Solaris Ocean Study', 'Kris Kelvin to analyze the behavioral patterns of the Solaris ocean.', 3, '2023-08-03 11:00:00', 'IN_PROGRESS', 'CUSTOM', 'NIGHT'),
    ('Zone Artifact Retrieval', 'Stalker to lead an expedition to retrieve a rumored powerful artifact from The Zone.', 4, '2023-08-04 12:00:00', 'IN_PROGRESS', 'CUSTOM', 'NIGHT'),
    ('Replicant Uprising Investigation', 'Investigate reports of a replicant uprising led by Roy Batty.', 1, '2023-08-05 13:00:00', 'IN_PROGRESS', 'CUSTOM', 'NIGHT'),
    ('Dune Reconnaissance', 'Duncan Idaho to conduct surveillance missions on Dune for strategic insights.', 5, '2023-08-06 10:30:00', 'IN_PROGRESS', 'CUSTOM', 'DAY'),
    ('Predictive Analysis', 'Hari Seldon to use psychohistory for predictive analysis of future events.', 6, '2023-08-07 11:00:00', 'IN_PROGRESS', 'CUSTOM', 'DAY'),
    ('Baliset Performance', 'Gurney Halleck to perform a musical piece on the baliset for troop morale.', 8, '2023-08-08 12:00:00', 'IN_PROGRESS', 'CUSTOM', 'EVENING');


-- Insert assignments of tasks to users (UserTask)
INSERT INTO user_task (user_id, task_id, user_task_status, assignment_date, assigned_by)
VALUES
    (2, 1, 'ASSIGNED', '2023-08-06 14:00:00', 3),
    (3, 2, 'ASSIGNED', '2023-08-07 15:00:00', 1),
    (5, 3, 'ASSIGNED', '2023-08-08 09:30:00', 6),
    (6, 4, 'ASSIGNED', '2023-08-09 16:45:00', 5),
    (8, 5, 'ASSIGNED', '2023-08-10 17:00:00', 9),
    (5, 6, 'ASSIGNED', '2023-08-09 10:00:00', 6),
    (6, 7, 'ASSIGNED', '2023-08-10 11:30:00', 5),
    (8, 8, 'ASSIGNED', '2023-08-11 14:00:00', 7);


-- Insert specific comments
INSERT INTO comment (user_id, task_id, text, timestamp)
VALUES
    (1, 1, 'Administering Voight-Kampff test to suspected replicants. Awaiting results.', '2023-08-11 09:15:00'),
    (2, 2, 'Noticed unusual spice production patterns on Arrakis. Investigating.', '2023-08-12 10:30:00'),
    (3, 3, 'Solaris ocean exhibits new phenomena. Recording data for analysis.', '2023-08-13 11:45:00'),
    (4, 4, 'Preparing for The Zone expedition. Safety measures in place.', '2023-08-14 14:30:00'),
    (1, 5, 'Tracking Roy Batty. Possible locations identified. Proceeding with caution.', '2023-08-15 15:45:00'),
    (1, 1, 'Several suspects cleared. Continuing the search.', '2023-08-12 09:20:00'),
    (2, 2, 'Spice flow is critical. Monitoring for Harkonnen interference.', '2023-08-13 10:35:00'),
    (3, 3, 'The ocean responds to our presence. Need to study this interaction.', '2023-08-14 12:00:00'),
    (5, 6, 'Recon in progress. The desert holds many secrets.', '2023-08-15 13:30:00'),
    (6, 7, 'Initial predictions are concerning. Further analysis required.', '2023-08-16 15:00:00'),
    (8, 8, 'Preparing a new composition to inspire the troops.', '2023-08-17 16:45:00');
