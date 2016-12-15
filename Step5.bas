Type=Activity
Version=6.3
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: True
	#IncludeTitle: False
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
Dim T As Timer
Dim ad As Timer
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
Dim zip As ABZipUnzip
Dim zs As NNLZipSigner
Dim sd As String
	Private lb1 As Label
	Private lb2 As Label
	Private b As Button
	Dim abg As ColorDrawable
	Dim ml As MLfiles
	Dim Banner As AdView
Dim Interstitial As mwAdmobInterstitial
Dim p As Phone
Dim iv1 As ImageView
End Sub

Sub Activity_Create(FirstTime As Boolean)
			p.SetScreenOrientation(1)
	If p.SdkVersion > 19 Then
		Banner.Initialize("Banner","ca-app-pub-4173348573252986/6151874158")
		Banner.LoadAd
		Activity.AddView(Banner,0%x,90%y,100%x,10%y)
		
		Interstitial.Initialize("Interstitial","ca-app-pub-4173348573252986/7628607354")
		Interstitial.LoadAd
		ad.Initialize("ad",30000)
		ad.Enabled = True
	End If
	
	sd =  File.DirRootExternal & "/"
			abg.Initialize(Colors.RGB(255,10,144),1)
	Activity.Background = abg
Activity.LoadLayout("FinalStep")

lb1.Textsize = 30
lb1.TextColor = Colors.Black
lb1.Typeface = Typeface.DEFAULT_BOLD
lb1.Gravity = Gravity.CENTER
lb2.Text = "Build Your Android App Now!"
lb2.TextSize = 20
lb2.TextColor = Colors.White
lb2.Gravity = Gravity.CENTER
T.Initialize("T",1000)
T.Enabled = False

 Dim lbf As Label
 lbf.Initialize("lbf")
 lbf.Text = "Developed By Khun Htetz Naing"
 Activity.AddView(lbf,0%x,85%y,100%x,5%y)
 lbf.Gravity = Gravity.CENTER
 lbf.TextColor = Colors.White
End Sub

Sub b1_Click
	StartActivity(Preview)
End Sub

Sub b_Click
	File.Delete(File.DirRootExternal & "/GiftAppMaker/assets","img1.bak")
	File.Delete(File.DirRootExternal & "/GiftAppMaker/assets","img2.bak")
	File.Delete(File.DirRootExternal & "/GiftAppMaker/assets","img3.bak")
	File.Delete(File.DirRootExternal & "/GiftAppMaker/assets","img4.bak")
	File.Delete(File.DirRootExternal & "/GiftAppMaker/assets","p1.bak")
	File.Delete(File.DirRootExternal & "/GiftAppMaker/assets","p2.bak")
	File.Delete(File.DirRootExternal & "/GiftAppMaker/assets","p3.bak")
	File.Delete(File.DirRootExternal & "/GiftAppMaker/assets","p4.bak")
	File.Delete(File.DirRootExternal & "/GiftAppMaker/assets","p5.bak")
	File.Delete(File.DirRootExternal & "/GiftAppMaker/assets","birthday.bak")
	ProgressDialogShow("Building Your Apk!" & CRLF & "Please Wait...")
	T.Enabled = True
	zs.Initialize
End Sub

Sub T_Tick
    zip.ABZipDirectory(sd & "GiftAppMaker" , sd & "giftapp.apk") '--------------  project_hello ကို hello.apk ျဖစ္ေအာင္ ျပန္ပိတ္မယ္။ 
	zs.SignZip(sd & "giftapp.apk" , sd & "MyGiftApp.apk") '--------------- Sign လုပ္မယ္ New apk ကို hello_Output.apk အမည္နဲ႕ ထုတ္မယ္။
	
       ml.rm(sd & "giftapp.apk") '---------------  sdcard ထဲ႕ hello.apk အေဟာင္းကို ဖ်က္မယ္။
'	   ml.rmrf(sd & "GiftAppMaker") '--------------- sdcard ထဲက project_hello Folder ကိုလည္း ဖ်က္မယ္။
	 ProgressDialogHide
	         ToastMessageShow ("Successfully Created " & CRLF & "MyGiftApp.apk in SdCard!",True)     '---------  ပီးပီေပါ့
		T.Enabled = False
Dim in As Intent
in.Initialize(in.ACTION_VIEW,"file:///"&sd & "MyGiftApp.apk")
in.SetType("application/vnd.android.package-archive")
If in.IsInitialized Then 
 StartActivity(in)
 Else
 Msgbox("Please Restart Project","Attention!")
 End If
End Sub


Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub ad_Tick
	If p.SdkVersion > 19 Then
	If Interstitial.Status=Interstitial.Status_AdReadyToShow Then
		Interstitial.Show
		End If
		If Interstitial.Status=Interstitial.Status_Dismissed Then
			Interstitial.LoadAd
	End If
	End If
End Sub