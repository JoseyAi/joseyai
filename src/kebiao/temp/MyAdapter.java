package kebiao.temp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.Adapter;
import kk.index.AActivity;
public class MyAdapter {

	private Context context;
	private AActivity main;
	private Cursor[] cursor=new Cursor[7];
	private SimpleCursorAdapter[] adapter;
	
	private SharedPreferences preferences;
	
	public MyAdapter(Context context){
		this.context=context;
		main=(AActivity) context;
	}
	public void test(){
	
	
			
	}
	
}
