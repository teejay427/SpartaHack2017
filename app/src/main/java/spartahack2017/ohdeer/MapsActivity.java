package spartahack2017.ohdeer;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

	@Override
	protected void onCreate( Bundle savedInstanceState ){
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_maps );
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = ( SupportMapFragment ) getSupportFragmentManager()
				.findFragmentById( R.id.map );
		mapFragment.getMapAsync( this );
	}


	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used.
	 * This is where we can add markers or lines, add listeners or move the camera. In this case,
	 * we just add a marker near Sydney, Australia.
	 * If Google Play services is not installed on the device, the user will be prompted to install
	 * it inside the SupportMapFragment. This method will only be triggered once the user has
	 * installed Google Play services and returned to the app.
	 */
	@Override
	public void onMapReady( GoogleMap googleMap ){

		Log.i( "Count", Integer.toString( MainActivity.forMap.size() ) );
		LatLng tempLatLng;
		for( myLocation tempLocation : MainActivity.forMap ){
			//Log.i( "i", Integer.toString( i ) );
			tempLatLng = new LatLng( tempLocation.lat, tempLocation.lon );
			//Log.i( "LatLng", tempLatLng.toString() );
			String title = Integer.toString( tempLocation.calendar.get( Calendar.MONTH ) ) + "/" + Integer.toString( tempLocation.calendar.get( Calendar.DAY_OF_MONTH ) ) + "/" + Integer.toString( tempLocation.calendar.get( Calendar.YEAR ) );
			//Log.i( "title", title );
			googleMap.addMarker( new MarkerOptions().position( tempLatLng ).title( title ) );
		}

		float zoomLevel = 12.0f;
		//Log.i( "location", MainActivity.currentLocation.toString() );
		if( MainActivity.currentLocation != null ){
			googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom( MainActivity.currentLocation, zoomLevel ) );
		}
	}

}
