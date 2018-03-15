package com.example.huiyiqiandaotv.ui;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TextView;


import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.bumptech.glide.Glide;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.huiyiqiandaotv.MyApplication;
import com.example.huiyiqiandaotv.R;
import com.example.huiyiqiandaotv.beans.BaoCunBean;
import com.example.huiyiqiandaotv.beans.BaoCunBeanDao;


import com.example.huiyiqiandaotv.beans.MoShengRenBean;
import com.example.huiyiqiandaotv.beans.MoShengRenBeanDao;

import com.example.huiyiqiandaotv.beans.QianDaoIdDao;

import com.example.huiyiqiandaotv.beans.ShiBieBean;
import com.example.huiyiqiandaotv.beans.TanChuangBean;
import com.example.huiyiqiandaotv.beans.TanChuangBeanDao;

import com.example.huiyiqiandaotv.beans.WBBean;
import com.example.huiyiqiandaotv.donghua.ExplosionField;
import com.example.huiyiqiandaotv.interfaces.RecytviewCash;
import com.example.huiyiqiandaotv.service.AlarmReceiver;
import com.example.huiyiqiandaotv.tts.control.InitConfig;
import com.example.huiyiqiandaotv.tts.control.MySyntherizer;
import com.example.huiyiqiandaotv.tts.control.NonBlockSyntherizer;
import com.example.huiyiqiandaotv.tts.listener.UiMessageListener;
import com.example.huiyiqiandaotv.tts.util.OfflineResource;
import com.example.huiyiqiandaotv.utils.DateUtils;
import com.example.huiyiqiandaotv.utils.GsonUtil;
import com.example.huiyiqiandaotv.utils.Utils;
import com.example.huiyiqiandaotv.view.DividerItemDecoration;
import com.example.huiyiqiandaotv.view.GlideCircleTransform;
import com.example.huiyiqiandaotv.view.GlideRoundTransform;
import com.example.huiyiqiandaotv.view.WrapContentLinearLayoutManager;

import com.github.florent37.viewanimator.ViewAnimator;
import com.google.gson.Gson;

