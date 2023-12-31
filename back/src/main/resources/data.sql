-- Insert 5 random users
INSERT INTO usr (name, surname, email, password, is_admin)
VALUES
    ('Maximo', 'Timochenko', 'mtimochenko@noc.com', 'password123', false),
    ('Matias', 'Gioscio', 'mgioscio@noc.com', 'password456', true),
    ('Brian', 'Silva', 'basilva@noc.com', 'password789', false),
    ('Sebastian', 'Lopez', 'seblopez@noc.com', 'password101', false),
    ('Joaquin', 'Higa', 'jhiga@noc.com', 'password102', true);

-- Insert 5 specific tasks
INSERT INTO task (name, description, creator_id, creation_date, status)
VALUES
    ('Grafana Healthchecks', 'Realizar revisiones regulares de salud y monitoreo de flujos de red utilizando Grafana', 2, '2023-08-01 09:00:00', 'ACTIVE'),
    ('Traffic Overflow Management with QWILT Filter', 'Manage and optimize traffic overflow using QWILT filtering techniques', 2, '2023-08-02 10:00:00', 'ACTIVE'),
    ('EDGE Maintenance', 'Perform routine maintenance and updates on the EDGE network infrastructure', 5, '2023-08-03 11:00:00', 'ACTIVE'),
    ('Pending ICD TTs', 'Resolve pending Trouble Tickets related to issues and ICD queries', 5, '2023-08-04 12:00:00', 'ACTIVE'),
    ('FMC Alarms', 'Monitor and respond to alarms at the Failure Management Center (FMC)', 2, '2023-08-05 13:00:00', 'ACTIVE');

-- Insert 5 random assignments of tasks to users (UserTask)
INSERT INTO user_task (user_id, task_id, user_task_status, assignment_date, assigned_by)
VALUES
    (1, 1, 'ASSIGNED', '2023-08-06 14:00:00', 2),
    (1, 2, 'ASSIGNED', '2023-08-07 15:00:00', 2),
    (3, 3, 'ASSIGNED', '2023-08-08 09:30:00', 5),
    (4, 4, 'ASSIGNED', '2023-08-09 16:45:00', 5),
    (5, 5, 'ASSIGNED', '2023-08-10 17:00:00', 2);

-- Insert 5 specific comments
INSERT INTO comment (user_id, task_id, text, timestamp)
VALUES
    (1, 1, 'Las revisiones de salud en Grafana se han realizado correctamente.', '2023-08-11 09:15:00'),
    (3, 2, 'Gestión del desborde de tráfico con QWILT completada satisfactoriamente.', '2023-08-12 10:30:00'),
    (4, 3, 'Mantenimiento de EDGE realizado con éxito.', '2023-08-13 11:45:00'),
    (5, 4, 'Se han resuelto los TT pendientes de ICD.', '2023-08-14 14:30:00'),
    (2, 5, 'Respuesta a alarmas en el Centro de Gestión de Fallos gestionada.', '2023-08-15 15:45:00');
