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

public class preview extends Activity implements B4AActivity{
	public static preview mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "com.htetznaing.giftappmaker", "com.htetznaing.giftappmaker.preview");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (preview).");
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
		activityBA = new BA(this, layout, processBA, "com.htetznaing.giftappmaker", "com.htetznaing.giftappmaker.preview");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.htetznaing.giftappmaker.preview", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (preview) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (preview) Resume **");
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
		return preview.class;
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
        BA.LogInfo("** Activity (preview) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (preview) Resume **");
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
public static anywheresoftware.b4a.objects.Timer _t1 = null;
public static anywheresoftware.b4a.objects.Timer _t3 = null;
public static anywheresoftware.b4a.objects.Timer _t4 = null;
public static anywheresoftware.b4a.objects.Timer _it1 = null;
public static anywheresoftware.b4a.objects.Timer _it2 = null;
public static anywheresoftware.b4a.objects.Timer _it3 = null;
public static anywheresoftware.b4a.objects.Timer _it4 = null;
public static anywheresoftware.b4a.objects.Timer _it5 = null;
public static anywheresoftware.b4a.objects.Timer _it6 = null;
public static anywheresoftware.b4a.objects.Timer _it7 = null;
public static anywheresoftware.b4a.objects.Timer _it8 = null;
public static anywheresoftware.b4a.objects.Timer _it9 = null;
public static anywheresoftware.b4a.objects.Timer _it10 = null;
public static anywheresoftware.b4a.objects.Timer _it11 = null;
public static anywheresoftware.b4a.objects.Timer _it12 = null;
public static anywheresoftware.b4a.objects.Timer _it13 = null;
public static anywheresoftware.b4a.objects.Timer _it14 = null;
public static anywheresoftware.b4a.objects.Timer _it15 = null;
public static anywheresoftware.b4a.objects.Timer _b1t = null;
public static anywheresoftware.b4a.objects.Timer _b2t = null;
public static anywheresoftware.b4a.objects.Timer _b3t = null;
public static anywheresoftware.b4a.objects.Timer _b4t = null;
public static anywheresoftware.b4a.objects.Timer _b5t = null;
public static anywheresoftware.b4a.objects.Timer _b6t = null;
public static anywheresoftware.b4a.objects.Timer _sl1 = null;
public static anywheresoftware.b4a.objects.Timer _sl2 = null;
public static anywheresoftware.b4a.objects.Timer _sl3 = null;
public static anywheresoftware.b4a.objects.Timer _sl4 = null;
public static anywheresoftware.b4a.objects.Timer _sl5 = null;
public static int _theme_value = 0;
public anywheresoftware.b4a.objects.ImageViewWrapper _b1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _b2 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _b3 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _b4 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _b5 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _b6 = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _b1bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _b2bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _b3bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _b4bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _b5bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _b6bg = null;
public giuseppe.salvi.icos.library.ICOSSlideAnimation _b1ani = null;
public giuseppe.salvi.icos.library.ICOSSlideAnimation _b2ani = null;
public giuseppe.salvi.icos.library.ICOSSlideAnimation _b3ani = null;
public giuseppe.salvi.icos.library.ICOSSlideAnimation _b4ani = null;
public giuseppe.salvi.icos.library.ICOSSlideAnimation _b5ani = null;
public giuseppe.salvi.icos.library.ICOSSlideAnimation _b6ani = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _p1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _p2 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _p3 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _p4 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _p5 = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _p1bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _p2bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _p3bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _p4bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _p5bg = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _img1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _img2 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _img3 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _img4 = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _img1bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _img2bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _img3bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _img4bg = null;
public flm.b4a.animationplus.AnimationPlusWrapper _ani2 = null;
public flm.b4a.animationplus.AnimationPlusWrapper _ani3 = null;
public flm.b4a.animationplus.AnimationPlusWrapper _ani4 = null;
public anywheresoftware.b4a.objects.LabelWrapper _bw = null;
public com.htoophyoe.anitext.animatetext _bdtext = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _i1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _i2 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _i3 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _i4 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _i5 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _i6 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _i7 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _i8 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _i9 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _i10 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _i11 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _i12 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _i13 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _i14 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _i15 = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _i1bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _i2bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _i3bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _i4bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _i5bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _i6bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _i7bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _i8bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _i9bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _i10bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _i11bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _i12bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _i13bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _i14bg = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _i15bg = null;
public anywheresoftware.b4a.keywords.constants.TypefaceWrapper _tf = null;
public giuseppe.salvi.icos.library.ICOSSlideAnimation _i1ani = null;
public giuseppe.salvi.icos.library.ICOSSlideAnimation _i2ani = null;
public giuseppe.salvi.icos.library.ICOSSlideAnimation _i3ani = null;
public giuseppe.salvi.icos.library.ICOSSlideAnimation _i4ani = null;
public giuseppe.salvi.icos.library.ICOSSlideAnimation _i5ani = null;
public giuseppe.salvi.icos.library.ICOSSlideAnimation _i6ani = null;
public giuseppe.salvi.icos.library.ICOSSlideAnimation _i7ani = null;
public giuseppe.salvi.icos.library.ICOSSlideAnimation _i8ani = null;
public giuseppe.salvi.icos.library.ICOSSlideAnimation _i9ani = null;
public giuseppe.salvi.icos.library.ICOSSlideAnimation _i10ani = null;
public giuseppe.salvi.icos.library.ICOSSlideAnimation _i11ani = null;
public giuseppe.salvi.icos.library.ICOSSlideAnimation _i12ani = null;
public giuseppe.salvi.icos.library.ICOSSlideAnimation _i13ani = null;
public giuseppe.salvi.icos.library.ICOSSlideAnimation _i14ani = null;
public giuseppe.salvi.icos.library.ICOSSlideAnimation _i15ani = null;
public anywheresoftware.b4a.objects.MediaPlayerWrapper _mp = null;
public de.amberhome.objects.floatingactionbutton.FloatingActionButtonWrapper _fb = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _mbg = null;
public anywheresoftware.b4a.object.XmlLayoutBuilder _res = null;
public anywheresoftware.b4a.objects.ButtonWrapper _bb = null;
public anywheresoftware.b4a.phone.Phone _p = null;
public com.htetznaing.giftappmaker.main _main = null;
public com.htetznaing.giftappmaker.step1 _step1 = null;
public com.htetznaing.giftappmaker.step2 _step2 = null;
public com.htetznaing.giftappmaker.step3 _step3 = null;
public com.htetznaing.giftappmaker.step4 _step4 = null;
public com.htetznaing.giftappmaker.step5 _step5 = null;
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
anywheresoftware.b4a.phone.Phone _pp = null;
float _phvol = 0f;
float _maxvol = 0f;
String _t = "";
 //BA.debugLineNum = 40;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 41;BA.debugLine="p.SetScreenOrientation(1)";
mostCurrent._p.SetScreenOrientation(processBA,(int) (1));
 //BA.debugLineNum = 42;BA.debugLine="bb.Initialize(\"bb\")";
mostCurrent._bb.Initialize(mostCurrent.activityBA,"bb");
 //BA.debugLineNum = 43;BA.debugLine="bb.Text = \"Create Apk\"";
mostCurrent._bb.setText((Object)("Create Apk"));
 //BA.debugLineNum = 44;BA.debugLine="Activity.AddView(bb,20%x,89%y,60%x,10%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._bb.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (89),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 46;BA.debugLine="mbg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._mbg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","love.png").getObject()));
 //BA.debugLineNum = 47;BA.debugLine="Activity.LoadLayout(\"menu\")";
mostCurrent._activity.LoadLayout("menu",mostCurrent.activityBA);
 //BA.debugLineNum = 48;BA.debugLine="fb.Background = mbg";
mostCurrent._fb.setBackground((android.graphics.drawable.Drawable)(mostCurrent._mbg.getObject()));
 //BA.debugLineNum = 49;BA.debugLine="fb.HideOffset = 100%y - fb.Top";
mostCurrent._fb.setHideOffset((int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-mostCurrent._fb.getTop()));
 //BA.debugLineNum = 50;BA.debugLine="fb.Hide(False)";
mostCurrent._fb.Hide(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 51;BA.debugLine="fb.Show(True)";
mostCurrent._fb.Show(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 53;BA.debugLine="Dim pp As Phone";
_pp = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 56;BA.debugLine="Dim PhVol,MaxVol As Float";
_phvol = 0f;
_maxvol = 0f;
 //BA.debugLineNum = 57;BA.debugLine="PhVol = pp.GetVolume(pp.VOLUME_MUSIC)";
_phvol = (float) (_pp.GetVolume(_pp.VOLUME_MUSIC));
 //BA.debugLineNum = 58;BA.debugLine="MaxVol = pp.GetMaxVolume(pp.VOLUME_MUSIC)";
_maxvol = (float) (_pp.GetMaxVolume(_pp.VOLUME_MUSIC));
 //BA.debugLineNum = 59;BA.debugLine="pp.SetVolume(pp.VOLUME_MUSIC,MaxVol,False)";
_pp.SetVolume(_pp.VOLUME_MUSIC,(int) (_maxvol),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 61;BA.debugLine="img1bg.Initialize(LoadBitmap(File.DirRootExternal";
mostCurrent._img1bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","img1.png").getObject()));
 //BA.debugLineNum = 62;BA.debugLine="img1.Initialize(\"img1\")";
mostCurrent._img1.Initialize(mostCurrent.activityBA,"img1");
 //BA.debugLineNum = 63;BA.debugLine="img1.Background = img1bg";
mostCurrent._img1.setBackground((android.graphics.drawable.Drawable)(mostCurrent._img1bg.getObject()));
 //BA.debugLineNum = 64;BA.debugLine="Activity.AddView(img1,1%x,0%y,98%x,10%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._img1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (98),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 66;BA.debugLine="mp.Initialize2(\"mp\")";
mostCurrent._mp.Initialize2(processBA,"mp");
 //BA.debugLineNum = 67;BA.debugLine="mp.Load(File.DirRootExternal & \"/.GiftAppMaker/ass";
mostCurrent._mp.Load(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","birthday.mp3");
 //BA.debugLineNum = 68;BA.debugLine="mp.Play";
mostCurrent._mp.Play();
 //BA.debugLineNum = 70;BA.debugLine="Dim t As String";
_t = "";
 //BA.debugLineNum = 72;BA.debugLine="t = File.ReadString(File.DirRootExternal & \"/.Gift";
_t = anywheresoftware.b4a.keywords.Common.File.ReadString(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","bdw.txt");
 //BA.debugLineNum = 73;BA.debugLine="tf = tf.LoadFromAssets(\"Myanmar.ttf\")";
mostCurrent._tf.setObject((android.graphics.Typeface)(mostCurrent._tf.LoadFromAssets("Myanmar.ttf")));
 //BA.debugLineNum = 74;BA.debugLine="bw.Initialize(\"b2\")";
mostCurrent._bw.Initialize(mostCurrent.activityBA,"b2");
 //BA.debugLineNum = 75;BA.debugLine="bw.Typeface = tf";
mostCurrent._bw.setTypeface((android.graphics.Typeface)(mostCurrent._tf.getObject()));
 //BA.debugLineNum = 76;BA.debugLine="bw.TextColor= Colors.White";
mostCurrent._bw.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 77;BA.debugLine="bw.Gravity = Gravity.CENTER";
mostCurrent._bw.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 78;BA.debugLine="bdtext.initialize(\"bdtext\",Me,300)";
mostCurrent._bdtext._initialize(mostCurrent.activityBA,"bdtext",preview.getObject(),(int) (300));
 //BA.debugLineNum = 79;BA.debugLine="bdtext.Run(t,bw)";
mostCurrent._bdtext._run(_t,(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(mostCurrent._bw.getObject())));
 //BA.debugLineNum = 80;BA.debugLine="bdtext.Endable = True";
mostCurrent._bdtext._endable = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 82;BA.debugLine="Activity.AddView(bw,1%x,(img1.Top+img1.Height),98%";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._bw.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) ((mostCurrent._img1.getTop()+mostCurrent._img1.getHeight())),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (98),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (15),mostCurrent.activityBA));
 //BA.debugLineNum = 84;BA.debugLine="img2bg.Initialize(LoadBitmap(File.DirRootExternal";
mostCurrent._img2bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","img2.jpg").getObject()));
 //BA.debugLineNum = 85;BA.debugLine="img2.Initialize(\"img2\")";
mostCurrent._img2.Initialize(mostCurrent.activityBA,"img2");
 //BA.debugLineNum = 86;BA.debugLine="img2.Background = img2bg";
mostCurrent._img2.setBackground((android.graphics.drawable.Drawable)(mostCurrent._img2bg.getObject()));
 //BA.debugLineNum = 87;BA.debugLine="Activity.AddView(img2,10%x,(img1.Top+img1.Height)+";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._img2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),(int) ((mostCurrent._img1.getTop()+mostCurrent._img1.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (15),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA));
 //BA.debugLineNum = 88;BA.debugLine="img1.Visible = False";
mostCurrent._img1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 91;BA.debugLine="p1bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._p1bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","p1.jpg").getObject()));
 //BA.debugLineNum = 92;BA.debugLine="p2bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._p2bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","p2.jpg").getObject()));
 //BA.debugLineNum = 93;BA.debugLine="p3bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._p3bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","p3.jpg").getObject()));
 //BA.debugLineNum = 94;BA.debugLine="p4bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._p4bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","p4.jpg").getObject()));
 //BA.debugLineNum = 95;BA.debugLine="p5bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._p5bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","p5.jpg").getObject()));
 //BA.debugLineNum = 97;BA.debugLine="p1.Initialize(\"p1\")";
