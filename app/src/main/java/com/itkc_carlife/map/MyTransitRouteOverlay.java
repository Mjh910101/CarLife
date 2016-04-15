package com.itkc_carlife.map;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.itkc_carlife.R;

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
 * Created by Hua on 16/2/18.
 */
public class MyTransitRouteOverlay extends TransitRouteOverlay {

    boolean useDefaultIcon = false;

    public MyTransitRouteOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }

    @Override
    public BitmapDescriptor getStartMarker() {
        if (useDefaultIcon) {
            return BitmapDescriptorFactory.fromResource(R.drawable.position_icon);
        }
        return null;
    }

    @Override
    public BitmapDescriptor getTerminalMarker() {
        if (useDefaultIcon) {
            return BitmapDescriptorFactory.fromResource(R.drawable.position_icon);
        }
        return null;
    }
}


