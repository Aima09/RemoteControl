package com.yf.remotecontrolclient.service;


import com.yf.remotecontrolclient.domain.ImageFolder;
import com.yf.remotecontrolclient.domain.ImageFolderList;
import com.yf.remotecontrolclient.domain.ImageList;
import com.yf.remotecontrolclient.domain.OpenImage;

public interface ImageBusinessService {
    public void sendBsgetFolderList(ImageFolderList imageFolderList);

    public void sendBSgetImageList(ImageList imageList);

    public void sendBsopenFolder(ImageFolder imageFolder);

    public void senBsopenImage(OpenImage openImage);
}
