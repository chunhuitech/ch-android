package com.chunhuitech.reader.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chunhuitech.reader.App;
import com.chunhuitech.reader.R;
import com.chunhuitech.reader.adapter.ListViewAdapter;
import com.chunhuitech.reader.callback.ActivityLifecycleCallback;
import com.chunhuitech.reader.callback.ILoadDataCallback;
import com.chunhuitech.reader.entity.BaseResult;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private ImageView mImageView;
    private ListView mListChildren;
    private String area;
    private static String[] platforms = {
            "https://pv.sohu.com/cityjson",
            "https://pv.sohu.com/cityjson?ie=utf-8",
            "http://ip.chinaz.com/getip.aspx"
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.book_local);
                    mListChildren.setVisibility(View.INVISIBLE);
                    mTextMessage.setVisibility(View.VISIBLE);
                    mImageView.setVisibility(View.VISIBLE);
                    mImageView.setImageResource(R.drawable.qrcode_for_gh_a87a03c354b8_1280);
                    return true;
                case R.id.navigation_know:
                    mTextMessage.setVisibility(View.INVISIBLE);
                    mListChildren.setVisibility(View.VISIBLE);
                    mImageView.setVisibility(View.INVISIBLE);
                    initListView(1100);
                    return true;
                case R.id.navigation_teaching:
                    mTextMessage.setVisibility(View.INVISIBLE);
                    mListChildren.setVisibility(View.VISIBLE);
                    mImageView.setVisibility(View.INVISIBLE);
                    initListView(1200);
                    return true;
                case R.id.navigation_notifications:
                    mListChildren.setVisibility(View.INVISIBLE);
                    mTextMessage.setVisibility(View.VISIBLE);
                    mImageView.setVisibility(View.VISIBLE);
                    mImageView.setImageResource(R.drawable.qrcode_for_friend);
                    mTextMessage.setText(R.string.book_my);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 系统初始化
        App app = App.instanceApp();

        // 注册监听
        getApplication().registerActivityLifecycleCallbacks(new ActivityLifecycleCallback(app));

        setContentView(R.layout.activity_main);
        mTextMessage = (TextView) findViewById(R.id.message);
        mListChildren = (ListView) findViewById(R.id.listChildren);
        mImageView = (ImageView) findViewById(R.id.chpic);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        String szImei = getDeviceId(this);
        String pad = "No";
        if(isPad(this)) {
            pad = "Yes";
        }
        area = getNetWorkType(this);
        String osInfo = "Model:" + android.os.Build.MODEL + ",API:"  + android.os.Build.VERSION.SDK + ",Version:"  + android.os.Build.VERSION.RELEASE + ",Pad:" + pad + ",net:" + area;
        String ip =getInNetIp(this);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        String netIp =getOutNetIP(this,0);
        App.instanceApp().getDataService().addStartupEvent(szImei, osInfo, ip, netIp, area);
    }
    public String getOutNetIP(Context context, int index) {
        if (index < platforms.length) {
            BufferedReader buff = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(platforms[index]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(5000);//读取超时
                urlConnection.setConnectTimeout(5000);//连接超时
                urlConnection.setDoInput(true);
                urlConnection.setUseCaches(false);
                //连接
                urlConnection.connect();

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {//找到服务器的情况下,可能还会找到别的网站返回html格式的数据
                    InputStream is = urlConnection.getInputStream();
                    if(index == 0){
                        buff = new BufferedReader(new InputStreamReader(is, "GB2312"));//注意编码，会出现乱码
                    } else {
                        buff = new BufferedReader(new InputStreamReader(is, "UTF-8"));//注意编码，会出现乱码
                    }
                    StringBuilder builder = new StringBuilder();
                    String line = null;
                    while ((line = buff.readLine()) != null) {
                        builder.append(line);
                    }

                    buff.close();//内部会关闭 InputStream
                    urlConnection.disconnect();

                    Log.e("xiaoman", builder.toString());
                    if (index == 0 || index == 1) {
                        //截取字符串
                        int satrtIndex = builder.indexOf("{");//包含[
                        int endIndex = builder.indexOf("}");//包含]
                        String json = builder.substring(satrtIndex, endIndex + 1);//包含[satrtIndex,endIndex)
                        JSONObject jo = new JSONObject(json);
                        String ip = jo.getString("cip");
                        area = jo.getString("cname");
                        return ip;
                    } else if (index == 2) {
                        JSONObject jo = new JSONObject(builder.toString());
                        return jo.getString("ip");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return getInNetIp(context);
        }
        return getOutNetIP(context, ++index);
    }
    /**
     * 获取当前的网络类型
     * @return
     */
    public static String getNetWorkType(Context context) {
        try{
            //获取系统的网络服务
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //如果当前没有网络
            if (null == connManager)
                return "NO";
            //获取当前网络类型，如果为空，返回无网络
            NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
            if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
                return "NO";
            }
            // 判断是不是连接的是不是wifi
            NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (null != wifiInfo) {
                NetworkInfo.State state = wifiInfo.getState();
                if (null != state)
                    if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                        return "WIFI";
                    }
            }
            // 如果不是wifi，则判断当前连接的是运营商的哪种网络2g、3g、4g等
            NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (null != networkInfo) {
                NetworkInfo.State state = networkInfo.getState();
                String strSubTypeName = networkInfo.getSubtypeName();
                if (null != state)
                    if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                        switch (activeNetInfo.getSubtype()) {
                            //如果是2g类型
                            case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
                            case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                            case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                            case TelephonyManager.NETWORK_TYPE_1xRTT:
                            case TelephonyManager.NETWORK_TYPE_IDEN:
                                return "2G";
                            //如果是3g类型
                            case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
                            case TelephonyManager.NETWORK_TYPE_UMTS:
                            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                            case TelephonyManager.NETWORK_TYPE_HSDPA:
                            case TelephonyManager.NETWORK_TYPE_HSUPA:
                            case TelephonyManager.NETWORK_TYPE_HSPA:
                            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                            case TelephonyManager.NETWORK_TYPE_EHRPD:
                            case TelephonyManager.NETWORK_TYPE_HSPAP:
                                return "3G";
                            //如果是4g类型
                            case TelephonyManager.NETWORK_TYPE_LTE:
                                return "4G";
                            default:
                                //中国移动 联通 电信 三种3G制式
                                if (strSubTypeName.equalsIgnoreCase("TD-SCDMA")
                                        || strSubTypeName.equalsIgnoreCase("WCDMA")
                                        || strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                                    return "3G";
                                } else {
                                    return "MOBILE";
                                }
                        }
                    }
            }
            return "NO";
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    public static String getInNetIp(Context context) {
//        //获取wifi服务
//        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        //判断wifi是否开启
//        if (!wifiManager.isWifiEnabled()) {
//            wifiManager.setWifiEnabled(true);
//        }
//
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        int ipAddress = wifiInfo.getIpAddress();
//        String ip = intToIp(ipAddress);
//
//        return ip;
        String ipAddresses = "";
        try {
            for (Enumeration<NetworkInterface> enNetI = NetworkInterface
                    .getNetworkInterfaces(); enNetI.hasMoreElements(); ) {
                NetworkInterface netI = enNetI.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = netI
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        ipAddresses += inetAddress.getHostAddress() + ",";
                    }
                }
            }
            if(ipAddresses.length() > 1){
                ipAddresses.substring(0, ipAddresses.length() - 2);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipAddresses;
    }

//    //这段是转换成点分式IP的码
//    private static String intToIp(int ip) {
//        return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) &     0xFF) + "." + (ip >> 24 & 0xFF);
//    }

    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static String getDeviceId(Context context) {

        String id;
        //android.telephony.TelephonyManager
        TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
            id = mTelephony.getDeviceId();
        } else {
            //android.provider.Settings;
            //id = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            id = "39" + //we make this look like a valid IMEI
                    Build.BOARD.length()%10 +
                    Build.BRAND.length()%10 +
                    Build.CPU_ABI.length()%10 +
                    Build.DEVICE.length()%10 +
                    Build.DISPLAY.length()%10 +
                    Build.HOST.length()%10 +
                    Build.ID.length()%10 +
                    Build.MANUFACTURER.length()%10 +
                    Build.MODEL.length()%10 +
                    Build.PRODUCT.length()%10 +
                    Build.TAGS.length()%10 +
                    Build.TYPE.length()%10 +
                    Build.USER.length()%10 ; //13 digits
        }

        return id;
    }


    private void initListView(int id) {
        App.instanceApp().getDataService().getChildren(id, new ILoadDataCallback() {
            @Override
            public void loadFinish(BaseResult data) {
                List<HashMap<String, Object>> listData = (List<HashMap<String, Object>>)data.getData().get("dataList");
                ListViewAdapter listViewAdapter = new ListViewAdapter(getApplicationContext(), R.layout.list_item, listData);
                mListChildren.setAdapter(listViewAdapter);

                mListChildren.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int itemId = (int)mListChildren.getAdapter().getItemId(position);
                        Map<String, Object> data = (Map<String, Object>)mListChildren.getAdapter().getItem(position);
                        if ((int)Float.parseFloat(data.get("leaf").toString()) == 1) {
                            Intent startIntent = new Intent(MainActivity.this, LoadBookActivity.class);
                            startIntent.putExtra("bookCnName", data.get("cnName").toString());
                            startIntent.putExtra("page", 1);
                            App.instanceApp().getBookInfo().setBookId(data.get("id").toString());
                            startActivity(startIntent);
                        } else {
                            initListView(itemId);
                        }
                    }
                });
            }
        });
    }

}
