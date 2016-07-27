package stalkr.crawler;

import lombok.Data;
import stalkr.html.BindableText;

@Data
public class MeuIp {

	@BindableText( "span.tx_vermelho_30" )
	String ip;
}
