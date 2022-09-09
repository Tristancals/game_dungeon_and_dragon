create table inventory
(
    id           int auto_increment
        primary key,
    id_hero      int null,
    id_gear_heal int null,
    constraint fk_gear_heal
        foreign key (id_gear_heal) references gear (id),
    constraint inventory_ibfk_1
        foreign key (id_hero) references hero (id)
            on delete cascade
);

create index id_hero
    on inventory (id_hero);

