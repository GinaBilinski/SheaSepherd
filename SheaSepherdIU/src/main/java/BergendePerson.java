import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

//Entity-Klasse für eine bergende Person.
@Entity
public class BergendePerson implements Serializable {
	
	private static final long serialVersionUID = 1L;

	// Primärschlüssel, automatisch generiert.
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private String vorname;
    private String nachname;
    private String telefonnummer;
    private String passwort;
	
	public BergendePerson() {
		
	}

	// Getter und Setter-Methoden
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getTelefonnummer() {
		return telefonnummer;
	}

	public void setTelefonnummer(String telefonnummer) {
		this.telefonnummer = telefonnummer;
	}

	public String getPasswort() {
		return passwort;
	}

	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}
}
