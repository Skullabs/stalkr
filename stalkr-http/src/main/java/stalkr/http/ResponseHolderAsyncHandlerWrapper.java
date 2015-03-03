package stalkr.http;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import com.ning.http.client.AsyncHandler;
import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.HttpResponseHeaders;
import com.ning.http.client.HttpResponseStatus;

@Getter
@Accessors( fluent = true )
@RequiredArgsConstructor
class ResponseHolderAsyncHandlerWrapper<T> implements AsyncHandler<T> {

	final AsyncHandler<T> wrapped;
	final StoredContext storedContext;

	@Override
	public void onThrowable( final Throwable t ) {
		wrapped.onThrowable( t );
	}

	@Override
	public STATE onBodyPartReceived( final HttpResponseBodyPart bodyPart ) throws Exception {
		return wrapped.onBodyPartReceived( bodyPart );
	}

	@Override
	public STATE onStatusReceived( final HttpResponseStatus responseStatus ) throws Exception {
		return wrapped.onStatusReceived( responseStatus );
	}

	@Override
	public STATE onHeadersReceived( final HttpResponseHeaders headers ) throws Exception {
		storedContext.store( headers );
		return wrapped.onHeadersReceived( headers );
	}

	@Override
	public T onCompleted() throws Exception {
		return wrapped.onCompleted();
	}
}