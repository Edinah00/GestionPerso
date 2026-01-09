package Controller;

import Model.*;
import DAO.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@WebServlet({"/dashboard", "/", ""})
public class DashboardServlet extends HttpServlet {
    
    private TaskDAO taskDAO;
    private TripDAO tripDAO;
    private MealDAO mealDAO;
    private AlarmDAO alarmDAO;
    private WellnessDAO wellnessDAO;
    
    @Override
    public void init() throws ServletException {
        taskDAO = new TaskDAO();
        tripDAO = new TripDAO();
        mealDAO = new MealDAO();
        alarmDAO = new AlarmDAO();
        wellnessDAO = new WellnessDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        // Redirection si non connecté
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Récupérer la date sélectionnée (aujourd'hui par défaut)
        String dateParam = request.getParameter("date");
        LocalDate selectedDate = (dateParam != null) ? 
            LocalDate.parse(dateParam) : LocalDate.now();
        
        // Récupérer toutes les données pour le dashboard
        Map<String, Object> dashboardData = getDashboardData(userId, selectedDate);
        
        // Calculer et sauvegarder le score de bien-être
        wellnessDAO.calculateAndSaveTodayScore(userId);
        
        // Passer les données à la JSP
        request.setAttribute("dashboardData", dashboardData);
        request.setAttribute("selectedDate", selectedDate);
        request.setAttribute("currentTime", LocalTime.now());
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * Récupère toutes les données nécessaires pour le dashboard
     */
    private Map<String, Object> getDashboardData(int userId, LocalDate date) {
        Map<String, Object> data = new HashMap<>();
        
        // Récupérer les tâches
        List<Task> tasks = taskDAO.getTasksByUserAndDate(userId, date);
        data.put("tasks", tasks);
        data.put("totalTasks", tasks.size());
        data.put("completedTasks", tasks.stream()
            .filter(t -> t.getStatus() == TaskStatus.TERMINEE)
            .count());
        data.put("pendingTasks", tasks.stream()
            .filter(t -> t.getStatus() == TaskStatus.EN_ATTENTE)
            .count());
        data.put("urgentTasks", tasks.stream()
            .filter(t -> t.isUrgent())
            .count());
        
        // Récupérer les trajets
        List<Trip> trips = tripDAO.getTripsByUserAndDate(userId, date);
        data.put("trips", trips);
        data.put("totalTrips", trips.size());
        
        // Récupérer les alarmes actives
        List<Alarm> alarms = alarmDAO.getAlarmsByUserAndDate(userId, date);
        data.put("alarms", alarms);
        data.put("activeAlarms", alarms.stream()
            .filter(Alarm::isActive)
            .count());
        
        // Récupérer le score de bien-être
        WellnessScore wellnessScore = wellnessDAO.getWellnessScoreByDate(userId, date);
        if (wellnessScore == null) {
            wellnessScore = new WellnessScore(userId, date);
        }
        data.put("wellnessScore", wellnessScore);
        
        // Calculer les statistiques de temps
        int totalScheduledMinutes = tasks.stream()
            .mapToInt(Task::getDurationMinutes)
            .sum();
        totalScheduledMinutes += trips.stream()
            .mapToInt(Trip::getDurationMinutes)
            .sum();
        
        data.put("totalScheduledMinutes", totalScheduledMinutes);
        data.put("totalScheduledHours", totalScheduledMinutes / 60.0);
        
        // Calculer le temps libre restant (19h - temps planifié)
        int availableMinutes = 19 * 60; // De 4h à 23h
        int freeMinutes = availableMinutes - totalScheduledMinutes;
        data.put("freeMinutes", freeMinutes);
        data.put("freeHours", freeMinutes / 60.0);
        
        // Alerte de surcharge
        data.put("isOverloaded", totalScheduledMinutes > (availableMinutes * 0.9));
        
        // Calculer la progression de la journée
        LocalTime now = LocalTime.now();
        LocalTime startTime = LocalTime.of(4, 0);
        LocalTime endTime = LocalTime.of(23, 0);
        
        if (now.isAfter(startTime) && now.isBefore(endTime)) {
            int minutesSinceStart = (int) java.time.Duration.between(startTime, now).toMinutes();
            int totalMinutes = (int) java.time.Duration.between(startTime, endTime).toMinutes();
            int progressPercentage = (minutesSinceStart * 100) / totalMinutes;
            data.put("dayProgress", progressPercentage);
        } else {
            data.put("dayProgress", 0);
        }
        
        // Prochaine alarme
        Alarm nextAlarm = alarms.stream()
            .filter(a -> a.isActive() && a.getAlarmTime().isAfter(LocalTime.now()))
            .findFirst()
            .orElse(null);
        data.put("nextAlarm", nextAlarm);
        
        return data;
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("refresh".equals(action)) {
            doGet(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}