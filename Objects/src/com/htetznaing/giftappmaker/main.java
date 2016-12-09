package com.htetznaing.giftappmaker;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = true;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "com.htetznaing.giftappmaker", "com.htetznaing.giftappmaker.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "com.htetznaing.giftappmaker", "com.htetznaing.giftappmaker.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.htetznaing.giftappmaker.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEvent(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        Object[] o;
        if (permissions.length > 0)
            o = new Object[] {permissions[0], grantResults[0] == 0};
        else
            o = new Object[] {"", false};
        processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.objects.Timer _ad = null;
public static String _sd = "";
public com.AB.ABZipUnzip.ABZipUnzip _zip = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imv = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _ibg = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _abg = null;
public anywheresoftware.b4a.objects.LabelWrapper _l1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lb = null;
public com.htoophyoe.anitext.animatetext _anit = null;
public anywheresoftware.b4a.keywords.constants.TypefaceWrapper _mm = null;
public anywheresoftware.b4a.objects.PanelWrapper _bar = null;
public anywheresoftware.b4a.objects.ButtonWrapper _b = null;
public anywheresoftware.b4a.objects.ButtonWrapper _bd = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper _banner = null;
public mobi.mindware.admob.interstitial.AdmobInterstitialsAds _interstitial = null;
public anywheresoftware.b4a.phone.Phone _p = null;
public com.htetznaing.giftappmaker.step1 _step1 = null;
public com.htetznaing.giftappmaker.step2 _step2 = null;
public com.htetznaing.giftappmaker.step3 _step3 = null;
public com.htetznaing.giftappmaker.step4 _step4 = null;
public com.htetznaing.giftappmaker.step5 _step5 = null;
public com.htetznaing.giftappmaker.preview _preview = null;
public com.htetznaing.giftappmaker.starter _starter = null;
public com.htetznaing.giftappmaker.about _about = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (step1.mostCurrent != null);
vis = vis | (step2.mostCurrent != null);
vis = vis | (step3.mostCurrent != null);
vis = vis | (step4.mostCurrent != null);
vis = vis | (step5.mostCurrent != null);
vis = vis | (preview.mostCurrent != null);
vis = vis | (about.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _lbf = null;
 //BA.debugLineNum = 43;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 44;BA.debugLine="p.SetScreenOrientation(1)";
mostCurrent._p.SetScreenOrientation(processBA,(int) (1));
 //BA.debugLineNum = 45;BA.debugLine="If p.SdkVersion > 19 Then";
if (mostCurrent._p.getSdkVersion()>19) { 
 //BA.debugLineNum = 46;BA.debugLine="Banner.Initialize(\"Banner\",\"ca-app-pub-417334857";
mostCurrent._banner.Initialize(mostCurrent.activityBA,"Banner","ca-app-pub-4173348573252986/6151874158");
 //BA.debugLineNum = 47;BA.debugLine="Banner.LoadAd";
mostCurrent._banner.LoadAd();
 //BA.debugLineNum = 48;BA.debugLine="Activity.AddView(Banner,0%x,90%y,100%x,10%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._banner.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 50;BA.debugLine="Interstitial.Initialize(\"Interstitial\",\"ca-app-p";
mostCurrent._interstitial.Initialize(mostCurrent.activityBA,"Interstitial","ca-app-pub-4173348573252986/7628607354");
 //BA.debugLineNum = 51;BA.debugLine="Interstitial.LoadAd";
mostCurrent._interstitial.LoadAd(mostCurrent.activityBA);
 //BA.debugLineNum = 52;BA.debugLine="ad.Initialize(\"ad\",30000)";
_ad.Initialize(processBA,"ad",(long) (30000));
 //BA.debugLineNum = 53;BA.debugLine="ad.Enabled = True";
_ad.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 56;BA.debugLine="File.Copy(File.DirAssets,\"GiftApp.apk\",File.DirR";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"GiftApp.apk",anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"GiftApp.zip");
 //BA.debugLineNum = 57;BA.debugLine="zip.ABUnzip(sd & \"GiftApp.zip\", sd & \"GiftAppMake";
mostCurrent._zip.ABUnzip(mostCurrent._sd+"GiftApp.zip",mostCurrent._sd+"GiftAppMaker");
 //BA.debugLineNum = 58;BA.debugLine="File.Delete(sd,\"GiftApp.zip\")";
anywheresoftware.b4a.keywords.Common.File.Delete(mostCurrent._sd,"GiftApp.zip");
 //BA.debugLineNum = 60;BA.debugLine="File.Copy(File.DirAssets,\"Fonts.zip\",File.DirRootE";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Fonts.zip",anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"Fonts.zip");
 //BA.debugLineNum = 61;BA.debugLine="zip.ABUnzip(sd & \"Fonts.zip\",sd & \"MyanmarFonts\")";
mostCurrent._zip.ABUnzip(mostCurrent._sd+"Fonts.zip",mostCurrent._sd+"MyanmarFonts");
 //BA.debugLineNum = 62;BA.debugLine="File.Delete(File.DirRootExternal,\"Fonts.zip\")";
anywheresoftware.b4a.keywords.Common.File.Delete(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"Fonts.zip");
 //BA.debugLineNum = 64;BA.debugLine="bd.Initialize(\"bd\")";
mostCurrent._bd.Initialize(mostCurrent.activityBA,"bd");
 //BA.debugLineNum = 65;BA.debugLine="bd.Text = \"Delete Project\"";
mostCurrent._bd.setText((Object)("Delete Project"));
 //BA.debugLineNum = 67;BA.debugLine="l1.Initialize(\"l1\")";
mostCurrent._l1.Initialize(mostCurrent.activityBA,"l1");
 //BA.debugLineNum = 68;BA.debugLine="l1.Text = \"Gift App Maker\"";
mostCurrent._l1.setText((Object)("Gift App Maker"));
 //BA.debugLineNum = 69;BA.debugLine="Activity.AddView(l1,5%x,3%y,90%x,10%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._l1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 70;BA.debugLine="l1.Gravity = Gravity.CENTER";
mostCurrent._l1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 71;BA.debugLine="l1.TextSize = 30";
mostCurrent._l1.setTextSize((float) (30));
 //BA.debugLineNum = 72;BA.debugLine="l1.TextColor = Colors.Black";
mostCurrent._l1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 73;BA.debugLine="l1.Typeface = Typeface.DEFAULT_BOLD";
mostCurrent._l1.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD);
 //BA.debugLineNum = 74;BA.debugLine="bar.Initialize(\"bar\")";
mostCurrent._bar.Initialize(mostCurrent.activityBA,"bar");
 //BA.debugLineNum = 75;BA.debugLine="bar.Color = Colors.White";
mostCurrent._bar.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 76;BA.debugLine="Activity.AddView(bar,5%x,(l1.Top+l1.Height),90%x,0";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._bar.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) ((mostCurrent._l1.getTop()+mostCurrent._l1.getHeight())),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (0.5),mostCurrent.activityBA));
 //BA.debugLineNum = 78;BA.debugLine="mm = mm.LoadFromAssets(\"myanmar.ttf\")";
