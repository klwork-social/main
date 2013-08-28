package com.vdisk.net;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLException;

import net.sf.json.JSONObject;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

import com.vdisk.net.VDiskAPI.RequestAndResponse;
import com.vdisk.net.exception.VDiskException;
import com.vdisk.net.exception.VDiskIOException;
import com.vdisk.net.exception.VDiskParseException;
import com.vdisk.net.exception.VDiskSSLException;
import com.vdisk.net.exception.VDiskServerException;
import com.vdisk.net.exception.VDiskUnlinkedException;
import com.vdisk.net.session.Session;
import com.vdisk.net.session.Session.ProxyInfo;

/**
 * This class is mostly used internally by {@link VDiskAPI} for creating and
 * executing REST requests to the VDisk API, and parsing responses. You probably
 * won't have a use for it other than {@link #parseDate(String)} for parsing
 * modified times returned in metadata, or (in very rare circumstances) writing
 * your own API calls.
 */
public class RESTUtility {
	static Logger logger = Logger.getLogger(RESTUtility.class.getName());
	private RESTUtility() {
	}

	private static final DateFormat dateFormat = new SimpleDateFormat(
			"EEE, dd MMM yyyy kk:mm:ss ZZZZZ", Locale.US);
	private final static String TAG = RESTUtility.class.getSimpleName();

	public enum RequestMethod {
		GET, POST;
	}

	/**
	 * Creates and sends a request to the VDisk API, parses the response as
	 * JSON, and returns the result.
	 * 
	 * @param method
	 *            GET or POST.
	 * @param host
	 *            the hostname to use. Should be either api server, content
	 *            server, or web server.
	 * @param path
	 *            the URL path, starting with a '/'.
	 * @param apiVersion
	 *            the API version to use. This should almost always be set to
	 *            {@code VDiskAPI.VERSION}.
	 * @param params
	 *            the URL params in an array, with the even numbered elements
	 *            the parameter names and odd numbered elements the values, e.g.
	 *            <code>new String[] {"path", "/Public", "locale",
	 *         "en"}</code>.
	 * @param session
	 *            the {@link Session} to use for this request.
	 * 
	 * @return a parsed JSON object, typically a Map or a JSONArray.
	 * 
	 * @throws VDiskServerException
	 *             if the server responds with an error code. See the constants
	 *             in {@link VDiskServerException} for the meaning of each error
	 *             code.
	 * @throws VDiskIOException
	 *             if any network-related error occurs.
	 * @throws VDiskUnlinkedException
	 *             if the user has revoked access.
	 * @throws VDiskParseException
	 *             if a malformed or unknown response was received from the
	 *             server.
	 * @throws VDiskException
	 *             for any other unknown errors. This is also a superclass of
	 *             all other VDisk exceptions, so you may want to only catch
	 *             this exception which signals that some kind of error
	 *             occurred.
	 */
	static public Object request(RequestMethod method, String host,
			String path, int apiVersion, String[] params, Session session)
			throws VDiskException {
		HttpResponse resp = streamRequest(method, host, path, apiVersion,
				params, session).response;
		return parseAsJSON(resp);
	}

	static public Object request(RequestMethod method, String host,
			String path, int apiVersion, String[] params, Session session,
			int socketTimeoutOverrideMs) throws VDiskException {
		HttpResponse resp = streamRequest(method, host, path, apiVersion,
				params, session, true, socketTimeoutOverrideMs).response;
		return parseAsJSON(resp);
	}

	/**
	 * Creates and sends a request to the VDisk API, and returns a
	 * {@link RequestAndResponse} containing the {@link HttpUriRequest} and
	 * {@link HttpResponse}.
	 * 
	 * @param method
	 *            GET or POST.
	 * @param host
	 *            the hostname to use. Should be either api server, content
	 *            server, or web server.
	 * @param path
	 *            the URL path, starting with a '/'.
	 * @param apiVersion
	 *            the API version to use. This should almost always be set to
	 *            {@code VDiskAPI.VERSION}.
	 * @param params
	 *            the URL params in an array, with the even numbered elements
	 *            the parameter names and odd numbered elements the values, e.g.
	 *            <code>new String[] {"path", "/Public", "locale",
	 *         "en"}</code>.
	 * @param session
	 *            the {@link Session} to use for this request.
	 * 
	 * @return returns a {@link RequestAndResponse} containing the
	 *         {@link HttpUriRequest} and {@link HttpResponse}.
	 * 
	 * @throws VDiskServerException
	 *             if the server responds with an error code. See the constants
	 *             in {@link VDiskServerException} for the meaning of each error
	 *             code.
	 * @throws VDiskIOException
	 *             if any network-related error occurs.
	 * @throws VDiskUnlinkedException
	 *             if the user has revoked access.
	 * @throws VDiskException
	 *             for any other unknown errors. This is also a superclass of
	 *             all other VDisk exceptions, so you may want to only catch
	 *             this exception which signals that some kind of error
	 *             occurred.
	 */
	static public RequestAndResponse streamRequest(RequestMethod method,
			String host, String path, int apiVersion, String params[],
			Session session) throws VDiskException {
		return streamRequest(method, host, path, apiVersion, params, session,
				true, -1);
	}

