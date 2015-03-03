package stalkr.http;

import java.util.concurrent.ExecutorService;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import com.ning.http.client.AsyncHttpClient;

@Getter
@Accessors( fluent=true )
@RequiredArgsConstructor
public class ContextualRequests extends Requests {

	@NonNull AsyncHttpClient client;
	@NonNull ExecutorService executor;
	@NonNull StoredContext storedContext;

	@Override
	protected ListenableRequestBuilder builder() {
		final ListenableRequestBuilder builder = new ListenableRequestBuilder( this );
		storedContext.apply( builder );
		return builder;
	}
}