package com.tenz.hotchpotch.module.photo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bm.library.PhotoView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.base.BaseFragment;
import com.tenz.hotchpotch.base.BaseMvpFragment;
import com.tenz.hotchpotch.module.photo.activity.PhotoDetailActivity;
import com.tenz.hotchpotch.module.photo.adapter.PhotoAdapter;
import com.tenz.hotchpotch.module.photo.contract.PhotoContract;
import com.tenz.hotchpotch.module.photo.entity.GetPhotos;
import com.tenz.hotchpotch.module.photo.model.PhotoModel;
import com.tenz.hotchpotch.module.photo.presenter.PhotoPresenter;
import com.tenz.hotchpotch.util.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Author: TenzLiu
 * Date: 2018-01-20 16:51
 * Description: PhotoFragment
 */

public class PhotoFragment extends BaseMvpFragment<PhotoPresenter,PhotoModel>
        implements PhotoContract.IPhotoView,BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.abl_appbar)
    AppBarLayout abl_appbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.srl_photo)
    SwipeRefreshLayout srl_photo;
    @BindView(R.id.rcv_photo)
    RecyclerView rcv_photo;
    @BindView(R.id.fab_photo)
    FloatingActionButton fab_photo;

    private PhotoAdapter mPhotoAdapter;

    public static PhotoFragment getInstancec(){
        PhotoFragment photoFragment = new PhotoFragment();
        Bundle bundle = new Bundle();
        photoFragment.setArguments(bundle);
        return photoFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_photo;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        srl_photo.setColorSchemeColors(ResourceUtil.getColor(R.color.colorApp));
        rcv_photo.setLayoutManager(new GridLayoutManager(mContext,2));
        srl_photo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl_photo.setRefreshing(true);
                mPresenter.getPhotos(true);
            }
        });
        abl_appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(verticalOffset == 0){
                    fab_photo.hide();
                }else{
                    fab_photo.show();
                }
            }
        });
        fab_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rcv_photo.smoothScrollToPosition(0);
            }
        });
        initAdapter(new ArrayList<GetPhotos.Photo>());
    }

    @Override
    protected void initData() {
        super.initData();
        srl_photo.setRefreshing(true);
        mPresenter.getPhotos(true);
    }

    @NonNull
    @Override
    protected PhotoPresenter initPresenter() {
        return new PhotoPresenter(null,this);
    }

    /**
     * 初始化adapter
     */
    private void initAdapter(List<GetPhotos.Photo> photoList){
        mPhotoAdapter = new PhotoAdapter(R.layout.item_photo,photoList);
        mPhotoAdapter.setOnLoadMoreListener(this,rcv_photo);
        mPhotoAdapter.openLoadAnimation();
        rcv_photo.setAdapter(mPhotoAdapter);
        mPhotoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("pic_url",mPhotoAdapter.getItem(position).getUrl());
                startActivity(PhotoDetailActivity.class,bundle);
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        mPhotoAdapter.loadMoreComplete();
        mPresenter.getPhotos(false);
    }

    @Override
    public void showPhotos(List<GetPhotos.Photo> photoList) {
        srl_photo.setRefreshing(false);
        if(photoList.size() == 0){

        }else{
            mPhotoAdapter.addData(photoList);
        }
    }

}
