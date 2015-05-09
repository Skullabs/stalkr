package stalkr.http;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHandler;
import com.ning.http.client.Response;

/**
 * Simple {@link AsyncHandler} of type {@link Response}
 */
@Slf4j
@RequiredArgsConstructor
public class AsyncCompletionHandlerBase extends AsyncCompletionHandler<Response> {

	final CompletionListener<Response> listener;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Response onCompleted( Response response ) throws Exception {
		return response;
	}

	/**
	 * {@inheritDoc}
	 */
	/* @Override */
	public void onThrowable( Throwable t ) {
		log.warn( t.getMessage(), t );
		try {
			listener.onComplete( null, null );
		} catch ( Exception e ) {
			log.error( e.getMessage(), e );
		}
	}
}