# StarLD

Notre application permet de :

- Choisir une date et une heure en cliquant sur le textView prévu à cet effet
- Choisir une ligne de bus en cliquant sur le premier spinner
- Choisir un terminal correspondant à la ligne de bus précedemment choisie, en cliquant sur le deuxième spinner

Lorsque l'on installe l'application, la base de données est remplie grâce au fichier zip récupérée sur le site de la Star.
Ensuite, toutes les 15 minutes un service vérifiera qu'il n'y a pas de nouvelles données à télécharger. 
Si c'est le cas une notification apparaitra, permettant ainsi à l'utilisateur de télécharger les nouvelles données 
en cliquant sur la notification.


# Problèmes connue

Pour le moment, quelques problèmes n'ont pas encore été résolu :

- Lors du premier lancement de l'application, comme l'installation de la base de données n'est pas instantanée, l'application va envoyer une notification malgré le fait que le processus pour remplir la base de données soit commencé.
- Lors du remplissage de la base de données, l'écran est blanc ca l'application est en cours de traitement, il se peut que le téléphone pense que l'application ai crashé, mais ce n'est pas le cas, il faut attendre quelques minutes (5 maximum)
- Une fois les spinners rempli, lorsque l'on relance l'application, les spinners n'apparaissent pas automatiquement

Ces problèmes seront traité pour la V2.
