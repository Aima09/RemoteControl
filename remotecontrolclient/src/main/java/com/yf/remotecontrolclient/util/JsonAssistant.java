package com.yf.remotecontrolclient.util;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import com.yf.remotecontrolclient.domain.Action;
import com.yf.remotecontrolclient.domain.Boot;
import com.yf.remotecontrolclient.domain.File;
import com.yf.remotecontrolclient.domain.FileCategoryList;
import com.yf.remotecontrolclient.domain.FileList;
import com.yf.remotecontrolclient.domain.FileShowList;
import com.yf.remotecontrolclient.domain.Gateway;
import com.yf.remotecontrolclient.domain.ImageFolder;
import com.yf.remotecontrolclient.domain.ImageFolderList;
import com.yf.remotecontrolclient.domain.ImageList;
import com.yf.remotecontrolclient.domain.OpenBrower;
import com.yf.remotecontrolclient.domain.OpenFile;
import com.yf.remotecontrolclient.domain.OpenFileCategory;
import com.yf.remotecontrolclient.domain.OpenImage;
import com.yf.remotecontrolclient.domain.OpenSettings;
import com.yf.remotecontrolclient.domain.OpenZyglq;
import com.yf.remotecontrolclient.domain.Palpitation;
import com.yf.remotecontrolclient.domain.Position;
import com.yf.remotecontrolclient.domain.Setmode;
import com.yf.remotecontrolclient.domain.Setopenfileid;
import com.yf.remotecontrolclient.domain.Setplaysongid;
import com.yf.remotecontrolclient.domain.Setplaystatus;
import com.yf.remotecontrolclient.domain.Setplayvideoid;
import com.yf.remotecontrolclient.domain.Setvideoplaystatus;
import com.yf.remotecontrolclient.domain.Setvideovolumeadd;
import com.yf.remotecontrolclient.domain.Setvolumeadd;
import com.yf.remotecontrolclient.domain.SongList;
import com.yf.remotecontrolclient.domain.VideoList;
import com.yf.remotecontrolclient.domain.WebsiteList;
import com.yf.remotecontrolclient.domain.Writer;

/**
 * Created by sujuntao on 2017/6/22.
 */

public class JsonAssistant {
    private JSONObject jsonObject;

