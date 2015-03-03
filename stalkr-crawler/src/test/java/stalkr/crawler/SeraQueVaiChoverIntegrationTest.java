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

public class SeraQueVaiChoverIntegrationTest {

	@Provided
	JobRunner runner;

	@SneakyThrows
	@Test( timeout = 20000 )
	public void ensureThatCouldDownloadThePendenciesList() {
		val counter = new CountDownLatch( 1 );
		val tjSc = spy( new SeraQueVaiChoverJob( counter ) );
		runner.run( tjSc );
		counter.await();
		assertEquals( "não", tjSc.yesOrNot );
	}

	@Before
	@SneakyThrows
	public void provideDependencies() {
		new ServiceProvider().provideOn( this );
	}
}
