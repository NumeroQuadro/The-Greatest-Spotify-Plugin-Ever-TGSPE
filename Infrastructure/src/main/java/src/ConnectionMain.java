package src;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


public class ConnectionMain {
    private final ConnectionSetUper connectionSetUper = new ConnectionSetUper();
    public void AddNewSongInfo() {
        try {
            SessionFactory sessionFactory = connectionSetUper.setUp();

            SongInfo songInfo = new SongInfo("DENIS CHAT song", "dimon limon");
            try {
                Session session = sessionFactory.openSession();
                session.beginTransaction();
                session.save(songInfo);
                Transaction transaction = session.getTransaction();
                transaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("-------------some troubles with session: " + e.getMessage());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        ConnectionMain connectionMain = new ConnectionMain();
        connectionMain.AddNewSongInfo();
    }
}
