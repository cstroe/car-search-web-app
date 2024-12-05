CREATE DATABASE vehicles;
CREATE SCHEMA vehicles;

CREATE TABLE vehicles.all
(
    id         SERIAL PRIMARY KEY,
    year       smallint NOT NULL,
    make       varchar(16) NOT NULL,
    model      varchar(16) NOT NULL,
    trim       varchar(16),
    exterior   varchar(32),
    interior   varchar(16),
    miles      integer,
    vin        varchar(17) UNIQUE,
    available  boolean     NOT NULL DEFAULT true,
    interested boolean     NOT NULL DEFAULT true,
    dealer     varchar(32),
    url        varchar(2048)
);

CREATE TABLE vehicles.price
(
    id      SERIAL PRIMARY KEY,
    vehicle integer references vehicles.all (id),
    date    date,
    price   integer
);

