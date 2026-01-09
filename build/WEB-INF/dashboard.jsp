<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Gestion Emploi du Temps</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        :root {
            --primary-color: #6366f1;
            --success-color: #10b981;
            --warning-color: #f59e0b;
            --danger-color: #ef4444;
            --dark-bg: #1f2937;
            --card-bg: #ffffff;
        }
        
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        
        .dashboard-container {
            padding: 2rem;
            max-width: 1400px;
            margin: 0 auto;
        }
        .header-card {
            background: white;
            border-radius: 20px;
            padding: 2rem;
            margin-bottom: 2rem;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        }
        
        .stat-card {
            background: white;
            border-radius: 15px;
            padding: 1.5rem;
            margin-bottom: 1.5rem;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
            transition: transform 0.3s ease;
        }
        
        .stat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
        }
        
        .stat-icon {
            font-size: 2.5rem;
            margin-bottom: 0.5rem;
        }
        
        .stat-value {
            font-size: 2rem;
            font-weight: bold;
            margin: 0.5rem 0;
        }
        
        .stat-label {
            color: #6b7280;
            font-size: 0.9rem;
        }
        
        .task-item {
            background: #f9fafb;
            border-left: 4px solid var(--primary-color);
            padding: 1rem;
            margin-bottom: 0.8rem;
            border-radius: 8px;
            transition: all 0.3s ease;
        }
        
        .task-item:hover {
            background: #f3f4f6;
            transform: translateX(5px);
        }
        
        .task-item.completed {
            border-left-color: var(--success-color);
            opacity: 0.7;
        }
        
        .task-item.urgent {
            border-left-color: var(--danger-color);
            background: #fef2f2;
        }
        
        .badge-priority {
            padding: 0.3rem 0.8rem;
            border-radius: 20px;
            font-size: 0.75rem;
            font-weight: 600;
        }
        
        .badge-priority.high {
            background: #fee2e2;
            color: #991b1b;
        }
        
        .badge-priority.medium {
            background: #fef3c7;
            color: #92400e;
        }
        
        .badge-priority.low {
            background: #dbeafe;
            color: #1e40af;
        }
        
        .wellness-score {
            text-align: center;
            padding: 2rem;
        }
        
        /* ✅ CORRECTION : CSS simplifié, JavaScript gérera le style dynamique */
        .wellness-circle {
            width: 150px;
            height: 150px;
            border-radius: 50%;
            background: #e5e7eb;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto;
            position: relative;
        }
        
        .wellness-inner {
            width: 120px;
            height: 120px;
            border-radius: 50%;
            background: white;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            z-index: 1;
        }
        
        .wellness-value {
            font-size: 2.5rem;
            font-weight: bold;
            color: var(--primary-color);
        }
        
        .time-block {
            background: linear-gradient(135deg, var(--primary-color), #8b5cf6);
            color: white;
            padding: 1.5rem;
            border-radius: 15px;
            margin-bottom: 1.5rem;
        }
        
        .alarm-item {
            background: white;
            border: 2px solid #e5e7eb;
            border-radius: 10px;
            padding: 1rem;
            margin-bottom: 0.8rem;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
        
        .alarm-item.active {
            border-color: var(--primary-color);
            background: #eff6ff;
        }
        
        .trip-timeline {
            position: relative;
            padding-left: 2rem;
        }
        
        .trip-timeline::before {
            content: '';
            position: absolute;
            left: 0.5rem;
            top: 0;
            bottom: 0;
            width: 2px;
            background: var(--primary-color);
        }
        
        .trip-point {
            position: relative;
            margin-bottom: 2rem;
        }
        
        .trip-point::before {
            content: '';
            position: absolute;
            left: -1.65rem;
            top: 0.3rem;
            width: 12px;
            height: 12px;
            border-radius: 50%;
            background: var(--primary-color);
            border: 3px solid white;
            box-shadow: 0 0 0 2px var(--primary-color);
        }
        
        .quick-action-btn {
            width: 100%;
            padding: 1rem;
            margin-bottom: 1rem;
            border-radius: 12px;
            border: none;
            background: linear-gradient(135deg, var(--primary-color), #8b5cf6);
            color: white;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        
        .quick-action-btn:hover {
            transform: scale(1.05);
            box-shadow: 0 8px 20px rgba(99, 102, 241, 0.3);
        }
    </style>
</head>
<body>
    <div class="dashboard-container">
        <!-- En-tête -->
        <div class="header-card">
            <div class="row align-items-center">
                <div class="col-md-6">
                    <h1 class="mb-0">
                        <i class="bi bi-house-heart-fill text-primary"></i>
                        Tableau de Bord
                    </h1>
                    <p class="text-muted mb-0">
                        <fmt:formatDate value="${selectedDate}" pattern="EEEE d MMMM yyyy" />
                    </p>
                </div>
                <div class="col-md-6 text-end">
                    <div class="btn-group" role="group">
                        <button class="btn btn-outline-primary" onclick="changeDate(-1)">
                            <i class="bi bi-chevron-left"></i> Hier
                        </button>
                        <button class="btn btn-primary" onclick="changeDate(0)">
                            Aujourd'hui
                        </button>
                        <button class="btn btn-outline-primary" onclick="changeDate(1)">
                            Demain <i class="bi bi-chevron-right"></i>
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Statistiques principales -->
        <div class="row">
            <div class="col-md-3">
                <div class="stat-card text-center">
                    <i class="bi bi-check-circle-fill stat-icon text-success"></i>
                    <div class="stat-value text-success">
                        ${dashboardData.completedTasks}/${dashboardData.totalTasks}
                    </div>
                    <div class="stat-label">Tâches complétées</div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card text-center">
                    <i class="bi bi-geo-alt-fill stat-icon text-primary"></i>
                    <div class="stat-value text-primary">
                        ${dashboardData.totalTrips}
                    </div>
                    <div class="stat-label">Trajets planifiés</div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card text-center">
                    <i class="bi bi-alarm-fill stat-icon text-warning"></i>
                    <div class="stat-value text-warning">
                        ${dashboardData.activeAlarms}
                    </div>
                    <div class="stat-label">Alarmes actives</div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card text-center">
                    <i class="bi bi-clock-fill stat-icon text-info"></i>
                    <div class="stat-value text-info">
                        <fmt:formatNumber value="${dashboardData.freeHours}" maxFractionDigits="1"/>h
                    </div>
                    <div class="stat-label">Temps libre</div>
                </div>
            </div>
        </div>

        <!-- Alerte de surcharge -->
        <c:if test="${dashboardData.isOverloaded}">
            <div class="alert alert-warning alert-dismissible fade show" role="alert">
                <i class="bi bi-exclamation-triangle-fill"></i>
                <strong>Attention !</strong> Votre emploi du temps est surchargé pour aujourd'hui.
                Envisagez de reporter certaines tâches.
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <div class="row">
            <!-- Colonne gauche : Tâches et Trajets -->
            <div class="col-md-8">
                <!-- Score de bien-être -->
                <div class="stat-card">
                    <h4 class="mb-3">
                        <i class="bi bi-heart-pulse-fill text-danger"></i>
                        Score de Bien-être
                    </h4>
                    <div class="wellness-score">
                        <!-- ✅ CORRECTION : ID ajouté pour JavaScript -->
                        <div class="wellness-circle" id="wellnessCircle">
                            <div class="wellness-inner">
                                <div class="wellness-value" id="wellnessValue">
                                    <!-- ✅ CORRECTION : Vérification si le score existe -->
                                    <c:choose>
                                        <c:when test="${not empty dashboardData.wellnessScore}">
                                            <fmt:formatNumber value="${dashboardData.wellnessScore.totalScore}" 
                                                            maxFractionDigits="0"/>
                                        </c:when>
                                        <c:otherwise>
                                            --
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <small>/100</small>
                            </div>
                        </div>
                        <p class="mt-3 mb-0">
                            <c:if test="${not empty dashboardData.wellnessScore}">
                                <span class="badge bg-info">
                                    ${dashboardData.wellnessScore.scoreEvaluation}
                                </span>
                            </c:if>
                        </p>
                    </div>
                </div>

                <!-- Liste des tâches -->
                <div class="stat-card">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h4 class="mb-0">
                            <i class="bi bi-list-task"></i>
                            Tâches du jour
                        </h4>
                        <a href="${pageContext.request.contextPath}/tasks/add" 
                           class="btn btn-sm btn-primary">
                            <i class="bi bi-plus-circle"></i> Ajouter
                        </a>
                    </div>
                    
                    <c:choose>
                        <c:when test="${empty dashboardData.tasks}">
                            <p class="text-muted text-center py-4">
                                <i class="bi bi-inbox" style="font-size: 3rem;"></i><br>
                                Aucune tâche planifiée
                            </p>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${dashboardData.tasks}" var="task">
                                <div class="task-item ${task.status == 'TERMINEE' ? 'completed' : ''} 
                                            ${task.urgent ? 'urgent' : ''}">
                                    <div class="d-flex justify-content-between align-items-start">
                                        <div class="flex-grow-1">
                                            <div class="d-flex align-items-center mb-2">
                                                <input type="checkbox" 
                                                       class="form-check-input me-2" 
                                                       ${task.status == 'TERMINEE' ? 'checked' : ''}
                                                       onclick="toggleTaskCompletion(${task.taskId})">
                                                <strong>${task.title}</strong>
                                            </div>
                                            <p class="text-muted mb-2 small">
                                                ${task.description}
                                            </p>
                                            <div class="d-flex gap-2 align-items-center">
                                                <span class="badge-priority ${fn:toLowerCase(task.priority)}">
                                                    ${task.priority}
                                                </span>
                                                <span class="text-muted small">
                                                    <i class="bi bi-clock"></i>
                                                    ${task.durationMinutes} min
                                                </span>
                                                <c:if test="${task.scheduledTime != null}">
                                                    <span class="text-muted small">
                                                        <i class="bi bi-alarm"></i>
                                                        <fmt:formatDate value="${task.scheduledTime}" 
                                                                      pattern="HH:mm"/>
                                                    </span>
                                                </c:if>
                                            </div>
                                        </div>
                                        <div class="dropdown">
                                            <button class="btn btn-sm btn-light" 
                                                    data-bs-toggle="dropdown">
                                                <i class="bi bi-three-dots-vertical"></i>
                                            </button>
                                            <ul class="dropdown-menu">
                                                <li>
                                                    <a class="dropdown-item" 
                                                       href="${pageContext.request.contextPath}/tasks/edit?id=${task.taskId}">
                                                        <i class="bi bi-pencil"></i> Modifier
                                                    </a>
                                                </li>
                                                <li>
                                                    <a class="dropdown-item text-danger" 
                                                       href="#"
                                                       onclick="deleteTask(${task.taskId}); return false;">
                                                        <i class="bi bi-trash"></i> Supprimer
                                                    </a>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Trajets -->
                <div class="stat-card">
                    <h4 class="mb-3">
                        <i class="bi bi-map"></i>
                        Trajets
                    </h4>
                    <c:choose>
                        <c:when test="${empty dashboardData.trips}">
                            <p class="text-muted text-center py-3">Aucun trajet planifié</p>
                        </c:when>
                        <c:otherwise>
                            <div class="trip-timeline">
                                <c:forEach items="${dashboardData.trips}" var="trip">
                                    <div class="trip-point">
                                        <strong>${trip.departureLocation}</strong> 
                                        → 
                                        <strong>${trip.arrivalLocation}</strong>
                                        <br>
                                        <small class="text-muted">
                                            <fmt:formatDate value="${trip.departureTime}" pattern="HH:mm"/> - 
                                            <fmt:formatDate value="${trip.arrivalTime}" pattern="HH:mm"/>
                                            (${trip.durationMinutes} min)
                                        </small>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Colonne droite : Alarmes et Actions rapides -->
            <div class="col-md-4">
                <!-- Heure actuelle -->
                <div class="time-block">
                    <div class="text-center">
                        <div style="font-size: 3rem; font-weight: bold;" id="currentTime">
                            ${currentTime}
                        </div>
                        <p class="mb-0">Progression: <span id="dayProgressText">${dashboardData.dayProgress}</span>%</p>
                        <div class="progress mt-2" style="height: 8px;">
                            <div class="progress-bar bg-white" 
                                 role="progressbar" 
                                 id="dayProgressBar"
                                 style="width: ${dashboardData.dayProgress}%">
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Alarmes -->
                <div class="stat-card">
                    <h5 class="mb-3">
                        <i class="bi bi-bell-fill"></i>
                        Alarmes
                    </h5>
                    <c:choose>
                        <c:when test="${empty dashboardData.alarms}">
                            <p class="text-muted text-center py-3">Aucune alarme active</p>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${dashboardData.alarms}" var="alarm">
                                <div class="alarm-item ${alarm.active ? 'active' : ''}">
                                    <div>
                                        <strong>${alarm.title}</strong><br>
                                        <small class="text-muted">
                                            <fmt:formatDate value="${alarm.alarmTime}" pattern="HH:mm"/>
                                        </small>
                                    </div>
                                    <div class="form-check form-switch">
                                        <input class="form-check-input" 
                                               type="checkbox" 
                                               ${alarm.active ? 'checked' : ''}
                                               onchange="toggleAlarm(${alarm.alarmId})">
                                    </div>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Actions rapides -->
                <div class="stat-card">
                    <h5 class="mb-3">Actions rapides</h5>
                    <button class="quick-action-btn" onclick="window.location.href='${pageContext.request.contextPath}/tasks/add'">
                        <i class="bi bi-plus-circle"></i> Nouvelle tâche
                    </button>
                    <button class="quick-action-btn" onclick="window.location.href='${pageContext.request.contextPath}/trips/add'">
                        <i class="bi bi-geo-alt"></i> Nouveau trajet
                    </button>
                    <button class="quick-action-btn" onclick="window.location.href='${pageContext.request.contextPath}/alarms/add'">
                        <i class="bi bi-alarm"></i> Nouvelle alarme
                    </button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // ✅ CORRECTION : Code exécuté après le chargement du DOM
        document.addEventListener('DOMContentLoaded', function() {
            // ✅ CORRECTION : Récupération du score depuis l'élément HTML
            const scoreElement = document.getElementById('wellnessValue');
            if (scoreElement) {
                const scoreText = scoreElement.textContent.trim();
                const score = parseInt(scoreText);
                
                // ✅ CORRECTION : Vérification que le score est valide
                if (!isNaN(score) && score >= 0 && score <= 100) {
                    updateWellnessCircle(score);
                }
            }
            
            // Démarrer l'horloge
            updateTime();
            setInterval(updateTime, 1000);
        });
        
        // ✅ CORRECTION : Fonction pour mettre à jour le cercle de bien-être
        function updateWellnessCircle(score) {
            const circle = document.getElementById('wellnessCircle');
            if (!circle) return;
            
            // Calcul de l'angle (0-360 degrés)
            const degrees = (score / 100) * 360;
            
            // Choisir la couleur selon le score
            let color;
            if (score >= 80) {
                color = '#10b981'; // Vert (success)
            } else if (score >= 60) {
                color = '#3b82f6'; // Bleu (primary)
            } else if (score >= 40) {
                color = '#f59e0b'; // Orange (warning)
            } else {
                color = '#ef4444'; // Rouge (danger)
            }
            
            // Appliquer le gradient conique
            circle.style.background = `conic-gradient(
                ${color} 0deg,
                ${color} ${degrees}deg,
                #e5e7eb ${degrees}deg,
                #e5e7eb 360deg
            )`;
        }
        
        // Mise à jour de l'heure en temps réel
        function updateTime() {
            const now = new Date();
            const hours = String(now.getHours()).padStart(2, '0');
            const minutes = String(now.getMinutes()).padStart(2, '0');
            const seconds = String(now.getSeconds()).padStart(2, '0');
            
            const timeElement = document.getElementById('currentTime');
            if (timeElement) {
                timeElement.textContent = hours + ':' + minutes + ':' + seconds;
            }
            
            // Mettre à jour la progression de la journée (4h - 23h = 19h)
            const currentMinutes = now.getHours() * 60 + now.getMinutes();
            const startMinutes = 4 * 60; // 4h00
            const endMinutes = 23 * 60;   // 23h00
            const totalMinutes = endMinutes - startMinutes;
            
            if (currentMinutes >= startMinutes && currentMinutes <= endMinutes) {
                const progress = Math.round(((currentMinutes - startMinutes) / totalMinutes) * 100);
                
                const progressBar = document.getElementById('dayProgressBar');
                const progressText = document.getElementById('dayProgressText');
                
                if (progressBar) progressBar.style.width = progress + '%';
                if (progressText) progressText.textContent = progress;
            }
        }
        
        // Changement de date
        function changeDate(offset) {
            const url = new URL(window.location.href);
            const currentDate = url.searchParams.get('date') || new Date().toISOString().split('T')[0];
            const date = new Date(currentDate);
            date.setDate(date.getDate() + offset);
            url.searchParams.set('date', date.toISOString().split('T')[0]);
            window.location.href = url.toString();
        }
        
      // Toggler la complétude d'une tâche
function toggleTaskCompletion(taskId) {
    fetch('${pageContext.request.contextPath}/tasks', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: 'action=complete&taskId=' + taskId
    })
    .then(response => response.json())
    .then(data => {
        console.log('Tâche mise à jour:', data);
        location.reload(); // Rafraîchir la page
    })
    .catch(error => {
        console.error('Erreur de connexion:', error);
        alert('Erreur de connexion');
    });
}

// Supprimer une tâche
function deleteTask(taskId) {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette tâche ?')) {
        fetch('${pageContext.request.contextPath}/tasks', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: 'action=delete&taskId=' + taskId
        })
        .then(response => response.json())
        .then(data => location.reload())
        .catch(error => console.error('Erreur:', error));
    }
}

// Basculer une alarme
function toggleAlarm(alarmId) {
    fetch('${pageContext.request.contextPath}/alarms/toggle', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: 'alarmId=' + alarmId
    })
    .then(response => response.json())
    .then(data => location.reload())
    .catch(error => {
        console.error('Erreur:', error);
    });
}
    </script>
</body>
</html>