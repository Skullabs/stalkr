package stalkr.html;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import lombok.SneakyThrows;
import lombok.val;

import org.junit.Before;
import org.junit.Test;

import trip.spi.Provided;
import trip.spi.ServiceProvider;

public class HtmlBinderTest {

	private static final String LINK_FIRST_SUE = "/cpopg/show.do?processo.codigo=090000EKL0000&processo.foro=9&conversationId=&dadosConsulta.localPesquisa.cdLocal=-1&cbPesquisa=DOCPARTE&dadosConsulta.tipoNuProcesso=UNIFICADO&dadosConsulta.valorConsulta=05272057961&vlCaptcha=tkcpc&paginaConsulta=1";
	private static final String LINK_SECOND_SUE = "/cpopg/show.do?processo.codigo=1900042KD0000&processo.foro=45&conversationId=&dadosConsulta.localPesquisa.cdLocal=-1&cbPesquisa=DOCPARTE&dadosConsulta.tipoNuProcesso=UNIFICADO&dadosConsulta.valorConsulta=05272057961&vlCaptcha=tkcpc&paginaConsulta=1";

	@Provided
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

	@SneakyThrows
	private String readFile( String fileName ) {
		fileName = "src/test/resources/" + fileName;
		final Path path = Paths.get( fileName );
		final byte[] allBytes = Files.readAllBytes( path );
		return new String( allBytes );
	}

	@Before
	@SneakyThrows
	public void provideDependencies() {
		new ServiceProvider().provideOn( this );
	}
}