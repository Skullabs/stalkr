package stalkr.html;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@RequiredArgsConstructor
public class RepeatableElementSetter implements Setter {

	final String selector;
	final Field field;
	final BindableClass modelClass;

	@Override
	public void bind( final Element document, final Object instance ) {
		try {
			final Elements elements = document.select( selector );
			final List<Object> list = new ArrayList<Object>();
			for ( final Element element : elements )
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
