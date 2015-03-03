package stalkr.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ning.http.client.FluentCaseInsensitiveStringsMap;
import com.ning.http.client.HttpResponseHeaders;
import com.ning.http.client.cookie.Cookie;
import com.ning.http.client.cookie.CookieDecoder;

public class CookieStoredContext implements StoredContext {

	final List<Cookie> cookies = new ArrayList<>();
	final Map<String, Object> attachments = new HashMap<>();

	@Override
	public void store( final HttpResponseHeaders headers ) {
		final FluentCaseInsensitiveStringsMap fluentHeaders = headers.getHeaders();
		final List<String> foundCookies = fluentHeaders.get( "Set-Cookie" );
		if ( foundCookies != null )
			for ( final String cookie : foundCookies )
				cookies.add( CookieDecoder.decode( cookie ) );
	}

	@Override
	public void apply( final ListenableRequestBuilder builder ) {
		for ( final Cookie cookie : cookies )
			builder.addOrReplaceCookie( cookie );
	}

	@Override
	public void attach( final String key, final Object value )
	{
		attachments.put( key, value );
	}

	@Override
	public Object getAttachedObject( final String key )
	{
		return attachments.get( key );
	}

	@Override
	public String getCookie( final String key )
	{
		for ( final Cookie cookie : cookies )
			if ( cookie.getName().equals( key ) )
				return cookie.getValue();
		return null;
	}
}