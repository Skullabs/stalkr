package stalkr.html.parser;

import java.lang.reflect.Field;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;

import lombok.val;
import org.jsoup.nodes.Element;

@RequiredArgsConstructor
public class TextElementSetter implements Setter {

	final Field field;
	final String selector;
	final Function<String, Object> valueParser;
	final boolean isNonnull;

	@Override
	public void bind( final Element document, final Object instance ) {
		try {
			final Element selected = document.select( selector ).first();
			val value = selected.text();
			if(value.isEmpty() && isNonnull)
				throw new RuntimeException("The element can't be null");
			else if(value.isEmpty() && !isNonnull)
				return;
			field.set( instance, valueParser.apply(value) );
		} catch ( IllegalArgumentException | IllegalAccessException e ) {
			throw new RuntimeException( e );
		}
	}
}