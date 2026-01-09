-- ============================================
-- SYSTÈME DE GESTION D'EMPLOI DU TEMPS
-- Base de données PostgreSQL
-- ============================================

-- Table des utilisateurs
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des tâches à la maison
CREATE TABLE tasks (
    task_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    priority VARCHAR(20) CHECK (priority IN ('HAUTE', 'MOYENNE', 'BASSE')),
    duration_minutes INTEGER NOT NULL,
    status VARCHAR(20) CHECK (status IN ('EN_ATTENTE', 'EN_COURS', 'TERMINEE', 'ANNULEE')),
    scheduled_date DATE NOT NULL,
    scheduled_time TIME,
    category VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP
);

-- Table des trajets
CREATE TABLE trips (
    trip_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    departure_location VARCHAR(200) NOT NULL,
    arrival_location VARCHAR(200) NOT NULL,
    departure_time TIME NOT NULL,
    arrival_time TIME NOT NULL,
    duration_minutes INTEGER NOT NULL,
    trip_type VARCHAR(50) CHECK (trip_type IN ('MATIN', 'SOIR', 'AUTRE')),
    transport_mode VARCHAR(50),
    trip_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des repas/cuisine
CREATE TABLE meals (
    meal_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    meal_type VARCHAR(50) CHECK (meal_type IN ('PETIT_DEJEUNER', 'DEJEUNER', 'DINER', 'COLLATION')),
    preparation_minutes INTEGER DEFAULT 30,
    scheduled_time TIME NOT NULL,
    meal_date DATE NOT NULL,
    notes TEXT,
    completed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des périodes de repos
CREATE TABLE rest_periods (
    rest_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    duration_minutes INTEGER NOT NULL,
    rest_type VARCHAR(50) CHECK (rest_type IN ('SOMMEIL', 'SIESTE', 'PAUSE', 'RELATIONNEL')),
    rest_date DATE NOT NULL,
    quality_score INTEGER CHECK (quality_score BETWEEN 1 AND 10),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des alarmes
CREATE TABLE alarms (
    alarm_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    alarm_time TIME NOT NULL,
    alarm_date DATE NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    alarm_type VARCHAR(50) CHECK (alarm_type IN ('REVEIL', 'TRAJET', 'TACHE', 'REPAS', 'REPOS', 'RELATIONNEL')),
    is_active BOOLEAN DEFAULT TRUE,
    is_recurring BOOLEAN DEFAULT FALSE,
    recurrence_pattern VARCHAR(50),
    notification_sent BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des scores de bien-être
CREATE TABLE wellness_scores (
    wellness_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    score_date DATE NOT NULL,
    total_score DECIMAL(5,2) NOT NULL,
    tasks_completed INTEGER DEFAULT 0,
    tasks_total INTEGER DEFAULT 0,
    sleep_hours DECIMAL(4,2),
    exercise_minutes INTEGER DEFAULT 0,
    stress_level INTEGER CHECK (stress_level BETWEEN 1 AND 10),
    productivity_score DECIMAL(5,2),
    social_time_minutes INTEGER DEFAULT 0,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, score_date)
);

-- Table des activités de programmation
CREATE TABLE programming_sessions (
    session_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    start_time TIME NOT NULL,
    end_time TIME,
    duration_minutes INTEGER,
    session_date DATE NOT NULL,
    project_name VARCHAR(200),
    technologies_used TEXT,
    progress_notes TEXT,
    productivity_rating INTEGER CHECK (productivity_rating BETWEEN 1 AND 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes pour optimiser les requêtes
CREATE INDEX idx_tasks_user_date ON tasks(user_id, scheduled_date);
CREATE INDEX idx_tasks_status ON tasks(status);
CREATE INDEX idx_trips_user_date ON trips(user_id, trip_date);
CREATE INDEX idx_alarms_user_date ON alarms(user_id, alarm_date, alarm_time);
CREATE INDEX idx_wellness_user_date ON wellness_scores(user_id, score_date);
CREATE INDEX idx_meals_user_date ON meals(user_id, meal_date);
CREATE INDEX idx_rest_user_date ON rest_periods(user_id, rest_date);

-- Vue pour le dashboard quotidien
CREATE VIEW daily_dashboard AS
SELECT 
    u.user_id,
    u.username,
    CURRENT_DATE as today,
    COUNT(DISTINCT t.task_id) as total_tasks,
    COUNT(DISTINCT CASE WHEN t.status = 'TERMINEE' THEN t.task_id END) as completed_tasks,
    COUNT(DISTINCT tr.trip_id) as total_trips,
    COUNT(DISTINCT m.meal_id) as total_meals,
    COUNT(DISTINCT a.alarm_id) as active_alarms,
    ws.total_score as wellness_score
FROM users u
LEFT JOIN tasks t ON u.user_id = t.user_id AND t.scheduled_date = CURRENT_DATE
LEFT JOIN trips tr ON u.user_id = tr.user_id AND tr.trip_date = CURRENT_DATE
LEFT JOIN meals m ON u.user_id = m.user_id AND m.meal_date = CURRENT_DATE
LEFT JOIN alarms a ON u.user_id = a.user_id AND a.alarm_date = CURRENT_DATE AND a.is_active = TRUE
LEFT JOIN wellness_scores ws ON u.user_id = ws.user_id AND ws.score_date = CURRENT_DATE
GROUP BY u.user_id, u.username, ws.total_score;

-- Fonction pour calculer le score de bien-être
CREATE OR REPLACE FUNCTION calculate_wellness_score(p_user_id INTEGER, p_date DATE)
RETURNS DECIMAL AS $$
DECLARE
    v_score DECIMAL := 0;
    v_task_completion_rate DECIMAL;
    v_sleep_score DECIMAL;
    v_social_score DECIMAL;
BEGIN
    -- Score basé sur la complétion des tâches (40 points)
    SELECT 
        CASE 
            WHEN COUNT(*) = 0 THEN 20
            ELSE (COUNT(CASE WHEN status = 'TERMINEE' THEN 1 END)::DECIMAL / COUNT(*)::DECIMAL) * 40
        END INTO v_task_completion_rate
    FROM tasks
    WHERE user_id = p_user_id AND scheduled_date = p_date;
    
    -- Score basé sur le sommeil (30 points)
    SELECT 
        CASE 
            WHEN SUM(duration_minutes) >= 420 THEN 30  -- 7 heures ou plus
            WHEN SUM(duration_minutes) >= 360 THEN 20  -- 6-7 heures
            ELSE 10
        END INTO v_sleep_score
    FROM rest_periods
    WHERE user_id = p_user_id AND rest_date = p_date AND rest_type = 'SOMMEIL';
    
    -- Score basé sur le temps social/relationnel (30 points)
    SELECT 
        CASE 
            WHEN SUM(duration_minutes) >= 60 THEN 30  -- 1 heure ou plus
            WHEN SUM(duration_minutes) >= 30 THEN 20
            ELSE 10
        END INTO v_social_score
    FROM rest_periods
    WHERE user_id = p_user_id AND rest_date = p_date AND rest_type = 'RELATIONNEL';
    
    v_score := COALESCE(v_task_completion_rate, 0) + 
               COALESCE(v_sleep_score, 0) + 
               COALESCE(v_social_score, 0);
    
    RETURN v_score;
END;
$$ LANGUAGE plpgsql;

-- Trigger pour mettre à jour automatiquement le score de bien-être
CREATE OR REPLACE FUNCTION update_wellness_score_trigger()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO wellness_scores (user_id, score_date, total_score)
    VALUES (NEW.user_id, NEW.scheduled_date, calculate_wellness_score(NEW.user_id, NEW.scheduled_date))
    ON CONFLICT (user_id, score_date) 
    DO UPDATE SET total_score = calculate_wellness_score(NEW.user_id, NEW.scheduled_date);
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER task_wellness_update
AFTER INSERT OR UPDATE ON tasks
FOR EACH ROW
EXECUTE FUNCTION update_wellness_score_trigger();

-- Données de test
INSERT INTO users (username, email, password_hash) 
VALUES ('jean_dupont', 'jean@example.com', 'hashed_password_here');

-- Exemple d'insertion d'une journée type
INSERT INTO trips (user_id, departure_location, arrival_location, departure_time, arrival_time, 
                   duration_minutes, trip_type, trip_date, transport_mode)
VALUES (1, 'Domicile', 'Université', '04:00:00', '05:00:00', 60, 'MATIN', CURRENT_DATE, 'Bus');

INSERT INTO meals (user_id, meal_type, preparation_minutes, scheduled_time, meal_date)
VALUES (1, 'DINER', 30, '19:00:00', CURRENT_DATE);

INSERT INTO rest_periods (user_id, start_time, end_time, duration_minutes, rest_type, rest_date)
VALUES (1, '22:00:00', '23:00:00', 60, 'RELATIONNEL', CURRENT_DATE);

INSERT INTO rest_periods (user_id, start_time, end_time, duration_minutes, rest_type, rest_date)
VALUES (1, '23:00:00', '04:00:00', 300, 'SOMMEIL', CURRENT_DATE);

INSERT INTO alarms (user_id, alarm_time, alarm_date, title, alarm_type)
VALUES 
    (1, '03:45:00', CURRENT_DATE, 'Réveil matinal', 'REVEIL'),
    (1, '22:00:00', CURRENT_DATE, 'Temps relationnel', 'RELATIONNEL');