package stalkr.html.parser;

import java.lang.reflect.Field;

import lombok.RequiredArgsConstructor;
import lombok.val;

import org.jsoup.nodes.Element;

@RequiredArgsConstructor
public class AttributeElementSetter implements Setter {

	final Field field;
	final String selector;
	final String attribute;

	@Override
	public void bind( final Element document, final Object instance ) {
		try {
			val element = document.select( selector ).first();
			if ( element != null )
				field.set( instance, element.attr( attribute ) );
		} catch ( IllegalArgumentException | IllegalAccessException e ) {
			throw new RuntimeException( e );
		}
	}

}
