package kk.chat;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import kk.index.R;

public class GroupChatActivity extends Activity implements OnClickListener {

	private Button mBackBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_group_chat);
		
		mBackBtn = (Button) findViewById(R.id.groupchat_reback_btn);
		mBackBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {

		case R.id.groupchat_reback_btn:
			finish();
			break;
		default:
			break;
		}
	}

}
