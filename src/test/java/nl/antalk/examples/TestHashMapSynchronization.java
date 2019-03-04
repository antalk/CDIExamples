package nl.antalk.examples;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import nl.antalk.examples.model.ApplicationEvent;
import nl.antalk.examples.model.ApplicationEvent.ACTION;

/**
 * CDI / {@link ExecutorService} in combination with synchronized maps
 * 
 * @author antalk
 *
 */
@RunWith(CdiRunner.class)
public class TestHashMapSynchronization {
	
	private final Map<Integer, String> numberMap = new HashMap<>(1000,2);
	
	@Inject
	private Event<ApplicationEvent> appEventService;
		
	private ExecutorService executor;
	
	
	@Test
	public void testHashMaps() {
		executor = Executors.newFixedThreadPool(10);
		for (int i=0;i<1000;i++) {
			
			synchronized (numberMap) {
				String uuid = UUID.randomUUID().toString();
				numberMap.put(Integer.valueOf(i),uuid );
				appEventService.fire(new ApplicationEvent(ACTION.notifyListeners,i ));
				System.err.println("zzZZzzz" + Thread.currentThread().getId());
			}
		}
		System.err.println(numberMap);
	}
	
	public void handleEvents(@Observes final ApplicationEvent event) {
		Integer justAddedKey = (Integer) event.getEventParam()[0];
		executor.execute(new EventHandler(justAddedKey));
	}
	
	public class EventHandler implements Runnable {
		
		final Integer key;
		
		EventHandler(final Integer key) {
			this.key = key;
		}

		@Override
		public void run() {
			System.err.println("Handled in thread " + Thread.currentThread().getId() );
			numberMap.get(this.key).length();
		}
		
	}
	
}
