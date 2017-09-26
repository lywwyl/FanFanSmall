package com.example.dell.fangfangsmall.util;

import android.util.Log;

import com.yuntongxun.ecsdk.ECChatManager;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;

/**
 * Created by lyw on 2017/9/22.
 */

public class SendToRobot {
    public  void SendRobot(String order,String type) {
        try {
            ECMessage msg = ECMessage.createECMessage(ECMessage.Type.TXT);
           // msg.setForm("123456");//设置消息发送者
            msg.setMsgTime(System.currentTimeMillis());
            msg.setTo("12345");// 设置消息接收者
            msg.setSessionId("12345");//接收者ID
            // 设置消息发送类型（发送或者接收）
            msg.setDirection(ECMessage.Direction.SEND);
            // 创建一个文本消息体，并添加到消息对象中
            // 文本消息由用户自定义组装，接收端按照规则解析
            // text=”发送的指令”；
            ECTextMessageBody msgBody = new ECTextMessageBody(order);
            msg.setBody(msgBody);
            // 传入自定义消息类型
            msg.setUserData(type);
            // 调用SDK发送文本接口发送消息到服务器
            ECChatManager manager = ECDevice.getECChatManager();
            manager.sendMessage(msg, new ECChatManager.OnSendMessageListener() {
                @Override
                public void onProgress(String s, int i, int i1) {

                }

                @Override
                public void onSendMessageComplete(ECError error, ECMessage message) {
                    // 处理消息发送结果
                    if(message == null) {
                        return ;
                    }
                    // 将发送的消息更新到本地数据库并刷新UI
                    Log.i("WWDZ", "发送成功 ： " +message);
                }
            });   } catch (Exception e) {
            // 处理发送异常
            Log.i("WWDZ", "send message fail , e=" + e.getMessage());
        }}


    public void SendGroup(){
        try {
            // 组建一个待发送的ECMessage
            ECMessage msg = ECMessage.createECMessage(ECMessage.Type.TXT);
            //设置消息的属性：发出者，接受者，发送时间等
          //  msg.setForm("Tony的账号");
            msg.setMsgTime(System.currentTimeMillis());
            // 设置消息接收者
            msg.setTo("gg802113721");
            msg.setSessionId("gg802113721");
            // 设置消息发送类型（发送或者接收）
            msg.setDirection(ECMessage.Direction.SEND);
            // 创建一个文本消息体，并添加到消息对象中
            ECTextMessageBody msgBody = new ECTextMessageBody("来自Tony的消息");
            msg.setBody(msgBody);

// 图片、文件、语音可以参考点对点消息体创建
            // 调用SDK发送接口发送消息到服务器
            ECChatManager manager = ECDevice.getECChatManager();
            manager.sendMessage(msg, new ECChatManager.OnSendMessageListener() {
                @Override
                public void onSendMessageComplete(ECError error, ECMessage message) {
                    // 处理消息发送结果
                    if(message == null) {
                        return ;
                    }
                    // 将发送的消息更新到本地数据库并刷新UI
                    Log.i("WWDZ","发送成功");
                }

                @Override
                public void onProgress(String msgId, int totalByte, int progressByte) {
                    // 处理文件发送上传进度（尽上传文件、图片时候SDK回调该方法）
                }


            });
        } catch (Exception e) {
            // 处理发送异常
            Log.e("ECSDK_Demo", "send message fail , e=" + e.getMessage());
        }


    }
}
