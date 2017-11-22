package stalkr.html;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import kikaha.core.test.KikahaRunner;
import lombok.SneakyThrows;
import lombok.val;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith( KikahaRunner.class )
public class HtmlBinderTest {

	private static final String LINK_FIRST_SUE = "/cpopg/show.do?processo.codigo=090000EKL0000&processo.foro=9&conversationId=&dadosConsulta.localPesquisa.cdLocal=-1&cbPesquisa=DOCPARTE&dadosConsulta.tipoNuProcesso=UNIFICADO&dadosConsulta.valorConsulta=05272057961&vlCaptcha=tkcpc&paginaConsulta=1";
	private static final String LINK_SECOND_SUE = "/cpopg/show.do?processo.codigo=1900042KD0000&processo.foro=45&conversationId=&dadosConsulta.localPesquisa.cdLocal=-1&cbPesquisa=DOCPARTE&dadosConsulta.tipoNuProcesso=UNIFICADO&dadosConsulta.valorConsulta=05272057961&vlCaptcha=tkcpc&paginaConsulta=1";

	@Inject
	HtmlBinder binder;

	@Test
	public void ensureThatCouldBindDataRetrieveFromHtmlToModel() {
		val data = readFile( "tj-sc-list-response.html" );
		val tj = binder.bind( data, FakeTJPendencies.class );
		val processos = tj.getProcessos();
		assertThat( processos.size(), is( 2 ) );
		ensureThatHaveBindedProcesso1AsExpected( processos );
		ensureThatHaveBindedProcesso2AsExpected( processos );
	}

	@Test
	public void ensureThatCouldBindDataForEachNodeSelectedThrowCSSQuery() {
		val data = readFile( "tj-sc-list-response.html" );
		val cssQuery = "#listagemDeProcessos > div.fundoClaro, #listagemDeProcessos > div.fundoEscuro";
		val processos = binder.bind( data, FakeTJPendency.class, cssQuery );
		assertThat( processos.size(), is( 2 ) );
		ensureThatHaveBindedProcesso1AsExpected( processos );
		ensureThatHaveBindedProcesso2AsExpected( processos );
	}

	private void ensureThatHaveBindedProcesso1AsExpected( final List<FakeTJPendency> processos ) {
		val processo1 = processos.get( 0 );
		assertThat( processo1.getName(), is( "0000880-39.2011.8.24.0009" ) );
		assertThat( processo1.getLink(), is( LINK_FIRST_SUE ) );
	}

	private void ensureThatHaveBindedProcesso2AsExpected( final List<FakeTJPendency> processos ) {
		val processo2 = processos.get( 1 );
		assertThat( processo2.getName(), is( "0007720-20.2012.8.24.0045" ) );
		assertThat( processo2.getLink(), is( LINK_SECOND_SUE ) );
	}

	@Test
	public void ensureThatCouldRetrieveDescriptionForEachNodeSelectedThrowCSSQuery() {
		val data = readFile( "tj-sc-list-response.html" );
		val cssQuery = "#listagemDeProcessos > div.fundoClaro .linkProcesso, #listagemDeProcessos > div.fundoEscuro .linkProcesso";
		val descriptions = binder.select( data, cssQuery );
		assertThat( descriptions.get( 0 ), is( "0000880-39.2011.8.24.0009" ) );
		assertThat( descriptions.get( 1 ), is( "0007720-20.2012.8.24.0045" ) );
	}

	@Test
	public void ensureThatCouldRetrieveLinkForEachNodeSelectedThrowCSSQuery() {
		val data = readFile( "tj-sc-list-response.html" );
		val cssQuery = "#listagemDeProcessos > div.fundoClaro .linkProcesso, #listagemDeProcessos > div.fundoEscuro .linkProcesso";
		val links = binder.selectAttr( data, cssQuery, "href" );
		assertThat( links.get( 0 ), is( LINK_FIRST_SUE ) );
		assertThat( links.get( 1 ), is( LINK_SECOND_SUE ) );
	}

	@Test
	public void ensureThatCouldRetrieveDataThroughRepeatableAnnotations() {
		val data = readFile( "github-page.html" );
		val bookmark = binder.bind( data, Bookmark.class );
		assertEquals( "Skullabs/stalkr", bookmark.title() );
		assertEquals( "stalkr - The crawler took kit for Java", bookmark.description() );
		assertEquals( "https://avatars3.githubusercontent.com/u/7440531?v=3&s=400", bookmark.picture() );
	}
	
	@Test
	public void ensureThatCouldRetrieveDataThroughEmbeddedAnnotations() {
		val data = readFile( "person-infopage.html" );
		val person = binder.bind( data, Person.class );
		assertEquals( "Adriana", person.getName() );
		assertEquals( "Cafelandia", person.getAdress().getCity() );
		assertEquals( "89898-000", person.getAdress().getPostalCode() );
	}
	
	@Test
	public void ensureThatCouldRetrieveDataForAnyTypes() throws ParseException {
		val data = readFile( "any-data-types.html" );
		val dataTypes = binder.bind( data, DataTypes.class );
		assertEquals( 10, dataTypes.getIntegerText(), 0 );
		assertEquals( 11, dataTypes.getIntegerAttr(), 0 );
		assertEquals( 100L, dataTypes.getLongText(), 0 );
		assertEquals( 101L, dataTypes.getLongAttr(), 0 );
		assertEquals( 51F, dataTypes.getFloatText(), 0 );
		assertEquals( 51F, dataTypes.getFloatAttr(), 0 );
		assertEquals( 444.12, dataTypes.getDoubleText(), 0 );
		assertEquals( 444.12, dataTypes.getDoubleAttr(), 0 );
		assertEquals( date("28/07/1900"), dataTypes.getDateText() );
		assertEquals( date("28/07/1901"), dataTypes.getDateAttr() );
		assertEquals( time("01:12:59"), dataTypes.getTimeText() );
		assertEquals( time("18:10:00"), dataTypes.getTimeAttr() );
	}

