package com.overture.questdroid.fragment;

import android.R.array;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.android.volley.Response;
import com.android.volley.Request.Method;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.overture.questdroid.BitmapHelp;
import com.overture.questdroid.ContestsActivity;
import com.overture.questdroid.R;
import com.overture.questdroid.DB.user;
import com.overture.questdroid.app.VolleyController;
import com.overture.questdroid.utility.CustomReqListener;
import com.overture.questdroid.utility.CustomRequest;
import com.overture.questdroid.utility.Parse_tools;
import com.overture.questdroid.utility.TabButton;

/**
 * Author: Samuel & Wei
 * Date: 14-6-29
 * Contests fragment: handle the image list of the contests and their related information
 */
public class ProfileFragment extends Fragment{

    public static BitmapUtils bitmapUtils;
    Typeface font;
    @ViewInject(R.id.profile_setting_icon)
    private TextView settingIcon;
    @ViewInject(R.id.profile_star_icon)
    private TextView avgStar;
    @ViewInject(R.id.profile_coin_icon1)
    private TextView coinIcon1;
    @ViewInject(R.id.profile_coin_icon2)
    private TextView coinIcon2;
    @ViewInject(R.id.profile_toggle_icon)
    private TextView toggleIcon;
    @ViewInject(R.id.profile_grid_icon)
    private TextView gridIcon;
    private TabButton profileLeft;
    private TabButton profileRight;  



    @ViewInject(R.id.profile_img_list)
    private ListView profileImgList;

    private ImageListAdapter imageListAdapter;
    private JSONObject user = VolleyController.getInstance().user;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_profile, container, false); // Load fragment layout
        ViewUtils.inject(this, view); 		//Bind view and event
        
        bitmapUtils = BitmapHelp.getBitmapUtils(getActivity().getApplicationContext());
        bitmapUtils.configDefaultLoadingImage(R.drawable.ic_launcher);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_launcher);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
        
        profileLeft = (TabButton) getActivity().findViewById(R.id.btn_profile);
        profileRight = (TabButton) getActivity().findViewById(R.id.btn_reward);
        
        TextView profileHeader = (TextView) view.findViewById(R.id.profile_header_title);
        profileHeader.setText(user.optString("username"));
        ImageView userAvatar  = (ImageView) view.findViewById(R.id.profile_usr_avatar);
        bitmapUtils.display(userAvatar, user.optString("avatarUrl"));
        TextView profileUsername = (TextView) view.findViewById(R.id.profile_usrname);
        profileUsername.setText(user.optString("username"));
        TextView profileTotalCoin = (TextView) view.findViewById(R.id.profile_total_earned);
        profileTotalCoin.setText("Total Earned: "+user.optString("maxHistoryCoin"));
        TextView profileAvailableCoin = (TextView) view.findViewById(R.id.profile_available_coin);
        profileAvailableCoin.setText("Available: " + user.optString("coin"));
        TextView profileAvgRate= (TextView) view.findViewById(R.id.profile_average_star);
        //profileAvgRate.setText(user.optString("coin"));
        profileAvgRate.setText("5");
        // Load fondawesome icons
        font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf" );
        settingIcon.setTypeface(font);
        avgStar.setTypeface(font);
        coinIcon1.setTypeface(font);
        coinIcon2.setTypeface(font);
        toggleIcon.setTypeface(font);
        gridIcon.setTypeface(font);

        // Load image when scrolling (No loading if scroll too quickly)

        imageListAdapter = new ImageListAdapter(inflater.getContext());
        profileImgList.setAdapter(imageListAdapter);

        // Load URL, return the images from the urls and store into the list view
        loadListData();

        return view;
    }

