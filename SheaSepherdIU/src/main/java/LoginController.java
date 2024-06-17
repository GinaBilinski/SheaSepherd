
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

// Definiert eine Bean, die für die gesamte Dauer der Anwendung verfügbar bleibt
@Named
@ApplicationScoped
public class LoginController {
	
	// Java-Klasse erhält automatisch eine Instanz von BergendePersonDAO & BenutzerSession.
    @Inject
    private BergendePersonDAO bergendePersonDAO;  // DAO-Klasse für den Zugriff auf Personendaten
    
    @Inject
    private BenutzerSession benutzerSession; // Verwaltet Benutzersitzungsdaten   
     
    // Erstellt eine Instanz von BergendePerson, die zur Bearbeitung der Daten verwendet wird
    private BergendePerson bergendePerson = new BergendePerson();
 
    // Getter und Setter für BergendePerson
    public BergendePerson getBergendePerson() {
        return bergendePerson;
    }

    public void setBergendePerson(BergendePerson bergendePerson) {
        this.bergendePerson = bergendePerson;
    }

    // Speichert eine Person in der Datenbank
    public String savePerson() {
        FacesContext context = FacesContext.getCurrentInstance(); 
        // überprüft, ob die Telefonnummer bereits existiert und zeigt Fehlemeldung an
        if (bergendePersonDAO.existiertTelefonnummer(bergendePerson.getTelefonnummer())) { 
            context.addMessage("registrierung:telefonnummer", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registrierung fehlgeschlagen", " Telefonnummer bereits vergeben."));
            return null; 
        }
        try {
            bergendePersonDAO.save(bergendePerson);
            bergendePerson = new BergendePerson(); 
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Ihre Registrierung war erfolgreich."));
            return null; 
        } catch (Exception e) {
            // Fehlermeldung, wenn ein Fehler beim Speichern aufgetreten ist
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registrierung fehlgeschlagen", "Ein Fehler ist aufgetreten."));
            return null; 
        }
    }

    
    // Überprüft die Anmeldedaten und loggt den Benutzer ein, wenn sie gültig sind
    public String loginPerson() {
    	// Versucht, den Benutzer mit Telefonnummer und Passwort zu authentifizieren.
        BergendePerson personDaten = bergendePersonDAO.loginPruefen(bergendePerson.getTelefonnummer(), bergendePerson.getPasswort());
        FacesContext context = FacesContext.getCurrentInstance();
        if (personDaten != null) {
        	// Speichert die authentifizierte Person in der Benutzersession
            benutzerSession.setEingeloggtePerson(personDaten);
            return "geisternetzeBergen?faces-redirect=true";
        } else {
        	// Fügt eine Fehlermeldung hinzu, wenn Authentifizierung fehlschlägt.
            context.addMessage("login:telefonnummer", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login fehlgeschlagen", "- Ungültige Telefonnummer oder Passwort."));
            return null; 
        }
    }
}


