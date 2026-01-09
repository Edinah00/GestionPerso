package Model;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

// ============================================
// Classe RestPeriod (Période de repos)
// ============================================
public class RestPeriod {
    private int restId;
    private int userId;
    private LocalTime startTime;
    private LocalTime endTime;
    private int durationMinutes;
    private RestType restType;
    private LocalDate restDate;
    private int qualityScore;
    private String notes;
    private LocalDateTime createdAt;
    
    // Constructeur vide
    public RestPeriod() {
        this.createdAt = LocalDateTime.now();
    }
    
    // Constructeur principal
    public RestPeriod(int userId, LocalTime startTime, LocalTime endTime, 
                      RestType restType, LocalDate restDate) {
        this();
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.restType = restType;
        this.restDate = restDate;
        calculateDuration();
    }
    
    // Calcul automatique de la durée
    public void calculateDuration() {
        if (startTime != null && endTime != null) {
            int hours = endTime.getHour() - startTime.getHour();
            int minutes = endTime.getMinute() - startTime.getMinute();
            this.durationMinutes = hours * 60 + minutes;
            
            // Gérer le cas où endTime est le lendemain
            if (this.durationMinutes < 0) {
                this.durationMinutes += 24 * 60;
            }
        }
    }
    
    // Getters et Setters (similaire aux autres classes)
    public int getRestId() { return restId; }
    public void setRestId(int restId) { this.restId = restId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { 
        this.startTime = startTime;
        calculateDuration();
    }
    
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { 
        this.endTime = endTime;
        calculateDuration();
    }
    
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    
    public RestType getRestType() { return restType; }
    public void setRestType(RestType restType) { this.restType = restType; }
    
    public LocalDate getRestDate() { return restDate; }
    public void setRestDate(LocalDate restDate) { this.restDate = restDate; }
    
    public int getQualityScore() { return qualityScore; }
    public void setQualityScore(int qualityScore) { this.qualityScore = qualityScore; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}