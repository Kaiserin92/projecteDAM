-- Tabla Record Label (se crea solo si no existe)
CREATE TABLE IF NOT EXISTS record_label (
    id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (name)
);

-- Datos de Record Label (se insertan solo si no existen)
INSERT OR IGNORE INTO record_label (id, name) VALUES (1, 'Blackened');
INSERT OR IGNORE INTO record_label (id, name) VALUES (2, 'Warner Bros');
INSERT OR IGNORE INTO record_label (id, name) VALUES (3, 'Universal');
INSERT OR IGNORE INTO record_label (id, name) VALUES (4, 'MCA');
INSERT OR IGNORE INTO record_label (id, name) VALUES (5, 'Elektra');
INSERT OR IGNORE INTO record_label (id, name) VALUES (6, 'Capitol');

---------------------------------------------------
-- Tabla Artist
---------------------------------------------------
CREATE TABLE IF NOT EXISTS artist (
    id INTEGER NOT NULL,
    record_label_id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (record_label_id, name),
    FOREIGN KEY (record_label_id) REFERENCES record_label (id)
);

-- Datos de Artist
INSERT OR IGNORE INTO artist (id, record_label_id, name) VALUES (1, 1, 'Metallica');
INSERT OR IGNORE INTO artist (id, record_label_id, name) VALUES (2, 1, 'Megadeth');
INSERT OR IGNORE INTO artist (id, record_label_id, name) VALUES (3, 1, 'Anthrax');
INSERT OR IGNORE INTO artist (id, record_label_id, name) VALUES (4, 2, 'Eric Clapton');
INSERT OR IGNORE INTO artist (id, record_label_id, name) VALUES (5, 2, 'ZZ Top');
INSERT OR IGNORE INTO artist (id, record_label_id, name) VALUES (6, 2, 'Van Halen');
INSERT OR IGNORE INTO artist (id, record_label_id, name) VALUES (7, 3, 'Lynyrd Skynyrd');
INSERT OR IGNORE INTO artist (id, record_label_id, name) VALUES (8, 3, 'AC/DC');
INSERT OR IGNORE INTO artist (id, record_label_id, name) VALUES (9, 6, 'The Beatles');

---------------------------------------------------
-- Tabla Album
---------------------------------------------------
CREATE TABLE IF NOT EXISTS album (
    id INTEGER NOT NULL,
    artist_id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    year INTEGER NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (artist_id, name),
    FOREIGN KEY (artist_id) REFERENCES artist (id)
);

-- Datos de Album
INSERT OR IGNORE INTO album (id, artist_id, name, year) VALUES (1, 1, '...And Justice For All', 1988);
INSERT OR IGNORE INTO album (id, artist_id, name, year) VALUES (2, 1, 'Black Album', 1991);
INSERT OR IGNORE INTO album (id, artist_id, name, year) VALUES (3, 1, 'Master of Puppets', 1986);
INSERT OR IGNORE INTO album (id, artist_id, name, year) VALUES (4, 2, 'Endgame', 2009);
INSERT OR IGNORE INTO album (id, artist_id, name, year) VALUES (5, 2, 'Peace Sells', 1986);
INSERT OR IGNORE INTO album (id, artist_id, name, year) VALUES (6, 3, 'The Greater of 2 Evils', 2004);
INSERT OR IGNORE INTO album (id, artist_id, name, year) VALUES (7, 4, 'Reptile', 2001);
INSERT OR IGNORE INTO album (id, artist_id, name, year) VALUES (8, 4, 'Riding with the King', 2000);
INSERT OR IGNORE INTO album (id, artist_id, name, year) VALUES (9, 5, 'Greatest Hits', 1992);
INSERT OR IGNORE INTO album (id, artist_id, name, year) VALUES (10, 6, 'Greatest Hits', 2004);
INSERT OR IGNORE INTO album (id, artist_id, name, year) VALUES (11, 7, 'All-Time Greatest Hits', 1975);
INSERT OR IGNORE INTO album (id, artist_id, name, year) VALUES (12, 8, 'Greatest Hits', 2003);
INSERT OR IGNORE INTO album (id, artist_id, name, year) VALUES (13, 9, 'Sgt. Pepper''s Lonely Hearts Club Band', 1967);

---------------------------------------------------
-- Tabla Song
---------------------------------------------------
CREATE TABLE IF NOT EXISTS song (
    id INTEGER NOT NULL,
    album_id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    duration REAL NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (album_id, name),
    FOREIGN KEY (album_id) REFERENCES album (id)
);

-- Datos de Song
INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (1, 1, 'One', 7.25);
INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (2, 1, 'Blackened', 6.42);

INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (3, 2, 'Enter Sandman', 5.3);
INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (4, 2, 'Sad But True', 5.29);

INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (5, 3, 'Master of Puppets', 8.35);
INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (6, 3, 'Battery', 5.13);

INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (7, 4, 'Dialectic Chaos', 2.26);
INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (8, 4, 'Endgame', 5.57);

INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (9, 5, 'Peace Sells', 4.09);
INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (10, 5, 'The Conjuring', 5.09);

INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (11, 6, 'Madhouse', 4.26);
INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (12, 6, 'I am the Law', 6.03);

INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (13, 7, 'Reptile', 3.36);
INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (14, 7, 'Modern Girl', 4.49);

INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (15, 8, 'Riding with the King', 4.23);
INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (16, 8, 'Key to the Highway', 3.39);

INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (17, 9, 'Sharp Dressed Man', 4.15);
INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (18, 9, 'Legs', 4.32);

INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (19, 10, 'Eruption', 1.43);
INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (20, 10, 'Hot For Teacher', 4.43);

INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (21, 11, 'Sweet Home Alabama', 4.45);
INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (22, 11, 'Free Bird', 14.23);

INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (23, 12, 'Thunderstruck', 4.52);
INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (24, 12, 'T.N.T', 3.35);

INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (25, 13, 'Sgt. Pepper''s Lonely Hearts Club Band', 2.0333);
INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (26, 13, 'With a Little Help from My Friends', 2.7333);
INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (27, 13, 'Lucy in the Sky with Diamonds', 3.4666);
INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (28, 13, 'Getting Better', 2.80);
INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (29, 13, 'Fixing a Hole', 2.60);
INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (30, 13, 'She''s Leaving Home', 3.5833);
INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (31, 13, 'Being for the Benefit of Mr. Kite!', 2.6166);
INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (32, 13, 'Within You Without You', 5.066);
INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (33, 13, 'When I''m Sixty-Four', 2.6166);
INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (34, 13, 'Lovely Rita', 2.7);
INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (35, 13, 'Good Morning Good Morning', 2.6833);
INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (36, 13, 'Sgt. Pepper''s Lonely Hearts Club Band (Reprise)', 1.3166);
INSERT OR IGNORE INTO song (id, album_id, name, duration) VALUES (37, 13, 'A Day in the Life', 5.65);
