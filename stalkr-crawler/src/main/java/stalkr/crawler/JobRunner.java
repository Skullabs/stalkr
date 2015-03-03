package stalkr.crawler;

import stalkr.http.Requests;
import trip.spi.Provided;
import trip.spi.ServiceProvider;
import trip.spi.Singleton;

@Singleton
public class JobRunner {

	@Provided
	ServiceProvider provider;

	@Provided
	Requests requester;

	@Provided
	ErrorHandler errorHandler;

	public void run( final Job job ) {
		try {
			provider.provideOn( job );
			job.run( requester );
		} catch ( final Exception cause ) {
			errorHandler.handle( cause );
		}
	}
}
