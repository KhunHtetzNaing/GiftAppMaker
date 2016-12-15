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
Dim cc1,cc2,cc3 As ContentChooser
End Sub

Sub Activity_Create(FirstTime As Boolean)
	cc1.Initialize("cc1")
	cc2.Initialize("cc2")
	cc3.Initialize("cc3")
	
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
	
		abg.Initialize(Colors.RGB(255,10,144),1)
	Activity.Background = abg


lb1.Initialize("")
lb1.Text = "Step 1"
lb1.Textsize = 30
lb1.TextColor = Colors.Black
lb1.Typeface = Typeface.DEFAULT_BOLD
lb1.Gravity = Gravity.CENTER
Activity.AddView(lb1,0%x,1%y,100%x,7%y)

lb2.Initialize("")
lb2.Text =  "Choose Your Image Or Next"
lb2.TextSize = 20
lb2.TextColor = Colors.White
lb2.Gravity = Gravity.CENTER
Activity.AddView(lb2,0%x,(lb1.Top+lb1.Height),100%x,5%y)

'Imv1
iv1.Initialize("iv1")
iv1.Bitmap = LoadBitmap(File.DirAssets,"chooseimage.png")
iv1.Gravity = Gravity.FILL
Activity.AddView(iv1,30%x,(lb2.Top+lb2.Height)+2%y,80dip,80dip)

chm1.Initialize("chm1")
chm1.Bitmap = LoadBitmap(File.DirAssets,"checkmark.png")
chm1.Gravity = Gravity.FILL
Activity.AddView(chm1,(iv1.Width+iv1.Top)+5%x,(lb2.Top+lb2.Height)+4%y,45dip,40dip)

'Imv2
iv2.Initialize("iv2")
iv2.Bitmap = LoadBitmap(File.DirAssets,"chooseimage.png")
iv2.Gravity = Gravity.FILL
Activity.AddView(iv2,30%x,(iv1.Top+iv1.Height)+2%y,80dip,80dip)

chm2.Initialize("chm2")
chm2.Bitmap = LoadBitmap(File.DirAssets,"checkmark.png")
chm2.Gravity = Gravity.FILL
Activity.AddView(chm2,(iv1.Width+iv1.Top)+5%x,(iv1.Top+iv1.Height)+4%y,45dip,40dip)

'Imv3
iv3.Initialize("iv3")
iv3.Bitmap = LoadBitmap(File.DirAssets,"chooseimage.png")
iv3.Gravity = Gravity.FILL
Activity.AddView(iv3,30%x,(iv2.Top+iv2.Height)+2%y,80dip,80dip)

chm3.Initialize("chm3")
chm3.Bitmap = LoadBitmap(File.DirAssets,"checkmark.png")
chm3.Gravity = Gravity.FILL
Activity.AddView(chm3,(iv1.Width+iv1.Top)+5%x,(iv2.Top+iv2.Height)+4%y,45dip,40dip)

chm1.Visible = False
chm2.Visible = False
chm3.Visible = False
'Iv4
iv4.Initialize("iv4")
iv4.Bitmap = LoadBitmap(File.DirAssets,"save.png")
iv4.Gravity = Gravity.FILL
Activity.AddView(iv4,70%x,(iv3.Top+iv3.Height)+2%y,75dip,65dip)

b.Initialize("b")
b.Text = "Preview"
Activity.AddView(b,15%x,(iv3.Top+iv3.Height)+5%y,170dip,57dip)

chm1.Visible = False
chm2.Visible = False
chm3.Visible = False
 
 
End Sub

Sub b_Click
	StartActivity(Preview)
End Sub

Sub iv4_Click
	StartActivity(Step3)
End Sub

Sub iv1_Click
	cc1.Show("image/*", "Choose image")
End Sub

Sub cc1_Result (Success As Boolean, Dir As String, FileName As String)
    If Success Then
		File.Delete( File.DirRootExternal & "/.GiftAppMaker/assets", "img1.png")
             File.Copy(Dir, FileName, File.DirRootExternal & "/.GiftAppMaker/assets", "img1.png")
			 chm1.Visible = True
    End If
End Sub

Sub iv2_Click
	cc2.Show("image/*", "Choose image")
End Sub

Sub cc2_Result (Success As Boolean, Dir As String, FileName As String)
    If Success Then
		File.Delete( File.DirRootExternal & "/.GiftAppMaker/assets", "img3.png")
             File.Copy(Dir, FileName, File.DirRootExternal & "/.GiftAppMaker/assets", "img3.png")
			 chm2.Visible = True
    End If
End Sub

Sub iv3_Click
	cc3.Show("image/*", "Choose image")
End Sub

Sub cc3_Result (Success As Boolean, Dir As String, FileName As String)
    If Success Then
		File.Delete( File.DirRootExternal & "/.GiftAppMaker/assets", "img4.png")
             File.Copy(Dir, FileName, File.DirRootExternal & "/.GiftAppMaker/assets", "img4.png")
			 chm3.Visible = True
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