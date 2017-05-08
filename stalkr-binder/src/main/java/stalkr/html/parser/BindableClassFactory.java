package stalkr.html.parser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;

import lombok.val;
import stalkr.html.BindableAttribute;
import stalkr.html.BindableAttributes;
import stalkr.html.BindableEmbedded;
import stalkr.html.BindableManyTimes;
import stalkr.html.BindableText;
import stalkr.html.BindableTexts;

@Singleton
public class BindableClassFactory {

	final Map<Class<?>, BindableClass> cache = new ConcurrentHashMap<>();
	final List<FieldParser<?>> fieldParsers = Arrays.asList(
			bindableTexts(), bindableText(), bindableAttributes(), bindableAttribute(), bindableManyTimes(), bindableEmbedded() );

	/**
	 * @param type
	 * @return
	 */
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

	BindableClass createNewBindableClass( final Class<?> clazz ) {
		val fields = stripBindableFieldsOfClazz( clazz );
		return new BindableClass( clazz, fields );
	}

	List<Setter> stripBindableFieldsOfClazz( Class<?> clazz ) {
		val list = new ArrayList<Setter>();
		while ( !clazz.equals( Object.class ) ) {
			for ( val field : clazz.getDeclaredFields() ) {
				field.setAccessible( true );
				memorizeBindableField( list, field );
			}
			clazz = clazz.getSuperclass();
		}
		return list;
	}

	/**
	 * @param list
	 * @param field
	 */
	public void memorizeBindableField( final List<Setter> list, final Field field )
	{
		val originalSize = list.size();
		val listIterator = fieldParsers.iterator();
		while ( listIterator.hasNext() && list.size() <= originalSize )
			listIterator.next().parse( list, field );
	}

	FieldParser<BindableText> bindableText() {
		return FieldParser.of( BindableText.class,
				( field, annotation )
				-> new TextElementSetter( field, annotation.value() ) );
	}

	FieldParser<BindableTexts> bindableTexts() {
		return FieldParser.of( BindableTexts.class,
				( field, annotation ) -> {
					WrappedListOfElementSetter setters = new WrappedListOfElementSetter();
					for ( BindableText bindableText : annotation.value() )
						setters.wrap( new TextElementSetter( field, bindableText.value() ) );
					return setters;
				} );
	}

	FieldParser<BindableAttribute> bindableAttribute() {
		return FieldParser.of( BindableAttribute.class,
				( field, annotation )
				-> new AttributeElementSetter( field, annotation.selector(), annotation.attribute() ) );
	}

	FieldParser<BindableAttributes> bindableAttributes() {
		return FieldParser.of( BindableAttributes.class,
				( field, annotation ) -> {
					WrappedListOfElementSetter setters = new WrappedListOfElementSetter();
					for ( BindableAttribute attribute : annotation.value() )
						setters.wrap( new AttributeElementSetter( field, attribute.selector(), attribute.attribute() ) );
					return setters;
				} );
	}

	FieldParser<BindableManyTimes> bindableManyTimes() {
		return FieldParser.of( BindableManyTimes.class,
				( field, annotation ) -> {
					final BindableClass bindableClass = getBindableClassFor( annotation.model() );
					return new RepeatableElementSetter( annotation.selector(), field, bindableClass );
				} );
	}
	
	FieldParser<BindableEmbedded> bindableEmbedded() {
		return FieldParser.of( BindableEmbedded.class,
				( field, annotation ) -> {
					final BindableClass bindableClass = getBindableClassFor( field.getType() );
					return new EmbeddedElementSetter( annotation.value(), field, bindableClass );
				} );
	}
	
}