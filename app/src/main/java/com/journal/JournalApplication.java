package com.journal;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.journal.utils.FileUtil;
import com.journal.utils.ToastUtils;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;

/**
 * 类名称：com.journal
 * 类描述：
 * 创建人：小土豆   QQ:515353776
 * 创建时间：2017.12.29
 */
public class JournalApplication extends Application {

    public static JournalApplication gContext;
    public static int mAppState = -1;
    public static DisplayImageOptions mPictureOptions;

    @Override
    public void onCreate() {
        super.onCreate();
        gContext = this;
        ToastUtils.mToast = Toast.makeText(gContext, "", Toast.LENGTH_LONG);
        mAppState = -1;
        initImageLoader(this);
    }


    public static int getAppStatus() {
        return mAppState;
    }


    public static void initImageLoader(Context context) {
        File cacheDir = new File(FileUtil.getImgDir());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory().discCache(new LimitedAgeDiskCache(cacheDir, 5000)) // default
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
        mPictureOptions = new DisplayImageOptions.Builder().showStubImage(R.drawable.image_placeholder)
                .showImageForEmptyUri(R.drawable.image_placeholder).showImageOnFail(R.drawable.image_placeholder).cacheInMemory().cacheOnDisc()
                .build();
    }

}
