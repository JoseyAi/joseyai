package chuqin.kuangke.kkadapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import chuqin.kuangke.object.KkObject;
import kk.index.R;

public class KkAdapter extends BaseAdapter {

	private List<KkObject> list;
	private LayoutInflater inflater;
	// private String year,month;
	

	public KkAdapter(Context context, List<KkObject> list) {
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

	public void changeList(List<KkObject> list) {
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.chuqin_kuangke_items, null);
			holder.kk_name = (TextView) convertView.findViewById(R.id.kk_name);
			holder.kk_number = (TextView) convertView.findViewById(R.id.kk_number);
			holder.kk_xibie = (TextView) convertView.findViewById(R.id.kk_xibie);
			holder.kk_banji = (TextView) convertView.findViewById(R.id.kk_banji);
			holder.kk_course = (TextView) convertView.findViewById(R.id.kk_course);
			holder.kk_time = (TextView) convertView.findViewById(R.id.kk_time);
			holder.kk_reason = (TextView) convertView.findViewById(R.id.kk_reason);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		KkObject  kkObject = list.get(position);
		holder.kk_name.setText(kkObject.getName());
		holder.kk_number.setText(kkObject.getNumber());
		holder.kk_xibie.setText(kkObject.getXibie());
		holder.kk_banji.setText(kkObject.getBanji());
		holder.kk_course.setText(kkObject.getCourse());
		holder.kk_time.setText(kkObject.getTime());
		holder.kk_reason.setText(kkObject.getReason());
		return convertView;
	}

	public class ViewHolder {
		ImageView icon;
		TextView kk_name,kk_number,kk_xibie,kk_banji,kk_course,kk_time,kk_reason;
	}
}
