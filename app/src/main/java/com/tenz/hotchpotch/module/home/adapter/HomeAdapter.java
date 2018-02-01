package com.tenz.hotchpotch.module.home.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tenz.hotchpotch.R;
import com.tenz.hotchpotch.module.home.entity.GetBanner;
import com.tenz.hotchpotch.module.home.entity.HomeData;
import com.tenz.hotchpotch.util.DateUtil;
import com.tenz.hotchpotch.util.GlideUtil;
import com.tenz.hotchpotch.util.ToastUtil;
import com.tenz.hotchpotch.widget.banner.BannerImageLoader;
import com.tenz.hotchpotch.widget.image.ShapeImageView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: TenzLiu
 * Date: 2018-01-30 18:03
 * Description: HomeAdapter
 */

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ItemClickListener mItemClickListener;
    private Option mOption;
    private List<HomeData> mHomeDataList;

    public static final int VIEW_TYPE_BANNER = 0;
    public static final int VIEW_TYPE_MODULE = 1;
    public static final int VIEW_TYPE_NEWS_TOP = 2;
    public static final int VIEW_TYPE_NEWS_CONTENT = 3;
    public static final int VIEW_TYPE_VIDEO_TOP = 4;
    public static final int VIEW_TYPE_VIDEO_CONTENT = 5;
    public static final int VIEW_TYPE_PHOTO_TOP = 6;
    public static final int VIEW_TYPE_PHOTO_CONTENT = 7;

    public HomeAdapter(Context context, List<HomeData> homeDataList, Option option, ItemClickListener itemClickListener) {
        this.mContext = context;
        this.mHomeDataList = homeDataList;
        this.mOption = option;
        this.mItemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return mHomeDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mHomeDataList.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if(viewType == VIEW_TYPE_BANNER){//轮播图
            itemView = getView(R.layout.item_home_banner,parent);
            return new BannerViewHolder(itemView);
        }else if(viewType == VIEW_TYPE_MODULE){//模块
            itemView = getView(R.layout.item_home_module,parent);
            return new ModuleViewHolder(itemView);
        }else if(viewType == VIEW_TYPE_NEWS_TOP){//新闻头部
            itemView = getView(R.layout.item_home_item_top,parent);
            return new NewsTopViewHolder(itemView);
        }else if(viewType == VIEW_TYPE_NEWS_CONTENT){//新闻内容
            itemView = getView(R.layout.item_news,parent);
            return new NewsContentViewHolder(itemView);
        }else if(viewType == VIEW_TYPE_VIDEO_TOP){//视频头部
            itemView = getView(R.layout.item_home_item_top,parent);
            return new VideoTopViewHolder(itemView);
        }else if(viewType == VIEW_TYPE_VIDEO_CONTENT){//视频内容
            itemView = getView(R.layout.item_video,parent);
            return new VideoContentViewHolder(itemView);
        }else if(viewType == VIEW_TYPE_PHOTO_TOP){//图片头部
            itemView = getView(R.layout.item_home_item_top,parent);
            return new PhotoTopViewHolder(itemView);
        }else{//图片内容
            itemView = getView(R.layout.item_photo,parent);
            return new PhotoContentViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof BannerViewHolder){
            BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
            setBannerData(bannerViewHolder,position);
        }else if(holder instanceof ModuleViewHolder){
            ModuleViewHolder moduleViewHolder = (ModuleViewHolder) holder;
            setModuleData(moduleViewHolder,position);
        }else if(holder instanceof NewsTopViewHolder){
            NewsTopViewHolder newsTopViewHolder = (NewsTopViewHolder) holder;
            setNewsTopData(newsTopViewHolder,position);
        }else if(holder instanceof NewsContentViewHolder){
            NewsContentViewHolder newsContentViewHolder = (NewsContentViewHolder) holder;
            setNewContentData(newsContentViewHolder,position);
        }else if(holder instanceof VideoTopViewHolder){
            VideoTopViewHolder videoTopViewHolder = (VideoTopViewHolder) holder;
            setVideoTopData(videoTopViewHolder,position);
        }else if(holder instanceof VideoContentViewHolder){
            VideoContentViewHolder videoContentViewHolder = (VideoContentViewHolder) holder;
            setVideoContentData(videoContentViewHolder,position);
        }else if(holder instanceof PhotoTopViewHolder){
            PhotoTopViewHolder photoTopViewHolder = (PhotoTopViewHolder) holder;
            setPhotoTopData(photoTopViewHolder,position);
        }else{
            PhotoContentViewHolder photoContentViewHolder = (PhotoContentViewHolder) holder;
            setPhotoContentData(photoContentViewHolder,position);
        }
    }

    /**
     * 设置轮播图数据
     * @param bannerViewHolder
     * @param position
     */
    private void setBannerData(BannerViewHolder bannerViewHolder, int position) {
        List<String> bannerImageList = new ArrayList<>();
        List<String> bannerTitleList = new ArrayList<>();
        for (GetBanner.Banner banner : mHomeDataList.get(position).getBannerList()) {
            bannerImageList.add(banner.getPic_url());
        }
        for (GetBanner.Banner banner : mHomeDataList.get(position).getBannerList()) {
            bannerTitleList.add(banner.getTitle());
        }
        bannerViewHolder.mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器
        bannerViewHolder.mBanner.setImageLoader(new BannerImageLoader());
        //设置图片集合
        bannerViewHolder.mBanner.setImages(bannerImageList);
        bannerViewHolder.mBanner.setBannerTitles(bannerTitleList);
        //banner设置方法全部调用完毕时最后调用
        bannerViewHolder.mBanner.start();
    }

    /**
     * 设置模块数据
     * @param moduleViewHolder
     * @param position
     */
    private void setModuleData(ModuleViewHolder moduleViewHolder, final int position) {
        moduleViewHolder.ll_module.removeAllViews();
        //动态添加模块View
        for(int i=0; i<mHomeDataList.get(position).getModuleList().size(); i++){
            View view = View.inflate(mContext,R.layout.item_home_module_item,null);
            ShapeImageView siv_image = view.findViewById(R.id.siv_image);
            TextView tv_name = view.findViewById(R.id.tv_name);
            GlideUtil.loadImage(mContext,mHomeDataList.get(position).getModuleList().get(i).getLogo(),siv_image);
            tv_name.setText(mHomeDataList.get(position).getModuleList().get(i).getName());
            final int index = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.showToast(mHomeDataList.get(position).getModuleList().get(index).getName());
                }
            });
            moduleViewHolder.ll_module.addView(view);
        }
    }


    /**
     * 设置新闻头部数据
     * @param newsTopViewHolder
     * @param position
     */
    private void setNewsTopData(NewsTopViewHolder newsTopViewHolder, int position) {
        newsTopViewHolder.tv_item_name.setText("新闻");
        newsTopViewHolder.tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOption.toNews();
            }
        });
    }

    /**
     * 设置新闻内容数据
     * @param newsContentViewHolder
     * @param position
     */
    private void setNewContentData(NewsContentViewHolder newsContentViewHolder, final int position) {
        newsContentViewHolder.tv_title.setText(mHomeDataList.get(position).getNews().getTitle());
        newsContentViewHolder.tv_time.setText(DateUtil.getDay(mHomeDataList.get(position).getNews().getPublish_time()));
        newsContentViewHolder.tv_author.setText(mHomeDataList.get(position).getNews().getUser_info().getName());
        GlideUtil.loadImage(mContext,mHomeDataList.get(position).getNews().getUser_info().getAvatar_url(),newsContentViewHolder.iv_image);
        newsContentViewHolder.cv_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(position);
            }
        });
    }

    /**
     * 设置视频头部数据
     * @param videoTopViewHolder
     * @param position
     */
    private void setVideoTopData(VideoTopViewHolder videoTopViewHolder, int position) {
        videoTopViewHolder.tv_item_name.setText("视频");
        videoTopViewHolder.tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOption.toVideo();
            }
        });
    }

    /**
     * 设置视频内容数据
     * @param videoContentViewHolder
     * @param position
     */
    private void setVideoContentData(VideoContentViewHolder videoContentViewHolder, final int position) {
        GlideUtil.loadImage(mContext,mHomeDataList.get(position).getVideo().getVideoUrl(),
                videoContentViewHolder.siv_image);
        videoContentViewHolder.tv_name.setText(mHomeDataList.get(position).getVideo().getTitle());
        videoContentViewHolder.ll_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(position);
            }
        });
    }

    /**
     * 设置图片头部数据
     * @param photoTopViewHolder
     * @param position
     */
    private void setPhotoTopData(PhotoTopViewHolder photoTopViewHolder, int position) {
        photoTopViewHolder.tv_item_name.setText("图片");
        photoTopViewHolder.tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOption.toPhoto();
            }
        });
    }

    /**
     * 设置图片内容数据
     * @param photoContentViewHolder
     * @param position
     */
    private void setPhotoContentData(PhotoContentViewHolder photoContentViewHolder, final int position) {
        GlideUtil.loadImage(mContext,mHomeDataList.get(position).getPhoto().getUrl(),
                photoContentViewHolder.iv_image);
        photoContentViewHolder.cv_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(position);
            }
        });
    }

    /**
     * 获取布局
     * @param resId
     * @return
     */
    private View getView(int resId, ViewGroup parent){
        return LayoutInflater.from(mContext).inflate(resId,parent,false);
    }

    /**
     * BannerViewHolder
     */
    public class BannerViewHolder extends RecyclerView.ViewHolder{

        private Banner mBanner;

        public BannerViewHolder(View itemView) {
            super(itemView);
            mBanner = itemView.findViewById(R.id.banner);
        }
    }

    /**
     * ModuleViewHolder
     */
    public class ModuleViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout ll_module;

        public ModuleViewHolder(View itemView) {
            super(itemView);
            ll_module = itemView.findViewById(R.id.ll_module);
        }

    }

    /**
     * NewTopViewHolder
     */
    public class NewsTopViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_item_name,tv_more;

        public NewsTopViewHolder(View itemView) {
            super(itemView);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_more = itemView.findViewById(R.id.tv_more);
        }
    }

    /**
     * NewContentViewHolder
     */
    public class NewsContentViewHolder extends RecyclerView.ViewHolder{

        private CardView cv_container;
        private TextView tv_title,tv_time,tv_author;
        private ImageView iv_image;

        public NewsContentViewHolder(View itemView) {
            super(itemView);
            cv_container = itemView.findViewById(R.id.cv_container);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_author = itemView.findViewById(R.id.tv_author);
            iv_image = itemView.findViewById(R.id.iv_image);
        }
    }

    /**
     * VideoTopViewHolder
     */
    public class VideoTopViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_item_name,tv_more;

        public VideoTopViewHolder(View itemView) {
            super(itemView);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_more = itemView.findViewById(R.id.tv_more);
        }
    }

    /**
     * VideoContentViewHolder
     */
    public class VideoContentViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout ll_container;
        private ShapeImageView siv_image;
        private TextView tv_name;

        public VideoContentViewHolder(View itemView) {
            super(itemView);
            ll_container = itemView.findViewById(R.id.ll_container);
            siv_image = itemView.findViewById(R.id.siv_image);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }

    /**
     * PhotoTopViewHolder
     */
    public class PhotoTopViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_item_name,tv_more;

        public PhotoTopViewHolder(View itemView) {
            super(itemView);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_more = itemView.findViewById(R.id.tv_more);
        }
    }

    /**
     * PhotoTopViewHolder
     */
    public class PhotoContentViewHolder extends RecyclerView.ViewHolder{

        private CardView cv_container;
        private ImageView iv_image;

        public PhotoContentViewHolder(View itemView) {
            super(itemView);
            cv_container = itemView.findViewById(R.id.cv_container);
            iv_image = itemView.findViewById(R.id.iv_image);
        }
    }

    /**
     * 接口
     */
    public interface ItemClickListener{
        void onItemClick(int position);
    }

    /**
     * 接口
     */
    public interface Option{
        void toNews();
        void toVideo();
        void toPhoto();
    }

}
