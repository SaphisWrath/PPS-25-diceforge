# 4. Design di dettaglio

## Elementi principali
Si effettua ora una breve descrizione degli elementi alla base del funzionamento del programma, che verranno descritti più
approfonditamente in seguito.

### Model
Analizzando i requisiti, ci si è resi conto che le ricompense delle missioni e le facce dei dadi operavano secondo principi
simili; è stato dunque deciso di riassumere entrambi i funzionamenti nel concetto di `Effect`, le istanze delle cui implementazioni
sarebbero state ricompense e costi delle missioni e facce dei dadi. Di conseguenza, lo `Shop` del gioco vende `Effect`. `GameManager` incapsula tutti gli elementi
che compongono una partita a Dice Forge, incluso il `TurnManager` per la gestione dei turni e il `MapManager` per l'individuazione della
posizione del giocatore sul tabellone delle missioni. Contiene anche una mappa di `Mission`, divise per casella, e la lista di `Player` della partita.
Ciascun `Player` ha al proprio interno un riferimento alla propria `PlayerBoard`, che contiene il conteggio delle `Resource`
a lui disponibili, ai propri due `Die`, e alle missioni che ha ottenuto.

### Controller
Ad ogni schermata della View corrisponde un Controller, che si occupa di fornire tutte le informazioni necessarie alla UI e di raccogliere
gli input degli utenti e trasmetterli al Model. A questi Controller accoppiati, se ne aggiungono due, `ControllerManager` che controlla lo Stage (ed ha quindi
il compito di cambiare la scena visualizzata), e `ControllerStage` per restituire il Controller necessario a quest'ultima. Una serie di DTO
è stata preparata per tradurre gli oggetti del Model in pacchetti di informazioni per la View, oscurandone l'implementazione. Il pattern Navigator, descritto
in seguito, è stato utilizzato per la navigazione di schermata in schermata. Il package `choices`, inoltre, contiene i Controller dedicati alle finestre di popup per le
varie scelte necessarie nel corso della partita.

### View
La view, scritta in _ScalaFX_, è composta da quattro schermate, ciascuna rappresentata da una `Scene`, un popup delle regole, e all'occorrenza un popup per le scelte. Il popup delle regole è
visualizzabile in qualsiasi momento nel menù principale o nel corso della partita, premendo il tasto "Regole". Il tema che l'applicazione doveva seguire è stato stabilito nel trait `Theme` e nella sua implementazione
`JfxTheme` per evitare che troppi colori rendessero l'interfaccia poco coesa e per permettere di cambiarli senza sostituirli manualmente in ogni elemento. Le `Factory` di `Button` e `Text` hanno la stessa funzione, riferita allo stile degli elementi che costruiscono.
L'object `LanguageStrings` contiene tutte le stringhe necessarie alla UI, eccetto quelle della rappresentazione delle missioni, che abbiamo scorporato per agevolarne l'eventuale cambiamento.


## Publisher

Problema: Molte parti del sistema hanno bisogno di comunicare tra di loro, ma senza un collegamento diretto. Per esempio quando vengono modificate delle risorse bisogna avvisare la view in modo che si aggiorni di conseguenza.
Soluzione: Questo è il caso perfetto per utilizzare il pattern Observer.
```mermaid
---
config:
  class:
    hideEmptyMembersBox: true
---
classDiagram

namespace Controller {
class ViewSubscriber
class ViewContext
class ViewPublisher
}

class Context{
<<trait>>
}
class Subscriber~C <: Context~{
<<abstract>>
+update(context: C)
}
class Publisher~C <: Context~{
+subsribe(subscriber: Subscriber~C~)
+notify(context: C)
+unsubscribe(subscriber: Subscriber~C~)
+reset()
}

Publisher <.. Context
Subscriber <.. Context
Publisher o-- Subscriber

ViewContext --|> Context
ViewSubscriber ..> ViewContext
ViewSubscriber --|> Subscriber
ViewPublisher ..> ViewContext
ViewPublisher o-- ViewSubscriber
ViewPublisher --|> Publisher

namespace Model {
class ModelContext
class ModelSubscriber
class ModelPublisher
}

ModelContext --|> Context
ModelSubscriber --|> Subscriber
ModelSubscriber ..> ModelContext
ModelPublisher --|> Publisher
ModelPublisher ..> ModelContext
ModelPublisher o-- ModelSubscriber
```

