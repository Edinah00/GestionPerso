package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * Classe WellnessScore - Calcule et stocke le score de bien-être quotidien
 * Score basé sur : tâches complétées, sommeil, temps social, exercice
 */
public class WellnessScore {
    private int wellnessId;
    private int userId;
    private LocalDate scoreDate;
    private BigDecimal totalScore;      // Score total sur 100
    private int tasksCompleted;
    private int tasksTotal;
    private BigDecimal sleepHours;
    private int exerciseMinutes;
    private int stressLevel;            // 1-10
    private BigDecimal productivityScore;
    private int socialTimeMinutes;
    private String notes;
    private LocalDateTime createdAt;
    
    // Constructeur vide
    public WellnessScore() {
        this.scoreDate = LocalDate.now();
        this.totalScore = BigDecimal.ZERO;
        this.tasksCompleted = 0;
        this.tasksTotal = 0;
        this.exerciseMinutes = 0;
        this.socialTimeMinutes = 0;
        this.createdAt = LocalDateTime.now();
    }
    
    // Constructeur avec user et date
    public WellnessScore(int userId, LocalDate scoreDate) {
        this();
        this.userId = userId;
        this.scoreDate = scoreDate;
    }
    
    /**
     * Calcule le score total de bien-être
     * - 40 points : Complétion des tâches
     * - 30 points : Qualité du sommeil
     * - 30 points : Temps social/relationnel
     */
    public void calculateTotalScore() {
        BigDecimal taskScore = calculateTaskScore();
        BigDecimal sleepScore = calculateSleepScore();
        BigDecimal socialScore = calculateSocialScore();
        
        this.totalScore = taskScore.add(sleepScore).add(socialScore);
    }
    
    /**
     * Score basé sur la complétion des tâches (0-40 points)
     */
    private BigDecimal calculateTaskScore() {
        if (tasksTotal == 0) {
            return BigDecimal.valueOf(20); // Score neutre si pas de tâches
        }
        
        double completionRate = (double) tasksCompleted / tasksTotal;
        return BigDecimal.valueOf(completionRate * 40);
    }
    
    /**
     * Score basé sur les heures de sommeil (0-30 points)
     * 7-8h = 30 points
     * 6-7h = 20 points
     * <6h = 10 points
     */
    private BigDecimal calculateSleepScore() {
        if (sleepHours == null) {
            return BigDecimal.valueOf(10);
        }
        
        double hours = sleepHours.doubleValue();
        
        if (hours >= 7 && hours <= 8) {
            return BigDecimal.valueOf(30);
        } else if (hours >= 6) {
            return BigDecimal.valueOf(20);
        } else {
            return BigDecimal.valueOf(10);
        }
    }
    
    /**
     * Score basé sur le temps social (0-30 points)
     * ≥60 min = 30 points
     * 30-60 min = 20 points
     * <30 min = 10 points
     */
    private BigDecimal calculateSocialScore() {
        if (socialTimeMinutes >= 60) {
            return BigDecimal.valueOf(30);
        } else if (socialTimeMinutes >= 30) {
            return BigDecimal.valueOf(20);
        } else {
            return BigDecimal.valueOf(10);
        }
    }
    
    /**
     * Retourne une évaluation textuelle du score
     */
    public String getScoreEvaluation() {
        double score = totalScore.doubleValue();
        
        if (score >= 80) return "Excellent";
        if (score >= 60) return "Bon";
        if (score >= 40) return "Moyen";
        return "À améliorer";
    }
    
    // Getters et Setters
    public int getWellnessId() { return wellnessId; }
    public void setWellnessId(int wellnessId) { this.wellnessId = wellnessId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public LocalDate getScoreDate() { return scoreDate; }
    public void setScoreDate(LocalDate scoreDate) { this.scoreDate = scoreDate; }
    
    public BigDecimal getTotalScore() { return totalScore; }
    public void setTotalScore(BigDecimal totalScore) { this.totalScore = totalScore; }
    
    public int getTasksCompleted() { return tasksCompleted; }
    public void setTasksCompleted(int tasksCompleted) { 
        this.tasksCompleted = tasksCompleted; 
    }
    
    public int getTasksTotal() { return tasksTotal; }
    public void setTasksTotal(int tasksTotal) { this.tasksTotal = tasksTotal; }
    
    public BigDecimal getSleepHours() { return sleepHours; }
    public void setSleepHours(BigDecimal sleepHours) { this.sleepHours = sleepHours; }
    
    public int getExerciseMinutes() { return exerciseMinutes; }
    public void setExerciseMinutes(int exerciseMinutes) { 
        this.exerciseMinutes = exerciseMinutes; 
    }
    
    public int getStressLevel() { return stressLevel; }
    public void setStressLevel(int stressLevel) { this.stressLevel = stressLevel; }
    
    public BigDecimal getProductivityScore() { return productivityScore; }
    public void setProductivityScore(BigDecimal productivityScore) { 
        this.productivityScore = productivityScore; 
    }
    
    public int getSocialTimeMinutes() { return socialTimeMinutes; }
    public void setSocialTimeMinutes(int socialTimeMinutes) { 
        this.socialTimeMinutes = socialTimeMinutes; 
    }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return "WellnessScore{date=" + scoreDate + ", score=" + totalScore + 
               "/100, evaluation=" + getScoreEvaluation() + "}";
    }
}