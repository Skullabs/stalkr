package stalkr.html.parser;

import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Element;

import java.lang.reflect.Field;
import java.util.function.BiFunction;

@RequiredArgsConstructor
public class TextElementSetter implements Setter {

	final Field field;
	final String selector;
	final BiFunction<String, Boolean, Object> valueParser;
	final boolean isNonnull;

	@Override
	public void bind( final Element document, final Object instance ) {
		final Element selected = document.select( selector ).first();
		if ( selected == null ){
			if (!isNonnull) return;
			throw new RuntimeException( "Can't find element with selector [" + selector + "]" );
		}
		String value = selected.text();
		value = value == null ? null : value.trim();
		if ( value == null || value.isEmpty() ) {
			if (!isNonnull) return;
			throw new RuntimeException( "Value of element [" + selector + "] is null" );
		}
		set( field, instance, valueParser.apply(value, isNonnull) );
	}

}