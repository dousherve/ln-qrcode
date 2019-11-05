# ln-qrcode

⚙️ Générateur de codes QR en Java - Mini-projet EPFL

### Bonus
- Petite interface graphique en Swing qui utilise `Helpers.show()`
pour l'affichage de codes QR avec un certain masque et une
certaine version. Pour l'afficher, décommenter la ligne `QRCodeFrame frame = new QRCodeFrame();`
et commenter la ligne `Helpers.show(qrCode, SCALING);` du `main`.
- Méthodes `evaluate()`, `findBestMasking()` et toutes les méthodes écrites
 dans le but de modulariser au maximum le code. Par défaut, le masque choisi
 est celui qui est 'hardcodé' dans le `main`. Pour activer le choix automatique,
 décommenter la ligne 29 et commenter la ligne 26 du `main`.
- Nous sommes conscients des solutions plus "élégantes" qui existeraient pour alterner
entre les fonctionalités bonus et obligatoires mais ne sachant pas comment vous
effectuerez la correction, nous avons décidé de garder le squelette du `main`
identique à celui utilisé dans un mini-projet où aucun bonus ne serait implémenté.

##### Auteurs
TERRIER Noé, HERVÉ Louis | Oct-Nov 2019 - EPFL
