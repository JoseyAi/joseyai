package kk.index;

import org.jivesoftware.smack.util.StringUtils;

import com.appkefu.lib.service.KFMainService;
import com.appkefu.lib.service.KFSettingsManager;
import com.appkefu.lib.service.KFXmppManager;
//import com.appkefu.lib.service.KFMainService.LocalBinder;
import com.appkefu.lib.ui.activity.KFChatActivity;
import com.appkefu.lib.utils.KFSLog;
import com.appkefu.lib.utils.KFTools;

import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import kk.chat.AddFriendActivity;
import kk.chat.GroupChatActivity;
import kk.chat.HistoryActivity;
import kk.chat.LoginActivity;
import kk.chat.ProfileActivity;
import kk.chat.ProfileFriendActivity;
import kk.chat.RegisterActivity;
import kk.chat.RosterListActivity;
import kk.index.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;

public class CActivity extends Activity implements OnClickListener{
	
	private KFSettingsManager mSettingsMgr;
	private KFMainService mMainService;
	
	private TextView mTitle;
	
	private Button mRegisterBtn;
	private Button mLoginBtn;
	private Button mChatBtn;
	private Button mAddFriendBtn;
	private Button mShowRosterBtn;
	private Button mHistoryBtn;
	private Button mProfileBtn;
	private Button mProfileFriendBtn;
	private Button mGroupChatBtn;
	private Button mLogoutBtn;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_main);
		
		mSettingsMgr = KFSettingsManager.getSettingsManager(this);

		//检查 用户名/密码 是否已经设置,如果已经设置，则登录
		if(!"".equals(mSettingsMgr.getUsername()) 
				&& !"".equals(mSettingsMgr.getPassword()))
			KFTools.startSvcIntent(this, KFMainService.ACTION_CONNECT);//登录接口
		
		//初始化
		initView();
	}
	
	/*
	 * 资源初始化
	 */
	private void initView() {
		
		mTitle = (TextView) findViewById(R.id.demo_title);
		
		
		mChatBtn = (Button) findViewById(R.id.chat_button);
		mChatBtn.setOnClickListener(this);
		
		mAddFriendBtn = (Button) findViewById(R.id.add_friend_button);
		mAddFriendBtn.setOnClickListener(this);
		
		mShowRosterBtn = (Button) findViewById(R.id.roster_list_button);
		mShowRosterBtn.setOnClickListener(this);
		
		mHistoryBtn = (Button) findViewById(R.id.history_list_button);
		mHistoryBtn.setOnClickListener(this);
		
		mProfileBtn = (Button) findViewById(R.id.profile_button);
		mProfileBtn.setOnClickListener(this);
		
		mProfileFriendBtn = (Button) findViewById(R.id.friend_profile_button);
		mProfileFriendBtn.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Intent intent;
		switch(view.getId()) {
		case R.id.chat_button://与admin会话,实际应用中需要将admin替换对对方的用户名
			intent = new Intent(this, KFChatActivity.class);
			intent.putExtra("username", "admin");			
			startActivity(intent);
			
			//自定义：发送文本消息接口
			//KFTools.send(String msg, String to, String type, Context ctx)
			//其中：msg: 聊天消息内容，to: 对方用户名，type:一对一聊天固定为"chat",不能修改, ctx:Context
			//例如：给用户admin发送文本消息“聊天消息内容”为：
			//		KFTools.send("聊天消息内容", "admin", "chat", this);
			
			
			break;	
		case R.id.add_friend_button://添加好友
			intent = new Intent(this, AddFriendActivity.class);
			startActivity(intent);
			break;
		case R.id.roster_list_button://好友列表
			intent = new Intent(this, RosterListActivity.class);
			startActivity(intent);
			break;
		case R.id.history_list_button://历史聊天记录
			intent = new Intent(this, HistoryActivity.class);
			startActivity(intent);
			break;
		case R.id.profile_button://个人资料信息
			intent = new Intent(this, ProfileActivity.class);
			startActivity(intent);
			break;
		case R.id.friend_profile_button:
			//他人（"admin"）个人资料信息，实际操作时需要将admin替换为对方真实的用户名
			intent = new Intent(this, ProfileFriendActivity.class);
			intent.putExtra("chatName", "admin");
			startActivity(intent);
			break;
		default:			
			break;		
		}
		
	}

	@Override
	protected void onStart() {
		super.onStart();
		KFSLog.d("onStart");
		
		IntentFilter intentFilter = new IntentFilter();
		//监听网络连接变化情况
        intentFilter.addAction(KFMainService.ACTION_XMPP_CONNECTION_CHANGED);
        //监听消息
        intentFilter.addAction(KFMainService.ACTION_XMPP_MESSAGE_RECEIVED);
        registerReceiver(mXmppreceiver, intentFilter);        
        
        Intent intent = new Intent(KFMainService.ACTION_CONNECT);
        bindService(intent, mMainServiceConnection, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		
	}
	
	@Override
	protected void onStop() {
		super.onStop();

		KFSLog.d("onStop");
		unbindService(mMainServiceConnection);
        unregisterReceiver(mXmppreceiver);
	}
	
	private BroadcastReceiver mXmppreceiver = new BroadcastReceiver() 
	{
        public void onReceive(Context context, Intent intent) 
        {
            String action = intent.getAction();
            if (action.equals(KFMainService.ACTION_XMPP_CONNECTION_CHANGED)) 
            {
                updateStatus(intent.getIntExtra("new_state", 0));        
            }
            else if(action.equals(KFMainService.ACTION_XMPP_MESSAGE_RECEIVED))
            {
            	String body = intent.getStringExtra("body");
            	String from = StringUtils.parseName(intent.getStringExtra("from"));
            	
            	KFSLog.d("body:"+body+" from:"+from);
            }
        }
    };
     
    private ServiceConnection mMainServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
        	KFSLog.d("MainActivity: MainService connected");
        	com.appkefu.lib.service.KFMainService$LocalBinder binder = 
        			(com.appkefu.lib.service.KFMainService$LocalBinder) service;
        	//LocalBinder binder = (LocalBinder)service;
        	KFMainService mainService = binder.getService();
            mMainService = mainService;
            updateStatus(mMainService.getConnectionStatus());
        }

        public void onServiceDisconnected(ComponentName className) {
        	KFSLog.d("mainActivity: MainService disconnected");
            mMainService = null;
        }
    };
    
    //根据监听到的连接变化情况更新界面显示
    private void updateStatus(int status) {
		//Log.d(TAG, "status:"+status);
        switch (status) {
            case KFXmppManager.CONNECTED:
            	KFSLog.d("connected");
            	mTitle.setText("微客服Demo("+mSettingsMgr.getUsername()+")");
            	mLoginBtn.setText("登录("+mSettingsMgr.getUsername()+"已登录)");
                break;
            case KFXmppManager.DISCONNECTED:
            	KFSLog.d("disconnected");
            	mTitle.setText("微客服Demo(未连接)");
            	mLoginBtn.setText("登录(未登录)");
                break;
            case KFXmppManager.CONNECTING:
            	KFSLog.d("connecting");
            	mTitle.setText("微客服Demo(登录中...)");
            	mLoginBtn.setText("登录(未登录)");
            	break;
            case KFXmppManager.DISCONNECTING:
            	KFSLog.d("connecting");
            	mTitle.setText("微客服Demo(登出中...)");
            	mLoginBtn.setText("登录(未登录)");
                break;
            case KFXmppManager.WAITING_TO_CONNECT:
            case KFXmppManager.WAITING_FOR_NETWORK:
            	KFSLog.d("waiting to connect");
            	mTitle.setText("微客服Demo(登录中)");
            	mLoginBtn.setText("登录(未登录)");
                break;
            default:
                throw new IllegalStateException();
        }
    }

    //退出登录
	private void logout() {
		new AlertDialog.Builder(this)
		.setMessage("确认退出登录?")
		.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int which) 
					{			
						//退出登录
						KFTools.startSvcIntent(CActivity.this, KFMainService.ACTION_DISCONNECT);
					}
				}).setNegativeButton("取消", null).create()
		.show();
	}

}
