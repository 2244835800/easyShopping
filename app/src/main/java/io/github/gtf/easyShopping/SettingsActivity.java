package io.github.gtf.easyShopping;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.*;
import java.net.*;
import android.app.*;
import android.graphics.*;
import android.view.*;
import android.content.*;
import android.os.*;
import android.widget.*;
import java.util.*;
import android.renderscript.*;
import android.net.*;
import android.content.pm.*;
import android.view.View.*;
import java.io.*;
import com.pgyersdk.update.*;
import android.preference.*;
import com.pgyersdk.feedback.*;
import com.pgyersdk.activity.*;

public class SettingsActivity extends BaseActivity
{

	private boolean xianyuOK;
	private boolean jingdongOK;
	private Toolbar toolbar;
	private AlertDialog.Builder Dialog;
	private AlertDialog.Builder Dialog2;

	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
	    Dialog = new AlertDialog.Builder(this);
		Dialog2 = new AlertDialog.Builder(this);
        
	
	
		this.getFragmentManager().beginTransaction()
			.replace(android.R.id.content, new SettingsFragment())
			.commit();	
			
	}

	
    @Override
    public void onBackPressed()
	{
		SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
		xianyuOK = shp.getBoolean("check_xianyu", false);
		jingdongOK = shp.getBoolean("check_jingdong",false);
		//Toast.makeText(MyApplication.getContext(),"咸鱼："+xianyuOK+" \n京东："+jingdongOK,Toast.LENGTH_LONG).show();
		if(xianyuOK && jingdongOK){
			Toast.makeText(MyApplication.getContext(),"两个选项只能选一个哟,检查一下啦~",Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(MyApplication.getContext(),"设置保存喽，欧耶，萌萌哒！",Toast.LENGTH_SHORT).show();
			Intent backMain = new Intent(MyApplication.getContext(),Main.class);
			startActivity(backMain);
		}
		System.out.println("按下了back键   onBackPressed()");    	
    }
	
	public void mFeedBack(){
			// 以对话框的形式弹出
			PgyFeedback.getInstance().showDialog(SettingsActivity.this);

// 以Activity的形式打开，这种情况下必须在AndroidManifest.xml配置FeedbackActivity
// 打开沉浸式,默认为false
			//FeedbackActivity.setBarImmersive(true);
			//PgyFeedback.getInstance().showActivity(SettingsActivity.this);
	}
	
	public void mUpdata(){
		PgyUpdateManager.setIsForced(false); //设置是否强制更新。true为强制更新；false为不强制更新（默认值）。
		PgyUpdateManager.register(this);
	}
	
	public void pay(){
		Dialoginit();
		Dialog.show();
	}
	
	/**
     * 支付宝支付
     * @param payCode 收款码后面的字符串；例如：收款二维码里面的字符串为 https://qr.alipay.com/stx00187oxldjvyo3ofaw60 ，则
     *                payCode = stx00187oxldjvyo3ofaw60 
     *                注：不区分大小写
     */
    private void donateAlipay(String payCode) {
        boolean hasInstalledAlipayClient = AlipayDonate.hasInstalledAlipayClient(this);
        if (hasInstalledAlipayClient) {
            AlipayDonate.startAlipayClient(this, payCode);
        }
    }

	/**
     * 需要提前准备好 微信收款码 照片，可通过微信客户端生成
     */
    private void donateWeixin() {
        InputStream weixinQrIs = getResources().openRawResource(R.raw.pay_weixin);
        String qrPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "AndroidDonateSample" + File.separator +
			"didikee_weixin.png";
        WeiXinDonate.saveDonateQrImage2SDCard(qrPath, BitmapFactory.decodeStream(weixinQrIs));
        WeiXinDonate.donateViaWeiXin(this, qrPath);
    }

	private void Dialoginit(){
		Dialog.setCancelable(false);
	    Dialog.setTitle("感谢有你：");
		Dialog.setMessage("很高兴你对我作品的肯定。 \n这个作品看起来很简单，但是对于我来说，我付出了大量的心血。 \n金额不限，支持微信支付宝 \n请随(duo)意(duo)捐赠 \n其实不付款装个样子也会有VIP的，有这份心我就知足了。😉");
		Dialog.setPositiveButton("微信",  new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{

					Dialog2.setTitle("向导：");
					Dialog2.setCancelable(false);
					Dialog2.setMessage("受到微信API的限制，您需要在在弹出的界面点击右上角，从相册选取，然后选择第一张二维码向我捐赠。谢谢。");
					Dialog2.setPositiveButton("好的",  new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								setVipIcon();
								donateWeixin();
							}
						});
					Dialog2.show();

				}
			});
		Dialog.setNeutralButton("算了",  new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					
				}
			});

		Dialog.setNegativeButton("支付宝",  new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					setVipIcon();
					donateAlipay("FKX074315FOSAPU3BB5F7B");
				}
			});
	}

	private void back(){
		Intent back = new Intent(SettingsActivity.this,Main.class);
		startActivity(back);
	}

	private PackageManager mPackageManager;
    //默认组件
    private ComponentName componentNameDefault;
    private ComponentName VipIcon;
    private ComponentName mainIcon;

    /**
     * 设置VIP图标生效
     */
    private void enableVip() {
        //disableComponent(componentNameDefault);
        disableComponent(mainIcon);
        enableComponent(VipIcon);
    }

    /**
     * 设置vip图标失效
     */
    private void disableVip() {
        disableComponent(componentNameDefault);
        disableComponent(VipIcon);
        enableComponent(mainIcon);
    }

    /**
     * 启动组件
     *
     * @param componentName 组件名
     */
    private void enableComponent(ComponentName componentName) {
        //此方法用以启用和禁用组件，会覆盖Androidmanifest文件下定义的属性
        mPackageManager.setComponentEnabledSetting(componentName,
												   PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
												   PackageManager.DONT_KILL_APP);
    }

    /**
     * 禁用组件
     *
     * @param componentName 组件名
     */
    private void disableComponent(ComponentName componentName) {
        mPackageManager.setComponentEnabledSetting(componentName,
												   PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
												   PackageManager.DONT_KILL_APP);
    }

    //最后调用
    public void setVipIcon() {
        //获取到包管理类实例
        mPackageManager = getPackageManager();
        //得到此activity的全限定名
        componentNameDefault = getComponentName();
        //根据全限定名创建一个组件，即activity-alias 节点下的name：HomeActivity2 对应的组件
		VipIcon = new ComponentName(getBaseContext(), "io.github.gtf.easyShopping.小购物Vip");
        mainIcon = new ComponentName(getBaseContext(), "io.github.gtf.easyShopping.小购物");
		enableVip();
    }

	public void setDisableVipIcon() {
        //获取到包管理类实例
        mPackageManager = getPackageManager();
        //得到此activity的全限定名
        componentNameDefault = getComponentName();
        //根据全限定名创建一个组件，即activity-alias 节点下的name：HomeActivity2 对应的组件
        VipIcon = new ComponentName(getBaseContext(), "io.github.gtf.easyShopping.小购物Vip");
        mainIcon = new ComponentName(getBaseContext(), "io.github.gtf.easyShopping.小购物");
		disableVip();
    }
	
}
