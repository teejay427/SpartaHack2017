package spartahack2017.ohdeer;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

class Cloud {

	private static final String SQL_URL = "http://35.20.93.181/";

	@SuppressLint( "DefaultLocale" )
	String getDataFromCloud( Double lat, Double lon ){
		String response;
		String postDataStr = "";

		//Log.d( "data", "starting data pull" );

		try{
			postDataStr = "lat=" + URLEncoder.encode( String.format( "%.1f", lat ), "UTF-8" );
			postDataStr += "&lon=" + URLEncoder.encode( String.format( "%.1f", lon ), "UTF-8" );
		}
		catch( UnsupportedEncodingException ignore ){
		}
		byte[] postData = postDataStr.getBytes();

		InputStream stream = null;

		try{
			URL url = new URL( SQL_URL );

			HttpURLConnection conn = ( HttpURLConnection ) url.openConnection();

			conn.setDoOutput( true );
			conn.setRequestMethod( "POST" );
			conn.setRequestProperty( "Content-Length", Integer.toString( postData.length ) );
			conn.setUseCaches( false );

			OutputStream out = conn.getOutputStream();
			out.write( postData );
			out.close();

			int responseCode = conn.getResponseCode();
			if( responseCode != HttpURLConnection.HTTP_OK ){
				return "http not ok";
			}

			stream = conn.getInputStream();

			BufferedReader r = new BufferedReader( new InputStreamReader( stream ) );
			StringBuilder total = new StringBuilder();
			String line;
			while( ( line = r.readLine() ) != null ){
				total.append( line ).append( '\n' );
			}

			response = total.substring( 199 );

			stream.close();
			//Log.d( "data", "finished data pull" );
			return response;
		}
		catch( MalformedURLException e ){
			return "malformed url";
		}
		catch( IOException ex ){
			return "ioexception";
		}
		finally{
			if( stream != null ){
				try{
					stream.close();
				}
				catch( IOException ex ){
					// Fail silently
				}
			}
		}
	}

}
