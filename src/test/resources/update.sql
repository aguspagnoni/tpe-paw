SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

-- New tables --

-- Table: ads

-- DROP TABLE ads;

CREATE SEQUENCE ads_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE ads_id_seq OWNED BY ads.id;

CREATE TABLE ads
(
  id serial NOT NULL,
  imageurl character varying(255) NOT NULL,
  linkurl character varying(255) NOT NULL,
  name character varying(255) NOT NULL,
  CONSTRAINT ads_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ads
  OWNER TO paw;

 -- Table: gatherings

-- DROP TABLE gatherings;

CREATE SEQUENCE gatherings_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE gatherings_id_seq OWNED BY gatherings.id;
  
CREATE TABLE gatherings
(
  id serial NOT NULL,
  name character varying(255) NOT NULL,
  owner_id integer,
  CONSTRAINT gatherings_pkey PRIMARY KEY (id),
  CONSTRAINT fk_6ta8nmm3vud74qulclv7qnkst FOREIGN KEY (owner_id)
      REFERENCES users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE gatherings
  OWNER TO paw;

  -- Table: gatherings_users

-- DROP TABLE gatherings_users;

CREATE TABLE gatherings_users
(
  gatherings_id integer NOT NULL,
  users_id integer NOT NULL,
  CONSTRAINT gatherings_users_pkey PRIMARY KEY (gatherings_id, users_id),
  CONSTRAINT fk_fvact53mhv0u4ddvn5ikrunpl FOREIGN KEY (gatherings_id)
      REFERENCES gatherings (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_tcu7naja3ogjwit789ht8li5j FOREIGN KEY (users_id)
      REFERENCES users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE gatherings_users
  OWNER TO paw;

  
  -- Table: users_blacklist

-- DROP TABLE users_blacklist;

CREATE TABLE users_blacklist
(
  users_id integer NOT NULL,
  blacklist_id integer NOT NULL,
  CONSTRAINT fk_10fn2eia7lss7wkrvwmo3w0fx FOREIGN KEY (blacklist_id)
      REFERENCES users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_g8ynuqkkqs60r1f0rwcrwbssd FOREIGN KEY (users_id)
      REFERENCES users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_10fn2eia7lss7wkrvwmo3w0fx UNIQUE (blacklist_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE users_blacklist
  OWNER TO paw;

  -- Table: users_gatherings

-- DROP TABLE users_gatherings;

CREATE TABLE users_gatherings
(
  users_id integer NOT NULL,
  gatherings_id integer NOT NULL,
  CONSTRAINT fk_hibdy5vwhukc1mygomm1719an FOREIGN KEY (users_id)
      REFERENCES users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_quiixykdj917eb9y7fo805wav FOREIGN KEY (gatherings_id)
      REFERENCES gatherings (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_quiixykdj917eb9y7fo805wav UNIQUE (gatherings_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE users_gatherings
  OWNER TO paw;


-- Migration starts here

ALTER TABLE users RENAME TO __users;

DROP SEQUENCE users_id_seq CASCADE;

-- Table: users

-- DROP TABLE users;

CREATE TABLE users
(
  id serial NOT NULL,
  answer character varying(255) NOT NULL,
  created timestamp without time zone NOT NULL,
  description character varying(255),
  firstname character varying(255) NOT NULL,
  followers integer NOT NULL,
  lastname character varying(255) NOT NULL,
  password character varying(255) NOT NULL,
  profilevisits integer NOT NULL,
  question character varying(255) NOT NULL,
  token character varying(255),
  username character varying(255) NOT NULL,
  viewable boolean NOT NULL,
  CONSTRAINT users_pkey PRIMARY KEY (id),
  CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE users
  OWNER TO paw;

INSERT INTO users(
  id, 
  answer, 
  created, 
  description, 
  firstname, 
  followers, 
  lastname, 
  password, 
  profilevisits, 
  question, 
  username, 
  viewable)

SELECT 
  __users.id, 
  __users.answer,
  __users.created,
  __users.description, 
  __users.firstname, 
  __users.followers, 
  __users.lastname, 
  __users.password,
  __users.profilevists,
  __users.question,
  null,
  __users.username,
  __users.viewable
FROM  
  public.__users;
  
DROP TABLE __users;
  
SELECT SETVAL('users_id_seq', (SELECT MAX(id) FROM users) + 1);