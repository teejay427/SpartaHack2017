package spartahack2017.ohdeer;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

		LatLng lansing = new LatLng( 42.712697, -84.544615 );
		if( plotAroundDeer ){
			mMap.addMarker( new MarkerOptions().position( lansing ).title( "Marker in Lansing" ) );
			// This goes up to 21, 7.0 is good for zooming into the lower peninsula on a Nexus 7
			float zoomLevel = 7.0f;
			mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( lansing, zoomLevel ) );
		}

	}
}
