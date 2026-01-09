package DAO;

import Model.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AlarmDAO {
    
    public boolean createAlarm(Alarm alarm) {
        String sql = "INSERT INTO alarms (user_id, alarm_time, alarm_date, title, " +
                    "description, alarm_type, is_active, is_recurring, recurrence_pattern) " +
                    "VALUES (?, ?, ?, ?, ?, ?::alarm_type_enum, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, alarm.getUserId());
            pstmt.setTime(2, Time.valueOf(alarm.getAlarmTime()));
            pstmt.setDate(3, Date.valueOf(alarm.getAlarmDate()));
            pstmt.setString(4, alarm.getTitle());
            pstmt.setString(5, alarm.getDescription());
            pstmt.setString(6, alarm.getAlarmType().name());
            pstmt.setBoolean(7, alarm.isActive());
            pstmt.setBoolean(8, alarm.isRecurring());
            pstmt.setString(9, alarm.getRecurrencePattern());
            
            int affected = pstmt.executeUpdate();
            
            if (affected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    alarm.setAlarmId(rs.getInt(1));
                }
                System.out.println("✓ Alarme créée : " + alarm.getTitle());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur création alarme : " + e.getMessage());
        }
        return false;
    }
    
    public List<Alarm> getActiveAlarmsByUser(int userId) {
        List<Alarm> alarms = new ArrayList<>();
        String sql = "SELECT * FROM alarms WHERE user_id = ? AND is_active = TRUE " +
                    "ORDER BY alarm_date, alarm_time";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Alarm alarm = extractAlarmFromResultSet(rs);
                alarms.add(alarm);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération alarmes : " + e.getMessage());
        }
        return alarms;
    }
    
    public List<Alarm> getAlarmsByUserAndDate(int userId, LocalDate date) {
        List<Alarm> alarms = new ArrayList<>();
        String sql = "SELECT * FROM alarms WHERE user_id = ? AND alarm_date = ? " +
                    "ORDER BY alarm_time";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setDate(2, Date.valueOf(date));
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Alarm alarm = extractAlarmFromResultSet(rs);
                alarms.add(alarm);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération alarmes par date : " + e.getMessage());
        }
        return alarms;
    }
    
    public List<Alarm> getPendingAlarms(int userId, LocalDate date, LocalTime currentTime) {
        List<Alarm> alarms = new ArrayList<>();
        String sql = "SELECT * FROM alarms WHERE user_id = ? AND alarm_date = ? " +
                    "AND alarm_time <= ? AND is_active = TRUE AND notification_sent = FALSE " +
                    "ORDER BY alarm_time";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setDate(2, Date.valueOf(date));
            pstmt.setTime(3, Time.valueOf(currentTime));
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Alarm alarm = extractAlarmFromResultSet(rs);
                alarms.add(alarm);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération alarmes en attente : " + e.getMessage());
        }
        return alarms;
    }
    
    public boolean markAlarmAsSent(int alarmId) {
        String sql = "UPDATE alarms SET notification_sent = TRUE WHERE alarm_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, alarmId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur marquage alarme envoyée : " + e.getMessage());
        }
        return false;
    }
    
    public boolean toggleAlarm(int alarmId, boolean isActive) {
        String sql = "UPDATE alarms SET is_active = ? WHERE alarm_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setBoolean(1, isActive);
            pstmt.setInt(2, alarmId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur basculement alarme : " + e.getMessage());
        }
        return false;
    }
    
    public boolean deleteAlarm(int alarmId) {
        String sql = "DELETE FROM alarms WHERE alarm_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, alarmId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur suppression alarme : " + e.getMessage());
        }
        return false;
    }
    
    private Alarm extractAlarmFromResultSet(ResultSet rs) throws SQLException {
        Alarm alarm = new Alarm();
        alarm.setAlarmId(rs.getInt("alarm_id"));
        alarm.setUserId(rs.getInt("user_id"));
        alarm.setAlarmTime(rs.getTime("alarm_time").toLocalTime());
        alarm.setAlarmDate(rs.getDate("alarm_date").toLocalDate());
        alarm.setTitle(rs.getString("title"));
        alarm.setDescription(rs.getString("description"));
        alarm.setAlarmType(AlarmType.valueOf(rs.getString("alarm_type")));
        alarm.setActive(rs.getBoolean("is_active"));
        alarm.setRecurring(rs.getBoolean("is_recurring"));
        alarm.setRecurrencePattern(rs.getString("recurrence_pattern"));
        alarm.setNotificationSent(rs.getBoolean("notification_sent"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            alarm.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return alarm;
    }
}