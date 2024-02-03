INSERT INTO usr (name, surname, email, password, is_active, role, shift)
VALUES
    ('Joaquin', 'Miga', 'jmiga@noc.com', 'noche', true, 'REPORTER', 'NIGHT'),
    ('Brian', 'Silver', 'bsilver@noc.com', 'noche', true, 'HELPER', 'NIGHT'),
    ('Lucas', 'Bardo', 'lbardo@noc.com', 'dia', true, 'ATTENDANT', 'DAY'),
    ('Mariano', 'Villano', 'mvillano@noc.com', 'dia', true, null, 'DAY'),
    ('Daniel', 'Turbio', 'dturbio@noc.com', 'tarde', true, null, 'EVENING'),
    ('Gonzalo', 'Burgues', 'gburgues@noc.com', 'tarde', true, null, 'EVENING');


-- Insert specific tasks
INSERT INTO task (name, description, creator_id, creation_date, status, category, shift_status)
VALUES
    ('Controles de Salud Grafana', 'Realizar revisiones regulares de salud y monitoreo de flujos de red utilizando Grafana', 2, '2023-08-01 09:00:00', 'IN_PROGRESS', 'TEMPLATE', 'NIGHT'),
    ('Gestión Tráfico con Filtro QWILT', 'Gestionar y optimizar el exceso de tráfico utilizando técnicas de filtrado QWILT', 2, '2023-08-02 10:00:00', 'IN_PROGRESS', 'TEMPLATE', 'NIGHT'),
    ('Mantenimiento EDGE', 'Realizar mantenimiento de rutina y actualizaciones en la infraestructura de la red EDGE', 5, '2023-08-03 11:00:00', 'IN_PROGRESS', 'TEMPLATE', 'NIGHT'),
    ('TTs ICD Pendientes', 'Resolver Tickets de Problemas pendientes relacionados con problemas y consultas de ICD', 5, '2023-08-04 12:00:00', 'IN_PROGRESS', 'TEMPLATE', 'NIGHT'),
    ('Alarmas FMC', 'Monitorear y responder a las alarmas en el Centro de Gestión de Fallas (FMC)', 2, '2023-08-05 13:00:00', 'IN_PROGRESS', 'TEMPLATE', 'NIGHT');


-- Insert assignments of tasks to users (UserTask)
INSERT INTO user_task (user_id, task_id, user_task_status, assignment_date, assigned_by)
VALUES
    (1, 1, 'ASSIGNED', '2023-08-06 14:00:00', 2),
    (2, 2, 'ASSIGNED', '2023-08-07 15:00:00', 1),
    (3, 3, 'ASSIGNED', '2023-08-08 09:30:00', 4),
    (4, 4, 'ASSIGNED', '2023-08-09 16:45:00', 3),
    (5, 5, 'ASSIGNED', '2023-08-10 17:00:00', 6);


-- Insert specific comments
INSERT INTO comment (user_id, task_id, text, timestamp)
VALUES
    (2, 1, 'Atento hoy a las notificaciones en Grafana.', '2023-08-11 09:15:00'),
    (1, 2, 'Desborde de tráfico con QWILT para desactivar.', '2023-08-12 10:30:00'),
    (4, 3, 'Verificar EDGE 10 que esta en mantenimiento.', '2023-08-13 11:45:00'),
    (3, 4, 'Hay algunos TT pendientes en ICD.', '2023-08-14 14:30:00'),
    (6, 5, 'Gestionar las alarmas por evento de la fecha.', '2023-08-15 15:45:00');


-- Insert schedules for users, with consecutive days and 1-2 days off included
INSERT INTO user_schedule (user_id, date, day_of_week, day_number, is_working_day, role) VALUES
    (1, '2024-02-04', 'SUNDAY', 4, TRUE, 'HELPER'),
    (1, '2024-02-05', 'MONDAY', 5, TRUE, 'HELPER'),
    (1, '2024-02-06', 'TUESDAY', 6, FALSE, 'HELPER'),
    (1, '2024-02-07', 'WEDNESDAY', 7, TRUE, 'HELPER'),
    (1, '2024-02-08', 'THURSDAY', 8, TRUE, 'HELPER'),
    (1, '2024-02-09', 'FRIDAY', 9, TRUE, 'HELPER'),
    (1, '2024-02-10', 'SATURDAY', 10, FALSE, 'HELPER'),

    (2, '2024-02-04', 'SUNDAY', 4, TRUE, 'HELPER'),
    (2, '2024-02-05', 'MONDAY', 5, FALSE, 'HELPER'),
    (2, '2024-02-06', 'TUESDAY', 6, FALSE, 'HELPER'),
    (2, '2024-02-07', 'WEDNESDAY', 7, TRUE, 'HELPER'),
    (2, '2024-02-08', 'THURSDAY', 8, TRUE, 'HELPER'),
    (2, '2024-02-09', 'FRIDAY', 9, TRUE, 'HELPER'),
    (2, '2024-02-10', 'SATURDAY', 10, TRUE, 'HELPER'),

    (3, '2024-02-04', 'SUNDAY', 4, TRUE, 'ATTENDANT'),
    (3, '2024-02-05', 'MONDAY', 5, TRUE, 'ATTENDANT'),
    (3, '2024-02-06', 'TUESDAY', 6, TRUE, 'ATTENDANT'),
    (3, '2024-02-07', 'WEDNESDAY', 7, FALSE, 'ATTENDANT'),
    (3, '2024-02-08', 'THURSDAY', 8, FALSE, 'ATTENDANT'),
    (3, '2024-02-09', 'FRIDAY', 9, TRUE, 'ATTENDANT'),
    (3, '2024-02-10', 'SATURDAY', 10, TRUE, 'ATTENDANT'),

    (4, '2024-02-04', 'SUNDAY', 4, TRUE, 'ATTENDANT'),
    (4, '2024-02-05', 'MONDAY', 5, FALSE, 'ATTENDANT'),
    (4, '2024-02-06', 'TUESDAY', 6, FALSE, 'ATTENDANT'),
    (4, '2024-02-07', 'WEDNESDAY', 7, TRUE, 'ATTENDANT'),
    (4, '2024-02-08', 'THURSDAY', 8, TRUE, 'ATTENDANT'),
    (4, '2024-02-09', 'FRIDAY', 9, TRUE, 'ATTENDANT'),
    (4, '2024-02-10', 'SATURDAY', 10, TRUE, 'ATTENDANT'),

    (5, '2024-02-04', 'SUNDAY', 4, TRUE, null),
    (5, '2024-02-05', 'MONDAY', 5, TRUE, null),
    (5, '2024-02-06', 'TUESDAY', 6, TRUE, null),
    (5, '2024-02-07', 'WEDNESDAY', 7, FALSE, null),
    (5, '2024-02-08', 'THURSDAY', 8, FALSE, null),
    (5, '2024-02-09', 'FRIDAY', 9, TRUE, null),
    (5, '2024-02-10', 'SATURDAY', 10, TRUE, null),

    (6, '2024-02-04', 'SUNDAY', 4, TRUE, null),
    (6, '2024-02-05', 'MONDAY', 5, FALSE, null),
    (6, '2024-02-06', 'TUESDAY', 6, FALSE, null),
    (6, '2024-02-07', 'WEDNESDAY', 7, TRUE, null),
    (6, '2024-02-08', 'THURSDAY', 8, TRUE, null),
    (6, '2024-02-09', 'FRIDAY', 9, TRUE, null),
    (6, '2024-02-10', 'SATURDAY', 10, TRUE, null);





