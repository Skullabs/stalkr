package stalkr.crawler;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.java.Log;
import stalkr.html.HtmlBinder;
import stalkr.http.Requests;

import com.ning.http.client.Response;

@Log
@RequiredArgsConstructor
public class MeuIpJob implements Job {

	final static String URL = "http://meuip.co";
	final CountDownLatch counter;
	String ip;

	@Inject
	HtmlBinder binder;

	@Override
	public void run( Requests requester ) throws Exception {
		requester.get( URL ).execute( this::handleResponse );
	}

	public void handleResponse( final Requests requester, final Response result ) throws IOException {
		val binded = binder.bind( result.getResponseBody(), MeuIp.class );
		ip = binded.getIp();
		counter.countDown();
		log.info( "Response received: " + ip );
	}
}
