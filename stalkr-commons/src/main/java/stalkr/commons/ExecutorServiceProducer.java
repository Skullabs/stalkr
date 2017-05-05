package stalkr.commons;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import kikaha.core.cdi.ProviderContext;
import lombok.val;

@Singleton
public class ExecutorServiceProducer {

	final Map<String, ExecutorService> executors = new HashMap<>();

	@Produces
	public ExecutorService produceExecutorService( final ProviderContext context ) {
		val name = retrieveNameFrom( context );
		val executor = retrieveOrCreateExecutorForName( name );
		return executor;
	}

	String retrieveNameFrom( final ProviderContext context ) {
		val name = context.getAnnotation( Name.class );
		if ( name != null )
			return name.value();
		throw new IllegalStateException( "Nothing produced: no name provided" );
	}

	ExecutorService retrieveOrCreateExecutorForName( final String name ) {
		ExecutorService executor = executors.get( name );
		if ( executor == null ) {
			synchronized ( executors ) {
				executor = executors.get( name );
				if ( executor == null )
					executors.put( name, executor = createExecutorService( name ) );
			}
		}
		return executor;
	}

	ExecutorService createExecutorService( final String name ) {
		return Executors.newCachedThreadPool();
	}
}
