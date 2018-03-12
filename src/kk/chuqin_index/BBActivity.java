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
import chuqin.chidao.chdadapter.ChdAdapter;
import chuqin.chidao.db.DBHelper;
import chuqin.chidao.excel.ExcelUtils;
import chuqin.chidao.object.ChdObject;
import kk.index.R;

@SuppressLint("SimpleDateFormat")
public class BBActivity extends Activity implements OnClickListener {
	private EditText chdNameEdt;
	private EditText chdNumberEdt;
	private EditText chdXibieEdt;
	private EditText chdBanjiEdt;
	private EditText chdTimeEdt;
	private EditText chdReasonEdt;

	private Button export_chidao, import_chidao;
	private File file;
	private String[] title = { "日期", "姓名", "学号", "系别", "班级" , "时间", "原因"};
	private String[] saveData;
	private DBHelper chdDbHelper;
	private ArrayList<ArrayList<String>> chidao2List;
	private ListView chidao_listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chuqin_chidao);
		findViewsById();
		chdDbHelper = new DBHelper(this);
		chdDbHelper.open();
		chidao2List = new ArrayList<ArrayList<String>>();
	}

	private void findViewsById() {
		chdNameEdt = (EditText) findViewById(R.id.chidao_name_edt);
		chdNumberEdt = (EditText) findViewById(R.id.chidao_number_edt);
		chdXibieEdt = (EditText) findViewById(R.id.chidao_xibie_edt);
		chdBanjiEdt = (EditText) findViewById(R.id.chidao_banji_edt);
		chdTimeEdt = (EditText) findViewById(R.id.chidao_time_edt);
		chdReasonEdt = (EditText) findViewById(R.id.chidao_reason_edt);
		chidao_listview = (ListView) findViewById(R.id.chidao_listview);
		import_chidao = (Button) findViewById(R.id.import_chidao);
		export_chidao = (Button) findViewById(R.id.export_chidao);
		import_chidao.setOnClickListener(this);
		export_chidao.setOnClickListener(this);
		View contentHeader = LayoutInflater.from(this).inflate(
				R.layout.chuqin_listview_header_chidao, null);
		chidao_listview.addHeaderView(contentHeader);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.export_chidao:

			saveData = new String[] {
					new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date()),
					chdNameEdt.getText().toString().trim(),
					chdNumberEdt.getText().toString().trim(),
					chdXibieEdt.getText().toString().trim(),
					chdBanjiEdt.getText().toString().trim(),
					chdTimeEdt.getText().toString().trim(),
					chdReasonEdt.getText().toString().trim()};
			if (canSave(saveData)) {
				ContentValues values = new ContentValues();
				values.put("time", new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date()));
				values.put("name", chdNameEdt.getText().toString());
				values.put("number", chdNumberEdt.getText().toString());
				values.put("xibie", chdXibieEdt.getText().toString());
				values.put("banji", chdBanjiEdt.getText().toString());
				values.put("chd_time", chdTimeEdt.getText().toString());
				values.put("chd_reason", chdReasonEdt.getText().toString());

				long insert = chdDbHelper.insert("chuqin_chd", values);
				if (insert > 0) {
					initData();
				}
			} else {
				Toast.makeText(this, "请填写任意一项内容", Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.import_chidao:
			ArrayList<ChdObject> billList = (ArrayList<ChdObject>) ExcelUtils
					.read2DB(new File(getSDPath() + "/Chuqin/chidao/chidao.xls"), this);
			chidao_listview.setAdapter(new ChdAdapter(this, billList));
			break;
		}
	}

	@SuppressLint("SimpleDateFormat")
	public void initData() {
		file = new File(getSDPath() + "/Chuqin/chidao");
		makeDir(file);
		ExcelUtils.initExcel(file.toString() + "/chidao.xls", title);
		ExcelUtils.writeObjListToExcel(getChidaoData(), getSDPath()
				+ "/Chuqin/chidao/chidao.xls", this);
	}

	private ArrayList<ArrayList<String>> getChidaoData() {
		Cursor chdCrusor = chdDbHelper.exeSql("select * from chuqin_chd");
		while (chdCrusor.moveToNext()) {
			ArrayList<String> beanList = new ArrayList<String>();
			beanList.add(chdCrusor.getString(1));
			beanList.add(chdCrusor.getString(2));
			beanList.add(chdCrusor.getString(3));
			beanList.add(chdCrusor.getString(4));
			beanList.add(chdCrusor.getString(5));
			beanList.add(chdCrusor.getString(6));
			beanList.add(chdCrusor.getString(7));

			chidao2List.add(beanList);
		}
		chdCrusor.close();
		return chidao2List;
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
