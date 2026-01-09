<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nouvelle Tâche - Gestion Emploi du Temps</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 2rem 0;
        }
        
        .form-container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            border-radius: 20px;
            padding: 2.5rem;
            box-shadow: 0 20px 60px rgba(0,0,0,0.2);
        }
        
        .form-header {
            text-align: center;
            margin-bottom: 2rem;
        }
        
        .form-header i {
            font-size: 3rem;
            color: #6366f1;
            margin-bottom: 1rem;
        }
        
        .form-label {
            font-weight: 600;
            color: #374151;
            margin-bottom: 0.5rem;
        }
        
        .form-control, .form-select {
            border-radius: 10px;
            border: 2px solid #e5e7eb;
            padding: 0.75rem 1rem;
            transition: all 0.3s ease;
        }
        
        .form-control:focus, .form-select:focus {
            border-color: #6366f1;
            box-shadow: 0 0 0 0.25rem rgba(99, 102, 241, 0.1);
        }
        
        .btn-submit {
            background: linear-gradient(135deg, #6366f1, #8b5cf6);
            color: white;
            border: none;
            padding: 1rem 2rem;
            border-radius: 12px;
            font-weight: 600;
            width: 100%;
            margin-top: 1.5rem;
            transition: all 0.3s ease;
        }
        
        .btn-submit:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(99, 102, 241, 0.3);
        }
        
        .btn-cancel {
            background: #6b7280;
            color: white;
            border: none;
            padding: 1rem 2rem;
            border-radius: 12px;
            font-weight: 600;
            width: 100%;
            margin-top: 1rem;
        }
        
        .priority-badge {
            display: inline-block;
            padding: 0.5rem 1rem;
            border-radius: 8px;
            margin: 0.25rem;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .priority-badge.haute {
            background: #fee2e2;
            color: #991b1b;
            border: 2px solid transparent;
        }
        
        .priority-badge.moyenne {
            background: #fef3c7;
            color: #92400e;
            border: 2px solid transparent;
        }
        
        .priority-badge.basse {
            background: #dbeafe;
            color: #1e40af;
            border: 2px solid transparent;
        }
        
        .priority-badge.selected {
            border-color: currentColor;
            font-weight: bold;
        }
        
        .duration-preset {
            display: inline-block;
            padding: 0.5rem 1rem;
            background: #f3f4f6;
            border-radius: 8px;
            margin: 0.25rem;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .duration-preset:hover {
            background: #e5e7eb;
            transform: scale(1.05);
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="form-container">
            <!-- En-tête -->
            <div class="form-header">
                <i class="bi bi-plus-circle-fill"></i>
                <h2 class="mb-0">Nouvelle Tâche</h2>
                <p class="text-muted">Planifiez votre prochaine activité</p>
            </div>

            <!-- Affichage des erreurs -->
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="bi bi-exclamation-triangle-fill"></i>
                    ${errorMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Formulaire -->
            <form action="${pageContext.request.contextPath}/tasks" method="post" id="taskForm">
                <input type="hidden" name="action" value="create">
                
                <!-- Titre -->
                <div class="mb-3">
                    <label for="title" class="form-label">
                        <i class="bi bi-card-heading"></i> Titre de la tâche *
                    </label>
                    <input type="text" 
                           class="form-control" 
                           id="title" 
                           name="title" 
                           required
                           placeholder="Ex: Réviser le cours de Java"
                           maxlength="200">
                </div>

                <!-- Description -->
                <div class="mb-3">
                    <label for="description" class="form-label">
                        <i class="bi bi-text-paragraph"></i> Description
                    </label>
                    <textarea class="form-control" 
                              id="description" 
                              name="description" 
                              rows="4"
                              placeholder="Détails supplémentaires..."></textarea>
                </div>

                <!-- Priorité -->
                <div class="mb-3">
                    <label class="form-label">
                        <i class="bi bi-exclamation-circle"></i> Priorité *
                    </label>
                    <input type="hidden" id="priority" name="priority" value="MOYENNE" required>
                    <div>
                        <span class="priority-badge haute" onclick="selectPriority('HAUTE')">
                            🔴 Haute
                        </span>
                        <span class="priority-badge moyenne selected" onclick="selectPriority('MOYENNE')">
                            🟡 Moyenne
                        </span>
                        <span class="priority-badge basse" onclick="selectPriority('BASSE')">
                            🟢 Basse
                        </span>
                    </div>
                </div>

                <!-- Durée -->
                <div class="mb-3">
                    <label for="duration" class="form-label">
                        <i class="bi bi-clock"></i> Durée (en minutes) *
                    </label>
                    <input type="number" 
                           class="form-control" 
                           id="duration" 
                           name="duration" 
                           required
                           min="5"
                           max="480"
                           value="60">
                    <div class="mt-2">
                        <small class="text-muted">Préréglages :</small><br>
                        <span class="duration-preset" onclick="setDuration(30)">30 min</span>
                        <span class="duration-preset" onclick="setDuration(60)">1h</span>
                        <span class="duration-preset" onclick="setDuration(90)">1h30</span>
                        <span class="duration-preset" onclick="setDuration(120)">2h</span>
                        <span class="duration-preset" onclick="setDuration(180)">3h</span>
                    </div>
                </div>

                <!-- Date prévue -->
                <div class="mb-3">
                    <label for="scheduledDate" class="form-label">
                        <i class="bi bi-calendar-event"></i> Date prévue *
                    </label>
                    <input type="date" 
                           class="form-control" 
                           id="scheduledDate" 
                           name="scheduledDate" 
                           required>
                </div>

                <!-- Heure prévue -->
                <div class="mb-3">
                    <label for="scheduledTime" class="form-label">
                        <i class="bi bi-alarm"></i> Heure prévue (optionnel)
                    </label>
                    <input type="time" 
                           class="form-control" 
                           id="scheduledTime" 
                           name="scheduledTime">
                    <small class="text-muted">
                        Laisser vide si l'heure n'est pas encore déterminée
                    </small>
                </div>

                <!-- Catégorie -->
                <div class="mb-3">
                    <label for="category" class="form-label">
                        <i class="bi bi-tag"></i> Catégorie
                    </label>
                    <select class="form-select" id="category" name="category">
                        <option value="">-- Sélectionner --</option>
                        <option value="Études">📚 Études</option>
                        <option value="Programmation">💻 Programmation</option>
                        <option value="Maison">🏠 Maison</option>
                        <option value="Personnel">👤 Personnel</option>
                        <option value="Sport">⚽ Sport</option>
                        <option value="Autre">📌 Autre</option>
                    </select>
                </div>

                <!-- Boutons d'action -->
                <button type="submit" class="btn btn-submit">
                    <i class="bi bi-check-circle"></i> Créer la tâche
                </button>
                
                <button type="button" 
                        class="btn btn-cancel" 
                        onclick="window.location.href='${pageContext.request.contextPath}/dashboard'">
                    <i class="bi bi-x-circle"></i> Annuler
                </button>
            </form>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Définir la date d'aujourd'hui par défaut
        document.addEventListener('DOMContentLoaded', function() {
            const today = new Date().toISOString().split('T')[0];
            document.getElementById('scheduledDate').value = today;
        });
        
        // Sélection de la priorité
        function selectPriority(priority) {
            // Retirer la classe selected de tous les badges
            document.querySelectorAll('.priority-badge').forEach(badge => {
                badge.classList.remove('selected');
            });
            
            // Ajouter la classe selected au badge cliqué
            event.target.classList.add('selected');
            
            // Mettre à jour le champ caché
            document.getElementById('priority').value = priority;
        }
        
        // Définir une durée préréglée
        function setDuration(minutes) {
            document.getElementById('duration').value = minutes;
        }
        
        // Validation du formulaire
        document.getElementById('taskForm').addEventListener('submit', function(e) {
            const duration = parseInt(document.getElementById('duration').value);
            
            if (duration < 5) {
                e.preventDefault();
                alert('La durée doit être d\'au moins 5 minutes');
                return false;
            }
            
            if (duration > 480) {
                e.preventDefault();
                alert('La durée ne peut pas dépasser 8 heures (480 minutes)');
                return false;
            }
            
            return true;
        });
    </script>
</body>
</html>