-- ============================================
-- DONNÉES DE TEST COMPLÈTES
-- Emploi du temps réaliste pour un étudiant
-- ============================================

-- Nettoyage des données existantes (sauf utilisateurs)
TRUNCATE TABLE programming_sessions, wellness_scores, alarms, 
               rest_periods, meals, trips, tasks RESTART IDENTITY CASCADE;

-- ============================================
-- UTILISATEUR DE TEST
-- ============================================

-- Mot de passe : password123 (à hasher en production avec BCrypt)
INSERT INTO users (username, email, password_hash) 
VALUES ('jean_dupont', 'jean@example.com', 'password123')
ON CONFLICT (username) DO NOTHING;

-- ============================================
-- EMPLOI DU TEMPS TYPE D'UNE SEMAINE
-- ============================================

-- === LUNDI ===

-- Trajets Lundi
INSERT INTO trips (user_id, departure_location, arrival_location, 
                   departure_time, arrival_time, duration_minutes, 
                   trip_type, trip_date, transport_mode) VALUES
(1, 'Domicile', 'ITUniversity', '04:00:00', '05:00:00', 60, 'MATIN', CURRENT_DATE, 'Bus'),
(1, 'ITUniversity', 'Domicile', '17:00:00', '18:00:00', 60, 'SOIR', CURRENT_DATE, 'Bus');

-- Tâches Lundi
INSERT INTO tasks (user_id, title, description, priority, duration_minutes, 
                   status, scheduled_date, scheduled_time, category) VALUES
(1, 'Cours Java POO', 'Réviser les concepts d''héritage et polymorphisme', 
    'HAUTE', 120, 'EN_ATTENTE', CURRENT_DATE, '06:00:00', 'Études'),
(1, 'TP Base de Données', 'Exercices SQL et requêtes complexes', 
    'HAUTE', 90, 'EN_ATTENTE', CURRENT_DATE, '08:30:00', 'Études'),
(1, 'Projet Servlet', 'Développer les contrôleurs MVC', 
    'HAUTE', 180, 'EN_ATTENTE', CURRENT_DATE, '14:00:00', 'Programmation'),
(1, 'Rangement chambre', 'Nettoyer et organiser', 
    'BASSE', 30, 'EN_ATTENTE', CURRENT_DATE, '19:30:00', 'Maison');

-- Repas Lundi
INSERT INTO meals (user_id, meal_type, preparation_minutes, 
                   scheduled_time, meal_date, notes) VALUES
(1, 'DINER', 30, '19:00:00', CURRENT_DATE, 'Riz au poulet et légumes');

-- Repos Lundi
INSERT INTO rest_periods (user_id, start_time, end_time, duration_minutes, 
                          rest_type, rest_date, quality_score) VALUES
(1, '22:00:00', '23:00:00', 60, 'RELATIONNEL', CURRENT_DATE, 8),
(1, '23:00:00', '04:00:00', 300, 'SOMMEIL', CURRENT_DATE, 7);

-- Alarmes Lundi
INSERT INTO alarms (user_id, alarm_time, alarm_date, title, description, 
                   alarm_type, is_recurring) VALUES
(1, '03:45:00', CURRENT_DATE, 'Réveil matinal', 'Préparer le trajet', 'REVEIL', TRUE),
(1, '05:30:00', CURRENT_DATE, 'Début cours Java', 'Se préparer', 'TACHE', FALSE),
(1, '13:30:00', CURRENT_DATE, 'Début projet Servlet', 'Lancer IDE', 'TACHE', FALSE),
(1, '19:00:00', CURRENT_DATE, 'Préparer le dîner', 'Cuisine', 'REPAS', TRUE),
(1, '22:00:00', CURRENT_DATE, 'Temps relationnel', 'Appel famille/amis', 'RELATIONNEL', TRUE);

-- === MARDI ===

-- Trajets Mardi
INSERT INTO trips (user_id, departure_location, arrival_location, 
                   departure_time, arrival_time, duration_minutes, 
                   trip_type, trip_date, transport_mode) VALUES
(1, 'Domicile', 'ITUniversity', '04:00:00', '05:00:00', 60, 'MATIN', CURRENT_DATE + 1, 'Bus'),
(1, 'ITUniversity', 'Domicile', '17:00:00', '18:00:00', 60, 'SOIR', CURRENT_DATE + 1, 'Bus');

-- Tâches Mardi
INSERT INTO tasks (user_id, title, description, priority, duration_minutes, 
                   status, scheduled_date, scheduled_time, category) VALUES
(1, 'Cours Réseau', 'Architecture TCP/IP et protocoles', 
    'HAUTE', 120, 'EN_ATTENTE', CURRENT_DATE + 1, '06:00:00', 'Études'),
(1, 'TP JSP', 'Créer des pages web dynamiques', 
    'HAUTE', 90, 'EN_ATTENTE', CURRENT_DATE + 1, '09:00:00', 'Programmation'),
