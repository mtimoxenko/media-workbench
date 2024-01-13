-- Insert 5 random users
INSERT INTO usr (name, surname, email, password, is_admin)
VALUES
    ('Maximo', 'Timochenko', 'mtimochenko@noc.com', 'password123', false),
    ('Matias', 'Gioscio', 'mgioscio@noc.com', 'password456', false),
    ('Brian', 'Silva', 'basilva@noc.com', 'password789', false),
    ('Sebastian', 'Lopez', 'seblopez@noc.com', 'password101', false),
    ('Joaquin', 'Higa', 'jhiga@noc.com', 'password102', false);

-- Insert 5 specific tasks
INSERT INTO task (name, description, creator_id, creation_date, status)
VALUES
    ('Controles de Salud Grafana', 'Realizar revisiones regulares de salud y monitoreo de flujos de red utilizando Grafana', 2, '2023-08-01 09:00:00', 'IN_PROGRESS'),
    ('Gestión Tráfico con Filtro QWILT', 'Gestionar y optimizar el exceso de tráfico utilizando técnicas de filtrado QWILT', 2, '2023-08-02 10:00:00', 'IN_PROGRESS'),
    ('Mantenimiento EDGE', 'Realizar mantenimiento de rutina y actualizaciones en la infraestructura de la red EDGE', 5, '2023-08-03 11:00:00', 'IN_PROGRESS'),
    ('TTs ICD Pendientes', 'Resolver Tickets de Problemas pendientes relacionados con problemas y consultas de ICD', 5, '2023-08-04 12:00:00', 'IN_PROGRESS'),
    ('Alarmas FMC', 'Monitorear y responder a las alarmas en el Centro de Gestión de Fallas (FMC)', 2, '2023-08-05 13:00:00', 'IN_PROGRESS');

-- Insert 5 random assignments of tasks to users (UserTask)
INSERT INTO user_task (user_id, task_id, user_task_status, assignment_date, assigned_by)
VALUES
    (1, 1, 'ASSIGNED', '2023-08-06 14:00:00', 2),
    (1, 2, 'ASSIGNED', '2023-08-07 15:00:00', 4),
    (3, 3, 'ASSIGNED', '2023-08-08 09:30:00', 1),
    (4, 4, 'ASSIGNED', '2023-08-09 16:45:00', 5),
    (5, 5, 'ASSIGNED', '2023-08-10 17:00:00', 3);

-- Insert 5 specific comments
INSERT INTO comment (user_id, task_id, text, timestamp)
VALUES
    (1, 1, 'Las revisiones de salud en Grafana se han realizado correctamente.', '2023-08-11 09:15:00'),
    (3, 2, 'Gestión del desborde de tráfico con QWILT completada satisfactoriamente.', '2023-08-12 10:30:00'),
    (4, 3, 'Mantenimiento de EDGE realizado con éxito.', '2023-08-13 11:45:00'),
    (5, 4, 'Se han resuelto los TT pendientes de ICD.', '2023-08-14 14:30:00'),
    (2, 5, 'Respuesta a alarmas en el Centro de Gestión de Fallos gestionada.', '2023-08-15 15:45:00');
