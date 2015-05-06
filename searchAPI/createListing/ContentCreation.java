package searchAPI.createListing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.apache.http.message.BasicHeader;

import classicDriver.JSONAndExcelConversionTOArrayList;
import classicDriver.JSONMethods;

import com.sun.net.httpserver.Headers;

public class ContentCreation extends JSONAndExcelConversionTOArrayList {

	/**
	 * this method is for creating content/node by setting cookies and X-CSRF-Token
	 */
	@Test
	public void createContent() {

		try {
			URI url = new URI("http://dev.classicdriver.com/api/user/login");
			DefaultHttpClient client = new DefaultHttpClient();
			CookieStore cookieStore = new BasicCookieStore();
			HttpPost post = new HttpPost(url);
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("username",
					"jayjitraj jadeja"));
			urlParameters.add(new BasicNameValuePair("password", "srijan123"));
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			HttpResponse response = HttpClientBuilder.create().build()
					.execute(post);
			response = client.execute(post);
			System.out.println("Response Code : "
					+ response.getStatusLine().getStatusCode());
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			System.out.println("Ressponse 1 >>>>" + result.toString());

			String sessionId = getValueFromObject(result.toString(), "sessid");
			String sessionName = getValueFromObject(result.toString(),
					"session_name");

			String token1 = getValueFromObject(result.toString(), "token");
			String cookie_session = sessionName + "=" + sessionId;
			BasicClientCookie cookie = new BasicClientCookie(sessionName,
					sessionId);
			cookieStore.addCookie(cookie);
			System.out.println("Custom_Cookies->" + cookie);
			System.out.println("this is cookie session " + cookie_session);

			url = new URI("http://dev.classicdriver.com/api/bikes");
			post = new HttpPost(url);
			Header head = new BasicHeader("X-CSRF-Token", token1);
			post.setHeader(head);
			urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("type", "bike"));
			urlParameters.add(new BasicNameValuePair("title", "newBike"));
			// header1=post.getLastHeader("")
			post.setEntity(new UrlEncodedFormEntity(urlParameters));

			// client.setCookieStore(cookie_session);
			response = client.execute(post);
			System.out.println("Sending 'POST' request to URL : " + url);
			System.out.println("Response Code : "
					+ response.getStatusLine().getStatusCode());
			System.out.println("Response Code : " + response.toString());

			rd = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			result = new StringBuffer();
			line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			System.out.println("Ressponse 1 >>>>" + result.toString());

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	/*
	 * public String generateCSRF(CookieStore cookieStore) throws IOException{
	 * System.out.println("In generate CSRF"); URI url = null; try { url = new
	 * URI("http://dev.classicdriver.com/en/services/session/token"); } catch
	 * (URISyntaxException e) { // TODO Auto-generated catch block
	 * System.out.println("Catch block"); e.printStackTrace(); }
	 * DefaultHttpClient client = new DefaultHttpClient(); CloseableHttpClient
	 * client1 = new DefaultHttpClient();
	 * 
	 * HttpGet getReq= new HttpGet(url); client.setCookieStore(cookieStore);
	 * CookieStore cook = client.getCookieStore();
	 * 
	 * List<Cookie> cookieList = cook.getCookies(); for(Cookie c1 : cookieList){
	 * System.out.println(c1.getName()+"-"+c1.getValue()); }
	 * 
	 * CloseableHttpResponse response = client.execute(getReq);
	 * System.out.println
	 * (response.getStatusLine().getStatusCode()+response.toString()); String
	 * csrfToken = response.toString();
	 * 
	 * return csrfToken; }
	 */

	public String getValueFromObject(String url, String parent)
			throws FileNotFoundException, IOException, ParseException {
		String jsonFileName1 = url;
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(jsonFileName1);
		JSONObject jsonObj = (JSONObject) obj;

		jsonObj = (JSONObject) obj;
		String outVal = null;
		if (jsonObj.get(parent) == null) {
			outVal = "null";
		} else {
			if (jsonObj.get(parent).toString().endsWith(".0")) {
				String temp = jsonObj.get(parent).toString();
				outVal = temp.substring(0, temp.indexOf(".0"));
			} else
				outVal = jsonObj.get(parent).toString();
		}
		return outVal;
	}

}