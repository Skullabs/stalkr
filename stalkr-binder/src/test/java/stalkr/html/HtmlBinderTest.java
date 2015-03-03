package stalkr.html;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import lombok.SneakyThrows;

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
		final String data = readFile( "tj-sc-list-response.html" );
		final FakeTJPendencies tj = binder.bind( data, FakeTJPendencies.class );
		final List<FakeTJPendency> processos = tj.getProcessos();
		assertThat( processos.size(), is( 2 ) );
		ensureThatHaveBindedProcesso1AsExpected( processos );
		ensureThatHaveBindedProcesso2AsExpected( processos );
	}

	@Test
	public void ensureThatCouldBindDataForEachNodeSelectedThrowCSSQuery() {
		final String data = readFile( "tj-sc-list-response.html" );
		final String cssQuery = "#listagemDeProcessos > div.fundoClaro, #listagemDeProcessos > div.fundoEscuro";
		final List<FakeTJPendency> processos = binder.bind( data, FakeTJPendency.class, cssQuery );
		assertThat( processos.size(), is( 2 ) );
		ensureThatHaveBindedProcesso1AsExpected( processos );
		ensureThatHaveBindedProcesso2AsExpected( processos );
	}

	@Test
	public void ensureThatCouldRetrieveDescriptionForEachNodeSelectedThrowCSSQuery() {
		final String data = readFile( "tj-sc-list-response.html" );
		final String cssQuery = "#listagemDeProcessos > div.fundoClaro .linkProcesso, #listagemDeProcessos > div.fundoEscuro .linkProcesso";
		final List<String> descriptions = binder.select( data, cssQuery );
		assertThat( descriptions.get( 0 ), is( "0000880-39.2011.8.24.0009" ) );
		assertThat( descriptions.get( 1 ), is( "0007720-20.2012.8.24.0045" ) );
	}

	@Test
	public void ensureThatCouldRetrieveLinkForEachNodeSelectedThrowCSSQuery() {
		final String data = readFile( "tj-sc-list-response.html" );
		final String cssQuery = "#listagemDeProcessos > div.fundoClaro .linkProcesso, #listagemDeProcessos > div.fundoEscuro .linkProcesso";
		final List<String> links = binder.selectAttr( data, cssQuery, "href" );
		assertThat( links.get( 0 ), is( LINK_FIRST_SUE ) );
		assertThat( links.get( 1 ), is( LINK_SECOND_SUE ) );
	}

	private void ensureThatHaveBindedProcesso1AsExpected( final List<FakeTJPendency> processos ) {
		final FakeTJPendency processo1 = processos.get( 0 );
		assertThat( processo1.getName(), is( "0000880-39.2011.8.24.0009" ) );
		assertThat( processo1.getLink(), is( LINK_FIRST_SUE ) );
	}

	private void ensureThatHaveBindedProcesso2AsExpected( final List<FakeTJPendency> processos ) {
		final FakeTJPendency processo2 = processos.get( 1 );
		assertThat( processo2.getName(), is( "0007720-20.2012.8.24.0045" ) );
		assertThat( processo2.getLink(), is( LINK_SECOND_SUE ) );
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