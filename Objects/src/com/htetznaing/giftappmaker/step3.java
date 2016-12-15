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

public class step3 extends Activity implements B4AActivity{
	public static step3 mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "com.htetznaing.giftappmaker", "com.htetznaing.giftappmaker.step3");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (step3).");
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
		activityBA = new BA(this, layout, processBA, "com.htetznaing.giftappmaker", "com.htetznaing.giftappmaker.step3");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.htetznaing.giftappmaker.step3", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (step3) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (step3) Resume **");
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
		return step3.class;
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
        BA.LogInfo("** Activity (step3) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (step3) Resume **");
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
public anywheresoftware.b4a.objects.ImageViewWrapper _iv1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _iv2 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _iv3 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _iv4 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _iv5 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _iv6 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _iv7 = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _abg = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _chm1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _chm2 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _chm3 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _chm6 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _chm5 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _chm7 = null;
public MLfiles.Fileslib.MLfiles _ml = null;
public anywheresoftware.b4a.objects.ButtonWrapper _b = null;
public anywheresoftware.b4a.objects.LabelWrapper _lb1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lb2 = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper _banner = null;
public mobi.mindware.admob.interstitial.AdmobInterstitialsAds _interstitial = null;
public anywheresoftware.b4a.phone.Phone _p = null;
public anywheresoftware.b4a.phone.Phone.ContentChooser _c1 = null;
public anywheresoftware.b4a.phone.Phone.ContentChooser _c2 = null;
public anywheresoftware.b4a.phone.Phone.ContentChooser _c3 = null;
public anywheresoftware.b4a.phone.Phone.ContentChooser _c4 = null;
public anywheresoftware.b4a.phone.Phone.ContentChooser _c5 = null;
public anywheresoftware.b4a.phone.Phone.ContentChooser _c6 = null;
public com.htetznaing.giftappmaker.main _main = null;
public com.htetznaing.giftappmaker.step1 _step1 = null;
public com.htetznaing.giftappmaker.step2 _step2 = null;
public com.htetznaing.giftappmaker.step4 _step4 = null;
public com.htetznaing.giftappmaker.step5 _step5 = null;
public com.htetznaing.giftappmaker.preview _preview = null;
public com.htetznaing.giftappmaker.about _about = null;
public com.htetznaing.giftappmaker.starter _starter = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 25;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 26;BA.debugLine="c1.Initialize(\"c1\")";
mostCurrent._c1.Initialize("c1");
 //BA.debugLineNum = 27;BA.debugLine="c2.Initialize(\"c2\")";
mostCurrent._c2.Initialize("c2");
 //BA.debugLineNum = 28;BA.debugLine="c3.Initialize(\"c3\")";
mostCurrent._c3.Initialize("c3");
 //BA.debugLineNum = 29;BA.debugLine="c4.Initialize(\"c4\")";
mostCurrent._c4.Initialize("c4");
 //BA.debugLineNum = 30;BA.debugLine="c5.Initialize(\"c5\")";
mostCurrent._c5.Initialize("c5");
 //BA.debugLineNum = 31;BA.debugLine="c6.Initialize(\"c6\")";
mostCurrent._c6.Initialize("c6");
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
 //BA.debugLineNum = 45;BA.debugLine="abg.Initialize(Colors.RGB(255,10,144),1)";
mostCurrent._abg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (255),(int) (10),(int) (144)),(int) (1));
 //BA.debugLineNum = 46;BA.debugLine="Activity.Background = abg";
mostCurrent._activity.setBackground((android.graphics.drawable.Drawable)(mostCurrent._abg.getObject()));
 //BA.debugLineNum = 48;BA.debugLine="lb1.Initialize(\"\")";
mostCurrent._lb1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 49;BA.debugLine="lb1.Text = \"Step 1\"";
mostCurrent._lb1.setText((Object)("Step 1"));
 //BA.debugLineNum = 50;BA.debugLine="lb1.Textsize = 30";
mostCurrent._lb1.setTextSize((float) (30));
 //BA.debugLineNum = 51;BA.debugLine="lb1.TextColor = Colors.Black";
mostCurrent._lb1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 52;BA.debugLine="lb1.Typeface = Typeface.DEFAULT_BOLD";
mostCurrent._lb1.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.DEFAULT_BOLD);
 //BA.debugLineNum = 53;BA.debugLine="lb1.Gravity = Gravity.CENTER";
