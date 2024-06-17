import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

//Definiert eine Entitätsklasse, die Daten über Geisternetze speichert.
@Entity
public class Geisternetze implements Serializable {
    
    private static final long serialVersionUID = 1L;

    // Primärschlüssel, automatisch generiert.
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
  
    private Double breitengrad;
    private Double laengengrad;
    private int groesse;
    private String status;
    
    @Temporal(TemporalType.DATE)
    private Date gesichtetAm;
    
    // Verbindet jedes Geisternetz mit einer meldenden Person
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "meldendePersonId")
    private MeldendePerson meldendePerson;
    
    // Verbindet jedes Geisternetz mit einer bergenden Person
    @ManyToOne
    @JoinColumn(name = "bergendePersonId") // Referenz auf den Fremdschlüssel in der Datenbank
    private BergendePerson bergendePerson;  
    
    // Standardkonstruktor
    public Geisternetze() {
    	
    }

    //Getter und Setter Methoden
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Double getBreitengrad() {
		return breitengrad;
	}

	public void setBreitengrad(Double breitengrad) {
		this.breitengrad = breitengrad;
	}

	public Double getLaengengrad() {
		return laengengrad;
	}

	public void setLaengengrad(Double laengengrad) {
		this.laengengrad = laengengrad;
	}

	public int getGroesse() {
		return groesse;
	}

	public void setGroesse(int groesse) {
		this.groesse = groesse;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}    
	
    public Date getGesichtetAm() {
        return gesichtetAm;
    }

    public void setGesichtetAm(Date gesichtetAm) {
        this.gesichtetAm = gesichtetAm;
    }
		
		
    // Getter und Setter für meldende und bergende Person
	public MeldendePerson getMeldendePerson() {
		return meldendePerson;
    }
	
	public void setMeldendePerson(MeldendePerson meldendePerson) {
		this.meldendePerson = meldendePerson;
	}
	     
	
	    
    public BergendePerson getBergendePerson() {
        return bergendePerson;
    }

    public void setBergendePerson(BergendePerson bergendePerson) {
        this.bergendePerson = bergendePerson;
    }
}
