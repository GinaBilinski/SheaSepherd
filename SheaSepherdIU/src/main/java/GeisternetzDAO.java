import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class GeisternetzDAO {	
	
	// Speichert ein Geisternetz-Objekt in der Datenbank.
	public void save(Geisternetze geisternetz) {
	    EntityManager entityManager = EMFactory.getEntityManager();
	    EntityTransaction transaction = entityManager.getTransaction();
	    try {
	        transaction.begin(); 
	        // Prüfen, ob die meldende Person bereits existiert und zusammenführen oder neu speichern
	        MeldendePerson meldendePerson = geisternetz.getMeldendePerson();
	        if (meldendePerson != null) {
	            if (meldendePerson.getId() != null && meldendePerson.getId() != 0) {
	                meldendePerson = entityManager.merge(meldendePerson); // Wenn die meldende Person existiert - zusammenführen
	            } else {
	                entityManager.persist(meldendePerson); // Wenn die meldende Person neu ist - speichern
	            }
	            geisternetz.setMeldendePerson(meldendePerson); // Setze die meldende Person im Geisternetz
	        }            
	        entityManager.persist(geisternetz); // Fügt das Geisternetz in die Datenbank ein.
	        transaction.commit(); 
	    } catch (Exception e) {
	        if (transaction.isActive()) {
	            transaction.rollback(); 
	        }
	        throw e; // Exception weitergeworfen, damit sie im Controller abgefangen und eine Fehlermeldung angezeigt wird
	    } finally {
	        entityManager.close(); 
	    }
	}


	// Methode zum Finden einer meldenden Person anhand des Namens und der Telefonnummer
    public MeldendePerson findMeldendePerson(String vorname, String nachname, String telefonnummer) {
    	EntityManager entityManager = EMFactory.getEntityManager();
        try {
            return entityManager.createQuery("SELECT m FROM MeldendePerson m WHERE m.vorname = :vorname AND m.nachname = :nachname AND m.telefonnummer = :telefonnummer", MeldendePerson.class)
            .setParameter("vorname", vorname)
            .setParameter("nachname", nachname)
            .setParameter("telefonnummer", telefonnummer)
            .getSingleResult(); 
        } catch (NoResultException e) {
            return null; // Wenn keine Ergebnisse gefunden wurden, wird null zurückgegeben
        } finally {
        	entityManager.close();
        }
    }

    
    // Aktualisiert ein Geisternetz in der Datenbank
    public void update(Geisternetze geisternetz) {
        EntityManager entityManager = EMFactory.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(geisternetz); 
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback(); 
            }
            throw e; 
        } finally {
            entityManager.close();
        }
    }


    // Gibt eine Liste aller Geisternetze zurück.
    public List<Geisternetze> findAll() {
        EntityManager entityManager = EMFactory.getEntityManager();
        // Erstellt eine Abfrage, die alle Geisternetze-Objekte selektiert und diese nach der ID absteigend sortiert.
        List<Geisternetze> listeGeisternetze = entityManager.createQuery("SELECT g FROM Geisternetze g ORDER BY g.id DESC", Geisternetze.class).getResultList();
        entityManager.close(); 
        return listeGeisternetze;
    }

    
    // Aktualisiert die Liste der Geisternetze durch Löschen des EntityManager-Caches.
    public void neuladenListe() {
    	 EntityManager entityManager = EMFactory.getEntityManager();
    	    entityManager.clear(); // Löscht den Cache des EntityManager, um sicherzustellen, dass alle Entitäten neu geladen werden
    	    entityManager.close(); // Schließt den EntityManager.
    }
    
    
    // Sucht Geisternetze, die durch eine bestimmte Person geborgen werden.
    public List<Geisternetze> findGeisternetz(long personId) {
        EntityManager entityManager = EMFactory.getEntityManager();
        // Erstellt eine Query zur Suche von Geisternetzen, die von einer spezifischen Person geborgen wurden.
        TypedQuery<Geisternetze> abfrage = entityManager.createQuery("SELECT g FROM Geisternetze g WHERE g.status = :status AND g.bergendePerson.id = :personId", Geisternetze.class);
        abfrage.setParameter("status", "Bergung"); 
        abfrage.setParameter("personId", personId);
        List<Geisternetze> ergebnis = abfrage.getResultList();
        entityManager.close(); 
        return ergebnis;  // Gibt die Liste der gefundenen Geisternetze zurück.
    }
}
