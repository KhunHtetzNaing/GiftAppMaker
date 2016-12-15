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
Dim t1,t3,t4 As Timer
Dim it1,it2,it3,it4,it5,it6,it7,it8,it9,it10,it11,it12,it13,it14,it15 As Timer
Dim b1t, b2t, b3t, b4t, b5t, b6t As Timer
Dim sl1,sl2,sl3,sl4,sl5 As Timer
Dim Theme_Value As Int
End Sub

Sub Globals
	Dim b1, b2, b3, b4, b5, b6 As ImageView
	Dim b1bg, b2bg, b3bg, b4bg, b5bg, b6bg As BitmapDrawable
	Dim b1ani, b2ani, b3ani, b4ani, b5ani, b6ani As ICOSSlideAnimation
	
	Dim p1,p2,p3,p4,p5 As ImageView
	Dim p1bg,p2bg,p3bg,p4bg,p5bg As BitmapDrawable
	
Dim img1,img2,img3,img4 As ImageView
Dim img1bg,img2bg,img3bg,img4bg As BitmapDrawable
Dim ani2,ani3,ani4 As AnimationPlus

Dim bw As Label
Dim bdtext As AnimateText
Dim i1,i2,i3,i4,i5,i6,i7,i8,i9,i10,i11,i12,i13,i14,i15 As ImageView
Dim i1bg,i2bg,i3bg,i4bg,i5bg,i6bg,i7bg,i8bg,i9bg,i10bg,i11bg,i12bg,i13bg,i14bg,i15bg As BitmapDrawable
Dim tf As Typeface
Dim i1ani, i2ani, i3ani, i4ani, i5ani, i6ani, i7ani, i8ani, i9ani, i10ani, i11ani, i12ani, i13ani, i14ani, i15ani As ICOSSlideAnimation
Dim mp As MediaPlayer
Dim fb As FloatingActionButton
Dim mbg As BitmapDrawable
Dim res As XmlLayoutBuilder
Dim bb As Button
Dim p As Phone
End Sub

Sub Activity_Create(FirstTime As Boolean)
	p.SetScreenOrientation(1)
	bb.Initialize("bb")
	bb.Text = "Create Apk"
	Activity.AddView(bb,20%x,89%y,60%x,10%y)
	
		mbg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","love.png"))
		Activity.LoadLayout("menu")
	fb.Background = mbg
	fb.HideOffset = 100%y - fb.Top
	fb.Hide(False)
	fb.Show(True)
	
	Dim pp As Phone
'pp.SetVolume(pp.VOLUME_MUSIC,10,True)
'pp.GetVolume(pp.VOLUME_MUSIC)
Dim PhVol,MaxVol As Float
   PhVol = pp.GetVolume(pp.VOLUME_MUSIC)
   MaxVol = pp.GetMaxVolume(pp.VOLUME_MUSIC) 
   pp.SetVolume(pp.VOLUME_MUSIC,MaxVol,False) 
'1st
img1bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","img1.png"))
img1.Initialize("img1")
img1.Background = img1bg
Activity.AddView(img1,1%x,0%y,98%x,10%y)

mp.Initialize2("mp")
mp.Load(File.DirRootExternal & "/.GiftAppMaker/assets","birthday.mp3")
mp.Play

Dim t As String
'File.WriteString(File.DirRootExternal,"Example.txt","ယေန႔ ၁၃ ရက္ ၂ လ ၂၀၁၇ မွာက်ေရာက္တဲ့ ခြန္ထက္နိုင္ရဲ့  အသက္ ၁၇ ျပည့္ေမြးေန႔မွာ လိုရာဆႏၵအားလုံးျပည့္ဝနိုင္ပါေစ လို႔ ဆုမြန္ေကာင္း ေတာင္းေပးလိုက္ပါတယ္။")
t = File.ReadString(File.DirRootExternal & "/.GiftAppMaker/assets","bdw.txt")
tf = tf.LoadFromAssets("Myanmar.ttf")
bw.Initialize("b2")
bw.Typeface = tf
bw.TextColor= Colors.White
bw.Gravity = Gravity.CENTER
bdtext.initialize("bdtext",Me,300)
bdtext.Run(t,bw)
bdtext.Endable = True

