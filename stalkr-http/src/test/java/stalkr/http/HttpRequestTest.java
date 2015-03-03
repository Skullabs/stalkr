package stalkr.http;

import java.util.concurrent.CountDownLatch;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.junit.Before;
import org.junit.Test;

import trip.spi.Provided;
import trip.spi.ServiceProvider;

import com.ning.http.client.Response;

public class HttpRequestTest {

	@Provided
	Requests requests;

	@Test( timeout = 20000 )
	@SneakyThrows
	public void ensureThatCouldRequestSomething() {
		printTime( ( ) -> {
			final CountDownLatch counter = new CountDownLatch( 50 );
			final CountdownListener listener = new CountdownListener( counter );
			for ( int i = 0; i < 50; i++ )
				requests.get( "http://pudim.com.br" ).execute( listener );
			counter.await();
		} );
	}

	@Before
	@SneakyThrows
	public void provideDependencies()
	{
		new ServiceProvider().provideOn( this );
	}

	public void printTime( final ThrowableRunnable runnable ) throws Exception
	{
		final long start = System.currentTimeMillis();
		runnable.run();
		final long elapsed = System.currentTimeMillis() - start;
		System.out.println( "Executed in: " + elapsed + "ms" );
	}

	public interface ThrowableRunnable {
		void run() throws Exception;
	}
}

@RequiredArgsConstructor
class CountdownListener implements CompletionListener<Response> {

	final CountDownLatch counter;

	@Override
	@SneakyThrows
	public void onComplete( final Requests requests, final Response response ) {
		counter.countDown();
	}
}