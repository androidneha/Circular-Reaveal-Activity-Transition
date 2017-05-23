package demo.com.circulartransition;

/**
 * Created by Developer-1 on 5/22/2017.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class BottomFragment extends Fragment implements OnMapReadyCallback {
    MapView mapView;
    GoogleMap googleMap;
    FrameLayout rootLayout; CardView top,bottom;
    Animation animShow, animHide, animShow2, animHide2;
    public BottomFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);
        //getActivity().setTitle("Contact Us");
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        top = (CardView)view.findViewById(R.id.pathview);
        bottom = (CardView)view.findViewById(R.id.pathview2);
        initAnimation();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView.getMapAsync(this);
        myfunc();

    }

    public void myfunc()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bottom.setVisibility(View.VISIBLE);
                bottom.startAnimation( animShow );
                top.setVisibility(View.VISIBLE);
                top.startAnimation( animShow2 );
            }
        },4000);
    }
    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMapReady(GoogleMap googleMapp) {
        googleMap=googleMapp;
        if(mapView.isAttachedToWindow()){
            doCircularReveal();
            Log.e("err11","err11");
        }

        if(googleMap!=null) {
            final Marker m = googleMap.addMarker(new MarkerOptions()//.icon(BitmapDescriptorFactory.fromResource(R.drawable.mark))
                    .position(new LatLng(28.603108, 77.1032100)));
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            //googleMap.setMyLocationEnabled(true);

//            MapsInitializer.initialize(this.getActivity());
//            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//            builder.include(new LatLng(28.603108, 77.1032100));
//            LatLngBounds bounds = builder.build();
//            int padding = 0;
            // Updates the location and zoom of the MapView
            LatLng drievrCurrentLocation = new LatLng(28.603108, 77.1032100);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(drievrCurrentLocation, 13));


            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final long duration = 5000;
            final Interpolator interpolator = new BounceInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = Math.max(1 - interpolator
                            .getInterpolation((float) elapsed / duration), 0);
                    m.setAnchor(0.5f, 1.0f + 2 * t);

                    if (t > 0.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    }
                }
            });
        }
    }
    private void initAnimation()
    {
        animShow = AnimationUtils.loadAnimation( getActivity(), R.anim.view_show);
        animHide = AnimationUtils.loadAnimation( getActivity(), R.anim.hide);
        animShow2 = AnimationUtils.loadAnimation( getActivity(), R.anim.view_show2);
        animHide2 = AnimationUtils.loadAnimation( getActivity(), R.anim.hide2);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void doCircularReveal()
    {
        int centerX = (mapView.getLeft() + mapView.getRight()) / 2;
        int centerY = (mapView.getTop() + mapView.getBottom()) / 2;
        int startRadius = 0;
        int endRadius = Math.max(mapView.getWidth(), mapView.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(mapView,centerX, centerY, startRadius, endRadius);
        anim.setDuration(2000);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        mapView.setVisibility(View.VISIBLE);
        anim.start();
    }


}