    /**
     * 解析boot（连接）
     *
     * @return
     */
    public Boot getBoot(String data) {
        Boot b = null;
        try {
            jsonObject = new JSONObject(data);
            Gson gson = new Gson();
            b = gson.fromJson(data, Boot.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    /**
     * 创建boot（连接）
     *
     * @param
     * @return
     */
    public String paseBoot(Boot boot) {
        String json = null;
        try {
            Gson gson = new Gson();
            json = gson.toJson(boot);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 创建网关
     */
    public String createMessage(Gateway gateway) {
        try {
            jsonObject = new JSONObject();
            jsonObject.put("key", gateway.getKey());
            jsonObject.put("gwID", gateway.getGwID());
            jsonObject.put("gwIP", gateway.getGwIp());
            jsonObject.put("gwPort", gateway.getGwPort() + "");
            return jsonObject.toString();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    // 解析心跳
    public Palpitation pasePalpitation(String data) {
        Palpitation palpitation = null;
        try {
            jsonObject = new JSONObject(data);
            palpitation = new Palpitation(jsonObject.getString("cmd"),
                    jsonObject.getString("ip"));
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return palpitation;
    }

    public JSONObject paseJsonObject(String data) {
        try {
            return new JSONObject(data);
        } catch (JSONException e) {
            throw new RuntimeException();
        }
    }

    public String createPosition(Position position) {
        try {
            jsonObject = new JSONObject();
            jsonObject.put("cmd", position.getCmd());
            jsonObject.put("x", position.getX());
            jsonObject.put("y", position.getY());
            return jsonObject.toString();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public String createAction(Action action) {
        try {
            jsonObject = new JSONObject();
            jsonObject.put("cmd", action.getCmd());
            jsonObject.put("data", action.getData());
            return jsonObject.toString();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * 创建心跳
     *
     * @return
     */
    public String createPalpitation(Palpitation palpitation) {
        try {
            jsonObject = new JSONObject();
            jsonObject.put("cmd", palpitation.getCmd());
            jsonObject.put("ip", palpitation.getIp());
            return jsonObject.toString();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public String createWriter(Writer writer) {
        try {
            jsonObject = new JSONObject();
            jsonObject.put("cmd", writer.getCmd());
            jsonObject.put("data", writer.getData());
            return jsonObject.toString();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public String createGetSongList(SongList songList) {
        try {
            jsonObject = new JSONObject();
            jsonObject.put("cmd", songList.getCmd());
            jsonObject.put("pagesize", songList.getPageSize());
            jsonObject.put("pageindex", songList.getPageIndex());
            return jsonObject.toString();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public SongList paseGetSongList(String data) {
        SongList songList = null;
        try {
            Gson gson = new Gson();
            songList = gson.fromJson(data, SongList.class);// 对于javabean直接给出class实例
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songList;
    }

    public String createSetplaysongid(Setplaysongid setplaysongid) {
        Gson gson = new Gson();
        return gson.toJson(setplaysongid);
    }

    public String createSetplayvideoid(Setplayvideoid setvideoplaysongid) {
        Gson gson = new Gson();
        return gson.toJson(setvideoplaysongid);
    }

    public String createSetvideovolumeadd(Setvideovolumeadd setvideovolumeadd) {
        Gson gson = new Gson();
        return gson.toJson(setvideovolumeadd);
    }

    public String createSetvolumeadd(Setvolumeadd setvolumeadd) {
        Gson gson = new Gson();
        return gson.toJson(setvolumeadd);
    }

    public String createSetplaystatus(Setplaystatus setplaystatus) {
        Gson gson = new Gson();
        return gson.toJson(setplaystatus);
    }

    public String createSetvideoplaystatus(Setvideoplaystatus setvideoplaystatus) {
        Gson gson = new Gson();
        return gson.toJson(setvideoplaystatus);
    }

    public Setplaystatus paseSetplaystatus(String data) {
        Setplaystatus setplaystatus = null;
        try {
            Gson gson = new Gson();
            setplaystatus = gson.fromJson(data, Setplaystatus.class);// 对于javabean直接给出class实例
        } catch (Exception e) {
            e.printStackTrace();
        }
        return setplaystatus;
    }

    public Setmode paseSetmode(String data) {
        Setmode setmode = null;
        try {
            Gson gson = new Gson();
            setmode = gson.fromJson(data, Setmode.class);// 对于javabean直接给出class实例
        } catch (Exception e) {
            e.printStackTrace();
        }
        return setmode;
    }

    /**
     * 解析设置播放视频状态
     *
     * @param data
     * @return
     */
    public Setvideoplaystatus paseSetvideoplaystatus(String data) {
        Setvideoplaystatus setvideoplaystatus = null;
        try {
            setvideoplaystatus = new Setvideoplaystatus();
            jsonObject = new JSONObject(data);
            setvideoplaystatus.setCmd(jsonObject.getString("cmd"));
            setvideoplaystatus.setStatus(jsonObject.getString("status"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return setvideoplaystatus;
    }

    public VideoList paseGetVedioList(String data) {
        VideoList vedioList = new VideoList();
        try {
            jsonObject = new JSONObject(data);
            vedioList.setCmd(jsonObject.getString("cmd"));
            vedioList.setPageIndex(jsonObject.getInt("pageindex"));
            vedioList.setPageSize(jsonObject.getInt("pagesize"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vedioList;
    }

    public String createGetVideoList(VideoList vedioList) {
        String json = null;
        try {
            Gson gson = new Gson();
            json = gson.toJson(vedioList);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public VideoList paseGetVideoList(String data) {
        VideoList videoList = null;
        try {
            Gson gson = new Gson();
            videoList = gson.fromJson(data, VideoList.class);// 对于javabean直接给出class实例
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoList;
    }

    public String createSetmode(Setmode setmode) {
        String json = null;
        try {
            Gson gson = new Gson();
            json = gson.toJson(setmode);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public String createGetFolderList(ImageFolderList imageFolderList) {
        String json = null;
        try {
            Gson gson = new Gson();
            json = gson.toJson(imageFolderList);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public ImageFolderList paseGetimageFolderList(String data) {
        ImageFolderList imageFolderList = null;
        try {
            Gson gson = new Gson();
            imageFolderList = gson.fromJson(data, ImageFolderList.class);// 对于javabean直接给出class实例
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageFolderList;
    }

    public ImageList paseGetimageList(String data) {
        ImageList imageList = null;
        try {
            Gson gson = new Gson();
            imageList = gson.fromJson(data, ImageList.class);// 对于javabean直接给出class实例
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageList;
    }


    public String createGetImageList(ImageList imageList) {
        String json = null;
        try {
            Gson gson = new Gson();
            json = gson.toJson(imageList);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public String createOpenImageFolder(ImageFolder imageFolder) {
        String json = null;
        try {
            Gson gson = new Gson();
            json = gson.toJson(imageFolder);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public String createOpenImage(OpenImage openImage) {
        String json = null;
        try {
            Gson gson = new Gson();
            json = gson.toJson(openImage);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public String createOpenBrowser(OpenBrower openBrower) {
        String json = null;
        try {
            Gson gson = new Gson();
            json = gson.toJson(openBrower);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public String createOpenSettings(OpenSettings openSettings) {
        String json = null;
        try {
            Gson gson = new Gson();
            json = gson.toJson(openSettings);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public String createOpenZyglq(OpenZyglq openZyglq) {
        String json = null;
        try {
            Gson gson = new Gson();
            json = gson.toJson(openZyglq);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public String createGetFileList(FileList fileList) {
        String json = null;
        try {
            Gson gson = new Gson();
            json = gson.toJson(fileList);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public File paseGetfile(String data) {
        File file = null;
        try {
            Gson gson = new Gson();
            file = gson.fromJson(data, File.class);// 对于javabean直接给出class实例
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public OpenZyglq paseOpenZyglq(String data) {
        OpenZyglq openZyglq = null;
        try {
            Gson gson = new Gson();
            openZyglq = gson.fromJson(data, OpenZyglq.class);// 对于javabean直接给出class实例
        } catch (Exception e) {
            e.printStackTrace();
        }
        return openZyglq;
    }

    public String createFileList(FileList fileList) {
        String json = null;
        try {
            Gson gson = new Gson();
            json = gson.toJson(fileList);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public FileList paseFileList(String data) {
        FileList fileList = null;
        try {
            Gson gson = new Gson();
            fileList = gson.fromJson(data, FileList.class);// 对于javabean直接给出class实例
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileList;
    }

    public String createOpenfileid(Setopenfileid setopenfileid) {
        String json = null;
        try {
            Gson gson = new Gson();
            json = gson.toJson(setopenfileid);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public Setopenfileid paseOpenfileid(String data) {
        Setopenfileid setopenfileid = null;
        try {
            Gson gson = new Gson();
            setopenfileid = gson.fromJson(data, Setopenfileid.class);// 对于javabean直接给出class实例
        } catch (Exception e) {
            e.printStackTrace();
        }
        return setopenfileid;
    }

    public String createGetFileCategoryList(FileCategoryList fileCategoryList) {
        String json = null;
        try {
            Gson gson = new Gson();
            json = gson.toJson(fileCategoryList);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public FileCategoryList paseFileCategoryList(String data) {
        FileCategoryList fileCategoryList = null;
        try {
            Gson gson = new Gson();
            fileCategoryList = gson.fromJson(data, FileCategoryList.class);// 对于javabean直接给出class实例
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileCategoryList;
    }

    public String createOpenFileCategory(OpenFileCategory openFileCategory) {
        String json = null;
        try {
            Gson gson = new Gson();
            json = gson.toJson(openFileCategory);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public OpenFileCategory paseOpenFileCategory(String data) {
        OpenFileCategory openFileCategory = null;
        try {
            Gson gson = new Gson();
            openFileCategory = gson.fromJson(data, OpenFileCategory.class);// 对于javabean直接给出class实例
        } catch (Exception e) {
            e.printStackTrace();
        }
        return openFileCategory;
    }

    public String createFileShowList(FileShowList fileShowList) {
        String json = null;
        try {
            Gson gson = new Gson();
            json = gson.toJson(fileShowList);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public FileShowList paseFileShowList(String data) {
        FileShowList fileShowList = null;
        try {
            Gson gson = new Gson();
            fileShowList = gson.fromJson(data, FileShowList.class);// 对于javabean直接给出class实例
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileShowList;
    }

    public String createOpenFile(OpenFile openFile) {
        String json = null;
        try {
            Gson gson = new Gson();
            json = gson.toJson(openFile);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public String createFile(File file) {
        String json = null;
        try {
            Gson gson = new Gson();
            json = gson.toJson(file);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public String createWebsite(WebsiteList websiteList) {
        String json = null;
        try {
            Gson gson = new Gson();
            json = gson.toJson(websiteList);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public WebsiteList paseWebsiteList(String websiteListJson) {
        WebsiteList websiteList = null;
        try {
            Gson gson = new Gson();
            websiteList = gson.fromJson(websiteListJson, WebsiteList.class);// 对于javabean直接给出class实例
        } catch (Exception e) {
            e.printStackTrace();
        }
        return websiteList;
    }
}
