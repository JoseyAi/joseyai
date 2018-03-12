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
import chuqin.zaotui.db.DBHelper;
import chuqin.zaotui.excel.ExcelUtils;
import chuqin.zaotui.object.ZtObject;
import chuqin.zaotui.ztadapter.ZtAdapter;
import kk.index.R;

@SuppressLint("SimpleDateFormat")
public class BCActivity extends Activity implements OnClickListener {
	private EditText ztNameEdt;
	private EditText ztNumberEdt;
	private EditText ztXibieEdt;
	private EditText ztBanjiEdt;
	private EditText ztCourseEdt;
	private EditText ztReasonEdt;
	private EditText ztQuxiangEdt;

	private Button export_zaotui, import_zaotui;
	private File file;
	private String[] title = { "日期", "姓名", "学号", "系别", "班级", "早退课程", "早退原因", "早退去向"  };
	private String[] saveData;
	private DBHelper ztDbHelper;
	private ArrayList<ArrayList<String>> zaotui2List;
	private ListView zaotui_listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chuqin_zaotui);
		findViewsById();
		ztDbHelper = new DBHelper(this);
		ztDbHelper.open();
		zaotui2List = new ArrayList<ArrayList<String>>();
	}

	private void findViewsById() {
		ztNameEdt = (EditText) findViewById(R.id.zaotui_name_edt);
		ztNumberEdt = (EditText) findViewById(R.id.zaotui_number_edt);
		ztXibieEdt = (EditText) findViewById(R.id.zaotui_xibie_edt);
		ztBanjiEdt = (EditText) findViewById(R.id.zaotui_banji_edt);
		ztCourseEdt = (EditText) findViewById(R.id.zaotui_course_edt);
		ztReasonEdt = (EditText) findViewById(R.id.zaotui_reason_edt);
		ztQuxiangEdt = (EditText) findViewById(R.id.zaotui_quxiang_edt);
		
		zaotui_listview = (ListView) findViewById(R.id.zaotui_listview);
		export_zaotui = (Button) findViewById(R.id.export_zaotui);
		import_zaotui = (Button) findViewById(R.id.import_zaotui);
		export_zaotui.setOnClickListener(this);
		import_zaotui.setOnClickListener(this);
		View contentHeader = LayoutInflater.from(this).inflate(
				R.layout.chuqin_listview_header_zaotui, null);
		zaotui_listview.addHeaderView(contentHeader);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.export_zaotui:

			saveData = new String[] {
					new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date()),
					ztNameEdt.getText().toString().trim(),
					ztNumberEdt.getText().toString().trim(),
					ztXibieEdt.getText().toString().trim(),
					ztBanjiEdt.getText().toString().trim(),
					ztCourseEdt.getText().toString().trim(),
					ztReasonEdt.getText().toString().trim(),
					ztQuxiangEdt.getText().toString().trim()};
			if (canSave(saveData)) {
				ContentValues values = new ContentValues();
				values.put("time",
						new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date()));
				values.put("name", ztNameEdt.getText().toString());
				values.put("number", ztNumberEdt.getText().toString());
				values.put("xibie", ztXibieEdt.getText().toString());
				values.put("banji", ztBanjiEdt.getText().toString());
				values.put("zt_course", ztCourseEdt.getText().toString());
				values.put("zt_reason", ztReasonEdt.getText().toString());
				values.put("zt_quxiang", ztQuxiangEdt.getText().toString());

				long insert = ztDbHelper.insert("chuqin_zt", values);
				if (insert > 0) {
					initData();
				}
			} else {
				Toast.makeText(this, "请填写任意一项内容", Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.import_zaotui:
			ArrayList<ZtObject> zaotuiList = (ArrayList<ZtObject>) ExcelUtils
					.read2DB(new File(getSDPath() + "/Chuqin/zaotui/zaotui.xls"), this);
			zaotui_listview.setAdapter(new ZtAdapter(this, zaotuiList));
			break;
		}
	}

	@SuppressLint("SimpleDateFormat")
	public void initData() {
		file = new File(getSDPath() + "/Chuqin/zaotui");
		makeDir(file);
		ExcelUtils.initExcel(file.toString() + "/zaotui.xls", title);
		ExcelUtils.writeObjListToExcel(getZaotuiData(), getSDPath()
				+ "/Chuqin/zaotui/zaotui.xls", this);
	}

	private ArrayList<ArrayList<String>> getZaotuiData() {
		Cursor ztCrusor = ztDbHelper.exeSql("select * from chuqin_zt");
		while (ztCrusor.moveToNext()) {
			ArrayList<String> beanList = new ArrayList<String>();
			beanList.add(ztCrusor.getString(1));
			beanList.add(ztCrusor.getString(2));
			beanList.add(ztCrusor.getString(3));
			beanList.add(ztCrusor.getString(4));
			beanList.add(ztCrusor.getString(5));
			beanList.add(ztCrusor.getString(6));
			beanList.add(ztCrusor.getString(7));
			beanList.add(ztCrusor.getString(8));

			zaotui2List.add(beanList);
		}
		ztCrusor.close();
		return zaotui2List;
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
