# 🟢 Application Chikayat

## 📌 Description
Application complète de gestion des réclamations composée de :
- **Backend** : Java Spring Boot
- **Frontend** : Angular

---

## 🚀 Instructions de déploiement sous Windows

### ✅ Étapes préalables :

#### 1\. Installation de Tesseract-OCR
- Télécharger Tesseract-OCR : [https://github.com/tesseract-ocr/tesseract](https://github.com/tesseract-ocr/tesseract)
- Installer sous le dossier :
```
C:\Program Files\Tesseract-OCR
```
- Ajouter `C:\Program Files\Tesseract-OCR` à la variable d’environnement Windows (`Path`).

---

#### 2\. Installation de MySQL Server
- Télécharger MySQL Server : [https://dev.mysql.com/downloads/mysql](https://dev.mysql.com/downloads/mysql)
- Installer MySQL Server (version recommandée : 8.x).
- Créer une base de données nommée `reclamations`.
- Importer la base fournie (`export.sql`) :

```cmd
mysql -u root -p --default-character-set=utf8 < export.sql
```

---

#### 3\. Création du dossier pour le stockage des scans
- Créer le dossier suivant :

```
C:\stockage_scans
```

---

### ✅ Déploiement du Backend Spring Boot

- Copier le contenu du dossier `backend` vers :

```
C:\deploy\backend
```

Arborescence finale obtenue :

```
C:\deploy\backend\
├── chikayat-0.0.1-SNAPSHOT.jar
└── config\
    └── application.properties
```

- **Lancer le backend** en utilisant la commande suivante (CMD) :

```cmd
cd C:\deploy\backend
java -jar chikayat-0.0.1-SNAPSHOT.jar --spring.config.additional-location=file:./config/
```

---

### ✅ Déploiement du Frontend Angular avec IIS

- Activer IIS sur Windows :

```
Panneau de configuration → Programmes → Activer ou désactiver des fonctionnalités Windows → cocher "Internet Information Services"
```

- Copier le contenu du dossier `frontend` vers :

```
C:\inetpub\wwwroot\frontend
```

Arborescence finale obtenue :

```
C:\inetpub\wwwroot\frontend\
├── index.html
├── assets\
├── uploads\
├── main-xxxxx.js
├── polyfills-xxxxx.js
├── scripts-xxxxx.js
└── styles-xxxxx.css
```

- Ajouter un document par défaut dans IIS :
  - Ouvrir IIS (`inetmgr`)
  - Cliquer sur `frontend` → Double-cliquer sur `Document par défaut`
  - Ajouter : `index.html`

- **Important** : Utiliser ce chemin dans ton fichier `index.html` :

```html
<base href="/frontend/">
```

---

### ✅ Configuration IIS : Proxy inverse vers Backend Spring Boot ==> Optionnel, jsute en cas de problème

- Installer les modules IIS suivants :
  - [URL Rewrite](https://www.iis.net/downloads/microsoft/url-rewrite)
  - [Application Request Routing](https://www.iis.net/downloads/microsoft/application-request-routing)

- Dans IIS :
  - Cliquer sur ton site (`Default Web Site`) → Ouvrir `URL Rewrite` → Ajouter une règle :
    - Type de règle : **Proxy inverse**
    - Modèle d’URL : `^api/(.*)`
    - Réécrire l’URL vers : `http://localhost:8080/api/{R:1}`

- Redémarrer IIS pour appliquer les modifications.

---

## 🚩 Accès à l'application une fois déployée :

- Frontend : [http://localhost/frontend](http://localhost/frontend)     ==> remplacer localhost par l'IP local
- Backend API : [http://localhost:8080/api](http://localhost:8080/api)

---

## 🔧 Script pratique pour arrêter facilement le backend :

Créer un fichier batch nommé `stop-backend.bat` avec le contenu suivant :

```batch
@echo off
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8080') do (
    taskkill /F /PID %%a
)
echo Backend arrêté.
pause
```

---

## 🚨 Points de configuration importants (à vérifier avant démarrage) :

**Fichier : `application.properties` du backend**

```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/reclamations
spring.datasource.username=root
spring.datasource.password=ton_mot_de_passe
file.upload-dir=C:/stockage_scans
```

---

## ✅ Dépendances techniques requises :

| Composant            | Version recommandée      |
|----------------------|--------------------------|
| Java (JDK)           | 17+                      |
| Node.js              | 18+                      |
| Angular CLI          | 16+                      |
| MySQL                | 8.x                      |
| Tesseract-OCR        | Dernière version stable  |
| IIS (Windows)        | Inclus dans Windows      |

---

## 📌 Auteur :

- **Issam Mhamedi** - [imhamedi](https://github.com/imhamedi)
