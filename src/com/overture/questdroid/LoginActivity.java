package com.overture.questdroid;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request.Method;
import com.lidroid.xutils.util.LogUtils;
import com.overture.questdroid.R;
import com.overture.questdroid.app.VolleyController;
import com.overture.questdroid.utility.BaseActivity;
import com.overture.questdroid.utility.CustomReqListener;
import com.overture.questdroid.utility.CustomRequest;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
                String url = "https://test.snaapiq.com/api2/user/login.json";
                Map<String, String> params = new HashMap<String, String>();
                params.put("j_username", "questassignee+test0@gmail.com");
                params.put("j_password", "test1");
//                params.put("j_username", name);
//                params.put("j_password", password);
                params.put("_spring_security_remember_me", "true");
                CustomRequest myReq = new CustomRequest(Method.POST, url, params, 
                                                        CustomReqListener.loginListener(LoginActivity.this),
                                                        CustomReqListener.ErrorListener(LoginActivity.this)
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

}
