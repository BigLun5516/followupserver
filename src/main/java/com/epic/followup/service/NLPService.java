package com.epic.followup.service;

import org.apache.xmlrpc.XmlRpcException;


/**
 * @author : zx
 * @version V1.0
 */


public interface NLPService {

    int fun_add();
    double questionFunSelect(int i, String answer) throws XmlRpcException;

    /*
     * 0 没有人脸 1 不是同一人 2 是同一个人
     */
    int baiduFaceIden_base64(String data1, String data2) throws XmlRpcException;

}
