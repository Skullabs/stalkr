package stalkr.http;

import java.io.IOException;

import lombok.val;

import com.ning.http.client.RequestBuilderBase;
import com.ning.http.client.Response;

public class ListenableRequestBuilder extends RequestBuilderBase<ListenableRequestBuilder> {

	final ContextualRequests requests;

	public ListenableRequestBuilder( final ContextualRequests requests ) {
		super( ListenableRequestBuilder.class, "GET", false );
		this.requests = requests;
	}

	public ListenableRequestBuilder execute( final CompletionListener<Response> listener ) throws IOException {
		val handler = new AsyncCompletionHandlerBase( requests, listener );
		val responseWrapper = new ResponseHolderAsyncHandlerWrapper<Response>( handler, requests.storedContext() );
		requests.client().executeRequest( build(), responseWrapper );
		return this;
	}

}