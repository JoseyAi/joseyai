package kk.chat.adapter;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.util.StringUtils;

import kk.index.R;
import com.appkefu.lib.ui.activity.KFChatActivity;
import com.appkefu.lib.ui.activity.KFSubscribeNotificationActivity;
import com.appkefu.lib.ui.entity.KFConversationEntity;
import com.appkefu.lib.utils.KFImageUtils;
import com.appkefu.lib.utils.KFSLog;
import com.appkefu.lib.xmpp.XmppVCard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ConversationAdapter extends BaseAdapter {

	private static final String TAG = ConversationAdapter.class.getSimpleName();
	
	private Context context;
	private List<KFConversationEntity> list;
	//private BeemApplication application;
	private LayoutInflater inflater;
	//private int[] imgs = { R.drawable.icon, R.drawable.f1, R.drawable.f2,
	//		R.drawable.f3, R.drawable.f4, R.drawable.f5, R.drawable.f6,
	//		R.drawable.f7, R.drawable.f8, R.drawable.f9 };

	public ConversationAdapter(Context context, List<KFConversationEntity> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		//application = (BeemApplication) context.getApplicationContext();
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	// 通过对象移除
	public void remove(KFConversationEntity entity) {
		list.remove(entity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.chat_history_list_item, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.recent_userhead);
			holder.name = (TextView) convertView.findViewById(R.id.recent_name);
			holder.date = (TextView) convertView.findViewById(R.id.recent_time);
			holder.msg = (TextView) convertView.findViewById(R.id.recent_msg);
			holder.count = (TextView) convertView.findViewById(R.id.recent_new_num);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final KFConversationEntity entity = list.get(position);

		KFSLog.d("conv.id "+entity.getId()+ " conv.name:"+entity.getName());

		if(entity.getName().equals("admin"))
		{
			holder.name.setText("微客服");
		}
		else if(entity.getName().equals(""))
		{
			holder.name.setText("系统通知");
		}
		else if(entity.getName().equals("appkefu_subscribe_notification_message"))
		{
			holder.name.setText("验证消息");
		}
		else 
			holder.name.setText(entity.getName());
		
		if(entity.getType().equals("chat") && !entity.getName().equals(""))
		{ 
			updateVCardThread(entity.getName(), holder.icon);
		}
		else 
		{
			holder.icon.setImageResource(R.drawable.app_panel_friendcard_icon);
		}
		
		holder.name.setTextColor(Color.BLACK);
		holder.date.setText(entity.getTime());
		holder.msg.setText(entity.getMsg());
		
		if (entity.getCount() > 0) {
			holder.count.setText(entity.getCount() + "");
			holder.count.setTextColor(Color.BLACK);
		} else {
			holder.count.setVisibility(View.INVISIBLE);
		}

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//if(entity.getName().equals("appkefu_subscribe_notification_message"))
				if(entity.getType().equals("subscribe"))
				{
					Intent intent = new Intent(context, KFSubscribeNotificationActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}								
				else//type == chat
				{
					String jid = entity.getName() + "@appkefu.com";
					Log.d(TAG, "chatWith:"+jid);				
					
					Intent intent = new Intent(context, KFChatActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("username", entity.getName());
					context.startActivity(intent);
				}	
			}
		});
		
		
		convertView.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				Log.d(TAG, "setOnCreateContextMenuListener");								
			}
		});
		
		return convertView;
	}

	static class ViewHolder {
		public ImageView icon;
		public TextView name;
		public TextView date;
		public TextView msg;
		public TextView count;
	}
	
	private void updateVCardThread(final String username, final ImageView avatar) {
		
		final Handler handler = new Handler() 
		{
			public void handleMessage(Message msg) 
			{
				if(msg.what == 1)
				{
					String avatarPath = (String)msg.obj;
					Bitmap bitmap = KFImageUtils.getBitmapByPath(avatarPath);
					if(bitmap != null)
						avatar.setImageBitmap(bitmap);
					else
						avatar.setImageResource(R.drawable.app_panel_friendcard_icon);
				}
				
			}
		};
		
		new Thread() 
		{
			public void run() 
			{
				Message msg = new Message();			
				
				String avatarPath = XmppVCard.getInstance(context).getOthersVCard(username);
		
				msg.what = 1; 
				msg.obj = avatarPath; 
								
				handler.sendMessage(msg);
			}
		}.start();
		
	}

}


















