package com.yf.remotecontrolclient.service.imp;


import com.yf.remotecontrolclient.domain.FileCategoryList;
import com.yf.remotecontrolclient.domain.FileList;
import com.yf.remotecontrolclient.domain.OpenFileCategory;
import com.yf.remotecontrolclient.domain.Setopenfileid;
import com.yf.remotecontrolclient.minaclient.tcp.MinaMessageManager;
import com.yf.remotecontrolclient.service.FileBusinessService;
import com.yf.remotecontrolclient.util.JsonAssistant;

public class FileBusinessServiceImpl implements FileBusinessService {
    private JsonAssistant jsonAssistant;
    public static final String CMD = "cmd";

    public FileBusinessServiceImpl() {
        super();
        this.jsonAssistant = new JsonAssistant();
    }

    @Override
    public void sendBSgetFileList(FileList fileList) {
        MinaMessageManager.getInstance()
                .sendControlCmd(jsonAssistant.createGetFileList(fileList));
    }

    @Override
    public void sendBSopenfileid(Setopenfileid setopenfileid) {
        MinaMessageManager.getInstance()
                .sendControlCmd(jsonAssistant.createOpenfileid(setopenfileid));
    }

    @Override
    public void sendBSgetFileCategoryList(FileCategoryList fileCategoryList) {
        MinaMessageManager.getInstance()
                .sendControlCmd(jsonAssistant.createGetFileCategoryList(fileCategoryList));
    }

    @Override
    public void sendBSgetFileCategoryList(OpenFileCategory openFileCategory) {
        MinaMessageManager.getInstance()
                .sendControlCmd(jsonAssistant.createOpenFileCategory(openFileCategory));
    }

}
