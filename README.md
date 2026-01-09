# 🚀 Guide de Démarrage Rapide - Système Gestion Emploi du Temps

## ⚡ Installation en 5 Étapes

### 1️ Configurer PostgreSQL (5 minutes)
psql : se connecter au serveur PostgreSQL.

\l : lister toutes les bases de données.

\c nom_base : se connecter à une base spécifique.

\dt : lister les tables.

\d nom_table : décrire la structure d’une table.

\q : quitter psql.
📌 4. Commandes DDL (Data Definition Language)
CREATE TABLE nom_table (...);

ALTER TABLE nom_table ADD COLUMN nouvelle_colonne TYPE;

ALTER TABLE nom_table DROP COLUMN colonne;

DROP TABLE nom_table;

📌 5. Commandes DML (Data Manipulation Language)
INSERT INTO nom_table (col1, col2) VALUES (val1, val2);

UPDATE nom_table SET col1 = val WHERE condition;

DELETE FROM nom_table WHERE condition;

```bash
# Démarrer PostgreSQL
sudo systemctl start postgresql

# Se connecter
sudo -u postgres psql
#LE MICO AM BASE
 sudo -u postgres psql psql -U wellness_user -d wellness_db

# Créer la base et l'utilisateur
CREATE DATABASE wellness_db;
CREATE USER wellness_user WITH PASSWORD '123';
GRANT ALL PRIVILE GES ON DATABASE wellness_db TO wellness_user;
ALTER DATABASE wellness_db OWNER TO wellness_user;
\q

# Importer le schéma
psql -U wellness_user -d wellness_db -f src/main/java/Persistence/schema-complete.sql
```

✅ **Vérification** : `psql -U wellness_user -d wellness_db -c "\dt"`

---

### 2️⃣ Configurer Tomcat (3 minutes)

```bash
# Télécharger Tomcat 10 (si non installé)
wget https://dlcdn.apache.org/tomcat/tomcat-10/v10.1.17/bin/apache-tomcat-10.1.17.tar.gz
tar -xzf apache-tomcat-10.1.17.tar.gz
mv apache-tomcat-10.1.17 ~/tomcat

# Rendre les scripts exécutables
chmod +x ~/tomcat/bin/*.sh

# Ajouter le driver PostgreSQL
wget https://jdbc.postgresql.org/download/postgresql-42.7.1.jar
cp postgresql-42.7.1.jar ~/tomcat/lib/
```

✅ **Vérification** : `~/tomcat/bin/version.sh`

---

### 3️⃣ Compiler le Projet (2 minutes)

```bash
# Aller dans le dossier du projet
cd /path/to/your/project

# Modifier le script deploy.sh
nano deploy.sh

# Adapter ces lignes :
# TOMCAT_WEBAPPS="/home/VOTRE_USER/tomcat/webapps"
# LIB_DIR="/home/VOTRE_USER/tomcat/lib"

# Rendre le script exécutable
chmod +x deploy.sh

# Compiler et déployer
./deploy.sh
```

✅ **Vérification** : Vous devriez voir "Déploiement terminé"

---

### 4️⃣ Démarrer Tomcat (1 minute)

```bash
# Démarrer le serveur
~/tomcat/bin/startup.sh

# Suivre les logs en temps réel
tail -f ~/tomcat/logs/catalina.out
```

✅ **Vérification** : Vous devriez voir "Server startup in XXX milliseconds"

---

### 5️⃣ Accéder à l'Application (30 secondes)

```bash
# Ouvrir le navigateur
firefox http://localhost:8080/WellnessApp/
# ou
google-chrome http://localhost:8080/WellnessApp/
```

**Identifiants de test** :
- Username: `jean_dupont`
- Password: `password123`

✅ **Vérification** : Vous devriez voir le dashboard

---

## 📁 Structure des Fichiers Créés

Voici tous les fichiers que vous avez besoin de créer :