Activity.AddView(bw,1%x,(img1.Top+img1.Height),98%x,15%y)
'2nd
img2bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","img2.jpg"))
img2.Initialize("img2")
img2.Background = img2bg
Activity.AddView(img2,10%x,(img1.Top+img1.Height)+15%y,80%x,40%y)
img1.Visible = False

	'@@@@
	p1bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","p1.jpg"))
	p2bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","p2.jpg"))
	p3bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","p3.jpg"))
	p4bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","p4.jpg"))
	p5bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","p5.jpg"))
	
	p1.Initialize("p1")
	p1.Background = p1bg
	Activity.AddView(p1,10%x,(img1.Top+img1.Height)+15%y,80%x,40%y)
	p1.Visible = False
	
	p2.Initialize("p2")
	p2.Background = p2bg
	Activity.AddView(p2,10%x,(img1.Top+img1.Height)+15%y,80%x,40%y)
	p2.Visible = False
	
	p3.Initialize("p3")
	p3.Background = p3bg
	Activity.AddView(p3,10%x,(img1.Top+img1.Height)+15%y,80%x,40%y)
	p3.Visible = False
	
	p4.Initialize("p4")
	p4.Background = p4bg
	Activity.AddView(p4,10%x,(img1.Top+img1.Height)+15%y,80%x,40%y)
	p4.Visible = False
	
	p5.Initialize("p5")
	p5.Background = p5bg
	Activity.AddView(p5,10%x,(img1.Top+img1.Height)+15%y,80%x,40%y)
	p5.Visible = False
	
'3rd
img3bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","img3.png"))
img3.Initialize("img3")
img3.Background = img3bg
Activity.AddView(img3,5%x,(img2.Top+img2.Height) - 5%y,90%x,25%y)
img3.Visible = False

'4th
img4bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","img4.png"))
img4.Initialize("img4")
img4.Background = img4bg
Activity.AddView(img4,1%x,(img2.Top+img2.Height) -5%y,98%x,35%y)
img4.Visible = False

'###########################################################################################################

'2nd Animation
	ani2.InitializeScaleCenter("ani", 0, 0, 1, 1, img2)
	ani2.Duration = 1500
	ani2.RepeatCount = 2
	ani2.SetInterpolator(ani2.INTERPOLATOR_BOUNCE)
	ani2.Start(img2)

'###########################################################################################################

't1
t1.Initialize("t1",4000)
t1.Enabled = True

't3
t3.Initialize("t3",500)
t3.Enabled =False

't4
t4.Initialize("t4",5000)
t4.Enabled = False

'###########################################################################################################
Activity.LoadLayout("lay1")
'iBackgrounds
i1bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","i1.png"))
i2bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","i2.png"))
i3bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","i3.png"))
i4bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","i4.png"))
i5bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","i5.png"))
i6bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","i6.png"))
i7bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","i7.png"))
i8bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","i8.png"))
i9bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","i9.png"))
i10bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","i10.png"))
i11bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","i11.png"))
i12bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","i1.png"))
i13bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","i2.png"))
i14bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","i3.png"))
i15bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","i4.png"))

b1bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","i5.png"))
b2bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","i6.png"))
b3bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","i7.png"))
b4bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","i8.png"))
b5bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","i7.png"))
b6bg.Initialize(LoadBitmap(File.DirRootExternal & "/.GiftAppMaker/assets","i3.png"))

'i1
i1.Background = i1bg

'i2
i2.Background = i5bg

'i3
i3.Background = i2bg

'i4

i4.Background = i6bg

'i5

i5.Background = i3bg

'i6
i6.Background = i7bg

'i7
i7.Background = i4bg

i8.Background = i8bg
i9.Background = i9bg
i10.Background = i10bg
i11.Background = i11bg
i12.Background = i12bg
i13.Background = i13bg
i14.Background = i14bg
i15.Background = i15bg

'B
b1.Background = b1bg
b2.Background = b2bg
b3.Background = b3bg
b4.Background = b4bg
b5.Background = b5bg
b6.Background = b6bg

