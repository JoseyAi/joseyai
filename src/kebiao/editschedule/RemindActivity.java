package kebiao.editschedule;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Vibrator;
import kk.index.R;

public class RemindActivity extends Activity
{
	private Vibrator vibrator;
		
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		SharedPreferences pre = getSharedPreferences("time", Context.MODE_MULTI_PROCESS);
		int advance_time = pre.getInt("time_choice", 30);
				
		//��ȡϵͳ��vibrator���񣬲������ֻ���2��
		vibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
		vibrator.vibrate(2000);

		// ����һ���Ի���
		final AlertDialog.Builder builder= new AlertDialog.Builder(RemindActivity.this);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setTitle("��ܰ��ʾ");
			builder.setMessage("ͯЬ������" + advance_time + "���Ӿ�Ҫ�Ͽ���Ŷ��");
			builder.setPositiveButton(
				"ȷ��" ,
				new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog , int which)
					{
						// ������Activity
						RemindActivity.this.finish();
					}
				}
			)
			.show();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// ������Activity
		RemindActivity.this.finish();	
	}
	
	
}