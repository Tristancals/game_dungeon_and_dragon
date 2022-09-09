create table gear
(
    id         int auto_increment
        primary key,
    gear_name  varchar(20)  not null,
    gear_stats int          not null,
    gear_type  varchar(255) not null
);

