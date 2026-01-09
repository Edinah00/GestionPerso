package Filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Filtre d'authentification - Protège les pages nécessitant une connexion
 */
public class AuthenticationFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialisation si nécessaire
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        String loginURI = httpRequest.getContextPath() + "/login";
        String requestURI = httpRequest.getRequestURI();
        
        // Vérifier si l'utilisateur est connecté
        boolean isLoggedIn = (session != null && session.getAttribute("userId") != null);
        
        // Vérifier si c'est une requête vers la page de login
        boolean isLoginRequest = requestURI.equals(loginURI);
        
        // Vérifier si c'est une ressource statique
        boolean isStaticResource = requestURI.contains("/css/") || 
                                   requestURI.contains("/js/") || 
                                   requestURI.contains("/images/");
        
        if (isLoggedIn || isLoginRequest || isStaticResource) {
            // L'utilisateur est connecté ou demande la page de login
            chain.doFilter(request, response);
        } else {
            // Rediriger vers la page de login
            httpResponse.sendRedirect(loginURI);
        }
    }
    
    @Override
    public void destroy() {
        // Nettoyage si nécessaire
    }
}