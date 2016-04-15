package com.itkc_carlife.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.itkc_carlife.R;
import com.itkc_carlife.box.ServicesObj;
import com.itkc_carlife.box.handler.ServicesObjHandler;
import com.itkc_carlife.box.handler.UserObjHandler;
import com.itkc_carlife.handlers.JsonHandle;
import com.itkc_carlife.handlers.MapHandler;
import com.itkc_carlife.handlers.MessageHandler;
import com.itkc_carlife.http.HttpUtilsBox;
import com.itkc_carlife.http.UrlHandler;
import com.itkc_carlife.views.MapMessageView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * *
 * * ┏┓      ┏┓
 * *┏┛┻━━━━━━┛┻┓
 * *┃          ┃
 * *┃          ┃
 * *┃ ┳┛   ┗┳  ┃
 * *┃          ┃
 * *┃    ┻     ┃
 * *┃          ┃
 * *┗━┓      ┏━┛
 * *  ┃      ┃
 * *  ┃      ┃
 * *  ┃      ┗━━━┓
 * *  ┃          ┣┓
 * *  ┃         ┏┛
 * *  ┗┓┓┏━━━┳┓┏┛
 * *   ┃┫┫   ┃┫┫
 * *   ┗┻┛   ┗┻┛
 * Created by Hua on 16/2/16.
 */
public class MapActivity extends BaseActivity {

    private final static int MAPZOOM = 14;
    public final static int LIMIT = 20;

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.map_baiduMap)
    private MapView mMapView;
    @ViewInject(R.id.map_progress)
    private ProgressBar progress;

    private BaiduMap mBaiduMap;

    private int page = 1, pages = 1;
    private List<Overlay> mOverlayList;
    private List<ServicesObj> mServiceObjList;
    private Marker onClickMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);
        context = this;
        ViewUtils.inject(this);

        initActivity();
        setOnMarkerClickListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @OnClick({R.id.title_backBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
        }
    }

    private void setOnMarkerClickListener() {
        mBaiduMap
                .setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        for (int i = 0; i < mOverlayList.size(); i++) {
                            Marker mMarker = (Marker) mOverlayList.get(i);
                            if (marker == mMarker) {
                                setOnClickIcon(i);
                                return false;
                            }
                        }
                        return false;
                    }

                });
    }

    private Marker setOnClickIcon(int position) {
        if (!mOverlayList.isEmpty()) {
            onClickMarker = (Marker) mOverlayList.get(position);
            showInfoWindow(onClickMarker, mServiceObjList.get(position));
            return onClickMarker;
        }
        return null;
    }

    private void showInfoWindow(Marker marker, ServicesObj obj) {
        MapMessageView msg = new MapMessageView(context, obj);
        InfoWindow mInfoWindow = new InfoWindow(msg, marker.getPosition(), -80);
        mBaiduMap.showInfoWindow(mInfoWindow);
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("地图");

        mBaiduMap = mMapView.getMap();
        initMap();

    }

    private void initMap() {
        LocationClient locationClient = MapHandler.getAddress(context, new MapHandler.MapListener() {
            @Override
            public void callback(BDLocation location) {
                LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(MAPZOOM)
                        .build();
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                        .newMapStatus(mMapStatus);
                mBaiduMap.animateMapStatus(mMapStatusUpdate, 500);

                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.position_icon);
                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .icon(bitmap);
                mBaiduMap.addOverlay(option);
                dowmloadMapServer(point);
            }
        });
    }

    public void setMapMessage(List<ServicesObj> list) {
        addOverlay(list);
    }

    private void addOverlay(List<ServicesObj> list) {
        mOverlayList = new ArrayList<Overlay>();
        mServiceObjList = new ArrayList<ServicesObj>();
        for (ServicesObj obj : list) {
            if (obj != null && obj.isHaveCoordinate()) {
                mServiceObjList.add(obj);
                mOverlayList
                        .add(addOverlay(obj, true));
            }
        }
    }

    private Overlay addOverlay(ServicesObj mServiceObj,
                               boolean isOnClick) {
        LatLng point = new LatLng(mServiceObj.getLatitude(),
                mServiceObj.getLongitude());
        return addOverlay(point, mServiceObj.getIconDrawble());

    }

    private Overlay addOverlay(LatLng point, int icon) {
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(icon);
        OverlayOptions option = new MarkerOptions().position(point)
                .icon(bitmap);
        return mBaiduMap.addOverlay(option);
    }

    private void dowmloadMapServer(LatLng point) {
        progress.setVisibility(View.VISIBLE);
        String url = UrlHandler.getNearServices() + "?sessiontoken=" + UserObjHandler.getSessionToken(context)
                + "&limit=" + LIMIT + "&page=" + page + "&latitude=" + point.latitude + "&longitude=" + point.longitude;

        RequestParams params = HttpUtilsBox.getRequestParams(context);

        HttpUtilsBox.getHttpUtil().send(HttpMethod.GET, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        progress.setVisibility(View.GONE);
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);
                        progress.setVisibility(View.GONE);

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            int status = JsonHandle.getInt(json, "status");
                            if (status == 1) {
                                JSONArray array = JsonHandle.getArray(json, "result");
                                if (array != null) {
                                    List<ServicesObj> list = ServicesObjHandler.getServicesObjList(array);
                                    setMapMessage(list);
                                }
                            }
                        }
                    }

                });
    }

}
