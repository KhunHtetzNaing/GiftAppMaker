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
	Dim MediaPlayer1 As MediaPlayer
	Dim timer1 As Timer
	Dim ad As Timer
End Sub

Sub Globals
Dim lb1,lb2 As Label
Dim abg As ColorDrawable

	Dim barPosition As SeekBar
	Dim barVolume As SeekBar
	Dim lblPosition As Label
	Dim iv1 As ImageView
Dim chm1 As ImageView
	Dim iv2 As ImageView
	Dim iv3 As ImageView
Dim  iv4 As ImageView
Dim ml As MLfiles
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
	
		MediaPlayer1.Initialize( )
		timer1.Initialize("timer1", 100)
			abg.Initialize(Colors.RGB(255,10,144),1)
	Activity.Background = abg
Activity.LoadLayout("l4")
lb1.Text = "Step 4"
lb1.Textsize = 30
lb1.TextColor = Colors.Black
lb1.Typeface = Typeface.DEFAULT_BOLD
lb1.Gravity = Gravity.CENTER
lb2.Text = "Choose Your Music Or Next"
lb2.TextSize = 20
lb2.TextColor = Colors.White
lb2.Gravity = Gravity.CENTER

chm1.Visible = False

 Dim lbf As Label
 lbf.Initialize("lbf")
 lbf.Text = "Developed By Khun Htetz Naing"
 Activity.AddView(lbf,0%x,85%y,100%x,5%y)
 lbf.Gravity = Gravity.CENTER
 lbf.TextColor = Colors.White
End Sub

Sub iv1_Click
	Dim fd As FileDialog
	fd.FilePath = File.DirRootExternal
fd.Show("Choose Your Image","Open","Reset","Cancel",Null)
If fd.Response = DialogResponse.POSITIVE Then
	ml.mv(File.DirRootExternal & "/GiftAppMaker/assets/birthday.mp3",File.DirRootExternal & "/GiftAppMaker/assets/birthday.bak")
File.Copy(fd.FilePath,fd.ChosenName,File.DirRootExternal & "/GiftAppMaker/assets","birthday.mp3")
chm1.Visible = True
	End If
	If fd.Response = DialogResponse.CANCEL Then
		File.Delete(File.DirRootExternal & "/GiftAppMaker/assets","birthday.mp3")
		ml.mv(File.DirRootExternal & "/GiftAppMaker/assets/birthday.bak",File.DirRootExternal & "/GiftAppMaker/assets/birthday.mp3")
		chm1.Visible = False
	End If

End Sub

Sub iv2_Click
	If MediaPlayer1.IsPlaying = False Then
	MediaPlayer1.Load(File.DirRootExternal & "/GiftAppMaker/assets","birthday.mp3")
	MediaPlayer1.Play
	timer1.Enabled = True
	End If
End Sub

Sub iv3_Click
	If MediaPlayer1.IsPlaying Then
	MediaPlayer1.Stop
	End If
End Sub

Sub iv4_Click
	StartActivity(Step5)
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub timer1_Tick
	If MediaPlayer1.IsPlaying Then
		barPosition.Value = MediaPlayer1.Position / MediaPlayer1.Duration * 100
		lblPosition.Text = "Position: " & ConvertToTimeFormat(MediaPlayer1.Position) & _
			" (" & ConvertToTimeFormat(MediaPlayer1.Duration) & ")"
	End If
End Sub

Sub ConvertToTimeFormat(ms As Int) As String
	Dim seconds, minutes As Int
	seconds = Round(ms / 1000)
	minutes = Floor(seconds / 60)
	seconds = seconds Mod 60
	Return NumberFormat(minutes, 1, 0) & ":" & NumberFormat(seconds, 2, 0) 'ex: 3:05
End Sub

Sub barVolume_ValueChanged (Value As Int, UserChanged As Boolean)
	MediaPlayer1.SetVolume(barVolume.Value / 100, barVolume.Value / 100)
End Sub

Sub barPosition_ValueChanged (Value As Int, UserChanged As Boolean)
	If UserChanged = False Then Return 'the value was changed programmatically
	MediaPlayer1.Position = Value / 100 * MediaPlayer1.Duration
	If MediaPlayer1.IsPlaying = False Then 'this can happen when the playback reached the end and the user changes the position
		MediaPlayer1.Play
	End If
	timer1_Tick 'immediately update the progress label
End Sub

Sub b_Click
	StartActivity(Preview)
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