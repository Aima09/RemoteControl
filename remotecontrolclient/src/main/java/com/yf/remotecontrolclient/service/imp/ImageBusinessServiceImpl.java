package com.yf.remotecontrolclient.service.imp;


import com.yf.remotecontrolclient.domain.ImageFolder;
import com.yf.remotecontrolclient.domain.ImageFolderList;
import com.yf.remotecontrolclient.domain.ImageList;
import com.yf.remotecontrolclient.domain.OpenImage;
import com.yf.remotecontrolclient.minaclient.tcp.MinaCmdManager;
import com.yf.remotecontrolclient.service.ImageBusinessService;
import com.yf.remotecontrolclient.util.JsonAssistant;

public class ImageBusinessServiceImpl implements ImageBusinessService {
    private JsonAssistant jsonAssistant;
    public static final String CMD = "cmd";

    public ImageBusinessServiceImpl() {
        super();
        this.jsonAssistant = new JsonAssistant();
    }

    @Override
    public void sendBsgetFolderList(ImageFolderList imageFolderList) {
        MinaCmdManager.getInstance()
                .sendControlCmd(jsonAssistant.createGetFolderList(imageFolderList));
    }

    @Override
    public void sendBSgetImageList(ImageList imageList) {
        MinaCmdManager.getInstance()
                .sendControlCmd(jsonAssistant.createGetImageList(imageList));
    }

    @Override
    public void sendBsopenFolder(ImageFolder imageFolder) {
        MinaCmdManager.getInstance()
                .sendControlCmd(jsonAssistant.createOpenImageFolder(imageFolder));
    }

    public void senBsopenImage(OpenImage openImage) {
        MinaCmdManager.getInstance()
                .sendControlCmd(jsonAssistant.createOpenImage(openImage));
    }
}