	@Test
	public void ensureThatCouldRetrieveDataForAnyTypesWithRegexSearch() throws ParseException {
		val data = readFile( "any-data-types-with-regex.html" );
		val dataTypes = binder.bind( data, DataTypesWithRegex.class );
		assertEquals( 10, dataTypes.getIntegerText(), 0 );
		assertEquals( 11, dataTypes.getIntegerAttr(), 0 );
		assertEquals( 100L, dataTypes.getLongText(), 0 );
		assertEquals( 101L, dataTypes.getLongAttr(), 0 );
		assertEquals( 51F, dataTypes.getFloatText(), 0 );
		assertEquals( 51F, dataTypes.getFloatAttr(), 0 );
		assertEquals( 444.12, dataTypes.getDoubleText(), 0 );
		assertEquals( 444.12, dataTypes.getDoubleAttr(), 0 );
		assertEquals( date("28/07/1900"), dataTypes.getDateText() );
		assertEquals( date("28/07/1901"), dataTypes.getDateAttr() );
		assertEquals( time("01:12:59"), dataTypes.getTimeText() );
		assertEquals( time("18:10:00"), dataTypes.getTimeAttr() );
	}

	@Test
	public void ensureThatNotThrowExceptionForEmptyValueWithRegexOption() throws ParseException {
		val data = readFile( "empty-any-data-types-with-regex.html" );
		val dataTypes = binder.bind( data, DataTypesWithRegex.class );
		assertNull( dataTypes.getIntegerText() );
		assertNull( dataTypes.getIntegerAttr() );
		assertNull( dataTypes.getLongText() );
		assertNull( dataTypes.getLongAttr() );
		assertNull( dataTypes.getFloatText() );
		assertNull( dataTypes.getFloatAttr() );
		assertNull( dataTypes.getDoubleText() );
		assertNull( dataTypes.getDoubleAttr() );
		assertNull( dataTypes.getDateText() );
		assertNull( dataTypes.getDateAttr() );
		assertNull( dataTypes.getTimeText() );
		assertNull( dataTypes.getTimeAttr() );
	}

	@Test
	public void ensureThatNotThrowExceptionForNotFoundElementsWithRegexOption() throws ParseException {
		val dataTypes = binder.bind( "<div></div>", DataTypesWithRegex.class );
		assertNull( dataTypes.getIntegerText() );
		assertNull( dataTypes.getIntegerAttr() );
		assertNull( dataTypes.getLongText() );
		assertNull( dataTypes.getLongAttr() );
		assertNull( dataTypes.getFloatText() );
		assertNull( dataTypes.getFloatAttr() );
		assertNull( dataTypes.getDoubleText() );
		assertNull( dataTypes.getDoubleAttr() );
		assertNull( dataTypes.getDateText() );
		assertNull( dataTypes.getDateAttr() );
		assertNull( dataTypes.getTimeText() );
		assertNull( dataTypes.getTimeAttr() );
	}

	@Test
	public void ensureThatNotThrowExceptionForEmptyValue() throws ParseException {
		val data = readFile("empty-any-data-types.html");
		val dataTypes = binder.bind( data, DataTypes.class );
		assertNull( dataTypes.getIntegerText() );
		assertNull( dataTypes.getIntegerAttr());
		assertNull( dataTypes.getLongText());
		assertNull( dataTypes.getLongAttr());
		assertNull( dataTypes.getFloatText() );
		assertNull( dataTypes.getFloatAttr() );
		assertNull( dataTypes.getDoubleText() );
		assertNull( dataTypes.getDoubleAttr() );
		assertNull( dataTypes.getDateText() );
		assertNull( dataTypes.getDateAttr() );
		assertNull( dataTypes.getTimeText() );
		assertNull( dataTypes.getTimeAttr() );
	}

	@Test
	public void ensureThatNotThrowExceptionForNotFoundElements() throws ParseException {
		val dataTypes = binder.bind( "<div></div>", DataTypes.class );
		assertNull( dataTypes.getIntegerText() );
		assertNull( dataTypes.getIntegerAttr());
		assertNull( dataTypes.getLongText());
		assertNull( dataTypes.getLongAttr());
		assertNull( dataTypes.getFloatText() );
		assertNull( dataTypes.getFloatAttr() );
		assertNull( dataTypes.getDoubleText() );
		assertNull( dataTypes.getDoubleAttr() );
		assertNull( dataTypes.getDateText() );
		assertNull( dataTypes.getDateAttr() );
		assertNull( dataTypes.getTimeText() );
		assertNull( dataTypes.getTimeAttr() );
	}

	@Test(expected = RuntimeException.class)
	public void ensureThatThrowExceptionForEmptyValue() throws ParseException {
		val data = readFile("empty-any-data-types.html");
		binder.bind( data, OptionalDataTypes.class );
	}

	@SneakyThrows
	Date date(String dateString){
		return new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
	}
	
	@SneakyThrows
	Time time(String timeString){
		val date = new SimpleDateFormat("HH:mm:ss").parse(timeString);
		return new Time(date.getTime());
	}
	
	@SneakyThrows
	private String readFile( String fileName ) {
		fileName = "src/test/resources/" + fileName;
		final Path path = Paths.get( fileName );
		final byte[] allBytes = Files.readAllBytes( path );
		return new String( allBytes );
	}

}