ALTER TABLE `uc_oauth`
    ADD `recommend_code` VARCHAR(32) NULL DEFAULT NULL COMMENT 'recommend code' AFTER `union_id`;
