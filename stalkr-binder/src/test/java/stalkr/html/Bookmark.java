package stalkr.html;

import lombok.Getter;
import lombok.experimental.Accessors;
import stalkr.html.BindableAttribute;
import stalkr.html.BindableText;

@Getter
@Accessors( fluent = true )
public class Bookmark {

	@BindableText( "html head title" )
	String title;

	@BindableAttribute( selector = "html head meta[property=\"og:image\"]", attribute = "content" )
	String picture;

	@BindableAttribute( selector = "html head meta[property=\"og:description\"]", attribute = "content" )
	@BindableAttribute( selector = "html head meta[property=\"description\"]", attribute = "content" )
	String description;
}
