package com.yf.remotecontrolclient.service.imp;


import com.yf.remotecontrolclient.domain.File;
import com.yf.remotecontrolclient.domain.FileList;
import com.yf.remotecontrolclient.domain.FileShowList;
import com.yf.remotecontrolclient.domain.OpenFile;
import com.yf.remotecontrolclient.domain.OpenZyglq;
import com.yf.remotecontrolclient.minaclient.tcp.MinaCmdManager;
import com.yf.remotecontrolclient.service.ZyglqBusinessService;
import com.yf.remotecontrolclient.util.JsonAssistant;

public class ZyglqBusinessServiceImpl implements ZyglqBusinessService {
    private JsonAssistant jsonAssistant;
    public static final String CMD = "cmd";

    public ZyglqBusinessServiceImpl() {
        super();
        this.jsonAssistant = new JsonAssistant();
    }

    @Override
    public void sendBsOpenZyglq(OpenZyglq openZyglq) {
        MinaCmdManager.getInstance()
                .sendControlCmd(jsonAssistant.createOpenZyglq(openZyglq));
    }

    @Override
    public void sendBsFileList(FileList fileList) {
        MinaCmdManager.getInstance()
                .sendControlCmd(jsonAssistant.createFileList(fileList));
    }

    @Override
    public void sendBsFileShowList(FileShowList fileShowList) {
        MinaCmdManager.getInstance()
                .sendControlCmd(jsonAssistant.createFileShowList(fileShowList));
    }

    @Override
    public void sendBsOpenFile(OpenFile openFile) {
        MinaCmdManager.getInstance()
                .sendControlCmd(jsonAssistant.createOpenFile(openFile));
    }


    @Override
    public void sendBSfinishIFileExplorerCommonActivity(File file) {
        MinaCmdManager.getInstance()
                .sendControlCmd(jsonAssistant.createFile(file));
    }


}