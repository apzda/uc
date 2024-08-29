alter table uc_role
    add description TEXT null comment 'Description' after provider;

drop index IDX_PHONE on uc_user;

create unique index UDX_EMAIL on uc_user (email);

create unique index UDX_PHONE on uc_user (phone_prefix, phone_number);
