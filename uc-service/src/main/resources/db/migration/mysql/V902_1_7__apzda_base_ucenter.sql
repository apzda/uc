DROP INDEX UDX_PHONE ON uc_user;
CREATE INDEX IDX_PHONE ON uc_user (phone_number, phone_prefix);
