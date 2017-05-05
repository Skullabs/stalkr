package stalkr.captcha.dbc;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import com.deathbycaptcha.Client;
import com.deathbycaptcha.SocketClient;

@Singleton
public class DeadByCaptchaClientProducer {

	final String username = System.getProperty( "dbc-username", "" );
	final String password = System.getProperty( "dbc-password", "" );

	@Produces
	public Client produceClient() {
		if ( username.isEmpty() || password.isEmpty() )
			throw new IllegalArgumentException( "Credentials for DeadByCaptcha are not set" );
		return new SocketClient( username, password );
	}
}