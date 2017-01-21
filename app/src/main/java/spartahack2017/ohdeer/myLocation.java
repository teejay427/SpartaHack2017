package spartahack2017.ohdeer;

import java.util.Calendar;

class myLocation {

	Calendar calendar;
	Double lat;
	Double lon;

	myLocation( Calendar calendar, Double lat, Double lon ){
		this.calendar = calendar;
		this.lat = lat;
		this.lon = lon;
	}

	@Override
	public String toString(){
		return calendar.get( Calendar.YEAR ) + "-" + calendar.get( Calendar.MONTH ) + "-" + calendar.get( Calendar.DAY_OF_MONTH ) + "," + Double.toString( lat ) + "," + Double.toString( lon );
	}
}
