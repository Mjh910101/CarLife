package com.itkc_carlife.handlers;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class MapHandler {


    public interface MapListener {
        public void callback(BDLocation location);
    }

    public static LocationClient getAddress(Context context, final MapListener callback) {
        final LocationClient locationClient = new LocationClient(context);
        LocationClientOption option = getLocationClientOption();
        locationClient.setLocOption(option);
        locationClient.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation location) {
                if (location != null) {
                    Log.e("baidu", "lat: " + location.getLatitude() + "    lon : "
                            + location.getLongitude() + "      add : " + location.getAddrStr());
                    callback.callback(location);
                }
                locationClient.stop();
            }
        });

        locationClient.start();
        return locationClient;
    }

    private static LocationClientOption getLocationClientOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("gcj02");// 返回的定位结果是百度经纬度，默认值gcj02
        option.setOpenGps(true);
        int span = 1000;
        option.setScanSpan(span);
        option.setIsNeedAddress(true);
//        option.SetIgnoreCacheException(true);
        return option;
    }

}
