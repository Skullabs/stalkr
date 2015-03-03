package stalkr.crawler;

import lombok.Data;
import stalkr.html.BindableText;

@Data
public class SeraQueVaiChover {

	@BindableText( "#resposta p" )
	String yesOrNot;
}
