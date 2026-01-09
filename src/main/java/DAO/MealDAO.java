package DAO;

import Model.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MealDAO {
    
    public boolean createMeal(Meal meal) {
        String sql = "INSERT INTO meals (user_id, meal_type, preparation_minutes, " +
                    "scheduled_time, meal_date, notes, completed) " +
                    "VALUES (?, ?::meal_type_enum, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, meal.getUserId());
            pstmt.setString(2, meal.getMealType().name());
            pstmt.setInt(3, meal.getPreparationMinutes());
            pstmt.setTime(4, Time.valueOf(meal.getScheduledTime()));
            pstmt.setDate(5, Date.valueOf(meal.getMealDate()));
            pstmt.setString(6, meal.getNotes());
            pstmt.setBoolean(7, meal.isCompleted());
            
            int affected = pstmt.executeUpdate();
            
            if (affected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    meal.setMealId(rs.getInt(1));
                }
                System.out.println("✓ Repas créé : " + meal.getMealType());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur création repas : " + e.getMessage());
        }
        return false;
    }
    
    public List<Meal> getMealsByUserAndDate(int userId, LocalDate date) {
        List<Meal> meals = new ArrayList<>();
        String sql = "SELECT * FROM meals WHERE user_id = ? AND meal_date = ? " +
                    "ORDER BY scheduled_time";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setDate(2, Date.valueOf(date));
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Meal meal = extractMealFromResultSet(rs);
                meals.add(meal);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération repas : " + e.getMessage());
        }
        return meals;
    }
    
    public boolean updateMeal(Meal meal) {
        String sql = "UPDATE meals SET meal_type = ?::meal_type_enum, " +
                    "preparation_minutes = ?, scheduled_time = ?, meal_date = ?, " +
                    "notes = ?, completed = ? WHERE meal_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, meal.getMealType().name());
            pstmt.setInt(2, meal.getPreparationMinutes());
            pstmt.setTime(3, Time.valueOf(meal.getScheduledTime()));
            pstmt.setDate(4, Date.valueOf(meal.getMealDate()));
            pstmt.setString(5, meal.getNotes());
            pstmt.setBoolean(6, meal.isCompleted());
            pstmt.setInt(7, meal.getMealId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour repas : " + e.getMessage());
        }
        return false;
    }
    
    public boolean deleteMeal(int mealId) {
        String sql = "DELETE FROM meals WHERE meal_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, mealId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur suppression repas : " + e.getMessage());
        }
        return false;
    }
    
    private Meal extractMealFromResultSet(ResultSet rs) throws SQLException {
        Meal meal = new Meal();
        meal.setMealId(rs.getInt("meal_id"));
        meal.setUserId(rs.getInt("user_id"));
        meal.setMealType(MealType.valueOf(rs.getString("meal_type")));
        meal.setPreparationMinutes(rs.getInt("preparation_minutes"));
        meal.setScheduledTime(rs.getTime("scheduled_time").toLocalTime());
        meal.setMealDate(rs.getDate("meal_date").toLocalDate());
        meal.setNotes(rs.getString("notes"));
        meal.setCompleted(rs.getBoolean("completed"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            meal.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return meal;
    }
}