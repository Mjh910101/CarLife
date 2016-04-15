package com.itkc_carlife.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.itkc_carlife.R;
import com.itkc_carlife.dialogs.ListDialog;
import com.itkc_carlife.dialogs.MessageDialog;
import com.itkc_carlife.handlers.MapHandler;
import com.itkc_carlife.handlers.MessageHandler;
import com.itkc_carlife.map.MyDrivingRouteOverlay;
import com.itkc_carlife.tool.Passageway;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.net.URISyntaxException;

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
 * Created by Hua on 16/1/14.
 */
public class PositionActivity extends BaseActivity {

    public final static String IS_HAVE = "isHave";
    public final static String IS_ARTICIAL = "isArtificial";
    public final static String IS_NAVIGATION = "isNavigation";
    private final static int MAPZOOM = 14;
    public final static int REQUEST_CODE = 100;

    public final static String IS_OK = "isOk";
    public final static String ADDRESS_KEY = "address";
    public final static String LATITUDE_KEY = "latitude";
    public final static String LONGITUDE_KEY = "longitude";

    @ViewInject(R.id.title_backBtn)
    private TextView backBtn;
    @ViewInject(R.id.title_titleName)
    private TextView titleName;
    @ViewInject(R.id.position_baiduMap)
    private MapView mMapView;
    @ViewInject(R.id.position_progress)
    private ProgressBar progress;
    @ViewInject(R.id.position_btnBox)
    private LinearLayout btnBox;
    @ViewInject(R.id.position_positionIconBox)
    private RelativeLayout positionIconBox;
    @ViewInject(R.id.position_dialog)
    private RelativeLayout positionDialog;
    @ViewInject(R.id.positionDialog_sosoIcon)
    private ImageView sosoIcon;
    @ViewInject(R.id.positionDialog_sosoDownText)
    private TextView sosoDownText;
    @ViewInject(R.id.positionDialog_baiduIcon)
    private ImageView baiduIcon;
    @ViewInject(R.id.positionDialog_baiduDownText)
    private TextView baiduDownText;

    private BaiduMap mBaiduMap;
    private Overlay mOverlay;
    private BitmapDescriptor positionIcon;
    private BDLocation mBDLocation;
    private LatLng mapPoint;
    private boolean isArtificial;
    private GeoCoder mSearch;
    private double latitude, longitude;
    private String address = "";
    private LatLng start;
    private LatLng end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_position);
        context = this;
        ViewUtils.inject(this);

        initActivity();
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
            if (positionDialog.getVisibility() != View.GONE) {
                positionDialog.setVisibility(View.GONE);
            } else {
                finish();
            }
        }
        return false;
    }

    @OnClick({R.id.title_backBtn, R.id.position_refreshBtn, R.id.position_saveBtn,
            R.id.positionDialog_soso, R.id.positionDialog_baidu, R.id.positionDialog_flishBtn, R.id.position_dialog})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backBtn:
                finish();
                break;
            case R.id.position_refreshBtn:
                refreshPosition();
                break;
            case R.id.position_saveBtn:
                if (isArtificial) {
                    saveLocation(address, latitude, longitude);
                } else {
                    saveLocation(mBDLocation);
                }
                break;
            case R.id.positionDialog_soso:
                if (isInstallTencentMap()) {
                    openSosoMap(start, end);
                }
                positionDialog.setVisibility(View.GONE);
                break;
            case R.id.positionDialog_baidu:
                if (isInstallBaiduMap()) {
                    openBaiduMap(start, end);
                }
                positionDialog.setVisibility(View.GONE);
                break;
            case R.id.positionDialog_flishBtn:
            case R.id.position_dialog:
                positionDialog.setVisibility(View.GONE);
                break;
        }
    }

    private void saveLocation(BDLocation mBDLocation) {
        if (mBDLocation != null) {
            saveLocation(mBDLocation.getAddrStr(), mBDLocation.getLatitude(), mBDLocation.getLongitude());
        }
    }

    private void saveLocation(String address, double latitude, double longitude) {
        Intent i = new Intent();
        Bundle b = new Bundle();
        b.putString(ADDRESS_KEY, address);
        b.putDouble(LATITUDE_KEY, latitude);
        b.putDouble(LONGITUDE_KEY, longitude);
        i.putExtras(b);
        setResult(REQUEST_CODE, i);
        finish();
    }

    private void refreshPosition() {
        if (progress.getVisibility() == View.GONE) {
            getMapPosition();
        }
    }

    private void initActivity() {
        backBtn.setVisibility(View.VISIBLE);
        titleName.setVisibility(View.VISIBLE);
        titleName.setText("定位");

        mBaiduMap = mMapView.getMap();
        mMapView.removeViewAt(1);
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(false);
        positionIcon = BitmapDescriptorFactory
                .fromResource(R.drawable.position_icon);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.getBoolean(IS_HAVE)) {
                btnBox.setVisibility(View.GONE);
//                LatLng point = new LatLng(22.58421, 113.377176);
                LatLng point = new LatLng(b.getDouble(LATITUDE_KEY), b.getDouble(LONGITUDE_KEY));
                if (b.getBoolean(IS_NAVIGATION)) {
                    setNavigation(point);
//                    jumpMapApp(point);
                    return;
                }
                setMapPoint(point);
                return;
            }
            isArtificial = b.getBoolean(IS_ARTICIAL, false);
            if (isArtificial) {
                initListener();
                LatLng point = new LatLng(b.getDouble(LATITUDE_KEY), b.getDouble(LONGITUDE_KEY));
                initMap(point);
                updateMapState(point);
            } else {
                getMapPosition();
            }
        }

    }

    private void initListener() {
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(geoCoderListener);
//        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
//            public void onMapClick(LatLng point) {
//                updateMapState(point);
//            }
//
//            public boolean onMapPoiClick(MapPoi poi) {
//                //在此处理底图标注点击事件
//                return false;
//            }
//        });
        positionIconBox.setVisibility(View.VISIBLE);
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus status) {
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus status) {
                updateMapState(status);
            }

            @Override
            public void onMapStatusChange(MapStatus status) {
            }
        });
    }

    private void updateMapState(MapStatus status) {
        LatLng point = status.target;
        updateMapState(point);
    }

    private void updateMapState(LatLng point) {
        latitude = point.latitude;
        longitude = point.longitude;
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(point));
//        setMapOverlay(point);
    }

    OnGetGeoCoderResultListener geoCoderListener = new OnGetGeoCoderResultListener() {
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有检索到结果
            }
            //获取地理编码结果
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
//                MessageHandler.showToast(context,"");
                return;
            }
            address = result.getAddress();
