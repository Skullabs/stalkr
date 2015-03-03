package stalkr.html;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.jsoup.nodes.Element;

/**
 * Stores the data stripped from classes able to be binded against HTML nodes.
 *
 * @author Miere Teixeira
 */
@RequiredArgsConstructor
class BindableClass {

	final Class<?> type;
	final List<Setter> values;

	public void bind( final Element document, final Object instance ) {
		for ( final Setter setter : values )
			setter.bind( document, instance );
	}

	public Object newInstanceOf() {
		try {
			final Constructor<?> constructor = type.getConstructor();
			constructor.setAccessible( true );
			return constructor.newInstance();
		} catch ( InstantiationException | IllegalAccessException
			| NoSuchMethodException | SecurityException
			| IllegalArgumentException | InvocationTargetException e ) {
			throw new RuntimeException( e );
		}
	}
}