Di seguito una breve spiegazione del grafico.

Tipi generici:
- **C**: Rappresenta il sottotipo di `Context` accettato da `Subscriber` e `Publisher`
  Struttura:
  Si tratta di una implementazione del pattern Observer dove i `Subscriber` si possono mettere in ascolto in uno o più `Publisher` e i `Publisher` quando richiesto mandano in broadcast il dato contesto lasciando che siano i `Subscriber` a elaborare l'informazione.
  Dato che nel sistema ci sono molte entità che possono generare eventi che interessano molti subscriber e che questi eventi si sovrappongono, rimanendo sul cambio di risorse questo può avvenire in vari contesti e in ognuno di questi casi va ad interessare varie componenti della view che si devono aggiornare di conseguenza. Per questo motivo si è optato per l'utilizzo del pattern Singleton per il `Publisher`, in particolare si sono implementati `ModelPublisher` e `ViewPublisher` per la pubblicazione di tutti gli eventi del programma, si è optato per l'utilizzo di due `Publisher` per mantenere la separazione dettata dal MVC.

## Navigator

Come abbiamo già spiegato per la realizzazione della GUI si è optato per l'utilizzo di scalaFX, tuttavia non si voleva creare una dipendenza forte da questa libreria. Per ovviare a questo problema abbiamo realizzato un sistema di navigazione tra le pagine che nascondesse il più possibile le classi di scalaFX al controller.
Il sistema è stato realizzato seguendo il seguente schema:

```mermaid
---
config:
  class:
    hideEmptyMembersBox: true
---
classDiagram

namespace Controller {

class ControllerManager{
<<trait>>
+ gameController() GameController
+ stageController() ControllerStage
+ matchInitController() ControllerMatchInit
+ matchEndController() ControlelrMatchEnd
}
class Navigator~VS<:ViewState~{

<<trait>>
+ navigateTo(viewState: VS)

+ currentState: VS

}

class ControllerStage~VS<:ViewState~{
<<trait>>
+ init()
+ changeScene(newState: VS)
+ currentViewState(): VS
}
}

ControllerManager ..> ControllerStage
ControllerStage *-- Navigator
ControllerStage ..> ViewState

namespace View {
class ViewScene~T~{

<<trait>>
+ content: T

}
class MainStage~T~{

<<trait>>
+ setContent(viewScene: ViewScene~T~)

+ stage: T

}

class ViewFactory~T,VS<:ViewState~{
- controllerManager: ControllerManager
<<trait>>
+ createView(viewState: VS): ViewScene~T~
}

class ViewState{

<<trait>>

}
}
ViewState <.. Navigator

Navigator o-- ViewFactory

MainStage o-- ViewScene

ViewFactory ..> ViewScene

ViewFactory o-- ControllerManager

Navigator <.. MainStage

ViewState <.. ViewFactory
```

Si riporta una breve spiegazione:
- Tipi generici:
    - **T**: Il tipo base della libreria grafica(es: Node per JavaFx, Component per Java Swing).
    - **VS**: Il tipo sottoclasse di `ViewState` che viene accettato da `Navigator` e `ViewFactory`.
- Trait:
    - `ViewState`: Un  trait usato per denotare le classi che possono essere accettate da un `Navigator` o una `ViewFactory`.
    - `Navigator[VS]`: Ha il compito di gestire i cambi di `MainStage`.
    - `MainStage[T]`: Rappresenta il contenitore delle scene.
    - `ViewScene[T]`: Rappresenta una scena della view.
    - `ViewFactory[T, VS]`: Ha il compito di creare le scene concrete.
    - `ControllerManager`: Fornisce alla `ViewFactory` le dipendenze necessarie per creare le `ViewScene`
    - `ControllerStage[VS]`: Wrapper per la navigazione che permette di aggiungere altre operazioni al cambio di scena. Si tratta della classe che viene utilizzata dalla view per richiedere il cambio di scena.

Grazie a questa struttura possiamo gestire la View nascondendo il tipo concreto di T dal resto del controller.