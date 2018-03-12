package kk.index;


import android.app.TabActivity; 
import android.content.Intent; 
import android.os.Bundle; 
import android.view.Window; 
import android.widget.CompoundButton; 
import android.widget.RadioButton; 
import android.widget.CompoundButton.OnCheckedChangeListener; 
import android.widget.TabHost;
import kk.more_index.EAActivity;
import kk.more_index.EBActivity;

public class EActivity extends TabActivity implements OnCheckedChangeListener{ 
   
  private TabHost emTabHost; 
  private Intent emAIntent; 
  private Intent emBIntent; 
   
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) { 
    super.onCreate(savedInstanceState); 
    requestWindowFeature(Window.FEATURE_NO_TITLE); 
    setContentView(R.layout.tabs_more); 
     
    this.emAIntent = new Intent(this,EAActivity.class); 
    this.emBIntent = new Intent(this,EBActivity.class); 
     
    ((RadioButton) findViewById(R.id.radio_button_beiwang)) 
    .setOnCheckedChangeListener(this); 
    ((RadioButton) findViewById(R.id.radio_button_other)) 
    .setOnCheckedChangeListener(this); 
     
    setupIntent(); 
  } 
 
  @Override 
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { 
    if(isChecked){ 
      switch (buttonView.getId()) { 
      case R.id.radio_button_beiwang: 
        this.emTabHost.setCurrentTabByTag("A_TAB"); 
        break; 
      case R.id.radio_button_other: 
        this.emTabHost.setCurrentTabByTag("B_TAB"); 
        break; 
      } 
    } 
     
  } 
   
  private void setupIntent() { 
    this.emTabHost = getTabHost(); 
    TabHost localTabHost = this.emTabHost; 
 
    localTabHost.addTab(buildTabSpec("A_TAB", R.string.more_beiwang, 
        R.drawable.Ityellow, this.emAIntent)); 
 
    localTabHost.addTab(buildTabSpec("B_TAB", R.string.more_others, 
        R.drawable.Ityellow, this.emBIntent)); 
 
  } 
   
  private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon, 
      final Intent content) { 
    return this.emTabHost.newTabSpec(tag).setIndicator(getString(resLabel), 
        getResources().getDrawable(resIcon)).setContent(content); 
  } 
} 