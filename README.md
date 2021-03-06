# Distributed Systems Project for AUEB

Το project αυτό έγινε για τους σκοπούς του Μαθήματος "Κατανεμημένα Συστήματα" του Οικονομικού Πανεπιστημίου Αθηνών.

Υλοποιεί τον αλγόριθμο Map-Reduce αξιοποιώντας τα Streams της Java 8. Ο χρήστης επιλέγει για μια συγκεκριμένη γεωγραφική περιοχή της Υόρκης και για μια συγκεκριμένη περίοδο να πληροφορηθεί για τα top-K πιο δημοφιλή μέρη. Κάθε Mapper παίρνει μια υποπεριοχή από την επιλογή του χρήστη και στέλνει τα αποτελέσματά του στον Reducer ο οποίος συνδυάζει τα ενδιάμεσα αποτελέσματα από τον καθένα παράγοντας το τελικό αποτέλεσμα που θα φτάσει τελικά στο χρήστη.

Για να τρέξουμε την εφαρμογή τρέχουμε σε μια γραμμή εντολών την εντολή `java -jar DS_Project.jar` ή την εντολή `java -jar DS_Project_GUI.jar` αν θέλουμε την έκδοση της εφαρμογής με το γραφικό περιβάλλον (GUI).

Στο ξεκίνημά της η εφαρμογή ζητάει από το χρήστη να διαλέξει ποιον ρόλο θέλει να επιτελέσει.

Πατώντας `1`  ο χρήστης αντιμετωπίζεται σαν Client δηλαδή είναι αυτός που θα καθορίσει το χωρικό και χρονικό εύρος για τις τοποθεσίες που θα επεξεργαστεί ο κάθε Mapper. Στη συνέχεια ο χρήστης επιλέγει `1` αν θέλει να δοκιμάστει μια προκαθορισμένη περιοχή ή `2` αν θέλει να καθορίσει ο ίδιος τα Inputs των Mappers. Συγκεκριμένα θα καθορίσει τα Min, Max Longitude και Latitude καθώς επίσης και μια αρχική και τελική ημερομηνία. 

Πατώντας `2`  ο χρήστης αντιμετωπίζεται σαν Mapper και στη συνέχεια διαλέγει ανάμεσα στις επιλογές `1`, `2` και `3` για να επιλέξει ποιος Mapper θα είναι. Τέλος περιμένει συνδέσεις από κάποιον Client και στέλνει τα ενδιάμεσα αποτελέσματα του στον Reducer που ήδη έχει ανοίξει και περιμένει για συνδέσεις από κάποιον Mapper.

Πατώντας `3` ο χρήστης αντιμετωπίζεται σαν Reducer και περιμένει από κάποιον Mapper ο οποίος θα του στείλει τα ενδιάμεσα αποτελέσματά του. Όταν λάβει αποτελέσματα από ακριβώς 3 Mappers ξεκινάει τη Reduce διαδικασία και στέλνει το τελικό αποτέλεσμα στον Client.

Για το Project αυτό ασχολήθηκαν οι φοιτητές:
- Μπούζας Βασίλειος
- Τασσιάς Παναγιώτης
- Τζέλου Μαρία
- Χαλκιώτης Παναγιώτης
