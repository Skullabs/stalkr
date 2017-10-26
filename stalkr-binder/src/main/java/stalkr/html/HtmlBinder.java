package stalkr.html;

import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import stalkr.html.parser.BindableClass;
import stalkr.html.parser.BindableClassFactory;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Singleton
@AllArgsConstructor
public class HtmlBinder {

	final BindableClassFactory factory;

	public HtmlBinder(){
		this( new BindableClassFactory.Builder().build() );
	}

	public <T> T bind( final String data, final Class<T> type ) {
		final Document document = Jsoup.parse( data );
		final BindableClass bindableClass = factory.getBindableClassFor( type );
		return newBindedDataFrom( bindableClass, document );
	}

	public <T> List<T> bind( final String data, final Class<T> type, final String selector ) {
		final Elements elements = Jsoup.parse( data ).select( selector );
		final BindableClass bindableClass = factory.getBindableClassFor( type );
		final List<T> list = new ArrayList<T>();
		for ( final Element element : elements )
			list.add( newBindedDataFrom( bindableClass, element ) );
		return list;
	}

	@SuppressWarnings( "unchecked" )
	<T> T newBindedDataFrom( final BindableClass bindableClass, final Element element ) {
		final T instance = (T)bindableClass.newInstanceOf();
		bindableClass.bind( element, instance );
		return instance;
	}

	public List<String> select( final String data, final String selector ) {
		final List<String> list = new ArrayList<>();
		selectElements( data, selector, ( element ) -> {
			list.add( element.text() );
		} );
		return list;
	}

	public List<String> selectAttr( final String data, final String selector, final String attr ) {
		final List<String> list = new ArrayList<>();
		selectElements( data, selector, ( element ) -> {
			list.add( element.attr( attr ) );
		} );
		return list;
	}

	public void selectElements( final String data, final String selector, final Consumer<Element> consumer ) {
		final Document document = Jsoup.parse( data );
		final Elements elements = document.select( selector );
		for ( final Element element : elements )
			consumer.accept( element );
	}

	public static class Builder {

		private BindableClassFactory.Builder factoryBuilder = new BindableClassFactory.Builder();

		public Builder parser( Class<?> type, Function<String, Object> parser ){
			factoryBuilder.parser( type, parser );
			return this;
		}

		public Builder datePattern( String pattern ){
			factoryBuilder.datePattern(pattern);
			return this;
		}

		public Builder timePattern( String pattern ){
			factoryBuilder.timePattern(pattern);
			return this;
		}

		public HtmlBinder build(){
			return new HtmlBinder( factoryBuilder.build() );
		}

	}

}