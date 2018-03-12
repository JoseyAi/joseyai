package chuqin.qingjia.qjadapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import chuqin.qingjia.object.QjObject;
import kk.index.R;

public class QjAdapter extends BaseAdapter {

	private List<QjObject> list;
	private LayoutInflater inflater;
	// private String year,month;
	

	public QjAdapter(Context context, List<QjObject> list) {
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

	public void changeList(List<QjObject> list) {
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.chuqin_qingjia_items, null);
			holder.qj_name = (TextView) convertView.findViewById(R.id.qj_name);
			holder.qj_number = (TextView) convertView.findViewById(R.id.qj_number);
			holder.qj_xibie = (TextView) convertView.findViewById(R.id.qj_xibie);
			holder.qj_banji = (TextView) convertView.findViewById(R.id.qj_banji);
			holder.qj_reason = (TextView) convertView.findViewById(R.id.qj_reason);
			holder.qj_time = (TextView) convertView.findViewById(R.id.qj_time);
			holder.qj_fxtime = (TextView) convertView.findViewById(R.id.qj_fxtime);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		QjObject  qjObject = list.get(position);
		holder.qj_name.setText(qjObject.getName());
		holder.qj_number.setText(qjObject.getNumber());
		holder.qj_xibie.setText(qjObject.getXibie());
		holder.qj_banji.setText(qjObject.getBanji());
		holder.qj_reason.setText(qjObject.getReason());
		holder.qj_time.setText(qjObject.getTime());
		holder.qj_fxtime.setText(qjObject.getFxtime());
		return convertView;
	}

	public class ViewHolder {
		ImageView icon;
		TextView qj_name,qj_number,qj_xibie,qj_banji,qj_reason,qj_time,qj_fxtime;
	}
}
