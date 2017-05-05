package stalkr.http;

import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

import kikaha.core.test.KikahaRunner;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.ning.http.client.Response;

@RunWith( KikahaRunner.class )
public class HttpRequestTest {

	@Inject
	Requests requests;

	@Mock
	CompletionListener<Response> listener;
	
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
		MockitoAnnotations.initMocks( this );
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
	
	@Test
	@SneakyThrows
	public void testUniqueListenerCall(){
		requests.get("http://www.google.com").execute(listener);
		Thread.sleep( 1000 );
		Mockito.verify(listener, Mockito.times(1)).onComplete(Mockito.any(), Mockito.any());
	}
	
	@Test
	@SneakyThrows
	public void testUniqueListenerCallOnError(){
		requests.get("http://notfoundsite.xxx.yyy").execute(listener);
		Thread.sleep( 1000 );
		Mockito.verify(listener, Mockito.times(1)).onComplete(Mockito.any(), Mockito.any());
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