```
wellness-app/
├── deploy.sh                                    ✅ Script fourni
├── README.md                                    ✅ Documentation
├── QUICK_START.md                              ✅ Ce guide
│
├── src/
│   └── main/
│       ├── java/
│       │   ├── Controller/
│       │   │   ├── DashboardServlet.java       ✅ Fourni
│       │   │   ├── TaskServlet.java            ✅ Fourni
│       │   │   ├── TripServlet.java            ⚠️ À créer (similaire à Task)
│       │   │   ├── AlarmServlet.java           ✅ Fourni
│       │   │   ├── WellnessServlet.java        ⚠️ À créer (simple)
│       │   │   └── LoginServlet.java           ✅ Fourni
│       │   │
│       │   ├── Model/
│       │   │   ├── User.java                   ✅ Fourni
│       │   │   ├── Task.java                   ✅ Fourni
│       │   │   ├── Trip.java                   ✅ Fourni
│       │   │   ├── Meal.java                   ✅ Fourni
│       │   │   ├── RestPeriod.java            ✅ Fourni
│       │   │   ├── Alarm.java                  ✅ Fourni
│       │   │   ├── WellnessScore.java         ✅ Fourni
│       │   │   ├── Priority.java               ✅ Fourni
│       │   │   ├── TaskStatus.java             ✅ Fourni
│       │   │   ├── TripType.java               ✅ Fourni
│       │   │   ├── MealType.java               ✅ Fourni
│       │   │   ├── RestType.java               ✅ Fourni
│       │   │   └── AlarmType.java              ✅ Fourni
│       │   │
│       │   ├── DAO/
│       │   │   ├── DatabaseConnection.java     ✅ Fourni
│       │   │   ├── TaskDAO.java                ✅ Fourni
│       │   │   ├── TripDAO.java                ✅ Fourni
│       │   │   ├── MealDAO.java                ✅ Fourni
│       │   │   ├── AlarmDAO.java               ✅ Fourni
│       │   │   └── WellnessDAO.java           ✅ Fourni
│       │   │
│       │   ├── Filter/
│       │   │   └── AuthenticationFilter.java   ✅ Fourni
│       │   │
│       │   └── Persistence/
│       │       ├── schema-complete.sql         ✅ Fourni
│       │       └── database.properties         ✅ Fourni
│       │
│       └── webapp/
│           ├── WEB-INF/
│           │   ├── web.xml                     ✅ Fourni
│           │   └── views/
│           │       ├── login.jsp               ✅ Fourni
│           │       ├── dashboard.jsp           ✅ Fourni
│           │       └── tasks/
│           │           └── add.jsp             ✅ Fourni
│           └── css/
│               └── style.css                   ⚠️ Optionnel (styles inline)
```

---

## 🛠️ Commandes Utiles

### PostgreSQL

```bash
# Connexion
psql -U wellness_user -d wellness_db

# Lister les tables
\dt

# Voir les données
SELECT * FROM users;
SELECT * FROM tasks;

# Compter les enregistrements
SELECT COUNT(*) FROM tasks;

# Quitter
\q

# Réinitialiser la base
psql -U wellness_user -d wellness_db -c "DROP SCHEMA public CASCADE; CREATE SCHEMA public;"
psql -U wellness_user -d wellness_db -f src/main/java/Persistence/schema-complete.sql
```

### Tomcat

```bash
# Démarrer
~/tomcat/bin/startup.sh

# Arrêter
~/tomcat/bin/shutdown.sh

# Redémarrer
~/tomcat/bin/shutdown.sh && sleep 2 && ~/tomcat/bin/startup.sh

# Voir les logs
tail -f ~/tomcat/logs/catalina.out

# Nettoyer les anciens déploiements
rm -rf ~/tomcat/webapps/WellnessApp*
rm -rf ~/tomcat/work/Catalina/localhost/WellnessApp
```

### Compilation

```bash
# Recompiler et redéployer
./deploy.sh

# Compilation manuelle
mkdir -p build/WEB-INF/classes
find src/main/java -name "*.java" > sources.txt
javac -cp "~/tomcat/lib/servlet-api.jar:~/tomcat/lib/postgresql-42.7.1.jar" \
      -d build/WEB-INF/classes @sources.txt

# Créer le WAR
cd build && jar -cvf WellnessApp.war * && cd ..

# Déployer
cp build/WellnessApp.war ~/tomcat/webapps/
```

---

## 🐛 Dépannage Rapide

### Problème : "Connexion refusée PostgreSQL"

```bash
# Solution 1 : Démarrer PostgreSQL
sudo systemctl start postgresql

# Solution 2 : Vérifier les paramètres
cat src/main/java/Persistence/database.properties

# Solution 3 : Tester la connexion
psql -U wellness_user -d wellness_db -c "SELECT 1;"
```

### Problème : "404 - Application non trouvée"

```bash
# Solution 1 : Vérifier le déploiement
ls -l ~/tomcat/webapps/

# Solution 2 : Vérifier les logs
grep -i error ~/tomcat/logs/catalina.out

# Solution 3 : Redéployer
./deploy.sh
~/tomcat/bin/shutdown.sh && sleep 2 && ~/tomcat/bin/startup.sh
```

