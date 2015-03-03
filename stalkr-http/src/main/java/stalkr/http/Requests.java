package stalkr.http;

public abstract class Requests {

	protected abstract ListenableRequestBuilder builder();

	public abstract StoredContext storedContext();

	public ListenableRequestBuilder get( final String uri ) {
		return builder().setMethod( "GET" ).setUrl( uri );
	}

	public ListenableRequestBuilder post( final String uri ) {
		return builder().setMethod( "POST" ).setUrl( uri );
	}

	public ListenableRequestBuilder put( final String uri ) {
		return builder().setMethod( "PUT" ).setUrl( uri );
	}

	public ListenableRequestBuilder delete( final String uri ) {
		return builder().setMethod( "DELETE" ).setUrl( uri );
	}
}