(1, 'Projet Dashboard', 'Design de l''interface utilisateur', 
    'MOYENNE', 150, 'EN_ATTENTE', CURRENT_DATE + 1, '14:00:00', 'Programmation'),
(1, 'Lessive', 'Laver et étendre le linge', 
    'MOYENNE', 45, 'EN_ATTENTE', CURRENT_DATE + 1, '19:30:00', 'Maison');

-- Repas Mardi
INSERT INTO meals (user_id, meal_type, preparation_minutes, 
                   scheduled_time, meal_date) VALUES
(1, 'DINER', 30, '19:00:00', CURRENT_DATE + 1);

-- === MERCREDI ===

-- Trajets Mercredi
INSERT INTO trips (user_id, departure_location, arrival_location, 
                   departure_time, arrival_time, duration_minutes, 
                   trip_type, trip_date, transport_mode) VALUES
(1, 'Domicile', 'ITUniversity', '04:00:00', '05:00:00', 60, 'MATIN', CURRENT_DATE + 2, 'Bus'),
(1, 'ITUniversity', 'Domicile', '17:00:00', '18:00:00', 60, 'SOIR', CURRENT_DATE + 2, 'Bus');

-- Tâches Mercredi
INSERT INTO tasks (user_id, title, description, priority, duration_minutes, 
                   status, scheduled_date, scheduled_time, category) VALUES
(1, 'Cours Architecture Logicielle', 'Patterns MVC, DAO, Singleton', 
    'HAUTE', 120, 'EN_ATTENTE', CURRENT_DATE + 2, '06:00:00', 'Études'),
(1, 'Anglais technique', 'Vocabulaire IT et rédaction', 
    'MOYENNE', 60, 'EN_ATTENTE', CURRENT_DATE + 2, '09:00:00', 'Études'),
(1, 'Intégration PostgreSQL', 'Connexion DB et requêtes', 
    'HAUTE', 180, 'EN_ATTENTE', CURRENT_DATE + 2, '14:00:00', 'Programmation'),
(1, 'Sport', 'Exercices physiques à domicile', 
    'MOYENNE', 30, 'EN_ATTENTE', CURRENT_DATE + 2, '19:30:00', 'Sport');

-- === JEUDI ===

-- Trajets Jeudi
INSERT INTO trips (user_id, departure_location, arrival_location, 
                   departure_time, arrival_time, duration_minutes, 
                   trip_type, trip_date, transport_mode) VALUES
(1, 'Domicile', 'ITUniversity', '04:00:00', '05:00:00', 60, 'MATIN', CURRENT_DATE + 3, 'Bus'),
(1, 'ITUniversity', 'Domicile', '17:00:00', '18:00:00', 60, 'SOIR', CURRENT_DATE + 3, 'Bus');

-- Tâches Jeudi
INSERT INTO tasks (user_id, title, description, priority, duration_minutes, 
                   status, scheduled_date, scheduled_time, category) VALUES
(1, 'Cours Sécurité Web', 'Authentification et sessions', 
    'HAUTE', 120, 'EN_ATTENTE', CURRENT_DATE + 3, '06:00:00', 'Études'),
(1, 'Tests unitaires', 'Vérifier les DAO et Servlets', 
    'HAUTE', 90, 'EN_ATTENTE', CURRENT_DATE + 3, '09:00:00', 'Programmation'),
(1, 'Documentation projet', 'Rédiger le README et javadoc', 
    'MOYENNE', 120, 'EN_ATTENTE', CURRENT_DATE + 3, '14:00:00', 'Programmation'),
(1, 'Courses', 'Faire les achats de la semaine', 
    'HAUTE', 60, 'EN_ATTENTE', CURRENT_DATE + 3, '19:00:00', 'Maison');

-- === VENDREDI ===

-- Trajets Vendredi
INSERT INTO trips (user_id, departure_location, arrival_location, 
                   departure_time, arrival_time, duration_minutes, 
                   trip_type, trip_date, transport_mode) VALUES
(1, 'Domicile', 'ITUniversity', '04:00:00', '05:00:00', 60, 'MATIN', CURRENT_DATE + 4, 'Bus'),
(1, 'ITUniversity', 'Domicile', '17:00:00', '18:00:00', 60, 'SOIR', CURRENT_DATE + 4, 'Bus');

-- Tâches Vendredi
INSERT INTO tasks (user_id, title, description, priority, duration_minutes, 
                   status, scheduled_date, scheduled_time, category) VALUES
(1, 'Révision générale', 'Reprendre tous les cours de la semaine', 
    'HAUTE', 150, 'EN_ATTENTE', CURRENT_DATE + 4, '06:00:00', 'Études'),
(1, 'Finalisation projet', 'Derniers ajustements et déploiement', 
    'HAUTE', 240, 'EN_ATTENTE', CURRENT_DATE + 4, '13:00:00', 'Programmation'),
