package stalkr.html.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public interface SetterConstructor<T extends Annotation> {
	Setter construct( Field field, T annotation );
}