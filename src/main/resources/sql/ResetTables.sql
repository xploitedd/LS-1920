drop table if exists room_label;
drop table if exists booking;
drop table if exists room;
drop table if exists "user";
drop table if exists "label";

CREATE TABLE room (
    rid SERIAL PRIMARY KEY,
    name varchar(50),
    location varchar(50),
    capacity smallint,
    constraint room_capacity_min_check check (capacity > 1)
);

CREATE TABLE DESCRIPTION (
    rid SERIAL PRIMARY KEY REFERENCES room(rid),
    description varchar(200)
);

CREATE TABLE "user" (
    uid SERIAL PRIMARY KEY,
    email varchar(64) UNIQUE,
    name varchar(50)
);

CREATE TABLE "label" (
    lid SERIAL PRIMARY KEY,
    name varchar(50) UNIQUE
);

CREATE TABLE booking (
    bid SERIAL PRIMARY KEY,
    begin TIMESTAMP,
    "end" TIMESTAMP,
    rid SERIAL REFERENCES room(rid),
    uid SERIAL REFERENCES "user"(uid),
    constraint booking_begin_end_diff_check check ((extract(epoch from "end"-begin) / 60) >= 10),
    constraint booking_begin_minute_check check (cast(extract(minute from begin) as decimal) % 10 = 0),
    constraint booking_end_minute_check check (cast(extract(minute from "end") as decimal) % 10 = 0)
);

CREATE TABLE room_label (
    lid SERIAL REFERENCES "label"(lid),
    rid SERIAL REFERENCES room(rid)
);