package stalkr.html.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor( staticName = "of" )
public class FieldParser<T extends Annotation> {

	final Class<T> annotation;
	final SetterConstructor<T> constructor;

	public void parse( final List<Setter> list, final Field field ) {
		val textAnnotation = field.getAnnotation( annotation );
		if ( textAnnotation != null )
			list.add( constructor.construct( field, textAnnotation ) );
	}
}
