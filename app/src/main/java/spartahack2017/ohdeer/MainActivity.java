package spartahack2017.ohdeer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

	final static int MY_PERMISSIONS_REQUEST_LOCATION = 0;
	final Cloud cloud = new Cloud();
	LocationManager locationManager = null;
	private final ActiveListener activeListener = new ActiveListener();
	private double lon;
	private double lat;
	private float bearing;
	static ArrayList<myLocation> forMap = new ArrayList<>();
	static LatLng currentLocation;
	private static final Random random = new Random();

	@Override
	protected void onCreate( Bundle savedInstanceState ){
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

		checkIfPermissionsGranted();

		new Thread( new Runnable() {
			@Override
			public void run(){
				runTips();
			}
		} ).start();

		// Get the location manager
		locationManager = ( LocationManager ) getSystemService( Context.LOCATION_SERVICE );
	}


	public void updateText( String newText ){
		( ( TextView ) findViewById( R.id.textView ) ).setText( newText );
	}


	public void runTips(){
		Resources res = getResources();
		String[] tipsArray;

		tipsArray = res.getStringArray( R.array.myArray );
		int sleepTime;

		//noinspection InfiniteLoopStatement
		while( true ){
			final String nextTip = tipsArray[ random.nextInt( tipsArray.length ) ];

			Log.i( "nextTip", "Next tip: " + nextTip );

			Handler mainHandler = new Handler( MainActivity.this.getMainLooper() );
			Runnable myRunnable = new Runnable() {
				@Override
				public void run(){
					updateTip( nextTip );
				}
			};
			mainHandler.post( myRunnable );

			sleepTime = nextTip.length() * 100;
			SystemClock.sleep( sleepTime );
		}
	}


	public void updateTip( String nextTip ){
		TextSwitcher textSwitcher = ( TextSwitcher ) findViewById( R.id.tipsTextSwitcher );
		Log.d( "textSwitcher", Boolean.toString( textSwitcher == null ) );
		Log.d( "nextTip", Boolean.toString( nextTip == null ) );
		if( textSwitcher != null && nextTip != null ){
			textSwitcher.setText( nextTip );
		}
	}


	public void getCloudData(){
		String cloudData = cloud.getDataFromCloud( lat, lon );
		ArrayList<String> locations = new ArrayList<>();
		ArrayList<myLocation> sqlLocations = new ArrayList<>();

		Collections.addAll( locations, cloudData.split( ";" ) );

		locations.remove( locations.size() - 1 );

		String data = "Current location: " + Double.toString( lat ) + ", " + Double.toString( lon ) + "\n";
		data += "Bearing: " + Float.toString( bearing ) + "\n";
		double tempLat;
		double tempLon;
		Calendar calendar = new Calendar() {
			@Override
			protected void computeTime(){

			}

			@Override
			protected void computeFields(){

			}

			@Override
			public void add( int i, int i1 ){

			}

			@Override
			public void roll( int i, boolean b ){

			}

			@Override
			public int getMinimum( int i ){
				return 0;
			}

			@Override
			public int getMaximum( int i ){
				return 0;
			}

			@Override
			public int getGreatestMinimum( int i ){
				return 0;
			}

			@Override
			public int getLeastMaximum( int i ){
				return 0;
			}
		};
		for( String location : locations ){
			String[] dataPoints = location.split( "," );
			calendar.set( Integer.parseInt( dataPoints[ 0 ].substring( 0, 4 ) ), Integer.parseInt( dataPoints[ 0 ].substring( 5, 7 ) ), Integer.parseInt( dataPoints[ 0 ].substring( 8, 10 ) ) );
			tempLat = Double.parseDouble( dataPoints[ 1 ] );
			tempLon = Double.parseDouble( dataPoints[ 2 ] );
			sqlLocations.add( new myLocation( calendar, tempLat, tempLon ) );
		}

		ArrayList<myLocation> nearLocations = new ArrayList<>();
		double updatedLat;
		double updatedLon;
		for( int i = 0; i < 3; ++i ){
			updatedLat = i * 0.01 * Math.cos( bearing - 90 ) + lat;
			updatedLon = i * 0.01 * Math.sin( bearing - 90 ) + lon;

			for( myLocation tempLocation : sqlLocations ){
				float[] distance = new float[ 1 ];

				Location.distanceBetween( updatedLat, updatedLon, tempLocation.lat, tempLocation.lon, distance );

				if( distance[ 0 ] < 4828 ){
					if( !nearLocations.contains( tempLocation ) ){
						nearLocations.add( tempLocation );
						data += nearLocations.get( nearLocations.size() - 1 ).toString() + "\n";
					}
				}
			}
		}

		data += "Count: " + Integer.toString( nearLocations.size() ) + "\n";

		Log.i( "data", data );

		forMap = nearLocations;

		final String finalData = data;

		Handler mainHandler = new Handler( MainActivity.this.getMainLooper() );

		Runnable myRunnable = new Runnable() {
			@Override
			public void run(){
				updateText( finalData );
			}
		};
		mainHandler.post( myRunnable );
	}


	public void onLaunchMapButtonClick( View view ){
		Intent mapActivity = new Intent( this, MapsActivity.class );
		startActivity( mapActivity );
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
		bearing = location.getBearing();
		currentLocation = new LatLng( lat, lon );
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