mostCurrent._mm.setObject((android.graphics.Typeface)(mostCurrent._mm.LoadFromAssets("myanmar.ttf")));
 //BA.debugLineNum = 79;BA.debugLine="lb.Initialize(\"\")";
mostCurrent._lb.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 80;BA.debugLine="lb.Gravity = Gravity.CENTER";
mostCurrent._lb.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 81;BA.debugLine="lb.Textsize =  20";
mostCurrent._lb.setTextSize((float) (20));
 //BA.debugLineNum = 82;BA.debugLine="lb.TextColor = Colors.White";
mostCurrent._lb.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 83;BA.debugLine="lb.Typeface = mm";
mostCurrent._lb.setTypeface((android.graphics.Typeface)(mostCurrent._mm.getObject()));
 //BA.debugLineNum = 84;BA.debugLine="anit.initialize(\"\",Me,300)";
mostCurrent._anit._initialize(mostCurrent.activityBA,"",main.getObject(),(int) (300));
 //BA.debugLineNum = 85;BA.debugLine="anit.Run(\"Make Easily your Own\" &CRLF & \"Birthday";
mostCurrent._anit._run("Make Easily your Own"+anywheresoftware.b4a.keywords.Common.CRLF+"Birthday Wish App for"+anywheresoftware.b4a.keywords.Common.CRLF+"Your Dear!",(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(mostCurrent._lb.getObject())));
 //BA.debugLineNum = 86;BA.debugLine="anit.Endable = True";
