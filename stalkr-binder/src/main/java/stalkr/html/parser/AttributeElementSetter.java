package stalkr.html.parser;

import java.lang.reflect.Field;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import lombok.val;

import org.jsoup.nodes.Element;

@RequiredArgsConstructor
public class AttributeElementSetter implements Setter {

	final Field field;
	final String selector;
	final String attribute;
	final Function<String, Object> valueParser;
	final boolean isNonnull;

	@Override
	public void bind( final Element document, final Object instance ) {
		try {
			val element = document.select( selector ).first();
			if ((element == null || element.attr(attribute).isEmpty()) && isNonnull  ) {
				throw new RuntimeException("The element is null, please make this field optional");
			}else if((element == null || element.attr(attribute).isEmpty()) && !isNonnull  ) {
				return;
			}
			field.set(instance, valueParser.apply(element.attr(attribute)));
		} catch ( IllegalArgumentException | IllegalAccessException e ) {
			throw new RuntimeException( e );
		}
	}

}
