package com.yf.remotecontrolclient.service;


import com.yf.remotecontrolclient.domain.File;
import com.yf.remotecontrolclient.domain.FileList;
import com.yf.remotecontrolclient.domain.FileShowList;
import com.yf.remotecontrolclient.domain.OpenFile;
import com.yf.remotecontrolclient.domain.OpenZyglq;

public interface ZyglqBusinessService {
    public void sendBsOpenZyglq(OpenZyglq openZyglq);

    public void sendBsFileList(FileList fileList);

    public void sendBsFileShowList(FileShowList fileShowList);

    public void sendBsOpenFile(OpenFile openFile);

    public void sendBSfinishIFileExplorerCommonActivity(File file);
}
