Examen M1GL - Gestion des Classes et Secteurs
Ce projet est une application web monolithique développée avec Spring Boot. L'application utilise Thymeleaf pour le rendu des pages côté serveur, offrant une interface utilisateur complète. En plus de cette interface web, elle expose une API RESTful pour permettre la gestion des données de manière programmatique.

Fonctionnalités
Le projet implémente les fonctionnalités de base (CRUD) pour la ressource Classe via deux interfaces :

Interface web (Thymeleaf) :

Lister toutes les classes et leurs secteurs associés.

Ajouter une nouvelle classe en la liant à un secteur existant.

Modifier une classe existante.

Supprimer une classe.

API RESTful :

Fournit des endpoints pour les mêmes opérations, permettant l'intégration avec d'autres services ou des clients légers.

Le projet gère également la relation entre les entités Classe et Sector.

Prérequis
Pour compiler et exécuter ce projet, vous avez besoin des outils suivants :

Environnement: 
JDK 17
Maven 3.9.*
Base de données: PostgreSQL

L'application est un projet Maven, donc toutes les dépendances sont gérées automatiquement.

Structure du projet
Le projet suit une architecture de type MVC (Model-View-Controller) et utilise des DTO (Data Transfer Objects) pour séparer les données des entités de celles des couches de présentation.

controller : Gère les requêtes HTTP. Le ClasseController est un contrôleur hybride gérant à la fois les vues Thymeleaf et une API REST.

service : Contient la logique métier pour les classes et les secteurs.

dao : Couche d'accès aux données (Data Access Object) pour interagir avec la base de données.

entity : Contient les entités JPA (ClasseEntity et SectorEntity).

dto : Contient les DTO (ClasseDto et SectorDto).

mapper : Gère la conversion entre les entités et les DTO.

exception : Contient les classes d'exceptions personnalisées.

resources : Contient les fichiers de configuration (application.properties) et les templates HTML (Thymeleaf).

<img width="605" height="892" alt="image" src=
  "https://github.com/user-attachments/assets/7486a9e8-cd4b-4f70-b735-d64738a8e581" />


Captures d'écran de l'application
Page d'accueil de l'application avec le port 8088
<img width="1919" height="851" alt="image" src="https://github.com/user-attachments/assets/5b707900-4f91-42a2-93a2-77b540043af9" />

Liste des classes et des secteurs

SECTEURS

Cette vue affiche la liste des classes et des secteurs. Un bouton "Ajouter" est disponible pour créer une nouvelle classe.

<img width="1810" height="946" alt="image" src="https://github.com/user-attachments/assets/fe5e64a7-8fbf-468e-a11a-e2acdabf90d4" />


Formulaire d'ajout

<img width="1910" height="907" alt="image" src="https://github.com/user-attachments/assets/7cbaf9b4-ea4c-428e-ac88-040835d11238" />

Formulaire de modification
<img width="1919" height="924" alt="image" src="https://github.com/user-attachments/assets/427d96ac-1515-4d2b-95fb-ac210605f8cb" />
Supression d'un secteur 

<img width="1873" height="938" alt="image" src="https://github.com/user-attachments/assets/388abcb9-2ff3-49ad-b022-b673b08ce3dd" />
CLASSES
<img width="1911" height="996" alt="image" src="https://github.com/user-attachments/assets/504b5222-0746-4aeb-bb98-429d9194f450" />

Formulaire d'ajout
<img width="1912" height="955" alt="image" src="https://github.com/user-attachments/assets/9ca1a38f-88a6-480c-ab6e-ef7eac5ba62a" />


Formulaire de modification
<img width="1878" height="814" alt="image" src="https://github.com/user-attachments/assets/5ff4a181-ac71-4583-8fec-d2adc116045f" />

-Test unitaire (a faire)

Supression d'une classe
<img width="1861" height="929" alt="image" src="https://github.com/user-attachments/assets/98e28095-5402-4051-a145-33a92c55fe9e" />



