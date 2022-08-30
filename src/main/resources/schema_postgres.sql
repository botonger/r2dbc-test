CREATE TABLE IF NOT EXISTS apod (id serial primary key,
                   copyright varchar(100), date varchar(12), explanation text,
                   hdurl varchar(255), media_type varchar(12), title varchar(255), url varchar(255), created_time timestamp DEFAULT NOW());
