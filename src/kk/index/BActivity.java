package kk.index;


import android.app.TabActivity; 
import android.content.Intent; 
import android.os.Bundle; 
import android.view.Window; 
import android.widget.CompoundButton; 
import android.widget.RadioButton; 
import android.widget.CompoundButton.OnCheckedChangeListener; 
import android.widget.TabHost;
import kk.chuqin_index.BAActivity;
import kk.chuqin_index.BBActivity;
import kk.chuqin_index.BCActivity;
import kk.chuqin_index.BDActivity;
import kk.chuqin_index.BEActivity; 
 
public class BActivity extends TabActivity implements OnCheckedChangeListener{ 
   
  private TabHost bmTabHost; 
  private Intent bmAIntent; 
  private Intent bmBIntent; 
  private Intent bmCIntent; 
  private Intent bmDIntent; 
  private Intent bmEIntent; 
   
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) { 
    super.onCreate(savedInstanceState); 
    requestWindowFeature(Window.FEATURE_NO_TITLE); 
    setContentView(R.layout.tabs_chuqin); 
     
    this.bmAIntent = new Intent(this,BAActivity.class); 
    this.bmBIntent = new Intent(this,BBActivity.class); 
    this.bmCIntent = new Intent(this,BCActivity.class); 
    this.bmDIntent = new Intent(this,BDActivity.class); 
    this.bmEIntent = new Intent(this,BEActivity.class); 
     
    ((RadioButton) findViewById(R.id.radio_button5)) 
    .setOnCheckedChangeListener(this); 
    ((RadioButton) findViewById(R.id.radio_button6)) 
    .setOnCheckedChangeListener(this); 
    ((RadioButton) findViewById(R.id.radio_button7)) 
    .setOnCheckedChangeListener(this); 
    ((RadioButton) findViewById(R.id.radio_button8)) 
    .setOnCheckedChangeListener(this); 
    ((RadioButton) findViewById(R.id.radio_button9)) 
    .setOnCheckedChangeListener(this); 
     
    setupIntent(); 
  } 
 
  @Override 
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { 
    if(isChecked){ 
      switch (buttonView.getId()) { 
      case R.id.radio_button5: 
        this.bmTabHost.setCurrentTabByTag("A_TAB"); 
        break; 
      case R.id.radio_button6: 
        this.bmTabHost.setCurrentTabByTag("B_TAB"); 
        break; 
      case R.id.radio_button7: 
        this.bmTabHost.setCurrentTabByTag("C_TAB"); 
        break; 
      case R.id.radio_button8: 
        this.bmTabHost.setCurrentTabByTag("D_TAB"); 
        break; 
      case R.id.radio_button9: 
        this.bmTabHost.setCurrentTabByTag("MORE_TAB"); 
        break; 
      } 
    } 
     
  } 
   
  private void setupIntent() { 
    this.bmTabHost = getTabHost(); 
    TabHost localTabHost = this.bmTabHost; 
 
    localTabHost.addTab(buildTabSpec("A_TAB", R.string.main_kebiao, 
        R.drawable.Ityellow, this.bmAIntent)); 
 
    localTabHost.addTab(buildTabSpec("B_TAB", R.string.main_chuqin, 
        R.drawable.Ityellow, this.bmBIntent)); 
 
    localTabHost.addTab(buildTabSpec("C_TAB", 
        R.string.main_news, R.drawable.Ityellow, 
        this.bmCIntent)); 
 
    localTabHost.addTab(buildTabSpec("D_TAB", R.string.main_me, 
        R.drawable.Ityellow, this.bmDIntent)); 
 
    localTabHost.addTab(buildTabSpec("MORE_TAB", R.string.more,
        R.drawable.Ityellow, this.bmEIntent)); 
 
  } 
   
  private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon, 
      final Intent content) { 
    return this.bmTabHost.newTabSpec(tag).setIndicator(getString(resLabel), 
        getResources().getDrawable(resIcon)).setContent(content); 
  } 
} 