package Controller;

import DAO.DatabaseConnection;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.sql.*;

@WebServlet({"/login", "/authenticate", "/logout"})
public class LoginServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        if ("/logout".equals(path)) {
            handleLogout(request, response);
        } else {
            showLoginPage(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        if ("/authenticate".equals(path)) {
            handleAuthentication(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    /**
     * Affiche la page de connexion
     */
    private void showLoginPage(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Si déjà connecté, rediriger vers le dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/login.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * Gère l'authentification de l'utilisateur
     */
    private void handleAuthentication(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Validation basique
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Veuillez remplir tous les champs");
            showLoginPage(request, response);
            return;
        }
        
        try {
            // Vérifier les identifiants dans la base de données
            Integer userId = authenticateUser(username, password);
            
            if (userId != null) {
                // Authentification réussie
                HttpSession session = request.getSession(true);
                session.setAttribute("userId", userId);
                session.setAttribute("username", username);
                session.setMaxInactiveInterval(30 * 60); // 30 minutes
                
                System.out.println("✓ Connexion réussie pour l'utilisateur : " + username);
                
                // Redirection vers le dashboard
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                // Échec de l'authentification
                request.setAttribute("errorMessage", "Identifiants incorrects");
                request.setAttribute("username", username);
                showLoginPage(request, response);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'authentification : " + e.getMessage());
            request.setAttribute("errorMessage", "Erreur de connexion au serveur");
            showLoginPage(request, response);
        }
    }
    
    /**
     * Vérifie les identifiants de l'utilisateur
     * Note : Dans une vraie application, le mot de passe devrait être hashé (BCrypt)
     */
    private Integer authenticateUser(String username, String password) throws SQLException {
        String sql = "SELECT user_id FROM users WHERE username = ? AND password_hash = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            // Pour simplification, on compare directement (à sécuriser en production)
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        }
        
        return null;
    }
    
    /**
     * Gère la déconnexion de l'utilisateur
     */
    private void handleLogout(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            String username = (String) session.getAttribute("username");
            session.invalidate();
            System.out.println("✓ Déconnexion de l'utilisateur : " + username);
        }
        
        response.sendRedirect(request.getContextPath() + "/login");
    }
}