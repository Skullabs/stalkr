package stalkr.crawler;

import lombok.extern.java.Log;
import trip.spi.Singleton;

@Log
@Singleton( exposedAs = ErrorHandler.class )
public class LogErrorHandlerCallback implements ErrorHandler {

	@Override
	public void handle( final Throwable cause ) {
		log.severe( cause.getMessage() );
		// if ( log.isLoggable( Level.FINE ) )
			cause.printStackTrace();
	}
}