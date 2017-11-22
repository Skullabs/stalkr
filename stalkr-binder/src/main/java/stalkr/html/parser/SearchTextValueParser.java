package stalkr.html.parser;

import lombok.AllArgsConstructor;
import lombok.val;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Created by ronei.gebert on 11/08/2017.
 */
@AllArgsConstructor
public class SearchTextValueParser implements BiFunction<String, Boolean, Object> {

    Pattern pattern;
    int groupIndex;
    BiFunction<String, Boolean, Object> parser;

    @Override
    public Object apply(String text, Boolean required) {
        val matcher = pattern.matcher( text );
        if ( !matcher.find() ) {
            if ( !required ) return null;
            throw new RuntimeException("Text not found for pattern [" + pattern.pattern() + "] and text [" + text + "]");
        }
        return parser.apply( matcher.group(groupIndex), required );
    }
}
