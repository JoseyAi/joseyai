package kk.chat.adapter;


import java.util.List;

import org.jivesoftware.smack.util.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.SectionIndexer;
import android.widget.TextView;

import kk.index.R;
import com.appkefu.lib.ui.activity.KFChatActivity;
import com.appkefu.lib.ui.entity.KFRosterEntity;


public class RosterListViewAdapter extends BaseAdapter {
	
	private static final String TAG = RosterListViewAdapter.class.getSimpleName();	
	
	private Context context;
	private List<KFRosterEntity> list;
	private LayoutInflater inflater;
		
	public RosterListViewAdapter(Context context, List<KFRosterEntity> list){
		this.context = context;
		this.list = list;
		this.inflater = LayoutInflater.from(context);
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
	
	public void remove(KFRosterEntity entity){
		list.remove(entity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.chat_address_item, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.recent_userhead);
			holder.name = (TextView) convertView.findViewById(R.id.recent_name);
			holder.relation = (TextView) convertView.findViewById(R.id.relation_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final KFRosterEntity entity = list.get(position);
		holder.icon.setImageResource(R.drawable.app_panel_friendcard_icon);
		/*
		if(entity.getNick().equals("admin") || entity.getNick().equals(""))
		{
			holder.name.setText("微客服");
		}		
		else
			holder.name.setText(entity.getNick());
		*/
		
		holder.name.setText(StringUtils.parseName(entity.getJid()));
		
		holder.name.setTextColor(Color.BLACK);
		/*
		if(entity.getType().equals("from"))
		{
			holder.relation.setText("粉丝");
		}
		if(entity.getType().equals("to"))
		{
			holder.relation.setText("关注");
		}
		if(entity.getType().equals("both"))
		{
			holder.relation.setText("好友");
		}
		if(entity.getType().equals("none"))
		{
			holder.relation.setText("请求已发送");
		}*/

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String jid = entity.getJid();
				Log.d(TAG, "chatWith:"+jid);	
				
				Intent intent = new Intent(context, KFChatActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("username", StringUtils.parseName(jid));
				context.startActivity(intent);
				
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
		public TextView relation;
	}
	

}
















