import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;

//Verwalten der Benutzersitzung, einschließlich Login-Status und Ausloggen
@Named
@SessionScoped
public class BenutzerSession implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Speichert die Informationen des eingeloggten Benutzers
    private BergendePerson eingeloggtePerson;

    // Getter und Setter für die eingeloggte Person
    public BergendePerson getEingeloggtePerson() {
        return eingeloggtePerson;
    }

    public void setEingeloggtePerson(BergendePerson eingeloggtePerson) {
        this.eingeloggtePerson = eingeloggtePerson;
    }
    
    // Prüft, ob ein Benutzer eingeloggt ist
    public boolean istEingeloggt() {
        return eingeloggtePerson != null;
    }
    
    // Methode zum Ausloggen des Benutzers
    public String logout() {
        eingeloggtePerson = null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        // Umleitung zur Login-Seite
        return "/login.xhtml?faces-redirect=true";
    }
}