mostCurrent._p1.Initialize(mostCurrent.activityBA,"p1");
 //BA.debugLineNum = 98;BA.debugLine="p1.Background = p1bg";
mostCurrent._p1.setBackground((android.graphics.drawable.Drawable)(mostCurrent._p1bg.getObject()));
 //BA.debugLineNum = 99;BA.debugLine="Activity.AddView(p1,10%x,(img1.Top+img1.Height)+1";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._p1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),(int) ((mostCurrent._img1.getTop()+mostCurrent._img1.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (15),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA));
 //BA.debugLineNum = 100;BA.debugLine="p1.Visible = False";
mostCurrent._p1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 102;BA.debugLine="p2.Initialize(\"p2\")";
mostCurrent._p2.Initialize(mostCurrent.activityBA,"p2");
 //BA.debugLineNum = 103;BA.debugLine="p2.Background = p2bg";
mostCurrent._p2.setBackground((android.graphics.drawable.Drawable)(mostCurrent._p2bg.getObject()));
 //BA.debugLineNum = 104;BA.debugLine="Activity.AddView(p2,10%x,(img1.Top+img1.Height)+1";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._p2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),(int) ((mostCurrent._img1.getTop()+mostCurrent._img1.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (15),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA));
 //BA.debugLineNum = 105;BA.debugLine="p2.Visible = False";
mostCurrent._p2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 107;BA.debugLine="p3.Initialize(\"p3\")";
mostCurrent._p3.Initialize(mostCurrent.activityBA,"p3");
 //BA.debugLineNum = 108;BA.debugLine="p3.Background = p3bg";
mostCurrent._p3.setBackground((android.graphics.drawable.Drawable)(mostCurrent._p3bg.getObject()));
 //BA.debugLineNum = 109;BA.debugLine="Activity.AddView(p3,10%x,(img1.Top+img1.Height)+1";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._p3.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),(int) ((mostCurrent._img1.getTop()+mostCurrent._img1.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (15),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA));
 //BA.debugLineNum = 110;BA.debugLine="p3.Visible = False";
mostCurrent._p3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 112;BA.debugLine="p4.Initialize(\"p4\")";
mostCurrent._p4.Initialize(mostCurrent.activityBA,"p4");
 //BA.debugLineNum = 113;BA.debugLine="p4.Background = p4bg";
mostCurrent._p4.setBackground((android.graphics.drawable.Drawable)(mostCurrent._p4bg.getObject()));
 //BA.debugLineNum = 114;BA.debugLine="Activity.AddView(p4,10%x,(img1.Top+img1.Height)+1";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._p4.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),(int) ((mostCurrent._img1.getTop()+mostCurrent._img1.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (15),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA));
 //BA.debugLineNum = 115;BA.debugLine="p4.Visible = False";
mostCurrent._p4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 117;BA.debugLine="p5.Initialize(\"p5\")";
mostCurrent._p5.Initialize(mostCurrent.activityBA,"p5");
 //BA.debugLineNum = 118;BA.debugLine="p5.Background = p5bg";
mostCurrent._p5.setBackground((android.graphics.drawable.Drawable)(mostCurrent._p5bg.getObject()));
 //BA.debugLineNum = 119;BA.debugLine="Activity.AddView(p5,10%x,(img1.Top+img1.Height)+1";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._p5.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),(int) ((mostCurrent._img1.getTop()+mostCurrent._img1.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (15),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA));
 //BA.debugLineNum = 120;BA.debugLine="p5.Visible = False";
mostCurrent._p5.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 123;BA.debugLine="img3bg.Initialize(LoadBitmap(File.DirRootExternal";
mostCurrent._img3bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","img3.png").getObject()));
 //BA.debugLineNum = 124;BA.debugLine="img3.Initialize(\"img3\")";
mostCurrent._img3.Initialize(mostCurrent.activityBA,"img3");
 //BA.debugLineNum = 125;BA.debugLine="img3.Background = img3bg";
mostCurrent._img3.setBackground((android.graphics.drawable.Drawable)(mostCurrent._img3bg.getObject()));
 //BA.debugLineNum = 126;BA.debugLine="Activity.AddView(img3,5%x,(img2.Top+img2.Height) -";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._img3.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),(int) ((mostCurrent._img2.getTop()+mostCurrent._img2.getHeight())-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA));
 //BA.debugLineNum = 127;BA.debugLine="img3.Visible = False";
mostCurrent._img3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 130;BA.debugLine="img4bg.Initialize(LoadBitmap(File.DirRootExternal";
mostCurrent._img4bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","img4.png").getObject()));
 //BA.debugLineNum = 131;BA.debugLine="img4.Initialize(\"img4\")";
mostCurrent._img4.Initialize(mostCurrent.activityBA,"img4");
 //BA.debugLineNum = 132;BA.debugLine="img4.Background = img4bg";
mostCurrent._img4.setBackground((android.graphics.drawable.Drawable)(mostCurrent._img4bg.getObject()));
 //BA.debugLineNum = 133;BA.debugLine="Activity.AddView(img4,1%x,(img2.Top+img2.Height) -";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._img4.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),(int) ((mostCurrent._img2.getTop()+mostCurrent._img2.getHeight())-anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (98),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (35),mostCurrent.activityBA));
 //BA.debugLineNum = 134;BA.debugLine="img4.Visible = False";
mostCurrent._img4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 139;BA.debugLine="ani2.InitializeScaleCenter(\"ani\", 0, 0, 1, 1, img";
mostCurrent._ani2.InitializeScaleCenter(mostCurrent.activityBA,"ani",(float) (0),(float) (0),(float) (1),(float) (1),(android.view.View)(mostCurrent._img2.getObject()));
 //BA.debugLineNum = 140;BA.debugLine="ani2.Duration = 1500";
mostCurrent._ani2.setDuration((long) (1500));
 //BA.debugLineNum = 141;BA.debugLine="ani2.RepeatCount = 2";
mostCurrent._ani2.setRepeatCount((int) (2));
 //BA.debugLineNum = 142;BA.debugLine="ani2.SetInterpolator(ani2.INTERPOLATOR_BOUNCE)";
mostCurrent._ani2.SetInterpolator(mostCurrent._ani2.INTERPOLATOR_BOUNCE);
 //BA.debugLineNum = 143;BA.debugLine="ani2.Start(img2)";
mostCurrent._ani2.Start((android.view.View)(mostCurrent._img2.getObject()));
 //BA.debugLineNum = 148;BA.debugLine="t1.Initialize(\"t1\",4000)";
_t1.Initialize(processBA,"t1",(long) (4000));
 //BA.debugLineNum = 149;BA.debugLine="t1.Enabled = True";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 152;BA.debugLine="t3.Initialize(\"t3\",500)";
_t3.Initialize(processBA,"t3",(long) (500));
 //BA.debugLineNum = 153;BA.debugLine="t3.Enabled =False";
_t3.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 156;BA.debugLine="t4.Initialize(\"t4\",5000)";
_t4.Initialize(processBA,"t4",(long) (5000));
 //BA.debugLineNum = 157;BA.debugLine="t4.Enabled = False";
_t4.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 160;BA.debugLine="Activity.LoadLayout(\"lay1\")";
mostCurrent._activity.LoadLayout("lay1",mostCurrent.activityBA);
 //BA.debugLineNum = 162;BA.debugLine="i1bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._i1bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","i1.png").getObject()));
 //BA.debugLineNum = 163;BA.debugLine="i2bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._i2bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","i2.png").getObject()));
 //BA.debugLineNum = 164;BA.debugLine="i3bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._i3bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","i3.png").getObject()));
 //BA.debugLineNum = 165;BA.debugLine="i4bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._i4bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","i4.png").getObject()));
 //BA.debugLineNum = 166;BA.debugLine="i5bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._i5bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","i5.png").getObject()));
 //BA.debugLineNum = 167;BA.debugLine="i6bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._i6bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","i6.png").getObject()));
 //BA.debugLineNum = 168;BA.debugLine="i7bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._i7bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","i7.png").getObject()));
 //BA.debugLineNum = 169;BA.debugLine="i8bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._i8bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","i8.png").getObject()));
 //BA.debugLineNum = 170;BA.debugLine="i9bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._i9bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","i9.png").getObject()));
 //BA.debugLineNum = 171;BA.debugLine="i10bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._i10bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","i10.png").getObject()));
 //BA.debugLineNum = 172;BA.debugLine="i11bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._i11bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","i11.png").getObject()));
 //BA.debugLineNum = 173;BA.debugLine="i12bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._i12bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","i1.png").getObject()));
 //BA.debugLineNum = 174;BA.debugLine="i13bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._i13bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","i2.png").getObject()));
 //BA.debugLineNum = 175;BA.debugLine="i14bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._i14bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","i3.png").getObject()));
 //BA.debugLineNum = 176;BA.debugLine="i15bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._i15bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","i4.png").getObject()));
 //BA.debugLineNum = 178;BA.debugLine="b1bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._b1bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","i5.png").getObject()));
 //BA.debugLineNum = 179;BA.debugLine="b2bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._b2bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","i6.png").getObject()));
 //BA.debugLineNum = 180;BA.debugLine="b3bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._b3bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","i7.png").getObject()));
 //BA.debugLineNum = 181;BA.debugLine="b4bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._b4bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","i8.png").getObject()));
 //BA.debugLineNum = 182;BA.debugLine="b5bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._b5bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","i7.png").getObject()));
 //BA.debugLineNum = 183;BA.debugLine="b6bg.Initialize(LoadBitmap(File.DirRootExternal &";
