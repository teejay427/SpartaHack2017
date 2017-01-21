package spartahack2017.ohdeer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

	static String AVOID_DEER = "AVOID_DEER";
	static String POP_UPS = "POP_UPS";
	final static int MY_PERMISSIONS_REQUEST_LOCATION = 0;
	final Cloud cloud = new Cloud();
	LocationManager locationManager = null;
	private final ActiveListener activeListener = new ActiveListener();
	private double lon;
	private double lat;


	@Override
	protected void onCreate( Bundle savedInstanceState ){
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

		checkIfPermissionsGranted();

		// Get the location manager
		locationManager = ( LocationManager ) getSystemService( Context.LOCATION_SERVICE );

		new Thread(
				new Runnable() {
					@Override
					public void run(){
						getCloudData();
					}
				}
		).start();
	}


	public void updateText( String newText ){
		( ( TextView ) findViewById( R.id.textView ) ).setText( newText );
	}


	public void getCloudData(){
		final String data = "Current Location: " + Double.toString( lat ) + ", " + Double.toString( lon ) + "\n" + cloud.getDataFromCloud();
		Log.i( "data", data );

		Handler mainHandler = new Handler( MainActivity.this.getMainLooper() );

		Runnable myRunnable = new Runnable() {
			@Override
			public void run(){
				updateText( data );
			}
		};
		mainHandler.post( myRunnable );
	}


	public void onLaunchMapButtonClick( View view ){
		Intent mapActivity = new Intent( this, MapsActivity.class );
		mapActivity.putExtra( AVOID_DEER, getAvoidDeerCheckBox().isChecked() );
		mapActivity.putExtra( POP_UPS, getPopUpsCheckBox().isChecked() );
		startActivity( mapActivity );
		this.finish();
	}


	private CheckBox getAvoidDeerCheckBox(){
		return ( CheckBox ) findViewById( R.id.avoidDeerCheckBox );
	}


	private CheckBox getPopUpsCheckBox(){
		return ( CheckBox ) findViewById( R.id.popUpsCheckBox );
	}


	@Override
	public void onRequestPermissionsResult( int requestCode, @NonNull String permissions[], @NonNull int[] grantResults ){
		switch( requestCode ){
			case MY_PERMISSIONS_REQUEST_LOCATION:
				// If request is cancelled, the result arrays are empty.
				if( grantResults.length > 0 && grantResults[ 0 ] != PackageManager.PERMISSION_GRANTED ){
					Toast.makeText( this, getString( R.string.permissions_rationale ), Toast.LENGTH_LONG ).show();
					ActivityCompat.requestPermissions( this, new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION }, MY_PERMISSIONS_REQUEST_LOCATION );
				}
				break;
		}
	}


	private void checkIfPermissionsGranted(){
		// Here, thisActivity is the current activity
		if( ContextCompat.checkSelfPermission( this, android.Manifest.permission.READ_CONTACTS ) != PackageManager.PERMISSION_GRANTED ){
			ActivityCompat.requestPermissions( this, new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION }, MY_PERMISSIONS_REQUEST_LOCATION );
		}
	}


	@SuppressWarnings( "MissingPermission" )
	private void unregisterListeners(){
		locationManager.removeUpdates( activeListener );
	}


	@SuppressWarnings( "MissingPermission" )
	private void registerListeners(){
		unregisterListeners();

		// Create a Criteria object
		Criteria criteria = new Criteria();
		criteria.setAccuracy( Criteria.ACCURACY_FINE );
		criteria.setPowerRequirement( Criteria.POWER_HIGH );
		criteria.setAltitudeRequired( true );
		criteria.setBearingRequired( false );
		criteria.setSpeedRequired( false );
		criteria.setCostAllowed( false );

		String bestAvailable = locationManager.getBestProvider( criteria, true );

		if( bestAvailable != null ){
			locationManager.requestLocationUpdates( bestAvailable, 500, 1, activeListener );
			Location location = locationManager.getLastKnownLocation( bestAvailable );
			onLocation( location );
			Log.i( "provider", bestAvailable );
		}
	}


	private void onLocation( Location location ){
		if( location == null ){
			return;
		}

		Log.i( "location", Double.toString( location.getLatitude() ) + ", " + Double.toString( location.getLongitude() ) );
		lat = location.getLatitude();
		lon = location.getLongitude();
		new Thread(
				new Runnable() {
					@Override
					public void run(){
						getCloudData();
					}
				}
		).start();
	}


	private class ActiveListener implements LocationListener {

		@Override
		public void onLocationChanged( Location location ){
			onLocation( location );
		}

		@Override
		public void onStatusChanged( String s, int i, Bundle bundle ){

		}

		@Override
		public void onProviderEnabled( String provider ){
			registerListeners();
		}

		@Override
		public void onProviderDisabled( String provider ){
			registerListeners();
		}
	}


	@Override
	protected void onResume(){
		super.onResume();

		registerListeners();
	}


	@Override
	protected void onPause(){
		unregisterListeners();
		super.onPause();
	}
}
