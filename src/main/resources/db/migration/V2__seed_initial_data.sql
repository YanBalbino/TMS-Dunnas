SET TIME ZONE 'America/Fortaleza';

-- =====================================
-- 1) CONFIGURAÇÕES BÁSICAS DO SISTEMA
-- =====================================

INSERT INTO ticket_status (name, hex_color, is_default, is_finalizer, created_at, updated_at, created_by, updated_by)
VALUES
    ('ABERTO', '#ADD8E6', TRUE, FALSE, '2026-04-08T08:15:00-03', NULL, 'admin', NULL),
    ('EM ANDAMENTO', '#FFFFE0', FALSE, FALSE, '2026-04-08T08:17:00-03', NULL, 'admin', NULL),
    ('CONCLUIDO', '#90EE90', FALSE, TRUE, '2026-04-08T08:19:00-03', NULL, 'admin', NULL);

INSERT INTO ticket_type (title, deadline_days, created_at, updated_at, created_by, updated_by)
VALUES
    ('Reparo Emergencial', 1, '2026-04-08T08:25:00-03', NULL, 'admin', NULL),
    ('Solicitação / Reserva', 5, '2026-04-08T08:27:00-03', NULL, 'admin', NULL),
    ('Manutenção Preventiva', 15, '2026-04-08T08:29:00-03', NULL, 'admin', NULL);

INSERT INTO block (name, street, number, floor_count, units_per_floor, created_at, updated_at, created_by, updated_by)
VALUES
    ('Bloco A', 'Rua das Esmeraldas', 158, 4, 10, '2026-04-08T09:00:00-03', NULL, 'admin', NULL),
    ('Bloco B', 'Rua das Esmeraldas', 158, 4, 10, '2026-04-08T09:02:00-03', NULL, 'admin', NULL);

-- =====================================
-- 2) USUÁRIOS INICIAIS (BCrypt)
-- =====================================

INSERT INTO user_account (name, username, password, role, created_at, updated_at, created_by, updated_by)
VALUES
    ('Administrador', 'admin', '$2b$12$KthTTEVYD9/TBEyPhazArO3JJAJMlcE/8v5msUsmXjI7qFQtVSMs6', 'ROLE_ADMIN', '2026-04-08T09:15:00-03', NULL, 'admin', NULL),
    ('Colaborador', 'collab', '$2b$12$BvycpzGBWa0F9JxQaRjm6uTR23IxcBL6LpyK6fE9.6EeaVK3zDWyK', 'ROLE_COLLABORATOR', '2026-04-08T09:17:00-03', NULL, 'admin', NULL),
    ('Residente 1', 'resident1', '$2b$12$CdbNWOgNO7X5WRnzpS2SKO81CpgeWT/N/XN8EslUxm04boKMDG06W', 'ROLE_RESIDENT', '2026-04-08T09:19:00-03', NULL, 'admin', NULL),
    ('Resident 2', 'resident2', '$2b$12$q45eTIwKQObbKOGhwwTlUOeONkKY2NzYNKKrlFNadpq4H18YQseT6', 'ROLE_RESIDENT', '2026-04-08T09:21:00-03', NULL, 'admin', NULL);

-- =====================================
-- 3) UNIDADES (40 POR BLOCO)
-- =====================================

INSERT INTO unit (number, floor_number, block_id, created_at, updated_at, created_by, updated_by)
SELECT v.unit_number, v.floor_number, b.id, '2026-04-08T10:00:00-03', NULL, 'admin', NULL
FROM (
    VALUES
        (101, 1), (102, 1), (103, 1), (104, 1), (105, 1), (106, 1), (107, 1), (108, 1), (109, 1), (110, 1),
        (201, 2), (202, 2), (203, 2), (204, 2), (205, 2), (206, 2), (207, 2), (208, 2), (209, 2), (210, 2),
        (301, 3), (302, 3), (303, 3), (304, 3), (305, 3), (306, 3), (307, 3), (308, 3), (309, 3), (310, 3),
        (401, 4), (402, 4), (403, 4), (404, 4), (405, 4), (406, 4), (407, 4), (408, 4), (409, 4), (410, 4)
) AS v(unit_number, floor_number)
CROSS JOIN (
    SELECT id FROM block WHERE name = 'Bloco A'
) b;

