package stalkr.http;

import com.ning.http.client.HttpResponseHeaders;

public interface StoredContext {

	void attach( final String key, final Object value );
	Object getAttachedObject( final String key );
	String getCookie( final String key );
	void store( final HttpResponseHeaders headers );
	void apply( final ListenableRequestBuilder builder );
}
