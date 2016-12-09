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
Dim ad As Timer
End Sub

Sub Globals
	Dim iv1,iv2,iv3,iv4 As ImageView
Dim abg As ColorDrawable
Dim chm1,chm2,chm3 As ImageView
Dim ml As MLfiles
Dim b As Button
Dim lb1,lb2 As Label
Dim Banner As AdView
Dim Interstitial As mwAdmobInterstitial
Dim p As Phone
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
	
	Activity.LoadLayout("l2")
		abg.Initialize(Colors.RGB(255,10,144),1)
	Activity.Background = abg

lb1.Text = "Step 2"
lb1.Textsize = 30
lb1.TextColor = Colors.Black
lb1.Typeface = Typeface.DEFAULT_BOLD
lb1.Gravity = Gravity.CENTER

lb2.Text = "Choose Your Image Or Next"
lb2.TextSize = 20
lb2.TextColor = Colors.White
lb2.Gravity = Gravity.CENTER

b.Text = "Preview"

chm1.Visible = False
chm2.Visible = False
chm3.Visible = False

 Dim lbf As Label
 lbf.Initialize("lbf")
 lbf.Text = "Developed By Khun Htetz Naing"
 Activity.AddView(lbf,0%x,85%y,100%x,5%y)
 lbf.Gravity = Gravity.CENTER
 lbf.TextColor = Colors.White
 
End Sub

Sub b_Click
	StartActivity(Preview)
End Sub

Sub iv4_Click
	StartActivity(Step3)
End Sub

Sub iv1_Click
	
Dim fd As FileDialog
fd.FilePath = File.DirRootExternal
fd.Show("Choose Your Image","Open","Reset","Cancel",Null)
If fd.Response = DialogResponse.POSITIVE Then
	ml.mv(File.DirRootExternal & "/GiftAppMaker/assets/img1.png",File.DirRootExternal & "/GiftAppMaker/assets/img1.bak")
File.Copy(fd.FilePath,fd.ChosenName,File.DirRootExternal & "/GiftAppMaker/assets","img1.png")
chm1.Visible = True
	End If
	If fd.Response = DialogResponse.CANCEL Then
		File.Delete(File.DirRootExternal & "/GiftAppMaker/assets","img1.png")
		ml.mv(File.DirRootExternal & "/GiftAppMaker/assets/img1.bak",File.DirRootExternal & "/GiftAppMaker/assets/img1.png")
		chm1.Visible = False
	End If
End Sub

Sub iv2_Click
Dim fd As FileDialog
fd.FilePath = File.DirRootExternal
fd.Show("Choose Your Image","Open","Reset","Cancel",Null)
If fd.Response = DialogResponse.POSITIVE Then
	ml.mv(File.DirRootExternal & "/GiftAppMaker/assets/img3.png",File.DirRootExternal & "/GiftAppMaker/assets/img3.bak")
File.Copy(fd.FilePath,fd.ChosenName,File.DirRootExternal & "/GiftAppMaker/assets","img3.png")
chm2.Visible = True
	End If
	If fd.Response = DialogResponse.CANCEL Then
		File.Delete(File.DirRootExternal & "/GiftAppMaker/assets","img3.png")
		ml.mv(File.DirRootExternal & "/GiftAppMaker/assets/img3.bak",File.DirRootExternal & "/GiftAppMaker/assets/img3.png")
		chm2.Visible = False
	End If
End Sub

Sub iv3_Click
Dim fd As FileDialog
fd.FilePath = File.DirRootExternal
fd.Show("Choose Your Image","Open","Reset","Cancel",Null)
If fd.Response = DialogResponse.POSITIVE Then
	ml.mv(File.DirRootExternal & "/GiftAppMaker/assets/img4.png",File.DirRootExternal & "/GiftAppMaker/assets/img4.bak")
File.Copy(fd.FilePath,fd.ChosenName,File.DirRootExternal & "/GiftAppMaker/assets","img4.png")
chm3.Visible = True
	End If
	If fd.Response = DialogResponse.CANCEL Then
		File.Delete(File.DirRootExternal & "/GiftAppMaker/assets","img4.png")
		ml.mv(File.DirRootExternal & "/GiftAppMaker/assets/img4.bak",File.DirRootExternal & "/GiftAppMaker/assets/img4.png")
	chm3.Visible = False
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