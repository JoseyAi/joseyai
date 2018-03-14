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

		//��� �û���/���� �Ƿ��Ѿ�����,����Ѿ����ã����¼
		if(!"".equals(mSettingsMgr.getUsername()) 
				&& !"".equals(mSettingsMgr.getPassword()))
			KFTools.startSvcIntent(this, KFMainService.ACTION_CONNECT);//��¼�ӿ�
		
		//��ʼ��
		initView();
	}
	
	/*
	 * ��Դ��ʼ��
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
		case R.id.chat_button://��admin�Ự,ʵ��Ӧ������Ҫ��admin�滻�ԶԷ����û���
			intent = new Intent(this, KFChatActivity.class);
			intent.putExtra("username", "admin");			
			startActivity(intent);
			
			//�Զ��壺�����ı���Ϣ�ӿ�
			//KFTools.send(String msg, String to, String type, Context ctx)
			//���У�msg: ������Ϣ���ݣ�to: �Է��û�����type:һ��һ����̶�Ϊ"chat",�����޸�, ctx:Context
			//���磺���û�admin�����ı���Ϣ��������Ϣ���ݡ�Ϊ��
			//		KFTools.send("������Ϣ����", "admin", "chat", this);
			
			
			break;	
		case R.id.add_friend_button://��Ӻ���
			intent = new Intent(this, AddFriendActivity.class);
			startActivity(intent);
			break;
		case R.id.roster_list_button://�����б�
			intent = new Intent(this, RosterListActivity.class);
			startActivity(intent);
			break;
		case R.id.history_list_button://��ʷ�����¼
			intent = new Intent(this, HistoryActivity.class);
			startActivity(intent);
			break;
		case R.id.profile_button://����������Ϣ
			intent = new Intent(this, ProfileActivity.class);
			startActivity(intent);
			break;
		case R.id.friend_profile_button:
			//���ˣ�"admin"������������Ϣ��ʵ�ʲ���ʱ��Ҫ��admin�滻Ϊ�Է���ʵ���û���
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
		//�����������ӱ仯���
        intentFilter.addAction(KFMainService.ACTION_XMPP_CONNECTION_CHANGED);
        //������Ϣ
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
    
    //���ݼ����������ӱ仯������½�����ʾ
    private void updateStatus(int status) {
		//Log.d(TAG, "status:"+status);
        switch (status) {
            case KFXmppManager.CONNECTED:
            	KFSLog.d("connected");
            	mTitle.setText("΢�ͷ�Demo("+mSettingsMgr.getUsername()+")");
            	mLoginBtn.setText("��¼("+mSettingsMgr.getUsername()+"�ѵ�¼)");
                break;
            case KFXmppManager.DISCONNECTED:
            	KFSLog.d("disconnected");
            	mTitle.setText("΢�ͷ�Demo(δ����)");
            	mLoginBtn.setText("��¼(δ��¼)");
                break;
            case KFXmppManager.CONNECTING:
            	KFSLog.d("connecting");
            	mTitle.setText("΢�ͷ�Demo(��¼��...)");
            	mLoginBtn.setText("��¼(δ��¼)");
            	break;
            case KFXmppManager.DISCONNECTING:
            	KFSLog.d("connecting");
            	mTitle.setText("΢�ͷ�Demo(�ǳ���...)");
            	mLoginBtn.setText("��¼(δ��¼)");
                break;
            case KFXmppManager.WAITING_TO_CONNECT:
            case KFXmppManager.WAITING_FOR_NETWORK:
            	KFSLog.d("waiting to connect");
            	mTitle.setText("΢�ͷ�Demo(��¼��)");
            	mLoginBtn.setText("��¼(δ��¼)");
                break;
            default:
                throw new IllegalStateException();
        }
    }

    //�˳���¼
	private void logout() {
		new AlertDialog.Builder(this)
		.setMessage("ȷ���˳���¼?")
		.setPositiveButton("ȷ��",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int which) 
					{			
						//�˳���¼
						KFTools.startSvcIntent(CActivity.this, KFMainService.ACTION_DISCONNECT);
					}
				}).setNegativeButton("ȡ��", null).create()
		.show();
	}

}
