package stalkr.html.parser;

import java.lang.reflect.Field;
import java.util.ArrayList;

import lombok.RequiredArgsConstructor;
import lombok.val;

import org.jsoup.nodes.Element;

@RequiredArgsConstructor
public class RepeatableElementSetter implements Setter {

	final String selector;
	final Field field;
	final BindableClass modelClass;

	@Override
	public void bind( final Element document, final Object instance ) {
		try {
			val elements = document.select( selector );
			val list = new ArrayList<Object>();
			for ( val element : elements )
				list.add( createNewBinddedDataFromModelClass( element ) );
			field.set( instance, list );
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
