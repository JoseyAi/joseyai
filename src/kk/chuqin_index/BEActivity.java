package kk.chuqin_index;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import chuqin.qingjia.db.DBHelper;
import chuqin.qingjia.excel.ExcelUtils;
import chuqin.qingjia.object.QjObject;
import chuqin.qingjia.qjadapter.QjAdapter;
import kk.index.R;

@SuppressLint("SimpleDateFormat")
public class BEActivity extends Activity implements OnClickListener {
	private EditText qjNameEdt;
	private EditText qjNumberEdt;
	private EditText qjXibieEdt;
	private EditText qjBanjiEdt;
	private EditText qjReasonEdt;
	private EditText qjTimeEdt;
	private EditText qjFxtimeEdt;

	private Button export_qingjia, import_qingjia;
	private File file;
	private String[] title = { "日期", "姓名", "学号", "系别", "班级", "请假原因", "请假时间", "返校时间" };
	private String[] saveData;
	private DBHelper qjDbHelper;
	private ArrayList<ArrayList<String>> qingjia2List;
	private ListView qingjia_listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chuqin_qingjia);
		findViewsById();
		qjDbHelper = new DBHelper(this);
		qjDbHelper.open();
		qingjia2List = new ArrayList<ArrayList<String>>();
	}

	private void findViewsById() {
		qjNameEdt = (EditText) findViewById(R.id.qingjia_name_edt);
		qjNumberEdt = (EditText) findViewById(R.id.qingjia_number_edt);
		qjXibieEdt = (EditText) findViewById(R.id.qingjia_xibie_edt);
		qjBanjiEdt = (EditText) findViewById(R.id.qingjia_banji_edt);
		qjReasonEdt = (EditText) findViewById(R.id.qingjia_reason_edt);
		qjTimeEdt = (EditText) findViewById(R.id.qingjia_time_edt);
		qjFxtimeEdt = (EditText) findViewById(R.id.qingjia_fxtime_edt);
		
		qingjia_listview = (ListView) findViewById(R.id.qingjia_listview);
		
		import_qingjia = (Button) findViewById(R.id.import_qingjia);
		export_qingjia = (Button) findViewById(R.id.export_qingjia);
		import_qingjia.setOnClickListener(this);
		export_qingjia.setOnClickListener(this);
		View contentHeader = LayoutInflater.from(this).inflate(
				R.layout.chuqin_listview_header_qingjia, null);
		qingjia_listview.addHeaderView(contentHeader);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.export_qingjia:

			saveData = new String[] {
					new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date()),
					qjNameEdt.getText().toString().trim(),
					qjNumberEdt.getText().toString().trim(),
					qjXibieEdt.getText().toString().trim(),
					qjBanjiEdt.getText().toString().trim(),
					qjReasonEdt.getText().toString().trim(),
					qjTimeEdt.getText().toString().trim(),
					qjFxtimeEdt.getText().toString().trim()};
			if (canSave(saveData)) {
				ContentValues values = new ContentValues();
				values.put("time",
						new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date()));
				values.put("name", qjNameEdt.getText().toString());
				values.put("number", qjNumberEdt.getText().toString());
				values.put("xibie", qjXibieEdt.getText().toString());
				values.put("banji", qjBanjiEdt.getText().toString());
				values.put("qj_reason", qjReasonEdt.getText().toString());
				values.put("qj_time", qjTimeEdt.getText().toString());
				values.put("qj_fxtime", qjFxtimeEdt.getText().toString());

				long insert = qjDbHelper.insert("chuqin_qj", values);
				if (insert > 0) {
					initData();
				}
			} else {
				Toast.makeText(this, "请填写任意一项内容", Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.import_qingjia:
			ArrayList<QjObject> billList = (ArrayList<QjObject>) ExcelUtils
					.read2DB(new File(getSDPath() + "/Chuqin/qingjia/qingjia.xls"), this);
			qingjia_listview.setAdapter(new QjAdapter(this, billList));
			break;
		}
	}

	@SuppressLint("SimpleDateFormat")
	public void initData() {
		file = new File(getSDPath() + "/Chuqin/qingjia");
		makeDir(file);
		ExcelUtils.initExcel(file.toString() + "/qingjia.xls", title);
		ExcelUtils.writeObjListToExcel(getqingjiaData(), getSDPath()
				+ "/Chuqin/qingjia/qingjia.xls", this);
	}

	private ArrayList<ArrayList<String>> getqingjiaData() {
		Cursor qjCrusor = qjDbHelper.exeSql("select * from chuqin_qj");
		while (qjCrusor.moveToNext()) {
			ArrayList<String> beanList = new ArrayList<String>();
			beanList.add(qjCrusor.getString(1));
			beanList.add(qjCrusor.getString(2));
			beanList.add(qjCrusor.getString(3));
			beanList.add(qjCrusor.getString(4));
			beanList.add(qjCrusor.getString(5));
			beanList.add(qjCrusor.getString(6));
			beanList.add(qjCrusor.getString(7));
			beanList.add(qjCrusor.getString(8));

			qingjia2List.add(beanList);
		}
		qjCrusor.close();
		return qingjia2List;
	}

	public static void makeDir(File dir) {
		if (!dir.getParentFile().exists()) {
			makeDir(dir.getParentFile());
		}
		dir.mkdir();
	}

	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();
		}
		String dir = sdDir.toString();
		return dir;

	}

	private boolean canSave(String[] data) {
		boolean isOk = false;
		for (int i = 0; i < data.length; i++) {
			if (i > 0 && i < data.length) {
				if (!TextUtils.isEmpty(data[i])) {
					isOk = true;
				}
			}
		}
		return isOk;
	}
}