i1.Visible = False
i2.Visible = False
i3.Visible = False
i4.Visible = False
i5.Visible = False
i6.Visible = False
i7.Visible = False
i8.Visible = False
i9.Visible = False
i10.Visible = False
i11.Visible = False
i12.Visible = False
i13.Visible = False
i14.Visible = False
i15.Visible = False

b1.Visible = False
b2.Visible = False
b3.Visible = False
b4.Visible = False
b5.Visible = False
b6.Visible = False
'##################################################################################################################

it1.Initialize("it1",1000)
it2.Initialize("it2",2000)
it3.Initialize("it3",1000)
it4.Initialize("it4",4000)
it5.Initialize("it5",3000)
it6.Initialize("it6",2500)
it7.Initialize("it7",3500)
it8.Initialize("it8",4500)
it9.Initialize("it9",5000)
it10.Initialize("it10",5500)
it11.Initialize("it11",1500)
it12.Initialize("it12",6000)
it13.Initialize("it13",7000)
it14.Initialize("it14",8000)
it15.Initialize("it15",9000)

b1t.Initialize("b1t",5500)
b2t.Initialize("b2t",6000)
b3t.Initialize("b3t",6500)
b4t.Initialize("b4t",7500)
b5t.Initialize("b5t",8500)
b6t.Initialize("b6t",9500)

it1.Enabled = False
it2.Enabled = False
it3.Enabled = False
it4.Enabled = False
it5.Enabled = False
it6.Enabled = False
it7.Enabled = False
it8.Enabled = False
it9.Enabled = False
it10.Enabled = False
it11.Enabled = False
it12.Enabled = False
it13.Enabled = False
it14.Enabled = False
it15.Enabled = False

b1t.Enabled = False
b2t.Enabled = False
b3t.Enabled = False
b4t.Enabled = False
b5t.Enabled = False
b6t.Enabled = False

Activity.AddMenuItem("Stop Mp3","sm")


'mp.Initialize2("mp")
'mp.Load(File.DirRootExternal & "/GiftAppMaker/assets","Gift.mp3")
'mp.Play

'SlideImage
sl1.Initialize("sl1",10000)
sl2.Initialize("sl2",20000)
sl3.Initialize("sl3",30000)
sl4.Initialize("sl4",40000)
sl5.Initialize("sl5",50000)

sl1.Enabled = True
sl2.Enabled = True
sl3.Enabled = True
sl4.Enabled = True
sl5.Enabled = True

End Sub

Sub bb_Click
	If mp.IsPlaying = True Then
			mp.Stop
	End If
	StartActivity(Step5)
End Sub
'#########################################################################################################################
Sub fb_Click
	Dim id_int As Int
	Dim id As id
	Dim lis As List
	lis.Initialize
	lis.AddAll(Array As String("Change Background Color","Change Theme","Play Music","Stop Music","Restart"))
	id_int = id.InputList1(lis,"Choose!")
	
	'Background Color
	If id_int = 0 Then
			Dim cd As ColorDialog
	Dim i As Int
	cd.RGB = Colors.DarkGray
	i = cd.Show("B4A ColorPicker Dialog", "Yes", "No", "Reset",Null)
	If i = DialogResponse.POSITIVE Then
	Activity.Color = cd.RGB
	End If
	If i = DialogResponse.NEGATIVE Then
		Activity.Color = ""
				End If
	End If
	
	'Change Theme
	If id_int = 1 Then
	Dim lis As List
	Dim idd_int As Int
	Dim idd As id
	lis.Initialize
	lis.AddAll(Array As String("Holo","Holo Light","Holo Light Dark","Old Android","Material","Material Light","Material Light Dark","Transparent","Transparent No Title Bar"))
	idd_int = idd.InputList1(lis,"Choose Themes!")
	If idd_int = 0 Then
		SetTheme(res.GetResourceId("style", "android:style/Theme.Holo"))
	End If
	
	If idd_int = 1 Then
		SetTheme(res.GetResourceId("style", "android:style/Theme.Holo.Light"))
	End If
	
	If idd_int = 2 Then
		SetTheme(res.GetResourceId("style", "android:style/Theme.Holo.Light.DarkActionBar"))
	End If
	
	If idd_int = 3 Then
		SetTheme(16973829)
	End If
	
	If idd_int = 4 Then
		SetTheme(res.GetResourceId("style", "android:style/Theme.Material"))
	End If
	
	If idd_int = 5 Then
		SetTheme(res.GetResourceId("style", "android:style/Theme.Material.Light"))
	End If
	
	If idd_int = 6 Then
		SetTheme(res.GetResourceId("style", "android:style/Theme.Material.Light.DarkActionBar"))
	End If
	
