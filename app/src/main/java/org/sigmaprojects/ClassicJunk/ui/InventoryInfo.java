package org.sigmaprojects.ClassicJunk.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.sigmaprojects.ClassicJunk.R;
import org.sigmaprojects.ClassicJunk.api.beans.Inventory;
import org.sigmaprojects.ClassicJunk.api.beans.Location;
import org.sigmaprojects.ClassicJunk.util.Utils;

public class InventoryInfo extends Activity {

    public final static String ARG_INVENTORY = "inventory";

    public final static String ARG_POSITION = "position";

    public static final String INVENTORY_INFO_SWIPE_NEXT = "org.sigmaprojects.ClassicJunk.INVENTORY_INFO_SWIPE_NEXT";
    public static final String INVENTORY_INFO_SWIPE_PREVIOUS = "org.sigmaprojects.ClassicJunk.INVENTORY_INFO_SWIPE_PREVIOUS";


    private final String TAG = InventoryInfo.class.getName();

    private Inventory inventory;
    private int lastPosition = -1;

    private Context mContext;

    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_info);

        mContext = this;

        // attempt to center the title
        try {
            ((TextView) ((FrameLayout) ((LinearLayout) ((ViewGroup) getWindow().getDecorView()).getChildAt(0)).getChildAt(0)).getChildAt(0)).setGravity(Gravity.CENTER);
        } catch (Exception e) {}
        try {
            ((TextView)((LinearLayout)((ViewGroup) getWindow().getDecorView()).getChildAt(0)).getChildAt(0)).setGravity(Gravity.CENTER);
        } catch (Exception e) {}


        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        Bundle b = getIntent().getExtras();
        inventory = (Inventory)b.getParcelable(ARG_INVENTORY);
        lastPosition = b.getInt(ARG_POSITION);

        if( inventory != null ) {
            try {
                ImageView carImage = (ImageView) this.findViewById(R.id.carImage);
                TextView address = (TextView) this.findViewById(R.id.address);
                TextView phoneNumber = (TextView) this.findViewById(R.id.phonenumber);

                super.setTitle(inventory.getCaryear() + " " + inventory.getCar());

                Location location = inventory.getLocation();

                address.setText("Address: " + location.getAddress().replace(", United States", ""));

                String formattedPhone = Utils.formatPhone(location.getphonenumber());
                phoneNumber.setText("Phone: " + formattedPhone);

                //Log.v(TAG, "imageURL: " + inventory.getImageurl() + "?h=400");

                Picasso.with(this)
                        .load(inventory.getImageurl() + "?h=400")
                         .placeholder(R.drawable.car_image_placeholder)
                        .error(R.drawable.car_image_error)
                        .into(carImage);
            } catch (Exception e) {

            }

        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        boolean eventResult = this.mDetector.onTouchEvent(event);
        //Log.v(TAG, "eventResult: " + eventResult);
        //finish();
        /*
        if( eventResult ) {
            finish();
            return true;
        } else {
            finish();
            return true;
        }
        */
        if( eventResult ) {
            finish();
            return true;
        }
        return false;
        //return eventResult;
    }




    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            String swipe = "";
            float sensitivity = 50;

            boolean SWIPE_LEFT = false;
            boolean SWIPE_RIGHT = false;
            boolean SWIPE_UP = false;
            boolean SWIPE_DOWN = false;

            // if Swipe Left is first, go to next
            // if Swipe UP is first, go next

            // TODO Auto-generated method stub
            if((e1.getX() - e2.getX()) > sensitivity){
                swipe += "Swipe Left\n";
                SWIPE_LEFT = true;
            }else if((e2.getX() - e1.getX()) > sensitivity){
                swipe += "Swipe Right\n";
                SWIPE_RIGHT = true;
            }else{
                swipe += "\n";
            }

            if((e1.getY() - e2.getY()) > sensitivity){
                swipe += "Swipe Up\n";
                SWIPE_UP = true;
            }else if((e2.getY() - e1.getY()) > sensitivity){
                swipe += "Swipe Down\n";
                SWIPE_DOWN = true;
            }else{
                swipe += "\n";
            }

            //Log.v(TAG, "gesture: " + swipe);

            Intent intent;
            if( SWIPE_LEFT || SWIPE_UP ) {
                intent = new Intent(INVENTORY_INFO_SWIPE_NEXT);
            } else {
                intent = new Intent(INVENTORY_INFO_SWIPE_PREVIOUS);
            }

            if( intent.getAction() != null ) {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }

            //return super.onFling(e1, e2, velocityX, velocityY);
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            //Log.d(TAG, "onSingleTapUp: " + event.toString());
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent event) {
            //Log.d(TAG, "onDoubleTap: " + event.toString());
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent event) {
            //Log.d(TAG, "onDoubleTapEvent: " + event.toString());
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            //Log.d(TAG, "onSingleTapConfirmed: " + event.toString());
            finish();
            return true;
        }

    };




}
