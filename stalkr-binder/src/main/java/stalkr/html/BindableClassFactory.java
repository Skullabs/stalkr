package stalkr.html;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.val;
import trip.spi.Singleton;

@Singleton
public class BindableClassFactory {

	final Map<Class<?>, BindableClass> cache = new ConcurrentHashMap<>();

	BindableClass createNewBindableClass( final Class<?> clazz ) {
		final List<Setter> fields = stripBindableFieldsOfClazz( clazz );
		return new BindableClass( clazz, fields );
	}

	List<Setter> stripBindableFieldsOfClazz( Class<?> clazz ) {
		final List<Setter> list = new ArrayList<Setter>();
		while ( !clazz.equals( Object.class ) ) {
			for ( val field : clazz.getDeclaredFields() ) {
				field.setAccessible( true );
				memorizeBindableField( clazz, list, field );
			}
			clazz = clazz.getSuperclass();
		}
		return list;
	}

	void memorizeBindableField( final Class<?> clazz, final List<Setter> list, final Field field )
	{
		val textAnnotation = field.getAnnotation( BindableText.class );
		if ( textAnnotation != null )
			list.add( memorizeBindableTextField( clazz, field, textAnnotation ) );
		val attributeAnnotation = field.getAnnotation( BindableAttribute.class );
		if ( attributeAnnotation != null )
			list.add( memorizeBindableAttributeField( clazz, field, attributeAnnotation ) );
		val repeatableAnnotation = field.getAnnotation( BindableManyTimes.class );
		if ( repeatableAnnotation != null )
			list.add( memorizeRepeatableBinding( clazz, field, repeatableAnnotation ) );
	}

	Setter memorizeBindableTextField( final Class<?> clazz, final Field field, final BindableText annotation ) {
		final String selector = annotation.value();
		return new TextElementSetter( field, selector );
	}

	Setter memorizeBindableAttributeField( final Class<?> clazz, final Field field, final BindableAttribute annotation ) {
		final String selector = annotation.selector();
		final String attribute = annotation.attribute();
		return new AttributeElementSetter( field, selector, attribute );
	}

	Setter memorizeRepeatableBinding( final Class<?> clazz, final Field field, final BindableManyTimes annotation ) {
		final String selector = annotation.selector();
		final BindableClass bindableClass = getBindableClassFor( annotation.model() );
		return new RepeatableElementSetter( selector, field, bindableClass );
	}

	public <T> BindableClass getBindableClassFor( final Class<T> type ) {
		BindableClass bindableData = cache.get( type );
		if ( bindableData == null )
			synchronized ( cache ) {
				bindableData = cache.get( type );
				if ( bindableData == null )
					cache.put( type, bindableData = createNewBindableClass( type ) );
			}
		return bindableData;
	}
}