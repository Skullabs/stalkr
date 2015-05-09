package stalkr.http;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.ning.http.client.ListenableFuture;

@Slf4j
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
			log.error( e.getMessage() );
			e.printStackTrace();
			callCompletionListenerForError();
		}
	}

	void callCompletionListenerForError() {
		try {
			log.warn( "Trying to recovery from failure..." );
			listener.onComplete( requests, null );
		} catch ( Exception e ) {
			log.error( e.getMessage() );
			e.printStackTrace();
		}
	}
}