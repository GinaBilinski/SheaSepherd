import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

public class BergendePersonDAO {

	// Speichert eine BergendePerson in der Datenbank.
	public void save(BergendePerson person) {
	    EntityManager entityManager = EMFactory.getEntityManager();
	    EntityTransaction transaction = entityManager.getTransaction();
	    try {
	        transaction.begin(); 
	        entityManager.persist(person); // Fügt die Person in die Datenbank ein
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

	
    // Überprüft, ob eine Telefonnummer bereits in der Datenbank existiert.
    public boolean existiertTelefonnummer(String telefonnummer) {
        EntityManager entityManager = EMFactory.getEntityManager();
        try {
            Long count = entityManager.createQuery("SELECT COUNT(p) FROM BergendePerson p WHERE p.telefonnummer = :telefonnummer", Long.class)
                    .setParameter("telefonnummer", telefonnummer)
                    .getSingleResult();
            return count > 0; // Gibt true zurück, wenn die Telefonnummer existiert, sonst false
        } catch (Exception e) {
            return false;
        } finally {
            entityManager.close(); 
        }
    }
	
    
    // Überprüft die Anmeldedaten (Telefonnummer und Passwort) einer BergendePerson.
    public BergendePerson loginPruefen(String telefonnummer, String passwort) {
        EntityManager entityManager = EMFactory.getEntityManager();
        try { 
            return entityManager.createQuery("SELECT p FROM BergendePerson p WHERE p.telefonnummer = :telefonnummer AND p.passwort = :passwort", BergendePerson.class)
                    .setParameter("telefonnummer", telefonnummer)
                    .setParameter("passwort", passwort)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Gibt null zurück, wenn keine Person gefunden wurde
        } finally {
                entityManager.close();     
        }
    }
}


