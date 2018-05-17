package com.wavpayment.wavpay.app;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.lan.sponge.activity.ProxyActivity;
import com.lan.sponge.config.Sponge;
import com.lan.sponge.delegate.SpongeDelegate;
import com.lan.sponge.util.callback.CallbackManager;
import com.lan.sponge.util.callback.CallbackType;
import com.lan.sponge.util.callback.IGlobalCallback;
import com.lan.sponge.util.log.SpongeLogger;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.RxPhotoTool;
import com.vondear.rxtools.notification.NotificationUtils;
import com.wavpayment.wavpay.R;
import com.wavpayment.wavpay.downline.DownlineReciver;
import com.wavpayment.wavpay.ui.main.StartDelegate;
import com.wavpayment.wavpay.utils.Utils;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import qiu.niorgai.StatusBarCompat;

public class WavPayActivity extends ProxyActivity {
    public static final int SCAN_REQUEST_CODE = 100;
    private static final int CAMERA_PERMISSION = 110;

    @Override
    public SpongeDelegate setRootDelegate() {
        return new StartDelegate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registDownLineRecever();
        initNotificationChannel();
        StatusBarCompat.translucentStatusBar(this, true);
        Sponge.getConfigurator().withActivity(this);
        RxActivityTool.addActivity(this);
    }



    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RxPhotoTool.GET_IMAGE_FROM_PHONE:
                if (resultCode == RESULT_OK) {
                    initUCrop(data.getData());
                }
                break;
            case RxPhotoTool.GET_IMAGE_BY_CAMERA:
                if (resultCode == RESULT_OK) {
                    initUCrop(RxPhotoTool.imageUriFromCamera);
                }
                break;
            case UCrop.REQUEST_CROP://UCrop裁剪之后的处理
                if (resultCode == RESULT_OK) {
                    final Uri cropUri = UCrop.getOutput(data);
                    //拿到剪裁后的数据进行处理
                    final IGlobalCallback<Uri> callback = CallbackManager
                            .getInstance()
                            .getCallback(CallbackType.ON_CROP);
                    if (callback != null) {
                        callback.executeCallback(cropUri);
                    }
                }
                break;
            case UCrop.RESULT_ERROR://UCrop裁剪错误之后的处理
                break;

            case 134:
                if (resultCode == RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor cursor = managedQuery(contactData, null, null, null, null);
                    cursor.moveToFirst();
                    String num = Utils.getContactPhone(this, cursor);
                    num = num.replace(" ", "");
                    num = num.replace("-", "");
                    SpongeLogger.e("num", num);
                    IGlobalCallback<String> callback = CallbackManager.getInstance().getCallback(CallbackType.PHONE);
                    callback.executeCallback(num);
                }
                break;

            case SCAN_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    String isbn = data.getStringExtra("CaptureIsbn");
                    IGlobalCallback<String> callback = CallbackManager.getInstance().getCallback(CallbackType.QR_CODE);
                    callback.executeCallback(isbn);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initUCrop(Uri uri) {
        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));

        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), imageName + ".jpeg"));

        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.colorPrimaryDark));
        //设置最大缩放比例
        options.setMaxScaleMultiplier(5);
        //设置图片在切换比例时的动画
        options.setImageToCropBoundsAnimDuration(666);
        UCrop.of(uri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(1000, 1000)
                .withOptions(options)
                .start(this);
    }

    @Override
    public void post(Runnable runnable) {

    }
    /**
     * 注册强制下线广播
     */
    private DownlineReciver downlineReciver;
    private void registDownLineRecever(){
        IntentFilter intentFilter = new IntentFilter("com.wavpayment.wavpay.FORCE_OFFLINE");
        downlineReciver = new DownlineReciver();
        downlineReciver.setmActivity(this);
        downlineReciver.setmContext(this);
        WavPayActivity.this.registerReceiver(downlineReciver,intentFilter);
    }


    /**
     * 适配8.0通知
     */
    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // 通知渠道的id
            String id = "downline_notify";
            // 用户可以看到的通知渠道的名字.
            CharSequence name = getString(R.string.downline_notification);
            // 用户可以看到的通知渠道的描述
            String description = getString(R.string.downline_notification_message);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // 配置通知渠道的属性
            mChannel.setDescription(description);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.GREEN);
            // 设置通知出现时的震动（如果 android 设备支持的话）
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            //最后在notificationmanager中创建该通知渠道
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }





    @Override
    protected void onDestroy() {
        unregisterReceiver(downlineReciver);
        super.onDestroy();
    }
}
