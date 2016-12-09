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
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Dim su As StringUtils 
	Dim p As PhoneIntents 
	Dim lstOne As ListView 
	Dim abg As ColorDrawable
	Dim Banner As AdView
	Dim Interstitial As mwAdmobInterstitial
	Dim ph As Phone
End Sub

Sub Activity_Create(FirstTime As Boolean)
			ph.SetScreenOrientation(1)
	If ph.SdkVersion > 19 Then
		Banner.Initialize("Banner","ca-app-pub-4173348573252986/6151874158")
		Banner.LoadAd
		Activity.AddView(Banner,0%x,90%y,100%x,10%y)
		
		Interstitial.Initialize("Interstitial","ca-app-pub-4173348573252986/7628607354")
		Interstitial.LoadAd
		ad.Initialize("ad",30000)
		ad.Enabled = True
	End If
	

abg.Initialize(Colors.RGB(255,10,144),1)
	Activity.Background = abg
	
	Dim imvLogo As ImageView 
	imvLogo.Initialize ("imv")
	imvLogo.Bitmap = LoadBitmap(File.DirAssets , "about.png")
	imvLogo.Gravity = Gravity.FILL 
	Activity.AddView ( imvLogo , 50%x - 50dip  , 20dip ,  100dip  ,  100dip )
	
	Dim lblName As  Label 
	Dim bg As ColorDrawable 
	bg.Initialize (Colors.DarkGray , 10dip)
	lblName.Initialize ("lbname")
	lblName.Background = bg
	lblName.Gravity = Gravity.CENTER 
	lblName.Text = "Gift App Maker"
	lblName.TextSize = 13
	lblName.TextColor = Colors.White 
	Activity.AddView (lblName , 100%x / 2 - 90dip , 130dip , 180dip , 50dip)
	lblName.Height = su.MeasureMultilineTextHeight (lblName, lblName.Text ) + 5dip
	
	
	Dim c As ColorDrawable 
	c.Initialize (Colors.White , 10dip )
	lstOne.Initialize ("lstOnes")
	lstOne.Background = c
	lstOne.SingleLineLayout .Label.TextSize = 12
	lstOne.SingleLineLayout .Label .TextColor = Colors.DarkGray 
	lstOne.SingleLineLayout .Label .Gravity = Gravity.CENTER 
	lstOne.SingleLineLayout .ItemHeight = 40dip
	lstOne.AddSingleLine2 ("Developed By : Khun Htetz Naing    ", 1)
	lstOne.AddSingleLine2 ("Email : khunht3tzn4ing@gmail.com    ",2)
	lstOne.AddSingleLine2 ("Phone : +959776003982    ",3)
	lstOne.AddSingleLine2 ("Facebook : www.facebook.com/Khun.Htetz.Naing   ", 4)
	lstOne.AddSingleLine2 ("Special Thank All Myanmar Font Developer!",5)
	Activity.AddView ( lstOne, 30dip , 170dip , 100%x -  60dip, 200dip)
	
	Dim lblCredit As Label 
	lblCredit.Initialize ("lblCredit")
	lblCredit.TextColor = Colors.Black
	lblCredit.TextSize = 13
	lblCredit.Gravity = Gravity.CENTER 
	lblCredit.Text = "If You have any Problem You can contact Me."
	Activity.AddView (lblCredit, 10dip,(lstOne.Top+lstOne.Height)+3%y, 100%x - 20dip, 50dip)
	lblCredit.Height = su.MeasureMultilineTextHeight (lblCredit, lblCredit.Text )
		
End Sub
 
 Sub ad_Tick
 	If ph.SdkVersion > 19 Then
 	If Interstitial.Status = Interstitial.Status_AdReadyToShow Then
		Interstitial.Show
		End If
		
		If Interstitial.Status = Interstitial.Status_Dismissed Then
		Interstitial.LoadAd
End If
End If
End Sub

 Sub imv_Click
 	StartActivity(p.OpenBrowser("https://www.facebook.com/Khun.Htetz.Naing/"))
End Sub

Sub lbname_Click
 	StartActivity(p.OpenBrowser("https://play.google.com/store/apps/details?id=com.htetznaing.mmallsimregistration"))
End Sub

Sub lblCredit_Click
	StartActivity(p.OpenBrowser ("https://play.google.com/store/apps/details?id=com.htetznaing.mmallsimregistration"))
End Sub
Sub Activity_Resume
     
End Sub

Sub Activity_Pause (UserClosed As Boolean)
     
End Sub

Sub lstOnes_ItemClick (Position As Int, Value As Object)
     Select Value
	 	Case 3
Dim i As Intent
i.Initialize(i.ACTION_VIEW, "tel:+959776003982")
StartActivity(i)
	 			Case 4
				   StartActivity(p.OpenBrowser ("https://www.facebook.com/Khun.Htetz.Naing/"))
	 End Select
End Sub