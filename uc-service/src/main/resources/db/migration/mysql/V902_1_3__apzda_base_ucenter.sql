alter table uc_tenant
    add domain varchar(64) null comment 'the domain of this tenant' after name;
