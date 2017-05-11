package stalkr.html.parser;

import java.lang.reflect.Field;

import lombok.RequiredArgsConstructor;

import org.jsoup.nodes.Element;

@RequiredArgsConstructor
public class TextElementSetter implements Setter {

	final Field field;
	final String selector;
	final ValueParser valueParser;

	@Override
	public void bind( final Element document, final Object instance ) {
		try {
			final Element selected = document.select( selector ).first();
			field.set( instance, valueParser.parse(selected.text()) );
		} catch ( IllegalArgumentException | IllegalAccessException e ) {
			throw new RuntimeException( e );
		}
	}
	
}