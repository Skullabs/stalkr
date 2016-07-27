package stalkr.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import lombok.Getter;
import lombok.val;
import lombok.experimental.Accessors;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.AsyncHttpClientConfig.Builder;

@Getter
@Accessors( fluent = true )
@Singleton
public class DefaultRequestFactory extends Requests {

	final AsyncHttpClient client = configureClient();
	final StoredContext storedContext = null;
	final ExecutorService executor = Executors.newCachedThreadPool();

	AsyncHttpClient configureClient() {
		val config = createHttpClientConfig();
		return new AsyncHttpClient( config );
	}

	AsyncHttpClientConfig createHttpClientConfig() {
		return new Builder()
			.setCompressionEnforced( true )
			.setAllowPoolingConnections( true )
			.setExecutorService( executor )
			.setRequestTimeout( 30000 )
			.build();
	}

	@Override
	protected ListenableRequestBuilder builder() {
		val requests = new ContextualRequests( client, executor, new CookieStoredContext() );
		return new ListenableRequestBuilder( requests );
	}

}