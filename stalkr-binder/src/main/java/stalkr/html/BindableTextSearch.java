package stalkr.html;

import java.lang.annotation.*;

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface BindableTextSearch {

	/**
	 * Regex pattern to find text
	 */
	String value();
	int group() default 0;

}
