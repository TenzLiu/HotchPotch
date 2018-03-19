package com.tenz.hotchpotch.module.home.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.othershe.nicedialog.BaseNiceDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.base.BaseActivity;
import com.tenz.hotchpotch.helper.RecyclerViewDivider;
import com.tenz.hotchpotch.module.home.adapter.ContactsAdapter;
import com.tenz.hotchpotch.module.home.entity.Contacts;
import com.tenz.hotchpotch.util.AppUtil;
import com.tenz.hotchpotch.util.DisplayUtil;
import com.tenz.hotchpotch.util.ResourceUtil;
import com.tenz.hotchpotch.util.StringUtil;
import com.tenz.hotchpotch.widget.dialog.ConfirmDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * Author: TenzLiu
 * Date: 2018-03-16 10:56
 * Description: 通讯录
 */

public class ContactsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rcv_contacts)
    RecyclerView rcv_contacts;

    private ContactsHandler mContactsHandler;
    private ContactsAdapter mContactsAdapter;
    private List<Contacts> mContactsList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contacts;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        rcv_contacts.setLayoutManager(linearLayoutManager);
        RecyclerViewDivider recyclerViewDivider = new RecyclerViewDivider(mContext,
                linearLayoutManager.getOrientation(), DisplayUtil.dp2px(2),
                ResourceUtil.getColor(R.color.colorGray));
        rcv_contacts.addItemDecoration(recyclerViewDivider);
    }

    @Override
    protected void initData() {
        super.initData();
        initTitleBar(mToolbar,"通讯录");
        mContactsHandler = new ContactsHandler(this);
        mContactsList = new ArrayList<>();
        initAdapter(mContactsList);
        requestPermissions();
    }

    /**
     * 申请权限
     */
    private void requestPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if(!granted){
                            showToast("App未能获取相关权限");
                            return;
                        }
                        showLoadingDialog();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mContactsList = getContactsData();
                                mContactsHandler.sendEmptyMessage(0);
                            }
                        }).start();
                    }
                });
    }

    /**
     * 初始化adapter
     * @param contactsList
     */
    private void initAdapter(List<Contacts> contactsList) {
        mContactsAdapter = new ContactsAdapter(R.layout.item_contacts,contactsList);
        mContactsAdapter.openLoadAnimation();
        rcv_contacts.setAdapter(mContactsAdapter);
        mContactsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ConfirmDialog.show(getSupportFragmentManager(), "拨打电话", "确定拨打" +
                        contactsList.get(position).getPhone().get(0), new ConfirmDialog.ConfirmDialogListener() {
                    @Override
                    public void onNegative(BaseNiceDialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onPositive(BaseNiceDialog dialog) {
                        dialog.dismiss();
                        if(contactsList.get(position).getPhone().size()>0)
                            AppUtil.callPhone(mContext,contactsList.get(position).getPhone().get(0),false);
                    }
                });
            }
        });
    }

    /**
     * 获取手机通讯录数据
     */
    private List<Contacts> getContactsData() {
        List<Contacts> contactsList = new ArrayList<>();
        Uri uri = Uri.parse("content://com.android.contacts/contacts");
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{"_id"}, null,
                null, null);
        while(cursor.moveToNext()){
            int contactid = cursor.getInt(cursor.getColumnIndex("_id"));
            Contacts contacts = new Contacts();
            List<String> phoneList = new ArrayList<>();
            uri = Uri.parse("content://com.android.contacts/contacts/"+ contactid+ "/data");
            Cursor dataCursor = resolver.query(uri, new String[]{"mimetype","data1"},
                    null, null, null);
            while(dataCursor.moveToNext()){
                String data = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                String type = dataCursor.getString(dataCursor.getColumnIndex("mimetype"));
                if("vnd.android.cursor.item/name".equals(type)){//姓名
                    contacts.setName(data);
                }else if("vnd.android.cursor.item/email_v2".equals(type)){//email
                    contacts.setEmail(data);
                }else if("vnd.android.cursor.item/phone_v2".equals(type)){//phone
                    phoneList.add(data);
                }
            }
            contacts.setPhone(phoneList);
            if(!StringUtil.isEmpty(contacts.getName())&&contacts.getPhone().size()>0)
                contactsList.add(contacts);
            dataCursor.close();
        }
        cursor.close();
        return contactsList;
    }

    /**
     * 声明为静态防止内存泄露
     */
    private static class ContactsHandler extends Handler{

        private WeakReference<ContactsActivity> mContactsActivityWeakReference;

        public ContactsHandler(ContactsActivity contactsActivity) {
            mContactsActivityWeakReference = new WeakReference<>(contactsActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ContactsActivity contactsActivity = mContactsActivityWeakReference.get();
            if(contactsActivity != null){
                contactsActivity.dismissLoadingDialog();
                contactsActivity.mContactsAdapter.addData(contactsActivity.mContactsList);
            }
        }

    }

}
