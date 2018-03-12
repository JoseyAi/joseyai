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
import chuqin.kuangke.db.DBHelper;
import chuqin.kuangke.excel.ExcelUtils;
import chuqin.kuangke.kkadapter.KkAdapter;
import chuqin.kuangke.object.KkObject;
import kk.index.R;

@SuppressLint("SimpleDateFormat")
public class BDActivity extends Activity implements OnClickListener {
	private EditText kkNameEdt;
	private EditText kkNumberEdt;
	private EditText kkXibieEdt;
	private EditText kkBanjiEdt;
	private EditText kkCourseEdt;
	private EditText kkTimeEdt;
	private EditText kkReasonEdt;

	private Button export_kuangke, import_kuangke;
	private File file;
	private String[] title = { "日期", "姓名", "学号", "系别", "班级", "课程", "旷课时间", "旷课原因" };
	private String[] saveData;
	private DBHelper kkDbHelper;
	private ArrayList<ArrayList<String>> kuangke2List;
	private ListView kuangke_listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chuqin_kuangke);
		findViewsById();
		kkDbHelper = new DBHelper(this);
		kkDbHelper.open();
		kuangke2List = new ArrayList<ArrayList<String>>();
	}

	private void findViewsById() {
		kkNameEdt = (EditText) findViewById(R.id.kuangke_name_edt);
		kkNumberEdt = (EditText) findViewById(R.id.kuangke_number_edt);
		kkXibieEdt = (EditText) findViewById(R.id.kuangke_xibie_edt);
		kkBanjiEdt = (EditText) findViewById(R.id.kuangke_banji_edt);
		kkCourseEdt = (EditText) findViewById(R.id.kuangke_course_edt);
		kkTimeEdt = (EditText) findViewById(R.id.kuangke_time_edt);
		kkReasonEdt = (EditText) findViewById(R.id.kuangke_reason_edt);
		
		kuangke_listview = (ListView) findViewById(R.id.kuangke_listview);
		import_kuangke = (Button) findViewById(R.id.import_kuangke);
		export_kuangke = (Button) findViewById(R.id.export_kuangke);
		import_kuangke.setOnClickListener(this);
		export_kuangke.setOnClickListener(this);
		View contentHeader = LayoutInflater.from(this).inflate(
				R.layout.chuqin_listview_header_kuangke, null);
		kuangke_listview.addHeaderView(contentHeader);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.export_kuangke:

			saveData = new String[] {
					new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date()),
					kkNameEdt.getText().toString().trim(),
					kkNumberEdt.getText().toString().trim(),
					kkXibieEdt.getText().toString().trim(),
					kkBanjiEdt.getText().toString().trim(),
					kkCourseEdt.getText().toString().trim(),
					kkTimeEdt.getText().toString().trim(),
					kkReasonEdt.getText().toString().trim()
			};
			if (canSave(saveData)) {
				ContentValues values = new ContentValues();
				values.put("time",
						new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date()));
				values.put("name", kkNameEdt.getText().toString());
				values.put("number", kkNumberEdt.getText().toString());
				values.put("xibie", kkXibieEdt.getText().toString());
				values.put("banji", kkBanjiEdt.getText().toString());
				values.put("kk_course", kkCourseEdt.getText().toString());
				values.put("kk_time", kkTimeEdt.getText().toString());
				values.put("kk_reason", kkReasonEdt.getText().toString());

				long insert = kkDbHelper.insert("chuqin_kk", values);
				if (insert > 0) {
					initData();
				}
			} else {
				Toast.makeText(this, "请填写任意一项内容", Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.import_kuangke:
			ArrayList<KkObject> billList = (ArrayList<KkObject>) ExcelUtils
					.read2DB(new File(getSDPath() + "/Chuqin/kuangke/kuangke.xls"), this);
			kuangke_listview.setAdapter(new KkAdapter(this, billList));
			break;
		}
	}

	@SuppressLint("SimpleDateFormat")
	public void initData() {
		file = new File(getSDPath() + "/Chuqin/kuangke");
		makeDir(file);
		ExcelUtils.initExcel(file.toString() + "/kuangke.xls", title);
		ExcelUtils.writeObjListToExcel(getkuangkeData(), getSDPath()
				+ "/Chuqin/kuangke/kuangke.xls", this);
	}

	private ArrayList<ArrayList<String>> getkuangkeData() {
		Cursor kkCrusor = kkDbHelper.exeSql("select * from chuqin_kk");
		while (kkCrusor.moveToNext()) {
			ArrayList<String> beanList = new ArrayList<String>();
			beanList.add(kkCrusor.getString(1));
			beanList.add(kkCrusor.getString(2));
			beanList.add(kkCrusor.getString(3));
			beanList.add(kkCrusor.getString(4));
			beanList.add(kkCrusor.getString(5));
			beanList.add(kkCrusor.getString(6));
			beanList.add(kkCrusor.getString(7));
			beanList.add(kkCrusor.getString(8));

			kuangke2List.add(beanList);
		}
		kkCrusor.close();
		return kuangke2List;
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
