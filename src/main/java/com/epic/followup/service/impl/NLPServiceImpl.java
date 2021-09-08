package com.epic.followup.service.impl;

import com.epic.followup.conf.RpcConfig;
import com.epic.followup.service.NLPService;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author : zx
 * @version V1.0
 */

@Service
public class NLPServiceImpl implements NLPService {

    @Autowired
    private RpcConfig rpcConfig;
    private org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    private XmlRpcClient client;

    public NLPServiceImpl(){
        try {
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL("http://127.0.0.1:8021/"));
//            config.setServerURL(new URL("http://follwup-test.cmas2020.cn:8021/"));

            client = new XmlRpcClient();
            client.setConfig(config);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int fun_add() {
        try {
            Object[] params = new Object[]{31, 9};
            return (int)client.execute("fun_add", params);
        }catch (XmlRpcException e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public double questionFunSelect(int i, String answer) throws XmlRpcException {

        Object[] params = new Object[]{answer};
//            return (float)client.execute("ques_"+i+"_detection", params);
        double r = (double)client.execute("ques_"+i+"_detection", params);
        return r;
    }

    @Override
    /*
     * 0 没有人脸 1 不是同一人 2 是同一个人
     */
    public int baiduFaceIden_base64(String data1, String data2) throws XmlRpcException {

//        log.info(data1);
//        log.info(data2);

        Object[] params = new Object[]{data1, data2};

        return (int)client.execute("baiduFaceIden_base64", params);
    }

}
