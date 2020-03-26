DROP TABLE IF EXISTS description;
DROP TABLE IF EXISTS booking;
DROP TABLE IF EXISTS room_label;
DROP TABLE IF EXISTS "user";
DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS "label";

CREATE TABLE IF NOT EXISTS room (rid SERIAL PRIMARY KEY, name varchar(50), location varchar(50), capacity smallint, constraint room_capacity_min_check check (capacity > 1));
CREATE TABLE IF NOT EXISTS description (rid SERIAL PRIMARY KEY REFERENCES room(rid), description varchar(200));
CREATE TABLE IF NOT EXISTS "user" ( uid SERIAL PRIMARY KEY, email varchar(64) UNIQUE, name varchar(50));
CREATE TABLE IF NOT EXISTS "label" ( lid SERIAL PRIMARY KEY, name varchar(50) UNIQUE);
CREATE TABLE IF NOT EXISTS booking ( bid SERIAL PRIMARY KEY, begin TIMESTAMP, "end" TIMESTAMP, rid SERIAL REFERENCES room(rid), uid SERIAL REFERENCES "user"(uid), constraint booking_begin_end_diff_check check ((extract(epoch from "end"-begin) / 60) >= 10), constraint booking_begin_minute_check check (cast(extract(minute from begin) as decimal) % 10 = 0), constraint booking_end_minute_check check (cast(extract(minute from "end") as decimal) % 10 = 0));
CREATE TABLE IF NOT EXISTS room_label ( lid SERIAL REFERENCES "label"(lid), rid SERIAL REFERENCES room(rid));