	static public RequestAndResponse streamRequest(RequestMethod method,
			String host, String path, int apiVersion, String params[],
			Session session, boolean needSign, int socketTimeoutOverrideMs)
			throws VDiskException {
		String target = null;

		if (method == RequestMethod.GET) {
			target = buildURL(host, apiVersion, path, params);
		} else {
			target = buildURL(host, apiVersion, path, null);
		}
		logger.info("url:" + target);
		return streamRequestAndResponse(method, session, target, params, 0,
				null, needSign, socketTimeoutOverrideMs);
	}

	static public RequestAndResponse streamRequestAndResponse(
			RequestMethod method, Session session, String target,
			String params[]) throws VDiskException {
		return streamRequestAndResponse(method, session, target, params, 0,
				null, false, -1);
	}

	static public RequestAndResponse streamRequestAndResponse(
			RequestMethod method, Session session, String target,
			String params[], long range, String md5, boolean needSign,
			int socketTimeoutOverrideMs) throws VDiskException {
		String curlHeader = "";


		HttpUriRequest req = null;
		if (method == RequestMethod.GET) {
			req = new HttpGet(target);
			if (range > 0 && md5 != null) {
				req.setHeader("Range", "bytes=" + range + "-");// 设置下载范围 // Set download range
				req.setHeader("If-Range", "\"" + md5 + "\"");
			}
		} else {
			HttpPost post = new HttpPost(target);

			if (params != null && params.length >= 2) {
				if (params.length % 2 != 0) {
					throw new IllegalArgumentException(
							"Params must have an even number of elements.");
				}
				ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();

				StringBuilder postParams = new StringBuilder();
				for (int i = 0; i < params.length; i += 2) {
					if (params[i + 1] != null) {
						nvps.add(new BasicNameValuePair(params[i],
								params[i + 1]));
						postParams.append(params[i]).append("=")
								.append(params[i + 1]).append("&");
					}
				}

				try {
					post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
				} catch (UnsupportedEncodingException e) {
					throw new VDiskException(e);
				}
			}

			req = post;
		}

		if (needSign) {
			session.sign(req);
		}

		HttpResponse resp = null;
		if (socketTimeoutOverrideMs >= 0) {
			resp = execute(session, req, socketTimeoutOverrideMs);
		} else {
			resp = execute(session, req);
		}
		return new RequestAndResponse(req, resp);
	}

	/**
	 * Reads in content from an {@link HttpResponse} and parses it as JSON.
	 * 
	 * @param response
	 *            the {@link HttpResponse}.
	 * 
	 * @return a parsed JSON object, typically a Map or a JSONArray.
	 * 
	 * @throws VDiskServerException
	 *             if the server responds with an error code. See the constants
	 *             in {@link VDiskServerException} for the meaning of each error
	 *             code.
	 * @throws VDiskIOException
	 *             if any network-related error occurs while reading in content
	 *             from the {@link HttpResponse}.
	 * @throws VDiskUnlinkedException
	 *             if the user has revoked access.
	 * @throws VDiskParseException
	 *             if a malformed or unknown response was received from the
	 *             server.
	 * @throws VDiskException
	 *             for any other unknown errors. This is also a superclass of
	 *             all other VDisk exceptions, so you may want to only catch
	 *             this exception which signals that some kind of error
	 *             occurred.
	 */
	public static Object parseAsJSON(HttpResponse response)
			throws VDiskException {
		Object result = null;

		BufferedReader bin = null;

		try {
			HttpEntity ent = response.getEntity();
			if (ent != null) {
				InputStreamReader in = new InputStreamReader(ent.getContent());
				// Wrap this with a Buffer, so we can re-parse it if it's
				// not JSON
				// Has to be at least 16384, because this is defined as the
				// buffer size in
				// org.json.simple.parser.Yylex.java
				// and otherwise the reset() call won't work
				bin = new BufferedReader(in, 16384);
				bin.mark(16384);
				
				String jsonText = readAll(bin);
				result = JSONObject.fromObject(jsonText);
			}
		} catch (IOException e) {
			throw new VDiskIOException(e);
		} catch (OutOfMemoryError e) {
			throw new VDiskException(e);
		} finally {
			if (bin != null) {
				try {
					bin.close();
				} catch (IOException e) {
				}
			}
		}

		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != VDiskServerException._200_OK) {
			if (statusCode == VDiskServerException._401_UNAUTHORIZED) {
			
				throw new VDiskUnlinkedException();
			} else {
				throw new VDiskServerException(response, result);
			}
		} else {
			if (result == null) {
				throw new VDiskParseException(
						"Response code is 200, but body is empty");
			}
		}


