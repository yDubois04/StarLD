# StarLD

Lorsque l'on installe l'application, la base de données est remplie grâce au fichier zip récupérée sur le site de la Star.
Ensuite, toutes les 15 minutes un service vérifiera qu'il n'y a pas de nouvelles données à télécharger. 
Si c'est le cas une notification apparaitra, permettant ainsi à l'utilisateur de télécharger les nouvelles données en cliquant sur la notification.

Lorsque la base de données se remplit, son remplissage est suivi avec une progress bar. Lorsque la base de données est remplie, l'application se ferme.


# Problèmes connus

Le download manager ne marche que pour les API d'Androïd inférieure à l'API 28
