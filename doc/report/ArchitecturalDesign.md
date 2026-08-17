# 3. Design Architetturale

## Scelta del modello
Dice forge è un gioco da tavolo, e come tale è incentrato sull'interazione del giocatore (o nel nostro caso, utente); dunque è risultata naturale la scelta del pattern **Model-View-Controller (MVC)**, per scorporare la logica dell'applicazione dalla sua rappresentazione visiva e dall'interazione con l'utente.

## Utilizzo del pattern MVC
Ciascuna schermata della View è controllata da un proprio controller. A ciascuna entità del Model che è necessario rappresentare corrisponde un DTO nel Controller, nel quale vengono inserite tutte le informazioni necessarie a tale scopo, oscurando l'implementazione delle classi le cui istanze le contengono.

## Utilizzo del pattern Publisher-Subscriber
Nel corso del gioco, come esposto in precedenza, vi è spesso la necessità che l'utente scelga tra una serie di opzioni disponibili; a tale scopo, è stato implementato il pattern Publisher-Subscriber, dove il Controller è subscriber del Model e publisher della View. Per reagire alla scelta dell'utente, una serie di listener viene inserita nei DTO delle classi del Model tra cui viene effettuata la scelta. 


[Capitolo Precedente](Requirements.md) | [Indice](Index.md) | [Prossimo Capitolo](DetailedDesign.md)