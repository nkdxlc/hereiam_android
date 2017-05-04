package com.vuforia.samples.VuforiaSamples;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polygon;
import com.amap.api.maps2d.model.PolygonOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearch.OnDistrictSearchListener;
import com.amap.api.services.district.DistrictSearchQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;


public class BoundaryActivity extends AppCompatActivity implements OnDistrictSearchListener, OnGeocodeSearchListener {

    private TextView txt_map_detailaddr;//详细地址显示
    private Button btn_map_ok;//确定按钮
    private MapView mapView;
    private AMap aMap;
    private GeocodeSearch geocoderSearch;
    private String addressName = "";
    private LatLonPoint latLonPoint;
    private Marker regeoMarker;
    private LatLonPoint centerLatLng;//行政区域的中心点坐标
    private String addrThree = "";//地图加载时显示的区域边界(值为districtId)
    private PolygonOptions pOption;
    private LatLng markerLatLng;//用于传递给CompanyInfoActivity页面的经纬度


    protected void onInitEvent() {

        btn_map_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("detailAddr", txt_map_detailaddr.getText().toString());
                intent.putExtra("markerLatLng", markerLatLng);
                setResult(1, intent);
                finish();

            }
        });

        aMap.setOnMapClickListener(new OnMapClickListener() {


            @Override
            public void onMapClick(LatLng latLng) {

                aMap.clear();//清除所有的地图上覆盖物
                Polygon polygon = aMap.addPolygon(pOption.strokeColor(Color.BLUE).fillColor(Color.argb(0, 0, 0, 0)));//重画区域边界
                boolean flag = polygon.contains(latLng);

                if (flag) {       //是否在区域内
                    //点击地图，显示标注
                    final Marker marker = aMap.addMarker(new MarkerOptions().
                            position(latLng).
                            title("详细地址").
                            snippet("DefaultMarker"));
                    //根据latLng编译成地理描述
                    latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
                    getAddress(latLonPoint);
                    markerLatLng = latLng;
                } else {
                    Toast.makeText(BoundaryActivity.this, "请在区域内点击",Toast.LENGTH_LONG).show();
                }


            }
        });

    }


    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }


    protected void onInitVariable() {

        addrThree = getIntent().getStringExtra("districtId");


    }

    protected void onInitView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_boundary);
        //获取地图控件引用
        mapView = (MapView) findViewById(R.id.map2);
        txt_map_detailaddr = (TextView) findViewById(R.id.txt_map_detailaddr);
        btn_map_ok = (Button) findViewById(R.id.btn_map_ok);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mapView.onCreate(savedInstanceState);
        init();

    }

    protected void onLoadData() {
        // TODO Auto-generated method stub

    }

    protected void onRequestData() {
        // TODO Auto-generated method stub

    }


    protected void onUnLoadData() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle arg0) {

        super.onSaveInstanceState(arg0);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mapView.onSaveInstanceState(arg0);
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.clear();
        DistrictSearch search = new DistrictSearch(getApplicationContext());
        DistrictSearchQuery query = new DistrictSearchQuery();
        query.setKeywords(addrThree);
        query.setShowBoundary(true);
        search.setQuery(query);
        search.setOnDistrictSearchListener(this);
        search.searchDistrictAsyn();
        //返回地址详细信息代码
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);

    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            PolylineOptions polylineOption = (PolylineOptions) msg.obj;
            aMap.addPolyline(polylineOption);

        }
    };

    @Override
    public void onDistrictSearched(DistrictResult districtResult) {

        if (districtResult == null || districtResult.getDistrict() == null) {
            return;
        }
        final DistrictItem item = districtResult.getDistrict().get(0);


        if (item == null) {
            return;
        }
        centerLatLng = item.getCenter();//得到行政中心点坐标
        if (centerLatLng != null) {  //地图加载时就显示行政区域
            aMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(new LatLng(centerLatLng.getLatitude(), centerLatLng.getLongitude()), 13));//13为缩放级别
        }


        new Thread() {
            private PolylineOptions polylineOption;


            public void run() {

                String[] polyStr = item.districtBoundary();
                if (polyStr == null || polyStr.length == 0) {
                    return;
                }
                for (String str : polyStr) {
                    String[] lat = str.split(";");
                    polylineOption = new PolylineOptions();
                    boolean isFirst = true;
                    LatLng firstLatLng = null;
                    for (String latstr : lat) {
                        String[] lats = latstr.split(",");
                        if (isFirst) {
                            isFirst = false;
                            firstLatLng = new LatLng(Double
                                    .parseDouble(lats[1]), Double
                                    .parseDouble(lats[0]));
                        }
                        polylineOption.add(new LatLng(Double
                                .parseDouble(lats[1]), Double
                                .parseDouble(lats[0])));
                    }
                    if (firstLatLng != null) {
                        polylineOption.add(firstLatLng);
                    }

                    polylineOption.width(6).color(Color.BLUE);
                    Message message = handler.obtainMessage();
                    message.obj = polylineOption;
                    handler.sendMessage(message);


                }
                pOption = new PolygonOptions();
                pOption.addAll(polylineOption.getPoints());//转换成PolygonOptions类型，为了判断marker是否在区域内

            }
        }.start();


    }


    @Override
    public void onGeocodeSearched(GeocodeResult arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {


        if (rCode == 1000) {

            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {

                addressName = result.getRegeocodeAddress().getFormatAddress();

                txt_map_detailaddr.setText(addressName);

            } else {

            }
        } else {

        }

    }

}