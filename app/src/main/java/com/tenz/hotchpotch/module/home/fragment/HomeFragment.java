package com.tenz.hotchpotch.module.home.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.base.BaseFragment;
import com.tenz.hotchpotch.module.home.adapter.HomeAdapter;
import com.tenz.hotchpotch.module.home.entity.GetBanner;
import com.tenz.hotchpotch.module.home.entity.HomeData;
import com.tenz.hotchpotch.module.home.entity.Module;
import com.tenz.hotchpotch.module.news.activity.NewsDetailActivity;
import com.tenz.hotchpotch.module.news.entity.GetNews;
import com.tenz.hotchpotch.module.photo.activity.PhotoDetailActivity;
import com.tenz.hotchpotch.module.photo.entity.GetPhotos;
import com.tenz.hotchpotch.module.video.entity.GetVideos;
import com.tenz.hotchpotch.util.JsonUtil;
import com.tenz.hotchpotch.util.ResourceUtil;
import com.tenz.hotchpotch.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Author: TenzLiu
 * Date: 2018-01-20 16:51
 * Description: HomeFragment
 */

public class HomeFragment extends BaseFragment implements HomeAdapter.ItemClickListener {

    @BindView(R.id.srl_container)
    SwipeRefreshLayout srl_container;
    @BindView(R.id.rcv_home)
    RecyclerView rcv_home;

    private HomeAdapter mHomeAdapter;
    private List<GetBanner.Banner> mBannerList;
    private List<Module> mModuleList;
    private List<GetVideos.Video> mVideoList;
    private List<GetNews.News> mNewsList;
    private List<GetPhotos.Photo> mPhotoList;
    private List<HomeData> mHomeDataList;
    private HomeAdapter.Option mOption;

