package stalkr.crawler;

import stalkr.http.Requests;

public interface Job {

	void run( final Requests requester ) throws Exception;

}
