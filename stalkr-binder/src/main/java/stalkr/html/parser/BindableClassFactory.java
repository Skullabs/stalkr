package stalkr.html.parser;

import java.lang.reflect.Field;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Pattern;

import javax.inject.Singleton;

import lombok.RequiredArgsConstructor;
import lombok.val;
import stalkr.html.*;

@RequiredArgsConstructor
public class BindableClassFactory {
	
	final Map<Class<?>, BindableClass> cache = new ConcurrentHashMap<>();
	final List<FieldParser<?>> fieldParsers = Arrays.asList(
			bindableTexts(), bindableText(), bindableAttributes(), bindableAttribute(), bindableManyTimes(), bindableEmbedded() );
	final Map<Class<?>, Function<String, Object>> parsers = createValueParsers();
	final Function<String, Object> dateParser;
	final Function<String, Object> timeParser;

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
				( field, annotation ) -> {
					return new TextElementSetter( field, annotation.value(), valueParseFor(field) );
				});
	}

	FieldParser<BindableTexts> bindableTexts() {
		return FieldParser.of( BindableTexts.class,
				( field, annotation ) -> {
					WrappedListOfElementSetter setters = new WrappedListOfElementSetter();
					for ( BindableText bindableText : annotation.value() )
						setters.wrap( new TextElementSetter( field, bindableText.value(), valueParseFor(field) ) );
					return setters;
				} );
	}

	FieldParser<BindableAttribute> bindableAttribute() {
		return FieldParser.of( BindableAttribute.class,
				( field, annotation )
				-> new AttributeElementSetter( field, annotation.selector(), annotation.attribute(), valueParseFor(field) ) );
	}

	FieldParser<BindableAttributes> bindableAttributes() {
		return FieldParser.of( BindableAttributes.class,
				( field, annotation ) -> {
					WrappedListOfElementSetter setters = new WrappedListOfElementSetter();
					for ( BindableAttribute attribute : annotation.value() )
						setters.wrap( new AttributeElementSetter( field, attribute.selector(), attribute.attribute(), valueParseFor(field) ) );
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

	Function<String, Object> valueParseFor(Field field){
		val valueParser = valueParseForField( field );
		val textSearch = field.getAnnotation(BindableTextSearch.class);
		if ( textSearch != null ) {
			final Pattern pattern = Pattern.compile( textSearch.value() );
			return new SearchTextValueParser(pattern, textSearch.group(), valueParser);
		}
		return valueParser;
	}

	Function<String, Object> valueParseForField(Field field){
		val parser = parsers.get( field.getType() );
		if ( parser != null )
			return parser;
		val dateParser = dateOrTimeParser(field);
		if ( dateParser != null )
			return dateParser;
		throw new RuntimeException("No parser found for field " + field.getName() + " - " + field.getType());
	}

	Function<String, Object> dateOrTimeParser(Field field){
		val datePatternAnnotation = field.getAnnotation( DatePattern.class );
		if ( Time.class.isAssignableFrom(field.getType()) ){
			if ( datePatternAnnotation != null ) return timeParser( datePatternAnnotation.value() );
			return timeParser;
		} else if ( Date.class.isAssignableFrom(field.getType()) ){
			if ( datePatternAnnotation != null ) return dateParser( datePatternAnnotation.value() );
			return dateParser;
		}
		return null;
	}

	static Function<String, Object> dateParser(String datePattern){
		return (text)-> {
			try {
				return new SimpleDateFormat(datePattern).parse(text);
			} catch ( ParseException e ){
				throw new RuntimeException("Unable to parse date from text [" + text + "]");
			}
		};
	}

	static Function<String, Object> timeParser(String datePattern){
		return (text)-> {
			try {
				return new Time(new SimpleDateFormat(datePattern).parse(text).getTime());
			} catch ( ParseException e ){
				throw new RuntimeException("Unable to parse date from text [" + text + "]");
			}
		};
	}
	
	Map<Class<?>, Function<String, Object>> createValueParsers(){
		val map = new HashMap<Class<?>, Function<String, Object>>();
		map.put(String.class, (text)-> text);
		map.put(Integer.class, (text)-> Integer.parseInt(text));
		map.put(Long.class, (text)-> Long.parseLong(text));
		map.put(Double.class, (text)-> Double.parseDouble(text));
		map.put(Float.class, (text)-> Float.parseFloat(text));
		return map;
	}

	public static class Builder {

		Map<Class<?>, Function<String, Object>> customParsers = new HashMap<>();
		String datePattern = "dd/MM/yyyy";
		String timePattern = "HH:mm:ss";

		public Builder datePattern(String pattern){
			this.datePattern = pattern;
			return this;
		}

		public Builder timePattern(String pattern){
			this.timePattern = pattern;
			return this;
		}

		public Builder parser(Class<?> type, Function<String, Object> parser){
			this.customParsers.put(type, parser);
			return this;
		}

		public BindableClassFactory build(){
			val factory = new BindableClassFactory( dateParser(datePattern), timeParser(timePattern) );
			factory.parsers.putAll( customParsers );
			return factory;
		}

	}

}