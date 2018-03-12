package kk.chat;

import com.appkefu.lib.xmpp.XmppVCard;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import kk.index.R;

public class ProfileChangeActivity extends Activity implements OnClickListener{

	private Button   changeBtn;
	private TextView profileFieldTextView;
	
	private String  profileField;
	private String 	profileValue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_profile_change);
		
		changeBtn = (Button) findViewById(R.id.change_profile_btn);
		changeBtn.setOnClickListener(this);
		
		profileFieldTextView = (TextView) findViewById(R.id.change_profile_user_edit);
		
		profileField = getIntent().getStringExtra("profileField");
		profileValue = getIntent().getStringExtra("value");
		
		profileFieldTextView.setText(profileValue);
	}
	
	public void changeProfile()
	{		
		profileValue = profileFieldTextView.getText().toString();
		if(profileValue.length() <= 0)
		{
			Toast.makeText(this, "²»ÄÜÎª¿Õ", Toast.LENGTH_SHORT).show();
			return;
		}	
		
		changeBtn.setEnabled(false);
		changeProfileThread();
	}
	
	public void changeProfileThread()
	{
    	final Handler handler = new Handler(){
    		public void handleMessage(Message msg) 
			{
				if(msg.what == 1)
				{					
					changeBtn.setEnabled(true);
					finish();
				}
			}
    	};
    	
    	new Thread() 
		{
			public void run() 
			{
				Message msg = new Message();
				
				XmppVCard.setField(profileField, profileValue);

				msg.what = 1; 
				msg.obj = null; 
								
				handler.sendMessage(msg);
			}
		}.start();    	
    }

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()) {
		case R.id.change_profile_reback_btn:
			finish();
			break;
		case R.id.change_profile_btn:
			changeProfile();
			break;
		default:
			break;
		}	
	}

}
