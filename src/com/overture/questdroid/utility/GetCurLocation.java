package com.overture.questdroid.utility;
//package com.overture.questdroid.toolbox;
//
//public class GetCurLocation extends Activity
//{ 
//    private TextView textView = null; 
//    @Override
//    public void onCreate(Bundle savedInstanceState)
//    { 
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main); 
//        textView = (TextView) findViewById(R.id.loc);
//        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        // 返回所有已知的位置提供者的名称列表，包括未获准访问或调用活动目前已停用的。
//        List<String> lp = lm.getAllProviders();
//        for (String item:lp)
//        {
//            Log.i("8023", "可用位置服务："+item); 
//        }
//
//
//        Criteria criteria = new Criteria();  
//        criteria.setCostAllowed(false); 
////设置位置服务免费 
//        criteria.setAccuracy(Criteria.ACCURACY_COARSE); //设置水平位置精度
//         //getBestProvider 只有允许访问调用活动的位置供应商将被返回
//        String  providerName =         lm.getBestProvider(criteria, true);
//        Log.i("8023", "------位置服务："+providerName);
//
//
//
//        if (providerName != null)
//        {        
//            Location location = lm.getLastKnownLocation(providerName);
//            Log.i("8023", "-------"+location);    
//             //获取维度信息
//            double latitude = location.getLatitude();
//            //获取经度信息
//            double longitude = location.getLongitude();
//            textView.setText("定位方式： "+providerName+"  维度："+latitude+"  经度："+longitude);   
//        }
//        else
//        {
//              Toast.makeText(this, "1.请检查网络连接 \n2.请打开我的位置", Toast.LENGTH_SHORT).show();
//        }
//    } 
//
//
//}
