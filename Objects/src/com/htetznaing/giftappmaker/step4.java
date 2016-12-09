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

public class step4 extends Activity implements B4AActivity{
	public static step4 mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "com.htetznaing.giftappmaker", "com.htetznaing.giftappmaker.step4");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (step4).");
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
		activityBA = new BA(this, layout, processBA, "com.htetznaing.giftappmaker", "com.htetznaing.giftappmaker.step4");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.htetznaing.giftappmaker.step4", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (step4) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (step4) Resume **");
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
		return step4.class;
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
        BA.LogInfo("** Activity (step4) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (step4) Resume **");
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
public static anywheresoftware.b4a.objects.MediaPlayerWrapper _mediaplayer1 = null;
public static anywheresoftware.b4a.objects.Timer _timer1 = null;
public static anywheresoftware.b4a.objects.Timer _ad = null;
public anywheresoftware.b4a.objects.LabelWrapper _lb1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lb2 = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _abg = null;
public anywheresoftware.b4a.objects.SeekBarWrapper _barposition = null;
public anywheresoftware.b4a.objects.SeekBarWrapper _barvolume = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblposition = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _iv1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _chm1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _iv2 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _iv3 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _iv4 = null;
public MLfiles.Fileslib.MLfiles _ml = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper _banner = null;
public mobi.mindware.admob.interstitial.AdmobInterstitialsAds _interstitial = null;
public anywheresoftware.b4a.phone.Phone _p = null;
public com.htetznaing.giftappmaker.main _main = null;
public com.htetznaing.giftappmaker.step1 _step1 = null;
public com.htetznaing.giftappmaker.step2 _step2 = null;
public com.htetznaing.giftappmaker.step3 _step3 = null;
public com.htetznaing.giftappmaker.step5 _step5 = null;
public com.htetznaing.giftappmaker.preview _preview = null;
public com.htetznaing.giftappmaker.starter _starter = null;
public com.htetznaing.giftappmaker.about _about = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _lbf = null;
 //BA.debugLineNum = 32;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 33;BA.debugLine="p.SetScreenOrientation(1)";
mostCurrent._p.SetScreenOrientation(processBA,(int) (1));
 //BA.debugLineNum = 34;BA.debugLine="If p.SdkVersion > 19 Then";
if (mostCurrent._p.getSdkVersion()>19) { 
 //BA.debugLineNum = 35;BA.debugLine="Banner.Initialize(\"Banner\",\"ca-app-pub-417334857";
mostCurrent._banner.Initialize(mostCurrent.activityBA,"Banner","ca-app-pub-4173348573252986/6151874158");
 //BA.debugLineNum = 36;BA.debugLine="Banner.LoadAd";
mostCurrent._banner.LoadAd();
 //BA.debugLineNum = 37;BA.debugLine="Activity.AddView(Banner,0%x,90%y,100%x,10%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._banner.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 39;BA.debugLine="Interstitial.Initialize(\"Interstitial\",\"ca-app-p";
mostCurrent._interstitial.Initialize(mostCurrent.activityBA,"Interstitial","ca-app-pub-4173348573252986/7628607354");
 //BA.debugLineNum = 40;BA.debugLine="Interstitial.LoadAd";
mostCurrent._interstitial.LoadAd(mostCurrent.activityBA);
 //BA.debugLineNum = 41;BA.debugLine="ad.Initialize(\"ad\",30000)";
_ad.Initialize(processBA,"ad",(long) (30000));
 //BA.debugLineNum = 42;BA.debugLine="ad.Enabled = True";
_ad.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 45;BA.debugLine="MediaPlayer1.Initialize( )";
_mediaplayer1.Initialize();
 //BA.debugLineNum = 46;BA.debugLine="timer1.Initialize(\"timer1\", 100)";
_timer1.Initialize(processBA,"timer1",(long) (100));
 //BA.debugLineNum = 47;BA.debugLine="abg.Initialize(Colors.RGB(255,10,144),1)";
mostCurrent._abg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (255),(int) (10),(int) (144)),(int) (1));
 //BA.debugLineNum = 48;BA.debugLine="Activity.Background = abg";
mostCurrent._activity.setBackground((android.graphics.drawable.Drawable)(mostCurrent._abg.getObject()));
 //BA.debugLineNum = 49;BA.debugLine="Activity.LoadLayout(\"l4\")";
mostCurrent._activity.LoadLayout("l4",mostCurrent.activityBA);
 //BA.debugLineNum = 50;BA.debugLine="lb1.Text = \"Step 4\"";
