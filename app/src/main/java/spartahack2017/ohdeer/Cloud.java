package spartahack2017.ohdeer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

class Cloud {

	private static final String SQL_URL = "http://35.20.93.181/";

	String getDataFromCloud(){
		String response = "";
		try{
			URL sql_data = new URL( SQL_URL );

			BufferedReader in = new BufferedReader( new InputStreamReader( sql_data.openStream() ) );

			String inputLine;

			while( ( inputLine = in.readLine() ) != null ){
				response += inputLine;
			}

			in.close();
		}
		catch( IOException e ){
			e.printStackTrace();
		}

		return response;
	}

}