mostCurrent._anit._endable = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 87;BA.debugLine="Activity.AddView(lb,10%x,10%y,80%x,20%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lb.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (20),mostCurrent.activityBA));
 //BA.debugLineNum = 89;BA.debugLine="abg.Initialize(Colors.RGB(255,10,144),1)";
mostCurrent._abg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (255),(int) (10),(int) (144)),(int) (1));
 //BA.debugLineNum = 90;BA.debugLine="Activity.Background = abg";
mostCurrent._activity.setBackground((android.graphics.drawable.Drawable)(mostCurrent._abg.getObject()));
 //BA.debugLineNum = 91;BA.debugLine="ibg.Initialize(LoadBitmap(File.DirAssets,\"imv.png\"";
mostCurrent._ibg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"imv.png").getObject()));
 //BA.debugLineNum = 92;BA.debugLine="imv.Initialize(\"imv\")";
mostCurrent._imv.Initialize(mostCurrent.activityBA,"imv");
 //BA.debugLineNum = 93;BA.debugLine="imv.Background = ibg";
mostCurrent._imv.setBackground((android.graphics.drawable.Drawable)(mostCurrent._ibg.getObject()));
 //BA.debugLineNum = 94;BA.debugLine="Activity.AddView(imv,20%x,40%y,200dip,200dip)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._imv.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (200)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (200)));
 //BA.debugLineNum = 96;BA.debugLine="b.Initialize(\"b\")";
mostCurrent._b.Initialize(mostCurrent.activityBA,"b");
 //BA.debugLineNum = 97;BA.debugLine="b.Text = \">> Start >>\"";
mostCurrent._b.setText((Object)(">> Start >>"));
 //BA.debugLineNum = 98;BA.debugLine="Activity.AddView(b,50%x,75%y,45%x,10%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (75),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (45),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 100;BA.debugLine="Dim lbf As Label";
_lbf = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 101;BA.debugLine="lbf.Initialize(\"lbf\")";
_lbf.Initialize(mostCurrent.activityBA,"lbf");
 //BA.debugLineNum = 102;BA.debugLine="lbf.Text = \"Developed By Khun Htetz Naing\"";
_lbf.setText((Object)("Developed By Khun Htetz Naing"));
 //BA.debugLineNum = 103;BA.debugLine="Activity.AddView(lbf,0%x,85%y,100%x,5%y)";
mostCurrent._activity.AddView((android.view.View)(_lbf.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (85),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA));
 //BA.debugLineNum = 104;BA.debugLine="lbf.Gravity = Gravity.CENTER";
