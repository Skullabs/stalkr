package stalkr.html;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Repeatable( BindableAttributes.class )
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface BindableAttribute {

	String selector();
	String attribute();
}
