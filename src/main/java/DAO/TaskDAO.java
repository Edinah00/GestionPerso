package DAO;

import Model.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * TaskDAO - Gestion des opérations CRUD pour les tâches
 */
public class TaskDAO {
    
    /**
     * Crée une nouvelle tâche dans la base de données
     */
    public boolean createTask(Task task) {
        String sql = "INSERT INTO tasks (user_id, title, description, priority, " +
                    "duration_minutes, status, scheduled_date, scheduled_time, category) " +
                    "VALUES (?, ?, ?, ?::priority_enum, ?, ?::status_enum, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, task.getUserId());
            pstmt.setString(2, task.getTitle());
            pstmt.setString(3, task.getDescription());
            pstmt.setString(4, task.getPriority().name());
            pstmt.setInt(5, task.getDurationMinutes());
            pstmt.setString(6, task.getStatus().name());
            pstmt.setDate(7, Date.valueOf(task.getScheduledDate()));
            pstmt.setTime(8, task.getScheduledTime() != null ? 
                              Time.valueOf(task.getScheduledTime()) : null);      
            pstmt.setString(9, task.getCategory());
            
            int affected = pstmt.executeUpdate();
            
            if (affected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    task.setTaskId(rs.getInt(1));
                }
                System.out.println("✓ Tâche créée : " + task.getTitle());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur création tâche : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Récupère toutes les tâches d'un utilisateur pour une date donnée
     */
    public List<Task> getTasksByUserAndDate(int userId, LocalDate date) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE user_id = ? AND scheduled_date = ? " +
                    "ORDER BY scheduled_time, priority DESC";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setDate(2, Date.valueOf(date));
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Task task = extractTaskFromResultSet(rs);
                tasks.add(task);
            }
            
            System.out.println("✓ " + tasks.size() + " tâche(s) trouvée(s) pour le " + date);
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération tâches : " + e.getMessage());
        }
        return tasks;
    }
    
    /**
     * Récupère une tâche par son ID
     */
    public Task getTaskById(int taskId) {
        String sql = "SELECT * FROM tasks WHERE task_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, taskId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractTaskFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération tâche : " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Met à jour une tâche existante
     */
    public boolean updateTask(Task task) {
        String sql = "UPDATE tasks SET title = ?, description = ?, priority = ?::priority_enum, " +
                    "duration_minutes = ?, status = ?::status_enum, scheduled_date = ?, " +
                    "scheduled_time = ?, category = ? WHERE task_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getPriority().name());
            pstmt.setInt(4, task.getDurationMinutes());
            pstmt.setString(5, task.getStatus().name());
            pstmt.setDate(6, Date.valueOf(task.getScheduledDate()));
            pstmt.setTime(7, task.getScheduledTime() != null ? 
                              Time.valueOf(task.getScheduledTime()) : null);
            pstmt.setString(8, task.getCategory());
            pstmt.setInt(9, task.getTaskId());
            
            int affected = pstmt.executeUpdate();
            
            if (affected > 0) {
                System.out.println("✓ Tâche mise à jour : " + task.getTitle());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour tâche : " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Marque une tâche comme terminée
     */
    public boolean markTaskAsCompleted(int taskId) {
        String sql = "UPDATE tasks SET status = 'TERMINEE'::status_enum, " +
                    "completed_at = CURRENT_TIMESTAMP WHERE task_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, taskId);
            int affected = pstmt.executeUpdate();
            
            if (affected > 0) {
                System.out.println("✓ Tâche " + taskId + " marquée comme terminée");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur complétion tâche : " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Supprime une tâche
     */
    public boolean deleteTask(int taskId) {
        String sql = "DELETE FROM tasks WHERE task_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, taskId);
            int affected = pstmt.executeUpdate();
            
            if (affected > 0) {
                System.out.println("✓ Tâche " + taskId + " supprimée");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur suppression tâche : " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Compte les tâches terminées pour une date donnée
     */
    public int countCompletedTasks(int userId, LocalDate date) {
        String sql = "SELECT COUNT(*) FROM tasks WHERE user_id = ? AND " +
                    "scheduled_date = ? AND status = 'TERMINEE'::status_enum";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setDate(2, Date.valueOf(date));
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur comptage tâches : " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Compte le total de tâches pour une date donnée
     */
    public int countTotalTasks(int userId, LocalDate date) {
        String sql = "SELECT COUNT(*) FROM tasks WHERE user_id = ? AND scheduled_date = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setDate(2, Date.valueOf(date));
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur comptage total tâches : " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Extrait un objet Task depuis un ResultSet
     */
    private Task extractTaskFromResultSet(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setTaskId(rs.getInt("task_id"));
        task.setUserId(rs.getInt("user_id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setPriority(Priority.valueOf(rs.getString("priority")));
        task.setDurationMinutes(rs.getInt("duration_minutes"));
        task.setStatus(TaskStatus.valueOf(rs.getString("status")));
        task.setScheduledDate(rs.getDate("scheduled_date").toLocalDate());
        
        Time scheduledTime = rs.getTime("scheduled_time");
        if (scheduledTime != null) {
            task.setScheduledTime(scheduledTime.toLocalTime());
        }
        
        task.setCategory(rs.getString("category"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            task.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp completedAt = rs.getTimestamp("completed_at");
        if (completedAt != null) {
            task.setCompletedAt(completedAt.toLocalDateTime());
        }
        
        return task;
    }
}