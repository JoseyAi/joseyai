package chuqin.chidao.chdadapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import chuqin.chidao.object.ChdObject;
import kk.index.R;

public class ChdAdapter extends BaseAdapter {

	private List<ChdObject> list;
	private LayoutInflater inflater;
	// private String year,month;
	

	public ChdAdapter(Context context, List<ChdObject> list) {
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

	public void changeList(List<ChdObject> list) {
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.chuqin_chidao_items, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.number = (TextView) convertView.findViewById(R.id.number);
			holder.xibie = (TextView) convertView.findViewById(R.id.xibie);
			holder.banji = (TextView) convertView.findViewById(R.id.banji);
			holder.chd_time = (TextView) convertView.findViewById(R.id.chd_time);
			holder.chd_reason = (TextView) convertView.findViewById(R.id.chd_reason);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ChdObject chdObject = list.get(position);
		holder.name.setText(chdObject.getName());
		holder.number.setText(chdObject.getNumber());
		holder.xibie.setText(chdObject.getXibie());
		holder.banji.setText(chdObject.getBanji());
		holder.chd_time.setText(chdObject.getTime());
		holder.chd_reason.setText(chdObject.getReason());
		return convertView;
	}

	public class ViewHolder {
		ImageView icon;
		TextView name,number,xibie,banji,chd_time,chd_reason;
	}
}