		return result;
	}
	
	/* JSON utility method */
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	/**
	 * Reads in content from an {@link HttpResponse} and parses it as a query
	 * string.
	 * 
	 * @param response
	 *            the {@link HttpResponse}.
	 * 
	 * @return a map of parameter names to values from the query string.
	 * 
	 * @throws VDiskIOException
	 *             if any network-related error occurs while reading in content
	 *             from the {@link HttpResponse}.
	 * @throws VDiskParseException
	 *             if a malformed or unknown response was received from the
	 *             server.
	 * @throws VDiskException
	 *             for any other unknown errors. This is also a superclass of
	 *             all other VDisk exceptions, so you may want to only catch
	 *             this exception which signals that some kind of error
	 *             occurred.
	 */
	public static Map<String, String> parseAsQueryString(HttpResponse response)
			throws VDiskException {
		HttpEntity entity = response.getEntity();

		if (entity == null) {
			throw new VDiskParseException("Bad response from VDisk.");
		}

		Scanner scanner;
		try {
			scanner = new Scanner(entity.getContent()).useDelimiter("&");
		} catch (IOException e) {
			throw new VDiskIOException(e);
		}

		Map<String, String> result = new HashMap<String, String>();

		while (scanner.hasNext()) {
			String nameValue = scanner.next();
			String[] parts = nameValue.split("=");
			if (parts.length != 2) {
				throw new VDiskParseException("Bad query string from VDisk.");
			}
			result.put(parts[0], parts[1]);
		}

		return result;
	}

	/**
	 * Executes an {@link HttpUriRequest} with the given {@link Session} and
	 * returns an {@link HttpResponse}.
	 * 
	 * @param session
	 *            the session to use.
	 * @param req
	 *            the request to execute.
	 * 
	 * @return an {@link HttpResponse}.
	 * 
	 * @throws VDiskServerException
	 *             if the server responds with an error code. See the constants
	 *             in {@link VDiskServerException} for the meaning of each error
	 *             code.
	 * @throws VDiskIOException
	 *             if any network-related error occurs.
	 * @throws VDiskUnlinkedException
	 *             if the user has revoked access.
	 * @throws VDiskException
	 *             for any other unknown errors. This is also a superclass of
	 *             all other VDisk exceptions, so you may want to only catch
	 *             this exception which signals that some kind of error
	 *             occurred.
	 */
	public static HttpResponse execute(Session session, HttpUriRequest req)
			throws VDiskException {
		return execute(session, req, -1);
	}

	/**
	 * Executes an {@link HttpUriRequest} with the given {@link Session} and
	 * returns an {@link HttpResponse}.
	 * 
	 * @param session
	 *            the session to use.
	 * @param req
	 *            the request to execute.
	 * @param socketTimeoutOverrideMs
	 *            if >= 0, the socket timeout to set on this request. Does
	 *            nothing if set to a negative number.
	 * 
	 * @return an {@link HttpResponse}.
	 * 
	 * @throws VDiskServerException
	 *             if the server responds with an error code. See the constants
	 *             in {@link VDiskServerException} for the meaning of each error
	 *             code.
	 * @throws VDiskIOException
	 *             if any network-related error occurs.
	 * @throws VDiskUnlinkedException
	 *             if the user has revoked access.
	 * @throws VDiskException
	 *             for any other unknown errors. This is also a superclass of
	 *             all other VDisk exceptions, so you may want to only catch
	 *             this exception which signals that some kind of error
	 *             occurred.
	 */
	public static HttpResponse execute(Session session, HttpUriRequest req,
			int socketTimeoutOverrideMs) throws VDiskException {
		HttpClient client = updatedHttpClient(session);
		// Set request timeouts.
		session.setRequestTimeout(req);
		if (socketTimeoutOverrideMs >= 0) {
			HttpParams reqParams = req.getParams();
			HttpConnectionParams.setSoTimeout(reqParams,
					socketTimeoutOverrideMs);
		}

		boolean retry = true;
		int executionCount = 0;
		IOException cause = null;
		HttpRequestRetryHandler retryHandler = ((DefaultHttpClient) client)
				.getHttpRequestRetryHandler();

		HttpContext localContext = new BasicHttpContext();

		while (retry) {
			try {
				HttpResponse response = null;
				for (int retries = 0; response == null && retries < 5; retries++) {
					/*
					 * The try/catch is a workaround for a bug in the HttpClient
					 * libraries. It should be returning null instead when an
					 * error occurs. Fixed in HttpClient 4.1, but we're stuck
					 * with this for now. See:
					 * http://code.google.com/p/android/issues/detail?id=5255
					 */
					try {
						response = client.execute(req, localContext);
					} catch (NullPointerException e) {
					}

					/*
					 * We've potentially connected to a different network, but
					 * are still using the old proxy settings. Refresh proxy
					 * settings so that we can retry this request.
					 */
					if (response == null) {
						updateClientProxy(client, session);
					}
				}

				if (response == null) {
					// This is from that bug, and retrying hasn't fixed it.
					throw new VDiskIOException(
							"Apache HTTPClient encountered an error. No response, try again.");
				} else if (response.getStatusLine().getStatusCode() != VDiskServerException._200_OK) {
					// This will throw the right thing: either a
					// VDiskServerException or a VDiskProxyException
					int code = response.getStatusLine().getStatusCode();
					//Log.d("StatusCode", "StatusCode:" + code);

					// 如果发现是断点下载返回的206或者302跳转，直接返回response
					// If response code is 206 or 302,directly return response
					if (code == VDiskServerException._206_OK
							|| code == VDiskServerException._302_FOUND) {
						return response;
					}
					parseAsJSON(response);
				}

				return response;
			} catch (SSLException e) {
				/*if (Logger.DEBUG_MODE) {
					Logger.writeException(e);
				}*/
				throw new VDiskSSLException(e);
			} catch (IOException e) {
				// Quite common for network going up & down or the request being
				// cancelled, so don't worry about logging this
				cause = e;
				//Log.d(TAG, "execute IOException:retry count=" + executionCount);

				if (req.isAborted()) {
					throw new VDiskIOException(e);
				}

				retry = retryHandler.retryRequest(cause, ++executionCount,
						localContext);

				e.printStackTrace();

				/*if (Logger.DEBUG_MODE) {
					if (cause != null) {
						if (retry) {
							Logger.writeException("retry:" + executionCount, e);
						} else {
							Logger.writeException("at last:", e);
						}
					}
				}*/

			} catch (OutOfMemoryError e) {
				/*if (Logger.DEBUG_MODE) {
					Logger.writeException(e);
				}*/

				throw new VDiskException(e);
			}
		}

		throw new VDiskIOException(cause);
	}

	/**
	 * Creates a URL for a request to the VDisk API.
	 * 
	 * @param host
	 *            the VDisk host (i.e., api server, content server, or web
	 *            server).
	 * @param apiVersion
	 *            the API version to use. You should almost always use
	 *            {@code VDiskAPI.VERSION} for this.
	 * @param target
	 *            the target path, staring with a '/'.
	 * @param params
	 *            any URL params in an array, with the even numbered elements
	 *            the parameter names and odd numbered elements the values, e.g.
	 *            <code>new String[] {"path", "/Public", "locale",
	 *         "en"}</code>.
	 * 
	 * @return a full URL for making a request.
	 */
	public static String buildURL(String host, int apiVersion, String target,
			String[] params) {
		if (!target.startsWith("/")) {
			target = "/" + target;
		}

		try {
			// We have to encode the whole line, then remove + and / encoding
			// to get a good OAuth URL.
			target = URLEncoder.encode("/" + apiVersion + target, "UTF-8");
			target = target.replace("%2F", "/");

			if (params != null && params.length > 0) {
				target += "?" + urlencode(params);
			}

			// These substitutions must be made to keep OAuth happy.
			target = target.replace("+", "%20").replace("*", "%2A");
		} catch (UnsupportedEncodingException uce) {
			return null;
		}

		int index = host.lastIndexOf(":");
		if (index != -1) {
			return "https://" + host + target;
		}

		if (host.equals("upload-vdisk.sina.com.cn")) {
			return "https://" + host + target;
		}
		return "https://" + host + ":443" + target;
		//return "https://" + host + "" + target;
	}

	/**
	 * Parses a date/time returned by the VDisk API. Returns null if it cannot
	 * be parsed.
	 * 
	 * @param date
	 *            a date returned by the API.
	 * 
	 * @return a {@link Date}.
	 */
	public static Date parseDate(String date) {
		try {
			return dateFormat.parse(date);
		} catch (java.text.ParseException e) {
			return null;
		}
	}

	/**
	 * Gets the session's client and updates its proxy.
	 */
	private static synchronized HttpClient updatedHttpClient(Session session) {
		HttpClient client = session.getHttpClient();
		updateClientProxy(client, session);
		return client;
	}

	/**
	 * Updates the given client's proxy from the session.
	 */
	private static void updateClientProxy(HttpClient client, Session session) {
		ProxyInfo proxyInfo = session.getProxyInfo();
		if (proxyInfo != null && proxyInfo.host != null
				&& !proxyInfo.host.equals("")) {
			HttpHost proxy;
			if (proxyInfo.port < 0) {
				proxy = new HttpHost(proxyInfo.host);
			} else {
				proxy = new HttpHost(proxyInfo.host, proxyInfo.port);
			}
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		} else {
			client.getParams().removeParameter(ConnRoutePNames.DEFAULT_PROXY);
		}
	}

	/**
	 * URL encodes an array of parameters into a query string.
	 */
	public static String urlencode(String[] params) {
		if (params.length % 2 != 0) {
			throw new IllegalArgumentException(
					"Params must have an even number of elements.");
		}

		String result = "";
		try {
			boolean firstTime = true;
			for (int i = 0; i < params.length; i += 2) {
				if (params[i + 1] != null) {
					if (firstTime) {
						firstTime = false;
					} else {
						result += "&";
					}
					result += URLEncoder.encode(params[i], "UTF-8") + "="
							+ URLEncoder.encode(params[i + 1], "UTF-8");
				}
			}
			result.replace("*", "%2A");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		return result;
	}

	static public HttpUriRequest streamHttpUriRequest(String url,
			String[] params) {
		HttpUriRequest req = null;
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");// 必须进行encode处理，否则报错 // Encode setting must be done to avoid an Error!
		byte[] data = null;
		String postParam = urlencode(params);
		//Log.e(TAG, postParam);
		try {
			data = postParam.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		ByteArrayEntity formEntity = new ByteArrayEntity(data);
		post.setEntity(formEntity);
		req = post;
		return req;
	}

	static public String parseResponse(Session session, String url,
			String[] params) throws VDiskException {

		String result = "";
		try {
			HttpUriRequest req;
			req = streamHttpUriRequest(url, params);
			////Log.d(TAG, url);
			HttpResponse response = execute(session, req);
			StatusLine status = response.getStatusLine();
			int statusCode = status.getStatusCode();
			////Log.e(TAG, statusCode + "");
			if (statusCode != 200) {
				result = read(response);
				////Log.e(TAG, result + "");
				String err = null;
				int errCode = 0;
				//JSONObject json = new JSONObject(result);
				JSONObject json = JSONObject.fromObject(result);
				err = json.getString("error");
				errCode = json.getInt("error_code");
				//Log.d(TAG, "errCode-->" + errCode + ";err-->" + err);
				return result;
			}
			// parse content stream from response
			result = read(response);
		} catch (VDiskException e) {
			throw new VDiskException(e);
		}
		return result;
	}

	/*
	 * response parse to json
	 */

	static private String read(HttpResponse response) throws VDiskException {
		String result = "";
		HttpEntity entity = response.getEntity();
		InputStream inputStream;
		try {
			inputStream = entity.getContent();
			ByteArrayOutputStream content = new ByteArrayOutputStream();
			Header header = response.getFirstHeader("Content-Encoding");
			if (header != null
					&& header.getValue().toLowerCase().indexOf("gzip") > -1) {
				inputStream = new GZIPInputStream(inputStream);
			}
			int readBytes = 0;
			byte[] sBuffer = new byte[512];
			while ((readBytes = inputStream.read(sBuffer)) != -1) {
				content.write(sBuffer, 0, readBytes);
			}
			result = new String(content.toByteArray());
			return result;
		} catch (IllegalStateException e) {
			throw new VDiskException(e);
		} catch (IOException e) {
			throw new VDiskException(e);
		}
	}
}
