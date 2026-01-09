package DAO;

import Model.WellnessScore;
import java.sql.*;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class WellnessDAO {
    
    /**
     * Crée ou met à jour le score de bien-être pour une date
     */
    public boolean saveWellnessScore(WellnessScore score) {
        String sql = "INSERT INTO wellness_scores (user_id, score_date, total_score, " +
                    "tasks_completed, tasks_total, sleep_hours, exercise_minutes, " +
                    "stress_level, productivity_score, social_time_minutes, notes) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON CONFLICT (user_id, score_date) DO UPDATE SET " +
                    "total_score = EXCLUDED.total_score, " +
                    "tasks_completed = EXCLUDED.tasks_completed, " +
                    "tasks_total = EXCLUDED.tasks_total, " +
                    "sleep_hours = EXCLUDED.sleep_hours, " +
                    "exercise_minutes = EXCLUDED.exercise_minutes, " +
                    "stress_level = EXCLUDED.stress_level, " +
                    "productivity_score = EXCLUDED.productivity_score, " +
                    "social_time_minutes = EXCLUDED.social_time_minutes, " +
                    "notes = EXCLUDED.notes";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, score.getUserId());
            pstmt.setDate(2, Date.valueOf(score.getScoreDate()));
            pstmt.setBigDecimal(3, score.getTotalScore());
            pstmt.setInt(4, score.getTasksCompleted());
            pstmt.setInt(5, score.getTasksTotal());
            pstmt.setBigDecimal(6, score.getSleepHours());
            pstmt.setInt(7, score.getExerciseMinutes());
            pstmt.setInt(8, score.getStressLevel());
            pstmt.setBigDecimal(9, score.getProductivityScore());
            pstmt.setInt(10, score.getSocialTimeMinutes());
            pstmt.setString(11, score.getNotes());
            
            int affected = pstmt.executeUpdate();
            
            if (affected > 0) {
                System.out.println("✓ Score bien-être sauvegardé : " + 
                                 score.getTotalScore() + "/100");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur sauvegarde score bien-être : " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Récupère le score de bien-être pour une date donnée
     */
    public WellnessScore getWellnessScoreByDate(int userId, LocalDate date) {
        String sql = "SELECT * FROM wellness_scores WHERE user_id = ? AND score_date = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setDate(2, Date.valueOf(date));
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractWellnessScoreFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération score bien-être : " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Récupère l'historique des scores sur une période
     */
    public List<WellnessScore> getWellnessScoreHistory(int userId, int days) {
        List<WellnessScore> scores = new ArrayList<>();
        String sql = "SELECT * FROM wellness_scores WHERE user_id = ? " +
                    "AND score_date >= CURRENT_DATE - INTERVAL '" + days + " days' " +
                    "ORDER BY score_date DESC";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                WellnessScore score = extractWellnessScoreFromResultSet(rs);
                scores.add(score);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération historique : " + e.getMessage());
        }
        return scores;
    }
    
    /**
     * Calcule la moyenne du score sur une période
     */
    public BigDecimal getAverageScore(int userId, int days) {
        String sql = "SELECT AVG(total_score) as avg_score FROM wellness_scores " +
                    "WHERE user_id = ? AND score_date >= CURRENT_DATE - INTERVAL '" + 
                    days + " days'";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getBigDecimal("avg_score");
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur calcul moyenne : " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * Calcule et sauvegarde automatiquement le score pour aujourd'hui
     */
    public boolean calculateAndSaveTodayScore(int userId) {
        LocalDate today = LocalDate.now();
        
        // Récupérer les données nécessaires
        TaskDAO taskDAO = new TaskDAO();
        int completedTasks = taskDAO.countCompletedTasks(userId, today);
        int totalTasks = taskDAO.countTotalTasks(userId, today);
        
        // Récupérer heures de sommeil et temps social
        BigDecimal sleepHours = getSleepHours(userId, today);
        int socialMinutes = getSocialMinutes(userId, today);
        
        // Créer et calculer le score
        WellnessScore score = new WellnessScore(userId, today);
        score.setTasksCompleted(completedTasks);
        score.setTasksTotal(totalTasks);
        score.setSleepHours(sleepHours);
        score.setSocialTimeMinutes(socialMinutes);
        score.calculateTotalScore();
        
        return saveWellnessScore(score);
    }
    
    /**
     * Récupère les heures de sommeil pour une date
     */
    private BigDecimal getSleepHours(int userId, LocalDate date) {
        String sql = "SELECT SUM(duration_minutes) / 60.0 as sleep_hours " +
                    "FROM rest_periods WHERE user_id = ? AND rest_date = ? " +
                    "AND rest_type = 'SOMMEIL'::rest_type_enum";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setDate(2, Date.valueOf(date));
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("sleep_hours");
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération heures sommeil : " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * Récupère les minutes de temps social pour une date
     */
    private int getSocialMinutes(int userId, LocalDate date) {
        String sql = "SELECT SUM(duration_minutes) as social_minutes " +
                    "FROM rest_periods WHERE user_id = ? AND rest_date = ? " +
                    "AND rest_type = 'RELATIONNEL'::rest_type_enum";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setDate(2, Date.valueOf(date));
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("social_minutes");
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération temps social : " + e.getMessage());
        }
        return 0;
    }
    
    private WellnessScore extractWellnessScoreFromResultSet(ResultSet rs) throws SQLException {
        WellnessScore score = new WellnessScore();
        score.setWellnessId(rs.getInt("wellness_id"));
        score.setUserId(rs.getInt("user_id"));
        score.setScoreDate(rs.getDate("score_date").toLocalDate());
        score.setTotalScore(rs.getBigDecimal("total_score"));
        score.setTasksCompleted(rs.getInt("tasks_completed"));
        score.setTasksTotal(rs.getInt("tasks_total"));
        score.setSleepHours(rs.getBigDecimal("sleep_hours"));
        score.setExerciseMinutes(rs.getInt("exercise_minutes"));
        score.setStressLevel(rs.getInt("stress_level"));
        score.setProductivityScore(rs.getBigDecimal("productivity_score"));
        score.setSocialTimeMinutes(rs.getInt("social_time_minutes"));
        score.setNotes(rs.getString("notes"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            score.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return score;
    }
}