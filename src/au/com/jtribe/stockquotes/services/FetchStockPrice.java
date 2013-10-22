package au.com.jtribe.stockquotes.services;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class FetchStockPrice {
	private final String TAG = FetchStockPrice.class.getSimpleName();
	//private final String STOCK_CODE = "INDEXHANGSENG:HSI";
	private final String STOCK_CODE = "RIO";
	private final String URL = "http://www.google.com/finance/info?infotype=infoquoteall&q=" + STOCK_CODE;

	public String getLatestStockPrice() {
		String response = getJsonStockResponse();
		String stockValue = extractStockValueFromResponse(response);
		return stockValue;
	}
	
	private String getJsonStockResponse() {
		DefaultHttpClient httpClient = new DefaultHttpClient();

		HttpGet httpGet = new HttpGet(URL);
		httpGet.setHeader("Accept", "application/json");
		httpGet.setHeader("Content-Type", "application/json");
		
		HttpResponse response = null;

		try {
			response = httpClient.execute(httpGet);
		} catch (Exception e) {
			Log.e(TAG, "Error executing httpGet", e);
		}

		if (response == null) {
			return null;
		}

		String ret = null;
		try {
			ret = EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			Log.e(TAG, "Error converting response entity to string", e);
		}

		ret = ret.replace("\n", "");
		ret = ret.substring(3);
		return ret;
	}

	private String extractStockValueFromResponse(String response) {	
		String stockValue = null;
		try {
			if (response == null || response.equals("")) {
				return null;
			}

			JSONArray responseArray = new JSONArray(response);
			JSONObject stockObject = (JSONObject) responseArray.get(0);

			stockValue = extractJsonValue(stockObject, "l");

		} catch (Exception e) {
			Log.e(TAG, "Error extracting stock value from response", e);
		}
		return stockValue;
	}

	private String extractJsonValue(JSONObject tweetObject, String jsonParam) throws JSONException {
		Object jsonVal = tweetObject.get(jsonParam);

		if (jsonVal != null) {
			return jsonVal.toString();
		} else {
			return "";
		}
	}
}