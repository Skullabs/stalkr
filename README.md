# Stalkr Binder

Stalkr Binder is a simple library to bind HTML data into java objects

## Contributors
- [Ronei Vinicius Gebert](https://github.com/roneigebert)
- [Miere Liniel Teixeira](https://github.com/miere)

## Configure as maven dependency

```xml
<dependency>
    <groupId>io.skullabs.stalkr</groupId>
    <artifactId>stalkr-binder</artifactId>
    <version>0.2.7</version>
</dependency>
```

## Usage

You can use kikaha injection:

```java
@Inject
stalkr.html.HtmlBinder binder;

// Read your html ...
String myHtml = readMyHtml();  // implement this for get your html...

// End bind single object
MyExample singleObjectResult = binder.bind( myHtml, MyExample.class );
System.out.println( singleObjectResult );

// Or bind a list
String listCssSelector = "ul#example-ul-list > li"; // you selector for get many HTML elements
List<MyExample> listResult = binder.bind( myHtml, MyExample.class, listCssSelector );
System.out.println( listResult );
```

and in your class you must define fields with annotations containing css selectors, example:

```java
class MyExample {

    @stalkr.html.BindableText( "p#name" )
    String name;

    @stalkr.html.BindableEmbedded( "div#embedded" )
    MyEmbeddedClassExample embeddedField;

    @stalkr.html.BindableManyTimes( selector = "ul#many-times-example > li", model = ManyTimesClassExample.class )
    List<ManyTimesClassExample> manyTimesExample;

}

class MyEmbeddedClassExample {

    @stalkr.html.BindableText( "p#first" )
    String firstField;

    @stalkr.html.BindableText( "p#second" )
    String secondField;

}

class ManyTimesClassExample {

    @stalkr.html.BindableText( "p#my-item" )
    String myItemField;

}
```

## Supported types

String, Integer, Long, Float, Double, Date and Time

For date/time is used "dd/MM/yyyy" and "HH:mm:ss" pattern, but you can change it:

```java
class CustomPatternsExample {

    @stalkr.html.DatePattern( "yyyy-MM-dd" ) // <- your pattern here
    @stalkr.html.BindableText( "p#date" )
    Date dateExample;

    @stalkr.html.DatePattern( "ss:mm:HH" ) // <- your pattern here
    @stalkr.html.BindableText( "p#time" )
    Time timeExample;

}
```

## Using regex search

```java
class FindUsingRegexPatternExample {

    @stalkr.html.BindableTextSearch( "My name is .* and I'm \\d+ years old" )
    @stalkr.html.BindableText( "p#profile-phrase" )
    String profilePhrase;

    @stalkr.html.BindableTextSearch( value = "My name is (.*) and I'm (\\d+) years old", group = 1 )
    @stalkr.html.BindableText( "p#profile-phrase" )
    String name;

    @stalkr.html.BindableTextSearch( value = "My name is (.*) and I'm (\\d+) years old", group = 2 )
    @stalkr.html.BindableText( "p#profile-phrase" )
    Integer old;

}
```

## Example

```java
public class Person {

	@stalkr.html.BindableText( "p#name" )
	String name;

	@stalkr.html.BindableEmbedded( "div#address" )
	Address address;

	// ... getters and setters ...

}

class Address {

	@stalkr.html.BindableText( "p#postalCode" )
	String postalCode;

	@stalkr.html.BindableText( "p#city" )
	String city;

	// ... getters and setters ...

}

// And bind:

String html = "<html>\n" +
			"<body>\n" +
			"    <p id=\"name\">Adriana</p>\n" +
			"        <div id=\"address\">\n" +
			"            <p id=\"city\">Cafelandia</p>\n" +
			"            <p id=\"postalCode\">89898-000</p>\n" +
			"        </div>\n" +
			"    </body>\n" +
			"</html>";

Person person = binder.bind( html, Person.class );
System.out.println( person.getName() );
System.out.println( person.getAddress().getCity() );
```