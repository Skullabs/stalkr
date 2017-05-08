package stalkr.html.parser;

import java.lang.reflect.Field;

import lombok.RequiredArgsConstructor;
import lombok.val;

import org.jsoup.nodes.Element;

@RequiredArgsConstructor
public class EmbeddedElementSetter implements Setter {

	final String selector;
	final Field field;
	final BindableClass modelClass;

	@Override
	public void bind( final Element document, final Object instance ) {
		try {
			val elements = document.select( selector );
			if ( elements.isEmpty() )
				throw new RuntimeException("No element found for selector: " + selector);
			val object = createNewBinddedDataFromModelClass( elements.first() );
			field.set( instance, object );
		} catch ( IllegalArgumentException | IllegalAccessException e ) {
			throw new RuntimeException( e );
		}
	}

	Object createNewBinddedDataFromModelClass( final Element element ) {
		final Object newInstance = modelClass.newInstanceOf();
		modelClass.bind( element, newInstance );
		return newInstance;
	}
}
