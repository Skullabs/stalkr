package stalkr.crawler;

import javax.inject.Singleton;

import lombok.extern.java.Log;

@Log
@Singleton
public class LogErrorHandlerCallback implements ErrorHandler {

	@Override
	public void handle( final Throwable cause ) {
		log.severe( cause.getMessage() );
		// if ( log.isLoggable( Level.FINE ) )
			cause.printStackTrace();
	}
}