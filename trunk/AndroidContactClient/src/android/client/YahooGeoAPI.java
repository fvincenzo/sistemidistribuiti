package android.client;

import java.io.IOException;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpURL;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;

import android.util.Log;

public class YahooGeoAPI {

//	private static final String LOGGER = "lordhong.yahoo";
	private static final String APPID = "MyContactsClient";
	private static final String YAHOO_GEO_API_URL = "http://local.yahooapis.com/MapsService/V1/geocode?appid=";
	private static HttpConnectionManager connectionManager = new SimpleHttpConnectionManager();
	
	public static String getGeoCode(String location) throws IOException {
		StringBuffer url = new StringBuffer(YAHOO_GEO_API_URL);
		url.append(APPID).append("&location=").append(location.replaceAll(" ", "+"));
		
		HttpURL httpURL = new HttpURL(url.toString());
		HostConfiguration host = new HostConfiguration();
		host.setHost(httpURL.getHost(), httpURL.getPort());
		HttpConnection connection = connectionManager.getConnection(host);
		connection.open();
		GetMethod get = new GetMethod(url.toString());
		get.execute(new HttpState(), connection);
		String response = get.getResponseBodyAsString();
		connection.close();
		return response;
	}
}
