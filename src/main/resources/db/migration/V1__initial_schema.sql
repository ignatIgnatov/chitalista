-- Initial schema migration for Community Centers Service
-- Flyway migration: V1__initial_schema.sql

-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_ADMIN',
    enabled BOOLEAN NOT NULL DEFAULT true
);

-- Create chitalishte table (id as PRIMARY KEY, registration_number as regular field)
CREATE TABLE chitalishte (
    id SERIAL PRIMARY KEY,
    registration_number INTEGER,
    name VARCHAR(500),
    chitalishta_url TEXT,
    status TEXT,
    bulstat VARCHAR(50) UNIQUE,
    chairman VARCHAR(255),
    secretary VARCHAR(255),
    phone TEXT,
    email VARCHAR(255),
    region VARCHAR(255),
    municipality VARCHAR(255),
    town VARCHAR(255),
    address TEXT,
    url_to_libraries_site TEXT
);

-- Create information_card table
CREATE TABLE information_card (
    id SERIAL PRIMARY KEY,
    chitalishte_id INTEGER NOT NULL,
    url TEXT,
    year INTEGER,
    total_members_count INTEGER,
    membership_applications INTEGER,
    new_members INTEGER,
    rejected_members INTEGER,
    participation_in_live_human_treasures_regional INTEGER,
    participation_in_live_human_treasures_national INTEGER,
    workshops_clubs_arts_text TEXT,
    workshops_clubs_arts INTEGER,
    language_courses_text TEXT,
    language_courses INTEGER,
    kraeznanie_clubs_text TEXT,
    kraeznanie_clubs INTEGER,
    museum_collections_text TEXT,
    museum_collections INTEGER,
    folklore_formations INTEGER,
    theatre_formations INTEGER,
    dancing_groups INTEGER,
    modern_ballet INTEGER,
    vocal_groups INTEGER,
    amateur_arts INTEGER,
    other_clubs INTEGER,
    has_pc_and_internet_services BOOLEAN,
    participation_in_events INTEGER,
    projects_participation_leading INTEGER,
    projects_participation_partner INTEGER,
    disabilities_and_volunteers INTEGER,
    other_activities INTEGER,
    subsidiary_count DOUBLE PRECISION,
    employees_count DOUBLE PRECISION,
    employees_with_higher_education INTEGER,
    employees_specialized INTEGER,
    administrative_positions INTEGER,
    supporting_employees INTEGER,
    participation_in_trainings INTEGER,
    sanctions_for31and33 TEXT,
    bulstat VARCHAR(50),
    reg_number INTEGER,
    town_population INTEGER,
    town_users INTEGER,
    library_activity INTEGER,
    email VARCHAR(255),
    webpage TEXT,
    CONSTRAINT fk_information_card_chitalishte
        FOREIGN KEY (chitalishte_id)
        REFERENCES chitalishte(id)
        ON DELETE CASCADE
);

-- Create articles table
CREATE TABLE articles (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    content TEXT,
    template VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    published BOOLEAN DEFAULT false
);

-- Create article_images table
CREATE TABLE article_images (
    id BIGSERIAL PRIMARY KEY,
    article_id BIGINT,
    image_url VARCHAR(1000) NOT NULL,
    alt_text VARCHAR(500),
    display_order INTEGER,
    CONSTRAINT fk_article_images_article
        FOREIGN KEY (article_id)
        REFERENCES articles(id)
        ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX idx_chitalishte_registration_number ON chitalishte(registration_number);
CREATE INDEX idx_information_card_chitalishte ON information_card(chitalishte_id);
CREATE INDEX idx_information_card_year ON information_card(year);
CREATE INDEX idx_article_images_article ON article_images(article_id);
CREATE INDEX idx_articles_published ON articles(published);
CREATE INDEX idx_articles_created_at ON articles(created_at DESC);
CREATE INDEX idx_chitalishte_region ON chitalishte(region);
CREATE INDEX idx_chitalishte_municipality ON chitalishte(municipality);
CREATE INDEX idx_chitalishte_bulstat ON chitalishte(bulstat);