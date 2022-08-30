DROP TABLE IF EXISTS apod;
CREATE TABLE apod (id BIGINT auto_increment primary key,
                copyright varchar(100), date varchar(12), explanation longtext,
                hdurl varchar(255), media_type varchar(12), title varchar(255), url varchar(255), created_time timestamp DEFAULT NOW());
