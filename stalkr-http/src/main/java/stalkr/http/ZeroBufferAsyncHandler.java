package stalkr.http;

import java.io.OutputStream;

import lombok.RequiredArgsConstructor;

import com.ning.http.client.AsyncHandler;
import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.HttpResponseHeaders;
import com.ning.http.client.HttpResponseStatus;
import com.ning.http.client.Response;
import com.ning.http.client.Response.ResponseBuilder;

@RequiredArgsConstructor
public class ZeroBufferAsyncHandler implements AsyncHandler<Response> {

	final ResponseBuilder builder = new ResponseBuilder();
	final OutputStream stream;

	@Override
	public void onThrowable( final Throwable t ) {
	}

	@Override
	public STATE onBodyPartReceived( final HttpResponseBodyPart bodyPart ) throws Exception {
		bodyPart.writeTo( stream );
		return STATE.CONTINUE;
	}

	@Override
	public STATE onStatusReceived( final HttpResponseStatus responseStatus ) throws Exception {
		builder.accumulate( responseStatus );
		return STATE.CONTINUE;
	}

	@Override
	public STATE onHeadersReceived( final HttpResponseHeaders headers ) throws Exception {
		builder.accumulate( headers );
		return STATE.CONTINUE;
	}

	@Override
	public Response onCompleted() throws Exception {
		return builder.build();
	}
}