import com.google.gson.JsonObject;
import com.sdsmdg.tastytoast.TastyToast;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Vector;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class YiDongNianHuiActivity extends Activity implements RecytviewCash {
	private final static String TAG = "WebsocketPushMsg";
//	private IjkVideoView ijkVideoView;
	private MyReceiver myReceiver=null;
	//private SurfaceView surfaceview;
	private RecyclerView recyclerView;
	private MyAdapter adapter=null;
	private RecyclerView recyclerView2;
	private MyAdapter2 adapter2=null;
	private MoShengRenBeanDao daoSession=null;
	//private SpeechSynthesizer mSpeechSynthesizer;
	private WrapContentLinearLayoutManager manager;
	private WrapContentLinearLayoutManager manager2;
	private static  WebSocketClient webSocketClient=null;
	private ExplosionField mExplosionField;
//	private MediaPlayer mediaPlayer=null;
//	private IVLCVout vlcVout=null;
//	private IVLCVout.Callback callback;
//	private LibVLC libvlc;
//	private Media media;
//	private SurfaceHolder mSurfaceHolder;
	private String zhuji=null;
	private static final String zhuji2="http://121.46.3.20";
	private  static Vector<TanChuangBean> tanchuangList=null;
	private  static Vector<TanChuangBean> yuangongList=null;
	private int dw,dh;
	private ImageView dabg;
	private BaoCunBeanDao baoCunBeanDao=null;
	private BaoCunBean baoCunBean=null;
	private NetWorkStateReceiver netWorkStateReceiver=null;
	private TextView wangluo;
	private boolean isLianJie=false;
	private TanChuangBeanDao tanChuangBeanDao=null;
	private Typeface typeFace1;
	private String zhanghuID=null,huiyiID=null;
	protected Handler mainHandler;
	private String appId = "10588094";
	private String appKey = "dfudSSFfNNhDCDoK7UG9G5jn";
	private String secretKey = "9BaCHNSTw3TGRgTKht4ZZvPEb2fjKEC8";
	// TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
	private TtsMode ttsMode = TtsMode.MIX;
	// 离线发音选择，VOICE_FEMALE即为离线女声发音。
	// assets目录下bd_etts_speech_female.data为离线男声模型；bd_etts_speech_female.data为离线女声模型
	private String offlineVoice = OfflineResource.VOICE_FEMALE;
	// 主控制类，所有合成控制方法从这个类开始
	private MySyntherizer synthesizer;
	private TextView shi,riqi,xingqi,tianqi,wendu,tishi_tv33,jian,dian;
	private QianDaoIdDao qianDaoIdDao=null;
	private ImageView bao;
	private MyReceiverFile myReceiverFile;


	public  Handler handler=new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(final Message msg) {

			//识别
			if (msg.arg1==1){

				ShiBieBean.PersonBeanSB dataBean= (ShiBieBean.PersonBeanSB) msg.obj;

				try {

					final TanChuangBean bean=new TanChuangBean();
					bean.setBytes(null);
					bean.setIdid(dataBean.getId());
					bean.setType(dataBean.getSubject_type());
					bean.setName(dataBean.getName());
					bean.setTouxiang(dataBean.getAvatar());
					switch (dataBean.getSubject_type()){
						case 0: //员工
							Log.d(TAG, "员工");
							yuangongList.add(bean);
							adapter2.notifyItemInserted(yuangongList.size());
							manager2.scrollToPosition(yuangongList.size()-1);
							new Thread(new Runnable() {
								@Override
								public void run() {

									try {
										Thread.sleep(15000);

										Message message=Message.obtain();
										message.what=110;
										handler.sendMessage(message);


									} catch (InterruptedException e) {
										e.printStackTrace();
									}


								}
							}).start();

							break;
						case 1: //普通访客
							Log.d(TAG, "普通访客");

							mExplosionField.explode(bao);

							tanchuangList.add(bean);
							adapter.notifyItemInserted(tanchuangList.size());
							manager.scrollToPosition(tanchuangList.size()-1);
							new Thread(new Runnable() {
								@Override
								public void run() {

									try {
										Thread.sleep(15000);

										Message message=Message.obtain();
										message.what=999;
										handler.sendMessage(message);

									} catch (InterruptedException e) {
										e.printStackTrace();
									}


								}
							}).start();

							break;
						case 2:  //VIP访客
							Log.d(TAG, "VIP");
//								if (!fllowerAnimation.isRunings()){
//									fllowerAnimation.startAnimation();
//								}

							mExplosionField.explode(bao);
							//int z=tanchuangList.size();
							int a=0;
							for (int i2=0;i2<tanchuangList.size();i2++){
								if (tanchuangList.get(i2).getIdid()==bean.getIdid()){
									a=1;
								}
							}
							if (a==0){
								tanchuangList.add(bean);
								adapter.notifyItemInserted(tanchuangList.size());
								manager.scrollToPosition(tanchuangList.size()-1);
								new Thread(new Runnable() {
									@Override
									public void run() {

										try {
											Thread.sleep(15000);

											Message message=Message.obtain();
											message.what=999;
											handler.sendMessage(message);


										} catch (InterruptedException e) {
											e.printStackTrace();
										}


									}
								}).start();
							}

							break;

					}



				} catch (Exception e) {
					//Log.d("WebsocketPushMsg", e.getMessage());
					e.printStackTrace();
				}

			}

			switch (msg.what){
				case 111:
					//更新地址

					break;
				case 110:
					//员工弹窗消失

					if (yuangongList.size()>2) {
						yuangongList.remove(2);

						adapter2.notifyItemRemoved(2);
						//adapter.notifyItemChanged(1);
						//adapter.notifyItemRangeChanged(1,tanchuangList.size());
						//adapter.notifyDataSetChanged();
						manager2.scrollToPosition(yuangongList.size() - 1);
						//Log.d(TAG, "tanchuangList.size():" + tanchuangList.size());


					}


					break;
				case 999:
					//访客弹窗消失

					if (tanchuangList.size()>1) {
						tanchuangList.remove(1);

						adapter.notifyItemRemoved(1);
						//adapter.notifyItemChanged(1);
						//	adapter.notifyItemRangeChanged(1,tanchuangList.size());
						//adapter.notifyDataSetChanged();
						manager.scrollToPosition(tanchuangList.size() - 1);
						//Log.d(TAG, "tanchuangList.size():" + tanchuangList.size());

					}


					break;
				case 19: //更新识别记录
					int size=yuangongList.size();

					//adapter2.notifyItemInserted(size-1);
					//manager2.smoothScrollToPosition(recyclerView2,null,size-1);

					break;

			}


			return false;
		}
	});


	@Override
	public void reset() {

		//数据重置
		chongzhi();
	}



	private void chongzhi(){
		//yuangongList.clear();
		//tanchuangList.clear();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(600);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						yuangongList.clear();
						tanchuangList.clear();

						TanChuangBean bean=new TanChuangBean();
						bean.setBytes(null);
						bean.setName(null);
						bean.setType(-2);
						bean.setTouxiang(null);
						tanchuangList.add(bean);

						TanChuangBean bean3=new TanChuangBean();
						bean3.setBytes(null);
						bean3.setName(null);
						bean3.setType(-2);
						bean3.setTouxiang(null);
						yuangongList.add(bean3);

						TanChuangBean bean4=new TanChuangBean();
						bean4.setBytes(null);
						bean4.setName(null);
						bean4.setType(-2);
						bean4.setTouxiang(null);
						yuangongList.add(bean4);

						if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || (!recyclerView.isComputingLayout())) {
							adapter.notifyDataSetChanged();
						}
					}
				});

			}
		}).start();

	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//	Log.d(TAG, "创建111");


		qianDaoIdDao=MyApplication.myApplication.getDaoSession().getQianDaoIdDao();
		tanChuangBeanDao=MyApplication.myApplication.getDaoSession().getTanChuangBeanDao();
		baoCunBeanDao = MyApplication.myApplication.getDaoSession().getBaoCunBeanDao();
		baoCunBean = baoCunBeanDao.load(123456L);
		if (baoCunBean == null) {
			BaoCunBean baoCunBea = new BaoCunBean();
			baoCunBea.setId(123456L);
			baoCunBeanDao.insert(baoCunBea);
			baoCunBean = baoCunBeanDao.load(123456L);
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
		//DisplayMetrics dm = getResources().getDisplayMetrics();
		dw = Utils.getDisplaySize(YiDongNianHuiActivity.this).x;
		dh = Utils.getDisplaySize(YiDongNianHuiActivity.this).y;

		setContentView(R.layout.yidongnianhuiactivity);
		bao= (ImageView) findViewById(R.id.bao);
		wangluo = (TextView) findViewById(R.id.wangluo);
		//typeFace1 = Typeface.createFromAsset(getAssets(), "fonts/xk.TTF");

		Typeface typeFace =Typeface.createFromAsset(getAssets(),"fonts/led.ttf");
		shi= (TextView) findViewById(R.id.shi);
		jian= (TextView) findViewById(R.id.jian);
		dian= (TextView) findViewById(R.id.dian);
		riqi= (TextView) findViewById(R.id.riqi);
		xingqi= (TextView) findViewById(R.id.xingqi);

		//使用字体
		riqi.setTypeface(typeFace);
		shi.setTypeface(typeFace);
		dian.setTypeface(typeFace);
		jian.setTypeface(typeFace);

		final String time=(System.currentTimeMillis()/1000)+"";
		shi.setText(DateUtils.timeShi(time));
		jian.setText(DateUtils.timeJian(time));
		riqi.setText(DateUtils.timesTwo(time));
		Animation animation = AnimationUtils.loadAnimation(YiDongNianHuiActivity.this, R.anim.alpha_anim);
		animation.setRepeatCount(-1);
		dian.setAnimation(animation);

		tanchuangList=new Vector<>();
		yuangongList = new Vector<>();
		TanChuangBean bean=new TanChuangBean();
		bean.setBytes(null);
		bean.setName(null);
		bean.setType(-2);
		bean.setTouxiang(null);
		tanchuangList.add(bean);

		TanChuangBean bean3=new TanChuangBean();
		bean3.setBytes(null);
		bean3.setName(null);
		bean3.setType(-2);
		bean3.setTouxiang(null);
		yuangongList.add(bean3);

		TanChuangBean bean4=new TanChuangBean();
		bean4.setBytes(null);
		bean4.setName(null);
		bean4.setType(-2);
		bean4.setTouxiang(null);
		yuangongList.add(bean4);

		Button button = (Button) findViewById(R.id.dddk);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				chongzhi();

				startActivity(new Intent(YiDongNianHuiActivity.this, SheZhiActivity.class));
				finish();
			}
		});


		IntentFilter filter = null;


		myReceiverFile=new MyReceiverFile();
		filter = new IntentFilter();
		filter.addAction(Intent.ACTION_MEDIA_MOUNTED);   //接受外媒挂载过滤器
		filter.addAction(Intent.ACTION_MEDIA_REMOVED);   //接受外媒挂载过滤器
		filter.addDataScheme("file");
		registerReceiver(myReceiverFile, filter);

		//实例化过滤器并设置要过滤的广播
		myReceiver = new MyReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("duanxianchonglian");
		intentFilter.addAction("gxshipingdizhi");
		intentFilter.addAction("shoudongshuaxin");
		intentFilter.addAction("updateGonggao");
		intentFilter.addAction("updateTuPian");
		intentFilter.addAction("updateShiPing");
		intentFilter.addAction("delectShiPing");
		intentFilter.addAction("guanbi");
		intentFilter.addAction(Intent.ACTION_TIME_TICK);

		// 注册广播
		registerReceiver(myReceiver, intentFilter);

		dabg= (ImageView) findViewById(R.id.dabg);

		daoSession = MyApplication.getAppContext().getDaoSession().getMoShengRenBeanDao();
		daoSession.deleteAll();
		recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		recyclerView2 = (RecyclerView) findViewById(R.id.recyclerView2);
