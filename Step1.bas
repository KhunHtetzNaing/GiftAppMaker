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
Dim ed,edn As EditText
Dim iv1,iv2,chm1 As ImageView
Dim b As Button
Dim lb2 As Label
Dim lb1 As Label
Dim abg,ebg As ColorDrawable
Dim sp As Spinner
Dim zip As ABZipUnzip
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

		abg.Initialize(Colors.RGB(255,10,144),1)
	Activity.Background = abg

iv2.Initialize("iv2")
iv2.Bitmap = LoadBitmap(File.DirAssets,"save.png")
iv2.Gravity = Gravity.FILL

b.Initialize("b")
b.Text = "Preview"

lb1.Initialize("")
lb1.Text = "Step 1"
lb1.Textsize = 30
lb1.TextColor = Colors.Black
lb1.Typeface = Typeface.DEFAULT_BOLD
lb1.Gravity = Gravity.CENTER
Activity.AddView(lb1,0%x,1%y,100%x,7%y)

lb2.Initialize("")
lb2.Text = "Choose Your App icon & Text"
lb2.TextSize = 20
lb2.TextColor = Colors.White
lb2.Gravity = Gravity.CENTER
Activity.AddView(lb2,0%x,(lb1.Top+lb1.Height),100%x,5%y)

iv1.Initialize("iv1")
iv1.Bitmap = LoadBitmap(File.DirAssets,"icon.png")
iv1.Gravity = Gravity.FILL
Activity.AddView(iv1,30%x,(lb2.Top+lb2.Height)+1%y,100dip,100dip)

chm1.Initialize("chm1")
chm1.Bitmap = LoadBitmap(File.DirAssets,"checkmark.png")
Activity.AddView(chm1,(iv1.Width+iv1.Top)+5%x,(lb2.Top+lb2.Height)+4%y,45dip,40dip)
chm1.Gravity = Gravity.FILL

ed.Initialize("ed")
ed.Hint = "Enter Your Text"
ebg.Initialize(Colors.White,1)

ed.Background = ebg
ed.HintColor = Colors.Gray
ed.TextColor = Colors.Black
'
sp.Initialize("sp")
sp.Add(">> Choose Font Style <<")
sp.Add("Love Font")
sp.Add("Heart Font")
sp.Add("Flower Font")
sp.Add("BeikThaNo Font")
sp.Add("Transformer Font")
sp.Add("Yoe Yar Font")
sp.Add("Chococooky Font")
sp.Add("Matrix Font")
sp.Add("Metrix Smart")

edn.Initialize("edn")
edn.Hint = "Enter Your App Name"
edn.Background = ebg
edn.HintColor = Colors.Gray
edn.TextColor = Colors.Black
Activity.AddView(edn,20%x,(iv1.Top+iv1.Height)+2%y,60%x,10%y)
Activity.AddView(ed,2%x,(edn.Top+edn.Height)+2%y,96%x,100dip)
Activity.AddView(sp,20%x,(ed.Top+ed.Height)+2%x,60%x,10%y)
Activity.AddView(b,15%x,(sp.Top+sp.Height)+5%x,170dip,57dip)
Activity.AddView(iv2,70%x,(sp.Top+sp.Height)+2%y,75dip,65dip)

chm1. Visible = False

End Sub

Sub sp_ItemClick (Position As Int, Value As Object)
 Select Position
  Case 0
  Case 1 : File.Copy(File.DirRootExternal & "/MyanmarFonts","Love.ttf",File.DirRootExternal & "/GiftAppMaker/assets","myanmar.ttf")
  Case 2 : File.Copy(File.DirRootExternal & "/MyanmarFonts","Heart.ttf",File.DirRootExternal & "/GiftAppMaker/assets","myanmar.ttf")
  Case 3 : File.Copy(File.DirRootExternal & "/MyanmarFonts","Flower.ttf",File.DirRootExternal & "/GiftAppMaker/assets","myanmar.ttf")
  	Case 4 : File.Copy(File.DirRootExternal & "/MyanmarFonts","BeikThaNo.ttf",File.DirRootExternal & "/GiftAppMaker/assets","myanmar.ttf")
		Case 5 : File.Copy(File.DirRootExternal & "/MyanmarFonts","Transformer.ttf",File.DirRootExternal & "/GiftAppMaker/assets","myanmar.ttf")
			Case 6 : File.Copy(File.DirRootExternal & "/MyanmarFonts","YoeYar.ttf",File.DirRootExternal & "/GiftAppMaker/assets","myanmar.ttf")
				Case 7 : File.Copy(File.DirRootExternal & "/MyanmarFonts","Chococooky.ttf",File.DirRootExternal & "/GiftAppMaker/assets","myanmar.ttf")
					Case 8 : File.Copy(File.DirRootExternal & "/MyanmarFonts","Matrix.ttf",File.DirRootExternal & "/GiftAppMaker/assets","myanmar.ttf")
						Case 9 : File.Copy(File.DirRootExternal & "/MyanmarFonts","Metrix Smart.ttf",File.DirRootExternal & "/GiftAppMaker/assets","myanmar.ttf")
 End Select
End Sub

Sub iv1_Click
	Dim ml As MLfiles
Dim fd As FileDialog
fd.FilePath = File.DirRootExternal
fd.Show("Choose Your Image","Open","Reset","Cancel",Null)
If fd.Response = DialogResponse.POSITIVE Then
	ml.mv(File.DirRootExternal & "/GiftAppMaker/res/drawable/icon.png",File.DirRootExternal & "/GiftAppMaker/res/drawable/icon.bak")
File.Copy(fd.FilePath,fd.ChosenName,File.DirRootExternal & "/GiftAppMaker/res/drawable","icon.png")
chm1.Visible = True
	End If
	If fd.Response = DialogResponse.CANCEL Then
		File.Delete(File.DirRootExternal & "/GiftAppMaker/res/drawable","icon.png")
		ml.mv(File.DirRootExternal & "/GiftAppMaker/res/drawable/icon.bak",File.DirRootExternal & "/GiftAppMaker/res/drawable/icon.png")
		chm1.Visible = False
	End If
End Sub

Sub iv2_Click
		If ed.Text = "" Then
		Else
		File.Delete(File.DirRootExternal & "/GiftAppMaker/assets","bdw.txt")
		File.WriteString(File.DirRootExternal & "/GiftAppMaker/assets","bdw.txt",ed.Text)
		End If
		If edn.Text = "" Then
			Else
	Dim arg(3) As String
	Dim pc As NNLPackageChanger
	arg(0) = File.DirRootExternal & "/GiftAppMaker/AndroidManifest.xml"
	arg(1) = "com.htetznaing.giftapp"
	arg(2) = edn.Text
	pc.Change(arg)
		End If
		StartActivity(Step2)
End Sub

Sub b_Click
		If ed.Text = "" Then
		Else
		File.Delete(File.DirRootExternal & "/GiftAppMaker/assets","bdw.txt")
		File.WriteString(File.DirRootExternal & "/GiftAppMaker/assets","bdw.txt",ed.Text)
		End If
		If edn.Text = "" Then
			Else
	Dim arg(3) As String
	Dim pc As NNLPackageChanger
	arg(0) = File.DirRootExternal & "/GiftAppMaker/AndroidManifest.xml"
	arg(1) = "com.htetznaing.giftapp"
	arg(2) = edn.Text
	pc.Change(arg)
		End If
	StartActivity(Preview)
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