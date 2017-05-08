package stalkr.html;

import lombok.Data;

@Data
public class Person {
	
	@BindableText( "p#name" )
	String name;
	
	@BindableEmbedded( "div#address" )
	Address adress;

}


@Data
class Address {
	
	@BindableText( "p#postalCode" )
	String postalCode;
	
	@BindableText( "p#city" )
	String city;
	
}