package stalkr.crawler;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.java.Log;
import stalkr.html.HtmlBinder;
import stalkr.http.Requests;
import trip.spi.Provided;

import com.ning.http.client.Response;

@Log
@RequiredArgsConstructor
public class SeraQueVaiChoverJob implements Job {

	final static String URL = "http://www.seraquevaichover.com.br";
	final CountDownLatch counter;
	String yesOrNot;

	@Provided
	HtmlBinder binder;

	@Override
	public void run( Requests requester ) throws Exception {
		requester.get( URL ).execute( this::handleResponse );
	}

	public void handleResponse( final Requests requester, final Response result ) throws IOException {
		val binded = binder.bind( result.getResponseBody(), SeraQueVaiChover.class );
		yesOrNot = binded.getYesOrNot();
		counter.countDown();
		log.info( "Response received: " + yesOrNot );
	}
}
