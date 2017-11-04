package com.yf.minalibrary.encoderdecoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * Created by wuhuai on 2016/11/12 .
 * ;
 */

public class BaseMessageDecoder {
    /**
     * 解码器准备工作
     * @param typeInt  类型
     * @param in  相当于流
     * @return
     */
    public MessageDecoderResult deadWork(int typeInt,IoBuffer in){
        if (in.remaining() < 4) {
            return MessageDecoderResult.NEED_DATA;
        }
        //类型
        if(typeInt==in.getInt()){
            //是本解码器的数据
            return  MessageDecoderResult.OK;
        }else{
            //不是本解码器的数据
            return  MessageDecoderResult.NOT_OK;
        }
    }
}