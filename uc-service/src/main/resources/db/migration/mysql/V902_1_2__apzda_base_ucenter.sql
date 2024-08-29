alter table uc_user_meta
    add tenant_id bigint unsigned null comment 'Tenant Id' after updated_by;

alter table uc_user_meta
    drop key UDX_USER_META,
    add constraint UDX_USER_META unique (tenant_id, uid, name);

CREATE TABLE `uc_security_resource`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    `created_at`  BIGINT UNSIGNED NULL     DEFAULT NULL,
    `created_by`  VARCHAR(32)     NULL COMMENT 'Create User Id',
    `updated_at`  BIGINT UNSIGNED NULL     DEFAULT NULL,
    `updated_by`  VARCHAR(32)     NULL COMMENT 'Last updated by who',
    `deleted`     BIT             NOT NULL DEFAULT FALSE COMMENT 'Soft Deleted Flag',
    `pid`         BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT 'the parent id of this resource',
    `rid`         VARCHAR(32)     NOT NULL COMMENT 'the resource id',
    `name`        VARCHAR(64)     NOT NULL COMMENT 'the resource name',
    `actions`     VARCHAR(512)    NOT NULL DEFAULT 'c,r,u,d' COMMENT 'actions can be performed on resource',
    `explorer`    VARCHAR(256)             DEFAULT NULL COMMENT 'a FQDN class name for exploring the ids of this resource ',
    `description` VARCHAR(1024)            DEFAULT NULL COMMENT 'the description of this resource',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UDX_NAME` (`pid`, `name`)
) COMMENT ='Security Resource';
