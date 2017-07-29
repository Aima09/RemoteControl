package com.yf.remotecontrolclient.service;


import com.yf.remotecontrolclient.domain.FileCategoryList;
import com.yf.remotecontrolclient.domain.FileList;
import com.yf.remotecontrolclient.domain.OpenFileCategory;
import com.yf.remotecontrolclient.domain.Setopenfileid;

public interface FileBusinessService {
    public void sendBSgetFileList(FileList fileList);

    public void sendBSopenfileid(Setopenfileid setopenfileid);

    public void sendBSgetFileCategoryList(FileCategoryList fileCategoryList);

    public void sendBSgetFileCategoryList(OpenFileCategory openFileCategory);
}
