package kk.chat;

import com.appkefu.lib.xmpp.XmppBuddies;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import kk.index.R;

public class AddFriendActivity extends Activity implements OnClickListener{
	
	private EditText mFriendUsername; // �ʺű༭��

	private Button mAddFriendBtn;
	private Button mBackBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_add_friend);
		
		mFriendUsername = (EditText) findViewById(R.id.add_user_edit);
		
		mAddFriendBtn = (Button) findViewById(R.id.add_btn);
		mAddFriendBtn.setOnClickListener(this);
		
		mBackBtn = (Button) findViewById(R.id.add_reback_btn);
		mBackBtn.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.add_btn:
			add_friend();
			break;
		case R.id.add_reback_btn:
			finish();
			break;
		default:
			break;
		}
	}
	
	public void add_friend() 
	{
		String friendUsername = mFriendUsername.getText().toString();
		if ("".equals(friendUsername))// �ж� �ʺź�����
		{
			new AlertDialog.Builder(AddFriendActivity.this)
					.setIcon(
							getResources().getDrawable(
									R.drawable.login_error_icon))
					.setTitle("��Ӻ��Ѵ���").setMessage("�Է��ʺŲ���Ϊ�գ�\n��������ٵ�¼��")
					.create().show();
		}
		else if(friendUsername.contains("@"))
		{
			Toast.makeText(this, "���зǷ��ַ�", Toast.LENGTH_SHORT).show();
			return;
		}
		else 
		{
			//��Ӻ���
			XmppBuddies.addFriend(friendUsername);
		}  
	}

}




















