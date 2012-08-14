package uk.co.pmprog.tideclock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class TideClockActivity extends Activity {
	public static final String PREFS_NAME = "TidePrefs";
	
	
	DateFormat df = new SimpleDateFormat("dd/MMM/yyyy HH:mm");
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	try
    	{
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	        
	        DatePicker dp = (DatePicker)findViewById(R.id.datePicker1);
	        TimePicker tp = (TimePicker)findViewById(R.id.timePicker1);
	        EditText ev = (EditText)findViewById(R.id.editText1);
	        Date now = new Date();
	        
	        Button b = (Button) findViewById(R.id.button1);
	        b.setOnClickListener(
	            	new View.OnClickListener()
	            	{
	    				public void onClick(View v)
	    				{
	    			    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_WORLD_READABLE);
	    					  if( settings == null )
	    						  return;

	    			    	SharedPreferences.Editor editor = settings.edit();
	    			        DatePicker dp = (DatePicker)findViewById(R.id.datePicker1);
	    			        TimePicker tp = (TimePicker)findViewById(R.id.timePicker1);
	    			        EditText ev = (EditText)findViewById(R.id.editText1);
	    			        editor.putInt("year", dp.getYear());
	    			        editor.putInt("month", dp.getMonth());
	    			        editor.putInt("day", dp.getDayOfMonth());
	    			        editor.putInt("hour", tp.getCurrentHour());
	    			        editor.putInt("minute", tp.getCurrentMinute());
	    			        editor.putString("location", ev.getText().toString());
	    			        editor.commit();
	    			        
	    			        Calendar c = Calendar.getInstance();
							c.set( dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), tp.getCurrentHour(), tp.getCurrentMinute() );
	    			        
	    			        Toast t = Toast.makeText(getApplicationContext(), String.format("High Tide for %s set to %s", ev.getText(), df.format( c.getTime() )), Toast.LENGTH_SHORT);
	    			        t.show();
	    				}
	    			}
	            );
	        
	        b = (Button) findViewById(R.id.button2);
	        b.setOnClickListener(
	            	new View.OnClickListener()
	            	{
	    				public void onClick(View v)
	    				{
	    			        finish();
	    			        System.exit(0);
	    				}
	    			}
	            );
	        
	        
	      SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_WORLD_READABLE);
		  if( settings == null )
			  return;
	        try
	        {
	        	dp.init(settings.getInt("year", now.getYear() + 1900), settings.getInt("month", now.getMonth()), settings.getInt("day", now.getDate()), null);
	        } catch( Exception x ) {
	        } finally {
	        }
	    	tp.setCurrentHour(settings.getInt("hour", now.getHours()));
	    	tp.setCurrentMinute(settings.getInt("minute", now.getMinutes()));
	    	ev.setText(settings.getString("location", "TideClock"));
    	} catch( Exception x ) {
    	} finally {
    	}
    }

}