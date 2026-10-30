# Pac-Man en Java

Une version de Pac-Man en Java avec Swing, qui essaie de rester proche du jeu d'arcade d'origine. Le comportement du jeu (déplacements, vitesses, IA des fantômes) s'appuie sur le [Pac-Man Dossier](https://pacman.holenet.info/) de Jamey Pittman.

## Ce qui est implémenté

- le labyrinthe classique, construit à partir d'un fichier texte
- Pac-Man contrôlé au clavier
- les quatre fantômes avec leur propre façon de chasser (Blinky, Pinky, Inky et Clyde), la sortie de la maison des fantômes, les modes chase / scatter et le mode effrayé
- le score, les vies, le passage au niveau suivant et la vie supplémentaire
- les bonus (fruits)

## Organisation du code

Le projet suit une architecture MVC :

```
src/fr/upsaclay/bibs/pacman/
├── model/      logique du jeu
│   ├── actors/   Pac-Man, fantômes, bonus
│   ├── board/    plateau : score, niveaux, vies, événements
│   └── maze/     labyrinthe et tuiles
├── view/       affichage Swing
├── control/    lien entre la vue et le modèle
└── test/       tests JUnit 5
resources/      labyrinthes et sprites (fichiers texte)
```

Les sprites ne sont pas des images : chaque fichier de `resources/` est une grille de nombres, et chaque nombre est l'indice d'une couleur.

## Lancer le jeu

Il faut Java 17 ou plus récent.

Avec IntelliJ :
1. ouvrir le dossier du projet
2. marquer `src` comme *Sources Root* et `resources` comme *Resources Root*
3. lancer `PacManApp` (le répertoire de travail doit être la racine du projet)

En ligne de commande, depuis la racine du projet (sous Windows, remplacer `out:resources` par `out;resources`) :

```bash
javac -d out $(find src -name "*.java" -not -path "*/test/*")
java -cp out:resources fr.upsaclay.bibs.pacman.PacManApp
```

On joue avec les flèches du clavier.

## Tests

Les tests se trouvent dans `src/fr/upsaclay/bibs/pacman/test` et ont besoin de JUnit 5. Ils sont rangés par étape (`step1` à `step4`). Chaque étape reprend les tests de la précédente en les mettant à jour, donc certains tests des premières étapes ne passent plus avec la version finale du code.

## Crédits

Projet réalisé par Camille Le Corre et Laura Lefevre.

Les interfaces de départ et les tests ont été fournis par Viviane Pons (Université Paris-Saclay).