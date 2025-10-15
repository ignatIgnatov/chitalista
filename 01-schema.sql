-- PostgreSQL Database Initialization Script for Articles Application
-- This script runs automatically when PostgreSQL container starts for the first time

-- Създаване на extension за UUID ако е необходимо
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Създаване на таблица за потребители
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_ADMIN',
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Създаване на таблица за статии с правилния тип за content
CREATE TABLE IF NOT EXISTS articles (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    content TEXT, -- Променено на TEXT вместо bytea
    template VARCHAR(20) NOT NULL DEFAULT 'CLASSIC',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    published BOOLEAN NOT NULL DEFAULT TRUE
);

-- Създаване на таблица за изображения към статии
CREATE TABLE IF NOT EXISTS article_images (
    id BIGSERIAL PRIMARY KEY,
    article_id BIGINT NOT NULL,
    image_url VARCHAR(1000) NOT NULL,
    alt_text VARCHAR(500),
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE
);

-- Създаване на таблица за читалища
CREATE TABLE IF NOT EXISTS chitalishte (
    registration_number INTEGER PRIMARY KEY,
    address TEXT,
    bulstat VARCHAR(50),
    chairman VARCHAR(255),
    chitalishta_url TEXT,
    email VARCHAR(255),
    municipality VARCHAR(100),
    name VARCHAR(255),
    phone VARCHAR(100),
    region VARCHAR(100),
    secretary VARCHAR(255),
    status VARCHAR(50),
    town VARCHAR(100),
    url_to_libraries_site TEXT
);

-- Създаване на таблица за информационни карти
CREATE TABLE IF NOT EXISTS information_card (
    id BIGSERIAL PRIMARY KEY,
    chitalishte_id INTEGER NOT NULL,
    administrative_positions INTEGER,
    amateur_arts INTEGER,
    dancing_groups INTEGER,
    disabilities_and_volunteers INTEGER,
    employees_count FLOAT,
    employees_specialized INTEGER,
    employees_with_higher_education INTEGER,
    folklore_formations INTEGER,
    has_pc_and_internet_services BOOLEAN,
    kraeznanie_clubs INTEGER,
    language_courses INTEGER,
    library_activity TEXT,
    membership_applications INTEGER,
    modern_ballet INTEGER,
    museum_collections INTEGER,
    new_members INTEGER,
    other_activities TEXT,
    other_clubs INTEGER,
    participation_in_events INTEGER,
    participation_in_live_human_treasures_national INTEGER,
    participation_in_live_human_treasures_regional INTEGER,
    participation_in_trainings INTEGER,
    projects_participation_leading INTEGER,
    projects_participation_partner INTEGER,
    reg_number VARCHAR(50),
    rejected_members INTEGER,
    subsidiary_count FLOAT,
    supporting_employees INTEGER,
    theatre_formations INTEGER,
    total_members_count INTEGER,
    town_population INTEGER,
    town_users INTEGER,
    vocal_groups INTEGER,
    workshops_clubs_arts INTEGER,
    year INTEGER,
    bulstat VARCHAR(50),
    email VARCHAR(255),
    kraeznanie_clubs_text TEXT,
    language_courses_text TEXT,
    museum_collections_text TEXT,
    sanctions_for31and33 TEXT,
    url TEXT,
    webpage TEXT,
    workshops_clubs_arts_text TEXT,
    FOREIGN KEY (chitalishte_id) REFERENCES chitalishte(registration_number) ON DELETE CASCADE
);

-- Създаване на индекси за подобрена производителност
CREATE INDEX IF NOT EXISTS idx_articles_published ON articles(published);
CREATE INDEX IF NOT EXISTS idx_articles_created_at ON articles(created_at);
CREATE INDEX IF NOT EXISTS idx_articles_template ON articles(template);
CREATE INDEX IF NOT EXISTS idx_article_images_article_id ON article_images(article_id);
CREATE INDEX IF NOT EXISTS idx_chitalishte_region ON chitalishte(region);
CREATE INDEX IF NOT EXISTS idx_chitalishte_municipality ON chitalishte(municipality);
CREATE INDEX IF NOT EXISTS idx_chitalishte_status ON chitalishte(status);
CREATE INDEX IF NOT EXISTS idx_information_card_chitalishte_id ON information_card(chitalishte_id);
CREATE INDEX IF NOT EXISTS idx_information_card_year ON information_card(year);

