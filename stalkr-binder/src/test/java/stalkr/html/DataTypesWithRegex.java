package stalkr.html;

import lombok.Data;

import java.sql.Time;
import java.util.Date;

@Data
public class DataTypesWithRegex {

	@BindableTextSearch( "\\d+" )
	@BindableText( "p#int" )
	Integer integerText;

	@BindableTextSearch( value = "(#######)(\\d+)(#######)", group = 2 )
	@BindableAttribute( selector="p#int", attribute="data" )
	Integer integerAttr;

	@BindableTextSearch( "\\d+" )
	@BindableText( "p#long" )
	Long longText;

	@BindableTextSearch( value = "#######(\\d+).*", group = 1 )
	@BindableAttribute( selector="p#long", attribute="data" )
	Long longAttr;

	@BindableTextSearch( "\\d+" )
	@BindableText( "p#float" )
	Float floatText;

	@BindableTextSearch( value = ".*?(\\d+)", group = 1 )
	@BindableAttribute( selector="p#float", attribute="data" )
	Float floatAttr;

	@BindableTextSearch( "\\d+\\.\\d+" )
	@BindableText( "p#double" )
	Double doubleText;

	@BindableTextSearch( "\\d+\\.\\d+" )
	@BindableAttribute( selector="p#double", attribute="data" )
	Double doubleAttr;

	@BindableTextSearch( "\\d+{2}/\\d+{2}/\\d+{4}" )
	@BindableText( "p#date" )
	Date dateText;

	@BindableTextSearch( "\\d+{4}-\\d+{2}-\\d+{2}" )
	@DatePattern("yyyy-MM-dd")
	@BindableAttribute( selector="p#date", attribute="data" )
	Date dateAttr;

	@BindableTextSearch( "\\d+{2}:\\d+{2}:\\d+{2}" )
	@BindableText( "p#time" )
	Time timeText;

	@BindableTextSearch( "\\d+{2}:\\d+{2}" )
	@DatePattern("HH:mm")
	@BindableAttribute( selector="p#time", attribute="data" )
	Time timeAttr;
	
}