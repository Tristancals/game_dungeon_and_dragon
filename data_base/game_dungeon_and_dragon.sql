create table if not exists hero(
     id int primary key auto_increment
    ,heroname varchar(20) not null unique
    ,herolevel int not null
    ,herotype varchar(255) not null

    ,id_gear_def int
    ,constraint fk_gear_def foreign key(id_gear_def) references gear(id)
    ,id_gear_of int
    ,constraint fk_gear_of foreign key(id_gear_of) references gear(id)
    ,id_inventory int
    ,constraint fk_inventory foreign key(id_inventory) references inventory(id)
);


create table if not exists inventory(
    id int primary key auto_increment
    ,id_hero int
    ,constraint fk_hero foreign key(id_hero) references hero(id)
    ,id_gear_heal int
    ,constraint fk_gear_heal foreign key(id_gear_heal) references gear(id)
);

create table if not exists gear(
    id int primary key auto_increment
    ,gearname varchar(20) not null unique
    ,gearstats int not null
    ,herotype varchar(255) not null
);