package Controller;

import Model.*;
import DAO.TaskDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@WebServlet("/tasks/*")
public class TaskServlet extends HttpServlet {
    
    private TaskDAO taskDAO;
    
    @Override
    public void init() throws ServletException {
        taskDAO = new TaskDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            listTasks(request, response);
        } else if (pathInfo.equals("/add")) {
            showAddForm(request, response);
        } else if (pathInfo.equals("/edit")) {
            showEditForm(request, response);
        } else if (pathInfo.matches("/\\d+")) {
            viewTask(request, response, pathInfo);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        switch (action) {
            case "create":
                createTask(request, response);
                break;
            case "update":
                updateTask(request, response);
                break;
            case "complete":
                completeTask(request, response);
                break;
            case "delete":
                deleteTask(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    /**
     * Affiche la liste des tâches pour aujourd'hui ou une date spécifique
     */
    private void listTasks(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Récupérer la date (aujourd'hui par défaut)
        String dateParam = request.getParameter("date");
        LocalDate date = (dateParam != null) ? LocalDate.parse(dateParam) : LocalDate.now();
        
        // Récupérer les tâches
        List<Task> tasks = taskDAO.getTasksByUserAndDate(userId, date);
        
        // Calculer les statistiques
        int totalTasks = tasks.size();
        long completedTasks = tasks.stream()
            .filter(t -> t.getStatus() == TaskStatus.TERMINEE)
            .count();
        long pendingTasks = tasks.stream()
            .filter(t -> t.getStatus() == TaskStatus.EN_ATTENTE)
            .count();
        
        // Passer les données à la JSP
        request.setAttribute("tasks", tasks);
        request.setAttribute("selectedDate", date);
        request.setAttribute("totalTasks", totalTasks);
        request.setAttribute("completedTasks", completedTasks);
        request.setAttribute("pendingTasks", pendingTasks);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/tasks/list.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * Affiche le formulaire d'ajout de tâche
     */
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/tasks/add.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * Affiche le formulaire d'édition de tâche
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        int taskId = Integer.parseInt(request.getParameter("id"));
        Task task = taskDAO.getTaskById(taskId);
        
        if (task == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        request.setAttribute("task", task);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/tasks/edit.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * Affiche les détails d'une tâche
     */
    private void viewTask(HttpServletRequest request, HttpServletResponse response, String pathInfo) 
            throws ServletException, IOException {
        
        int taskId = Integer.parseInt(pathInfo.substring(1));
        Task task = taskDAO.getTaskById(taskId);
        
        if (task == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        request.setAttribute("task", task);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/tasks/view.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * Crée une nouvelle tâche
     */
    private void createTask(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            // Récupérer les paramètres du formulaire
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            Priority priority = Priority.valueOf(request.getParameter("priority"));
            int duration = Integer.parseInt(request.getParameter("duration"));
            LocalDate scheduledDate = LocalDate.parse(request.getParameter("scheduledDate"));
            String scheduledTimeStr = request.getParameter("scheduledTime");
            LocalTime scheduledTime = (scheduledTimeStr != null && !scheduledTimeStr.isEmpty()) 
                ? LocalTime.parse(scheduledTimeStr) : null;
            String category = request.getParameter("category");
            
            // Créer la tâche
            Task task = new Task(userId, title, priority, duration, scheduledDate);
            task.setDescription(description);
            task.setScheduledTime(scheduledTime);
            task.setCategory(category);
            
            boolean success = taskDAO.createTask(task);
            
            if (success) {
                session.setAttribute("successMessage", "Tâche créée avec succès !");
                response.sendRedirect(request.getContextPath() + "/tasks");
            } else {
                request.setAttribute("errorMessage", "Erreur lors de la création de la tâche");
                showAddForm(request, response);
            }
            
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Données invalides : " + e.getMessage());
            showAddForm(request, response);
        }
    }
    
    /**
     * Met à jour une tâche existante
     */
    private void updateTask(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int taskId = Integer.parseInt(request.getParameter("taskId"));
            Task task = taskDAO.getTaskById(taskId);
            
            if (task == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            
            // Mettre à jour les champs
            task.setTitle(request.getParameter("title"));
            task.setDescription(request.getParameter("description"));
            task.setPriority(Priority.valueOf(request.getParameter("priority")));
            task.setDurationMinutes(Integer.parseInt(request.getParameter("duration")));
            task.setScheduledDate(LocalDate.parse(request.getParameter("scheduledDate")));
            
            String scheduledTimeStr = request.getParameter("scheduledTime");
            if (scheduledTimeStr != null && !scheduledTimeStr.isEmpty()) {
                task.setScheduledTime(LocalTime.parse(scheduledTimeStr));
            }
            
            task.setCategory(request.getParameter("category"));
            task.setStatus(TaskStatus.valueOf(request.getParameter("status")));
            
            boolean success = taskDAO.updateTask(task);
            
            if (success) {
                HttpSession session = request.getSession();
                session.setAttribute("successMessage", "Tâche mise à jour avec succès !");
                response.sendRedirect(request.getContextPath() + "/tasks");
            } else {
                request.setAttribute("errorMessage", "Erreur lors de la mise à jour");
                request.setAttribute("task", task);
                showEditForm(request, response);
            }
            
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Données invalides : " + e.getMessage());
            showEditForm(request, response);
        }
    }
    
    /**
     * Marque une tâche comme terminée
     */
    private void completeTask(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int taskId = Integer.parseInt(request.getParameter("taskId"));
            boolean success = taskDAO.markTaskAsCompleted(taskId);
            
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": " + success + "}");
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"error\": \"" + 
                                      e.getMessage() + "\"}");
        }
    }
    
    /**
     * Supprime une tâche
     */
    private void deleteTask(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int taskId = Integer.parseInt(request.getParameter("taskId"));
            boolean success = taskDAO.deleteTask(taskId);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/tasks");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
}