-- Създаване на функция за автоматично обновяване на updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Създаване на тригери за автоматично обновяване на updated_at
DROP TRIGGER IF EXISTS update_articles_updated_at ON articles;
CREATE TRIGGER update_articles_updated_at
    BEFORE UPDATE ON articles
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_users_updated_at ON users;
CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Вмъкване на начални данни за администратор
-- Паролата е хеширана версия на 'admin123'
INSERT INTO users (username, password, role, enabled)
VALUES (
    'admin',
    '$2a$10$8.UnVuG9HpHffZAuEfsvE.7B.svl.IIA2l.7bqHn7APRkOMkX/oK2',
    'ROLE_ADMIN',
    TRUE
) ON CONFLICT (username) DO NOTHING;

-- Създаване на view за по-лесно извличане на данни за статии с техните изображения
CREATE OR REPLACE VIEW article_details AS
SELECT
    a.id,
    a.title,
    a.content,
    a.template,
    a.created_at,
    a.updated_at,
    a.published,
    COALESCE(
        json_agg(
            json_build_object(
                'id', ai.id,
                'imageUrl', ai.image_url,
                'altText', ai.alt_text,
                'displayOrder', ai.display_order
            ) ORDER BY ai.display_order
        ) FILTER (WHERE ai.id IS NOT NULL),
        '[]'::json
    ) as images
FROM articles a
LEFT JOIN article_images ai ON a.id = ai.article_id
WHERE a.published = TRUE
GROUP BY a.id, a.title, a.content, a.template, a.created_at, a.updated_at, a.published;

-- Създаване на view за комбинирани данни за читалища и информационни карти
CREATE OR REPLACE VIEW chitalishte_details AS
SELECT
    c.registration_number,
    c.address,
    c.bulstat,
    c.chairman,
    c.chitalishta_url,
    c.email,
    c.municipality,
    c.name,
    c.phone,
    c.region,
    c.secretary,
    c.status,
    c.town,
    c.url_to_libraries_site,
    COALESCE(
        json_agg(
            json_build_object(
                'id', ic.id,
                'year', ic.year,
                'totalMembersCount', ic.total_members_count,
                'employeesCount', ic.employees_count,
                'participationInEvents', ic.participation_in_events,
                'hasPcAndInternetServices', ic.has_pc_and_internet_services,
                'dancingGroups', ic.dancing_groups,
                'vocalGroups', ic.vocal_groups,
                'folkloreFormations', ic.folklore_formations
            ) ORDER BY ic.year DESC
        ) FILTER (WHERE ic.id IS NOT NULL),
        '[]'::json
    ) as information_cards
FROM chitalishte c
LEFT JOIN information_card ic ON c.registration_number = ic.chitalishte_id
GROUP BY c.registration_number, c.address, c.bulstat, c.chairman, c.chitalishta_url, c.email,
         c.municipality, c.name, c.phone, c.region, c.secretary, c.status, c.town, c.url_to_libraries_site;

