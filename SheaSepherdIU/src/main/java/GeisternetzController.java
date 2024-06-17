import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Definiert eine Bean, die für die gesamte Dauer der Anwendung verfügbar bleibt
@Named
@ApplicationScoped
public class GeisternetzController {

	// Java-Klasse erhält automatisch eine Instanz von GeisternetzDAO und BenutzerSession erhält.
    @Inject
    private GeisternetzDAO geisternetzDAO;

    @Inject
    private BenutzerSession benutzerSession;

    private Geisternetze geisternetz = new Geisternetze(); // Instanz von Geisternetze für das Formular
    private MeldendePerson meldendePerson = new MeldendePerson(); // Instanz von MeldendePerson für das Formular

    // Map zur Speicherung ausgewählter Statuswerte
    private Map<Integer, String> ausgewaehlteStatusMap = new HashMap<>();

    public GeisternetzController() {
    	
    }

    // Gibt das aktuelle Datum zurück 
    public Date getHeutigesDatum() {
        return new Date();
    }
    
    // Methode zum Abrufen der Liste aller Geisternetze
    public List<Geisternetze> getGeisternetzeList() {
        return geisternetzDAO.findAll();
    }
  

    // Getter und Setter
    public Geisternetze getGeisternetz() {
        return geisternetz;
    }

    public void setGeisternetz(Geisternetze geisternetz) {
        this.geisternetz = geisternetz;
    }

    public MeldendePerson getMeldendePerson() {
        return meldendePerson;
    }

    public void setMeldendePerson(MeldendePerson meldendePerson) {
        this.meldendePerson = meldendePerson;
    }

    public Map<Integer, String> getAusgewaehlteStatusMap() {
        return ausgewaehlteStatusMap;
    }

    public void setAusgewaehlteStatusMap(Map<Integer, String> ausgewaehlteStatusMap) {
        this.ausgewaehlteStatusMap = ausgewaehlteStatusMap;
    }
  
   
    //  Methode gibt die Geisternetze zurück, bei denen die eingeloggte Person "Bergung" ausgewählt hat
    public List<Geisternetze> getGeisternetzeBergendePerson() {
        if (!benutzerSession.istEingeloggt()) {
            return new ArrayList<>(); // Leere Liste, wenn niemand eingeloggt ist
        }      
        BergendePerson eingeloggtePerson = benutzerSession.getEingeloggtePerson();
        return geisternetzDAO.findGeisternetz(eingeloggtePerson.getId());
    }
    
     
    // Methode, die bei Tab-Wechsel aufgerufen wird, um die Listen zu aktualisieren
    public void aktualisiereListen() {
        geisternetzDAO.neuladenListe();
    }
    
    
    // Methode zum Speichern eines Geisternetzes mit der meldenden Person
    public String saveGeisternetz() {
        FacesContext context = FacesContext.getCurrentInstance();        
        // Prüfen, ob die meldende Person bereits existiert
        MeldendePerson existingPerson = geisternetzDAO.findMeldendePerson(meldendePerson.getVorname(), meldendePerson.getNachname(), meldendePerson.getTelefonnummer());
        if (existingPerson != null) {
            geisternetz.setMeldendePerson(existingPerson);
        } else {
            geisternetz.setMeldendePerson(meldendePerson);
        }
        geisternetz.setStatus("Gemeldet");
        try {
            geisternetzDAO.save(geisternetz); 
            geisternetz = new Geisternetze();
            meldendePerson = new MeldendePerson();  
            return "gemeldet?faces-redirect=true"; 
        } catch (Exception e) {
            // Fehlermeldung, wenn ein Fehler beim Speichern aufgetreten ist
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler", "Das Geisternetz konnte nicht gespeichert werden."));
            return null;
        }
    }

    
    // Methode zum Aktualisieren des Status eines Geisternetzes
    public void statusAktualisieren(Geisternetze geisternetz) {
        FacesContext context = FacesContext.getCurrentInstance(); 
        try {
            // Holt den ausgewählten Status aus der Map basierend auf der ID des Geisternetzes
            String ausgewaehlterStatus = ausgewaehlteStatusMap.get(geisternetz.getId());
            // Überprüft, ob ein gültiger Status ausgewählt wurde
            if (ausgewaehlterStatus != null && !"NONE".equals(ausgewaehlterStatus)) {          
                BergendePerson eingeloggtePerson = benutzerSession.getEingeloggtePerson();           
                if (eingeloggtePerson != null) {
                    geisternetz.setStatus(ausgewaehlterStatus); // Aktualisiert den Status des Geisternetzes
                    geisternetz.setBergendePerson(eingeloggtePerson); // Setzt die eingeloggte Person als bergende Person
                    geisternetzDAO.update(geisternetz); // Speichert die Änderungen in der Datenbank
                }
                ausgewaehlteStatusMap.put(geisternetz.getId(), "NONE");
            }
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler", "Der Status konnte nicht aktualisiert werden."));
        }
    }
}
