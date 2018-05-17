package com.hbyundu.shop.ui.credit.apply;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.hbyundu.shop.R;
import com.hbyundu.shop.manager.UserManager;
import com.hbyundu.shop.rest.api.repay.RepayAddDealAPI;
import com.hbyundu.shop.rest.api.repay.RepayDealCateAPI;
import com.hbyundu.shop.rest.api.repay.RepayDealRateAPI;
import com.hbyundu.shop.rest.base.subscribers.SubscriberOnListener;
import com.hbyundu.shop.rest.model.repay.DealCateDataModel;
import com.hbyundu.shop.rest.model.repay.DealRateModel;
import com.hbyundu.shop.ui.BaseActivity;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2017/12/3.
 */

public class ApplyLoansRateActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, ApplyLoansPhotoAdapter.MediaUploadListener,
        TakePhoto.TakeResultListener, InvokeListener {

    private static final int MAX_PHOTOS = 10;
    private static final int PHOTO_PIXEL = 1280;
    private static final int PHOTO_SIZE = 1024 * 1024;
    private Button submitButton;
    private SVProgressHUD mProgressHUD;
    private DealCateDataModel mDealCateDataModel;
    private ListView mRateListView;
    private GridView mPhotoGridView;
    private ApplyLoansPhotoAdapter mApplyLoansPhotoAdapter;
    private ApplyRateAdapter mRateAdapter;
    private List<String> mPhotos = new ArrayList<>();
    private List<DealRateModel.DealRateItemModel> mRates = new ArrayList<>();
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private double mMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loans_rate_apply);

        initTitle();
        initView();
        getDealCateData();
    }

    private void initTitle() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.installment_scheme);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mMoney = getIntent().getDoubleExtra("money", 0);

        mProgressHUD = new SVProgressHUD(this);

        submitButton = (Button) findViewById(R.id.activity_loans_rate_apply_submit_btn);
        submitButton.setOnClickListener(this);

        mPhotoGridView = (GridView) findViewById(R.id.activity_loans_rate_apply_photo_gv);
        mApplyLoansPhotoAdapter = new ApplyLoansPhotoAdapter(this, mPhotos, MAX_PHOTOS, this);
        mPhotoGridView.setAdapter(mApplyLoansPhotoAdapter);
        mPhotoGridView.setOnItemClickListener(this);

        mRateListView = (ListView) findViewById(R.id.activity_loans_rate_apply_rate_lv);
        mRateAdapter = new ApplyRateAdapter(this, mRates);
        mRateAdapter.setTotalMoney(mMoney);
        mRateListView.setAdapter(mRateAdapter);
        mRateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mRateAdapter.setSelected(i);
                mRateAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getDealCateData() {
        RepayDealCateAPI.getInstance().dealCate(new SubscriberOnListener<DealCateDataModel>() {
            @Override
            public void onSucceed(DealCateDataModel data) {
                mDealCateDataModel = data;
                getRate();
            }

            @Override
            public void onError(String msg) {
                mProgressHUD.dismissImmediately();
            }
        });

        mProgressHUD.showWithMaskType(SVProgressHUD.SVProgressHUDMaskType.Clear);
    }

    private void getRate() {
        RepayDealRateAPI.getInstance().dealRate(new SubscriberOnListener<List<DealRateModel.DealRateItemModel>>() {
            @Override
            public void onSucceed(List<DealRateModel.DealRateItemModel> data) {
                mRates.addAll(data);
                mRateAdapter.notifyDataSetChanged();
                mProgressHUD.dismissImmediately();
            }

            @Override
            public void onError(String msg) {
                mProgressHUD.dismissImmediately();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.activity_loans_rate_apply_submit_btn) {
            applyAction();
        }
    }

    private void applyAction() {
        if (mDealCateDataModel == null) {
            return;
        }

        if (mRateAdapter.getSelected() < 0) {
            return;
        }

        DealRateModel.DealRateItemModel rate = mRates.get(mRateAdapter.getSelected());

        RepayAddDealAPI.getInstance().dealyongtu = mDealCateDataModel.accessarray.get(0).yongtuname;
        RepayAddDealAPI.getInstance().repayTime = Integer.valueOf(rate.month);
        RepayAddDealAPI.getInstance().choubiaoqixian = Integer.valueOf(mDealCateDataModel.choubiaoqixian);
        RepayAddDealAPI.getInstance().addPhotos(mApplyLoansPhotoAdapter.getPhotoFiles());
        RepayAddDealAPI.getInstance().addDeal(UserManager.getInstance(getApplicationContext()).getUid(),
                String.format(getString(R.string.apply_loan_name_format), UserManager.getInstance(getApplicationContext()).getUsername()),
                mMoney, Double.valueOf(rate.min), "",
                new SubscriberOnListener<Long>() {
                    @Override
                    public void onSucceed(final Long resultId) {
                        mProgressHUD.dismissImmediately();
                        mProgressHUD.showSuccessWithStatus(getString(R.string.loan_apply_loan_success));
                        mProgressHUD.setOnDismissListener(new OnDismissListener() {
                            @Override
                            public void onDismiss(SVProgressHUD svProgressHUD) {
                                finishBeforeActivity(ApplyLoansActivity.class);
                            }
                        });
                    }

                    @Override
                    public void onError(String msg) {
                        mProgressHUD.dismissImmediately();
                        mProgressHUD.showErrorWithStatus(getString(R.string.loan_apply_loan_failure));
                    }
                });


        mProgressHUD.showWithMaskType(SVProgressHUD.SVProgressHUDMaskType.Clear);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (position < mPhotos.size()) {
        } else {
            new AlertDialog.Builder(this)
                    .setItems(R.array.photo_source, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) { //拍照
                                takePhotoFromCamera();
                            } else if (which == 1) { //相册
                                takePhotoFromGallery();
                            }
                        }
                    }).setNegativeButton(R.string.cancel, null).show();
        }
    }

    /**
     * 拍照
     */
    private void takePhotoFromCamera() {
        CompressConfig compressConfig = new CompressConfig.Builder().setMaxSize(PHOTO_SIZE).setMaxPixel(PHOTO_PIXEL).create();
        getTakePhoto().onEnableCompress(compressConfig, true);
        getTakePhoto().onPickFromCapture(getImageUri());
    }

    /**
     * 从相册选择照片
     */
    private void takePhotoFromGallery() {
        CompressConfig compressConfig = new CompressConfig.Builder().setMaxSize(PHOTO_SIZE).setMaxPixel(PHOTO_PIXEL).create();
        getTakePhoto().onEnableCompress(compressConfig, true);
        getTakePhoto().onPickFromGallery();
    }

    @Override
    public void onDeleteClick(int position) {
        mPhotos.remove(position);
        mApplyLoansPhotoAdapter.notifyDataSetChanged();
    }

    protected void onSaveInstanceState(Bundle outState) {
        this.getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, this.invokeParam, this);
    }

    public TakePhoto getTakePhoto() {
        if (this.takePhoto == null) {
            this.takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }

        return this.takePhoto;
    }

    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }

        return type;
    }


    /**
     * 取消选择图片
     */
    public void takeCancel() {

    }

    /**
     * 选择图片失败
     *
     * @param result
     * @param msg
     */
    public void takeFail(TResult result, String msg) {
        mProgressHUD.showErrorWithStatus(getString(R.string.take_photo_failure));
    }

    /**
     * 选择图片成功
     *
     * @param result
     */
    public void takeSuccess(TResult result) {
        mPhotos.add(result.getImage().getOriginalPath());
        mApplyLoansPhotoAdapter.notifyDataSetChanged();
    }

    private Uri getImageUri() {
        File dirFile = null;
        String newImageName = System.currentTimeMillis() + ".jpg";
        dirFile = getExternalCacheDir();
        File file = new File(dirFile, newImageName);
        return Uri.fromFile(file);
    }
}
