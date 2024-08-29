INSERT INTO uc_role (id, created_at, created_by, updated_at, updated_by, tenant_id, deleted, role, name, builtin,
                     provider, description)
VALUES (4, 1218153600, '1', 1218153600, '1', 0, false, 'a', 'a', false, 'db', null),
       (5, 1218153600, '1', 1218153600, '1', 0, false, 'b', 'b', false, 'db', null),
       (6, 1218153600, '1', 1218153600, '1', 0, false, 'c', 'c', false, 'db', null),
       (7, 1218153600, '1', 1218153600, '1', 0, false, 'd', 'd', false, 'db', null),
       (8, 1218153600, '1', 1218153600, '1', 0, false, 'e', 'e', false, 'db', null);

INSERT INTO uc_role_children (id, created_at, created_by, updated_at, updated_by, tenant_id, deleted, role_id, child_id)
VALUES (1, null, null, null, null, 0, false, 4, 5),
       (2, null, null, null, null, 0, false, 4, 6),
       (3, null, null, null, null, 0, false, 5, 7),
       (4, null, null, null, null, 0, false, 7, 8);


INSERT INTO uc_privilege (id, created_at, created_by, updated_at, updated_by, tenant_id, deleted, name, type, builtin,
                          permission, extra, description, remark)
VALUES (2, 1715656634639, null, 1715656634639, null, 0, false, 'Test2', 'authority', false, 'test2', null, 'Test2',
        null),
       (3, 1715656634639, null, 1715656634639, null, 0, false, 'Test3', 'authority', false, 'test3', null, 'Test3',
        null),
       (4, 1715656634639, null, 1715656634639, null, 0, false, 'Test4', 'authority', false, 'test4', null, 'Test4',
        null);
