create table planets(
ID INT NOT NULL primary key,
PLANET_NAME varchar(32) NOT NULL,
PLANET_SIZE INT,
PARENT_SYSTEM INT,
DISTANCE INT,
DEGREES FLOAT,
X_ON_MAP INT,
Y_ON_MAP INT
)