//		recyclerView2.addOnScrollListener(new RecyclerView.OnScrollListener() {
//			//用来标记是否正在向最后一个滑动
//			boolean isSlidingToLast = false;
//
//			@Override
//			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//				super.onScrollStateChanged(recyclerView, newState);
//				LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
//				// 当不滚动时
//				if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//					//获取最后一个完全显示的ItemPosition
//					int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
//					int totalItemCount = manager.getItemCount();
//
//					// 判断是否滚动到底部，并且是向右滚动
//					if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
//						//加载更多功能的代码
//						manager2.smoothScrollToPosition(recyclerView2,null,0);
//					}
//
//					if (lastVisibleItem==4 && !isSlidingToLast){
//						manager2.smoothScrollToPosition(recyclerView2,null,shiBieJiLuList.size()-1);
//					}
//
//				}
//			}
//
//			@Override
//			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//				super.onScrolled(recyclerView, dx, dy);
//
//				//dx用来判断横向滑动方向，dy用来判断纵向滑动方向
//				//大于0表示正在向下滚动
//				//小于等于0表示停止或向上滚动
//				isSlidingToLast = dy > 0;
//			}
//		});
		//	mSurfaceView.setLayerType(View.LAYER_TYPE_HARDWARE, null);


		manager = new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false,this);
		recyclerView.setLayoutManager(manager);

		manager2 = new WrapContentLinearLayoutManager(YiDongNianHuiActivity.this,LinearLayoutManager.HORIZONTAL, false,this);
		manager2.setOrientation(LinearLayoutManager.HORIZONTAL);
		recyclerView2.setLayoutManager(manager2);

		adapter = new MyAdapter(R.layout.tanchuang_item2,tanchuangList);

		adapter2 = new MyAdapter2(R.layout.shibiejilu_item,yuangongList);

		recyclerView.setAdapter(adapter);
		recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

		recyclerView2.setAdapter(adapter2);
		recyclerView2.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));

		mainHandler = new Handler() {
			/*
             * @param msg
             */

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				//Log.d(TAG, "msg:" + msg);
			}

		};

		//Utils.initPermission(YiDongNianHuiActivity.this);
		initialTts();



		new Thread(new Runnable() {
			@Override
			public void run() {

				SystemClock.sleep(10000);
				sendBroadcast(new Intent(YiDongNianHuiActivity.this,AlarmReceiver.class));
			}
		}).start();



