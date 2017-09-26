package com.example.dell.fangfangsmall.util;

import com.example.dell.fangfangsmall.bean.ComBean;

/**
 * Created by lyw on 2017/9/22.
 */

public class SerialControl extends SerialHelper {
    @Override
    protected void onDataReceived(ComBean ComRecData) {
        //数据接收量大或接收时弹出软键盘，界面会卡顿,可能和6410的显示性能有关
        //直接刷新显示，接收数据量大时，卡顿明显，但接收与显示同步。
        //用线程定时刷新显示可以获得较流畅的显示效果，但是接收数据速度快于显示速度时，显示会滞后。
        //最终效果差不多-_-，线程定时刷新稍好一些。
        //	DispQueue.AddQueue(ComRecData);//线程定时刷新显示(推荐)
			/*
			runOnUiThread(new Runnable()//直接刷新显示
			{
				public void run()
				{
					DispRecData(ComRecData);
				}
			});*/
    }
}