mostCurrent._lb1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 54;BA.debugLine="Activity.AddView(lb1,0%x,1%y,100%x,7%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lb1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (7),mostCurrent.activityBA));
 //BA.debugLineNum = 56;BA.debugLine="lb2.Initialize(\"\")";
mostCurrent._lb2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 57;BA.debugLine="lb2.Text =  \"Choose Your Image Or Next\"";
mostCurrent._lb2.setText((Object)("Choose Your Image Or Next"));
 //BA.debugLineNum = 58;BA.debugLine="lb2.TextSize = 20";
mostCurrent._lb2.setTextSize((float) (20));
 //BA.debugLineNum = 59;BA.debugLine="lb2.TextColor = Colors.White";
mostCurrent._lb2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 60;BA.debugLine="lb2.Gravity = Gravity.CENTER";
mostCurrent._lb2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 61;BA.debugLine="Activity.AddView(lb2,0%x,(lb1.Top+lb1.Height),100%";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lb2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),(int) ((mostCurrent._lb1.getTop()+mostCurrent._lb1.getHeight())),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA));
 //BA.debugLineNum = 64;BA.debugLine="iv1.Initialize(\"iv1\")";
mostCurrent._iv1.Initialize(mostCurrent.activityBA,"iv1");
 //BA.debugLineNum = 65;BA.debugLine="iv1.Bitmap = LoadBitmap(File.DirAssets,\"chooseimag";
mostCurrent._iv1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"chooseimage.png").getObject()));
 //BA.debugLineNum = 66;BA.debugLine="iv1.Gravity = Gravity.FILL";
mostCurrent._iv1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 67;BA.debugLine="Activity.AddView(iv1,1%x,(lb2.Top+lb2.Height)+2%y,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._iv1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) ((mostCurrent._lb2.getTop()+mostCurrent._lb2.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)));
 //BA.debugLineNum = 69;BA.debugLine="chm1.Initialize(\"chm1\")";
mostCurrent._chm1.Initialize(mostCurrent.activityBA,"chm1");
 //BA.debugLineNum = 70;BA.debugLine="chm1.Bitmap = LoadBitmap(File.DirAssets,\"checkmark";
mostCurrent._chm1.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"checkmark.png").getObject()));
 //BA.debugLineNum = 71;BA.debugLine="chm1.Gravity = Gravity.FILL";
mostCurrent._chm1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 72;BA.debugLine="Activity.AddView(chm1,(iv1.Top)+1%x,(lb2.Top+lb2.H";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._chm1.getObject()),(int) ((mostCurrent._iv1.getTop())+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),(int) ((mostCurrent._lb2.getTop()+mostCurrent._lb2.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (4),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (45)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40)));
 //BA.debugLineNum = 75;BA.debugLine="iv2.Initialize(\"iv2\")";
mostCurrent._iv2.Initialize(mostCurrent.activityBA,"iv2");
 //BA.debugLineNum = 76;BA.debugLine="iv2.Bitmap = LoadBitmap(File.DirAssets,\"chooseimag";
mostCurrent._iv2.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"chooseimage.png").getObject()));
 //BA.debugLineNum = 77;BA.debugLine="iv2.Gravity = Gravity.FILL";
mostCurrent._iv2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 78;BA.debugLine="Activity.AddView(iv2,(chm1.Top+chm1.Width)+6%x,(lb";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._iv2.getObject()),(int) ((mostCurrent._chm1.getTop()+mostCurrent._chm1.getWidth())+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA)),(int) ((mostCurrent._lb2.getTop()+mostCurrent._lb2.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)));
 //BA.debugLineNum = 80;BA.debugLine="chm2.Initialize(\"chm2\")";
mostCurrent._chm2.Initialize(mostCurrent.activityBA,"chm2");
 //BA.debugLineNum = 81;BA.debugLine="chm2.Bitmap = LoadBitmap(File.DirAssets,\"checkmark";
mostCurrent._chm2.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"checkmark.png").getObject()));
 //BA.debugLineNum = 82;BA.debugLine="chm2.Gravity = Gravity.FILL";
