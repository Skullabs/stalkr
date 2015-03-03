package stalkr.http;

public interface CompletionListener<T> {

	void onComplete( final Requests requests, final T response ) throws Exception;
}
