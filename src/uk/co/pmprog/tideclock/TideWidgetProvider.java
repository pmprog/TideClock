package uk.co.pmprog.tideclock;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TideWidgetProvider extends AppWidgetProvider
{
	public static final String PREFS_NAME = "TidePrefs";
	
	Calendar ShowDT = null;
	int ShowIsHT = 0;
	
	  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	  {

		  SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_WORLD_READABLE);
		  if( settings == null )
			  return;

		  DateFormat df = new SimpleDateFormat("dd/MMM HH:mm");
		  
		  int appWidgetId = appWidgetIds[0];
		  
	      //Intent intent = new Intent(context, TideClockActivity.class);
	      //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
	
		  RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget1);

			try
			{
				int isHigh = 1;
				if( settings.getInt("year", 0) != 0 )
				{
					Calendar now = Calendar.getInstance();
					Calendar c;
					
					if( ShowDT == null )
					{
						c = Calendar.getInstance();
						c.set(settings.getInt("year", 0), settings.getInt("month", 0), settings.getInt("day", 0), settings.getInt("hour", 0), settings.getInt("minute", 0));
						isHigh = 1;
					} else {
						c = (Calendar)ShowDT.clone();
						isHigh = ShowIsHT;
					}
					now.set(Calendar.HOUR_OF_DAY, 0);
					now.set(Calendar.MINUTE, 0);
					
					views.setTextViewText(R.id.textView1, String.valueOf(settings.getString("location", "TideClock")));
					/*
					views.setTextViewText(R.id.textView1, String.valueOf(settings.getInt("year", 0)));
					views.setTextViewText(R.id.textView4, String.valueOf(c.getTime().after(now.getTime())));
					views.setTextViewText(R.id.textView2, String.format("%s - %s", df.format(c.getTime()), df.format(now.getTime())));
					*/

					int timeout = 0;
			    	while( c.getTime().before(now.getTime()) )
			    	{
			    		c.add(Calendar.SECOND, 30);
			    		c.add(Calendar.MINUTE, 12);
			    		c.add(Calendar.HOUR_OF_DAY, 6);
			    		isHigh = 1 - isHigh;
			    		timeout++;
			    		if( timeout >= 400 )
			    			break;
			    	}
			    	/*
			    	SharedPreferences.Editor editor = settings.edit();
			        editor.putInt("year", c.get(Calendar.YEAR));
			        editor.putInt("month", c.get(Calendar.MONTH));
			        editor.putInt("day", c.get(Calendar.DATE));
			        editor.putInt("hour", c.get(Calendar.HOUR_OF_DAY));
			        editor.putInt("minute", c.get(Calendar.MINUTE));
			        editor.commit();
			        */
			    	/*
		    		views.setTextViewText(R.id.textView5, String.valueOf(c.getTime().after(now.getTime())));
		    		views.setTextViewText(R.id.textView3, String.format("%s - %s", df.format(c.getTime()), df.format(now.getTime())));
		    		*/

			    	if( timeout >= 400 )
			    	{
						views.setTextViewText(R.id.textView2, "Please set a more");
			    		views.setTextViewText(R.id.textView3, "recent high tide");
			    	} else {
			    		views.setTextViewText(R.id.textView2, df.format(c.getTime()));
						ShowDT = c; // (Calendar)c.clone();
						ShowIsHT = isHigh;
						
						c.add(Calendar.SECOND, 30);
			    		c.add(Calendar.MINUTE, 12);
			    		c.add(Calendar.HOUR_OF_DAY, 6);
			    		views.setTextViewText(R.id.textView3, df.format(c.getTime()));
			    		
			    		if( isHigh == 0 )
			    		{
			    			views.setTextViewText(R.id.textView4, "Low");
			    			views.setTextViewText(R.id.textView5, "High");
			    		} else {
			    			views.setTextViewText(R.id.textView4, "High");
			    			views.setTextViewText(R.id.textView5, "Low");
			    		}

			    	}

				}
				
				appWidgetManager.updateAppWidget(appWidgetId, views);
			} catch( Exception x ) {
				views.setTextViewText(R.id.textView1, x.getMessage());
			} finally {
			}
	      
	  }
}