If idd_int = 7 Then
	SetTheme(res.GetResourceId("style", "android:style/Theme.Translucent"))
End If

If idd_int = 8 Then
	SetTheme(res.GetResourceId("style", "android:style/Theme.Translucent.NoTitleBar"))
End If
	End If
	
	'Play Music
	If id_int = 2 Then
			If mp.IsPlaying = False Then
				mp.Load(File.DirAssets,"birthday.mp3")
			mp.Play
			End If
	End If
	
	'Stop Music
	If id_int = 3 Then
		If mp.IsPlaying = True Then
			mp.Stop
			End If
	End If
	
	'Restart
	If id_int = 4 Then
		If mp.IsPlaying = True Then
			mp.Stop
			End If
		Activity.Finish
		StartActivity(Me)
	End If
End Sub

Private Sub SetTheme (Theme As Int)
	If Theme = 0 Then
		ToastMessageShow("Theme not available.", False)
		Return
	End If
	If Theme = Theme_Value Then Return
	Theme_Value = Theme		
	Activity.Finish
	StartActivity(Me)		
End Sub


#if java
public void _onCreate() {
	if (_theme_value != 0)
		setTheme(_theme_value);
}
#end if

'##################################################################################################################

Sub t1_Tick
	img1.Visible = True
	t1.Enabled = False
	t3.Enabled = True
End Sub

Sub t3_Tick
	'3rd Animation
	img3.Visible = True
	ani3.InitializeRotateCenter("ani3", 0, 360, img3)
	ani3.Duration = 1500
	ani3.RepeatCount = 2
	ani3.SetInterpolator(ani3.INTERPOLATOR_BOUNCE)
	ani3.Start(img3)
	
	t3.Enabled = False
	t4.Enabled = True
End Sub

Sub t4_Tick
	'4th Animation
	img4.Visible = True
	ani4.InitializeScaleCenter("ani", 0, 0, 1, 1, img4)
	ani4.Duration = 1500
	ani4.RepeatCount = 2
	ani4.SetInterpolator(ani4.INTERPOLATOR_BOUNCE)
	ani4.Start(img4)
	t4.Enabled = False
	img3.Visible = False
	
	it1.Enabled = True
	it2.Enabled = True
	it3.Enabled = True
	it4.Enabled = True
	it5.Enabled = True
	it6.Enabled = True
	it7.Enabled = True
	it8.Enabled = True
	it9.Enabled = True
	it10.Enabled = True
	it11.Enabled = True
	it12.Enabled = True
	it13.Enabled = True
	it14.Enabled = True
	it15.Enabled = True
	
	b1t.Enabled = True
	b2t.Enabled = True
	b3t.Enabled = True
	b4t.Enabled = True
	b5t.Enabled = True
	b6t.Enabled = True
End Sub


'Falling Style ###################################################################
Sub it1_Tick
	'i1Ani
i1.Visible = True
i1ani.SlideFadeToBottom("i1ani",1200,10000)
i1ani.StartAnim(i1)
it1.Enabled = False
End Sub

Sub it2_Tick
	'i2Ani
i2.Visible = True
i2ani.SlideFadeToBottom("i2ani",1200,10000)
i2ani.StartAnim(i2)
it2.Enabled = False
End Sub

Sub it3_Tick
	'i3Ani
i3.Visible = True
i3ani.SlideFadeToBottom("i3ani",1200,10000)
i3ani.StartAnim(i3)
it3.Enabled = False
End Sub