mostCurrent._chm2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 83;BA.debugLine="Activity.AddView(chm2,(iv2.Width+iv2.Top+iv1.Width";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._chm2.getObject()),(int) ((mostCurrent._iv2.getWidth()+mostCurrent._iv2.getTop()+mostCurrent._iv1.getWidth())+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (4),mostCurrent.activityBA)),(int) ((mostCurrent._lb2.getTop()+mostCurrent._lb2.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (4),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (45)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40)));
 //BA.debugLineNum = 86;BA.debugLine="iv3.Initialize(\"iv3\")";
mostCurrent._iv3.Initialize(mostCurrent.activityBA,"iv3");
 //BA.debugLineNum = 87;BA.debugLine="iv3.Bitmap = LoadBitmap(File.DirAssets,\"chooseimag";
mostCurrent._iv3.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"chooseimage.png").getObject()));
 //BA.debugLineNum = 88;BA.debugLine="iv3.Gravity = Gravity.FILL";
mostCurrent._iv3.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 89;BA.debugLine="Activity.AddView(iv3,1%x,(iv2.Top+iv2.Height)+2%y,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._iv3.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) ((mostCurrent._iv2.getTop()+mostCurrent._iv2.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)));
 //BA.debugLineNum = 91;BA.debugLine="chm3.Initialize(\"chm3\")";
mostCurrent._chm3.Initialize(mostCurrent.activityBA,"chm3");
 //BA.debugLineNum = 92;BA.debugLine="chm3.Bitmap = LoadBitmap(File.DirAssets,\"checkmark";
mostCurrent._chm3.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"checkmark.png").getObject()));
 //BA.debugLineNum = 93;BA.debugLine="chm3.Gravity = Gravity.FILL";
mostCurrent._chm3.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 94;BA.debugLine="Activity.AddView(chm3,(iv1.Top)+1%x,(iv2.Top+iv2.H";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._chm3.getObject()),(int) ((mostCurrent._iv1.getTop())+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),(int) ((mostCurrent._iv2.getTop()+mostCurrent._iv2.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (4),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (45)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40)));
 //BA.debugLineNum = 97;BA.debugLine="iv5.Initialize(\"iv5\")";
mostCurrent._iv5.Initialize(mostCurrent.activityBA,"iv5");
 //BA.debugLineNum = 98;BA.debugLine="iv5.Bitmap = LoadBitmap(File.DirAssets,\"chooseimag";
mostCurrent._iv5.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"chooseimage.png").getObject()));
 //BA.debugLineNum = 99;BA.debugLine="iv5.Gravity = Gravity.FILL";
mostCurrent._iv5.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 100;BA.debugLine="Activity.AddView(iv5,(chm1.Top+chm1.Width)+6%x,(iv";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._iv5.getObject()),(int) ((mostCurrent._chm1.getTop()+mostCurrent._chm1.getWidth())+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA)),(int) ((mostCurrent._iv2.getTop()+mostCurrent._iv2.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)));
 //BA.debugLineNum = 102;BA.debugLine="chm5.Initialize(\"chm5\")";
mostCurrent._chm5.Initialize(mostCurrent.activityBA,"chm5");
 //BA.debugLineNum = 103;BA.debugLine="chm5.Bitmap = LoadBitmap(File.DirAssets,\"checkmark";
mostCurrent._chm5.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"checkmark.png").getObject()));
 //BA.debugLineNum = 104;BA.debugLine="chm5.Gravity = Gravity.FILL";
mostCurrent._chm5.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 105;BA.debugLine="Activity.AddView(chm5,(iv2.Width+iv2.Top+iv1.Width";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._chm5.getObject()),(int) ((mostCurrent._iv2.getWidth()+mostCurrent._iv2.getTop()+mostCurrent._iv1.getWidth())+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (4),mostCurrent.activityBA)),(int) ((mostCurrent._iv2.getTop()+mostCurrent._iv2.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (4),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (45)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40)));
 //BA.debugLineNum = 108;BA.debugLine="iv6.Initialize(\"iv6\")";
mostCurrent._iv6.Initialize(mostCurrent.activityBA,"iv6");
 //BA.debugLineNum = 109;BA.debugLine="iv6.Bitmap = LoadBitmap(File.DirAssets,\"chooseimag";
