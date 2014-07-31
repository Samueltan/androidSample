package com.overture.questdroid;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.lidroid.xutils.util.LogUtils;
import com.overture.questdroid.R;
import com.overture.questdroid.app.VolleyController;
import com.overture.questdroid.utility.BaseActivity;
import com.overture.questdroid.utility.CustomReqListener;
import com.overture.questdroid.utility.CustomRequest;
import com.overture.questdroid.utility.Parse_tools;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author weiwang
 *
 */
public class LoginActivity extends BaseActivity {

	private EditText usr_name;
    private EditText usr_password;
    private TextView testTextView; 
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        LogUtils.customTagPrefix = "questDroid-debug:";
        LogUtils.allowI = false;

		setContentView(R.layout.activity_login_page);
		usr_name =  (EditText) findViewById(R.id.user_name);
        usr_password = (EditText) findViewById(R.id.user_password);
        testTextView = (TextView) findViewById(R.id.result);		
        
        
        Button usrLogin = (Button) findViewById(R.id.login_button);
        usrLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	final String tag_auth_obj = "json_auth_req";
                final String name = usr_name.getText().toString();
                final String password = usr_password.getText().toString();
            
                /*set login parameters, url, http request type*/
                String url = getApplicationContext().getString(R.string.api_login);
                Map<String, String> params = new HashMap<String, String>();
                params.put("j_username", "questassignee+test0@gmail.com");
                params.put("j_password", "test1");
//                params.put("j_username", name);
//                params.put("j_password", password);
                params.put("_spring_security_remember_me", "true");
                CustomRequest myReq = new CustomRequest(Method.POST, url, params, 
                                                        loginListener(LoginActivity.this),
                                                        ErrorListener(LoginActivity.this)
                                                        );
                
                //add the request to the requestqueue. handled by Volley
                VolleyController.getInstance().addToRequestQueue(myReq, tag_auth_obj);
            }
        });
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public static Response.Listener<JSONObject> loginListener(final Context context) {
		
		return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response){
            	String recvCookie;
            	String successStatus;
            	BasicClientCookie cookie = null;
                LogUtils.customTagPrefix = "questDroid-debug:";
                try {
                	recvCookie = (String) response.get("Set-Cookie");
                	successStatus = response.optString("success");
                	if(recvCookie != null && successStatus != null) {
                		cookie = Parse_tools.parseRawCookie(recvCookie);
                		if(cookie.getValue() != null) {
                			VolleyController.getInstance().setCurCookie(cookie);
                			if(successStatus.equals("true")){
                		        Intent intent = new Intent(context, ContestsActivity.class);
                				context.startActivity(intent);
                			}
                			else Toast.makeText(context,
                					context.getString(R.string.warn_unknownLoginFailure),
	                				Toast.LENGTH_SHORT).show();
                				
                		}
                		else Toast.makeText(context,context.getString(R.string.warn_invalidCookie),
                				Toast.LENGTH_SHORT).show();
                	}
                } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        };
    }
	
	public static Response.ErrorListener ErrorListener(final Context context) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	Toast.makeText(context, new String (error.getMessage() + "\n" +
            error.networkResponse.data), Toast.LENGTH_LONG).show();
            }
        };
    }
}