INSERT INTO unit (number, floor_number, block_id, created_at, updated_at, created_by, updated_by)
SELECT v.unit_number, v.floor_number, b.id, '2026-04-08T10:05:00-03', NULL, 'admin', NULL
FROM (
    VALUES
        (101, 1), (102, 1), (103, 1), (104, 1), (105, 1), (106, 1), (107, 1), (108, 1), (109, 1), (110, 1),
        (201, 2), (202, 2), (203, 2), (204, 2), (205, 2), (206, 2), (207, 2), (208, 2), (209, 2), (210, 2),
        (301, 3), (302, 3), (303, 3), (304, 3), (305, 3), (306, 3), (307, 3), (308, 3), (309, 3), (310, 3),
        (401, 4), (402, 4), (403, 4), (404, 4), (405, 4), (406, 4), (407, 4), (408, 4), (409, 4), (410, 4)
) AS v(unit_number, floor_number)
CROSS JOIN (
    SELECT id FROM block WHERE name = 'Bloco B'
) b;

-- =====================================
-- 4) VÍNCULOS M:N
-- =====================================

-- Admin morador da unidade 405 do Bloco A
INSERT INTO resident_unit (user_id, unit_id)
SELECT u.id, un.id
FROM user_account u
JOIN unit un ON un.number = 405
JOIN block b ON b.id = un.block_id
WHERE u.username = 'admin' AND b.name = 'Bloco A';

-- resident1 -> A101 e A201
INSERT INTO resident_unit (user_id, unit_id)
SELECT u.id, un.id
FROM user_account u
JOIN unit un ON un.number IN (101, 201)
JOIN block b ON b.id = un.block_id
WHERE u.username = 'resident1' AND b.name = 'Bloco A';

-- resident2 -> B101 e B201
INSERT INTO resident_unit (user_id, unit_id)
SELECT u.id, un.id
FROM user_account u
JOIN unit un ON un.number IN (101, 201)
JOIN block b ON b.id = un.block_id
WHERE u.username = 'resident2' AND b.name = 'Bloco B';

-- collaborator -> acesso a Solicitação / Reserva e Manutenção Preventiva
INSERT INTO collaborator_ticket_type (user_id, ticket_type_id)
SELECT u.id, tt.id
FROM user_account u
JOIN ticket_type tt ON tt.title IN ('Solicitação / Reserva', 'Manutenção Preventiva')
WHERE u.username = 'collab';

-- =====================================
-- 5) TICKETS INICIAIS
-- =====================================

INSERT INTO ticket (
    title, description, status_id, author_id, unit_id, ticket_type_id,
    completed_at, due_date, created_at, updated_at, created_by, updated_by
)
SELECT
    'Instalação de argolas olímpicas',
    'Solicito autorização para instalar temporariamente argolas olímpicas na barra fixa da academia durante meus treinos de calistenia. Eu mesmo coloco e retiro o equipamento diariamente.',
    ts.id,
    ua.id,
    un.id,
    tt.id,
    NULL,
    '2026-04-21T17:45:00-03',
    '2026-04-16T09:12:00-03',
    NULL,
    'resident1',
    NULL
FROM ticket_status ts
JOIN user_account ua ON ua.username = 'resident1'
JOIN ticket_type tt ON tt.title = 'Solicitação / Reserva'
JOIN unit un ON un.number = 101
JOIN block b ON b.id = un.block_id
WHERE ts.name = 'ABERTO' AND b.name = 'Bloco A';

INSERT INTO ticket (
    title, description, status_id, author_id, unit_id, ticket_type_id,
    completed_at, due_date, created_at, updated_at, created_by, updated_by
)
SELECT
    'Trinco do Espaço Pet quebrado',
    'O portão principal da área de passeio dos cachorros não está trancando. Notei o problema na caminhada da manhã e há risco de fuga para a rua.',
    ts.id,
    ua.id,
    un.id,
    tt.id,
    NULL,
    '2026-04-17T18:10:00-03',
    '2026-04-16T10:03:00-03',
    NULL,
    'resident2',
    NULL
FROM ticket_status ts
JOIN user_account ua ON ua.username = 'resident2'
JOIN ticket_type tt ON tt.title = 'Reparo Emergencial'
JOIN unit un ON un.number = 201
JOIN block b ON b.id = un.block_id
WHERE ts.name = 'ABERTO' AND b.name = 'Bloco B';