-- Създаване на функция за търсене на статии
CREATE OR REPLACE FUNCTION search_articles(search_term TEXT)
RETURNS TABLE(
    id BIGINT,
    title VARCHAR,
    content TEXT,
    template VARCHAR,
    created_at TIMESTAMP,
    published BOOLEAN,
    images JSON
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        a.id,
        a.title,
        a.content,
        a.template,
        a.created_at,
        a.published,
        COALESCE(
            json_agg(
                json_build_object(
                    'id', ai.id,
                    'imageUrl', ai.image_url,
                    'altText', ai.alt_text,
                    'displayOrder', ai.display_order
                ) ORDER BY ai.display_order
            ) FILTER (WHERE ai.id IS NOT NULL),
            '[]'::json
        ) as images
    FROM articles a
    LEFT JOIN article_images ai ON a.id = ai.article_id
    WHERE a.published = TRUE
      AND (search_term IS NULL OR
           a.title ILIKE '%' || search_term || '%' OR
           a.content ILIKE '%' || search_term || '%')
    GROUP BY a.id, a.title, a.content, a.template, a.created_at, a.published
    ORDER BY a.created_at DESC;
END;
$$ LANGUAGE plpgsql;

-- Създаване на функция за търсене на читалища
CREATE OR REPLACE FUNCTION search_chitalishta(search_term TEXT, search_region TEXT DEFAULT NULL, search_municipality TEXT DEFAULT NULL)
RETURNS TABLE(
    registration_number INTEGER,
    name VARCHAR,
    municipality VARCHAR,
    region VARCHAR,
    town VARCHAR,
    status VARCHAR,
    chairman VARCHAR,
    phone VARCHAR,
    information_cards JSON
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        c.registration_number,
        c.name,
        c.municipality,
        c.region,
        c.town,
        c.status,
        c.chairman,
        c.phone,
        COALESCE(
            json_agg(
                json_build_object(
                    'id', ic.id,
                    'year', ic.year,
                    'totalMembersCount', ic.total_members_count
                ) ORDER BY ic.year DESC
            ) FILTER (WHERE ic.id IS NOT NULL),
            '[]'::json
        ) as information_cards
    FROM chitalishte c
    LEFT JOIN information_card ic ON c.registration_number = ic.chitalishte_id
    WHERE (search_term IS NULL OR
           c.name ILIKE '%' || search_term || '%' OR
           c.town ILIKE '%' || search_term || '%' OR
           c.municipality ILIKE '%' || search_term || '%')
      AND (search_region IS NULL OR c.region = search_region)
      AND (search_municipality IS NULL OR c.municipality = search_municipality)
    GROUP BY c.registration_number, c.name, c.municipality, c.region, c.town, c.status, c.chairman, c.phone
    ORDER BY c.name;
END;
$$ LANGUAGE plpgsql;

-- Създаване на статистическа функция за статии
CREATE OR REPLACE FUNCTION get_articles_statistics()
RETURNS TABLE(
    total_articles BIGINT,
    published_articles BIGINT,
    classic_count BIGINT,
    modern_count BIGINT,
    focus_count BIGINT,
    latest_article_date TIMESTAMP
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        COUNT(*) as total_articles,
        COUNT(*) FILTER (WHERE published = TRUE) as published_articles,
        COUNT(*) FILTER (WHERE template = 'CLASSIC') as classic_count,
        COUNT(*) FILTER (WHERE template = 'MODERN') as modern_count,
        COUNT(*) FILTER (WHERE template = 'FOCUS') as focus_count,
        MAX(created_at) as latest_article_date
    FROM articles;
END;
$$ LANGUAGE plpgsql;

-- Създаване на статистическа функция за читалища
CREATE OR REPLACE FUNCTION get_chitalishta_statistics()
RETURNS TABLE(
    total_chitalishta BIGINT,
    active_chitalishta BIGINT,
    regions_count BIGINT,
    municipalities_count BIGINT,
    avg_members_per_chitalishte NUMERIC,
    latest_data_year INTEGER
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        COUNT(DISTINCT c.registration_number) as total_chitalishta,
        COUNT(DISTINCT c.registration_number) FILTER (WHERE c.status = 'Действащо') as active_chitalishta,
        COUNT(DISTINCT c.region) as regions_count,
        COUNT(DISTINCT c.municipality) as municipalities_count,
        COALESCE(AVG(ic.total_members_count), 0) as avg_members_per_chitalishte,
        MAX(ic.year) as latest_data_year
    FROM chitalishte c
    LEFT JOIN information_card ic ON c.registration_number = ic.chitalishte_id;
END;
$$ LANGUAGE plpgsql;