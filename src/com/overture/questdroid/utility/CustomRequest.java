package com.overture.questdroid.utility;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;    

import javax.xml.transform.Templates;

import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONException;
import org.json.JSONObject;    

import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.overture.questdroid.app.VolleyController;

    public class CustomRequest extends Request<JSONObject> {

    private Listener<JSONObject> listener;
    private Map<String, String> params;

    public CustomRequest(String url, Map<String, String> params,
            Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    public CustomRequest(int method, String url, Map<String, String> params,
            Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }
    
    @Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		//Map<String, String> headers = new HashMap<String, String>();

    	Map<String, String> headers = super.getHeaders();
    	if (headers.isEmpty()){
    		headers = new HashMap<String, String>();
    		BasicClientCookie cur_cookie = VolleyController.getInstance().getCurCookie();
    		if( cur_cookie != null) {
    			StringBuilder builder = new StringBuilder(cur_cookie.getValue());
    			headers.put("SPRING_SECURITY_REMEMBER_ME_COOKIE",builder.toString());
    		}
    			
    	}
    		
    		
    	else if(headers.containsKey("Cookie")){
    		StringBuilder builder = new StringBuilder();
    		String header_cookie = headers.get("Cookie");
    		builder.append(header_cookie);
    		builder.append("; ");
    		builder.append("SPRING_SECURITY_REMEMBER_ME_COOKIE=" + VolleyController.getInstance().getCurCookie().getValue());
    		
    	}
    	String auth = "Basic "
				+ Base64.encodeToString(("handfree" + ":" + "autumnleaf").getBytes(),
						Base64.NO_WRAP);
		headers.put("Authorization", auth);
		return headers;
	}
    
    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    };

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
    	
    	try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            //Log.d("Wei Wang", jsonString);
            JSONObject temp = new JSONObject(jsonString);
            temp.accumulate("Set-Cookie", response.headers.get("Set-Cookie"));
            return Response.success(temp,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        // TODO Auto-generated method stub
        listener.onResponse(response);
    }
    }