_lbf.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 105;BA.debugLine="lbf.TextColor = Colors.White";
_lbf.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 107;BA.debugLine="Activity.AddView(bd,10%x,30%y,50%x,10%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._bd.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (30),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 108;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 128;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 130;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 124;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 126;BA.debugLine="End Sub";
return "";
}
public static String  _ad_tick() throws Exception{
 //BA.debugLineNum = 132;BA.debugLine="Sub ad_Tick";
 //BA.debugLineNum = 133;BA.debugLine="If p.SdkVersion > 19 Then";
if (mostCurrent._p.getSdkVersion()>19) { 
 //BA.debugLineNum = 134;BA.debugLine="If Interstitial.Status=Interstitial.Status_AdRead";
if (mostCurrent._interstitial.Status==mostCurrent._interstitial.Status_AdReadyToShow) { 
 //BA.debugLineNum = 135;BA.debugLine="Interstitial.Show";
mostCurrent._interstitial.Show(mostCurrent.activityBA);
 };
 //BA.debugLineNum = 137;BA.debugLine="If Interstitial.Status=Interstitial.Status_Dismi";
if (mostCurrent._interstitial.Status==mostCurrent._interstitial.Status_Dismissed) { 
 //BA.debugLineNum = 138;BA.debugLine="Interstitial.LoadAd";
mostCurrent._interstitial.LoadAd(mostCurrent.activityBA);
 };
 };
 //BA.debugLineNum = 141;BA.debugLine="End Sub";
return "";
}
public static String  _b_click() throws Exception{
 //BA.debugLineNum = 120;BA.debugLine="Sub b_Click";
 //BA.debugLineNum = 121;BA.debugLine="StartActivity(Step1)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._step1.getObject()));
 //BA.debugLineNum = 122;BA.debugLine="End Sub";
return "";
}
public static String  _bd_click() throws Exception{
MLfiles.Fileslib.MLfiles _ml = null;
 //BA.debugLineNum = 113;BA.debugLine="Sub bd_Click";
 //BA.debugLineNum = 114;BA.debugLine="Dim ml As MLfiles";
_ml = new MLfiles.Fileslib.MLfiles();
 //BA.debugLineNum = 115;BA.debugLine="ml.rmrf(sd & \"GiftAppMaker\")";
_ml.rmrf(mostCurrent._sd+"GiftAppMaker");
 //BA.debugLineNum = 116;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 117;BA.debugLine="StartActivity(Me)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,main.getObject());
 //BA.debugLineNum = 118;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 26;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 27;BA.debugLine="Dim sd As String = File.DirRootExternal & \"/\"";
mostCurrent._sd = anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/";
 //BA.debugLineNum = 28;BA.debugLine="Dim zip As ABZipUnzip";
mostCurrent._zip = new com.AB.ABZipUnzip.ABZipUnzip();
 //BA.debugLineNum = 30;BA.debugLine="Dim imv As ImageView";
mostCurrent._imv = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Dim ibg As BitmapDrawable";
mostCurrent._ibg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 32;BA.debugLine="Dim abg As ColorDrawable";
mostCurrent._abg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 33;BA.debugLine="Dim l1,lb As Label";
mostCurrent._l1 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lb = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Dim anit As AnimateText";
mostCurrent._anit = new com.htoophyoe.anitext.animatetext();
 //BA.debugLineNum = 35;BA.debugLine="Dim mm As Typeface";
mostCurrent._mm = new anywheresoftware.b4a.keywords.constants.TypefaceWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Dim bar As Panel";
mostCurrent._bar = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Dim b,bd As Button";
mostCurrent._b = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._bd = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Dim Banner As AdView";
mostCurrent._banner = new anywheresoftware.b4a.admobwrapper.AdViewWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Dim Interstitial As mwAdmobInterstitial";
mostCurrent._interstitial = new mobi.mindware.admob.interstitial.AdmobInterstitialsAds();
 //BA.debugLineNum = 40;BA.debugLine="Dim p As Phone";
mostCurrent._p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 41;BA.debugLine="End Sub";
return "";
}
public static String  _imv_click() throws Exception{
 //BA.debugLineNum = 110;BA.debugLine="Sub imv_Click";
 //BA.debugLineNum = 111;BA.debugLine="StartActivity(About)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._about.getObject()));
 //BA.debugLineNum = 112;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
step1._process_globals();
step2._process_globals();
step3._process_globals();
step4._process_globals();
step5._process_globals();
preview._process_globals();
starter._process_globals();
about._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 22;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 23;BA.debugLine="Dim ad As Timer";
_ad = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 24;BA.debugLine="End Sub";
return "";
}
}