//		File logFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"qiandao.txt");
//		// Make sure log file is exists
//		if (!logFile.exists()) {
//
//			try {
//				logFile.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//
//			}
//
//		}
//
//		FileOutputStream outputStream;
//
//		try {
//			outputStream = openFileOutput(logFile.getName(), Context.MODE_APPEND);
//			outputStream.write(builder.toString().getBytes());
//			outputStream.flush();
//			outputStream.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}



	}

	/**
	 * 初始化引擎，需要的参数均在InitConfig类里
	 * <p>
	 * DEMO中提供了3个SpeechSynthesizerListener的实现
	 * MessageListener 仅仅用log.i记录日志，在logcat中可以看见
	 * UiMessageListener 在MessageListener的基础上，对handler发送消息，实现UI的文字更新
	 * FileSaveListener 在UiMessageListener的基础上，使用 onSynthesizeDataArrived回调，获取音频流
	 */
	protected void initialTts() {
		// 设置初始化参数
		SpeechSynthesizerListener listener = new UiMessageListener(mainHandler); // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
		Map<String, String> params = getParams();
		// appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
		InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);
		synthesizer = new NonBlockSyntherizer(this, initConfig, mainHandler); // 此处可以改为MySyntherizer 了解调用过程


	}

	/**
	 * 合成的参数，可以初始化时填写，也可以在合成前设置。
	 *
	 * @return
	 */
	protected Map<String, String> getParams() {
		Map<String, String> params = new HashMap<String, String>();
		// 以下参数均为选填
		params.put(SpeechSynthesizer.PARAM_SPEAKER, baoCunBean.getBoyingren()+""); // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
		params.put(SpeechSynthesizer.PARAM_VOLUME, "8"); // 设置合成的音量，0-9 ，默认 5
		params.put(SpeechSynthesizer.PARAM_SPEED, baoCunBean.getYusu()+"");// 设置合成的语速，0-9 ，默认 5
		params.put(SpeechSynthesizer.PARAM_PITCH, baoCunBean.getYudiao()+"");// 设置合成的语调，0-9 ，默认 5
		params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);         // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
		// MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
		// MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
		// MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
		// MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线

        // 离线资源文件
        OfflineResource offlineResource = createOfflineResource(offlineVoice);
        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
        params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
        params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
                offlineResource.getModelFilename());

		return params;
	}
    protected OfflineResource createOfflineResource(String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(this, voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
           // toPrint("【error】:copy files from assets failed." + e.getMessage());
        }
        return offlineResource;
    }

	public  class MyAdapter extends BaseQuickAdapter<TanChuangBean,BaseViewHolder> {
		private View view;
		//	private List<TanChuangBean> d;
		private int p;
		private int vid;


		private MyAdapter(int layoutResId, List<TanChuangBean> data) {
			super(layoutResId, data);
			vid=layoutResId;
			//d=data;
		}



		@Override
		protected void convert(final BaseViewHolder helper, TanChuangBean item) {
			//Log.d(TAG, "动画执行");

			AnimatorSet animatorSet = new AnimatorSet();
			animatorSet.playTogether(
					ObjectAnimator.ofFloat(helper.itemView,"scaleY",0f,1f),
					ObjectAnimator.ofFloat(helper.itemView,"scaleX",0f,0f)
					//	ObjectAnimator.ofFloat(helper.itemView,"alpha",0f,1f)
			);
			animatorSet.setDuration(200);
			animatorSet.setInterpolator(new AccelerateInterpolator());
			animatorSet.addListener(new AnimatorListenerAdapter(){
				@Override public void onAnimationEnd(Animator animation) {

					AnimatorSet animatorSet2 = new AnimatorSet();
					animatorSet2.playTogether(
							ObjectAnimator.ofFloat(helper.itemView,"scaleX",0f,1f)
							//ObjectAnimator.ofFloat(helper.itemView,"alpha",0f,1f)
							//	ObjectAnimator.ofFloat(helper.itemView,"scaleY",1f,0.5f,1f)
					);
					animatorSet2.setInterpolator(new AccelerateInterpolator());
					animatorSet2.setDuration(500);
					animatorSet2.start();

				}
			});
			animatorSet.start();


//			ViewAnimator
//					.animate(helper.itemView)
//				//	.scale(0,1)
//					.alpha(0,1)
//					.duration(1000)
//					.start();

			RelativeLayout toprl= helper.getView(R.id.ffflll);


			TextView tishi_tv= helper.getView(R.id.tishi_tv);
			TextView tishi_tv2= helper.getView(R.id.ddd);
			ImageView imageView= helper.getView(R.id.touxiang);

			if (helper.getAdapterPosition()==0 ){
				toprl.setBackgroundColor(Color.parseColor("#00000000"));
				tishi_tv.setText("");
				tishi_tv2.setText("");
				imageView.setImageBitmap(null);

			}else {

			switch (item.getType()){
				case -1:
					//陌生人
					//	toprl.setBackgroundResource(R.drawable.tanchuang);


					break;
				case 0:
					//员工
					//	toprl.setBackgroundResource(R.drawable.tanchuang);
					//	tishi_tv.setText("欢迎老板");
					//	tishi_im.setBackgroundResource(R.drawable.tike);

					break;

				case 1:
					//访客

					view=toprl;
					toprl.setBackgroundResource(R.drawable.fgfgfg);
					String sa="热烈欢迎"+item.getName()+"莅临参观指导";
					StringBuilder sb=new StringBuilder();
					for(int i=0;i<sa.length();i++){
						sb.append((sa.charAt(i)));//依次加入sb中
						if((i+1)%(8)==0 &&((i+1)!=sa.length())){
							sb.append("\n");
						}
					}

					tishi_tv.setText(sb.toString());

					break;
				case 2:
					//VIP访客
					view=toprl;
					toprl.setBackgroundResource(R.drawable.fgfgfg);
					String sa1="热烈欢迎"+item.getName()+"莅临参观指导";
					StringBuilder sb1=new StringBuilder();
					for(int i=0;i<sa1.length();i++){
						sb1.append((sa1.charAt(i)));//依次加入sb中
						if((i+1)%(8)==0 &&((i+1)!=sa1.length())){
							sb1.append("\n");
						}
					}

					tishi_tv.setText(sb1.toString());

					break;

			}


			if (item.getTouxiang()!=null){

				Glide.with(MyApplication.getAppContext())
						.load(baoCunBean.getTouxiangzhuji()+item.getTouxiang())
						//.transform(new GlideCircleTransform(MyApplication.getAppContext(),2,Color.parseColor("#ffffffff")))
						.transform(new GlideRoundTransform(MyApplication.getAppContext(), 6))
						.into((ImageView) helper.getView(R.id.touxiang));
			}else {
				Glide.with(MyApplication.getAppContext())
						.load(item.getBytes())
						//.transform(new GlideCircleTransform(MyApplication.getAppContext(),2,Color.parseColor("#ffffffff")))
						.transform(new GlideRoundTransform(MyApplication.getAppContext(), 6))
						.into((ImageView) helper.getView(R.id.touxiang));
			}
		}
		}


	}


	private  class MyAdapter2 extends BaseQuickAdapter<TanChuangBean,BaseViewHolder> {

		private MyAdapter2(int layoutResId, List<TanChuangBean> data) {
			super(layoutResId, data);

		}

		@Override
		protected void convert(BaseViewHolder helper, TanChuangBean item) {
			ViewAnimator
					.animate(helper.itemView)
					.scale(0,1)
//					.alpha(0,1)
					.duration(1000)
					.start();

			RelativeLayout toprl= helper.getView(R.id.ffflll);

			ImageView imageView= helper.getView(R.id.touxiang22);
			//ImageView tishi_im= helper.getView(R.id.tishi_im);
			TextView tishi_tv= helper.getView(R.id.tishi_tv);

			if (helper.getAdapterPosition()==0 || helper.getAdapterPosition()==1){
				toprl.setBackgroundColor(Color.parseColor("#00000000"));
				tishi_tv.setText("");
				imageView.setImageBitmap(null);

			}else {

				switch (item.getType()) {
					case -1:
						//陌生人
						//	toprl.setBackgroundResource(R.drawable.tanchuang);

						break;
					case 0:
						//员工

						toprl.setBackgroundResource(R.drawable.gfgfgf);
						tishi_tv.setText(item.getName());
						//Log.d("SheZhiActivity", "名字0"+item.getName());

						break;

					case 1:
						//访客
						//	toprl.setBackgroundResource(R.drawable.tanchuang);

						//richeng.setText("");
						//name.setText(item.getName());
						//autoScrollTextView.setText("欢迎你来本公司参观指导。");
						//Log.d("SheZhiActivity", "名字1"+item.getName());
						break;
					case 2:
						//VIP访客
						//toprl.setBackgroundResource(R.drawable.tanchuang);
						//	richeng.setText("");
						//	name.setText(item.getName());
						//autoScrollTextView.setText("欢迎VIP访客 "+item.getName()+" 来本公司指导工作。");
						//Log.d("SheZhiActivity", "名字2"+item.getName());
						break;


				}
				if (item.getTouxiang()!=null){

					Glide.with(MyApplication.getAppContext())
							.load(baoCunBean.getTouxiangzhuji()+item.getTouxiang())
							.transform(new GlideCircleTransform(MyApplication.getAppContext()))
							.into((ImageView) helper.getView(R.id.touxiang22));
				}else {

					Glide.with(MyApplication.getAppContext())
							.load(item.getBytes())
							.transform(new GlideCircleTransform(MyApplication.getAppContext()))
							.into((ImageView) helper.getView(R.id.touxiang22));
				}
			}




		}
	}