mostCurrent._iv6.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"chooseimage.png").getObject()));
 //BA.debugLineNum = 110;BA.debugLine="iv6.Gravity = Gravity.FILL";
mostCurrent._iv6.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 111;BA.debugLine="Activity.AddView(iv6,1%x,(iv3.Top+iv3.Height)+2%y,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._iv6.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) ((mostCurrent._iv3.getTop()+mostCurrent._iv3.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)));
 //BA.debugLineNum = 113;BA.debugLine="chm6.Initialize(\"chm6\")";
mostCurrent._chm6.Initialize(mostCurrent.activityBA,"chm6");
 //BA.debugLineNum = 114;BA.debugLine="chm6.Bitmap = LoadBitmap(File.DirAssets,\"checkmark";
mostCurrent._chm6.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"checkmark.png").getObject()));
 //BA.debugLineNum = 115;BA.debugLine="chm6.Gravity = Gravity.FILL";
mostCurrent._chm6.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 116;BA.debugLine="Activity.AddView(chm6,(iv1.Top)+1%x,(iv3.Top+iv3.H";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._chm6.getObject()),(int) ((mostCurrent._iv1.getTop())+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA)),(int) ((mostCurrent._iv3.getTop()+mostCurrent._iv3.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (4),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (45)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40)));
 //BA.debugLineNum = 119;BA.debugLine="iv7.Initialize(\"iv7\")";
mostCurrent._iv7.Initialize(mostCurrent.activityBA,"iv7");
 //BA.debugLineNum = 120;BA.debugLine="iv7.Bitmap = LoadBitmap(File.DirAssets,\"chooseimag";
mostCurrent._iv7.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"chooseimage.png").getObject()));
 //BA.debugLineNum = 121;BA.debugLine="iv7.Gravity = Gravity.FILL";
mostCurrent._iv7.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 122;BA.debugLine="Activity.AddView(iv7,(chm1.Top+chm1.Width)+6%x,(iv";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._iv7.getObject()),(int) ((mostCurrent._chm1.getTop()+mostCurrent._chm1.getWidth())+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (6),mostCurrent.activityBA)),(int) ((mostCurrent._iv3.getTop()+mostCurrent._iv3.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80)));
 //BA.debugLineNum = 124;BA.debugLine="chm7.Initialize(\"chm7\")";
mostCurrent._chm7.Initialize(mostCurrent.activityBA,"chm7");
 //BA.debugLineNum = 125;BA.debugLine="chm7.Bitmap = LoadBitmap(File.DirAssets,\"checkmark";
mostCurrent._chm7.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"checkmark.png").getObject()));
 //BA.debugLineNum = 126;BA.debugLine="chm7.Gravity = Gravity.FILL";
mostCurrent._chm7.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 127;BA.debugLine="Activity.AddView(chm7,(iv2.Width+iv2.Top+iv1.Width";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._chm7.getObject()),(int) ((mostCurrent._iv2.getWidth()+mostCurrent._iv2.getTop()+mostCurrent._iv1.getWidth())+anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (4),mostCurrent.activityBA)),(int) ((mostCurrent._iv3.getTop()+mostCurrent._iv3.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (4),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (45)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40)));
 //BA.debugLineNum = 129;BA.debugLine="iv4.Initialize(\"iv4\")";
mostCurrent._iv4.Initialize(mostCurrent.activityBA,"iv4");
 //BA.debugLineNum = 130;BA.debugLine="iv4.Bitmap = LoadBitmap(File.DirAssets,\"save.png\")";
mostCurrent._iv4.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"save.png").getObject()));
 //BA.debugLineNum = 131;BA.debugLine="iv4.Gravity = Gravity.FILL";
mostCurrent._iv4.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 132;BA.debugLine="Activity.AddView(iv4,70%x,(iv7.Top+iv7.Height)+2%y";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._iv4.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA),(int) ((mostCurrent._iv7.getTop()+mostCurrent._iv7.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (75)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (65)));
 //BA.debugLineNum = 134;BA.debugLine="b.Initialize(\"b\")";
mostCurrent._b.Initialize(mostCurrent.activityBA,"b");
 //BA.debugLineNum = 135;BA.debugLine="b.Text = \"Preview\"";
