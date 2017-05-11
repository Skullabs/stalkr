package stalkr.html;

import java.sql.Time;
import java.util.Date;

import lombok.Data;

@Data
public class DataTypes {
	
	@BindableText( "p#int" )
	Integer integerText;
	
	@BindableAttribute( selector="p#int", attribute="data" )
	Integer integerAttr;

	@BindableText( "p#long" )
	Long longText;
	
	@BindableAttribute( selector="p#long", attribute="data" )
	Long longAttr;
	
	@BindableText( "p#float" )
	Float floatText;
	
	@BindableAttribute( selector="p#float", attribute="data" )
	Float floatAttr;
	
	@BindableText( "p#double" )
	Double doubleText;
	
	@BindableAttribute( selector="p#double", attribute="data" )
	Double doubleAttr;
	
	@BindableText( "p#date" )
	Date dateText;
	
	@DatePattern("yyyy-MM-dd")
	@BindableAttribute( selector="p#date", attribute="data" )
	Date dateAttr;
	
	@BindableText( "p#time" )
	Time timeText;
	
	@DatePattern("HH:mm")
	@BindableAttribute( selector="p#time", attribute="data" )
	Time timeAttr;
	
}