### Problème : "Erreur de compilation"

```bash
# Solution 1 : Vérifier les JARs
ls -l ~/tomcat/lib/servlet-api.jar
ls -l ~/tomcat/lib/postgresql*.jar

# Solution 2 : Nettoyer et recompiler
rm -rf build
./deploy.sh

# Solution 3 : Vérifier Java
java -version  # Doit être 11+
javac -version
```

### Problème : "Session expirée"

```bash
# Solution : Ajuster le timeout dans web.xml
<session-timeout>60</session-timeout>  <!-- 60 minutes au lieu de 30 -->
```

---

## 📝 Checklist Avant de Commencer

- [ ] PostgreSQL installé et démarré
- [ ] Tomcat 10+ téléchargé
- [ ] Java JDK 11+ installé
- [ ] Driver PostgreSQL dans `tomcat/lib/`
- [ ] Base de données `wellness_db` créée
- [ ] Schéma SQL importé
- [ ] Tous les fichiers Java créés
- [ ] `web.xml` configuré
- [ ] `deploy.sh` adapté à vos chemins
- [ ] Port 8080 disponible

---

## 🎯 Tester le Système

### Test 1 : Connexion

```
1. Ouvrir http://localhost:8080/WellnessApp/
2. Se connecter avec jean_dupont / password123
3. ✅ Vous devriez voir le dashboard
```

### Test 2 : Créer une Tâche

```
1. Cliquer sur "Nouvelle tâche"
2. Remplir le formulaire
3. Soumettre
4. ✅ La tâche apparaît dans le dashboard
```

### Test 3 : Score de Bien-être

```
1. Vérifier le score dans le dashboard
2. Compléter une tâche
3. Rafraîchir la page
4. ✅ Le score devrait augmenter
```

### Test 4 : Alarmes

```
1. Aller dans "Actions rapides" > "Nouvelle alarme"
2. Créer une alarme pour dans 5 minutes
3. Attendre 5 minutes
4. ✅ L'alarme devrait apparaître dans "Alarmes actives"
```

---

## 💡 Astuces Pro

### 1. Auto-reload pour le développement

```bash
# Installer JRebel ou utiliser :
export CATALINA_OPTS="-Dorg.apache.catalina.startup.EXIT_ON_INIT_FAILURE=true"
```

### 2. Logs en couleur

```bash
# Installer ccze
sudo apt install ccze

# Utiliser
tail -f ~/tomcat/logs/catalina.out | ccze -A
```

### 3. Sauvegarde automatique de la DB

```bash
# Créer un script backup.sh
#!/bin/bash
pg_dump -U wellness_user wellness_db > ~/backups/wellness_$(date +%Y%m%d_%H%M%S).sql

# Cron job (chaque jour à minuit)
0 0 * * * /path/to/backup.sh
```

### 4. Monitoring de l'application

```bash
# Installer Tomcat Manager (optionnel)
# Accéder à : http://localhost:8080/manager/html
```

---

## 📚 Prochaines Étapes

Une fois le système fonctionnel :

1. ✅ **Personnaliser** : Modifier les couleurs, textes, etc.
2. ✅ **Ajouter des fonctionnalités** : Export PDF, graphiques
3. ✅ **Optimiser** : Ajouter des caches, optimiser les requêtes
4. ✅ **Sécuriser** : Hasher les mots de passe avec BCrypt
5. ✅ **Déployer** : Mettre en production sur un serveur

---

## 🆘 Besoin d'Aide ?

### Erreurs Courantes

| Erreur | Solution |
|--------|----------|
| `ClassNotFoundException: org.postgresql.Driver` | Ajouter postgresql.jar dans tomcat/lib |
| `HTTP 500 - SQLException` | Vérifier database.properties |
| `HTTP 404` | Vérifier l'URL et le déploiement WAR |
| `Session expired` | Augmenter session-timeout dans web.xml |

### Ressources

- Documentation PostgreSQL : https://www.postgresql.org/docs/
- Documentation Tomcat : https://tomcat.apache.org/tomcat-10.0-doc/
- Tutoriel JDBC : https://www.tutorialspoint.com/jdbc/
- Bootstrap : https://getbootstrap.com/docs/5.3/

---

## ✨ Bon Développement !

Votre système de gestion d'emploi du temps est maintenant prêt à l'emploi. N'hésitez pas à l'adapter à vos besoins spécifiques !

**Questions ?** Consultez le README.md complet pour plus de détails.

---

*Créé avec ❤️ pour ITUniversity - Janvier 2025*