package com.overture.questdroid.utility;

import java.util.Date;
import java.util.List;

import javax.xml.transform.Templates;

import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.overture.questdroid.ContestsActivity;
import com.overture.questdroid.R;
import com.overture.questdroid.DB.user;
import com.overture.questdroid.DB.userDao.Properties;
import com.overture.questdroid.app.VolleyController;
import com.overture.questdroid.fragment.ContestsFragment;
import com.overture.questdroid.fragment.ContestsFragment.ImageListAdapter;
import com.overture.questdroid.fragment.ItemRow;

/**
 * 
 * @author weiwang
 * 
 */
public class CustomReqListener {
	/**
	 * use this method to generate a login request
	 * @param context current activity
	 * @return response listener
	 */	
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
	                			else Toast.makeText(context,"Unable to login for unknown reason",
		                				Toast.LENGTH_SHORT).show();
	                				
	                		}
	                		else Toast.makeText(context,"The cookie that received is invalid",
	                				Toast.LENGTH_SHORT).show();
	                	}
	                } catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	        };
	    }
	/***
	 * Use this method to generate common request, without 
	 * @param context current activity
	 * @return
	 */
	public static Response.Listener<JSONObject> requestListenerWithToken(final Context context) {
			
			return new Response.Listener<JSONObject>() {
	            @Override
	            public void onResponse(JSONObject response) {
	                try {
	                	
	                	/*here save the response to the right place as a JSONObject 
	                	 * either as a global variable, or in the database.
	                	 * All Http request should use this listener except "login".
	                	 */
	                	//Toast is used for debugging only. Should consider to remove this part
	                	Toast.makeText(context, 
	                					"response to a post request: "+ response.get("success").toString(),
	                					Toast.LENGTH_SHORT
	                					).show();
	                } catch (Exception e) {
	                	Toast.makeText(context, "fail to request the data", Toast.LENGTH_SHORT).show();
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	        };
	    }
	
	
	public static Response.Listener<JSONObject> successListenerUser(final Context context) {
		
		return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                	VolleyController.getInstance().user = response;

                	JSONObject user = VolleyController.getInstance().user;	
                	String coin = user.getString("coin");
                    
                	ContestsActivity ca = (ContestsActivity)context;
                    View view = ca.findViewById(android.R.id.content);
                    TextView userCoin = (TextView)view.findViewById(R.id.tv_user_coin_amount);
                    userCoin.setText(coin);
                	
//                	user entity = new user(null, response.optLong("id"), response.optString("username"), 
//                								response.optString("avatarUrl"), response.optLong("coin"));
//                	VolleyController.getInstance().getDao().insert(entity);
//                	List<user> joes = VolleyController.getInstance().getDao().queryBuilder()
//                			.where(Properties.ID.eq((long) 1))
//                			.list();
//                	LogUtils.d("__________________"+ joes.get(joes.size()-1).getAvatarurl());
//                	
                	
                } 
                catch (Exception e) {
					// TODO Auto-generated catch block
                	Toast.makeText(context, "fail to request the data", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
            }
        };
    }
	
//	public static Response.Listener<JSONObject> successListenerGlobalList(final Fragment fragment) {
//		
//		return new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                	VolleyController.getInstance().globalList = response;
//                	JSONArray contentArr = VolleyController.getInstance().globalList.optJSONArray("content");
//        
//                	for(int i=0; i<contentArr.length(); ++i){
//                		JSONObject content = contentArr.optJSONObject(i);
//                		
//                		// Get overview information
//                    	String cashValue = content.optString("cashValue");
//                    	String winnerReward = content.optString("winnerReward");
//                    	String contestName = content.optString("name");
//                    	String contestCoin = content.optString("winnerCoin");
//                    	String beginDate = longToDayCounts(content.optLong("beginDate"));
//                    	String Collabration = content.optString("collaborationTotal");
//                    	
//                		JSONObject topfav = content.optJSONObject("topFavorQuestMedia");
//                		String imgUrl = "";
//                		if(topfav!=null)
//                			imgUrl = topfav.optString("objectSignedUrl");
//                		
//                    	// Get detailed information
//                    	int winnerNums = Integer.parseInt(content.optString("winnerNums"));
//                    	String strWinnerNums = "Up to " + winnerNums + " Possible Winner";
//                    	if(winnerNums > 1) strWinnerNums += "s";
//                    	String rectBanner = imgUrl;		// To be updated!
//                    	String description = content.optString("description");
//                    	
//                    	String fragmentType = fragment.getClass().getSimpleName();
//                        if (fragmentType.equals("ContestsFragment")) {
//                        	ContestsFragment cFragment = (ContestsFragment)fragment;
//                        	ItemRow row = new ItemRow(imgUrl, cashValue, winnerReward, contestName, 
//                        			contestCoin, beginDate, Collabration,
//                        			rectBanner, strWinnerNums, description);
//                        	cFragment.injectListData(row);
//                        }else if (fragmentType.equals("RateFragment")) {
//                        	// Todo
//                        }else if (fragmentType.equals("MarketFragment")) {
//                        	// Todo
//                        }else if (fragmentType.equals("ActivityFragment")) {
//                        	// Todo
//                        }else if (fragmentType.equals("ProfileFragment")) {
//                        	// Todo
//                        }
//                	}
//                	
//                	
//                } catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//            }
//        };
//    }
	
	/**
	 * Function to convert the date long to a valid day count number
	 */
	public static String longToDayCounts(long beginDateLong){
		String result = "";
		double leftDays = (((new Date().getTime() - beginDateLong) / 1000.0) / 3600.0) / 24.0;
		int leftWeek, leftMonth, leftYear;
		if(leftDays < 7){			// Less than 1 week
			if(leftDays < 2)
				result = "1d";
			else
				result = (int)leftDays + "ds";
		}else if(leftDays < 30){	// Less than 1 month
			leftWeek = (int)(leftDays / 7);
			if(leftWeek < 2)
				result = "1w";
			else
				result = leftWeek + "ws";
		}else if(leftDays < 365){	// Less than 1 year
			leftMonth = (int)(leftDays / 30);
			if(leftMonth < 2)
				result = "1m";
			else
				result = leftMonth + "ms";
		}else if(leftDays < 365 * 2){
			result = "1y";
		}else{
			leftYear = (int)(leftDays / 365);
			result = leftYear + "ys";
		}
		
		return result;		
	}
	/***
	 * use this method to listen error
	 * @param context current activity
	 * @return
	 */
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
