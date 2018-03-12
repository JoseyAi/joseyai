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

	private EditText mUser; // 帐号编辑框
	private EditText mPassword; // 密码编辑框
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
						|| "".equals(repassword))// 判断 帐号和密码
		{
			new AlertDialog.Builder(RegisterActivity.this)
					.setIcon(
							getResources().getDrawable(
									R.drawable.login_error_icon))
					.setTitle("注册错误").setMessage("帐号或者密码不能为空，\n请输入后再登录！")
					.create().show();
		}
		else if(!password.equals(repassword)) 
		{
			new AlertDialog.Builder(RegisterActivity.this)
			.setIcon(getResources().getDrawable(R.drawable.login_error_icon))
			.setTitle("注册错误").setMessage("两次输入的密码不一致!")
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
						KFMainService.displayToast("注册成功", null, false);
						
						//保存用户名、密码
						mSettingsMgr.setUsername(username);
						mSettingsMgr.setPassword(password);
					}
					else
					{
						KFMainService.displayToast("注册失败", null, false);
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
						 //注册新用户，如果注册成功返回：true,否则返回: false
				msg.obj = XmppAccountManager.register(username, password); 
								
				handler.sendMessage(msg);
			}
		}.start();
    	
    }


}