mostCurrent._b6bg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.GiftAppMaker/assets","i3.png").getObject()));
 //BA.debugLineNum = 186;BA.debugLine="i1.Background = i1bg";
mostCurrent._i1.setBackground((android.graphics.drawable.Drawable)(mostCurrent._i1bg.getObject()));
 //BA.debugLineNum = 189;BA.debugLine="i2.Background = i5bg";
mostCurrent._i2.setBackground((android.graphics.drawable.Drawable)(mostCurrent._i5bg.getObject()));
 //BA.debugLineNum = 192;BA.debugLine="i3.Background = i2bg";
mostCurrent._i3.setBackground((android.graphics.drawable.Drawable)(mostCurrent._i2bg.getObject()));
 //BA.debugLineNum = 196;BA.debugLine="i4.Background = i6bg";
mostCurrent._i4.setBackground((android.graphics.drawable.Drawable)(mostCurrent._i6bg.getObject()));
 //BA.debugLineNum = 200;BA.debugLine="i5.Background = i3bg";
mostCurrent._i5.setBackground((android.graphics.drawable.Drawable)(mostCurrent._i3bg.getObject()));
 //BA.debugLineNum = 203;BA.debugLine="i6.Background = i7bg";
mostCurrent._i6.setBackground((android.graphics.drawable.Drawable)(mostCurrent._i7bg.getObject()));
 //BA.debugLineNum = 206;BA.debugLine="i7.Background = i4bg";
mostCurrent._i7.setBackground((android.graphics.drawable.Drawable)(mostCurrent._i4bg.getObject()));
 //BA.debugLineNum = 208;BA.debugLine="i8.Background = i8bg";
mostCurrent._i8.setBackground((android.graphics.drawable.Drawable)(mostCurrent._i8bg.getObject()));
 //BA.debugLineNum = 209;BA.debugLine="i9.Background = i9bg";
mostCurrent._i9.setBackground((android.graphics.drawable.Drawable)(mostCurrent._i9bg.getObject()));
 //BA.debugLineNum = 210;BA.debugLine="i10.Background = i10bg";
mostCurrent._i10.setBackground((android.graphics.drawable.Drawable)(mostCurrent._i10bg.getObject()));
 //BA.debugLineNum = 211;BA.debugLine="i11.Background = i11bg";
mostCurrent._i11.setBackground((android.graphics.drawable.Drawable)(mostCurrent._i11bg.getObject()));
 //BA.debugLineNum = 212;BA.debugLine="i12.Background = i12bg";
mostCurrent._i12.setBackground((android.graphics.drawable.Drawable)(mostCurrent._i12bg.getObject()));
 //BA.debugLineNum = 213;BA.debugLine="i13.Background = i13bg";
mostCurrent._i13.setBackground((android.graphics.drawable.Drawable)(mostCurrent._i13bg.getObject()));
 //BA.debugLineNum = 214;BA.debugLine="i14.Background = i14bg";
mostCurrent._i14.setBackground((android.graphics.drawable.Drawable)(mostCurrent._i14bg.getObject()));
 //BA.debugLineNum = 215;BA.debugLine="i15.Background = i15bg";
mostCurrent._i15.setBackground((android.graphics.drawable.Drawable)(mostCurrent._i15bg.getObject()));
 //BA.debugLineNum = 218;BA.debugLine="b1.Background = b1bg";
mostCurrent._b1.setBackground((android.graphics.drawable.Drawable)(mostCurrent._b1bg.getObject()));
 //BA.debugLineNum = 219;BA.debugLine="b2.Background = b2bg";
mostCurrent._b2.setBackground((android.graphics.drawable.Drawable)(mostCurrent._b2bg.getObject()));
 //BA.debugLineNum = 220;BA.debugLine="b3.Background = b3bg";
mostCurrent._b3.setBackground((android.graphics.drawable.Drawable)(mostCurrent._b3bg.getObject()));
 //BA.debugLineNum = 221;BA.debugLine="b4.Background = b4bg";
mostCurrent._b4.setBackground((android.graphics.drawable.Drawable)(mostCurrent._b4bg.getObject()));
 //BA.debugLineNum = 222;BA.debugLine="b5.Background = b5bg";
mostCurrent._b5.setBackground((android.graphics.drawable.Drawable)(mostCurrent._b5bg.getObject()));
 //BA.debugLineNum = 223;BA.debugLine="b6.Background = b6bg";
mostCurrent._b6.setBackground((android.graphics.drawable.Drawable)(mostCurrent._b6bg.getObject()));
 //BA.debugLineNum = 225;BA.debugLine="i1.Visible = False";
mostCurrent._i1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 226;BA.debugLine="i2.Visible = False";
mostCurrent._i2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 227;BA.debugLine="i3.Visible = False";
mostCurrent._i3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 228;BA.debugLine="i4.Visible = False";
mostCurrent._i4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 229;BA.debugLine="i5.Visible = False";
mostCurrent._i5.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 230;BA.debugLine="i6.Visible = False";
mostCurrent._i6.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 231;BA.debugLine="i7.Visible = False";
mostCurrent._i7.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 232;BA.debugLine="i8.Visible = False";
mostCurrent._i8.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 233;BA.debugLine="i9.Visible = False";
mostCurrent._i9.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 234;BA.debugLine="i10.Visible = False";
mostCurrent._i10.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 235;BA.debugLine="i11.Visible = False";
mostCurrent._i11.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 236;BA.debugLine="i12.Visible = False";
mostCurrent._i12.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 237;BA.debugLine="i13.Visible = False";
mostCurrent._i13.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 238;BA.debugLine="i14.Visible = False";
mostCurrent._i14.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 239;BA.debugLine="i15.Visible = False";
mostCurrent._i15.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 241;BA.debugLine="b1.Visible = False";
mostCurrent._b1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 242;BA.debugLine="b2.Visible = False";
mostCurrent._b2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 243;BA.debugLine="b3.Visible = False";
mostCurrent._b3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 244;BA.debugLine="b4.Visible = False";
mostCurrent._b4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 245;BA.debugLine="b5.Visible = False";
mostCurrent._b5.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 246;BA.debugLine="b6.Visible = False";
mostCurrent._b6.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 249;BA.debugLine="it1.Initialize(\"it1\",1000)";
_it1.Initialize(processBA,"it1",(long) (1000));
 //BA.debugLineNum = 250;BA.debugLine="it2.Initialize(\"it2\",2000)";
_it2.Initialize(processBA,"it2",(long) (2000));
 //BA.debugLineNum = 251;BA.debugLine="it3.Initialize(\"it3\",1000)";
_it3.Initialize(processBA,"it3",(long) (1000));
 //BA.debugLineNum = 252;BA.debugLine="it4.Initialize(\"it4\",4000)";
_it4.Initialize(processBA,"it4",(long) (4000));
 //BA.debugLineNum = 253;BA.debugLine="it5.Initialize(\"it5\",3000)";
_it5.Initialize(processBA,"it5",(long) (3000));
 //BA.debugLineNum = 254;BA.debugLine="it6.Initialize(\"it6\",2500)";
_it6.Initialize(processBA,"it6",(long) (2500));
 //BA.debugLineNum = 255;BA.debugLine="it7.Initialize(\"it7\",3500)";
_it7.Initialize(processBA,"it7",(long) (3500));
 //BA.debugLineNum = 256;BA.debugLine="it8.Initialize(\"it8\",4500)";
_it8.Initialize(processBA,"it8",(long) (4500));
 //BA.debugLineNum = 257;BA.debugLine="it9.Initialize(\"it9\",5000)";
_it9.Initialize(processBA,"it9",(long) (5000));
 //BA.debugLineNum = 258;BA.debugLine="it10.Initialize(\"it10\",5500)";
_it10.Initialize(processBA,"it10",(long) (5500));
 //BA.debugLineNum = 259;BA.debugLine="it11.Initialize(\"it11\",1500)";
_it11.Initialize(processBA,"it11",(long) (1500));
 //BA.debugLineNum = 260;BA.debugLine="it12.Initialize(\"it12\",6000)";
_it12.Initialize(processBA,"it12",(long) (6000));
 //BA.debugLineNum = 261;BA.debugLine="it13.Initialize(\"it13\",7000)";
_it13.Initialize(processBA,"it13",(long) (7000));
 //BA.debugLineNum = 262;BA.debugLine="it14.Initialize(\"it14\",8000)";
_it14.Initialize(processBA,"it14",(long) (8000));
 //BA.debugLineNum = 263;BA.debugLine="it15.Initialize(\"it15\",9000)";
_it15.Initialize(processBA,"it15",(long) (9000));
 //BA.debugLineNum = 265;BA.debugLine="b1t.Initialize(\"b1t\",5500)";
_b1t.Initialize(processBA,"b1t",(long) (5500));
 //BA.debugLineNum = 266;BA.debugLine="b2t.Initialize(\"b2t\",6000)";
_b2t.Initialize(processBA,"b2t",(long) (6000));
 //BA.debugLineNum = 267;BA.debugLine="b3t.Initialize(\"b3t\",6500)";
_b3t.Initialize(processBA,"b3t",(long) (6500));
 //BA.debugLineNum = 268;BA.debugLine="b4t.Initialize(\"b4t\",7500)";
_b4t.Initialize(processBA,"b4t",(long) (7500));
 //BA.debugLineNum = 269;BA.debugLine="b5t.Initialize(\"b5t\",8500)";
_b5t.Initialize(processBA,"b5t",(long) (8500));
 //BA.debugLineNum = 270;BA.debugLine="b6t.Initialize(\"b6t\",9500)";
_b6t.Initialize(processBA,"b6t",(long) (9500));
 //BA.debugLineNum = 272;BA.debugLine="it1.Enabled = False";
_it1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 273;BA.debugLine="it2.Enabled = False";
_it2.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 274;BA.debugLine="it3.Enabled = False";
_it3.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 275;BA.debugLine="it4.Enabled = False";
_it4.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 276;BA.debugLine="it5.Enabled = False";
_it5.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 277;BA.debugLine="it6.Enabled = False";
_it6.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 278;BA.debugLine="it7.Enabled = False";
_it7.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 279;BA.debugLine="it8.Enabled = False";
_it8.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 280;BA.debugLine="it9.Enabled = False";
_it9.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 281;BA.debugLine="it10.Enabled = False";
_it10.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 282;BA.debugLine="it11.Enabled = False";
_it11.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 283;BA.debugLine="it12.Enabled = False";
_it12.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 284;BA.debugLine="it13.Enabled = False";
_it13.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 285;BA.debugLine="it14.Enabled = False";
_it14.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 286;BA.debugLine="it15.Enabled = False";
_it15.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 288;BA.debugLine="b1t.Enabled = False";
_b1t.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 289;BA.debugLine="b2t.Enabled = False";
_b2t.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 290;BA.debugLine="b3t.Enabled = False";
_b3t.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 291;BA.debugLine="b4t.Enabled = False";
_b4t.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 292;BA.debugLine="b5t.Enabled = False";
_b5t.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 293;BA.debugLine="b6t.Enabled = False";
_b6t.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 295;BA.debugLine="Activity.AddMenuItem(\"Stop Mp3\",\"sm\")";
mostCurrent._activity.AddMenuItem("Stop Mp3","sm");
 //BA.debugLineNum = 303;BA.debugLine="sl1.Initialize(\"sl1\",10000)";