//    @OnItemClick(R.id.img_list)
//    public void onImageItemClick(AdapterView<?> parent, View view, int position, long id) {
////        Intent intent = new Intent(this.getActivity(), ImageActivity.class);
////        String s = imageListAdapter.getItem(position).toString();
////        intent.putExtra("url", s);
////        LogUtils.d("imageListAdapter.getItem(position=" + position + ") = " + s);
////        this.getActivity().startActivity(intent);
//    }
//    
    // 
    
    /**
     * Load the images from the top favorite list (test data)
     * @param url
     */
    private void loadListData() {

		final String tag = "json_getProfile_req";  
		Integer pageIndex = Integer.valueOf(1);

		Date date = new Date();

        // Get global list           
        String uri = String.format("https://test.snaapiq.com/api2/user/%1$s/questMedia?date=%2$s&size=%3$s",
                					user.optString("id"),
                					String.valueOf(date.getTime()),
                					"12");
            CustomRequest ProfileListReq = new CustomRequest(Method.GET, uri, null, 
            										new Response.Listener<JSONObject>() {
											            @Override
											            public void onResponse(JSONObject response){
											            	VolleyController.getInstance().userlist = response;
											            	injectListData(response);
											            }
            										},
            										CustomReqListener.ErrorListener(getActivity())
                                                    );
            VolleyController.getInstance().addToRequestQueue(ProfileListReq, tag);

    }
    
    /**
     * Load the images from the top favorite list
     * @param url
     */
    public void injectListData(JSONObject userlist) {
    	JSONArray contentArr = userlist.optJSONArray("content");
        ArrayList<String> questName = new ArrayList<String>();
        ArrayList<Integer> avgStar = new ArrayList<Integer>();
        ArrayList<String> picUrl = new ArrayList<String>();
        //ArrayList<String> iswinner;
        
    	for(int i=0; i<contentArr.length(); ++i){
    		JSONObject content = contentArr.optJSONObject(i);
    		
        	//String isWinner = content.optString("iswinner");
        	questName.add(i, content.optJSONObject("quest").optString("name"));
        	avgStar.add(i, content.optInt("rateAvg"));
        	picUrl.add(i, content.optString("objectSignedUrl"));
    	}
        //image
    	imageListAdapter.updateData(questName, picUrl, avgStar);
        imageListAdapter.notifyDataSetChanged();	//notify the listview to update data
    }
    

    /**
     * Image Adapter that handles the image related data
     * @author samueltango
     *
     */
    public class ImageListAdapter extends BaseAdapter {

        private Context mContext;
        private final LayoutInflater mInflater;
        private ArrayList<String> mQuestNmae;
        private ArrayList<String> mImgUrl;
        private ArrayList<Integer> mAvgStar;
        //private ArrayList<String> contestNameList;


        public ImageListAdapter(Context context) {
            super();
            this.mContext = context;
            mInflater = LayoutInflater.from(context);
            mQuestNmae = new ArrayList<String>();
            mImgUrl = new ArrayList<String>();
            mAvgStar = new ArrayList<Integer>();
            //contestNameList = new ArrayList<String>();

        }
        public void updateData (ArrayList<String> questName, ArrayList<String> imgUrl, ArrayList<Integer> AvgStar) {
        	mQuestNmae = questName;
        	mAvgStar = AvgStar;
        	mImgUrl = imgUrl;
        }
        
        @Override
        public int getCount() {
            return mImgUrl.size();
        }

        @Override
        public Object getItem(int position) {
            return mImgUrl.get(position);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            ImageItemHolder holder = null;
            if (view == null) {
                view = mInflater.inflate(R.layout.profile_list_view_item, null);
                holder = new ImageItemHolder();
                ViewUtils.inject(holder, view);
                view.setTag(holder);
            } else {
                holder = (ImageItemHolder) view.getTag();
            }
            //holder.imgPb.setProgress(0);            
            holder.profileIsWinner.setTypeface(font);
//            if(mAvgStar.get(position) != null)
//            	holder.profileAvgStar.setRating((float) mAvgStar.get(position));
            holder.profileQuestName.setText(mQuestNmae.get(position));

            
            
         // Initialize the image utility class which is used for the image list handling
            

            //bitmapUtils.configMemoryCacheEnabled(false);
            //bitmapUtils.configDiskCacheEnabled(false);

            //bitmapUtils.configDefaultAutoRotation(true);

            //ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
            //        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            //animation.setDuration(800);

            // Set the default max size (the photo will auto-fit the container if this is not set
            bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(getActivity()).scaleDown(3));
            
            // Display the images and related data in the view
            LogUtils.d("===== position = " + position);
            //bitmapUtils.display(holder.profileListItem, mImgUrl.get(position));
            bitmapUtils.display(holder.profileListItem, "https://explora-snaapiq-media-test-b1.s3.amazonaws.com/eeee68e8-fe06-4943-9775-724694fdab37_profile_image.png");
            //bitmapUtils.display((ImageView) view, imgSrcList.get(position), displayConfig);
            //bitmapUtils.display((ImageView) view, imgSrcList.get(position));
            
       
            return view;
        }
    }
    
    private class ImageItemHolder {
    	@ViewInject(R.id.profile_list_item)
    	private ImageView profileListItem;

    	// Dynamic data over the image
    	@ViewInject(R.id.profile_iswinner)
    	private TextView profileIsWinner;    
    	@ViewInject(R.id.profile_average_star)
    	private RatingBar profileAvgStar;    
    	@ViewInject(R.id.profile_quest_decription)
    	private TextView profileQuestName;    
    	
    }

//    public class CustomBitmapLoadCallBack extends DefaultBitmapLoadCallBack<ImageView> {
//        private final ImageItemHolder holder;
//
//        public CustomBitmapLoadCallBack(ImageItemHolder holder) {
//            this.holder = holder;
//        }
//
//        @Override
//        public void onLoading(ImageView container, String uri, BitmapDisplayConfig config, long total, long current) {
//            this.holder.imgPb.setProgress((int) (current * 100 / total));
//        }
//
//        @Override
//        public void onLoadCompleted(ImageView container, String uri, Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
//            //super.onLoadCompleted(container, uri, bitmap, config, from);
//            fadeInDisplay(container, bitmap);
//            this.holder.imgPb.setProgress(100);
//        }
//    }
//
//    private static final ColorDrawable TRANSPARENT_DRAWABLE = new ColorDrawable(android.R.color.transparent);
//
//    private void fadeInDisplay(ImageView imageView, Bitmap bitmap) {
//        final TransitionDrawable transitionDrawable =
//                new TransitionDrawable(new Drawable[]{
//                        TRANSPARENT_DRAWABLE,
//                        new BitmapDrawable(imageView.getResources(), bitmap)
//                });
//        imageView.setImageDrawable(transitionDrawable);
//        transitionDrawable.startTransition(500);
//    }
}
