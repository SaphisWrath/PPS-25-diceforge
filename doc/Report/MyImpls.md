### Resource
> Tutti i tipi di risorsa specifici nel gioco sono implementati con record immutabili
> che implementano il trait **Resource**. Tale trait tiene traccia della quantità
> di una risorsa e fornisce un metodo _copy_ per creare una nuova risorsa dello stesso tipo.
> Tale metodo è fondamentale per implementare operazioni fra **Resource** compatibili
> con qualunque oggetto che implementa **Resource**, fra cui la somma e differenza di due risorse.
> Il concetto di limite massimo di una risorsa è separato dal concetto di risorsa base
> e catturato dal trait **ResourceWithCap**. L'implementazione effettiva sfrutta
> il pattern _decorator_, quindi estende **Resource** e si appoggia a un'altra risorsa data
> a costruzione per calcolare la quantità della risorsa tenendo conto del limite massimo stabilito.

### EffectManager
> **EffectManager** è l'oggetto che si occupa della risoluzione di un insieme di effetti
> a volte legati fra loro. Questo oggetto è un singleton per renderlo facilmente accessibile
> da tutti i punti del codice, fra cui **Mission**, **Effect**, e **GameController**.
> Fra i metodi esposti ci sono _attemptSolve_ e _effectsToSolve_. Il primo riceve degli effetti
> legati ai giocatori e prova a risolverli tutti in fasi separate a seconda dei tipi di effetto.
> Se arriva in fondo comunica la riuscita della risoluzione, altrimenti salva internamente
> gli effetti non rilevanti per la fase corrente e comunica la necessità di un intervento esterno
> per specificare cosa faranno gli effetti variabili. Anche tali effetti sono salvati in EffectManager
> e sono accessibili tramite _effectsToSolve_. Infine **EffectManager** fornisce un metodo
> per aggiornare gli effetti correnti senza attivarli, e un metodo per specificare un modulo
> di risoluzione dei **ResourceEffects** da applicare per la prossima risoluzione.

### ChoiceController
> Uno dei problemi riscontrati nel progetto è stato la gestione di scelte da parte dell'utente
> che devono interrompere un'operazione e riprenderla una volta ottenuti i risultati.
> Il trait **ChoiceController** è stato pensato per risolvere tale problema insieme a **ChoiceWindow** nella view.
> **ChoiceController** permette di leggere le scelte in attesa e di riprendere l'esecuzione
> dell'operazione prendendo in considerazione i risultati dati dall'utente. Tale trait è generico
> sul tipo di scelte che l'utente deve eseguire, il che lo rende facilmente adattabile
> a diverse implementazioni, per esempio **EffectSolveController**, il quale è collegato
> a **EffectManager** e ha quindi accesso agli effetti non risolti e può richiamare EffectManager
> con i nuovi risultati dall'utente per concludere la risoluzione.
