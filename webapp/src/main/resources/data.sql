-- Insert 5 random users
INSERT INTO usr (name, surname, email, password, is_admin)
VALUES
    ('Maximo', 'Timochenko', 'mtimochenko@teco.com.ar', 'password123', false),
    ('Matias', 'Gioscio', 'mgioscio@teco.com.ar', 'password456', true),
    ('Brian', 'Silva', 'basilva@teco.com.ar', 'password789', false),
    ('Sebastian', 'Lopez', 'seblopez@teco.com.ar', 'password101', false),
    ('Joaquin', 'Higa', 'jhiga@teco.com.ar', 'password102', true);

-- Insertar 5 tareas específicas
INSERT INTO task (name, description, creator_id, creation_date, is_completed)
VALUES
    ('Grafana Healthchecks', 'Realizar revisiones regulares de salud y monitoreo de flujos de red utilizando Grafana', 2, '2023-08-01 09:00:00', false),
    ('Gestión de Desborde de Tráfico con Filtro QWILT', 'Gestionar y optimizar el desborde de tráfico utilizando técnicas de filtrado QWILT', 2, '2023-08-02 10:00:00', false),
    ('Mantenimiento de EDGE', 'Realizar mantenimiento y actualizaciones rutinarias en la infraestructura de red EDGE', 5, '2023-08-03 11:00:00', false),
    ('TT Pendientes de ICD', 'Resolver Tickets de Problemas pendientes relacionados con asuntos y consultas de ICD', 5, '2023-08-04 12:00:00', false),
    ('Alarmas FMC', 'Monitorear y responder a las alarmas en el Centro de Gestión de Fallos (FMC)', 2, '2023-08-05 13:00:00', false);


-- Insert 5 random assignments of tasks to users (UserTask)
INSERT INTO user_task (user_id, task_id, is_task_completed, assignment_date, assigned_by)
VALUES
    (1, 1, false, '2023-08-06 14:00:00', 2),
    (1, 2, false, '2023-08-07 15:00:00', 2),
    (3, 3, false, '2023-08-08 09:30:00', 5),
    (4, 4, false, '2023-08-09 16:45:00', 5),
    (5, 5, false, '2023-08-10 17:00:00', 2);

-- Insertar 5 comentarios específicos
INSERT INTO comment (user_id, task_id, text, timestamp)
VALUES
    (1, 1, 'Las revisiones de salud en Grafana se han realizado correctamente.', '2023-08-11 09:15:00'),
    (3, 2, 'Gestión del desborde de tráfico con QWILT completada satisfactoriamente.', '2023-08-12 10:30:00'),
    (4, 3, 'Mantenimiento de EDGE realizado con éxito.', '2023-08-13 11:45:00'),
    (5, 4, 'Se han resuelto los TT pendientes de ICD.', '2023-08-14 14:30:00'),
    (2, 5, 'Respuesta a alarmas en el Centro de Gestión de Fallos gestionada.', '2023-08-15 15:45:00');

