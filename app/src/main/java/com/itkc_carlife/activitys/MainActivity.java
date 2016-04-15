package com.itkc_carlife.activitys;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
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
import com.itkc_carlife.adapters.MainTapAdaper;
import com.itkc_carlife.box.ServicesObj;
import com.itkc_carlife.box.handler.ServicesObjHandler;
import com.itkc_carlife.box.handler.UserCarInfoObjHandler;
import com.itkc_carlife.box.handler.UserClaimsInfoObjHandler;
import com.itkc_carlife.box.handler.UserObjHandler;
import com.itkc_carlife.download.DownloadImageLoader;
import com.itkc_carlife.handlers.JsonHandle;
import com.itkc_carlife.handlers.MapHandler;
import com.itkc_carlife.handlers.MessageHandler;
import com.itkc_carlife.http.HttpUtilsBox;
import com.itkc_carlife.http.UrlHandler;
import com.itkc_carlife.tool.Passageway;
import com.itkc_carlife.tool.WinTool;
import com.itkc_carlife.views.LineGridView;
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


public class MainActivity extends BaseActivity {

    private final static int MAPZOOM = 14;
    public final static int LIMIT = 20;

    private final static long EXITTIME = 2000;
    private long EXIT = 0;

    @ViewInject(R.id.main_baiduMap)
    private MapView mMapView;
    @ViewInject(R.id.main_dataGrid)
    private LineGridView dataGrid;
    @ViewInject(R.id.title_userBtn)
    private TextView userBtn;
    @ViewInject(R.id.title_mapBtn)
    private ImageView mapBtn;
    @ViewInject(R.id.main_drawerLayout)
    private DrawerLayout drawerLayout;
    @ViewInject(R.id.main_left_drawer)
    private LinearLayout leftDrawer;
    @ViewInject(R.id.title_titleNameIcon)
    private ImageView titleNameIcon;
    @ViewInject(R.id.mainLsft_userPic)
    private ImageView userPic;
    @ViewInject(R.id.mainLsft_userName)
    private TextView userName;
    @ViewInject(R.id.main_left_notCarData)
    private TextView notCarData;
    @ViewInject(R.id.main_left_noeClaimsData)
    private TextView notClaimsData;
    @ViewInject(R.id.main_progress)
    private ProgressBar progress;
    @ViewInject(R.id.main_baiduMapBox)
    private RelativeLayout baiduMapBox;

    private BaiduMap mBaiduMap;
    private Overlay mOverlay;
    private int page = 1, pages = 1;
    private List<Overlay> mOverlayList;
    private List<ServicesObj> mServiceObjList;
    private Marker onClickMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        context = this;
        ViewUtils.inject(this);
        DownloadImageLoader.initLoader(context);
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    PushService.setDefaultPushCallback(context, MainActivity.class);
                } else {
                    e.printStackTrace();
                }
            }
        });

        initActivity();
        setDrawerListener();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (isOpenLift()) {
                closeLift();
            } else {
                if (System.currentTimeMillis() - EXIT < EXITTIME) {
                    finish();
                } else {
                    MessageHandler.showToast(context, "再次点击退出");
                }
                EXIT = System.currentTimeMillis();
            }
        }
        return false;
    }

    @OnClick({R.id.title_userBtn, R.id.main_left_userBox, R.id.main_left_topUpMoney, R.id.main_left_carData,
            R.id.main_left_claimsData, R.id.title_mapBtn, R.id.mainLsft_userName, R.id.main_positionIcon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_userBtn:
                openLift();
                break;
            case R.id.main_left_userBox:
            case R.id.mainLsft_userName:
                closeLift();
                Passageway.jumpActivity(context, UserModifyActivity.class);
                break;
            case R.id.main_left_topUpMoney:
                closeLift();
                Passageway.jumpActivity(context, TopUpMoneyActivity.class);
                break;
            case R.id.main_left_carData:
                closeLift();
                Passageway.jumpActivity(context, InputCarDataActivity.class);
                break;
            case R.id.main_left_claimsData:
                closeLift();
                Passageway.jumpActivity(context, InputClaimsDataActivity.class);
                break;
            case R.id.title_mapBtn:
//                Passageway.jumpActivity(context,MapActivity.class);
                if (dataGrid.getVisibility() == View.VISIBLE) {
                    dataGrid.setVisibility(View.GONE);
                    mapBtn.setImageResource(R.drawable.title_map_s_icon);
                } else {
                    dataGrid.setVisibility(View.VISIBLE);
                    mapBtn.setImageResource(R.drawable.title_map_icon);
                }
                break;
            case R.id.main_positionIcon:
                initMap();
                break;
        }
    }

    private void initActivity() {
        userBtn.setVisibility(View.VISIBLE);
        mapBtn.setVisibility(View.VISIBLE);
        titleNameIcon.setVisibility(View.VISIBLE);
        dataGrid.setAdapter(new MainTapAdaper(context));
        if (UserObjHandler.isLogin(context)) {
            mBaiduMap = mMapView.getMap();
            mMapView.removeViewAt(1);
            mMapView.showZoomControls(false);
            mMapView.showScaleControl(false);
            initMap();
            setOnMarkerClickListener();
            setOnMapClickListener();
        } else {
            Passageway.jumpActivity(context, LoginActivity.class);
            finish();
        }
    }

    private void setOnMapClickListener() {
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            public void onMapClick(LatLng point) {
                mBaiduMap.hideInfoWindow();
            }

            public boolean onMapPoiClick(MapPoi poi) {
                //在此处理底图标注点击事件
                return false;
            }
        });
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
                if (mOverlay != null) {
                    mOverlay.remove();
                }
                mOverlay = mBaiduMap.addOverlay(option);
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
                + "&limit=" + 1000 + "&page=" + page + "&latitude=" + point.latitude + "&longitude=" + point.longitude;//

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

    private boolean isOpenLift() {
        return drawerLayout.isDrawerOpen(leftDrawer);
    }

    private void openLift() {
        drawerLayout.openDrawer(leftDrawer);
    }

    private void closeLift() {
        drawerLayout.closeDrawer(leftDrawer);
    }

    private void drawerOpened() {
        int w = WinTool.getWinWidth(context) / 4;
        userPic.setLayoutParams(new RelativeLayout.LayoutParams(w, w));
        String a = UserObjHandler.getUserAvatar(context);
        if (a.equals("")) {
            if (UserObjHandler.isMan(context)) {
                userPic.setImageResource(R.drawable.man_pic_icon);
            } else {
                userPic.setImageResource(R.drawable.woman_pic_icon);
            }
        } else {
            DownloadImageLoader.loadImage(userPic, a, w / 2);
        }
//        UserObjHandler.setUserAvatar(context, userPic);
        userName.setText(UserObjHandler.getUserName(context));
        if (!UserCarInfoObjHandler.isThrough(context)) {
            notCarData.setVisibility(View.VISIBLE);
        } else {
            notCarData.setVisibility(View.GONE);
        }
        if (!UserClaimsInfoObjHandler.isThrough(context)) {
            notClaimsData.setVisibility(View.VISIBLE);
        } else {
            notClaimsData.setVisibility(View.GONE);
        }
    }

    private void setDrawerListener() {
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                drawerOpened();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }

}


