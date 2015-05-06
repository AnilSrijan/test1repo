package searchAPI.createListing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * @author Deepshikha
 *
 */
public class NodeCreation {
	HttpClient client = new DefaultHttpClient();
	HttpPost post = null;
	HttpResponse response = null;
	List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
	URI url = null;
	String tokenValue;
	String usertoken;
	BufferedReader rd;
	StringBuffer Result;
	String line;
	String node_id;
	static Logger logger = Logger.getLogger(NodeCreation.class);

	public NodeCreation() {
		PropertyConfigurator.configure("log4j.properties");
	}

	/**
	 * this method is for fetching the X-CSRF-Token
	 * 
	 * @return X-CSRF-Token
	 * @throws Exception
	 */
	public String getToken() throws Exception {
		HttpGet url = new HttpGet(
				"http://dev.classicdriver.com/en/services/session/token");
		// Create local HTTP context
		HttpContext localContext = new BasicHttpContext();
		HttpResponse response = client.execute(url, localContext);
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : "
				+ response.getStatusLine().getStatusCode());
		rd = new BufferedReader(new InputStreamReader(response.getEntity()
				.getContent()));
		Result = new StringBuffer();
		line = "";
		while ((line = rd.readLine()) != null) {

			Result.append(line);
		}
		System.out.println("The CSRF is " + Result.toString());
		return Result.toString();
	}

	/**
	 * this method is for user authentication with the X-CSRF-Token fetched
	 * above
	 * 
	 * @return user api details
	 * @throws Exception
	 */
	public String userAuthentication() throws Exception {
		url = new URI("http://dev.classicdriver.com/api/user/login");
		CookieStore cookieStore = new BasicCookieStore();
		post = new HttpPost(url);
		urlParameters
		.add(new BasicNameValuePair("username", "jayjitraj jadeja"));
		urlParameters.add(new BasicNameValuePair("password", "srijan123"));
		post.setEntity(new UrlEncodedFormEntity(urlParameters));
		// response = HttpClientBuilder.create().build().execute(post);
		response = client.execute(post);
		System.out.println("Response Code is "
				+ response.getStatusLine().getStatusCode());
		rd = new BufferedReader(new InputStreamReader(response.getEntity()
				.getContent()));
		Result = new StringBuffer();
		line = "";
		while ((line = rd.readLine()) != null) {
			Result.append(line);
		}
		// parsing json string to jsonObject
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(Result.toString());
		JSONObject jsonObj = (JSONObject) obj;
		String session_id = (String) jsonObj.get("sessid");
		String session_name = (String) jsonObj.get("session_name");
		usertoken = (String) jsonObj.get("token");
		BasicClientCookie cookie = new BasicClientCookie(session_name,
				session_id);
		cookieStore.addCookie(cookie);
		System.out.println("User is authenticated.." + "\n");
		return usertoken;

	}

	/**
	 * this method is for creating content/node for any product
	 * 
	 * @param node_type
	 *            = Ex. cars, bikes, etc
	 * @param parameters
	 *            = these are passed in the form of key value pair forcraeting
	 *            node
	 * @return = node_id
	 * @throws Exception
	 */
	public String createContent(String node_type,
			HashMap<String, String> parameters) throws Exception {
		url = new URI("http://dev.classicdriver.com/api/" + node_type);
		post = new HttpPost(url);
		Header head = new BasicHeader("X-CSRF-Token", usertoken);
		// post.setHeader(head);
		post.addHeader(head);
		Set<String> keySet = parameters.keySet();
		Object[] keyArr = keySet.toArray();
		for (int temp = 0; temp < keyArr.length; temp++) {
			String key = keyArr[temp].toString();
			// System.out.println(key+"---"+parameters.get(key));
			urlParameters.add(new BasicNameValuePair(key,
					replaceString(parameters.get(key))));
		}
		post.setEntity(new UrlEncodedFormEntity(urlParameters));
		response = client.execute(post);

		/* System.out.println("Sending 'POST' request to URL : " + url);
		  System.out.println("Response Code : " +
		  response.getStatusLine().getStatusCode());*/

		rd = new BufferedReader(new InputStreamReader(response.getEntity()
				.getContent()));
		Result = new StringBuffer();
		line = "";
		while ((line = rd.readLine()) != null) {
			Result.append(line);
		}
		node_id = Result.toString();

		// for fetching the node id
		node_id = node_id.replace("[\"", "");
		node_id = node_id.replace("\"]", "");
		System.out.println("The node_id is " + node_id);
		System.out.println("<==Node created successfully==>" + "\n");
		return node_id;
	}

	/**
	 * this method is for replacing .0 with blank
	 * 
	 * @param cellVAlue
	 * @return
	 */
	private String replaceString(String cellVAlue) {

		if (cellVAlue.contains(".0")) {
			cellVAlue = cellVAlue.replace(".0", "");
		}
		// System.out.println("The Final value after replacement is:-"+cellVAlue);
		return cellVAlue;

	}

}
