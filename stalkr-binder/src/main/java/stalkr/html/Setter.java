package stalkr.html;

import org.jsoup.nodes.Element;

interface Setter {

	void bind( final Element document, final Object instance );
}