//	/**
//	 * 生成二维码
//	 * @param string 二维码中包含的文本信息
//	 * @param mBitmap logo图片
//	 * @param format  编码格式
//	 * [url=home.php?mod=space&uid=309376]@return[/url] Bitmap 位图
//	 * @throws WriterException
//	 */
//	private static final int IMAGE_HALFWIDTH = 1;//宽度值，影响中间图片大小
//	public Bitmap createCode(String string,Bitmap mBitmap, BarcodeFormat format)
//			throws WriterException {
//		Matrix m = new Matrix();
//		float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
//		float sy = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getHeight();
//		m.setScale(sx, sy);//设置缩放信息
//		//将logo图片按martix设置的信息缩放
//		mBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
//				mBitmap.getWidth(), mBitmap.getHeight(), m, false);
//		MultiFormatWriter writer = new MultiFormatWriter();
//		Hashtable<EncodeHintType, String> hst = new Hashtable<EncodeHintType, String>();
//		hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");//设置字符编码
//		BitMatrix matrix = writer.encode(string, format, 600, 600, hst);//生成二维码矩阵信息
//		int width = matrix.getWidth();//矩阵高度
//		int height = matrix.getHeight();//矩阵宽度
//		int halfW = width/2;
//		int halfH = height/2;
//		int[] pixels = new int[width * height];//定义数组长度为矩阵高度*矩阵宽度，用于记录矩阵中像素信息
//		for (int y = 0; y < height; y++) {//从行开始迭代矩阵
//			for (int x = 0; x < width; x++) {//迭代列
//				if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
//						&& y > halfH - IMAGE_HALFWIDTH
//						&& y < halfH + IMAGE_HALFWIDTH) {//该位置用于存放图片信息
//			//记录图片每个像素信息
//					pixels[y * width + x] = mBitmap.getPixel(x - halfW
//							+ IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);              } else {
//					if (matrix.get(x, y)) {//如果有黑块点，记录信息
//						pixels[y * width + x] = 0xff000000;//记录黑块信息
//					}
//				}
//			}
//		}
//		Bitmap bitmap = Bitmap.createBitmap(width, height,
//				Bitmap.Config.ARGB_8888);
//		// 通过像素数组生成bitmap
//		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//		return bitmap;
//	}
	public class MyReceiverFile  extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, final Intent intent) {
			String action = intent.getAction();

			if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
				//USB设备移除，更新UI
				Log.d(TAG, "设备被移出");
				TastyToast.makeText(YiDongNianHuiActivity.this,"设备被移出",TastyToast.LENGTH_SHORT,TastyToast.INFO).show();


			} else if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
				//USB设备挂载，更新UI
				Log.d(TAG, "设备插入");
				String usbPath = intent.getDataString();//（usb在手机上的路径）
				try {

					Glide.with(MyApplication.getAppContext())
							.load(usbPath.split("file:///")[1]+File.separator+"dgx.png")
							.into(dabg);

				}catch (Exception e){
					Log.d(TAG, e.getMessage()+"");
				}


			}

		}
	}


	private class MyReceiver  extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, final Intent intent) {
			//Log.d(TAG, "intent:" + intent.getAction());

			if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
				//ijkVideoView.requestFocus();
				String time=(System.currentTimeMillis()/1000)+"";
				shi.setText(DateUtils.timeShi(time));
				jian.setText(DateUtils.timeJian(time));
				riqi.setText(DateUtils.timesTwo(time));
				xingqi.setText(DateUtils.getWeek(System.currentTimeMillis()));


			}
		}
	}

	// 遍历接收一个文件路径，然后把文件子目录中的所有文件遍历并输出来
	public static void getAllFiles(File root,String nameaaa){
		File files[] = root.listFiles();

		if(files != null){
			for (File f : files){
				if(f.isDirectory()){
					getAllFiles(f,nameaaa);
				}else{
					String name=f.getName();
					if (name.equals(nameaaa)){
						Log.d(TAG, "视频文件删除:" + f.delete());
					}
				}
			}
		}
	}

	private void link_fasong(String discernPhoto,int timestamp,long id,String name,String weizhi) {
		//Log.d(TAG, DateUtils.time(timestamp + "000"));
		OkHttpClient okHttpClient= MyApplication.getOkHttpClient();
		RequestBody body = new FormBody.Builder()
				.add("accountId",baoCunBean.getZhanghuId())
                .add("snapshotPhoto","")
                .add("discernPhoto",discernPhoto)
				.add("timestamp2",DateUtils.time(timestamp+"000"))
				.add("subjectId",id+"")
				.add("subjectName",name)
				.add("screenPosition",weizhi)
				.add("conference_id",baoCunBean.getHuiyiId())
				.build();
		Request.Builder requestBuilder = new Request.Builder()
				//.header("Content-Type", "application/json")
				.post(body)
				.url(baoCunBean.getHoutaiDiZhi()+"/appSave.do");
		//Log.d(TAG, baoCunBean.getHoutaiDiZhi() + "/appSave.do");
		// step 3：创建 Call 对象
		Call call = okHttpClient.newCall(requestBuilder.build());

		//step 4: 开始异步请求
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

				Log.d("AllConnects", "请求获取二维码失败"+e.getMessage());

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				//Log.d("AllConnects", "请求获取二维码成功"+call.request().toString());
				//获得返回体
				//List<YongHuBean> yongHuBeanList=new ArrayList<>();
				//List<MoShengRenBean2> intlist=new ArrayList<>();
			//	intlist.addAll(moShengRenBean2List);
				try {


				ResponseBody body = response.body();
				String ss=body.string();
				  Log.d("AllConnects", "aa   "+ss);

//					JsonObject jsonObject= GsonUtil.parse(body.string()).getAsJsonObject();
//					Gson gson=new Gson();
//						int code=jsonObject.get("resultCode").getAsInt();
//						if (code==0){
//					JsonArray array=jsonObject.get("data").getAsJsonArray();
//					int a=array.size();
//					for (int i=0;i<a;i++){
//						YongHuBean zhaoPianBean=gson.fromJson(array.get(i),YongHuBean.class);
//						moShengRenBean2List.add(zhaoPianBean);
//						//Log.d("VlcVideoActivity", zhaoPianBean.getSubjectId());
//					}

				//	}

				}catch (Exception e){
					Log.d("WebsocketPushMsg", e.getMessage());
				}

			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if( KeyEvent.KEYCODE_MENU == keyCode ){  //如果按下的是菜单
			Log.d(TAG, "按下菜单键 ");
			chongzhi();
			//isTiaoZhuang=false;
			startActivity(new Intent(YiDongNianHuiActivity.this, SheZhiActivity.class));
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {

		mExplosionField = ExplosionField.attach2Window(YiDongNianHuiActivity.this);
		mExplosionField.expandExplosionBound(900,1200);

		if (netWorkStateReceiver == null) {
			netWorkStateReceiver = new NetWorkStateReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			registerReceiver(netWorkStateReceiver, filter);
		}

		baoCunBean=baoCunBeanDao.load(123456L);

		if (baoCunBean!=null && baoCunBean.getZhujiDiZhi()!=null){
			try {
				String[] a1=baoCunBean.getZhujiDiZhi().split("//");
				String[] b1=a1[1].split(":");
				zhuji="http://"+b1[0];
				WebsocketPushMsg websocketPushMsg = new WebsocketPushMsg();
				websocketPushMsg.close();
				if (baoCunBean.getShipingIP() != null ) {
					websocketPushMsg.startConnection(baoCunBean.getZhujiDiZhi(), "rtsp://"+baoCunBean.getShipingIP()+":554/user=admin_password=tlJwpbo6_channel=1_stream=0.sdp?real_stream");
				}
			} catch (URISyntaxException e) {
				Log.d(TAG, e.getMessage()+"ddd");

			}
		}else {
			TastyToast.makeText(YiDongNianHuiActivity.this,"请先设置主机地址和摄像头IP",TastyToast.LENGTH_SHORT,TastyToast.INFO).show();
		}


		super.onResume();
	}


	@Override
	public void onPause() {

		Log.d(TAG, "暂停");

		super.onPause();
	}

	@Override
	protected void onStop() {

		super.onStop();
	}

	@Override
	protected void onDestroy() {
		if (webSocketClient!=null){
			webSocketClient.close();
			webSocketClient=null;
		}

		Intent intent1=new Intent("guanbi333"); //关闭监听服务
		sendBroadcast(intent1);
		synthesizer.release();
		handler.removeCallbacksAndMessages(null);
		if (myReceiver != null)
			unregisterReceiver(myReceiver);
		unregisterReceiver(netWorkStateReceiver);
		unregisterReceiver(myReceiverFile);
		super.onDestroy();

	}


//	private void changeSurfaceSize() {
//		// get screen size
//		int dw = Utils.getDisplaySize(getApplicationContext()).x;
//		int dh = Utils.getDisplaySize(getApplicationContext()).y;
//
////		RelativeLayout.LayoutParams re1 = (RelativeLayout.LayoutParams)surfaceview.getLayoutParams();
////
////		  re1.width=dw/3;
////		  re1.height = dh/5;
////
////		surfaceview.setLayoutParams(re1);
////		surfaceview.invalidate();
//		Log.d(TAG, baoCunBean.getShipingIP()+"hhhhh");
//		if (mediaPlayer != null) {
//			Log.d(TAG, baoCunBean.getShipingIP()+"gggg");
//
//			media = new Media(libvlc, Uri.parse("rtsp://"+baoCunBean.getShipingIP()+"/user=admin&password=&channel=1&stream=0.sdp"));
//			mediaPlayer.setMedia(media);
//			mediaPlayer.play();
//
//		}
//
//	}
//	/**
//	 * websocket接口返回face.image
//	 * image为base64编码的字符串
//	 * 将字符串转为可以识别的图片
//	 * @param imgStr
//	 * @return
//	 */
//	public Bitmap generateImage(String imgStr, int cont, WBWeiShiBieDATABean dataBean, Context context) throws Exception {
//		// 对字节数组字符串进行Base64解码并生成图片
//		if (imgStr == null) // 图像数据为空
//			return null;
//		BASE64Decoder decoder = new BASE64Decoder();
//		try {
//			// Base64解码
//			final byte[][] b = {decoder.decodeBuffer(imgStr)};
//			for (int i = 0; i < b[0].length; ++i) {
//				if (b[0][i] < 0) {// 调整异常数据
//					b[0][i] += 256;
//				}
//			}
//			MoShengRenBean2 moShengRenBean2=new MoShengRenBean2();
//			moShengRenBean2.setId(dataBean.getTrack());
//			moShengRenBean2.setBytes(b[0]);
//			moShengRenBean2.setUrl("dd");
//
//			moShengRenBean2List.add(moShengRenBean2);
//
//				adapter.notifyDataSetChanged();
//
//
//
//
//
//			//   Bitmap bitmap= BitmapFactory.decodeByteArray(b[0],0, b[0].length);
//
//			//  Log.d("WebsocketPushMsg", "bitmap.getHeight():" + bitmap.getHeight());
//
//			// 生成jpeg图片
//			//  OutputStream out = new FileOutputStream(imgFilePath);
//			//   out.write(b);
//			//  out.flush();
//			//  out.close();
//
//
////			dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
////				@Override
////				public void onDismiss(DialogInterface dialog) {
////					Log.d("VlcVideoActivity", "Dialog销毁2");
////					b[0] =null;
////				}
////			});
//			//dialog.show();
//
//
//			return null;
//		} catch (Exception e) {
//			throw e;
//
//		}
//	}

	public  int dip2px(Context context, float dipValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(dipValue * scale + 0.5f);
	}
	public  int px2dip(Context context, float pxValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(pxValue / scale + 0.5f);
	}
	/**
	 * 识别消息推送
	 * 主机盒子端API ws://[主机ip]:9000/video
	 * 通过 websocket 获取 识别结果
	 * @author Wangshutao
	 */
	private class WebsocketPushMsg {
		/** * 识别消息推送
		 * @param wsUrl websocket接口 例如 ws://192.168.1.50:9000/video
		 * @param rtspUrl 视频流地址 门禁管理-门禁设备-视频流地址
		 *                例如 rtsp://192.168.0.100/live1.sdp
		 *                或者 rtsp://admin:admin12345@192.168.1.64/live1.sdp
		 *                或者 rtsp://192.168.1.103/user=admin&password=&channel=1&stream=0.sdp
		 *                或者 rtsp://192.168.1.100/live1.sdp
		 *                       ?__exper_tuner=lingyun&__exper_tuner_username=admin
		 *                       &__exper_tuner_password=admin&__exper_mentor=motion
		 *                       &__exper_levels=312,1,625,1,1250,1,2500,1,5000,1,5000,2,10000,2,10000,4,10000,8,10000,10
		 *                       &__exper_initlevel=6
		 * @throws URISyntaxException
		 * @throws
		 * @throws
		 *
		 *  ://192.168.2.52/user=admin&password=&channel=1&stream=0.sdp
		 *
		 *   rtsp://192.166.2.55:554/user=admin_password=tljwpbo6_channel=1_stream=0.sdp?real_stream
		 */
		private void startConnection(String wsUrl, String rtspUrl) throws URISyntaxException {
			//当视频流地址中出现&符号时，需要进行进行url编码
			if (rtspUrl.contains("&")){
				try {
					//Log.d("WebsocketPushMsg", "dddddttttttttttttttt"+rtspUrl);
					rtspUrl = URLEncoder.encode(rtspUrl,"UTF-8");

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					//Log.d("WebsocketPushMsg", e.getMessage());
				}
			}

			URI uri = URI.create(wsUrl + "?url=" + rtspUrl);
		//	Log.d("WebsocketPushMsg", "url="+uri);
			  webSocketClient = new WebSocketClient(uri) {
			//	private Vector vector=new Vector();

				@Override
				public void onOpen(ServerHandshake serverHandshake) {
					isLianJie=true;
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (!YiDongNianHuiActivity.this.isFinishing())
								wangluo.setVisibility(View.GONE);
						}
					});
				}

				@Override
				public void onMessage(String ss) {

					JsonObject jsonObject= GsonUtil.parse(ss).getAsJsonObject();
					Gson gson=new Gson();
					WBBean wbBean= gson.fromJson(jsonObject, WBBean.class);

					//Log.d("WebsocketPushMsg", wbBean.getType());
					if (wbBean.getType().equals("recognized")) {


						//识别//Log.d("WebsocketPushMsg", "识别出了")
						//String s = ss.replace("\\\\\\", "").replace("\"tag\": \"{\"", "\"tag\": {\"").replace("jpg\"}\"", "jpg\"}");
						//	JsonObject jsonObject5 = GsonUtil.parse(ss).getAsJsonObject();

						//	JsonObject jsonObject1 = jsonObject.get("data").getAsJsonObject();
						//JsonObject jsonObject2 = jsonObject5.get("person").getAsJsonObject();
						//   JsonObject jsonObject3=jsonObject.get("screen").getAsJsonObject();
						final ShiBieBean dataBean = gson.fromJson(jsonObject, ShiBieBean.class);

						try {
							//Log.d("WebsocketPushMsg", dataBean.getPerson().getSrc()+"kkkk");

							//	final WBShiBiePersonBean personBean = gson.fromJson(jsonObject2, WBShiBiePersonBean.class);
							//Log.d("WebsocketPushMsg", "personBean.getSubject_type():" + personBean.getSubject_type());

//						if (dataBean.getPerson().getSubject_type() == 2) {
//
//							//Log.d("WebsocketPushMsg", personBean.getAvatar());
//							runOnUiThread(new Runnable() {
//								@Override
//								public void run() {
//
//									stringVector.add("欢迎VIP访客 "+dataBean.getPerson().getName()+" 来访！ 来访时间: "+DateUtils.getCurrentTime_Today());
//									Collections.reverse(stringVector);
//
//									delet();
//
//									runOnUiThread(new Runnable() {
//										@Override
//										public void run() {
//											marqueeView.stopFlipping();
//											marqueeView.startWithList(stringVector);
//
//										}
//									});
////									VipDialog dialog=new VipDialog(VlcVideoActivity.this,personBean.getAvatar(),R.style.dialog_style,personBean.getName());
////									Log.d("WebsocketPushMsg", "vip");
////									dialog.show();
//								}
//							});
//
//
//						}else {
							if (dataBean.getPerson().getSubject_type()==2 || dataBean.getPerson().getSubject_type()==1){

								synthesizer.speak("热烈欢迎"+dataBean.getPerson().getName()+"莅临参观指导");

							}

							MoShengRenBean bean = new MoShengRenBean(dataBean.getPerson().getId(), "sss");

							daoSession.insert(bean);


							//更新右边上下滚动列表
							//shiBieJiLuBeanDao.insert(shiBieJiLuBean);
//								yuangongList.add(shiBieJiLuBean);
//								Message message = Message.obtain();
//								message.what = 19;
//								handler.sendMessage(message);

//								if (vector2.size()>30){
//									vector2.clear();
//									vector2.add("欢迎 "+dataBean.getPerson().getName()+" 签到:"+DateUtils.getCurrentTime_Today());
//								}
//
//							vector2.add("欢迎 "+dataBean.getPerson().getName()+" 签到:"+DateUtils.getCurrentTime_Today());
//								Collections.reverse(vector2);


//								runOnUiThread(new Runnable() {
//									@Override
//									public void run() {
//										marqueeView2.stopFlipping();
//										marqueeView2.startWithList(vector2);
//
//									}
//								});


							//异步保存今天刷脸的人数

							Message message = new Message();
							message.arg1 = 1;
							message.obj = dataBean.getPerson();
							handler.sendMessage(message);



						}catch (Exception e){
							Log.d("WebsocketPushMsg", e.getMessage());
						}finally {
							try {
								Thread.sleep(300);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							try {
								daoSession.deleteByKey(dataBean.getPerson().getId());
								//	Log.d("WebsocketPushMsg", "删除");
							}catch (Exception e){
								Log.d("WebsocketPushMsg", e.getMessage());
							}
						}
					}

				}

				@Override
				public void onClose(int i, String s, boolean b) {
					isLianJie=false;

					//Log.d("WebsocketPushMsg", "onClose"+i);
					runOnUiThread( new Runnable() {
						@Override
						public void run() {
							if (!YiDongNianHuiActivity.this.isFinishing()){
								wangluo.setVisibility(View.VISIBLE);
								wangluo.setText("连接识别主机失败,重连中...");
							}

						}
					});
//
//					if (conntionHandler==null && runnable==null){
//						Looper.prepare();
//						conntionHandler=new Handler();
//						runnable=new Runnable() {
//							@Override
//							public void run() {
//
//								Intent intent=new Intent("duanxianchonglian");
//								sendBroadcast(intent);
//							}
//						};
//						conntionHandler.postDelayed(runnable,13000);
//						Looper.loop();
//					}

				}

				@Override
				public void onError(Exception e) {
					Log.d("WebsocketPushMsg", "onError"+e.getMessage());

				}
			};

			webSocketClient.connect();
		}
		private void close(){
//
//			if (conntionHandler!=null && runnable!=null){
//				conntionHandler.removeCallbacks(runnable);
//				conntionHandler=null;
//				runnable=null;
//
//			}
			if (webSocketClient!=null){
				webSocketClient.close();
				webSocketClient=null;
				System.gc();

			}

		}

	}



	private void creatUser(byte[] bytes, Long tt, String age) {
		//Log.d("WebsocketPushMsg", "创建用户");
		String fileName="tong"+System.currentTimeMillis()+".jpg";
		//通过bytes数组创建图片文件
		createFileWithByte(bytes,fileName,tt,age);
		//上传
	//	addPhoto(fileName);
	}

	/**
	 * 根据byte数组生成文件
	 *
	 * @param bytes
	 *            生成文件用到的byte数组
	 * @param age
	 */
	private void createFileWithByte(byte[] bytes, String filename, Long tt, String age) {
		/**
		 * 创建File对象，其中包含文件所在的目录以及文件的命名
		 */
		File file=null;
		String	sdDir = this.getFilesDir().getAbsolutePath();//获取跟目录
		makeRootDirectory(sdDir);

		// 创建FileOutputStream对象
		FileOutputStream outputStream = null;
		// 创建BufferedOutputStream对象
		BufferedOutputStream bufferedOutputStream = null;

		try {
			file = new File(sdDir +File.separator+ filename);
			// 在文件系统中根据路径创建一个新的空文件
		//	file2.createNewFile();
		//	Log.d(TAG, file.createNewFile()+"");

			// 获取FileOutputStream对象
			outputStream = new FileOutputStream(file);
			// 获取BufferedOutputStream对象
			bufferedOutputStream = new BufferedOutputStream(outputStream);
			// 往文件所在的缓冲输出流中写byte数据
			bufferedOutputStream.write(bytes);
			// 刷出缓冲输出流，该步很关键，要是不执行flush()方法，那么文件的内容是空的。
			bufferedOutputStream.flush();
			//上传文件


		} catch (Exception e) {
			// 打印异常信息
			//Log.d(TAG, "ssssssssssssssssss"+e.getMessage());
		} finally {
			// 关闭创建的流对象
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bufferedOutputStream != null) {
				try {
					bufferedOutputStream.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}

	public static void makeRootDirectory(String filePath) {
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}
		} catch (Exception e) {

		}
	}






	public class NetWorkStateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			//检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
			if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

				//获得ConnectivityManager对象
				ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

				//获取ConnectivityManager对象对应的NetworkInfo对象
				//以太网
				NetworkInfo wifiNetworkInfo1 = connMgr.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
				//获取WIFI连接的信息
				NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				//获取移动数据连接的信息
				NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				if (wifiNetworkInfo1.isConnected() || wifiNetworkInfo.isConnected() || dataNetworkInfo.isConnected()){
					wangluo.setVisibility(View.GONE);

				}else {
					isLianJie=false;

					wangluo.setVisibility(View.VISIBLE);
				}


