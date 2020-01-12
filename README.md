# StarLD

Lorsque l'on installe l'application, la base de données est remplie grâce au fichier zip récupérée sur le site de la Star.
Ensuite, toutes les 15 minutes un service vérifiera qu'il n'y a pas de nouvelles données à télécharger. 
Si c'est le cas une notification apparaitra, permettant ainsi à l'utilisateur de télécharger les nouvelles données en cliquant sur la notification.

Lorsque la base de données se remplit, son remplissage est suivi avec une barre de progression. Lorsque la base de données est remplie, l'application se ferme. 

La barre de progression n'indique que le remplissage de données il se peut qu'il se passe quelques minutes avant que celle-ci commence à se remplir (le temps que les opérations précédentes se terminent).


# Problème connu

Le download manager ne marche que pour les API d'Androïd inférieure à l'API 28 (pas de résultat concluant sur l'émulateur d'Androïd pour les API 28 et 29)
