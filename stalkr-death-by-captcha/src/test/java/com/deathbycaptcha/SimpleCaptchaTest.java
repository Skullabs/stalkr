package com.deathbycaptcha;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.deathbycaptcha.Captcha;
import com.deathbycaptcha.Client;
import com.deathbycaptcha.Exception;
import com.deathbycaptcha.SocketClient;

public class SimpleCaptchaTest {

	final String username = System.getProperty( "dbc-username" );
	final String password = System.getProperty( "dbc-password" );

	@Before
	public void ensureCredentialsAreSetBeforeRunTests() {
		assertNotNull( "Username not set", username );
		assertNotNull( "Password not set", password );
	}

	@Test
	public void ensureThatCouldSolveCaptcha() throws FileNotFoundException, IOException, Exception, InterruptedException {
		final Client client = new SocketClient( username, password );
		final File captchaImage = new File( "src/test/resources/captcha.png" );
		final Captcha captcha = client.decode( captchaImage, 20 );
		assertThat( captcha.isSolved(), is( true ) );
		assertThat( captcha.text, is( "bqWma" ) );
	}
}
