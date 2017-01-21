package spartahack2017.ohdeer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class MainActivity extends AppCompatActivity {

	static String AVOID_DEER = "AVOID_DEER";
	static String POP_UPS = "POP_UPS";

	@Override
	protected void onCreate( Bundle savedInstanceState ){
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
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
}
