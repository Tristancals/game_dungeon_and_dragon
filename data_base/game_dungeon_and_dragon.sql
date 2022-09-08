drop table if exists inventory;

drop table if exists hero;

drop table if exists gear;

drop table if exists dungeon_room;

drop table if exists game;

create table if not exists game
(
    id   int primary key auto_increment,
    dungeon_level int not null

);
create table if not exists dungeon_room
(
    id   int primary key auto_increment,
    id_game int,
    constraint fk_game foreign key (id_game) references game (id),
    type_enemy varchar(255),
    id_gear   int,
    constraint fk_gear foreign key (id_gear) references gear (id)

);

create table if not exists gear
(
    id         int primary key auto_increment,
    gear_name  varchar(20)  not null,
    gear_stats int          not null,
    gear_type  varchar(255) not null
);

create table if not exists hero
(
    id            int primary key auto_increment,
    hero_name     varchar(20)  not null,
    hero_level    int          not null,
    hero_life     int,
    hero_position int,
    hero_type     varchar(255) not null,
    id_game int,
    constraint fk_game foreign key (id_game) references game (id),
    id_gear_def   int,
    constraint fk_gear_def foreign key (id_gear_def) references gear (id),
    id_gear_of    int,
    constraint fk_gear_of foreign key (id_gear_of) references gear (id)
);

create table if not exists inventory
(
    id           int primary key auto_increment,
    id_hero      int,
    constraint fk_hero foreign key (id_hero) references hero (id),
    id_gear_heal int,
    constraint fk_gear_heal foreign key (id_gear_heal) references gear (id)
);
