package DWR.DMI.tstool;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Class to handle a URL request - test mode
 * @author sam
 *
 */
public class UrlHandler implements HttpHandler {
	/**
	 * TODO SAM 2016-02-10 Enable this to a reasonable level:
	 * - use the body of the URL to determine the command file to run.
	 * - use the query parameters to set TSCommandProcessor properties
	 * - similar to batchServer execution
	 * - enable some way to exit so that Java process does not need to be killed
	 */
	public void handle(HttpExchange t) throws IOException {
		URI uri = t.getRequestURI();
		String response = "TSTool response for " + uri;
		// Get query parameters
		Map parameters = splitQuery(uri.toURL());
		t.sendResponseHeaders(200,response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}

	/**
	 * Split a URL string into parameters.  Do light-weight without external library.
	 * See:  http://stackoverflow.com/questions/13592236/parse-a-uri-string-into-name-value-collection
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private Map<String, List<String>> splitQuery(URL url) throws UnsupportedEncodingException {
	  final Map<String, List<String>> query_pairs = new LinkedHashMap<String, List<String>>();
	  final String[] pairs = url.getQuery().split("&");
	  for (String pair : pairs) {
	    final int idx = pair.indexOf("=");
	    final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
	    if (!query_pairs.containsKey(key)) {
	      query_pairs.put(key, new LinkedList<String>());
	    }
	    final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
	    query_pairs.get(key).add(value);
	  }
	  return query_pairs;
	}

}