package chuqin.zaotui.ztadapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import chuqin.zaotui.object.ZtObject;
import kk.index.R;

public class ZtAdapter extends BaseAdapter {

	private List<ZtObject> list;
	private LayoutInflater inflater;
	// private String year,month;
	

	public ZtAdapter(Context context, List<ZtObject> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
		
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

	public void changeList(List<ZtObject> list) {
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.chuqin_zaotui_items, null);
			holder.zt_name = (TextView) convertView.findViewById(R.id.zt_name);
			holder.zt_number = (TextView) convertView.findViewById(R.id.zt_number);
			holder.zt_xibie = (TextView) convertView.findViewById(R.id.zt_xibie);
			holder.zt_banji = (TextView) convertView.findViewById(R.id.zt_banji);
			holder.zt_course = (TextView) convertView.findViewById(R.id.zt_course);
			holder.zt_reason = (TextView) convertView.findViewById(R.id.zt_reason);
			holder.zt_quxiang = (TextView) convertView.findViewById(R.id.zt_quxiang);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ZtObject ztObject = list.get(position);
		holder.zt_name.setText(ztObject.getName());
		holder.zt_number.setText(ztObject.getNumber());
		holder.zt_xibie.setText(ztObject.getXibie());
		holder.zt_banji.setText(ztObject.getBanji());
		holder.zt_course.setText(ztObject.getCourse());
		holder.zt_reason.setText(ztObject.getReason());
		holder.zt_quxiang.setText(ztObject.getQuxiang());
		return convertView;
	}

	public class ViewHolder {
		ImageView icon;
		TextView zt_name,zt_number,zt_xibie,zt_banji,zt_course,zt_reason,zt_quxiang;
	}
}
