# 6. Testing
In questo capitolo, verranno descritte le modalità di testing adottate, le tecnologie utilizzate per tale scopo e il grado di copertura che è stato ritenuto opportuno.

## Grado di copertura
L'utilizzo dell'architettura MVC ha permesso di isolare la logica del programma dalla parte che richiedeva l'interazione dell'utente; dunque è stato deciso che la scrittura formale di test si sarebbe concentrata principalmente sul Model, mentre la View e il Controller sono stati testati eseguendo il programma e controllando che non vi fossero errori nella visualizzazione delle informazioni, per poi passare alla verifica che tali informazioni venissero modificate correttamente a seconda dell'input dell'utente. Ove fosse possibile, il Controller è stato incluso nei test formali, ma ciò non è sempre stato ritenuto utile o necessario.

## Metodologia utilizzata
Per lo sviluppo si è scelto di procedere secondo i dogmi del TDD. Ciò ha significato la scrittura di test relativi a feature non ancora implementate prima di procedere con l'implementazione delle stesse per quanto riguarda il Model, mentre per View e Controller– ove non fosse possibile la scrittura di test per ques'ultimo– si è proceduto col creare classi di mockup di elementi del Model per permettere l'isolamento delle varie schermate dell'applicazione, e la successiva revisione delle reazione di queste ultime agli input dell'utente. Tali classi di mockup sono risultate utili anche nel testing delle parti più complesse di Model.

## Tecnologie utilizzate
La libreria **scalatest** è stata scelta per la scrittura della maggior parte dei test, utilizzando come **sbt** come build tool, unito al plugin **sbt-assembly** per la creazione del fat JAR. Il comando "sbt assembly" del plugin prevede come impostazione predefinita l'esecuzione di tutti i test prima della generazione del jar.


[Capitolo Precedente](Implementation.md) | [Indice](Index.md) | [Prossimo Capitolo](Retrospective.md)