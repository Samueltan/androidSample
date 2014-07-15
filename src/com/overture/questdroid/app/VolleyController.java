/**
 * Copyright 2013 Ognyan Bankov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.overture.questdroid.app;

import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.overture.questdroid.DB.DaoMaster;
import com.overture.questdroid.DB.DaoSession;
import com.overture.questdroid.DB.userDao;
import com.overture.questdroid.DB.DaoMaster.DevOpenHelper;
import com.overture.questdroid.utility.BitmapLruCache;
import com.overture.questdroid.utility.ExtHttpClientStack;
import com.overture.questdroid.utility.MySSLSocketFactory;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * Application class for the demo. Used to ensure that VolleyHelper is initialized. {@see VolleyHelper}
 * @author Wei Wang
 *
 */
public class VolleyController extends Application {
	public static final String TAG = VolleyController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    public double latitude;
    public double longitude;
    public boolean isActive;
    public JSONObject user;
    public JSONObject globalList;
    public JSONObject userlist;
    
	private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private userDao getuser;

 
    //instance of questqueue
    private static VolleyController mInstance;
    //global variance -- cookie
    private BasicClientCookie currentCookie;
 
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        currentCookie = null;
//		DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
//        db = helper.getWritableDatabase();
//        daoMaster = new DaoMaster(db);
//        daoSession = daoMaster.newSession();
//        getuser = daoSession.getUserDao();
    }
 
    
    
    
    public synchronized BasicClientCookie getCurCookie(){
    	return currentCookie;
    }
    
    public synchronized userDao getDao(){
    	return getuser;
    }
    
    public void setCurCookie(BasicClientCookie cookie) {
    	currentCookie = cookie;
    }
    
    public static synchronized VolleyController getInstance() {
        return mInstance;
    }
 
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext(),
            		new ExtHttpClientStack(MySSLSocketFactory.createMyHttpClient()));
        }
 
        return mRequestQueue;
    }
 
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new BitmapLruCache());
        }
        return this.mImageLoader;
    }
 
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
 
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
 
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
