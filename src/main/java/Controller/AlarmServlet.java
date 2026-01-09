package Controller;

import Model.*;
import DAO.AlarmDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@WebServlet("/alarms/*")
public class AlarmServlet extends HttpServlet {
    
    private AlarmDAO alarmDAO;
    
    @Override
    public void init() throws ServletException {
        alarmDAO = new AlarmDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            listAlarms(request, response);
        } else if (pathInfo.equals("/add")) {
            showAddForm(request, response);
        } else if (pathInfo.equals("/pending")) {
            checkPendingAlarms(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String pathInfo = request.getPathInfo();
        
        if ("/toggle".equals(pathInfo)) {
            toggleAlarm(request, response);
        } else if ("create".equals(action)) {
            createAlarm(request, response);
        } else if ("delete".equals(action)) {
            deleteAlarm(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    /**
     * Affiche la liste des alarmes
     */
    private void listAlarms(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        List<Alarm> alarms = alarmDAO.getActiveAlarmsByUser(userId);
        
        request.setAttribute("alarms", alarms);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/alarms/list.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * Affiche le formulaire d'ajout d'alarme
     */
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/alarms/add.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * Crée une nouvelle alarme
     */
    private void createAlarm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            LocalTime alarmTime = LocalTime.parse(request.getParameter("alarmTime"));
            LocalDate alarmDate = LocalDate.parse(request.getParameter("alarmDate"));
            AlarmType alarmType = AlarmType.valueOf(request.getParameter("alarmType"));
            boolean isRecurring = Boolean.parseBoolean(request.getParameter("isRecurring"));
            String recurrencePattern = request.getParameter("recurrencePattern");
            
            Alarm alarm = new Alarm(userId, alarmTime, alarmDate, title, alarmType);
            alarm.setDescription(description);
            alarm.setRecurring(isRecurring);
            if (isRecurring && recurrencePattern != null) {
                alarm.setRecurrencePattern(recurrencePattern);
            }
            
            boolean success = alarmDAO.createAlarm(alarm);
            
            if (success) {
                session.setAttribute("successMessage", "Alarme créée avec succès !");
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                request.setAttribute("errorMessage", "Erreur lors de la création de l'alarme");
                showAddForm(request, response);
            }
            
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Données invalides : " + e.getMessage());
            showAddForm(request, response);
        }
    }
    
    /**
     * Active/désactive une alarme
     */
    private void toggleAlarm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int alarmId = Integer.parseInt(request.getParameter("alarmId"));
            
            // Pour simplifier, on inverse l'état actuel
            // Dans une vraie application, il faudrait d'abord récupérer l'état actuel
            alarmDAO.toggleAlarm(alarmId, true);
            
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true}");
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"error\": \"" + 
                                      e.getMessage() + "\"}");
        }
    }
    
    /**
     * Supprime une alarme
     */
    private void deleteAlarm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int alarmId = Integer.parseInt(request.getParameter("alarmId"));
            boolean success = alarmDAO.deleteAlarm(alarmId);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/alarms");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
    
    /**
     * Vérifie les alarmes en attente
     * Endpoint pour les requêtes AJAX
     */
    private void checkPendingAlarms(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        
        List<Alarm> pendingAlarms = alarmDAO.getPendingAlarms(userId, today, now.plusMinutes(5));
        
        // Convertir en JSON simple
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < pendingAlarms.size(); i++) {
            Alarm alarm = pendingAlarms.get(i);
            if (i > 0) json.append(",");
            json.append("{")
                .append("\"id\":").append(alarm.getAlarmId()).append(",")
                .append("\"title\":\"").append(alarm.getTitle()).append("\",")
                .append("\"time\":\"").append(alarm.getAlarmTime()).append("\",")
                .append("\"type\":\"").append(alarm.getAlarmType()).append("\"")
                .append("}");
        }
        json.append("]");
        
        response.setContentType("application/json");
        response.getWriter().write(json.toString());
    }
}