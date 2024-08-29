alter table uc_oauth_session
    drop key oauth_id;

create index idx_oauth_id on uc_oauth_session (oauth_id);
