# StarLD

Notre application permet de :

- Choisir une date et une heure en cliquant sur le textView prévu à cet effet
- Choisir une ligne de bus en cliquant sur le premier spinner
- Choisir un terminal correspondant à la ligne de bus précedemment choisie, en cliquant sur le deuxième spinner

Lorsque l'on installe l'application, la base de données est remplie grâce au fichier zip récupérée sur le site de la Star.
Ensuite, toutes les 15 minutes un service vérifiera qu'il n'y a pas de nouvelles données à télécharger. 
Si c'est le cas une notification apparaitra, permettant ainsi à l'utilisateur de télécharger les nouvelles données 
en cliquant sur la notification.
