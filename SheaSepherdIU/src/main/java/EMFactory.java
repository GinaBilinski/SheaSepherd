import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

// Zentrale Stelle zur Verwaltung von EntityManager Instanzen 
public class EMFactory {
	// Erstellt ein EntityManagerFactory-Objekt für die Persistenzeinheit "SheaSepherdUnit".
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("SheaSepherdUnit");

    // Statische Methode, um eine EntityManager-Instanz zu erhalten.
    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
    
    // Statische Methode zum Schließen der EntityManagerFactory.
    public static void close() {
        entityManagerFactory.close();
    }
}