_sl1.Initialize(processBA,"sl1",(long) (10000));
 //BA.debugLineNum = 304;BA.debugLine="sl2.Initialize(\"sl2\",20000)";
_sl2.Initialize(processBA,"sl2",(long) (20000));
 //BA.debugLineNum = 305;BA.debugLine="sl3.Initialize(\"sl3\",30000)";
_sl3.Initialize(processBA,"sl3",(long) (30000));
 //BA.debugLineNum = 306;BA.debugLine="sl4.Initialize(\"sl4\",40000)";
_sl4.Initialize(processBA,"sl4",(long) (40000));
 //BA.debugLineNum = 307;BA.debugLine="sl5.Initialize(\"sl5\",50000)";
_sl5.Initialize(processBA,"sl5",(long) (50000));
 //BA.debugLineNum = 309;BA.debugLine="sl1.Enabled = True";
_sl1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 310;BA.debugLine="sl2.Enabled = True";
_sl2.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 311;BA.debugLine="sl3.Enabled = True";
_sl3.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 312;BA.debugLine="sl4.Enabled = True";
_sl4.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 313;BA.debugLine="sl5.Enabled = True";
_sl5.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 315;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
int _answ = 0;
 //BA.debugLineNum = 847;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 848;BA.debugLine="Dim Answ As Int";
_answ = 0;
 //BA.debugLineNum = 849;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 850;BA.debugLine="Answ = Msgbox2(\"Do you want to Exit App?\", \"At";
_answ = anywheresoftware.b4a.keywords.Common.Msgbox2("Do you want to Exit App?","Attention!","Yes","","No",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 851;BA.debugLine="If Answ = DialogResponse.NEGATIVE Then";
if (_answ==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
 //BA.debugLineNum = 852;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 };
 //BA.debugLineNum = 855;BA.debugLine="If Answ = DialogResponse.POSITIVE Then";
if (_answ==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 856;BA.debugLine="mp.Stop";
mostCurrent._mp.Stop();
 //BA.debugLineNum = 857;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 859;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 843;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 845;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 839;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 841;BA.debugLine="End Sub";
return "";
}
public static String  _b1ani_animationend() throws Exception{
 //BA.debugLineNum = 722;BA.debugLine="Sub b1ani_animationend";
 //BA.debugLineNum = 723;BA.debugLine="b1ani.StartAnim(b1)";
mostCurrent._b1ani.StartAnim((android.view.View)(mostCurrent._b1.getObject()));
 //BA.debugLineNum = 724;BA.debugLine="End Sub";
return "";
}
public static String  _b1t_tick() throws Exception{
 //BA.debugLineNum = 613;BA.debugLine="Sub b1t_Tick";
 //BA.debugLineNum = 615;BA.debugLine="b1.Visible = True";
mostCurrent._b1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 616;BA.debugLine="b1ani.SlideFadeToBottom(\"b1ani\",1200,10000)";
mostCurrent._b1ani.SlideFadeToBottom(mostCurrent.activityBA,"b1ani",(float) (1200),(long) (10000));
 //BA.debugLineNum = 617;BA.debugLine="b1ani.StartAnim(b1)";
mostCurrent._b1ani.StartAnim((android.view.View)(mostCurrent._b1.getObject()));
 //BA.debugLineNum = 618;BA.debugLine="b1t.Enabled = False";
_b1t.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 619;BA.debugLine="End Sub";
return "";
}
public static String  _b2ani_animationend() throws Exception{
 //BA.debugLineNum = 726;BA.debugLine="Sub b2ani_animationend";
 //BA.debugLineNum = 727;BA.debugLine="b2ani.StartAnim(b2)";
mostCurrent._b2ani.StartAnim((android.view.View)(mostCurrent._b2.getObject()));
 //BA.debugLineNum = 728;BA.debugLine="End Sub";
return "";
}
public static String  _b2t_tick() throws Exception{
 //BA.debugLineNum = 621;BA.debugLine="Sub b2t_Tick";
 //BA.debugLineNum = 623;BA.debugLine="b2.Visible = True";
mostCurrent._b2.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 624;BA.debugLine="b2ani.SlideFadeToBottom(\"b2ani\",1200,10000)";
mostCurrent._b2ani.SlideFadeToBottom(mostCurrent.activityBA,"b2ani",(float) (1200),(long) (10000));
 //BA.debugLineNum = 625;BA.debugLine="b2ani.StartAnim(b2)";
mostCurrent._b2ani.StartAnim((android.view.View)(mostCurrent._b2.getObject()));
 //BA.debugLineNum = 626;BA.debugLine="b2t.Enabled = False";
_b2t.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 627;BA.debugLine="End Sub";
return "";
}
public static String  _b3ani_animationend() throws Exception{
 //BA.debugLineNum = 730;BA.debugLine="Sub b3ani_animationend";
 //BA.debugLineNum = 731;BA.debugLine="b3ani.StartAnim(b3)";
mostCurrent._b3ani.StartAnim((android.view.View)(mostCurrent._b3.getObject()));
 //BA.debugLineNum = 732;BA.debugLine="End Sub";
return "";
}
public static String  _b3t_tick() throws Exception{
 //BA.debugLineNum = 629;BA.debugLine="Sub b3t_Tick";
 //BA.debugLineNum = 631;BA.debugLine="b3.Visible = True";
mostCurrent._b3.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 632;BA.debugLine="b3ani.SlideFadeToBottom(\"b3ani\",1200,10000)";
mostCurrent._b3ani.SlideFadeToBottom(mostCurrent.activityBA,"b3ani",(float) (1200),(long) (10000));
 //BA.debugLineNum = 633;BA.debugLine="b3ani.StartAnim(b3)";
mostCurrent._b3ani.StartAnim((android.view.View)(mostCurrent._b3.getObject()));
 //BA.debugLineNum = 634;BA.debugLine="b3t.Enabled = False";
_b3t.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 635;BA.debugLine="End Sub";
return "";
}
public static String  _b4ani_animationend() throws Exception{
 //BA.debugLineNum = 734;BA.debugLine="Sub b4ani_animationend";
 //BA.debugLineNum = 735;BA.debugLine="b4ani.StartAnim(b4)";
mostCurrent._b4ani.StartAnim((android.view.View)(mostCurrent._b4.getObject()));
 //BA.debugLineNum = 736;BA.debugLine="End Sub";
return "";
}
public static String  _b4t_tick() throws Exception{
 //BA.debugLineNum = 637;BA.debugLine="Sub b4t_Tick";
 //BA.debugLineNum = 639;BA.debugLine="b4.Visible = True";
mostCurrent._b4.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 640;BA.debugLine="b4ani.SlideFadeToBottom(\"b4ani\",1200,10000)";
mostCurrent._b4ani.SlideFadeToBottom(mostCurrent.activityBA,"b4ani",(float) (1200),(long) (10000));
 //BA.debugLineNum = 641;BA.debugLine="b4ani.StartAnim(b4)";
mostCurrent._b4ani.StartAnim((android.view.View)(mostCurrent._b4.getObject()));
 //BA.debugLineNum = 642;BA.debugLine="b4t.Enabled = False";
_b4t.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 643;BA.debugLine="End Sub";
return "";
}
public static String  _b5ani_animationend() throws Exception{
 //BA.debugLineNum = 738;BA.debugLine="Sub b5ani_animationend";
 //BA.debugLineNum = 739;BA.debugLine="b5ani.StartAnim(b5)";
mostCurrent._b5ani.StartAnim((android.view.View)(mostCurrent._b5.getObject()));
 //BA.debugLineNum = 740;BA.debugLine="End Sub";
return "";
}
public static String  _b5t_tick() throws Exception{
 //BA.debugLineNum = 645;BA.debugLine="Sub b5t_Tick";
 //BA.debugLineNum = 647;BA.debugLine="b5.Visible = True";
mostCurrent._b5.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 648;BA.debugLine="b5ani.SlideFadeToBottom(\"b5ani\",1200,10000)";
mostCurrent._b5ani.SlideFadeToBottom(mostCurrent.activityBA,"b5ani",(float) (1200),(long) (10000));
 //BA.debugLineNum = 649;BA.debugLine="b5ani.StartAnim(b5)";
mostCurrent._b5ani.StartAnim((android.view.View)(mostCurrent._b5.getObject()));
 //BA.debugLineNum = 650;BA.debugLine="b5t.Enabled = False";
_b5t.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 651;BA.debugLine="End Sub";
return "";
}
public static String  _b6ani_animationend() throws Exception{
 //BA.debugLineNum = 742;BA.debugLine="Sub b6ani_animationend";
 //BA.debugLineNum = 743;BA.debugLine="b6ani.StartAnim(b6)";
mostCurrent._b6ani.StartAnim((android.view.View)(mostCurrent._b6.getObject()));
 //BA.debugLineNum = 744;BA.debugLine="End Sub";
return "";
}
public static String  _b6t_tick() throws Exception{
 //BA.debugLineNum = 653;BA.debugLine="Sub b6t_Tick";
 //BA.debugLineNum = 655;BA.debugLine="b6.Visible = True";
mostCurrent._b6.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 656;BA.debugLine="b6ani.SlideFadeToBottom(\"b6ani\",1200,10000)";
mostCurrent._b6ani.SlideFadeToBottom(mostCurrent.activityBA,"b6ani",(float) (1200),(long) (10000));
 //BA.debugLineNum = 657;BA.debugLine="b6ani.StartAnim(b6)";
mostCurrent._b6ani.StartAnim((android.view.View)(mostCurrent._b6.getObject()));
 //BA.debugLineNum = 658;BA.debugLine="b6t.Enabled = False";
_b6t.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 659;BA.debugLine="End Sub";
return "";
}
public static String  _bb_click() throws Exception{
 //BA.debugLineNum = 317;BA.debugLine="Sub bb_Click";
 //BA.debugLineNum = 318;BA.debugLine="If mp.IsPlaying = True Then";
if (mostCurrent._mp.IsPlaying()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 319;BA.debugLine="mp.Stop";
mostCurrent._mp.Stop();
 };
 //BA.debugLineNum = 321;BA.debugLine="StartActivity(Step5)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._step5.getObject()));
 //BA.debugLineNum = 322;BA.debugLine="End Sub";
return "";
}
public static String  _fb_click() throws Exception{
int _id_int = 0;
com.maximus.id.id _id = null;
anywheresoftware.b4a.objects.collections.List _lis = null;
anywheresoftware.b4a.agraham.dialogs.InputDialog.ColorDialog _cd = null;
int _i = 0;
int _idd_int = 0;
com.maximus.id.id _idd = null;
 //BA.debugLineNum = 324;BA.debugLine="Sub fb_Click";
 //BA.debugLineNum = 325;BA.debugLine="Dim id_int As Int";
_id_int = 0;
 //BA.debugLineNum = 326;BA.debugLine="Dim id As id";
_id = new com.maximus.id.id();
 //BA.debugLineNum = 327;BA.debugLine="Dim lis As List";
_lis = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 328;BA.debugLine="lis.Initialize";
_lis.Initialize();
 //BA.debugLineNum = 329;BA.debugLine="lis.AddAll(Array As String(\"Change Background Col";
_lis.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"Change Background Color","Change Theme","Play Music","Stop Music","Restart"}));
 //BA.debugLineNum = 330;BA.debugLine="id_int = id.InputList1(lis,\"Choose!\")";