INSERT INTO ticket (
    title, description, status_id, author_id, unit_id, ticket_type_id,
    completed_at, due_date, created_at, updated_at, created_by, updated_by
)
SELECT
    'Roteador do Hall desconectando aparelhos',
    'Durante a ronda, o roteador do hall rejeitou conexões e gerou conflito de IP em alguns aparelhos Linux. Já iniciei a reconfiguração do gateway.',
    ts.id,
    ua.id,
    un.id,
    tt.id,
    NULL,
    '2026-04-25T19:05:00-03',
    '2026-04-15T14:42:00-03',
    NULL,
    'collab',
    NULL
FROM ticket_status ts
JOIN user_account ua ON ua.username = 'collab'
JOIN ticket_type tt ON tt.title = 'Manutenção Preventiva'
JOIN unit un ON un.number = 307
JOIN block b ON b.id = un.block_id
WHERE ts.name = 'EM ANDAMENTO' AND b.name = 'Bloco B';

INSERT INTO ticket (
    title, description, status_id, author_id, unit_id, ticket_type_id,
    completed_at, due_date, created_at, updated_at, created_by, updated_by
)
SELECT
    'Reserva do Salão de Festas Principal',
    'Reserva do salão para evento com sessão de RPG temática Lovecraftiana e, em seguida, roda de violão com repertório de clássicos da MPB.',
    ts.id,
    ua.id,
    un.id,
    tt.id,
    '2026-04-11T09:18:00-03',
    '2026-04-10T20:00:00-03',
    '2026-04-09T11:26:00-03',
    '2026-04-11T09:18:00-03',
    'admin',
    'admin'
FROM ticket_status ts
JOIN user_account ua ON ua.username = 'admin'
JOIN ticket_type tt ON tt.title = 'Solicitação / Reserva'
JOIN unit un ON un.number = 405
JOIN block b ON b.id = un.block_id
WHERE ts.name = 'CONCLUIDO' AND b.name = 'Bloco A';

-- =====================================
-- 6) COMENTÁRIOS DOS CHAMADOS
-- =====================================

INSERT INTO comment (description, ticket_id, author_id, created_at, updated_at, created_by, updated_by)
SELECT
    'Pedido registrado. Vamos avaliar a resistência da estrutura da barra e te damos um retorno até sexta-feira.',
    t.id,
    ua.id,
    '2026-04-16T10:05:00-03',
    NULL,
    'collab',
    NULL
FROM ticket t
JOIN user_account ua ON ua.username = 'collab'
WHERE t.title = 'Instalação de argolas olímpicas';

INSERT INTO comment (description, ticket_id, author_id, created_at, updated_at, created_by, updated_by)
SELECT
    'Obrigado pelo aviso rápido! Já isolei a área com fita e acionei o chaveiro do bairro. Deve estar resolvido até o final do dia.',
    t.id,
    ua.id,
    '2026-04-16T11:07:00-03',
    NULL,
    'admin',
    NULL
FROM ticket t
JOIN user_account ua ON ua.username = 'admin'
WHERE t.title = 'Trinco do Espaço Pet quebrado';

INSERT INTO comment (description, ticket_id, author_id, created_at, updated_at, created_by, updated_by)
SELECT
    'Fiz o reset de fábrica e atualizei o firmware. Monitorando a rede pelas próximas 24h para garantir a estabilidade.',
    t.id,
    ua.id,
    '2026-04-15T16:15:00-03',
    NULL,
    'collab',
    NULL
FROM ticket t
JOIN user_account ua ON ua.username = 'collab'
WHERE t.title = 'Roteador do Hall desconectando aparelhos';

INSERT INTO comment (description, ticket_id, author_id, created_at, updated_at, created_by, updated_by)
SELECT
    'Chaves separadas na portaria. O ar-condicionado foi testado ontem e está gelando perfeitamente.',
    t.id,
    ua.id,
    '2026-04-10T08:12:00-03',
    NULL,
    'collab',
    NULL
FROM ticket t
JOIN user_account ua ON ua.username = 'collab'
WHERE t.title = 'Reserva do Salão de Festas Principal';

INSERT INTO comment (description, ticket_id, author_id, created_at, updated_at, created_by, updated_by)
SELECT
    'Chaves devolvidas hoje pela manhã. O espaço foi limpo pela equipe e não houve nenhuma ocorrência ou reclamação de barulho. Chamado encerrado.',
    t.id,
    ua.id,
    '2026-04-11T09:20:00-03',
    NULL,
    'admin',
    NULL
FROM ticket t
JOIN user_account ua ON ua.username = 'admin'
WHERE t.title = 'Reserva do Salão de Festas Principal';