mostCurrent._lb1.setText((Object)("Step 4"));
 //BA.debugLineNum = 51;BA.debugLine="lb1.Textsize = 30";
mostCurrent._lb1.setTextSize((float) (30));
 //BA.debugLineNum = 52;BA.debugLine="lb1.TextColor = Colors.Black";
mostCurrent._lb1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 53;BA.debugLine="lb1.Typeface = Typeface.DEFAULT_BOLD";
mostCurrent._lb1.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD);
 //BA.debugLineNum = 54;BA.debugLine="lb1.Gravity = Gravity.CENTER";
mostCurrent._lb1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 55;BA.debugLine="lb2.Text = \"Choose Your Music Or Next\"";
mostCurrent._lb2.setText((Object)("Choose Your Music Or Next"));
 //BA.debugLineNum = 56;BA.debugLine="lb2.TextSize = 20";
mostCurrent._lb2.setTextSize((float) (20));
 //BA.debugLineNum = 57;BA.debugLine="lb2.TextColor = Colors.White";
mostCurrent._lb2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 58;BA.debugLine="lb2.Gravity = Gravity.CENTER";
mostCurrent._lb2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 60;BA.debugLine="chm1.Visible = False";
mostCurrent._chm1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 62;BA.debugLine="Dim lbf As Label";
_lbf = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 63;BA.debugLine="lbf.Initialize(\"lbf\")";
_lbf.Initialize(mostCurrent.activityBA,"lbf");
 //BA.debugLineNum = 64;BA.debugLine="lbf.Text = \"Developed By Khun Htetz Naing\"";
_lbf.setText((Object)("Developed By Khun Htetz Naing"));
 //BA.debugLineNum = 65;BA.debugLine="Activity.AddView(lbf,0%x,85%y,100%x,5%y)";
mostCurrent._activity.AddView((android.view.View)(_lbf.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (85),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA));
 //BA.debugLineNum = 66;BA.debugLine="lbf.Gravity = Gravity.CENTER";
