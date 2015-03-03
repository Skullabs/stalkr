package stalkr.http;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import com.ning.http.client.ListenableFuture;

@Log
@RequiredArgsConstructor
class CompletionListenerWrapper<T> implements Runnable {

	final CompletionListener<T> listener;
	final ListenableFuture<T> future;
	final Requests requests;

	@Override
	public void run() {
		try {
			final T response = future.get();
			listener.onComplete( requests, response );
		} catch ( final Exception e ) {
			log.severe( e.getMessage() );
			e.printStackTrace();
		}
	}
}