    public static HomeFragment getInstancec(){
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        srl_container.setColorSchemeColors(ResourceUtil.getColor(R.color.colorApp));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 6);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(mHomeDataList.get(position).getType() == HomeAdapter.VIEW_TYPE_VIDEO_CONTENT){
                    return 2;
                }else if(mHomeDataList.get(position).getType() == HomeAdapter.VIEW_TYPE_PHOTO_CONTENT){
                    return 3;
                }else{
                    return 6;
                }
            }
        });
        rcv_home.setLayoutManager(gridLayoutManager);
        srl_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl_container.setRefreshing(true);
                initData();
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        mBannerList = new ArrayList<>();
        mModuleList = new ArrayList<>();
        mNewsList = new ArrayList<>();
        mVideoList = new ArrayList<>();
        mPhotoList = new ArrayList<>();
        mHomeDataList = new ArrayList<>();
        //搞了一堆的数据，哈哈
        initBannerData();
        initModuleData();
        initNewsData();
        initVideoData();
        initPhotoData();
        //封装统一数据
        initHomeData();
        //跳转更多交由activity处理
        mOption = (HomeAdapter.Option) mActivity;
        mHomeAdapter = new HomeAdapter(mContext,mHomeDataList,mOption,this);
        rcv_home.setAdapter(mHomeAdapter);
        srl_container.setRefreshing(false);
    }

    /**
     * 初始化banner数据
     */
    private void initBannerData() {
        mBannerList.clear();
        GetBanner.Banner banner1 = new GetBanner.Banner();
        banner1.setPic_url("http://img.zcool.cn/community/01b72057a7e0790000018c1bf4fce0.png");
        banner1.setTitle("初秋特惠大放价");
        GetBanner.Banner banner2 = new GetBanner.Banner();
        banner2.setPic_url("http://img.zcool.cn/community/01fca557a7f5f90000012e7e9feea8.jpg");
        banner2.setTitle("坚果早餐生活");
        GetBanner.Banner banner3 = new GetBanner.Banner();
        banner3.setPic_url("http://img.zcool.cn/community/01996b57a7f6020000018c1bedef97.jpg");
        banner3.setTitle("夏日特惠4折起");
        GetBanner.Banner banner4 = new GetBanner.Banner();
        banner4.setPic_url("http://img.zcool.cn/community/01700557a7f42f0000018c1bd6eb23.jpg");
        banner4.setTitle("女性专场返利");
        mBannerList.add(banner1);
        mBannerList.add(banner2);
        mBannerList.add(banner3);
        mBannerList.add(banner4);
    }

    /**
     * 初始化模块数据
     */
    private void initModuleData() {
        for(int i=0; i<8; i++){
            Module module = new Module();
            module.setName("模块"+i);
            mModuleList.add(module);
        }
    }

    /**
     * 初始化新闻数据
     */
    private void initNewsData() {
        mNewsList.clear();

        GetNews.News news1 = new GetNews.News();
        news1.setTitle("面对3000万琴童，在线教育如何切入钢琴陪练市场？");
        news1.setPublish_time(1517142287);
        news1.setUrl("https://www.toutiao.com/a6514160042487316995/");
        GetNews.News.UserInfoBean userInfoBean1 = new GetNews.News.UserInfoBean();
        userInfoBean1.setName("三叶吉他教室");
        userInfoBean1.setAvatar_url("http://p9.pstatp.com/thumb/436f000321a820655920");
        news1.setUser_info(userInfoBean1);

        GetNews.News news2 = new GetNews.News();
        news2.setTitle("怎样开通《悟空问答》的收益？");
        news2.setPublish_time(1517101009);
        news2.setUrl("https://www.toutiao.com/a6516012931509912072/");
        GetNews.News.UserInfoBean userInfoBean2 = new GetNews.News.UserInfoBean();
        userInfoBean2.setName("绿水青山满人间");
        userInfoBean2.setAvatar_url("http://p3.pstatp.com/thumb/50a0000247c869c795fe");
        news2.setUser_info(userInfoBean2);

        GetNews.News news3 = new GetNews.News();
        news3.setTitle("移动联通双定制版手机怎么样可以用电信的号码？");
        news3.setPublish_time(1517092246);
        news3.setUrl("https://www.toutiao.com/a6492381281588871694/");
        GetNews.News.UserInfoBean userInfoBean3 = new GetNews.News.UserInfoBean();
        userInfoBean3.setName("手机用户69931465180");
        userInfoBean3.setAvatar_url("http://p0.pstatp.com/origin/3791/5035712059");
        news3.setUser_info(userInfoBean3);

        GetNews.News news4 = new GetNews.News();
        news4.setTitle("刘强东回答达沃斯记者提问，中国唯一让他感到不自由的地方是自己太有名，你怎么看？");
        news4.setPublish_time(1517098375);
        news4.setUrl("https://www.ixigua.com/a6514822427522892302/?utm_source=toutiao&utm_medium=feed_stream#mid=51045089537");
        GetNews.News.UserInfoBean userInfoBean4 = new GetNews.News.UserInfoBean();
        userInfoBean4.setName("明明154084355");
        userInfoBean4.setAvatar_url("http://p3.pstatp.com/thumb/43d60003666e415f2d1b");
        news4.setUser_info(userInfoBean4);

        GetNews.News news5 = new GetNews.News();
        news5.setTitle("如何看待线上抓娃娃APP的兴起？");
        news5.setPublish_time(1517111188);
        news5.setUrl("https://www.toutiao.com/a6494050081942012429/");
        GetNews.News.UserInfoBean userInfoBean5 = new GetNews.News.UserInfoBean();
        userInfoBean5.setName("多逗逼");
        userInfoBean5.setAvatar_url("http://p1.pstatp.com/thumb/3efd000d10f1ce637fa7");
        news5.setUser_info(userInfoBean5);

        GetNews.News news6 = new GetNews.News();
        news6.setTitle("南宁大街上有人要微信发100红包给你，你给他100现金，你会换吗？");
        news6.setPublish_time(1517153666);
        news6.setUrl("https://www.toutiao.com/a6467030617526632973/");
        GetNews.News.UserInfoBean userInfoBean6 = new GetNews.News.UserInfoBean();
        userInfoBean6.setName("入殓师2");
        userInfoBean6.setAvatar_url("http://p9.pstatp.com/thumb/1dce001338dd3fef070c");
        news6.setUser_info(userInfoBean6);

        mNewsList.add(news1);
        mNewsList.add(news2);
        mNewsList.add(news3);
        mNewsList.add(news4);
        mNewsList.add(news5);
        mNewsList.add(news6);
    }

    /**
     * 初始化视频数据
     */
    private void initVideoData() {
        mVideoList = new ArrayList<>();
        GetVideos.Video video1 = new GetVideos.Video();
        video1.setTitle("阿凡达2");
        video1.setVideoUrl("");
        GetVideos.Video video2 = new GetVideos.Video();
        video2.setTitle("芳华");
        video2.setVideoUrl("");
        GetVideos.Video video3 = new GetVideos.Video();
        video3.setTitle("钢铁侠4");
        video3.setVideoUrl("");
        GetVideos.Video video4 = new GetVideos.Video();
        video4.setTitle("重生之门");
        video4.setVideoUrl("");
        GetVideos.Video video5 = new GetVideos.Video();
        video5.setTitle("电锯惊魂8");
        video5.setVideoUrl("");
        GetVideos.Video video6 = new GetVideos.Video();
        video6.setTitle("大杂烩");
        video6.setVideoUrl("");
        mVideoList.add(video1);
        mVideoList.add(video2);
        mVideoList.add(video3);
        mVideoList.add(video4);
        mVideoList.add(video5);
        mVideoList.add(video6);
    }

    /**
     * 初始化图片数据
     */
    private void initPhotoData() {
        mPhotoList.clear();
        GetPhotos.Photo photo1 = new GetPhotos.Photo();
        photo1.setUrl("http://7xi8d6.com1.z0.glb.clouddn.com/20180129074038_O3ydq4_Screenshot.jpeg");
        GetPhotos.Photo photo2 = new GetPhotos.Photo();
        photo2.setUrl("http://7xi8d6.com1.z0.glb.clouddn.com/20180122090204_A4hNiG_Screenshot.jpeg");
        GetPhotos.Photo photo3 = new GetPhotos.Photo();
        photo3.setUrl("http://7xi8d6.com1.z0.glb.clouddn.com/20180115085556_8AeReR_taeyeon_ss_15_1_2018_7_58_51_833.jpeg");
        GetPhotos.Photo photo4 = new GetPhotos.Photo();
        photo4.setUrl("http://7xi8d6.com1.z0.glb.clouddn.com/20180109085038_4A7atU_rakukoo_9_1_2018_8_50_25_276.jpeg");
        GetPhotos.Photo photo5 = new GetPhotos.Photo();
        photo5.setUrl("http://7xi8d6.com1.z0.glb.clouddn.com/20171228085004_5yEHju_Screenshot.jpeg");
        GetPhotos.Photo photo6 = new GetPhotos.Photo();
        photo6.setUrl("http://7xi8d6.com1.z0.glb.clouddn.com/20180102083655_3t4ytm_Screenshot.jpeg");
        mPhotoList.add(photo1);
        mPhotoList.add(photo2);
        mPhotoList.add(photo3);
        mPhotoList.add(photo4);
        mPhotoList.add(photo5);
        mPhotoList.add(photo6);
    }


    /**
     * 封装初始化首页列表数据
     */
    private void initHomeData() {
        mHomeDataList.clear();
        //封装轮播
        HomeData homeDataBanner = new HomeData();
        homeDataBanner.setType(HomeAdapter.VIEW_TYPE_BANNER);
        homeDataBanner.setBannerList(mBannerList);
        mHomeDataList.add(homeDataBanner);
        //封装模块
        HomeData homeDataModule = new HomeData();
        homeDataModule.setType(HomeAdapter.VIEW_TYPE_MODULE);
        homeDataModule.setModuleList(mModuleList);
        mHomeDataList.add(homeDataModule);
        //封装新闻头部
        HomeData homeDataNewsTop = new HomeData();
        homeDataNewsTop.setType(HomeAdapter.VIEW_TYPE_NEWS_TOP);
        mHomeDataList.add(homeDataNewsTop);
        //封装新闻内容
        for(GetNews.News news : mNewsList){
            HomeData homeDataNewsContent = new HomeData();
            homeDataNewsContent.setType(HomeAdapter.VIEW_TYPE_NEWS_CONTENT);
            homeDataNewsContent.setNews(news);
            mHomeDataList.add(homeDataNewsContent);
        }
        //封装视频头部
        HomeData homeDataVideoTop = new HomeData();
        homeDataVideoTop.setType(HomeAdapter.VIEW_TYPE_VIDEO_TOP);
        mHomeDataList.add(homeDataVideoTop);
        //封装视频内容
        for(GetVideos.Video video : mVideoList){
            HomeData homeDataVideoContent = new HomeData();
            homeDataVideoContent.setType(HomeAdapter.VIEW_TYPE_VIDEO_CONTENT);
            homeDataVideoContent.setVideo(video);
            mHomeDataList.add(homeDataVideoContent);
        }
        //封装图片头部
        HomeData homeDataPhotoTop = new HomeData();
        homeDataPhotoTop.setType(HomeAdapter.VIEW_TYPE_PHOTO_TOP);
        mHomeDataList.add(homeDataPhotoTop);
        //封装图片内容
        for(GetPhotos.Photo photo : mPhotoList){
            HomeData homeDataPhotoContent = new HomeData();
            homeDataPhotoContent.setType(HomeAdapter.VIEW_TYPE_PHOTO_CONTENT);
            homeDataPhotoContent.setPhoto(photo);
            mHomeDataList.add(homeDataPhotoContent);
        }
    }

    @Override
    public void onItemClick(int position) {
        if(mHomeDataList.get(position).getType() == HomeAdapter.VIEW_TYPE_NEWS_CONTENT){
            GetNews.News news = mHomeDataList.get(position).getNews();
            Bundle bundle = new Bundle();
            bundle.putString("url",news.getUrl());
            bundle.putString("htmlData","");
            startActivity(NewsDetailActivity.class,bundle);
        }else if(mHomeDataList.get(position).getType() == HomeAdapter.VIEW_TYPE_VIDEO_CONTENT){

        }else if(mHomeDataList.get(position).getType() == HomeAdapter.VIEW_TYPE_PHOTO_CONTENT){
            Bundle bundle = new Bundle();
            bundle.putString("pic_url",mHomeDataList.get(position).getPhoto().getUrl());
            startActivity(PhotoDetailActivity.class,bundle);
        }
    }

}
