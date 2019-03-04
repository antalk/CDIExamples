package nl.antalk.examples;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CdiRunner.class)
public class TestAutocloseConnection {

    private EntityManagerFactory emf;

	@PostConstruct
	void init() {
	    emf = Persistence.createEntityManagerFactory("PUNIT");
	}
	
	@Test
	public void testAutoclose() {
		final EntityManager em = emf.createEntityManager();
		Session session  = (Session)em.getDelegate();
		session.doWork(new Work() {

			@Override
			public void execute(Connection connection) throws SQLException {
				try (Connection copycon = connection; ResultSet rs = copycon.prepareStatement("SELECT 1").executeQuery();){
				//try (ResultSet rs = connection.prepareStatement("SELECT 1").executeQuery();){
					if (rs.next()) {
						
					}
				} catch (Exception e) {
					e.printStackTrace();
					Assert.fail(e.getMessage());
				}
				
				if (!connection.isClosed()) {
					Assert.fail("connection is still open !");
				}
				
			}
			
		});
	}
}
