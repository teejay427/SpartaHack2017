package spartahack2017.ohdeer;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

	static String AVOID_DEER = "AVOID_DEER";
	static String POP_UPS = "POP_UPS";
	final static int MY_PERMISSIONS_REQUEST_LOCATION = 0;


	@Override
	protected void onCreate( Bundle savedInstanceState ){
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );

		checkIfPermissionsGranted();
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
}
