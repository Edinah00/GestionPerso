package DAO;

import Model.*;
import Model.TripType;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TripDAO {
    
    public boolean createTrip(Trip trip) {
        String sql = "INSERT INTO trips (user_id, departure_location, arrival_location, " +
                    "departure_time, arrival_time, duration_minutes, trip_type, " +
                    "transport_mode, trip_date) VALUES (?, ?, ?, ?, ?, ?, ?::trip_type_enum, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, trip.getUserId());
            pstmt.setString(2, trip.getDepartureLocation());
            pstmt.setString(3, trip.getArrivalLocation());
            pstmt.setTime(4, Time.valueOf(trip.getDepartureTime()));
            pstmt.setTime(5, Time.valueOf(trip.getArrivalTime()));
            pstmt.setInt(6, trip.getDurationMinutes());
            pstmt.setString(7, trip.getTripType().name());
            pstmt.setString(8, trip.getTransportMode());
            pstmt.setDate(9, Date.valueOf(trip.getTripDate()));
            
            int affected = pstmt.executeUpdate();
            
            if (affected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    trip.setTripId(rs.getInt(1));
                }
                System.out.println("✓ Trajet créé : " + trip.getDepartureLocation() + 
                                 " → " + trip.getArrivalLocation());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur création trajet : " + e.getMessage());
        }
        return false;
    }
    
    public List<Trip> getTripsByUserAndDate(int userId, LocalDate date) {
        List<Trip> trips = new ArrayList<>();
        String sql = "SELECT * FROM trips WHERE user_id = ? AND trip_date = ? " +
                    "ORDER BY departure_time";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setDate(2, Date.valueOf(date));
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Trip trip = extractTripFromResultSet(rs);
                trips.add(trip);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération trajets : " + e.getMessage());
        }
        return trips;
    }
    
    public Trip getTripById(int tripId) {
        String sql = "SELECT * FROM trips WHERE trip_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, tripId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractTripFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération trajet : " + e.getMessage());
        }
        return null;
    }
    
    public boolean updateTrip(Trip trip) {
        String sql = "UPDATE trips SET departure_location = ?, arrival_location = ?, " +
                    "departure_time = ?, arrival_time = ?, duration_minutes = ?, " +
                    "trip_type = ?::trip_type_enum, transport_mode = ?, trip_date = ? " +
                    "WHERE trip_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, trip.getDepartureLocation());
            pstmt.setString(2, trip.getArrivalLocation());
            pstmt.setTime(3, Time.valueOf(trip.getDepartureTime()));
            pstmt.setTime(4, Time.valueOf(trip.getArrivalTime()));
            pstmt.setInt(5, trip.getDurationMinutes());
            pstmt.setString(6, trip.getTripType().name());
            pstmt.setString(7, trip.getTransportMode());
            pstmt.setDate(8, Date.valueOf(trip.getTripDate()));
            pstmt.setInt(9, trip.getTripId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour trajet : " + e.getMessage());
        }
        return false;
    }
    
    public boolean deleteTrip(int tripId) {
        String sql = "DELETE FROM trips WHERE trip_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, tripId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur suppression trajet : " + e.getMessage());
        }
        return false;
    }
    
    private Trip extractTripFromResultSet(ResultSet rs) throws SQLException {
        Trip trip = new Trip();
        trip.setTripId(rs.getInt("trip_id"));
        trip.setUserId(rs.getInt("user_id"));
        trip.setDepartureLocation(rs.getString("departure_location"));
        trip.setArrivalLocation(rs.getString("arrival_location"));
        trip.setDepartureTime(rs.getTime("departure_time").toLocalTime());
        trip.setArrivalTime(rs.getTime("arrival_time").toLocalTime());
        trip.setDurationMinutes(rs.getInt("duration_minutes"));
        trip.setTripType(TripType.valueOf(rs.getString("trip_type")));
        trip.setTransportMode(rs.getString("transport_mode"));
        trip.setTripDate(rs.getDate("trip_date").toLocalDate());
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            trip.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return trip;
    }
}