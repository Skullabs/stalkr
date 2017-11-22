package stalkr.html.parser;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jsoup.nodes.Element;

import java.lang.reflect.Field;
import java.util.function.BiFunction;

@RequiredArgsConstructor
public class AttributeElementSetter implements Setter {

	final Field field;
	final String selector;
	final String attribute;
	final BiFunction<String, Boolean, Object> valueParser;
	final boolean isNonnull;

	@Override
	public void bind( final Element document, final Object instance ) {
		val element = document.select( selector ).first();
		if ( element == null ){
			if (!isNonnull) return;
			throw new RuntimeException( "Can't find element with selector [" + selector + "]" );
		}
		String value = element.attr(attribute);
		value = value == null ? null : value.trim();
		if ( value == null || value.isEmpty() ) {
			if (!isNonnull) return;
			throw new RuntimeException( "Value of attribute [" + attribute + "] on element [" + selector + "] is null" );
		}
		set( field, instance, valueParser.apply(value, isNonnull) );
	}

}
