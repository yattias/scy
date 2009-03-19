
DROP TABLE IF EXISTS users;
create table users (
    id bigint not null auto_increment,
    OPTLOCK integer,
    sds_user_fk bigint null unique,
    user_details_fk bigint null unique,
    primary key (id)
) type=InnoDB;



DROP TABLE IF EXISTS user_details;
create table user_details (
    id bigint not null auto_increment,
    account_not_expired bit not null,
    account_not_locked bit not null,
    credentials_not_expired bit not null,
    email_address varchar(255),
    enabled bit not null,
    password varchar(255) not null,
    username varchar(255) not null unique,
    OPTLOCK integer,
    primary key (id)
) type=InnoDB;

DROP TABLE IF EXISTS sds_users;
create table sds_users (
    id bigint not null auto_increment,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    user_id bigint not null unique,
    OPTLOCK integer,
    primary key (id)
) type=InnoDB;


create table granted_authorities (
    id bigint not null auto_increment,
    authority varchar(255) not null unique,
    OPTLOCK integer,
    primary key (id)
) type=InnoDB;

create table user_details_related_to_roles (
    user_details_fk bigint not null,
    granted_authorities_fk bigint not null,
    primary key (user_details_fk, granted_authorities_fk)
) type=InnoDB;




alter table user_details_related_to_roles
    add index FKE6A5FBDEE3B038C2 (user_details_fk),
    add constraint FKE6A5FBDEE3B038C2
    foreign key (user_details_fk)
    references user_details (id);

alter table user_details_related_to_roles
    add index FKE6A5FBDE44F8149A (granted_authorities_fk),
    add constraint FKE6A5FBDE44F8149A
    foreign key (granted_authorities_fk)
    references granted_authorities (id);


alter table users
        add index FK6A68E08E3B038C2 (user_details_fk),
        add constraint FK6A68E08E3B038C2
        foreign key (user_details_fk)
        references user_details (id);







