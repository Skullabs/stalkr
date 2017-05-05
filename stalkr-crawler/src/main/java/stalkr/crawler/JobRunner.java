package stalkr.crawler;

import javax.inject.Inject;
import javax.inject.Singleton;

import kikaha.core.cdi.DefaultCDI;
import kikaha.core.cdi.ProviderContext;
import stalkr.http.Requests;

@Singleton
public class JobRunner {

	@Inject
	ProviderContext provider;

	@Inject
	Requests requester;

	@Inject
	ErrorHandler errorHandler;

	public void run( final Job job ) {
		try {
			//XXX: verificar se esta é a forma correta de injetar as dependencias...
			new DefaultCDI().injectOn( job );
			job.run( requester );
		} catch ( final Exception cause ) {
			errorHandler.handle( cause );
		}
	}
}
