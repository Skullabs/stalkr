package stalkr.html.parser;

import org.jsoup.nodes.Element;

import java.lang.reflect.Field;

public interface Setter {

	void bind( final Element document, final Object instance );

	default void set(Field field, Object instance, Object value){
		try {
			field.set( instance, value );
		} catch ( IllegalArgumentException | IllegalAccessException e ) {
			throw new RuntimeException( "Error on set instance value", e );
		}
	}

}