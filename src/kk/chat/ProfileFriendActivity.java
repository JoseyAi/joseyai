package kk.chat;

import com.appkefu.lib.utils.KFImageUtils;
import com.appkefu.lib.xmpp.XmppBlacklist;
import com.appkefu.lib.xmpp.XmppBuddies;
import com.appkefu.lib.xmpp.XmppVCard;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import kk.index.R;

public class ProfileFriendActivity extends Activity implements OnClickListener{
	
	private TextView user_profile_title;
	
	private TextView personal_company_detail;
	private TextView personal_job_detail;
	private TextView personal_signature_detail;
	
	private ImageView personal_head_imageview;
	
	private Button add_friends_reback_btn;
	private Button make_it_black;
	private Button make_a_friend;
	
	private String chatName;
	
	private boolean isSubscribed;
	private boolean isBlocked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_profile_friend);
		
		chatName = getIntent().getStringExtra("chatName");
		
		user_profile_title = (TextView)findViewById(R.id.user_profile_title);
		user_profile_title.setText(chatName);
		
		personal_company_detail = (TextView)findViewById(R.id.personal_company_detail);		
		personal_job_detail = (TextView)findViewById(R.id.personal_job_detail);		
		personal_signature_detail = (TextView)findViewById(R.id.personal_signature_detail);
		
		personal_head_imageview = (ImageView)findViewById(R.id.personal_head_imageview);
		
		add_friends_reback_btn = (Button)findViewById(R.id.add_friends_reback_btn);
		add_friends_reback_btn.setOnClickListener(this);
		
		make_it_black = (Button)findViewById(R.id.make_it_black);	
		make_it_black.setOnClickListener(this);
		
		make_a_friend = (Button)findViewById(R.id.make_a_friend);
		make_a_friend.setOnClickListener(this);
		
		isSubscribed = false;
		isBlocked = false;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		personal_company_detail
			.setText(XmppVCard.getOtherField("company", chatName));
		personal_job_detail
			.setText(XmppVCard.getOtherField("job", chatName));
		personal_signature_detail
			.setText(XmppVCard.getOtherField("signature", chatName));
		
		//is_in_black
		isBlocked = XmppBlacklist.isUserExistInPrivacyList(chatName);
		if(isBlocked)
		{
			make_it_black.setText("取消黑名单");
		}
		else
			make_it_black.setText("拉黑");
		
		//is_a_friend
		isSubscribed = XmppBuddies.isFollowdOrFriend(chatName);
		if(isSubscribed)
		{
			make_a_friend.setText("取消好友");
		}
		else
			make_a_friend.setText("添加好友");
		
		Bitmap bitmap = KFImageUtils.loadImgThumbnail(XmppVCard.getOthersVCard(chatName), 100, 100);
		if(bitmap != null)
			personal_head_imageview.setImageBitmap(bitmap);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int viewId = v.getId();
		switch(viewId)
		{
		case R.id.add_friends_reback_btn:
			finish();
			break;
		case R.id.make_it_black:
			make_black();
			break;
		case R.id.make_a_friend:
			add_friend();
			break;
		default:
			break;
		}
	}
	
	public void make_black()
	{
		String blackTitle = "";
		if(isBlocked)
		{
			blackTitle = "确定要解除黑名单?";
		}
		else
		{
			blackTitle = "确认要将其加入黑名单?";
		}
		
		new AlertDialog.Builder(this)
		.setMessage(blackTitle)
		.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {						
						
						if(isBlocked)
						{
							XmppBlacklist.removeUserFromBlackList(chatName);
						}
						else
						{
							XmppBlacklist.addUserToBlackList(chatName);
						}
						
						refresh();
					}
				})
				.setNegativeButton("取消", null).create()
					.show();
	}
	
	public void add_friend()
	{
		String addTitle = "";
		if(isSubscribed)
		{
			addTitle = "确定要解除好友关系?";
		}
		else
		{
			addTitle = "确认要添加其为好友?";
		}
		
		new AlertDialog.Builder(this)
		.setMessage(addTitle)
		.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {						
						
						if(isSubscribed)
						{
							XmppBuddies.removeFriend(chatName);
						}
						else
						{
							XmppBuddies.addFriend(chatName);
						}
						
						refresh();
					}
				})
				.setNegativeButton("取消", null).create()
		.show();
		
		 
	}
	
	public void refresh ()
	{
	    Intent intent = getIntent();
	    intent.putExtra("chatName", chatName);
	    finish();
	    startActivity(intent);
	}
	


}
