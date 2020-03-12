drop table if exists ROOM_LABEL;
drop table if exists BOOKING;
drop table if exists ROOMS;
drop table if exists "user";
drop table if exists "label";


CREATE TABLE ROOMS(
    rid SERIAL PRIMARY KEY,
    name varchar(50),
    location varchar(50),
    capacity smallint
);

CREATE TABLE "user"
(
    uid SERIAL PRIMARY KEY ,
    email varchar(64) UNIQUE,
    name varchar(50)
);

CREATE TABLE "label"(
    lid SERIAL PRIMARY KEY,
    name varchar(50) UNIQUE
);

CREATE TABLE BOOKING(
    bid SERIAL PRIMARY KEY ,
    begin TIMESTAMP,
    "end" TIMESTAMP,
    rid SERIAL REFERENCES ROOMS(rid),
    uid SERIAL REFERENCES "user"(uid)
);

CREATE TABLE ROOM_LABEL(
    rlid SERIAL PRIMARY KEY ,
    lid SERIAL REFERENCES "label"(lid),
    rid SERIAL REFERENCES ROOMS(rid)
)