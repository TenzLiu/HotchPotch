package com.tenz.hotchpotch.module.home.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.module.home.entity.Contacts;
import com.tenz.hotchpotch.util.GlideUtil;
import com.tenz.hotchpotch.util.LogUtil;

import java.util.List;

/**
 * Author: TenzLiu
 * Date: 2018-03-16 16:09
 * Description: ContactsAdapter
 */

public class ContactsAdapter extends BaseQuickAdapter<Contacts, BaseViewHolder> {

    public ContactsAdapter(int layoutResId, @Nullable List data) {
        super(layoutResId, data);
    }

    public ContactsAdapter(@Nullable List data) {
        super(data);
    }

    public ContactsAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, Contacts item) {
        GlideUtil.loadImage(mContext,"",R.mipmap.contacts,helper.getView(R.id.siv_head_icon));
        ((TextView)helper.getView(R.id.tv_name)).setText(item.getName());
        ((TextView)helper.getView(R.id.tv_phone)).setText(item.getPhone().size()>0?item.getPhone().get(0):"");
    }

}