Sub it4_Tick
	'i4Ani
i4.Visible = True
i4ani.SlideFadeToBottom("i4ani",1200,10000)
i4ani.StartAnim(i4)
it4.Enabled = False
End Sub

Sub it5_Tick
	'i5Ani
i5.Visible = True
i5ani.SlideFadeToBottom("i5ani",1200,10000)
i5ani.StartAnim(i5)
it5.Enabled = False
End Sub

Sub it6_Tick
	'i6Ani
i6.Visible = True
i6ani.SlideFadeToBottom("i6ani",1200,10000)
i6ani.StartAnim(i6)
it6.Enabled = False
End Sub

Sub it7_Tick
	'i7Ani
i7.Visible = True
i7ani.SlideFadeToBottom("i7ani",1200,10000)
i7ani.StartAnim(i7)
it7.Enabled = False
End Sub

Sub it8_Tick
	'i8Ani
i8.Visible = True
i8ani.SlideFadeToBottom("i8ani",1200,10000)
i8ani.StartAnim(i8)
it8.Enabled = False
End Sub

Sub it9_Tick
	'i9Ani
i9.Visible = True
i9ani.SlideFadeToBottom("i9ani",1200,10000)
i9ani.StartAnim(i9)
it9.Enabled = False
End Sub

Sub it10_Tick
	'i10Ani
i10.Visible = True
i10ani.SlideFadeToBottom("i10ani",1200,10000)
i10ani.StartAnim(i10)
it10.Enabled = False
End Sub

Sub it11_Tick
	'i11Ani
i11.Visible = True
i11ani.SlideFadeToBottom("i11ani",1200,10000)
i11ani.StartAnim(i11)
it11.Enabled = False
End Sub

Sub it12_Tick
	'i12Ani
i12.Visible = True
i12ani.SlideFadeToBottom("i12ani",1200,10000)
i12ani.StartAnim(i12)
it12.Enabled = False
End Sub

Sub it13_Tick
	'i13Ani
i13.Visible = True
i13ani.SlideFadeToBottom("i13ani",1200,10000)
i13ani.StartAnim(i13)
it13.Enabled = False
End Sub

Sub it14_Tick
	'i14Ani
i14.Visible = True
i14ani.SlideFadeToBottom("i14ani",1200,10000)
i14ani.StartAnim(i14)
it14.Enabled = False
End Sub

Sub it15_Tick
	'i15Ani
i15.Visible = True
i15ani.SlideFadeToBottom("i15ani",1200,10000)
i15ani.StartAnim(i15)
it15.Enabled = False
End Sub

Sub b1t_Tick
	'b1Ani
b1.Visible = True
b1ani.SlideFadeToBottom("b1ani",1200,10000)
b1ani.StartAnim(b1)
b1t.Enabled = False
End Sub

Sub b2t_Tick
	'b2Ani
b2.Visible = True
b2ani.SlideFadeToBottom("b2ani",1200,10000)
b2ani.StartAnim(b2)
b2t.Enabled = False
End Sub

Sub b3t_Tick
	'b3Ani
b3.Visible = True
b3ani.SlideFadeToBottom("b3ani",1200,10000)
b3ani.StartAnim(b3)
b3t.Enabled = False
End Sub

Sub b4t_Tick
	'b4Ani
b4.Visible = True
b4ani.SlideFadeToBottom("b4ani",1200,10000)
b4ani.StartAnim(b4)
b4t.Enabled = False
End Sub

Sub b5t_Tick
	'b5Ani
b5.Visible = True
b5ani.SlideFadeToBottom("b5ani",1200,10000)
b5ani.StartAnim(b5)
b5t.Enabled = False
End Sub

Sub b6t_Tick
	'b6Ani
b6.Visible = True
b6ani.SlideFadeToBottom("b6ani",1200,10000)
b6ani.StartAnim(b6)
b6t.Enabled = False
End Sub

'AniMated ################################################################################
Sub i1ani_animationend
	i1ani.StartAnim(i1)
End Sub

Sub i2ani_animationend
	i2ani.StartAnim(i2)
End Sub

