package kk.chat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.appkefu.lib.service.KFSettingsManager;
import com.appkefu.lib.utils.KFFileUtils;
import com.appkefu.lib.utils.KFImageUtils;
import com.appkefu.lib.utils.KFMediaUtils;
import com.appkefu.lib.utils.KFSLog;
import com.appkefu.lib.utils.KFUtils;
import com.appkefu.lib.xmpp.XmppVCard;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import kk.index.R;

public class ProfileActivity extends Activity implements OnClickListener{

	private TextView personal_company_detail;
	private TextView personal_username_detail;
	private TextView personal_job_detail;
	private TextView personal_signature_detail;
	
	private ImageView personal_head_imageview;
	private String 	company, job, signature;
	
	private String theLarge;
	
	private Button mBackBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_profile);
		
		personal_company_detail = (TextView)findViewById(R.id.personal_company_detail);		
		personal_username_detail = (TextView)findViewById(R.id.personal_username_detail);		
		personal_job_detail = (TextView)findViewById(R.id.personal_job_detail);		
		personal_signature_detail = (TextView)findViewById(R.id.personal_signature_detail);
		personal_head_imageview = (ImageView)findViewById(R.id.personal_head_imageview);
		
		mBackBtn = (Button) findViewById(R.id.profile_reback_btn);
		mBackBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {

		case R.id.profile_reback_btn:
			finish();
			break;
		default:
			break;
		}
	}

	public void change_avatar(View v) 
	{
		CharSequence[] items = { "手机相册", "手机拍照" };
		imageChooseItem(items);
	}
	
	public void change_company(View v)
	{
		Intent intent = new Intent(this, ProfileChangeActivity.class);
		intent.putExtra("profileField", "company");
		intent.putExtra("value", company);
		startActivity(intent);
	}
	
	public void change_job(View v)
	{
		Intent intent = new Intent(this, ProfileChangeActivity.class);
		intent.putExtra("profileField", "job");
		intent.putExtra("value", job);
		startActivity(intent);
	}
	
	public void change_signature(View v)
	{
		Intent intent = new Intent(this, ProfileChangeActivity.class);
		intent.putExtra("profileField", "signature");
		intent.putExtra("value", signature);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		KFSLog.d( "onResume");

		company = XmppVCard.getCompany();
		job = XmppVCard.getJob();
		signature = XmppVCard.getSignature();
		
		Bitmap bitmap = KFImageUtils.loadImgThumbnail(XmppVCard.getVCard(), 100, 100);
		if(bitmap != null)
			personal_head_imageview.setImageBitmap(bitmap);
		
		personal_company_detail.setText(company);
		personal_username_detail.setText(KFSettingsManager.getSettingsManager(this).getUsername());
		personal_job_detail.setText(job);
		personal_signature_detail.setText(signature);
		
	}
	
	/**
	 * 操作选择
	 * 
	 * @param items
	 */
	public void imageChooseItem(CharSequence[] items) {
		AlertDialog imageDialog = new AlertDialog.Builder(this)
				.setTitle(R.string.appkefu_set_headview)
				.setIcon(android.R.drawable.btn_star)
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						// 手机选图
						if (item == 0) {
							Intent intent = new Intent(
									Intent.ACTION_GET_CONTENT);
							intent.addCategory(Intent.CATEGORY_OPENABLE);
							intent.setType("image/*");
							startActivityForResult(
									Intent.createChooser(intent, "选择图片"),
									KFImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
						}
						// 拍照
						else if (item == 1) {
							String savePath = "";
							// 判断是否挂载了SD卡
							String storageState = Environment
									.getExternalStorageState();
							if (storageState.equals(Environment.MEDIA_MOUNTED)) {
								savePath = Environment
										.getExternalStorageDirectory()
										.getAbsolutePath()
										+ "/AppKeFu/Camera/";// 存放照片的文件夹
								File savedir = new File(savePath);
								if (!savedir.exists()) {
									savedir.mkdirs();
								}
							}

							// 没有挂载SD卡，无法保存文件
							if (KFUtils.isEmpty(savePath)) {
								Toast.makeText(ProfileActivity.this,
										"无法保存照片，请检查SD卡是否挂载", Toast.LENGTH_SHORT)
										.show();
								return;
							}

							String timeStamp = new SimpleDateFormat(
									"yyyyMMddHHmmss").format(new Date());
							String fileName = "appkefu_" + timeStamp + ".jpg";// 照片命名
							File out = new File(savePath, fileName);
							Uri uri = Uri.fromFile(out);

							theLarge = savePath + fileName;// 该照片的绝对路径

							Intent intent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
							startActivityForResult(intent,
									KFImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
						}
					}
				}).create();

		imageDialog.show();
	}

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {

		if (requestCode == 1) {
			
		} else {
			KFSLog.d("uploadPicture result returned");

			if (resultCode != RESULT_OK)
				return;
			
			if (requestCode == KFImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD) {
				KFSLog.d("pick picture");
				if (data == null)
					return;

				Uri thisUri = data.getData();
				String filePath = KFImageUtils
						.getAbsolutePathFromNoStandardUri(thisUri);

				// 如果是标准Uri
				if (KFUtils.isEmpty(filePath)) {
					theLarge = KFImageUtils.getAbsoluteImagePath(
							ProfileActivity.this, thisUri);
				} else {
					theLarge = filePath;
				}

				String attFormat = KFFileUtils.getFileFormat(theLarge);
				if (!"photo".equals(KFMediaUtils
						.getContentType(attFormat))) {
					Toast.makeText(ProfileActivity.this,
							"图片格式错误",
							Toast.LENGTH_SHORT).show();
					return;
				}
			}
			// 拍摄图片
			else if (requestCode == KFImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA) {
				KFSLog.d("take picture");
			}

			changeAvatarThread();
		}
	}
	
	public void changeAvatarThread()
	{
    	final Handler handler = new Handler() {
    		public void handleMessage(Message msg) 
			{
				if(msg.what == 1)
				{				
					Bitmap bitmap = KFImageUtils.loadImgThumbnail((String)msg.obj, 100, 100);
					if(bitmap != null)
						personal_head_imageview.setImageBitmap(bitmap);
				}
			}
    	};
    	
    	new Thread() 
		{
			public void run() 
			{
				Message msg = new Message();
				
				XmppVCard.setAvatar(theLarge);

				msg.what = 1; 
				msg.obj = theLarge; 
								
				handler.sendMessage(msg);
			}
		}.start();    	
    }
}








