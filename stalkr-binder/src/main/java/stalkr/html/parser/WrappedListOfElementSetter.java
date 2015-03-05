package stalkr.html.parser;

import java.util.ArrayList;
import java.util.List;

import lombok.val;

import org.jsoup.nodes.Element;

public class WrappedListOfElementSetter implements Setter {

	final List<Setter> listOfSetters = new ArrayList<Setter>();

	@Override
	public void bind( Element document, Object instance ) {
		for ( val setter : listOfSetters )
			setter.bind( document, instance );
	}

	public void wrap( Setter setter ) {
		listOfSetters.add( setter );
	}
}
