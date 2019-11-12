# ln-qrcode

--- English version below ---

⚙️ Générateur de codes QR en Java - Mini-projet EPFL

### Bonus implémentés
- Petite interface graphique en Swing qui utilise `Helpers.show()`
pour l'affichage de codes QR avec un certain masque et une
certaine version. L'interace nécessite donc la présence du fichier `Helpers.java`
que vous nous avez mise à disposition, mais qui d'après les indications du programme de rendu
n'a pas besoin d'être inclus dans cette archive. Pour l'afficher, décommenter la ligne `QRCodeFrame frame = new QRCodeFrame();`
et commenter la ligne `Helpers.show(qrCode, SCALING);` du `main()`.
- Méthodes `evaluate()`, `findBestMasking()` et toutes les méthodes écrites
 dans le but de modulariser et d'améliorer la clarté et la lisibilité du code. Par défaut, le masque choisi
 est celui qui est 'hardcodé' dans le `main()`. Pour activer le choix automatique,
 décommenter la ligne 29 et commenter la ligne 26 du `main()`.
- Nous sommes conscients des solutions plus "élégantes" qui existeraient pour alterner
entre les fonctionalités bonus et obligatoires mais ne sachant pas comment vous
effectuerez la correction du travail, nous avons décidé de rendre le `main()`
qui correspondrait à celui utilisé dans un mini-projet où aucun bonus ne serait implémenté.

##### Auteurs
TERRIER Noé, HERVÉ Louis | Octobre 2019 - EPFL

--- English version ---

⚙️ QR Code generator in Java - EPFL IN Mini-project

### Implemented bonuses
- Small GUI written with Swing that uses `Helpers.show()`
to display QR Codes with a specific mask and a specific version.
Thus, the GUI relies on the file `Helpers.java` that you provided, but according to the
submission server, we do not need to include the file in this archive.
To display the GUI, remove the comment `QRCodeFrame frame = new QRCodeFrame();`
and comment the line `Helpers.show(qrCode, SCALING);` of the `main()` method.
- `evaluate()`, `findBestMasking()` methods and various others methods written
to improve the readablity and the clarity of the code. By default, the choosen mask is the one
 which is hardcoded in the `main()` method. To enable automatic mask selection,
 remove the comment on line 29 and comment line 26 of the `main()` method.
- We are well aware of the various more "elegant" solutions that exist to switch between the
mandatory features and the bonus ones. But, as we do not know how you will
proceed when analyzing our work, we decided to stick with the `main()` method that matches
the one needed for the mandatory features we had to implement.

##### Authors
TERRIER Noé, HERVÉ Louis | Oct 2019 - EPFL