(1, 'Préparation semaine suivante', 'Planifier les tâches', 
    'MOYENNE', 45, 'EN_ATTENTE', CURRENT_DATE + 4, '19:30:00', 'Organisation');

-- ============================================
-- SESSIONS DE PROGRAMMATION
-- ============================================

INSERT INTO programming_sessions (user_id, start_time, end_time, duration_minutes, 
                                  session_date, project_name, technologies_used, 
                                  progress_notes, productivity_rating) VALUES
(1, '14:00:00', '17:00:00', 180, CURRENT_DATE, 'Wellness App', 
    'Java, PostgreSQL, JSP, Servlets', 
    'Implémentation du système d''alarmes et calcul du score de bien-être', 5),
(1, '14:00:00', '16:30:00', 150, CURRENT_DATE + 1, 'Wellness App', 
    'Java, Bootstrap, JavaScript', 
    'Développement de l''interface dashboard avec animations', 4),
(1, '14:00:00', '17:00:00', 180, CURRENT_DATE + 2, 'Wellness App', 
    'PostgreSQL, JDBC', 
    'Optimisation des requêtes et création des indexes', 5);

-- ============================================
-- CALCUL ET INSERTION DES SCORES DE BIEN-ÊTRE
-- ============================================

-- Le trigger calculate_wellness_score se déclenche automatiquement
-- mais on peut aussi calculer manuellement :

INSERT INTO wellness_scores (user_id, score_date, total_score, 
                             tasks_completed, tasks_total, sleep_hours, 
                             social_time_minutes, notes)
SELECT 
    1 as user_id,
    CURRENT_DATE as score_date,
    calculate_wellness_score(1, CURRENT_DATE) as total_score,
    0 as tasks_completed,
    (SELECT COUNT(*) FROM tasks WHERE user_id = 1 AND scheduled_date = CURRENT_DATE) as tasks_total,
    5.00 as sleep_hours,
    60 as social_time_minutes,
    'Journée productive avec bon équilibre'
ON CONFLICT (user_id, score_date) DO UPDATE SET
    total_score = calculate_wellness_score(1, CURRENT_DATE);

-- ============================================
-- ALARMES RÉCURRENTES POUR TOUTE LA SEMAINE
-- ============================================

-- Réveil quotidien
INSERT INTO alarms (user_id, alarm_time, alarm_date, title, alarm_type, is_recurring, recurrence_pattern)
SELECT 1, '03:45:00', CURRENT_DATE + i, 'Réveil matinal', 'REVEIL', TRUE, 'DAILY'
FROM generate_series(0, 6) AS i;

-- Temps relationnel quotidien
INSERT INTO alarms (user_id, alarm_time, alarm_date, title, alarm_type, is_recurring, recurrence_pattern)
SELECT 1, '22:00:00', CURRENT_DATE + i, 'Temps relationnel', 'RELATIONNEL', TRUE, 'DAILY'
FROM generate_series(0, 6) AS i;

-- Heure du coucher
INSERT INTO alarms (user_id, alarm_time, alarm_date, title, alarm_type, is_recurring, recurrence_pattern)
SELECT 1, '23:00:00', CURRENT_DATE + i, 'Heure de dormir', 'REPOS', TRUE, 'DAILY'
FROM generate_series(0, 6) AS i;

-- ============================================
-- VÉRIFICATIONS
-- ============================================

-- Afficher un résumé
SELECT 
    'Total Tâches' as Catégorie, 
    COUNT(*) as Nombre 
FROM tasks WHERE user_id = 1
UNION ALL
SELECT 'Total Trajets', COUNT(*) FROM trips WHERE user_id = 1
UNION ALL
SELECT 'Total Alarmes', COUNT(*) FROM alarms WHERE user_id = 1
UNION ALL
SELECT 'Total Repas', COUNT(*) FROM meals WHERE user_id = 1
UNION ALL
SELECT 'Total Sessions Prog', COUNT(*) FROM programming_sessions WHERE user_id = 1;

-- Afficher le dashboard quotidien
SELECT * FROM daily_dashboard WHERE user_id = 1;

-- Afficher le score de bien-être d'aujourd'hui
SELECT 
    score_date,
    total_score,
    tasks_completed || '/' || tasks_total as taches,
    sleep_hours || 'h' as sommeil,
    social_time_minutes || 'min' as temps_social,
    CASE 
        WHEN total_score >= 80 THEN '🌟 Excellent'
        WHEN total_score >= 60 THEN '😊 Bon'
        WHEN total_score >= 40 THEN '😐 Moyen'
        ELSE '😔 À améliorer'
    END as evaluation
FROM wellness_scores 
WHERE user_id = 1 AND score_date = CURRENT_DATE;

-- Message de confirmation
SELECT '✅ Données de test insérées avec succès !' as message;