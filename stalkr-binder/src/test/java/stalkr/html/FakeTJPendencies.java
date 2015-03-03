package stalkr.html;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FakeTJPendencies {

	@BindableManyTimes(
		selector = "#listagemDeProcessos > div.fundoClaro, #listagemDeProcessos > div.fundoEscuro",
		model = FakeTJPendency.class )
	List<FakeTJPendency> processos;
}

@Getter
@NoArgsConstructor
class FakeTJPendency {

	@BindableText( ".linkProcesso" )
	String name;

	@BindableAttribute( selector = ".linkProcesso", attribute = "href" )
	String link;
}