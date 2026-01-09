package Model;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

// ============================================
// Classe Task (Tâche)
// ============================================
public class Task {
    private int taskId;
    private int userId;
    private String title;
    private String description;
    private Priority priority;
    private int durationMinutes;
    private TaskStatus status;
    private LocalDate scheduledDate;
    private LocalTime scheduledTime;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    
    // Constructeur vide
    public Task() {
        this.status = TaskStatus.EN_ATTENTE;
        this.createdAt = LocalDateTime.now();
    }
    
    // Constructeur principal
    public Task(int userId, String title, Priority priority, int durationMinutes, LocalDate scheduledDate) {
        this();
        this.userId = userId;
        this.title = title;
        this.priority = priority;
        this.durationMinutes = durationMinutes;
        this.scheduledDate = scheduledDate;
    }
    
    // Méthode pour marquer comme terminée
    public void markAsCompleted() {
        this.status = TaskStatus.TERMINEE;
        this.completedAt = LocalDateTime.now();
    }
    
    // Méthode pour démarrer la tâche
    public void startTask() {
        this.status = TaskStatus.EN_COURS;
    }
    
    // Méthode pour vérifier si la tâche est urgente (moins de 2h avant l'heure prévue)
    public boolean isUrgent() {
        if (scheduledTime == null) return false;
        LocalTime now = LocalTime.now();
        LocalTime twoHoursBefore = scheduledTime.minusHours(2);
        return now.isAfter(twoHoursBefore) && now.isBefore(scheduledTime);
    }
    
    // Getters et Setters
    public int getTaskId() { return taskId; }
    public void setTaskId(int taskId) { this.taskId = taskId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
    
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    
    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
    
    public LocalDate getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDate scheduledDate) { this.scheduledDate = scheduledDate; }
    
    public LocalTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalTime scheduledTime) { this.scheduledTime = scheduledTime; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    
    @Override
    public String toString() {
        return "Task{taskId=" + taskId + ", title='" + title + "', priority=" + priority + 
               ", status=" + status + ", date=" + scheduledDate + "}";
    }
}
