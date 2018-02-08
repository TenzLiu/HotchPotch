package com.tenz.hotchpotch.module.video.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.base.BaseMvpFragment;
import com.tenz.hotchpotch.module.video.activity.VideoDetailActivity;
import com.tenz.hotchpotch.module.video.adapter.VideoAdapter;
import com.tenz.hotchpotch.module.video.contract.VideoContract;
import com.tenz.hotchpotch.module.video.entity.GetVideos;
import com.tenz.hotchpotch.module.video.model.VideoModel;
import com.tenz.hotchpotch.module.video.presenter.VideoPresenter;
import com.tenz.hotchpotch.util.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Author: TenzLiu
 * Date: 2018-01-20 16:51
 * Description: VideoFragment
 */

public class VideoFragment extends BaseMvpFragment<VideoPresenter,VideoModel>
        implements VideoContract.IVideoView,BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.abl_appbar)
    AppBarLayout abl_appbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.srl_video)
    SwipeRefreshLayout srl_video;
    @BindView(R.id.rcv_video)
    RecyclerView rcv_video;
    @BindView(R.id.fab_video)
    FloatingActionButton fab_video;

    private VideoAdapter mVideoAdapter;

    public static VideoFragment getInstancec(){
        VideoFragment videoFragment = new VideoFragment();
        Bundle bundle = new Bundle();
        videoFragment.setArguments(bundle);
        return videoFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        srl_video.setColorSchemeColors(ResourceUtil.getColor(R.color.colorApp));
        rcv_video.setLayoutManager(new GridLayoutManager(mContext,3));
        srl_video.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl_video.setRefreshing(true);
                mPresenter.getVideos(true);
            }
        });
        abl_appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(verticalOffset == 0){
                    fab_video.hide();
                }else{
                    fab_video.show();
                }
            }
        });
        fab_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rcv_video.smoothScrollToPosition(0);
            }
        });
        initAdapter(new ArrayList<GetVideos.Video>());
    }

    @Override
    protected void initData() {
        super.initData();
        srl_video.setRefreshing(true);
        mPresenter.getVideos(true);
    }

    @NonNull
    @Override
    protected VideoPresenter initPresenter() {
        return VideoPresenter.newInstance();
    }

    /**
     * 初始化adapter
     */
    private void initAdapter(List<GetVideos.Video> videoList){
        mVideoAdapter = new VideoAdapter(R.layout.item_video,videoList);
        mVideoAdapter.setOnLoadMoreListener(this,rcv_video);
        mVideoAdapter.openLoadAnimation();
        rcv_video.setAdapter(mVideoAdapter);
        mVideoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("video",mVideoAdapter.getItem(position));
                startActivity(VideoDetailActivity.class,bundle);
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        mVideoAdapter.loadMoreComplete();
        mPresenter.getVideos(false);
    }

    @Override
    public void showVideos(List<GetVideos.Video> videoList) {
        srl_video.setRefreshing(false);
        if(videoList.size() == 0){

        }else{
            mVideoAdapter.addData(videoList);
        }
    }

}
