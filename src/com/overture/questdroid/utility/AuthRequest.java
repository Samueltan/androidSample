package com.overture.questdroid.utility;

import java.util.HashMap;
import java.util.Map;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
/***
 * 
 * @author weiwang
 *currently unused
 */
public class AuthRequest extends StringRequest {
	public AuthRequest(int method, String url, Listener<String> listener, ErrorListener errorListener) {
		super(method, url, listener, errorListener);
	}


	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = new HashMap<String, String>();
		String auth = "Basic "
				+ Base64.encodeToString(("handfree" + ":" + "autumnleaf").getBytes(),
						Base64.NO_WRAP);
		headers.put("Authorization", auth);
		return headers;
	}
}