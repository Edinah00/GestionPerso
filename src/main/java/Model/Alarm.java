package Model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * Classe Alarm - Représente une alarme/notification
 * Utilisée pour rappeler les tâches, trajets, repas, repos
 */
public class Alarm {
    private int alarmId;
    private int userId;
    private LocalTime alarmTime;
    private LocalDate alarmDate;
    private String title;
    private String description;
    private AlarmType alarmType;
    private boolean isActive;
    private boolean isRecurring;
    private String recurrencePattern; // "DAILY", "WEEKLY", "WORKDAYS"
    private boolean notificationSent;
    private LocalDateTime createdAt;
    
    // Constructeur vide
    public Alarm() {
        this.isActive = true;
        this.isRecurring = false;
        this.notificationSent = false;
        this.createdAt = LocalDateTime.now();
    }
    
    // Constructeur principal
    public Alarm(int userId, LocalTime alarmTime, LocalDate alarmDate, 
                 String title, AlarmType alarmType) {
        this();
        this.userId = userId;
        this.alarmTime = alarmTime;
        this.alarmDate = alarmDate;
        this.title = title;
        this.alarmType = alarmType;
    }
    
    /**
     * Vérifie si l'alarme doit sonner maintenant
     * @return true si l'heure actuelle correspond à l'alarme
     */
    public boolean shouldTrigger() {
        if (!isActive) return false;
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime alarmDateTime = LocalDateTime.of(alarmDate, alarmTime);
        
        // Déclenche si on est dans les 5 minutes de l'alarme
        return now.isAfter(alarmDateTime.minusMinutes(1)) && 
               now.isBefore(alarmDateTime.plusMinutes(5)) &&
               !notificationSent;
    }
    
    /**
     * Active l'alarme
     */
    public void activate() {
        this.isActive = true;
    }
    
    /**
     * Désactive l'alarme
     */
    public void deactivate() {
        this.isActive = false;
    }
    
    /**
     * Marque la notification comme envoyée
     */
    public void markAsSent() {
        this.notificationSent = true;
    }
    
    /**
     * Réinitialise la notification pour permettre un nouvel envoi
     */
    public void resetNotification() {
        this.notificationSent = false;
    }
    
    // Getters et Setters
    public int getAlarmId() { return alarmId; }
    public void setAlarmId(int alarmId) { this.alarmId = alarmId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public LocalTime getAlarmTime() { return alarmTime; }
    public void setAlarmTime(LocalTime alarmTime) { this.alarmTime = alarmTime; }
    
    public LocalDate getAlarmDate() { return alarmDate; }
    public void setAlarmDate(LocalDate alarmDate) { this.alarmDate = alarmDate; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public AlarmType getAlarmType() { return alarmType; }
    public void setAlarmType(AlarmType alarmType) { this.alarmType = alarmType; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public boolean isRecurring() { return isRecurring; }
    public void setRecurring(boolean recurring) { isRecurring = recurring; }
    
    public String getRecurrencePattern() { return recurrencePattern; }
    public void setRecurrencePattern(String recurrencePattern) { 
        this.recurrencePattern = recurrencePattern; 
    }
    
    public boolean isNotificationSent() { return notificationSent; }
    public void setNotificationSent(boolean notificationSent) { 
        this.notificationSent = notificationSent; 
    }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return "Alarm{id=" + alarmId + ", title='" + title + "', time=" + 
               alarmTime + ", date=" + alarmDate + ", active=" + isActive + "}";
    }
}