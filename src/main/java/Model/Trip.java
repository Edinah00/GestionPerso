package Model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

// ============================================
// Classe Trip (Trajet)
// ============================================
public class Trip {
    private int tripId;
    private int userId;
    private String departureLocation;
    private String arrivalLocation;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private int durationMinutes;
    private TripType tripType;
    private String transportMode;
    private LocalDate tripDate;
    private LocalDateTime createdAt;
    
    // Constructeur vide
    public Trip() {
        this.createdAt = LocalDateTime.now();
    }
    
    // Constructeur principal
    public Trip(int userId, String departureLocation, String arrivalLocation, 
                LocalTime departureTime, LocalTime arrivalTime, TripType tripType, LocalDate tripDate) {
        this();
        this.userId = userId;
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.tripType = tripType;
        this.tripDate = tripDate;
        calculateDuration();
    }
    
    // Calcul automatique de la durée
    public void calculateDuration() {
        if (departureTime != null && arrivalTime != null) {
            int hours = arrivalTime.getHour() - departureTime.getHour();
            int minutes = arrivalTime.getMinute() - departureTime.getMinute();
            this.durationMinutes = hours * 60 + minutes;
        }
    }
    
    // Vérifier si le trajet est imminent (dans les 30 prochaines minutes)
    public boolean isImminent() {
        LocalTime now = LocalTime.now();
        LocalTime thirtyMinutesFromNow = now.plusMinutes(30);
        return departureTime.isAfter(now) && departureTime.isBefore(thirtyMinutesFromNow);
    }
    
    // Getters et Setters
    public int getTripId() { return tripId; }
    public void setTripId(int tripId) { this.tripId = tripId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getDepartureLocation() { return departureLocation; }
    public void setDepartureLocation(String departureLocation) { this.departureLocation = departureLocation; }
    
    public String getArrivalLocation() { return arrivalLocation; }
    public void setArrivalLocation(String arrivalLocation) { this.arrivalLocation = arrivalLocation; }
    
    public LocalTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalTime departureTime) { 
        this.departureTime = departureTime;
        calculateDuration();
    }
    
    public LocalTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalTime arrivalTime) { 
        this.arrivalTime = arrivalTime;
        calculateDuration();
    }
    
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    
    public TripType getTripType() { return tripType; }
    public void setTripType(TripType tripType) { this.tripType = tripType; }
    
    public String getTransportMode() { return transportMode; }
    public void setTransportMode(String transportMode) { this.transportMode = transportMode; }
    
    public LocalDate getTripDate() { return tripDate; }
    public void setTripDate(LocalDate tripDate) { this.tripDate = tripDate; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return "Trip{" + departureLocation + " -> " + arrivalLocation + 
               " at " + departureTime + ", duration=" + durationMinutes + "min}";
    }
}