//				if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
//					Toast.makeText(context, "WIFI已连接,移动数据已连接", Toast.LENGTH_SHORT).show();
//				} else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
//					Toast.makeText(context, "WIFI已连接,移动数据已断开", Toast.LENGTH_SHORT).show();
//				} else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
//					Toast.makeText(context, "WIFI已断开,移动数据已连接", Toast.LENGTH_SHORT).show();
//				} else {
//					Toast.makeText(context, "WIFI已断开,移动数据已断开", Toast.LENGTH_SHORT).show();
//				}
//API大于23时使用下面的方式进行网络监听
			}else {

				Log.d(TAG, "API23");
				//获得ConnectivityManager对象
				ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

				//获取所有网络连接的信息
				Network[] networks = connMgr.getAllNetworks();
				//用于存放网络连接信息
				StringBuilder sb = new StringBuilder();
				//通过循环将网络信息逐个取出来
				Log.d(TAG, "networks.length:" + networks.length);
				if (networks.length==0){
					isLianJie=false;
					wangluo.setVisibility(View.VISIBLE);
				}
				for (int i=0; i < networks.length; i++){
					//获取ConnectivityManager对象对应的NetworkInfo对象
					NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);

					if (networkInfo.isConnected()){
						wangluo.setVisibility(View.GONE);

					}
				}

			}
		}
	}




}
