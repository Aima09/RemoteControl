package com.yf.remotecontrolclient.media;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.yf.remotecontrolclient.App;
import com.yf.remotecontrolclient.media.model.ImageBean;
import com.yf.remotecontrolclient.media.model.Media;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by xuie on 16-8-31.
 */

public class MediaSource {

    private static final String TAG = "MediaSource";

    private static MediaSource instance;

    //重新扫描
    public static void destroy() {
        instance = null;
    }

    public static MediaSource getInstance() {
        if (instance == null) {
            synchronized (MediaSource.class) {
                instance = new MediaSource();
            }
        }
        return instance;
    }


    private HashMap<String, List<String>> mGruopMap = new HashMap<String, List<String>>();
    private List<ImageBean> imageBeans = new ArrayList<ImageBean>();

    private List<Media> mMusicList = new ArrayList<>();
    private List<Media> mVideoList = new ArrayList<>();
    // private List<Media> mImageList = new ArrayList<>();

    private MediaSource() {
        loadMusic();
        loadVideo();
        getImages();
        imageBeans = subGroupOfImage(mGruopMap);
    }

    public List<Media> getMusicList() {
        return mMusicList;
    }

    public List<Media> getVideoList() {
        return mVideoList;
    }

    public HashMap<String, List<String>> getmGruopMap() {
        return mGruopMap;
    }


    public List<ImageBean> getImageBeans() {
        return imageBeans;
    }

    /**
     * 扫描歌曲
     */
    private void loadMusic() {
        Cursor cursor = null;
        try {
            cursor = App.getAppContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Audio.Media.IS_MUSIC + " = 1", null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            if (cursor == null) {
                Log.e(TAG, "loadMedias cursor is null ");
                return;
            }

            mMusicList.clear();

            while (cursor.moveToNext()) {
                Media media = new Media();
                media.setSize(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
                media.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                media.setDisplayName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                media.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                media.setDuration(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                media.setId(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                media.setAbulmId(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                media.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                mMusicList.add(media);
            }

            Log.d(TAG, "loadMusic: " + mMusicList.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    private void loadVideo() {
        Cursor cursor = null;
        try {
            cursor = App.getAppContext().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    null, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
            if (cursor == null) {
                Log.e(TAG, "loadMedias cursor is null ");
                return;
            }

            mVideoList.clear();

            while (cursor.moveToNext()) {
                Media media = new Media();
                media.setSize(cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.SIZE)));
                media.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE)));
                media.setDisplayName(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)));
                media.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ARTIST)));
                media.setDuration(cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)));
                media.setId(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID)));
//                media.setAbulmId(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.ALBUM_ID)));
                media.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));
                mVideoList.add(media);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    /*
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
	 */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(App.getAppContext(), "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        imageBeans.clear();
        mGruopMap.clear();
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = App.getAppContext().getContentResolver();

        //只查询jpeg和png的图片
        Cursor mCursor = mContentResolver.query(mImageUri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
        while (mCursor.moveToNext()) {
            //获取图片的路径
            String path = mCursor.getString(mCursor
                    .getColumnIndex(MediaStore.Images.Media.DATA));

            //获取该图片的父路径名
            String parentName = new File(path).getParentFile().getName();


            //根据父路径名将图片放入到mGruopMap中
            if (!mGruopMap.containsKey(parentName)) {
                List<String> chileList = new ArrayList<String>();
                chileList.add(path);
                mGruopMap.put(parentName, chileList);
            } else {
                mGruopMap.get(parentName).add(path);
            }
        }

        mCursor.close();
    }


    /**
     * 组装分组界面GridView的数据源，因为我们扫描手机的时候将图片信息放在HashMap中
     * 所以需要遍历HashMap将数据组装成List
     *
     * @param mGruopMap
     * @return
     */
    private List<ImageBean> subGroupOfImage(HashMap<String, List<String>> mGruopMap) {
        List<ImageBean> list = new ArrayList<ImageBean>();
        Iterator<Map.Entry<String, List<String>>> it = mGruopMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<String>> entry = it.next();
            ImageBean mImageBean = new ImageBean();
            String key = entry.getKey();
            List<String> value = entry.getValue();

            mImageBean.setFolderName(key);
            mImageBean.setImageCounts(value.size());
            mImageBean.setTopImagePath(value.get(0));//获取该组的第一张图片

            list.add(mImageBean);
        }
        return list;
    }

    // 获取连接mediaScannerConnection.connect();
    MediaScannerConnectionClient client = new MediaScannerConnectionClient() {

        public void onScanCompleted(String path, Uri uri) {
            // TODO Auto-generated method stub
            mediaScannerConnection.disconnect();
            Log.d("tag", "onScanCompleted");
        }

        public void onMediaScannerConnected() {
            // TODO Auto-generated method stub
            Log.d("tag", "onMediaScannerConnected");
        }
    };
    MediaScannerConnection mediaScannerConnection = new MediaScannerConnection(
            App.getAppContext(), client);

    private void scanfile(File f) {
        if (f.isDirectory()) {
            File[] files = f.listFiles();// 将指定文件夹下面的文件全部列出来
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory())
                        scanfile(files[i]);
                    else {
                        // 调用mediaScannerConnection.scanFile()方法，更新指定类型的文件到数据库中　　　　　　　　　　　　　　mediaScannerConnection.scanFile(files[i].getAbsolutePath(),
                        // "audio/mpeg");
                        //mediaScannerConnection.scanFile(files[i].getAbsolutePath(),"*/*");
                    }
                }
            }
        }
    }
}
