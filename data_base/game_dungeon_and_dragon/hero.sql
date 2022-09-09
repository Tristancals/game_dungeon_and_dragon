create table hero
(
    id            int auto_increment
        primary key,
    hero_name     varchar(20)  not null,
    hero_level    int          not null,
    hero_life     int          null,
    hero_position int          null,
    hero_type     varchar(255) not null,
    id_gear_def   int          null,
    id_gear_of    int          null,
    constraint fk_gear_def
        foreign key (id_gear_def) references gear (id),
    constraint fk_gear_of
        foreign key (id_gear_of) references gear (id)
);

