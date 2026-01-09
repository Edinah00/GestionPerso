<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion - Gestion Emploi du Temps</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        
        .login-container {
            max-width: 450px;
            width: 100%;
            padding: 0 1rem;
        }
        
        .login-card {
            background: white;
            border-radius: 20px;
            padding: 3rem;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            animation: slideUp 0.5s ease-out;
        }
        
        @keyframes slideUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
        
        .login-header {
            text-align: center;
            margin-bottom: 2rem;
        }
        
        .login-icon {
            font-size: 4rem;
            color: #6366f1;
            margin-bottom: 1rem;
            animation: pulse 2s infinite;
        }
        
        @keyframes pulse {
            0%, 100% { transform: scale(1); }
            50% { transform: scale(1.1); }
        }
        
        .login-title {
            font-size: 1.8rem;
            font-weight: bold;
            color: #1f2937;
            margin-bottom: 0.5rem;
        }
        
        .login-subtitle {
            color: #6b7280;
            font-size: 0.9rem;
        }
        
        .form-floating {
            margin-bottom: 1.5rem;
        }
        
        .form-control {
            border-radius: 12px;
            border: 2px solid #e5e7eb;
            padding: 1rem;
            transition: all 0.3s ease;
        }
        
        .form-control:focus {
            border-color: #6366f1;
            box-shadow: 0 0 0 0.25rem rgba(99, 102, 241, 0.1);
        }
        
        .btn-login {
            width: 100%;
            padding: 1rem;
            border-radius: 12px;
            border: none;
            background: linear-gradient(135deg, #6366f1, #8b5cf6);
            color: white;
            font-weight: 600;
            font-size: 1rem;
            margin-top: 1rem;
            transition: all 0.3s ease;
        }
        
        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 25px rgba(99, 102, 241, 0.3);
        }
        
        .btn-login:active {
            transform: translateY(0);
        }
        
        .demo-credentials {
            background: #f3f4f6;
            border-radius: 12px;
            padding: 1rem;
            margin-top: 2rem;
            font-size: 0.85rem;
        }
        
        .demo-credentials strong {
            color: #6366f1;
        }
        
        .alert {
            border-radius: 12px;
            animation: shake 0.5s;
        }
        
        @keyframes shake {
            0%, 100% { transform: translateX(0); }
            25% { transform: translateX(-10px); }
            75% { transform: translateX(10px); }
        }
        
        .footer-text {
            text-align: center;
            color: white;
            margin-top: 2rem;
            font-size: 0.9rem;
        }
        
        .footer-text a {
            color: white;
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="login-card">
            <!-- En-tête -->
            <div class="login-header">
                <div class="login-icon">
                    <i class="bi bi-calendar-check-fill"></i>
                </div>
                <h1 class="login-title">Bienvenue</h1>
                <p class="login-subtitle">Connectez-vous à votre espace personnel</p>
            </div>

            <!-- Affichage des erreurs -->
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="bi bi-exclamation-triangle-fill"></i>
                    ${errorMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Formulaire de connexion -->
            <form action="${pageContext.request.contextPath}/authenticate" method="post">
                <div class="form-floating">
                    <input type="text" 
                           class="form-control" 
                           id="username" 
                           name="username" 
                           placeholder="Nom d'utilisateur"
                           value="${username}"
                           required
                           autofocus>
                    <label for="username">
                        <i class="bi bi-person-fill"></i> Nom d'utilisateur
                    </label>
                </div>

                <div class="form-floating">
                    <input type="password" 
                           class="form-control" 
                           id="password" 
                           name="password" 
                           placeholder="Mot de passe"
                           required>
                    <label for="password">
                        <i class="bi bi-lock-fill"></i> Mot de passe
                    </label>
                </div>

                <div class="form-check mb-3">
                    <input class="form-check-input" 
                           type="checkbox" 
                           id="rememberMe"
                           name="rememberMe">
                    <label class="form-check-label" for="rememberMe">
                        Se souvenir de moi
                    </label>
                </div>

                <button type="submit" class="btn btn-login">
                    <i class="bi bi-box-arrow-in-right"></i>
                    Se connecter
                </button>
            </form>

            <!-- Informations de démo -->
            <div class="demo-credentials">
                <div class="text-center mb-2">
                    <i class="bi bi-info-circle-fill text-primary"></i>
                    <strong>Compte de démonstration</strong>
                </div>
                <div>
                    <i class="bi bi-person"></i> Username: <code>jean_dupont</code><br>
                    <i class="bi bi-key"></i> Password: <code>password123</code>
                </div>
            </div>
        </div>

        <!-- Footer -->
        <div class="footer-text">
            <p class="mb-1">
                <i class="bi bi-shield-check"></i>
                Connexion sécurisée
            </p>
            <p class="mb-0">
                © 2025 ITUniversity - Système de Gestion d'Emploi du Temps
            </p>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Animation au chargement
        document.addEventListener('DOMContentLoaded', function() {
            const card = document.querySelector('.login-card');
            card.style.opacity = '0';
            setTimeout(() => {
                card.style.transition = 'opacity 0.5s ease-out';
                card.style.opacity = '1';
            }, 100);
        });

        // Validation côté client
        document.querySelector('form').addEventListener('submit', function(e) {
            const username = document.getElementById('username').value.trim();
            const password = document.getElementById('password').value.trim();

            if (!username || !password) {
                e.preventDefault();
                alert('Veuillez remplir tous les champs');
                return false;
            }

            // Afficher un indicateur de chargement
            const btn = document.querySelector('.btn-login');
            btn.innerHTML = '<i class="bi bi-hourglass-split"></i> Connexion...';
            btn.disabled = true;
        });

        // Effet de particules en arrière-plan (optionnel)
        function createParticles() {
            const body = document.body;
            for (let i = 0; i < 20; i++) {
                const particle = document.createElement('div');
                particle.style.position = 'fixed';
                particle.style.width = Math.random() * 5 + 2 + 'px';
                particle.style.height = particle.style.width;
                particle.style.background = 'rgba(255, 255, 255, 0.3)';
                particle.style.borderRadius = '50%';
                particle.style.left = Math.random() * 100 + '%';
                particle.style.top = Math.random() * 100 + '%';
                particle.style.animation = `float ${Math.random() * 10 + 5}s infinite`;
                body.appendChild(particle);
            }
        }

        const style = document.createElement('style');
        style.textContent = `
            @keyframes float {
                0%, 100% { transform: translateY(0) translateX(0); }
                50% { transform: translateY(-20px) translateX(10px); }
            }
        `;
        document.head.appendChild(style);
        createParticles();
    </script>
</body>
</html>