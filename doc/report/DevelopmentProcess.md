# 1. Processo di sviluppo
Per questo progetto, è stato scelto di lavorare seguendo le modalità previste dalla programmazione _Agile_, in particolare nella modalità _SCRUM-inspired_ descritta nelle regole di progetto. Nelle seguente sezioni verranno descritti i documenti prodotti nel corso dello sviluppo, le fasi in cui è stato diviso il lavoro, e la metodologia adottata per dividersi i task.

Galileo Foschini ha svolto il ruolo di committente, mentre Giulia Bonifazi quello di product owner, durante tutto il corso del progetto, mentre tutti i tre membri hanno svolto il ruolo di programmatori.

## Requisiti e product backlog
All'inizio del progetto, dopo aver ascoltato i requisiti del committente e redatto il documento che li contiene in forma prosaica (che verrà proposto nella sua ultima revisione nel seguente capitolo), è stata svolta una riunione di tutti i membri in veste di programmatori, in cui è stato compilato il product backlog del progetto e sono stati discussi i criteri di qualità da rispettare nel corso dello sviluppo.

## Sprint e sprint backlog
Si è scelto di lavorare in quattro _sprint_ di 15 ore della durata di una o due settimane, per permettere a tutti i membri di raggiungere l'ammontare di lavoro prestabilito. Un meeting è stato svolto all'inizio di ogni sprint– il primo subito dopo aver redatto il product backlog– in cui si fissavano gli obiettivi per il nuovo sprint e si spartivano tra i membri i task necessari al raggiungimento di questi ultimi.

I task sono stati distribuiti sia secondo disponibilità, sia secondo una volontà di permettere a ciascun membro di cimentarsi nella programmazione di tutti gli elementi architetturali del prodotto.

## Meeting giornalieri
Si è scelto di indire meeting giornalieri secondo necessità; ogni membro poteva richiederne uno per uno o più dei seguenti motivi:
- richiesta di chiarimenti su un task da svolgere;
- aggiornamento dopo il completamento di un task;
- dubbi sull'implementazione di un proprio task in corso, spesso risolti in modalità di _pair programming_, lasciando che gli altri due membri osservassero durante la programmazione via condivisione dello schermo.

## Gestione della repository e continuous integration
Il product owner ha detenuto il controllo della repository su GitHub, mentre gli altri due membri hanno effettuato delle fork della repo principale e hanno contribuito per mezzo di pull request. La code review sulle suddette pull request veniva effettuata in autonomia dal product owner, la quale provvedeva all'approvazione a meno di dubbi o richieste di cambiamento relative al codice ricevuto, i quali venivano esplicitati in commenti lasciati nella code review su GitHub o nel corso di uno dei meeting giornalieri.