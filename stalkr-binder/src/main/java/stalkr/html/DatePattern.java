package stalkr.html;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use for custom date or time pattern <br/>
 * Default value is dd/MM/yyyy for date and HH:mm:ss for time
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface DatePattern {
	
	String value();

}