Sub i3ani_animationend
	i3ani.StartAnim(i3)
End Sub

Sub i4ani_animationend
	i4ani.StartAnim(i4)
End Sub

Sub i5ani_animationend
	i5ani.StartAnim(i5)
End Sub

Sub i6ani_animationend
	i6ani.StartAnim(i6)
End Sub

Sub i7ani_animationend
	i7ani.StartAnim(i7)
End Sub

Sub i8ani_animationend
	i8ani.StartAnim(i8)
End Sub

Sub i9ani_animationend
	i9ani.StartAnim(i9)
End Sub

Sub i10ani_animationend
	i10ani.StartAnim(i10)
End Sub

Sub i11ani_animationend
	i11ani.StartAnim(i11)
End Sub

Sub i12ani_animationend
	i12ani.StartAnim(i12)
End Sub

Sub i13ani_animationend
	i13ani.StartAnim(i13)
End Sub

Sub i14ani_animationend
	i14ani.StartAnim(i14)
End Sub

Sub i15ani_animationend
	i15ani.StartAnim(i15)
End Sub

Sub b1ani_animationend
	b1ani.StartAnim(b1)
End Sub

Sub b2ani_animationend
	b2ani.StartAnim(b2)
End Sub

Sub b3ani_animationend
	b3ani.StartAnim(b3)
End Sub

Sub b4ani_animationend
	b4ani.StartAnim(b4)
End Sub

Sub b5ani_animationend
	b5ani.StartAnim(b5)
End Sub

Sub b6ani_animationend
b6ani.StartAnim(b6)
End Sub

Sub sm_Click
	mp.Stop
End Sub

Sub img2_Click
	img2.Visible = False
	p1.Visible = True
	sl1.Enabled = False
	sl2.Enabled = False
	sl3.Enabled = False
	sl4.Enabled = False
End Sub

Sub p1_Click
	p1.Visible = False
	p2.Visible = True
	sl1.Enabled = False
	sl2.Enabled = False
	sl3.Enabled = False
	sl4.Enabled = False
	sl5.Enabled = False
End Sub

Sub p2_Click
	p2.Visible = False
	p3.Visible = True
	sl1.Enabled = False
	sl2.Enabled = False
	sl3.Enabled = False
	sl4.Enabled = False
	sl5.Enabled = False
End Sub

Sub p3_Click
	p3.Visible = False
	p4.Visible = True
	sl1.Enabled = False
	sl2.Enabled = False
	sl3.Enabled = False
	sl4.Enabled = False
	sl5.Enabled = False
End Sub

Sub p4_Click
	p4.Visible = False
	p5.Visible = True
	sl1.Enabled = False
	sl2.Enabled = False
	sl3.Enabled = False
	sl4.Enabled = False
	sl5.Enabled = False
End Sub

Sub p5_Click
	p5.Visible = False
	img2.Visible = True
	sl1.Enabled = False
	sl2.Enabled = False
	sl3.Enabled = False
	sl4.Enabled = False
	sl5.Enabled = False
End Sub

Sub sl1_Tick
	img2.Visible = False
	p1.Visible = True
	sl1.Enabled = False
End Sub

Sub sl2_Tick
	p1.Visible = False
	p2.Visible = True
	sl2.Enabled = False
End Sub

Sub sl3_Tick
	p2.Visible = False
	p3.Visible = True
	sl3.Enabled = False
End Sub

Sub sl4_Tick
	p3.Visible = False
	p4.Visible = True
	sl4.Enabled = False
End Sub

Sub sl5_Tick
	p4.Visible = False
	p5.Visible = True
	sl5.Enabled = False
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub Activity_KeyPress (KeyCode As Int) As Boolean
  Dim Answ As Int
  If KeyCode = KeyCodes.KEYCODE_BACK Then
    Answ = Msgbox2("Do you want to Exit App?", "Attention!", "Yes", "", "No", Null)
    If Answ = DialogResponse.NEGATIVE Then
      Return True
    End If
	End If
  If Answ = DialogResponse.POSITIVE Then
  	mp.Stop
  Return False
  End If
End Sub