mostCurrent._b.setText((Object)("Preview"));
 //BA.debugLineNum = 136;BA.debugLine="Activity.AddView(b,15%x,(iv7.Top+iv7.Height)+5%y,1";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),(int) ((mostCurrent._iv7.getTop()+mostCurrent._iv7.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (170)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (57)));
 //BA.debugLineNum = 138;BA.debugLine="chm1.Visible = False";
mostCurrent._chm1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 139;BA.debugLine="chm2.Visible =False";
mostCurrent._chm2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 140;BA.debugLine="chm3.Visible =False";
mostCurrent._chm3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 141;BA.debugLine="chm5.Visible = False";
mostCurrent._chm5.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 142;BA.debugLine="chm6.Visible = False";
mostCurrent._chm6.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 143;BA.debugLine="chm7.Visible = False";
mostCurrent._chm7.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 145;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 231;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 233;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 227;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 229;BA.debugLine="End Sub";
return "";
}
public static String  _ad_tick() throws Exception{
 //BA.debugLineNum = 235;BA.debugLine="Sub ad_Tick";
 //BA.debugLineNum = 236;BA.debugLine="If p.SdkVersion > 19 Then";
if (mostCurrent._p.getSdkVersion()>19) { 
 //BA.debugLineNum = 237;BA.debugLine="If Interstitial.Status=Interstitial.Status_AdRead";
if (mostCurrent._interstitial.Status==mostCurrent._interstitial.Status_AdReadyToShow) { 
 //BA.debugLineNum = 238;BA.debugLine="Interstitial.Show";
mostCurrent._interstitial.Show(mostCurrent.activityBA);
 };
 //BA.debugLineNum = 240;BA.debugLine="If Interstitial.Status=Interstitial.Status_Dismi";
if (mostCurrent._interstitial.Status==mostCurrent._interstitial.Status_Dismissed) { 
 //BA.debugLineNum = 241;BA.debugLine="Interstitial.LoadAd";
mostCurrent._interstitial.LoadAd(mostCurrent.activityBA);
 };
 };
 //BA.debugLineNum = 244;BA.debugLine="End Sub";
return "";
}
public static String  _b_click() throws Exception{
 //BA.debugLineNum = 147;BA.debugLine="Sub b_Click";
 //BA.debugLineNum = 148;BA.debugLine="StartActivity(Preview)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._preview.getObject()));
 //BA.debugLineNum = 149;BA.debugLine="End Sub";
return "";
}
public static String  _c1_result(boolean _success,String _dir,String _filename) throws Exception{
 //BA.debugLineNum = 159;BA.debugLine="Sub c1_Result (Success As Boolean, Dir As String,";
 //BA.debugLineNum = 160;BA.debugLine="If Success Then";
if (_success) { 
 //BA.debugLineNum = 161;BA.debugLine="File.Delete( File.DirRootExternal & \"/.GiftAppMa";
anywheresoftware.b4a.keywords.Common.File.Delete(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","img2.jpg");
 //BA.debugLineNum = 162;BA.debugLine="File.Copy(Dir, FileName, File.DirRoot";
anywheresoftware.b4a.keywords.Common.File.Copy(_dir,_filename,anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","img2.jpg");
 //BA.debugLineNum = 163;BA.debugLine="chm1.Visible = True";
mostCurrent._chm1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 165;BA.debugLine="End Sub";
return "";
}
public static String  _c2_result(boolean _success,String _dir,String _filename) throws Exception{
 //BA.debugLineNum = 171;BA.debugLine="Sub c2_Result (Success As Boolean, Dir As String,";
 //BA.debugLineNum = 172;BA.debugLine="If Success Then";
if (_success) { 
 //BA.debugLineNum = 173;BA.debugLine="File.Delete( File.DirRootExternal & \"/.GiftAppMa";
anywheresoftware.b4a.keywords.Common.File.Delete(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","p1.jpg");
 //BA.debugLineNum = 174;BA.debugLine="File.Copy(Dir, FileName, File.DirRoot";
anywheresoftware.b4a.keywords.Common.File.Copy(_dir,_filename,anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","p1.jpg");
 //BA.debugLineNum = 175;BA.debugLine="chm2.Visible = True";
mostCurrent._chm2.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 177;BA.debugLine="End Sub";
return "";
}
public static String  _c3_result(boolean _success,String _dir,String _filename) throws Exception{
 //BA.debugLineNum = 183;BA.debugLine="Sub c3_Result (Success As Boolean, Dir As String,";
 //BA.debugLineNum = 184;BA.debugLine="If Success Then";
if (_success) { 
 //BA.debugLineNum = 185;BA.debugLine="File.Delete( File.DirRootExternal & \"/.GiftAppMa";
anywheresoftware.b4a.keywords.Common.File.Delete(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","p2.jpg");
 //BA.debugLineNum = 186;BA.debugLine="File.Copy(Dir, FileName, File.DirRoot";
anywheresoftware.b4a.keywords.Common.File.Copy(_dir,_filename,anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","p2.jpg");
 //BA.debugLineNum = 187;BA.debugLine="chm3.Visible = True";
mostCurrent._chm3.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 189;BA.debugLine="End Sub";
return "";
}
public static String  _c4_result(boolean _success,String _dir,String _filename) throws Exception{
 //BA.debugLineNum = 195;BA.debugLine="Sub c4_Result (Success As Boolean, Dir As String,";
 //BA.debugLineNum = 196;BA.debugLine="If Success Then";
if (_success) { 
 //BA.debugLineNum = 197;BA.debugLine="File.Delete( File.DirRootExternal & \"/.GiftAppMa";
anywheresoftware.b4a.keywords.Common.File.Delete(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","p3.jpg");
 //BA.debugLineNum = 198;BA.debugLine="File.Copy(Dir, FileName, File.DirRoot";
anywheresoftware.b4a.keywords.Common.File.Copy(_dir,_filename,anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","p3.jpg");
 //BA.debugLineNum = 199;BA.debugLine="chm5.Visible = True";
mostCurrent._chm5.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 201;BA.debugLine="End Sub";
return "";
}
public static String  _c5_result(boolean _success,String _dir,String _filename) throws Exception{
 //BA.debugLineNum = 207;BA.debugLine="Sub c5_Result (Success As Boolean, Dir As String,";
 //BA.debugLineNum = 208;BA.debugLine="If Success Then";
if (_success) { 
 //BA.debugLineNum = 209;BA.debugLine="File.Delete( File.DirRootExternal & \"/.GiftAppMa";
anywheresoftware.b4a.keywords.Common.File.Delete(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","p4.jpg");
 //BA.debugLineNum = 210;BA.debugLine="File.Copy(Dir, FileName, File.DirRoot";
anywheresoftware.b4a.keywords.Common.File.Copy(_dir,_filename,anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","p4.jpg");
 //BA.debugLineNum = 211;BA.debugLine="chm6.Visible = True";
mostCurrent._chm6.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 213;BA.debugLine="End Sub";
return "";
}
public static String  _c6_result(boolean _success,String _dir,String _filename) throws Exception{
 //BA.debugLineNum = 219;BA.debugLine="Sub c6_Result (Success As Boolean, Dir As String,";
 //BA.debugLineNum = 220;BA.debugLine="If Success Then";
if (_success) { 
 //BA.debugLineNum = 221;BA.debugLine="File.Delete( File.DirRootExternal & \"/.GiftAppMa";
anywheresoftware.b4a.keywords.Common.File.Delete(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","p5.jpg");
 //BA.debugLineNum = 222;BA.debugLine="File.Copy(Dir, FileName, File.DirRoot";
anywheresoftware.b4a.keywords.Common.File.Copy(_dir,_filename,anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","p5.jpg");
 //BA.debugLineNum = 223;BA.debugLine="chm7.Visible = True";
mostCurrent._chm7.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 225;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 12;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 13;BA.debugLine="Dim iv1,iv2,iv3,iv4,iv5,iv6,iv7 As ImageView";
mostCurrent._iv1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._iv2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._iv3 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._iv4 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._iv5 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._iv6 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._iv7 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 14;BA.debugLine="Dim abg As ColorDrawable";
mostCurrent._abg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 15;BA.debugLine="Dim chm1,chm2,chm3,chm6,chm5,chm7 As ImageView";
mostCurrent._chm1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._chm2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._chm3 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._chm6 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._chm5 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._chm7 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 16;BA.debugLine="Dim ml As MLfiles";
mostCurrent._ml = new MLfiles.Fileslib.MLfiles();
 //BA.debugLineNum = 17;BA.debugLine="Dim b As Button";
mostCurrent._b = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Dim lb1,lb2 As Label";
mostCurrent._lb1 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lb2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Dim Banner As AdView";
mostCurrent._banner = new anywheresoftware.b4a.admobwrapper.AdViewWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Dim Interstitial As mwAdmobInterstitial";
mostCurrent._interstitial = new mobi.mindware.admob.interstitial.AdmobInterstitialsAds();
 //BA.debugLineNum = 21;BA.debugLine="Dim p As Phone";
mostCurrent._p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 22;BA.debugLine="Dim c1,c2,c3,c4,c5,c6 As ContentChooser";
mostCurrent._c1 = new anywheresoftware.b4a.phone.Phone.ContentChooser();
mostCurrent._c2 = new anywheresoftware.b4a.phone.Phone.ContentChooser();
mostCurrent._c3 = new anywheresoftware.b4a.phone.Phone.ContentChooser();
mostCurrent._c4 = new anywheresoftware.b4a.phone.Phone.ContentChooser();
mostCurrent._c5 = new anywheresoftware.b4a.phone.Phone.ContentChooser();
mostCurrent._c6 = new anywheresoftware.b4a.phone.Phone.ContentChooser();
 //BA.debugLineNum = 23;BA.debugLine="End Sub";
return "";
}
public static String  _iv1_click() throws Exception{
 //BA.debugLineNum = 155;BA.debugLine="Sub iv1_Click";
 //BA.debugLineNum = 156;BA.debugLine="c1.Show(\"image/*\", \"Choose image\")";
mostCurrent._c1.Show(processBA,"image/*","Choose image");
 //BA.debugLineNum = 157;BA.debugLine="End Sub";
return "";
}
public static String  _iv2_click() throws Exception{
 //BA.debugLineNum = 167;BA.debugLine="Sub iv2_Click";
 //BA.debugLineNum = 168;BA.debugLine="c2.Show(\"image/*\", \"Choose image\")";
mostCurrent._c2.Show(processBA,"image/*","Choose image");
 //BA.debugLineNum = 169;BA.debugLine="End Sub";
return "";
}
public static String  _iv3_click() throws Exception{
 //BA.debugLineNum = 179;BA.debugLine="Sub iv3_Click";
 //BA.debugLineNum = 180;BA.debugLine="c3.Show(\"image/*\", \"Choose image\")";
mostCurrent._c3.Show(processBA,"image/*","Choose image");
 //BA.debugLineNum = 181;BA.debugLine="End Sub";
return "";
}
public static String  _iv4_click() throws Exception{
 //BA.debugLineNum = 151;BA.debugLine="Sub iv4_Click";
 //BA.debugLineNum = 152;BA.debugLine="StartActivity(Step4)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._step4.getObject()));
 //BA.debugLineNum = 153;BA.debugLine="End Sub";
return "";
}
public static String  _iv5_click() throws Exception{
 //BA.debugLineNum = 191;BA.debugLine="Sub iv5_Click";
 //BA.debugLineNum = 192;BA.debugLine="c4.Show(\"image/*\", \"Choose image\")";
mostCurrent._c4.Show(processBA,"image/*","Choose image");
 //BA.debugLineNum = 193;BA.debugLine="End Sub";
return "";
}
public static String  _iv6_click() throws Exception{
 //BA.debugLineNum = 203;BA.debugLine="Sub iv6_Click";
 //BA.debugLineNum = 204;BA.debugLine="c5.Show(\"image/*\", \"Choose image\")";
mostCurrent._c5.Show(processBA,"image/*","Choose image");
 //BA.debugLineNum = 205;BA.debugLine="End Sub";
return "";
}
public static String  _iv7_click() throws Exception{
 //BA.debugLineNum = 215;BA.debugLine="Sub iv7_Click";
 //BA.debugLineNum = 216;BA.debugLine="c6.Show(\"image/*\", \"Choose image\")";
mostCurrent._c6.Show(processBA,"image/*","Choose image");
 //BA.debugLineNum = 217;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim ad As Timer";
_ad = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 10;BA.debugLine="End Sub";
return "";
}
}
