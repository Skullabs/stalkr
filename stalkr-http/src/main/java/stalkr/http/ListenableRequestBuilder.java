package stalkr.http;

import java.io.IOException;

import lombok.val;

import com.ning.http.client.AsyncCompletionHandlerBase;
import com.ning.http.client.AsyncHandler;
import com.ning.http.client.RequestBuilderBase;
import com.ning.http.client.Response;

public class ListenableRequestBuilder extends RequestBuilderBase<ListenableRequestBuilder> {

	final ContextualRequests requests;

	public ListenableRequestBuilder( final ContextualRequests requests ) {
		super( ListenableRequestBuilder.class, "GET", false );
		this.requests = requests;
	}

	public ListenableRequestBuilder execute( final CompletionListener<Response> listener ) throws IOException {
		return execute( new AsyncCompletionHandlerBase(), listener );
	}

	public <T> ListenableRequestBuilder execute( final AsyncHandler<T> handler, final CompletionListener<T> listener ) throws IOException {
		val responseWrapper = new ResponseHolderAsyncHandlerWrapper<T>( handler, requests.storedContext() );
		val future = requests.client().executeRequest( build(), responseWrapper );
		val completionListenerWrapper = new CompletionListenerWrapper<T>( listener, future, requests );
		future.addListener( completionListenerWrapper, requests.executor() );
		return this;
	}
}