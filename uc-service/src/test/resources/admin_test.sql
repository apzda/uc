INSERT INTO uc_user_meta (id, created_at, created_by, updated_at, updated_by, deleted, type, uid, name, value, remark)
VALUES (1, 0, '0', 0, '0', false, 'S', 1, 'test', 'test string', null);
INSERT INTO uc_user_meta (id, created_at, created_by, updated_at, updated_by, deleted, type, uid, name, value, remark)
VALUES (2, 0, '0', 0, '0', false, 'I', 1, 'int', '1', null);

INSERT INTO uc_user_role (id, created_at, created_by, updated_at, updated_by, deleted, tenant_id, uid, role_id)
VALUES (2, 1218153600, '1', 1218153600, '1', false, 0, 1, 2);

INSERT INTO uc_role_children (id, created_at, created_by, updated_at, updated_by, tenant_id, deleted, role_id, child_id)
VALUES (1, 0, '0', 0, '0', 0, false, 2, 3);

-- privileges
INSERT INTO uc_privilege (id, created_at, created_by, updated_at, updated_by, tenant_id, deleted, name, type, builtin,
                          permission, extra, description, remark)
VALUES (2, 1218153600, '1', 11218153600, '1', 0, false, 'View Audit Log', 'core', true, 'view:audit/log', null, null,
        null);

-- privileges of role

INSERT INTO uc_role_privilege (id, created_at, created_by, updated_at, updated_by, tenant_id, deleted, role_id,
                               privilege_id)
VALUES (3, 1218153600, '1', 1218153600, '1', 0, false, 3, 2);
-- roles for tenant 1
INSERT INTO uc_role (id, created_at, created_by, updated_at, updated_by, tenant_id, deleted, role, name, builtin,
                     provider)
VALUES (4, 1218153600, '1', 1218153600, '1', 1, false, 'sa', 'Super Administrator', true, 'db');

INSERT INTO uc_user_role (id, created_at, created_by, updated_at, updated_by, deleted, tenant_id, uid, role_id)
VALUES (3, 1218153600, '1', 1218153600, '1', false, 1, 1, 4);

INSERT INTO uc_privilege (id, created_at, created_by, updated_at, updated_by, tenant_id, deleted, name, type, builtin,
                          permission, extra, description, remark)
VALUES (3, 1218153600, '1', 11218153600, '1', 0, false, 'All Privileges', 'core', true, '*:tenant/*', null, null, null);

INSERT INTO uc_role_privilege (id, created_at, created_by, updated_at, updated_by, tenant_id, deleted, role_id,
                               privilege_id)
VALUES (4, 1218153600, '1', 1218153600, '1', 1, false, 4, 3);
