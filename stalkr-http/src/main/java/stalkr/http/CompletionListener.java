package stalkr.http;

/**
 * Listen for request that have been completed, even if the request have failed.
 * 
 * @author Miere L. Teixeira
 * @param <T>
 *            the type that represents the answer format the request will expect
 *            to receive.
 */
public interface CompletionListener<T> {

	/**
	 * @param requests
	 *            a {@link Requests} keeping data ( Cookies, etc ) from the
	 *            current session, and ready to make another requests. It could
	 *            be {@code null} if the current request have thrown a timeout
	 *            related exception.
	 * @param response
	 *            the response received by the request, or {@code null} if the
	 *            current request have thrown a timeout related exception.
	 * @throws Exception
	 *             the listener may throw any {@link Exception}s
	 */
	void onComplete( final Requests requests, final T response ) throws Exception;
}
