package kk.chat;

import com.appkefu.lib.service.KFMainService;
import com.appkefu.lib.service.KFSettingsManager;
import com.appkefu.lib.xmpp.XmppAccountManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import kk.index.R;

public class RegisterActivity extends Activity implements OnClickListener{

	private EditText mUser; // �ʺű༭��
	private EditText mPassword; // ����༭��
	private EditText mRePassword;

	private Button mRegisterBtn;
	private Button mBackBtn;
	
	private KFSettingsManager mSettingsMgr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_register);
		
		mSettingsMgr = KFSettingsManager.getSettingsManager(this);
		
		mUser = (EditText) findViewById(R.id.register_user_edit);
		mPassword = (EditText) findViewById(R.id.register_passwd_edit);
		mRePassword = (EditText) findViewById(R.id.re_register_passwd_edit);
		
		mRegisterBtn = (Button) findViewById(R.id.register_register_btn);
		mRegisterBtn.setOnClickListener(this);
		mBackBtn = (Button) findViewById(R.id.register_reback_btn);
		mBackBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.register_register_btn:
			reigster();
			break;
		case R.id.register_reback_btn:
			finish();
			break;
		default:
			break;
		}
	}
	
	public void reigster() 
	{
		String username = mUser.getText().toString();
		String password = mPassword.getText().toString();
		String repassword = mRePassword.getText().toString();
		
		if ("".equals(username)
				|| "".equals(password)
						|| "".equals(repassword))// �ж� �ʺź�����
		{
			new AlertDialog.Builder(RegisterActivity.this)
					.setIcon(
							getResources().getDrawable(
									R.drawable.login_error_icon))
					.setTitle("ע�����").setMessage("�ʺŻ������벻��Ϊ�գ�\n��������ٵ�¼��")
					.create().show();
		}
		else if(!password.equals(repassword)) 
		{
			new AlertDialog.Builder(RegisterActivity.this)
			.setIcon(getResources().getDrawable(R.drawable.login_error_icon))
			.setTitle("ע�����").setMessage("������������벻һ��!")
			.create().show();
		}
		else
		{		
			mRegisterBtn.setEnabled(false);
			
			registerThread(username, password);
		}  
	}
	
   public void registerThread(final String username, final String password){
    	
    	final Handler handler = new Handler()
    	{
    		public void handleMessage(Message msg) 
			{
				if(msg.what == 1)
				{
					if((Boolean)msg.obj)
					{
						//
						KFMainService.displayToast("ע��ɹ�", null, false);
						
						//�����û���������
						mSettingsMgr.setUsername(username);
						mSettingsMgr.setPassword(password);
					}
					else
					{
						KFMainService.displayToast("ע��ʧ��", null, false);
					}
					
					mRegisterBtn.setEnabled(true);
				}
			}
    	};
    	
    	new Thread() 
		{
			public void run() 
			{
				Message msg = new Message();

				msg.what = 1; 
						 //ע�����û������ע��ɹ����أ�true,���򷵻�: false
				msg.obj = XmppAccountManager.register(username, password); 
								
				handler.sendMessage(msg);
			}
		}.start();
    	
    }


}























