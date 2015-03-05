package stalkr.html.parser;

import org.jsoup.nodes.Element;

public interface Setter {

	void bind( final Element document, final Object instance );
}