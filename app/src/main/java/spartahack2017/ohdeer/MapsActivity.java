package spartahack2017.ohdeer;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

	private GoogleMap mMap;
	private boolean plotAroundDeer;
	private boolean showPopUps = true;

	@Override
	protected void onCreate( Bundle savedInstanceState ){
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_maps );
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = ( SupportMapFragment ) getSupportFragmentManager()
				.findFragmentById( R.id.map );
		mapFragment.getMapAsync( this );

		plotAroundDeer = this.getIntent().getBooleanExtra( MainActivity.AVOID_DEER, false );
		showPopUps = this.getIntent().getBooleanExtra( MainActivity.POP_UPS, false );
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
		mMap = googleMap;

		// Add a marker in Sydney and move the camera
		LatLng sydney = new LatLng( -34, 151 );
		mMap.addMarker( new MarkerOptions().position( sydney ).title( "Marker in Sydney" ) );
		mMap.moveCamera( CameraUpdateFactory.newLatLng( sydney ) );

		LatLng tempLatLng;
		for( myLocation tempLocation : MainActivity.forMap ){
			tempLatLng = new LatLng( tempLocation.lat, tempLocation.lon );
			mMap.addMarker( new MarkerOptions().position( tempLatLng ).title(
				tempLocation.calendar.get( Calendar.YEAR ) + "-" +
				tempLocation.calendar.get( Calendar.MONTH ) + "-" +
				tempLocation.calendar.get( Calendar.DAY_OF_MONTH ) ) );
		}

		float zoomLevel = 12.0f;
		mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( MainActivity.currentLocation, zoomLevel ) );
	}
}
