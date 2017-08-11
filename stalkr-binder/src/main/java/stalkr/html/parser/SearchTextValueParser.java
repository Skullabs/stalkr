package stalkr.html.parser;

import lombok.AllArgsConstructor;
import lombok.val;

import java.util.regex.Pattern;

/**
 * Created by ronei.gebert on 11/08/2017.
 */
@AllArgsConstructor
public class SearchTextValueParser implements ValueParser {

    Pattern pattern;
    int groupIndex;
    ValueParser parser;

    @Override
    public Object parse(String text) {
        val matcher = pattern.matcher( text );
        if ( !matcher.find() )
            throw new RuntimeException( "Text not found for pattern [" + pattern.pattern() + "] and text [" + text + "]" );
        return parser.parse( matcher.group(groupIndex) );
    }
}