_lbf.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 67;BA.debugLine="lbf.TextColor = Colors.White";
_lbf.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 68;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 109;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 111;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 105;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 107;BA.debugLine="End Sub";
return "";
}
public static String  _ad_tick() throws Exception{
 //BA.debugLineNum = 146;BA.debugLine="Sub ad_Tick";
 //BA.debugLineNum = 147;BA.debugLine="If p.SdkVersion > 19 Then";
if (mostCurrent._p.getSdkVersion()>19) { 
 //BA.debugLineNum = 148;BA.debugLine="If Interstitial.Status=Interstitial.Status_AdRead";
if (mostCurrent._interstitial.Status==mostCurrent._interstitial.Status_AdReadyToShow) { 
 //BA.debugLineNum = 149;BA.debugLine="Interstitial.Show";
mostCurrent._interstitial.Show(mostCurrent.activityBA);
 };
 //BA.debugLineNum = 151;BA.debugLine="If Interstitial.Status=Interstitial.Status_Dismi";
if (mostCurrent._interstitial.Status==mostCurrent._interstitial.Status_Dismissed) { 
 //BA.debugLineNum = 152;BA.debugLine="Interstitial.LoadAd";
mostCurrent._interstitial.LoadAd(mostCurrent.activityBA);
 };
 };
 //BA.debugLineNum = 155;BA.debugLine="End Sub";
return "";
}
public static String  _b_click() throws Exception{
 //BA.debugLineNum = 142;BA.debugLine="Sub b_Click";
 //BA.debugLineNum = 143;BA.debugLine="StartActivity(Preview)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._preview.getObject()));
 //BA.debugLineNum = 144;BA.debugLine="End Sub";
return "";
}
public static String  _barposition_valuechanged(int _value,boolean _userchanged) throws Exception{
 //BA.debugLineNum = 133;BA.debugLine="Sub barPosition_ValueChanged (Value As Int, UserCh";
 //BA.debugLineNum = 134;BA.debugLine="If UserChanged = False Then Return 'the value was";
if (_userchanged==anywheresoftware.b4a.keywords.Common.False) { 
if (true) return "";};
 //BA.debugLineNum = 135;BA.debugLine="MediaPlayer1.Position = Value / 100 * MediaPlayer";
_mediaplayer1.setPosition((int) (_value/(double)100*_mediaplayer1.getDuration()));
 //BA.debugLineNum = 136;BA.debugLine="If MediaPlayer1.IsPlaying = False Then 'this can";
if (_mediaplayer1.IsPlaying()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 137;BA.debugLine="MediaPlayer1.Play";
_mediaplayer1.Play();
 };
 //BA.debugLineNum = 139;BA.debugLine="timer1_Tick 'immediately update the progress labe";
_timer1_tick();
 //BA.debugLineNum = 140;BA.debugLine="End Sub";
return "";
}
public static String  _barvolume_valuechanged(int _value,boolean _userchanged) throws Exception{
 //BA.debugLineNum = 129;BA.debugLine="Sub barVolume_ValueChanged (Value As Int, UserChan";
 //BA.debugLineNum = 130;BA.debugLine="MediaPlayer1.SetVolume(barVolume.Value / 100, bar";
_mediaplayer1.SetVolume((float) (mostCurrent._barvolume.getValue()/(double)100),(float) (mostCurrent._barvolume.getValue()/(double)100));
 //BA.debugLineNum = 131;BA.debugLine="End Sub";
return "";
}
public static String  _converttotimeformat(int _ms) throws Exception{
int _seconds = 0;
int _minutes = 0;
 //BA.debugLineNum = 121;BA.debugLine="Sub ConvertToTimeFormat(ms As Int) As String";
 //BA.debugLineNum = 122;BA.debugLine="Dim seconds, minutes As Int";
_seconds = 0;
_minutes = 0;
 //BA.debugLineNum = 123;BA.debugLine="seconds = Round(ms / 1000)";
_seconds = (int) (anywheresoftware.b4a.keywords.Common.Round(_ms/(double)1000));
 //BA.debugLineNum = 124;BA.debugLine="minutes = Floor(seconds / 60)";
_minutes = (int) (anywheresoftware.b4a.keywords.Common.Floor(_seconds/(double)60));
 //BA.debugLineNum = 125;BA.debugLine="seconds = seconds Mod 60";
_seconds = (int) (_seconds%60);
 //BA.debugLineNum = 126;BA.debugLine="Return NumberFormat(minutes, 1, 0) & \":\" & Number";
if (true) return anywheresoftware.b4a.keywords.Common.NumberFormat(_minutes,(int) (1),(int) (0))+":"+anywheresoftware.b4a.keywords.Common.NumberFormat(_seconds,(int) (2),(int) (0));
 //BA.debugLineNum = 127;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 14;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 15;BA.debugLine="Dim lb1,lb2 As Label";
mostCurrent._lb1 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lb2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 16;BA.debugLine="Dim abg As ColorDrawable";
mostCurrent._abg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 18;BA.debugLine="Dim barPosition As SeekBar";
mostCurrent._barposition = new anywheresoftware.b4a.objects.SeekBarWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Dim barVolume As SeekBar";
mostCurrent._barvolume = new anywheresoftware.b4a.objects.SeekBarWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Dim lblPosition As Label";
mostCurrent._lblposition = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Dim iv1 As ImageView";
mostCurrent._iv1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Dim chm1 As ImageView";
mostCurrent._chm1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Dim iv2 As ImageView";
mostCurrent._iv2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Dim iv3 As ImageView";
mostCurrent._iv3 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Dim  iv4 As ImageView";
mostCurrent._iv4 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Dim ml As MLfiles";
mostCurrent._ml = new MLfiles.Fileslib.MLfiles();
 //BA.debugLineNum = 27;BA.debugLine="Dim Banner As AdView";
mostCurrent._banner = new anywheresoftware.b4a.admobwrapper.AdViewWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Dim Interstitial As mwAdmobInterstitial";
mostCurrent._interstitial = new mobi.mindware.admob.interstitial.AdmobInterstitialsAds();
 //BA.debugLineNum = 29;BA.debugLine="Dim p As Phone";
mostCurrent._p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 30;BA.debugLine="End Sub";
return "";
}
public static String  _iv1_click() throws Exception{
anywheresoftware.b4a.agraham.dialogs.InputDialog.FileDialog _fd = null;
 //BA.debugLineNum = 70;BA.debugLine="Sub iv1_Click";
 //BA.debugLineNum = 71;BA.debugLine="Dim fd As FileDialog";
_fd = new anywheresoftware.b4a.agraham.dialogs.InputDialog.FileDialog();
 //BA.debugLineNum = 72;BA.debugLine="fd.FilePath = File.DirRootExternal";
_fd.setFilePath(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal());
 //BA.debugLineNum = 73;BA.debugLine="fd.Show(\"Choose Your Image\",\"Open\",\"Reset\",\"Cancel";
_fd.Show("Choose Your Image","Open","Reset","Cancel",mostCurrent.activityBA,(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 74;BA.debugLine="If fd.Response = DialogResponse.POSITIVE Then";
if (_fd.getResponse()==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 75;BA.debugLine="ml.mv(File.DirRootExternal & \"/GiftAppMaker/asset";
mostCurrent._ml.mv(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/GiftAppMaker/assets/birthday.mp3",anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/GiftAppMaker/assets/birthday.bak");
 //BA.debugLineNum = 76;BA.debugLine="File.Copy(fd.FilePath,fd.ChosenName,File.DirRootEx";
anywheresoftware.b4a.keywords.Common.File.Copy(_fd.getFilePath(),_fd.getChosenName(),anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/GiftAppMaker/assets","birthday.mp3");
 //BA.debugLineNum = 77;BA.debugLine="chm1.Visible = True";
mostCurrent._chm1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 79;BA.debugLine="If fd.Response = DialogResponse.CANCEL Then";
if (_fd.getResponse()==anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
 //BA.debugLineNum = 80;BA.debugLine="File.Delete(File.DirRootExternal & \"/GiftAppMake";
anywheresoftware.b4a.keywords.Common.File.Delete(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/GiftAppMaker/assets","birthday.mp3");
 //BA.debugLineNum = 81;BA.debugLine="ml.mv(File.DirRootExternal & \"/GiftAppMaker/asse";
mostCurrent._ml.mv(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/GiftAppMaker/assets/birthday.bak",anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/GiftAppMaker/assets/birthday.mp3");
 //BA.debugLineNum = 82;BA.debugLine="chm1.Visible = False";
mostCurrent._chm1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 85;BA.debugLine="End Sub";
return "";
}
public static String  _iv2_click() throws Exception{
 //BA.debugLineNum = 87;BA.debugLine="Sub iv2_Click";
 //BA.debugLineNum = 88;BA.debugLine="If MediaPlayer1.IsPlaying = False Then";
if (_mediaplayer1.IsPlaying()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 89;BA.debugLine="MediaPlayer1.Load(File.DirRootExternal & \"/GiftAp";
_mediaplayer1.Load(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/GiftAppMaker/assets","birthday.mp3");
 //BA.debugLineNum = 90;BA.debugLine="MediaPlayer1.Play";
_mediaplayer1.Play();
 //BA.debugLineNum = 91;BA.debugLine="timer1.Enabled = True";
_timer1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 93;BA.debugLine="End Sub";
return "";
}
public static String  _iv3_click() throws Exception{
 //BA.debugLineNum = 95;BA.debugLine="Sub iv3_Click";
 //BA.debugLineNum = 96;BA.debugLine="If MediaPlayer1.IsPlaying Then";
if (_mediaplayer1.IsPlaying()) { 
 //BA.debugLineNum = 97;BA.debugLine="MediaPlayer1.Stop";
_mediaplayer1.Stop();
 };
 //BA.debugLineNum = 99;BA.debugLine="End Sub";
return "";
}
public static String  _iv4_click() throws Exception{
 //BA.debugLineNum = 101;BA.debugLine="Sub iv4_Click";
 //BA.debugLineNum = 102;BA.debugLine="StartActivity(Step5)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._step5.getObject()));
 //BA.debugLineNum = 103;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim MediaPlayer1 As MediaPlayer";
_mediaplayer1 = new anywheresoftware.b4a.objects.MediaPlayerWrapper();
 //BA.debugLineNum = 10;BA.debugLine="Dim timer1 As Timer";
_timer1 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 11;BA.debugLine="Dim ad As Timer";
_ad = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
return "";
}
public static String  _timer1_tick() throws Exception{
 //BA.debugLineNum = 113;BA.debugLine="Sub timer1_Tick";
 //BA.debugLineNum = 114;BA.debugLine="If MediaPlayer1.IsPlaying Then";
if (_mediaplayer1.IsPlaying()) { 
 //BA.debugLineNum = 115;BA.debugLine="barPosition.Value = MediaPlayer1.Position / Medi";
mostCurrent._barposition.setValue((int) (_mediaplayer1.getPosition()/(double)_mediaplayer1.getDuration()*100));
 //BA.debugLineNum = 116;BA.debugLine="lblPosition.Text = \"Position: \" & ConvertToTimeF";
mostCurrent._lblposition.setText((Object)("Position: "+_converttotimeformat(_mediaplayer1.getPosition())+" ("+_converttotimeformat(_mediaplayer1.getDuration())+")"));
 };
 //BA.debugLineNum = 119;BA.debugLine="End Sub";
return "";
}
}
