package stalkr.crawler;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;

import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

import kikaha.core.test.KikahaRunner;
import lombok.SneakyThrows;
import lombok.val;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith( KikahaRunner.class )
public class MeuIpTest {

	@Inject
	JobRunner runner;

	@SneakyThrows
	@Test( timeout = 10000 )
	public void ensureThatCouldDownloadThePendenciesList() {
		val counter = new CountDownLatch( 1 );
		val tjSc = spy( new MeuIpJob( counter ) );
		runner.run( tjSc );
		counter.await();
		assertTrue( tjSc.ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}.\\d{1,3}") );
	}

}
