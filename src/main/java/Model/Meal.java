package Model;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

// ============================================
// Classe Meal (Repas)
// ============================================
public class Meal {
    private int mealId;
    private int userId;
    private MealType mealType;
    private int preparationMinutes;
    private LocalTime scheduledTime;
    private LocalDate mealDate;
    private String notes;
    private boolean completed;
    private LocalDateTime createdAt;
    
    // Constructeur vide
    public Meal() {
        this.preparationMinutes = 30;
        this.completed = false;
        this.createdAt = LocalDateTime.now();
    }
    
    // Constructeur principal
    public Meal(int userId, MealType mealType, LocalTime scheduledTime, LocalDate mealDate) {
        this();
        this.userId = userId;
        this.mealType = mealType;
        this.scheduledTime = scheduledTime;
        this.mealDate = mealDate;
    }
    
    // Marquer le repas comme terminé
    public void markAsCompleted() {
        this.completed = true;
    }
    
    // Getters et Setters
    public int getMealId() { return mealId; }
    public void setMealId(int mealId) { this.mealId = mealId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public MealType getMealType() { return mealType; }
    public void setMealType(MealType mealType) { this.mealType = mealType; }
    
    public int getPreparationMinutes() { return preparationMinutes; }
    public void setPreparationMinutes(int preparationMinutes) { this.preparationMinutes = preparationMinutes; }
    
    public LocalTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalTime scheduledTime) { this.scheduledTime = scheduledTime; }
    
    public LocalDate getMealDate() { return mealDate; }
    public void setMealDate(LocalDate mealDate) { this.mealDate = mealDate; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}