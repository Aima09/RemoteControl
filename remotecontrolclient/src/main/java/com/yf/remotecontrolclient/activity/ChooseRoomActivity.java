package com.yf.remotecontrolclient.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.yf.remotecontrolclient.CommonConstant;
import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.activity.service.MouseService;
import com.yf.remotecontrolclient.domain.Equipment;
import com.yf.remotecontrolclient.minaclient.tcp.RemoteServerManager;
import com.yf.remotecontrolclient.util.SpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ChooseRoomActivity extends BaseActivity implements OnItemClickListener {
    public static final String TAG = "ChooseRoomActivity";
    public static List<Equipment> equipments = new ArrayList<Equipment>();
    private SimpleAdapter mSchedule;
    private boolean isNotifyDataSetChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "size:=" + equipments.size());
        setContentView(R.layout.activity_chooseroom);
        initspinner();
        //绑定XML中的ListView，作为Item的容器
        ListView list = (ListView) findViewById(R.id.MyListView);

        ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < equipments.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemTitle", "room:");
            map.put("ItemText", "ip:" + equipments.get(i).getIp());
            if (equipments.get(i).getIp().equals(MouseService.equipment.getIp())) {
                map.put("ItemDq", R.drawable.dq1);
            } else {
                map.put("ItemDq", null);
            }
            mylist.add(map);
        }
        mSchedule = new SimpleAdapter(this, //没什么解释
                mylist,//数据来源
                R.layout.my_listitem,//ListItem的XML实现

                //动态数组与ListItem对应的子项
                new String[]{"ItemTitle", "ItemText", "ItemDq"},

                //ListItem的XML文件里面的两个TextView ID
                new int[]{R.id.ItemTitle, R.id.ItemText, R.id.iv_dq});
        //添加并且显示
        list.setAdapter(mSchedule);
        list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        //MinaActivity.getMainActivity().restart();
        //当前设备
        SpUtil.putInt(getApplication(),CommonConstant.LINK_TYPE_KEY,CommonConstant.LINE_TYPE_LOCAL);
        MouseService.equipment = equipments.get(position);
        //存下设
        SpUtil.putString(getApplication(),CommonConstant.LOCAL_LINK_ADRESS_KEY,MouseService.equipment.getIp());
        RemoteServerManager.getInstance().startRemoteServer();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
