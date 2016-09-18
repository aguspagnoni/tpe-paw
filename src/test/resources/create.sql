--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: ads; Type: TABLE; Schema: public; Owner: paw; Tablespace: 
--

CREATE TABLE ads (
    id integer NOT NULL,
    imageurl character varying(255) NOT NULL,
    linkurl character varying(255) NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.ads OWNER TO paw;

--
-- Name: ads_id_seq; Type: SEQUENCE; Schema: public; Owner: paw
--

CREATE SEQUENCE ads_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ads_id_seq OWNER TO paw;

--
-- Name: ads_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw
--

ALTER SEQUENCE ads_id_seq OWNED BY ads.id;


--
-- Name: favorite_posts; Type: TABLE; Schema: public; Owner: paw; Tablespace: 
--

CREATE TABLE favorite_posts (
    user_id integer NOT NULL,
    post_id integer NOT NULL
);


ALTER TABLE public.favorite_posts OWNER TO paw;

--
-- Name: gatherings; Type: TABLE; Schema: public; Owner: paw; Tablespace: 
--

CREATE TABLE gatherings (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    owner_id integer
);


ALTER TABLE public.gatherings OWNER TO paw;

--
-- Name: gatherings_id_seq; Type: SEQUENCE; Schema: public; Owner: paw
--

CREATE SEQUENCE gatherings_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.gatherings_id_seq OWNER TO paw;

--
-- Name: gatherings_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw
--

ALTER SEQUENCE gatherings_id_seq OWNED BY gatherings.id;


--
-- Name: gatherings_users; Type: TABLE; Schema: public; Owner: paw; Tablespace: 
--

CREATE TABLE gatherings_users (
    gatherings_id integer NOT NULL,
    users_id integer NOT NULL
);


ALTER TABLE public.gatherings_users OWNER TO paw;

--
-- Name: hashtags; Type: TABLE; Schema: public; Owner: paw; Tablespace: 
--

CREATE TABLE hashtags (
    id integer NOT NULL,
    created timestamp without time zone,
    name character varying(255),
    creator_id integer
);


ALTER TABLE public.hashtags OWNER TO paw;

--
-- Name: hashtags_id_seq; Type: SEQUENCE; Schema: public; Owner: paw
--

CREATE SEQUENCE hashtags_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hashtags_id_seq OWNER TO paw;

--
-- Name: hashtags_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw
--

ALTER SEQUENCE hashtags_id_seq OWNED BY hashtags.id;


--
-- Name: notifications; Type: TABLE; Schema: public; Owner: paw; Tablespace: 
--

CREATE TABLE notifications (
    id integer NOT NULL,
    created timestamp without time zone NOT NULL,
    message character varying(255) NOT NULL,
    type character varying(255) NOT NULL,
    user_id integer
);


ALTER TABLE public.notifications OWNER TO paw;

--
-- Name: notifications_id_seq; Type: SEQUENCE; Schema: public; Owner: paw
--

CREATE SEQUENCE notifications_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.notifications_id_seq OWNER TO paw;

--
-- Name: notifications_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw
--

ALTER SEQUENCE notifications_id_seq OWNED BY notifications.id;


--
-- Name: posts; Type: TABLE; Schema: public; Owner: paw; Tablespace: 
--

CREATE TABLE posts (
    id integer NOT NULL,
    body character varying(255),
    created timestamp without time zone,
    originalpost_id integer,
    user_id integer
);


ALTER TABLE public.posts OWNER TO paw;

--
-- Name: posts_hashtags; Type: TABLE; Schema: public; Owner: paw; Tablespace: 
--

CREATE TABLE posts_hashtags (
    posts_id integer NOT NULL,
    hashtags_id integer NOT NULL
);


ALTER TABLE public.posts_hashtags OWNER TO paw;

--
-- Name: posts_id_seq; Type: SEQUENCE; Schema: public; Owner: paw
--

CREATE SEQUENCE posts_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.posts_id_seq OWNER TO paw;

--
-- Name: posts_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw
--

ALTER SEQUENCE posts_id_seq OWNED BY posts.id;


--
-- Name: shortenedurl; Type: TABLE; Schema: public; Owner: paw; Tablespace: 
--

CREATE TABLE shortenedurl (
    id integer NOT NULL,
    code character varying(255),
    destination character varying(255)
);


ALTER TABLE public.shortenedurl OWNER TO paw;

--
-- Name: shortenedurl_id_seq; Type: SEQUENCE; Schema: public; Owner: paw
--

CREATE SEQUENCE shortenedurl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.shortenedurl_id_seq OWNER TO paw;

--
-- Name: shortenedurl_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw
--

ALTER SEQUENCE shortenedurl_id_seq OWNED BY shortenedurl.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: paw; Tablespace: 
--

CREATE TABLE users (
    id integer NOT NULL,
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
    viewable boolean NOT NULL
);


ALTER TABLE public.users OWNER TO paw;

--
-- Name: users_blacklist; Type: TABLE; Schema: public; Owner: paw; Tablespace: 
--

CREATE TABLE users_blacklist (
    users_id integer NOT NULL,
    blacklist_id integer NOT NULL
);


ALTER TABLE public.users_blacklist OWNER TO paw;

--
-- Name: users_gatherings; Type: TABLE; Schema: public; Owner: paw; Tablespace: 
--

CREATE TABLE users_gatherings (
    users_id integer NOT NULL,
    gatherings_id integer NOT NULL
);


ALTER TABLE public.users_gatherings OWNER TO paw;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: paw
--

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_id_seq OWNER TO paw;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: paw
--

ALTER SEQUENCE users_id_seq OWNED BY users.id;


--
-- Name: users_notifications; Type: TABLE; Schema: public; Owner: paw; Tablespace: 
--

CREATE TABLE users_notifications (
    users_id integer NOT NULL,
    notifications_id integer NOT NULL
);


ALTER TABLE public.users_notifications OWNER TO paw;

--
-- Name: users_posts; Type: TABLE; Schema: public; Owner: paw; Tablespace: 
--

CREATE TABLE users_posts (
    users_id integer NOT NULL,
    posts_id integer NOT NULL
);


ALTER TABLE public.users_posts OWNER TO paw;

--
-- Name: users_users; Type: TABLE; Schema: public; Owner: paw; Tablespace: 
--

CREATE TABLE users_users (
    users_id integer NOT NULL,
    followees_id integer NOT NULL
);


ALTER TABLE public.users_users OWNER TO paw;

--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw
--

ALTER TABLE ONLY ads ALTER COLUMN id SET DEFAULT nextval('ads_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw
--

ALTER TABLE ONLY gatherings ALTER COLUMN id SET DEFAULT nextval('gatherings_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw
--

ALTER TABLE ONLY hashtags ALTER COLUMN id SET DEFAULT nextval('hashtags_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw
--

ALTER TABLE ONLY notifications ALTER COLUMN id SET DEFAULT nextval('notifications_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw
--

ALTER TABLE ONLY posts ALTER COLUMN id SET DEFAULT nextval('posts_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw
--

ALTER TABLE ONLY shortenedurl ALTER COLUMN id SET DEFAULT nextval('shortenedurl_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: paw
--

ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--
-- Name: ads_pkey; Type: CONSTRAINT; Schema: public; Owner: paw; Tablespace: 
--

ALTER TABLE ONLY ads
    ADD CONSTRAINT ads_pkey PRIMARY KEY (id);


--
-- Name: gatherings_pkey; Type: CONSTRAINT; Schema: public; Owner: paw; Tablespace: 
--

ALTER TABLE ONLY gatherings
    ADD CONSTRAINT gatherings_pkey PRIMARY KEY (id);


--
-- Name: gatherings_users_pkey; Type: CONSTRAINT; Schema: public; Owner: paw; Tablespace: 
--

ALTER TABLE ONLY gatherings_users
    ADD CONSTRAINT gatherings_users_pkey PRIMARY KEY (gatherings_id, users_id);


--
-- Name: hashtags_pkey; Type: CONSTRAINT; Schema: public; Owner: paw; Tablespace: 
--

ALTER TABLE ONLY hashtags
    ADD CONSTRAINT hashtags_pkey PRIMARY KEY (id);


--
-- Name: notifications_pkey; Type: CONSTRAINT; Schema: public; Owner: paw; Tablespace: 
--

ALTER TABLE ONLY notifications
    ADD CONSTRAINT notifications_pkey PRIMARY KEY (id);


--
-- Name: posts_hashtags_pkey; Type: CONSTRAINT; Schema: public; Owner: paw; Tablespace: 
--

ALTER TABLE ONLY posts_hashtags
    ADD CONSTRAINT posts_hashtags_pkey PRIMARY KEY (posts_id, hashtags_id);


--
-- Name: posts_pkey; Type: CONSTRAINT; Schema: public; Owner: paw; Tablespace: 
--

ALTER TABLE ONLY posts
    ADD CONSTRAINT posts_pkey PRIMARY KEY (id);


--
-- Name: shortenedurl_pkey; Type: CONSTRAINT; Schema: public; Owner: paw; Tablespace: 
--

ALTER TABLE ONLY shortenedurl
    ADD CONSTRAINT shortenedurl_pkey PRIMARY KEY (id);


--
-- Name: uk_10fn2eia7lss7wkrvwmo3w0fx; Type: CONSTRAINT; Schema: public; Owner: paw; Tablespace: 
--

ALTER TABLE ONLY users_blacklist
    ADD CONSTRAINT uk_10fn2eia7lss7wkrvwmo3w0fx UNIQUE (blacklist_id);


--
-- Name: uk_4nsbfp7tf7f9rlw76oop43w15; Type: CONSTRAINT; Schema: public; Owner: paw; Tablespace: 
--

ALTER TABLE ONLY users_posts
    ADD CONSTRAINT uk_4nsbfp7tf7f9rlw76oop43w15 UNIQUE (posts_id);


--
-- Name: uk_fqai0p0e0nsyh9pp2j0h32ggq; Type: CONSTRAINT; Schema: public; Owner: paw; Tablespace: 
--

ALTER TABLE ONLY users_notifications
    ADD CONSTRAINT uk_fqai0p0e0nsyh9pp2j0h32ggq UNIQUE (notifications_id);


--
-- Name: uk_oed8qhhrhflqj7olh3oeii6ym; Type: CONSTRAINT; Schema: public; Owner: paw; Tablespace: 
--

ALTER TABLE ONLY hashtags
    ADD CONSTRAINT uk_oed8qhhrhflqj7olh3oeii6ym UNIQUE (name);


--
-- Name: uk_quiixykdj917eb9y7fo805wav; Type: CONSTRAINT; Schema: public; Owner: paw; Tablespace: 
--

ALTER TABLE ONLY users_gatherings
    ADD CONSTRAINT uk_quiixykdj917eb9y7fo805wav UNIQUE (gatherings_id);


--
-- Name: uk_r43af9ap4edm43mmtq01oddj6; Type: CONSTRAINT; Schema: public; Owner: paw; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username);


--
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: paw; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: fk_10fn2eia7lss7wkrvwmo3w0fx; Type: FK CONSTRAINT; Schema: public; Owner: paw
--

ALTER TABLE ONLY users_blacklist
    ADD CONSTRAINT fk_10fn2eia7lss7wkrvwmo3w0fx FOREIGN KEY (blacklist_id) REFERENCES users(id);


--
-- Name: fk_19fmrcoiyo0yfq00pgxoa09sg; Type: FK CONSTRAINT; Schema: public; Owner: paw
--

ALTER TABLE ONLY favorite_posts
    ADD CONSTRAINT fk_19fmrcoiyo0yfq00pgxoa09sg FOREIGN KEY (user_id) REFERENCES users(id);


--
-- Name: fk_48rrhaq33ik1wopaihv1og3uf; Type: FK CONSTRAINT; Schema: public; Owner: paw
--

ALTER TABLE ONLY hashtags
    ADD CONSTRAINT fk_48rrhaq33ik1wopaihv1og3uf FOREIGN KEY (creator_id) REFERENCES users(id);


--
-- Name: fk_4nsbfp7tf7f9rlw76oop43w15; Type: FK CONSTRAINT; Schema: public; Owner: paw
--

ALTER TABLE ONLY users_posts
    ADD CONSTRAINT fk_4nsbfp7tf7f9rlw76oop43w15 FOREIGN KEY (posts_id) REFERENCES posts(id);


--
-- Name: fk_5rshkwfgpxcvm9pwgbausf6yy; Type: FK CONSTRAINT; Schema: public; Owner: paw
--

ALTER TABLE ONLY posts_hashtags
    ADD CONSTRAINT fk_5rshkwfgpxcvm9pwgbausf6yy FOREIGN KEY (posts_id) REFERENCES posts(id);


--
-- Name: fk_6ta8nmm3vud74qulclv7qnkst; Type: FK CONSTRAINT; Schema: public; Owner: paw
--

ALTER TABLE ONLY gatherings
    ADD CONSTRAINT fk_6ta8nmm3vud74qulclv7qnkst FOREIGN KEY (owner_id) REFERENCES users(id);


--
-- Name: fk_7jmkiaxph8k0ui6nfgpn9it53; Type: FK CONSTRAINT; Schema: public; Owner: paw
--

ALTER TABLE ONLY users_posts
    ADD CONSTRAINT fk_7jmkiaxph8k0ui6nfgpn9it53 FOREIGN KEY (users_id) REFERENCES users(id);


--
-- Name: fk_7n03ak9vril0ibbmjmbaf6uhb; Type: FK CONSTRAINT; Schema: public; Owner: paw
--

ALTER TABLE ONLY posts_hashtags
    ADD CONSTRAINT fk_7n03ak9vril0ibbmjmbaf6uhb FOREIGN KEY (hashtags_id) REFERENCES hashtags(id);


--
-- Name: fk_drf0mikfn0edc462mk8ea42cb; Type: FK CONSTRAINT; Schema: public; Owner: paw
--

ALTER TABLE ONLY users_users
    ADD CONSTRAINT fk_drf0mikfn0edc462mk8ea42cb FOREIGN KEY (followees_id) REFERENCES users(id);


--
-- Name: fk_fqai0p0e0nsyh9pp2j0h32ggq; Type: FK CONSTRAINT; Schema: public; Owner: paw
--

ALTER TABLE ONLY users_notifications
    ADD CONSTRAINT fk_fqai0p0e0nsyh9pp2j0h32ggq FOREIGN KEY (notifications_id) REFERENCES notifications(id);


--
-- Name: fk_fvact53mhv0u4ddvn5ikrunpl; Type: FK CONSTRAINT; Schema: public; Owner: paw
--

ALTER TABLE ONLY gatherings_users
    ADD CONSTRAINT fk_fvact53mhv0u4ddvn5ikrunpl FOREIGN KEY (gatherings_id) REFERENCES gatherings(id);


--
-- Name: fk_g8ynuqkkqs60r1f0rwcrwbssd; Type: FK CONSTRAINT; Schema: public; Owner: paw
--

ALTER TABLE ONLY users_blacklist
    ADD CONSTRAINT fk_g8ynuqkkqs60r1f0rwcrwbssd FOREIGN KEY (users_id) REFERENCES users(id);


--
-- Name: fk_gt169u2w27kk35aybnhh4ocnj; Type: FK CONSTRAINT; Schema: public; Owner: paw
--

ALTER TABLE ONLY notifications
    ADD CONSTRAINT fk_gt169u2w27kk35aybnhh4ocnj FOREIGN KEY (user_id) REFERENCES users(id);


--
-- Name: fk_hibdy5vwhukc1mygomm1719an; Type: FK CONSTRAINT; Schema: public; Owner: paw
--

ALTER TABLE ONLY users_gatherings
    ADD CONSTRAINT fk_hibdy5vwhukc1mygomm1719an FOREIGN KEY (users_id) REFERENCES users(id);


--
-- Name: fk_icftu76lg1l8x0roteo1cmc1k; Type: FK CONSTRAINT; Schema: public; Owner: paw
--

ALTER TABLE ONLY favorite_posts
    ADD CONSTRAINT fk_icftu76lg1l8x0roteo1cmc1k FOREIGN KEY (post_id) REFERENCES posts(id);


--
-- Name: fk_jk1pe75x6ly51cq39mmv0dc58; Type: FK CONSTRAINT; Schema: public; Owner: paw
--

ALTER TABLE ONLY posts
    ADD CONSTRAINT fk_jk1pe75x6ly51cq39mmv0dc58 FOREIGN KEY (originalpost_id) REFERENCES posts(id);


--
-- Name: fk_jtinty3o92lhifdq5kalgkj6y; Type: FK CONSTRAINT; Schema: public; Owner: paw
--

ALTER TABLE ONLY users_notifications
    ADD CONSTRAINT fk_jtinty3o92lhifdq5kalgkj6y FOREIGN KEY (users_id) REFERENCES users(id);


--
-- Name: fk_k1awmvy6qme1fmgum0xrc1iws; Type: FK CONSTRAINT; Schema: public; Owner: paw
--

ALTER TABLE ONLY users_users
    ADD CONSTRAINT fk_k1awmvy6qme1fmgum0xrc1iws FOREIGN KEY (users_id) REFERENCES users(id);


--
-- Name: fk_pn7a4a2mjksl19jlm7k106m7x; Type: FK CONSTRAINT; Schema: public; Owner: paw
--

ALTER TABLE ONLY posts
    ADD CONSTRAINT fk_pn7a4a2mjksl19jlm7k106m7x FOREIGN KEY (user_id) REFERENCES users(id);


--
-- Name: fk_quiixykdj917eb9y7fo805wav; Type: FK CONSTRAINT; Schema: public; Owner: paw
--

ALTER TABLE ONLY users_gatherings
    ADD CONSTRAINT fk_quiixykdj917eb9y7fo805wav FOREIGN KEY (gatherings_id) REFERENCES gatherings(id);


--
-- Name: fk_tcu7naja3ogjwit789ht8li5j; Type: FK CONSTRAINT; Schema: public; Owner: paw
--

ALTER TABLE ONLY gatherings_users
    ADD CONSTRAINT fk_tcu7naja3ogjwit789ht8li5j FOREIGN KEY (users_id) REFERENCES users(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

