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
//        // ����������֪��λ���ṩ�ߵ������б�����δ��׼���ʻ���ûĿǰ��ͣ�õġ�
//        List<String> lp = lm.getAllProviders();
//        for (String item:lp)
//        {
//            Log.i("8023", "����λ�÷���"+item); 
//        }
//
//
//        Criteria criteria = new Criteria();  
//        criteria.setCostAllowed(false); 
////����λ�÷������ 
//        criteria.setAccuracy(Criteria.ACCURACY_COARSE); //����ˮƽλ�þ���
//         //getBestProvider ֻ��������ʵ��û��λ�ù�Ӧ�̽�������
//        String  providerName =         lm.getBestProvider(criteria, true);
//        Log.i("8023", "------λ�÷���"+providerName);
//
//
//
//        if (providerName != null)
//        {        
//            Location location = lm.getLastKnownLocation(providerName);
//            Log.i("8023", "-------"+location);    
//             //��ȡά����Ϣ
//            double latitude = location.getLatitude();
//            //��ȡ������Ϣ
//            double longitude = location.getLongitude();
//            textView.setText("��λ��ʽ�� "+providerName+"  ά�ȣ�"+latitude+"  ���ȣ�"+longitude);   
//        }
//        else
//        {
//              Toast.makeText(this, "1.������������ \n2.����ҵ�λ��", Toast.LENGTH_SHORT).show();
//        }
//    } 
//
//
//}