//            MessageHandler.showToast(context, address);
            //获取反向地理编码结果
        }
    };

    public void setNavigation(final LatLng start) {
        progress.setVisibility(View.VISIBLE);
        LocationClient locationClient = MapHandler.getAddress(context, new MapHandler.MapListener() {
            @Override
            public void callback(BDLocation location) {
                LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
                initMap(point);
                showListDialog(point, start);
                progress.setVisibility(View.GONE);
                RoutePlanSearch mSearch = RoutePlanSearch.newInstance();
                mSearch.setOnGetRoutePlanResultListener(listener);
                PlanNode s = PlanNode.withLocation(start);
                PlanNode e = PlanNode.withLocation(point);
                mSearch.drivingSearch((new DrivingRoutePlanOption())
                        .from(e)
                        .to(s));
//                mSearch.destroy();
            }
        });


    }

    private void jumpMapApp(final LatLng end) {
        if (isInstallBaiduMap()) {
            LocationClient locationClient = MapHandler.getAddress(context, new MapHandler.MapListener() {
                @Override
                public void callback(BDLocation location) {
                    LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
                    showListDialog(point, end);
                }
            });
        }
    }

    private void showListDialog(final LatLng start, final LatLng end) {
        positionDialog.setVisibility(View.VISIBLE);
        this.start = start;
        this.end = end;
        if (isInstallBaiduMap()) {
            baiduIcon.setImageResource(R.drawable.baidu_icon);
            baiduDownText.setVisibility(View.INVISIBLE);
        } else {
            baiduIcon.setImageResource(R.drawable.baidu_gray_icon);
            baiduDownText.setVisibility(View.VISIBLE);
        }

        if (isInstallTencentMap()) {
            sosoIcon.setImageResource(R.drawable.soso_icon);
            sosoDownText.setVisibility(View.INVISIBLE);
        } else {
            sosoIcon.setImageResource(R.drawable.soso_gray_icon);
            sosoDownText.setVisibility(View.VISIBLE);
        }

//        final ListDialog dialog = new ListDialog(context);
//        dialog.setList(new String[]{"调用百度地图导航", "调用腾讯地图导航"});
//        dialog.setTitleGone();
//        dialog.setItemListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                switch (position) {
//                    case 0:
//                        if (isInstallBaiduMap()) {
//                            openBaiduMap(start, end);
//                        } else {
//                            MessageHandler.showToast(context, "请先安装百度地图");
//                        }
//                        break;
//                    case 1:
//                        if (isInstallTencentMap()) {
//                            openSosoMap(start, end);
//                        } else {
//                            MessageHandler.showToast(context, "请先安装腾讯地图");
//                        }
//                        break;
//                }
//                dialog.dismiss();
//            }
//        });
    }

    private void showMessageDialog(final LatLng start, final LatLng end) {
        MessageDialog dialog = new MessageDialog(context);
        dialog.setMessage("是否调用百度地图导航");
        dialog.setCommitStyle("是的");
        dialog.setCommitListener(new MessageDialog.CallBackListener() {
            @Override
            public void callback() {
                openSosoMap(start, end);
            }
        });
        dialog.setCancelStyle("取消");
        dialog.setCancelListener(null);
    }

    private void openGaoDeMap(LatLng start, LatLng end) {
        try {
            String str = "androidamap://route?sourceApplication=CarLife&dev=0&slat=" + start.latitude + "&slon=" + start.longitude + "&dlat=" + end.latitude + "&dlon=" + end.longitude + "&sname=起点&dname=终点";
            Log.e("", str);
            Intent intent = Intent.getIntent(str);
            startActivity(intent);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void openSosoMap(LatLng start, LatLng end) {
        try {
            String str = "qqmap://map/routeplan?type=drive&fromcoord=" + start.latitude + "," + start.longitude + "&tocoord=" + end.latitude + "," + end.longitude + "&referer=CarLife&coord_type=2&debug=true" + "&from=起点&to=终点";
            Log.e("", str);
            Intent intent = Intent.getIntent(str);
            startActivity(intent);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void openBaiduMap(LatLng start, LatLng end) {
//        Intent intent = null;
//        String str = "intent://map/direction?origin=latlng:" + start.latitude + "," + start.longitude
//                + "&destination=latlng:" + end.latitude + "," + end.longitude + "&coord_type=gcj02"
//                + "&mode=driving&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
//        try {
//            Log.e("", str);
//            intent = Intent.getIntent(str);
//            startActivity(intent); //启动调用
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }

//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        Uri uri = Uri.parse("geo:" + end.latitude + "," + end.longitude);
//        intent.setData(uri);
//        intent.setPackage("com.baidu.BaiduMap");
//        this.startActivity(intent);
        NaviParaOption npo = new NaviParaOption();
        npo.startPoint(start);
        npo.endPoint(end);
        BaiduMapNavigation.openBaiduMapNavi(npo, context);
    }

    private boolean isInstallBaiduMap() {
        return checkApkExist(context, "com.baidu.BaiduMap");
    }

    private boolean isInstallGaoDeMap() {
        return checkApkExist(context, "com.autonavi.minimap");
    }

    private boolean isInstallTencentMap() {
        return checkApkExist(context, "com.tencent.map");
    }

    private boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
        public void onGetWalkingRouteResult(WalkingRouteResult result) {
            //获取步行线路规划结果
        }

        public void onGetTransitRouteResult(TransitRouteResult result) {
            //获取公交换乘路径规划结果
        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult result) {
            //获取驾车线路规划结果
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(context, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                //result.getSuggestAddrInfo()
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }
        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }
    };

    public void getMapPosition() {
        getMapPosition(false);
    }

    public void getMapPosition(final boolean isArtificial) {
        progress.setVisibility(View.VISIBLE);
        LocationClient locationClient = MapHandler.getAddress(context, new MapHandler.MapListener() {
            @Override
            public void callback(BDLocation location) {
                mBDLocation = location;
                LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
                setMapPoint(point, isArtificial);

                progress.setVisibility(View.GONE);
            }
        });
    }

    public void setMapPoint(LatLng point) {
        setMapPoint(point, false);
    }

    public void setMapPoint(LatLng point, boolean isArtificial) {
        initMap(point);

        if (!isArtificial) {
            setMapOverlay(point);
        }

    }

    private void initMap(LatLng point) {
        MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(MAPZOOM)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                .newMapStatus(mMapStatus);
        mBaiduMap.animateMapStatus(mMapStatusUpdate, 500);

    }

    public void setMapOverlay(LatLng point) {
        if (mOverlay != null) {
            mOverlay.remove();
        }

        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(positionIcon);
        mOverlay = mBaiduMap.addOverlay(option);
    }

}
