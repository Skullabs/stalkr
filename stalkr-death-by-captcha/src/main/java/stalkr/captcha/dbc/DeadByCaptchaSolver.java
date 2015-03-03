package stalkr.captcha.dbc;

import java.util.concurrent.ExecutorService;

import lombok.RequiredArgsConstructor;
import stalkr.commons.Name;
import stalkr.crawler.ErrorHandler;
import trip.spi.Provided;
import trip.spi.Stateless;

import com.deathbycaptcha.Captcha;
import com.deathbycaptcha.Client;

@Stateless
public class DeadByCaptchaSolver {

	@Provided
	Client client;

	@Provided
	@Name( "captcha-client-thread-pool" )
	ExecutorService executor;

	@Provided
	ErrorHandler errorHandler;

	public void solve( final byte[] inputStream, final SolverCallback callback ) {
		solve( inputStream, callback, errorHandler );
	}

	public void solve( final byte[] inputStream, final SolverCallback callback, final ErrorHandler errorHandler ) {
		final AsyncCaptchaRunner runner = new AsyncCaptchaRunner( inputStream, callback, errorHandler );
		executor.execute( runner );
	}

	@RequiredArgsConstructor
	class AsyncCaptchaRunner implements Runnable {

		final byte[] inputStream;
		final SolverCallback callback;
		final ErrorHandler errorHandler;

		@Override
		public void run() {
			try {
				final Captcha decode = client.decode( inputStream );
				callback.run( decode.text );
			} catch ( final Exception e ) {
				errorHandler.handle( e );
			}
		}
	}
}