_id_int = _id.InputList1(_lis,"Choose!",mostCurrent.activityBA);
 //BA.debugLineNum = 333;BA.debugLine="If id_int = 0 Then";
if (_id_int==0) { 
 //BA.debugLineNum = 334;BA.debugLine="Dim cd As ColorDialog";
_cd = new anywheresoftware.b4a.agraham.dialogs.InputDialog.ColorDialog();
 //BA.debugLineNum = 335;BA.debugLine="Dim i As Int";
_i = 0;
 //BA.debugLineNum = 336;BA.debugLine="cd.RGB = Colors.DarkGray";
_cd.setRGB(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 337;BA.debugLine="i = cd.Show(\"B4A ColorPicker Dialog\", \"Yes\", \"No\"";
_i = _cd.Show("B4A ColorPicker Dialog","Yes","No","Reset",mostCurrent.activityBA,(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 338;BA.debugLine="If i = DialogResponse.POSITIVE Then";
if (_i==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 339;BA.debugLine="Activity.Color = cd.RGB";
mostCurrent._activity.setColor(_cd.getRGB());
 };
 //BA.debugLineNum = 341;BA.debugLine="If i = DialogResponse.NEGATIVE Then";
if (_i==anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE) { 
 //BA.debugLineNum = 342;BA.debugLine="Activity.Color = \"\"";
mostCurrent._activity.setColor((int)(Double.parseDouble("")));
 };
 };
 //BA.debugLineNum = 347;BA.debugLine="If id_int = 1 Then";
if (_id_int==1) { 
 //BA.debugLineNum = 348;BA.debugLine="Dim lis As List";
_lis = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 349;BA.debugLine="Dim idd_int As Int";
_idd_int = 0;
 //BA.debugLineNum = 350;BA.debugLine="Dim idd As id";
_idd = new com.maximus.id.id();
 //BA.debugLineNum = 351;BA.debugLine="lis.Initialize";
_lis.Initialize();
 //BA.debugLineNum = 352;BA.debugLine="lis.AddAll(Array As String(\"Holo\",\"Holo Light\",\"H";
_lis.AddAll(anywheresoftware.b4a.keywords.Common.ArrayToList(new String[]{"Holo","Holo Light","Holo Light Dark","Old Android","Material","Material Light","Material Light Dark","Transparent","Transparent No Title Bar"}));
 //BA.debugLineNum = 353;BA.debugLine="idd_int = idd.InputList1(lis,\"Choose Themes!\")";
_idd_int = _idd.InputList1(_lis,"Choose Themes!",mostCurrent.activityBA);
 //BA.debugLineNum = 354;BA.debugLine="If idd_int = 0 Then";
if (_idd_int==0) { 
 //BA.debugLineNum = 355;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:sty";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Holo"));
 };
 //BA.debugLineNum = 358;BA.debugLine="If idd_int = 1 Then";
if (_idd_int==1) { 
 //BA.debugLineNum = 359;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:sty";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Holo.Light"));
 };
 //BA.debugLineNum = 362;BA.debugLine="If idd_int = 2 Then";
if (_idd_int==2) { 
 //BA.debugLineNum = 363;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:sty";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Holo.Light.DarkActionBar"));
 };
 //BA.debugLineNum = 366;BA.debugLine="If idd_int = 3 Then";
if (_idd_int==3) { 
 //BA.debugLineNum = 367;BA.debugLine="SetTheme(16973829)";
_settheme((int) (16973829));
 };
 //BA.debugLineNum = 370;BA.debugLine="If idd_int = 4 Then";
if (_idd_int==4) { 
 //BA.debugLineNum = 371;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:sty";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Material"));
 };
 //BA.debugLineNum = 374;BA.debugLine="If idd_int = 5 Then";
if (_idd_int==5) { 
 //BA.debugLineNum = 375;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:sty";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Material.Light"));
 };
 //BA.debugLineNum = 378;BA.debugLine="If idd_int = 6 Then";
if (_idd_int==6) { 
 //BA.debugLineNum = 379;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:sty";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Material.Light.DarkActionBar"));
 };
 //BA.debugLineNum = 382;BA.debugLine="If idd_int = 7 Then";
if (_idd_int==7) { 
 //BA.debugLineNum = 383;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:styl";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Translucent"));
 };
 //BA.debugLineNum = 386;BA.debugLine="If idd_int = 8 Then";
if (_idd_int==8) { 
 //BA.debugLineNum = 387;BA.debugLine="SetTheme(res.GetResourceId(\"style\", \"android:styl";
_settheme(mostCurrent._res.GetResourceId("style","android:style/Theme.Translucent.NoTitleBar"));
 };
 };
 //BA.debugLineNum = 392;BA.debugLine="If id_int = 2 Then";
if (_id_int==2) { 
 //BA.debugLineNum = 393;BA.debugLine="If mp.IsPlaying = False Then";
if (mostCurrent._mp.IsPlaying()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 394;BA.debugLine="mp.Load(File.DirAssets,\"birthday.mp3\")";
mostCurrent._mp.Load(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"birthday.mp3");
 //BA.debugLineNum = 395;BA.debugLine="mp.Play";
mostCurrent._mp.Play();
 };
 };
 //BA.debugLineNum = 400;BA.debugLine="If id_int = 3 Then";
if (_id_int==3) { 
 //BA.debugLineNum = 401;BA.debugLine="If mp.IsPlaying = True Then";
if (mostCurrent._mp.IsPlaying()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 402;BA.debugLine="mp.Stop";
mostCurrent._mp.Stop();
 };
 };
 //BA.debugLineNum = 407;BA.debugLine="If id_int = 4 Then";
if (_id_int==4) { 
 //BA.debugLineNum = 408;BA.debugLine="If mp.IsPlaying = True Then";
if (mostCurrent._mp.IsPlaying()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 409;BA.debugLine="mp.Stop";
mostCurrent._mp.Stop();
 };
 //BA.debugLineNum = 411;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 412;BA.debugLine="StartActivity(Me)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,preview.getObject());
 };
 //BA.debugLineNum = 414;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 14;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 15;BA.debugLine="Dim b1, b2, b3, b4, b5, b6 As ImageView";
mostCurrent._b1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._b2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._b3 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._b4 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._b5 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._b6 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 16;BA.debugLine="Dim b1bg, b2bg, b3bg, b4bg, b5bg, b6bg As BitmapD";
mostCurrent._b1bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._b2bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._b3bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._b4bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._b5bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._b6bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 17;BA.debugLine="Dim b1ani, b2ani, b3ani, b4ani, b5ani, b6ani As I";
mostCurrent._b1ani = new giuseppe.salvi.icos.library.ICOSSlideAnimation();
mostCurrent._b2ani = new giuseppe.salvi.icos.library.ICOSSlideAnimation();
mostCurrent._b3ani = new giuseppe.salvi.icos.library.ICOSSlideAnimation();
mostCurrent._b4ani = new giuseppe.salvi.icos.library.ICOSSlideAnimation();
mostCurrent._b5ani = new giuseppe.salvi.icos.library.ICOSSlideAnimation();
mostCurrent._b6ani = new giuseppe.salvi.icos.library.ICOSSlideAnimation();
 //BA.debugLineNum = 19;BA.debugLine="Dim p1,p2,p3,p4,p5 As ImageView";
mostCurrent._p1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._p2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._p3 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._p4 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._p5 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Dim p1bg,p2bg,p3bg,p4bg,p5bg As BitmapDrawable";
mostCurrent._p1bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._p2bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._p3bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._p4bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._p5bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 22;BA.debugLine="Dim img1,img2,img3,img4 As ImageView";
mostCurrent._img1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._img2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._img3 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._img4 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Dim img1bg,img2bg,img3bg,img4bg As BitmapDrawable";
mostCurrent._img1bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._img2bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._img3bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._img4bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 24;BA.debugLine="Dim ani2,ani3,ani4 As AnimationPlus";
mostCurrent._ani2 = new flm.b4a.animationplus.AnimationPlusWrapper();
mostCurrent._ani3 = new flm.b4a.animationplus.AnimationPlusWrapper();
mostCurrent._ani4 = new flm.b4a.animationplus.AnimationPlusWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Dim bw As Label";
mostCurrent._bw = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Dim bdtext As AnimateText";
mostCurrent._bdtext = new com.htoophyoe.anitext.animatetext();
 //BA.debugLineNum = 28;BA.debugLine="Dim i1,i2,i3,i4,i5,i6,i7,i8,i9,i10,i11,i12,i13,i14";
mostCurrent._i1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._i2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._i3 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._i4 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._i5 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._i6 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._i7 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._i8 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._i9 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._i10 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._i11 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._i12 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._i13 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._i14 = new anywheresoftware.b4a.objects.ImageViewWrapper();
mostCurrent._i15 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Dim i1bg,i2bg,i3bg,i4bg,i5bg,i6bg,i7bg,i8bg,i9bg,i";
mostCurrent._i1bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._i2bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._i3bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._i4bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._i5bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._i6bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._i7bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._i8bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._i9bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._i10bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._i11bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._i12bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._i13bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._i14bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
mostCurrent._i15bg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 30;BA.debugLine="Dim tf As Typeface";
mostCurrent._tf = new anywheresoftware.b4a.keywords.constants.TypefaceWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Dim i1ani, i2ani, i3ani, i4ani, i5ani, i6ani, i7an";
mostCurrent._i1ani = new giuseppe.salvi.icos.library.ICOSSlideAnimation();
mostCurrent._i2ani = new giuseppe.salvi.icos.library.ICOSSlideAnimation();
mostCurrent._i3ani = new giuseppe.salvi.icos.library.ICOSSlideAnimation();
mostCurrent._i4ani = new giuseppe.salvi.icos.library.ICOSSlideAnimation();
mostCurrent._i5ani = new giuseppe.salvi.icos.library.ICOSSlideAnimation();
mostCurrent._i6ani = new giuseppe.salvi.icos.library.ICOSSlideAnimation();
mostCurrent._i7ani = new giuseppe.salvi.icos.library.ICOSSlideAnimation();
mostCurrent._i8ani = new giuseppe.salvi.icos.library.ICOSSlideAnimation();
mostCurrent._i9ani = new giuseppe.salvi.icos.library.ICOSSlideAnimation();
mostCurrent._i10ani = new giuseppe.salvi.icos.library.ICOSSlideAnimation();
mostCurrent._i11ani = new giuseppe.salvi.icos.library.ICOSSlideAnimation();
mostCurrent._i12ani = new giuseppe.salvi.icos.library.ICOSSlideAnimation();
mostCurrent._i13ani = new giuseppe.salvi.icos.library.ICOSSlideAnimation();
mostCurrent._i14ani = new giuseppe.salvi.icos.library.ICOSSlideAnimation();
mostCurrent._i15ani = new giuseppe.salvi.icos.library.ICOSSlideAnimation();
 //BA.debugLineNum = 32;BA.debugLine="Dim mp As MediaPlayer";
mostCurrent._mp = new anywheresoftware.b4a.objects.MediaPlayerWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Dim fb As FloatingActionButton";
mostCurrent._fb = new de.amberhome.objects.floatingactionbutton.FloatingActionButtonWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Dim mbg As BitmapDrawable";
mostCurrent._mbg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 35;BA.debugLine="Dim res As XmlLayoutBuilder";
mostCurrent._res = new anywheresoftware.b4a.object.XmlLayoutBuilder();
 //BA.debugLineNum = 36;BA.debugLine="Dim bb As Button";
mostCurrent._bb = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Dim p As Phone";
mostCurrent._p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 38;BA.debugLine="End Sub";
return "";
}
public static String  _i10ani_animationend() throws Exception{
 //BA.debugLineNum = 698;BA.debugLine="Sub i10ani_animationend";
 //BA.debugLineNum = 699;BA.debugLine="i10ani.StartAnim(i10)";
mostCurrent._i10ani.StartAnim((android.view.View)(mostCurrent._i10.getObject()));
 //BA.debugLineNum = 700;BA.debugLine="End Sub";
return "";
}
public static String  _i11ani_animationend() throws Exception{
 //BA.debugLineNum = 702;BA.debugLine="Sub i11ani_animationend";
 //BA.debugLineNum = 703;BA.debugLine="i11ani.StartAnim(i11)";
mostCurrent._i11ani.StartAnim((android.view.View)(mostCurrent._i11.getObject()));
 //BA.debugLineNum = 704;BA.debugLine="End Sub";
return "";
}
public static String  _i12ani_animationend() throws Exception{
 //BA.debugLineNum = 706;BA.debugLine="Sub i12ani_animationend";
 //BA.debugLineNum = 707;BA.debugLine="i12ani.StartAnim(i12)";
mostCurrent._i12ani.StartAnim((android.view.View)(mostCurrent._i12.getObject()));
 //BA.debugLineNum = 708;BA.debugLine="End Sub";
return "";
}
public static String  _i13ani_animationend() throws Exception{
 //BA.debugLineNum = 710;BA.debugLine="Sub i13ani_animationend";
 //BA.debugLineNum = 711;BA.debugLine="i13ani.StartAnim(i13)";
mostCurrent._i13ani.StartAnim((android.view.View)(mostCurrent._i13.getObject()));
 //BA.debugLineNum = 712;BA.debugLine="End Sub";
return "";
}
public static String  _i14ani_animationend() throws Exception{
 //BA.debugLineNum = 714;BA.debugLine="Sub i14ani_animationend";
 //BA.debugLineNum = 715;BA.debugLine="i14ani.StartAnim(i14)";
mostCurrent._i14ani.StartAnim((android.view.View)(mostCurrent._i14.getObject()));
 //BA.debugLineNum = 716;BA.debugLine="End Sub";
return "";
}
public static String  _i15ani_animationend() throws Exception{
 //BA.debugLineNum = 718;BA.debugLine="Sub i15ani_animationend";
 //BA.debugLineNum = 719;BA.debugLine="i15ani.StartAnim(i15)";
mostCurrent._i15ani.StartAnim((android.view.View)(mostCurrent._i15.getObject()));
 //BA.debugLineNum = 720;BA.debugLine="End Sub";
return "";
}
public static String  _i1ani_animationend() throws Exception{
 //BA.debugLineNum = 662;BA.debugLine="Sub i1ani_animationend";
 //BA.debugLineNum = 663;BA.debugLine="i1ani.StartAnim(i1)";
mostCurrent._i1ani.StartAnim((android.view.View)(mostCurrent._i1.getObject()));
 //BA.debugLineNum = 664;BA.debugLine="End Sub";
return "";
}
public static String  _i2ani_animationend() throws Exception{
 //BA.debugLineNum = 666;BA.debugLine="Sub i2ani_animationend";
 //BA.debugLineNum = 667;BA.debugLine="i2ani.StartAnim(i2)";
mostCurrent._i2ani.StartAnim((android.view.View)(mostCurrent._i2.getObject()));
 //BA.debugLineNum = 668;BA.debugLine="End Sub";
return "";
}
public static String  _i3ani_animationend() throws Exception{
 //BA.debugLineNum = 670;BA.debugLine="Sub i3ani_animationend";
 //BA.debugLineNum = 671;BA.debugLine="i3ani.StartAnim(i3)";
mostCurrent._i3ani.StartAnim((android.view.View)(mostCurrent._i3.getObject()));
 //BA.debugLineNum = 672;BA.debugLine="End Sub";
return "";
}
public static String  _i4ani_animationend() throws Exception{
 //BA.debugLineNum = 674;BA.debugLine="Sub i4ani_animationend";
 //BA.debugLineNum = 675;BA.debugLine="i4ani.StartAnim(i4)";
mostCurrent._i4ani.StartAnim((android.view.View)(mostCurrent._i4.getObject()));
 //BA.debugLineNum = 676;BA.debugLine="End Sub";
return "";
}
public static String  _i5ani_animationend() throws Exception{
 //BA.debugLineNum = 678;BA.debugLine="Sub i5ani_animationend";
 //BA.debugLineNum = 679;BA.debugLine="i5ani.StartAnim(i5)";
mostCurrent._i5ani.StartAnim((android.view.View)(mostCurrent._i5.getObject()));
 //BA.debugLineNum = 680;BA.debugLine="End Sub";
return "";
}
public static String  _i6ani_animationend() throws Exception{
 //BA.debugLineNum = 682;BA.debugLine="Sub i6ani_animationend";
 //BA.debugLineNum = 683;BA.debugLine="i6ani.StartAnim(i6)";
mostCurrent._i6ani.StartAnim((android.view.View)(mostCurrent._i6.getObject()));
 //BA.debugLineNum = 684;BA.debugLine="End Sub";
return "";
}
public static String  _i7ani_animationend() throws Exception{
 //BA.debugLineNum = 686;BA.debugLine="Sub i7ani_animationend";
 //BA.debugLineNum = 687;BA.debugLine="i7ani.StartAnim(i7)";
mostCurrent._i7ani.StartAnim((android.view.View)(mostCurrent._i7.getObject()));
 //BA.debugLineNum = 688;BA.debugLine="End Sub";
return "";
}
public static String  _i8ani_animationend() throws Exception{
 //BA.debugLineNum = 690;BA.debugLine="Sub i8ani_animationend";
 //BA.debugLineNum = 691;BA.debugLine="i8ani.StartAnim(i8)";
mostCurrent._i8ani.StartAnim((android.view.View)(mostCurrent._i8.getObject()));
 //BA.debugLineNum = 692;BA.debugLine="End Sub";
return "";
}
public static String  _i9ani_animationend() throws Exception{
 //BA.debugLineNum = 694;BA.debugLine="Sub i9ani_animationend";
 //BA.debugLineNum = 695;BA.debugLine="i9ani.StartAnim(i9)";
mostCurrent._i9ani.StartAnim((android.view.View)(mostCurrent._i9.getObject()));
 //BA.debugLineNum = 696;BA.debugLine="End Sub";
return "";
}
public static String  _img2_click() throws Exception{
 //BA.debugLineNum = 750;BA.debugLine="Sub img2_Click";
 //BA.debugLineNum = 751;BA.debugLine="img2.Visible = False";
mostCurrent._img2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 752;BA.debugLine="p1.Visible = True";
mostCurrent._p1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 753;BA.debugLine="sl1.Enabled = False";
_sl1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 754;BA.debugLine="sl2.Enabled = False";
_sl2.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 755;BA.debugLine="sl3.Enabled = False";
_sl3.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 756;BA.debugLine="sl4.Enabled = False";
_sl4.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 757;BA.debugLine="End Sub";
return "";
}
public static String  _it1_tick() throws Exception{
 //BA.debugLineNum = 493;BA.debugLine="Sub it1_Tick";
 //BA.debugLineNum = 495;BA.debugLine="i1.Visible = True";
mostCurrent._i1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 496;BA.debugLine="i1ani.SlideFadeToBottom(\"i1ani\",1200,10000)";
mostCurrent._i1ani.SlideFadeToBottom(mostCurrent.activityBA,"i1ani",(float) (1200),(long) (10000));
 //BA.debugLineNum = 497;BA.debugLine="i1ani.StartAnim(i1)";
mostCurrent._i1ani.StartAnim((android.view.View)(mostCurrent._i1.getObject()));
 //BA.debugLineNum = 498;BA.debugLine="it1.Enabled = False";
_it1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 499;BA.debugLine="End Sub";
return "";
}
public static String  _it10_tick() throws Exception{
 //BA.debugLineNum = 565;BA.debugLine="Sub it10_Tick";
 //BA.debugLineNum = 567;BA.debugLine="i10.Visible = True";
mostCurrent._i10.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 568;BA.debugLine="i10ani.SlideFadeToBottom(\"i10ani\",1200,10000)";
mostCurrent._i10ani.SlideFadeToBottom(mostCurrent.activityBA,"i10ani",(float) (1200),(long) (10000));
 //BA.debugLineNum = 569;BA.debugLine="i10ani.StartAnim(i10)";
mostCurrent._i10ani.StartAnim((android.view.View)(mostCurrent._i10.getObject()));
 //BA.debugLineNum = 570;BA.debugLine="it10.Enabled = False";
_it10.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 571;BA.debugLine="End Sub";
return "";
}
public static String  _it11_tick() throws Exception{
 //BA.debugLineNum = 573;BA.debugLine="Sub it11_Tick";
 //BA.debugLineNum = 575;BA.debugLine="i11.Visible = True";
mostCurrent._i11.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 576;BA.debugLine="i11ani.SlideFadeToBottom(\"i11ani\",1200,10000)";
mostCurrent._i11ani.SlideFadeToBottom(mostCurrent.activityBA,"i11ani",(float) (1200),(long) (10000));
 //BA.debugLineNum = 577;BA.debugLine="i11ani.StartAnim(i11)";
mostCurrent._i11ani.StartAnim((android.view.View)(mostCurrent._i11.getObject()));
 //BA.debugLineNum = 578;BA.debugLine="it11.Enabled = False";
_it11.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 579;BA.debugLine="End Sub";
return "";
}
public static String  _it12_tick() throws Exception{
 //BA.debugLineNum = 581;BA.debugLine="Sub it12_Tick";
 //BA.debugLineNum = 583;BA.debugLine="i12.Visible = True";
mostCurrent._i12.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 584;BA.debugLine="i12ani.SlideFadeToBottom(\"i12ani\",1200,10000)";
mostCurrent._i12ani.SlideFadeToBottom(mostCurrent.activityBA,"i12ani",(float) (1200),(long) (10000));
 //BA.debugLineNum = 585;BA.debugLine="i12ani.StartAnim(i12)";
mostCurrent._i12ani.StartAnim((android.view.View)(mostCurrent._i12.getObject()));
 //BA.debugLineNum = 586;BA.debugLine="it12.Enabled = False";
_it12.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 587;BA.debugLine="End Sub";
return "";
}
public static String  _it13_tick() throws Exception{
 //BA.debugLineNum = 589;BA.debugLine="Sub it13_Tick";
 //BA.debugLineNum = 591;BA.debugLine="i13.Visible = True";
mostCurrent._i13.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 592;BA.debugLine="i13ani.SlideFadeToBottom(\"i13ani\",1200,10000)";
mostCurrent._i13ani.SlideFadeToBottom(mostCurrent.activityBA,"i13ani",(float) (1200),(long) (10000));
 //BA.debugLineNum = 593;BA.debugLine="i13ani.StartAnim(i13)";
mostCurrent._i13ani.StartAnim((android.view.View)(mostCurrent._i13.getObject()));
 //BA.debugLineNum = 594;BA.debugLine="it13.Enabled = False";
_it13.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 595;BA.debugLine="End Sub";
return "";
}
public static String  _it14_tick() throws Exception{
 //BA.debugLineNum = 597;BA.debugLine="Sub it14_Tick";
 //BA.debugLineNum = 599;BA.debugLine="i14.Visible = True";
mostCurrent._i14.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 600;BA.debugLine="i14ani.SlideFadeToBottom(\"i14ani\",1200,10000)";
mostCurrent._i14ani.SlideFadeToBottom(mostCurrent.activityBA,"i14ani",(float) (1200),(long) (10000));
 //BA.debugLineNum = 601;BA.debugLine="i14ani.StartAnim(i14)";
mostCurrent._i14ani.StartAnim((android.view.View)(mostCurrent._i14.getObject()));
 //BA.debugLineNum = 602;BA.debugLine="it14.Enabled = False";
_it14.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 603;BA.debugLine="End Sub";
return "";
}
public static String  _it15_tick() throws Exception{
 //BA.debugLineNum = 605;BA.debugLine="Sub it15_Tick";
 //BA.debugLineNum = 607;BA.debugLine="i15.Visible = True";
mostCurrent._i15.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 608;BA.debugLine="i15ani.SlideFadeToBottom(\"i15ani\",1200,10000)";
mostCurrent._i15ani.SlideFadeToBottom(mostCurrent.activityBA,"i15ani",(float) (1200),(long) (10000));
 //BA.debugLineNum = 609;BA.debugLine="i15ani.StartAnim(i15)";
mostCurrent._i15ani.StartAnim((android.view.View)(mostCurrent._i15.getObject()));
 //BA.debugLineNum = 610;BA.debugLine="it15.Enabled = False";
_it15.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 611;BA.debugLine="End Sub";
return "";
}
public static String  _it2_tick() throws Exception{
 //BA.debugLineNum = 501;BA.debugLine="Sub it2_Tick";
 //BA.debugLineNum = 503;BA.debugLine="i2.Visible = True";
mostCurrent._i2.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 504;BA.debugLine="i2ani.SlideFadeToBottom(\"i2ani\",1200,10000)";
mostCurrent._i2ani.SlideFadeToBottom(mostCurrent.activityBA,"i2ani",(float) (1200),(long) (10000));
 //BA.debugLineNum = 505;BA.debugLine="i2ani.StartAnim(i2)";
mostCurrent._i2ani.StartAnim((android.view.View)(mostCurrent._i2.getObject()));
 //BA.debugLineNum = 506;BA.debugLine="it2.Enabled = False";
_it2.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 507;BA.debugLine="End Sub";
return "";
}
public static String  _it3_tick() throws Exception{
 //BA.debugLineNum = 509;BA.debugLine="Sub it3_Tick";
 //BA.debugLineNum = 511;BA.debugLine="i3.Visible = True";
mostCurrent._i3.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 512;BA.debugLine="i3ani.SlideFadeToBottom(\"i3ani\",1200,10000)";
mostCurrent._i3ani.SlideFadeToBottom(mostCurrent.activityBA,"i3ani",(float) (1200),(long) (10000));
 //BA.debugLineNum = 513;BA.debugLine="i3ani.StartAnim(i3)";
mostCurrent._i3ani.StartAnim((android.view.View)(mostCurrent._i3.getObject()));
 //BA.debugLineNum = 514;BA.debugLine="it3.Enabled = False";
_it3.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 515;BA.debugLine="End Sub";
return "";
}
public static String  _it4_tick() throws Exception{
 //BA.debugLineNum = 517;BA.debugLine="Sub it4_Tick";
 //BA.debugLineNum = 519;BA.debugLine="i4.Visible = True";
mostCurrent._i4.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 520;BA.debugLine="i4ani.SlideFadeToBottom(\"i4ani\",1200,10000)";
mostCurrent._i4ani.SlideFadeToBottom(mostCurrent.activityBA,"i4ani",(float) (1200),(long) (10000));
 //BA.debugLineNum = 521;BA.debugLine="i4ani.StartAnim(i4)";
mostCurrent._i4ani.StartAnim((android.view.View)(mostCurrent._i4.getObject()));
 //BA.debugLineNum = 522;BA.debugLine="it4.Enabled = False";
_it4.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 523;BA.debugLine="End Sub";
return "";
}
public static String  _it5_tick() throws Exception{
 //BA.debugLineNum = 525;BA.debugLine="Sub it5_Tick";
 //BA.debugLineNum = 527;BA.debugLine="i5.Visible = True";
mostCurrent._i5.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 528;BA.debugLine="i5ani.SlideFadeToBottom(\"i5ani\",1200,10000)";
mostCurrent._i5ani.SlideFadeToBottom(mostCurrent.activityBA,"i5ani",(float) (1200),(long) (10000));
 //BA.debugLineNum = 529;BA.debugLine="i5ani.StartAnim(i5)";
mostCurrent._i5ani.StartAnim((android.view.View)(mostCurrent._i5.getObject()));
 //BA.debugLineNum = 530;BA.debugLine="it5.Enabled = False";
_it5.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 531;BA.debugLine="End Sub";
return "";
}
public static String  _it6_tick() throws Exception{
 //BA.debugLineNum = 533;BA.debugLine="Sub it6_Tick";
 //BA.debugLineNum = 535;BA.debugLine="i6.Visible = True";
mostCurrent._i6.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 536;BA.debugLine="i6ani.SlideFadeToBottom(\"i6ani\",1200,10000)";
mostCurrent._i6ani.SlideFadeToBottom(mostCurrent.activityBA,"i6ani",(float) (1200),(long) (10000));
 //BA.debugLineNum = 537;BA.debugLine="i6ani.StartAnim(i6)";
mostCurrent._i6ani.StartAnim((android.view.View)(mostCurrent._i6.getObject()));
 //BA.debugLineNum = 538;BA.debugLine="it6.Enabled = False";
_it6.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 539;BA.debugLine="End Sub";
return "";
}
public static String  _it7_tick() throws Exception{
 //BA.debugLineNum = 541;BA.debugLine="Sub it7_Tick";
 //BA.debugLineNum = 543;BA.debugLine="i7.Visible = True";
mostCurrent._i7.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 544;BA.debugLine="i7ani.SlideFadeToBottom(\"i7ani\",1200,10000)";
mostCurrent._i7ani.SlideFadeToBottom(mostCurrent.activityBA,"i7ani",(float) (1200),(long) (10000));
 //BA.debugLineNum = 545;BA.debugLine="i7ani.StartAnim(i7)";
mostCurrent._i7ani.StartAnim((android.view.View)(mostCurrent._i7.getObject()));
 //BA.debugLineNum = 546;BA.debugLine="it7.Enabled = False";
_it7.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 547;BA.debugLine="End Sub";
return "";
}
public static String  _it8_tick() throws Exception{
 //BA.debugLineNum = 549;BA.debugLine="Sub it8_Tick";
 //BA.debugLineNum = 551;BA.debugLine="i8.Visible = True";
mostCurrent._i8.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 552;BA.debugLine="i8ani.SlideFadeToBottom(\"i8ani\",1200,10000)";
mostCurrent._i8ani.SlideFadeToBottom(mostCurrent.activityBA,"i8ani",(float) (1200),(long) (10000));
 //BA.debugLineNum = 553;BA.debugLine="i8ani.StartAnim(i8)";
mostCurrent._i8ani.StartAnim((android.view.View)(mostCurrent._i8.getObject()));
 //BA.debugLineNum = 554;BA.debugLine="it8.Enabled = False";
_it8.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 555;BA.debugLine="End Sub";
return "";
}
public static String  _it9_tick() throws Exception{
 //BA.debugLineNum = 557;BA.debugLine="Sub it9_Tick";
 //BA.debugLineNum = 559;BA.debugLine="i9.Visible = True";
mostCurrent._i9.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 560;BA.debugLine="i9ani.SlideFadeToBottom(\"i9ani\",1200,10000)";
mostCurrent._i9ani.SlideFadeToBottom(mostCurrent.activityBA,"i9ani",(float) (1200),(long) (10000));
 //BA.debugLineNum = 561;BA.debugLine="i9ani.StartAnim(i9)";
mostCurrent._i9ani.StartAnim((android.view.View)(mostCurrent._i9.getObject()));
 //BA.debugLineNum = 562;BA.debugLine="it9.Enabled = False";
_it9.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 563;BA.debugLine="End Sub";
return "";
}
public static String  _p1_click() throws Exception{
 //BA.debugLineNum = 759;BA.debugLine="Sub p1_Click";
 //BA.debugLineNum = 760;BA.debugLine="p1.Visible = False";
mostCurrent._p1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 761;BA.debugLine="p2.Visible = True";
mostCurrent._p2.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 762;BA.debugLine="sl1.Enabled = False";
_sl1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 763;BA.debugLine="sl2.Enabled = False";
_sl2.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 764;BA.debugLine="sl3.Enabled = False";
_sl3.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 765;BA.debugLine="sl4.Enabled = False";
_sl4.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 766;BA.debugLine="sl5.Enabled = False";
_sl5.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 767;BA.debugLine="End Sub";
return "";
}
public static String  _p2_click() throws Exception{
 //BA.debugLineNum = 769;BA.debugLine="Sub p2_Click";
 //BA.debugLineNum = 770;BA.debugLine="p2.Visible = False";
mostCurrent._p2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 771;BA.debugLine="p3.Visible = True";
mostCurrent._p3.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 772;BA.debugLine="sl1.Enabled = False";
_sl1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 773;BA.debugLine="sl2.Enabled = False";
_sl2.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 774;BA.debugLine="sl3.Enabled = False";
_sl3.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 775;BA.debugLine="sl4.Enabled = False";
_sl4.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 776;BA.debugLine="sl5.Enabled = False";
_sl5.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 777;BA.debugLine="End Sub";
return "";
}
public static String  _p3_click() throws Exception{
 //BA.debugLineNum = 779;BA.debugLine="Sub p3_Click";
 //BA.debugLineNum = 780;BA.debugLine="p3.Visible = False";
mostCurrent._p3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 781;BA.debugLine="p4.Visible = True";
mostCurrent._p4.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 782;BA.debugLine="sl1.Enabled = False";
_sl1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 783;BA.debugLine="sl2.Enabled = False";
_sl2.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 784;BA.debugLine="sl3.Enabled = False";
_sl3.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 785;BA.debugLine="sl4.Enabled = False";
_sl4.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 786;BA.debugLine="sl5.Enabled = False";
_sl5.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 787;BA.debugLine="End Sub";
return "";
}
public static String  _p4_click() throws Exception{
 //BA.debugLineNum = 789;BA.debugLine="Sub p4_Click";
 //BA.debugLineNum = 790;BA.debugLine="p4.Visible = False";
mostCurrent._p4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 791;BA.debugLine="p5.Visible = True";
mostCurrent._p5.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 792;BA.debugLine="sl1.Enabled = False";
_sl1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 793;BA.debugLine="sl2.Enabled = False";
_sl2.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 794;BA.debugLine="sl3.Enabled = False";
_sl3.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 795;BA.debugLine="sl4.Enabled = False";
_sl4.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 796;BA.debugLine="sl5.Enabled = False";
_sl5.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 797;BA.debugLine="End Sub";
return "";
}
public static String  _p5_click() throws Exception{
 //BA.debugLineNum = 799;BA.debugLine="Sub p5_Click";
 //BA.debugLineNum = 800;BA.debugLine="p5.Visible = False";
mostCurrent._p5.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 801;BA.debugLine="img2.Visible = True";
mostCurrent._img2.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 802;BA.debugLine="sl1.Enabled = False";
_sl1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 803;BA.debugLine="sl2.Enabled = False";
_sl2.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 804;BA.debugLine="sl3.Enabled = False";
_sl3.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 805;BA.debugLine="sl4.Enabled = False";
_sl4.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 806;BA.debugLine="sl5.Enabled = False";
_sl5.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 807;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 7;BA.debugLine="Dim t1,t3,t4 As Timer";
_t1 = new anywheresoftware.b4a.objects.Timer();
_t3 = new anywheresoftware.b4a.objects.Timer();
_t4 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 8;BA.debugLine="Dim it1,it2,it3,it4,it5,it6,it7,it8,it9,it10,it11,";
_it1 = new anywheresoftware.b4a.objects.Timer();
_it2 = new anywheresoftware.b4a.objects.Timer();
_it3 = new anywheresoftware.b4a.objects.Timer();
_it4 = new anywheresoftware.b4a.objects.Timer();
_it5 = new anywheresoftware.b4a.objects.Timer();
_it6 = new anywheresoftware.b4a.objects.Timer();
_it7 = new anywheresoftware.b4a.objects.Timer();
_it8 = new anywheresoftware.b4a.objects.Timer();
_it9 = new anywheresoftware.b4a.objects.Timer();
_it10 = new anywheresoftware.b4a.objects.Timer();
_it11 = new anywheresoftware.b4a.objects.Timer();
_it12 = new anywheresoftware.b4a.objects.Timer();
_it13 = new anywheresoftware.b4a.objects.Timer();
_it14 = new anywheresoftware.b4a.objects.Timer();
_it15 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 9;BA.debugLine="Dim b1t, b2t, b3t, b4t, b5t, b6t As Timer";
_b1t = new anywheresoftware.b4a.objects.Timer();
_b2t = new anywheresoftware.b4a.objects.Timer();
_b3t = new anywheresoftware.b4a.objects.Timer();
_b4t = new anywheresoftware.b4a.objects.Timer();
_b5t = new anywheresoftware.b4a.objects.Timer();
_b6t = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 10;BA.debugLine="Dim sl1,sl2,sl3,sl4,sl5 As Timer";
_sl1 = new anywheresoftware.b4a.objects.Timer();
_sl2 = new anywheresoftware.b4a.objects.Timer();
_sl3 = new anywheresoftware.b4a.objects.Timer();
_sl4 = new anywheresoftware.b4a.objects.Timer();
_sl5 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 11;BA.debugLine="Dim Theme_Value As Int";
_theme_value = 0;
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
return "";
}
public static String  _settheme(int _theme) throws Exception{
 //BA.debugLineNum = 416;BA.debugLine="Private Sub SetTheme (Theme As Int)";
 //BA.debugLineNum = 417;BA.debugLine="If Theme = 0 Then";
if (_theme==0) { 
 //BA.debugLineNum = 418;BA.debugLine="ToastMessageShow(\"Theme not available.\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Theme not available.",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 419;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 421;BA.debugLine="If Theme = Theme_Value Then Return";
if (_theme==_theme_value) { 
if (true) return "";};
 //BA.debugLineNum = 422;BA.debugLine="Theme_Value = Theme";
_theme_value = _theme;
 //BA.debugLineNum = 423;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 424;BA.debugLine="StartActivity(Me)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,preview.getObject());
 //BA.debugLineNum = 425;BA.debugLine="End Sub";
return "";
}
public static String  _sl1_tick() throws Exception{
 //BA.debugLineNum = 809;BA.debugLine="Sub sl1_Tick";
 //BA.debugLineNum = 810;BA.debugLine="img2.Visible = False";
mostCurrent._img2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 811;BA.debugLine="p1.Visible = True";
mostCurrent._p1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 812;BA.debugLine="sl1.Enabled = False";
_sl1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 813;BA.debugLine="End Sub";
return "";
}
public static String  _sl2_tick() throws Exception{
 //BA.debugLineNum = 815;BA.debugLine="Sub sl2_Tick";
 //BA.debugLineNum = 816;BA.debugLine="p1.Visible = False";
mostCurrent._p1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 817;BA.debugLine="p2.Visible = True";
mostCurrent._p2.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 818;BA.debugLine="sl2.Enabled = False";
_sl2.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 819;BA.debugLine="End Sub";
return "";
}
public static String  _sl3_tick() throws Exception{
 //BA.debugLineNum = 821;BA.debugLine="Sub sl3_Tick";
 //BA.debugLineNum = 822;BA.debugLine="p2.Visible = False";
mostCurrent._p2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 823;BA.debugLine="p3.Visible = True";
mostCurrent._p3.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 824;BA.debugLine="sl3.Enabled = False";
_sl3.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 825;BA.debugLine="End Sub";
return "";
}
public static String  _sl4_tick() throws Exception{
 //BA.debugLineNum = 827;BA.debugLine="Sub sl4_Tick";
 //BA.debugLineNum = 828;BA.debugLine="p3.Visible = False";
mostCurrent._p3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 829;BA.debugLine="p4.Visible = True";
mostCurrent._p4.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 830;BA.debugLine="sl4.Enabled = False";
_sl4.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 831;BA.debugLine="End Sub";
return "";
}
public static String  _sl5_tick() throws Exception{
 //BA.debugLineNum = 833;BA.debugLine="Sub sl5_Tick";
 //BA.debugLineNum = 834;BA.debugLine="p4.Visible = False";
mostCurrent._p4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 835;BA.debugLine="p5.Visible = True";
mostCurrent._p5.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 836;BA.debugLine="sl5.Enabled = False";
_sl5.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 837;BA.debugLine="End Sub";
return "";
}
public static String  _sm_click() throws Exception{
 //BA.debugLineNum = 746;BA.debugLine="Sub sm_Click";
 //BA.debugLineNum = 747;BA.debugLine="mp.Stop";
mostCurrent._mp.Stop();
 //BA.debugLineNum = 748;BA.debugLine="End Sub";
return "";
}
public static String  _t1_tick() throws Exception{
 //BA.debugLineNum = 437;BA.debugLine="Sub t1_Tick";
 //BA.debugLineNum = 438;BA.debugLine="img1.Visible = True";
mostCurrent._img1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 439;BA.debugLine="t1.Enabled = False";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 440;BA.debugLine="t3.Enabled = True";
_t3.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 441;BA.debugLine="End Sub";
return "";
}
public static String  _t3_tick() throws Exception{
 //BA.debugLineNum = 443;BA.debugLine="Sub t3_Tick";
 //BA.debugLineNum = 445;BA.debugLine="img3.Visible = True";
mostCurrent._img3.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 446;BA.debugLine="ani3.InitializeRotateCenter(\"ani3\", 0, 360, img3)";
mostCurrent._ani3.InitializeRotateCenter(mostCurrent.activityBA,"ani3",(float) (0),(float) (360),(android.view.View)(mostCurrent._img3.getObject()));
 //BA.debugLineNum = 447;BA.debugLine="ani3.Duration = 1500";
mostCurrent._ani3.setDuration((long) (1500));
 //BA.debugLineNum = 448;BA.debugLine="ani3.RepeatCount = 2";
mostCurrent._ani3.setRepeatCount((int) (2));
 //BA.debugLineNum = 449;BA.debugLine="ani3.SetInterpolator(ani3.INTERPOLATOR_BOUNCE)";
mostCurrent._ani3.SetInterpolator(mostCurrent._ani3.INTERPOLATOR_BOUNCE);
 //BA.debugLineNum = 450;BA.debugLine="ani3.Start(img3)";
mostCurrent._ani3.Start((android.view.View)(mostCurrent._img3.getObject()));
 //BA.debugLineNum = 452;BA.debugLine="t3.Enabled = False";
_t3.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 453;BA.debugLine="t4.Enabled = True";
_t4.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 454;BA.debugLine="End Sub";
return "";
}
public static String  _t4_tick() throws Exception{
 //BA.debugLineNum = 456;BA.debugLine="Sub t4_Tick";
 //BA.debugLineNum = 458;BA.debugLine="img4.Visible = True";
mostCurrent._img4.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 459;BA.debugLine="ani4.InitializeScaleCenter(\"ani\", 0, 0, 1, 1, img";
mostCurrent._ani4.InitializeScaleCenter(mostCurrent.activityBA,"ani",(float) (0),(float) (0),(float) (1),(float) (1),(android.view.View)(mostCurrent._img4.getObject()));
 //BA.debugLineNum = 460;BA.debugLine="ani4.Duration = 1500";
mostCurrent._ani4.setDuration((long) (1500));
 //BA.debugLineNum = 461;BA.debugLine="ani4.RepeatCount = 2";
mostCurrent._ani4.setRepeatCount((int) (2));
 //BA.debugLineNum = 462;BA.debugLine="ani4.SetInterpolator(ani4.INTERPOLATOR_BOUNCE)";
mostCurrent._ani4.SetInterpolator(mostCurrent._ani4.INTERPOLATOR_BOUNCE);
 //BA.debugLineNum = 463;BA.debugLine="ani4.Start(img4)";
mostCurrent._ani4.Start((android.view.View)(mostCurrent._img4.getObject()));
 //BA.debugLineNum = 464;BA.debugLine="t4.Enabled = False";
_t4.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 465;BA.debugLine="img3.Visible = False";
mostCurrent._img3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 467;BA.debugLine="it1.Enabled = True";
_it1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 468;BA.debugLine="it2.Enabled = True";
_it2.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 469;BA.debugLine="it3.Enabled = True";
_it3.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 470;BA.debugLine="it4.Enabled = True";
_it4.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 471;BA.debugLine="it5.Enabled = True";
_it5.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 472;BA.debugLine="it6.Enabled = True";
_it6.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 473;BA.debugLine="it7.Enabled = True";
_it7.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 474;BA.debugLine="it8.Enabled = True";
_it8.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 475;BA.debugLine="it9.Enabled = True";
_it9.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 476;BA.debugLine="it10.Enabled = True";
_it10.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 477;BA.debugLine="it11.Enabled = True";
_it11.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 478;BA.debugLine="it12.Enabled = True";
_it12.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 479;BA.debugLine="it13.Enabled = True";
_it13.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 480;BA.debugLine="it14.Enabled = True";
_it14.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 481;BA.debugLine="it15.Enabled = True";
_it15.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 483;BA.debugLine="b1t.Enabled = True";
_b1t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 484;BA.debugLine="b2t.Enabled = True";
_b2t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 485;BA.debugLine="b3t.Enabled = True";
_b3t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 486;BA.debugLine="b4t.Enabled = True";
_b4t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 487;BA.debugLine="b5t.Enabled = True";
_b5t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 488;BA.debugLine="b6t.Enabled = True";
_b6t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 489;BA.debugLine="End Sub";
return "";
}
public void _onCreate() {
	if (_theme_value != 0)
		setTheme(_theme_value);
}
}
