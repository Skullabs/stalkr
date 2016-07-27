package stalkr.crawler;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;

import java.util.concurrent.CountDownLatch;

import lombok.SneakyThrows;
import lombok.val;

import org.junit.Before;
import org.junit.Test;

import trip.spi.Provided;
import trip.spi.ServiceProvider;

public class MeuIpTest {

	@Provided
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

	@Before
	@SneakyThrows
	public void provideDependencies() {
		new ServiceProvider().provideOn( this );
	}
}
