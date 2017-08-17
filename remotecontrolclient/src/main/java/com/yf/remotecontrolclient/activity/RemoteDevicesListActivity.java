package com.yf.remotecontrolclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.yf.remotecontrolclient.CommonConstant;
import com.yf.remotecontrolclient.R;
import com.yf.remotecontrolclient.minaclient.tcp.DeviceInfo;
import com.yf.remotecontrolclient.minaclient.tcp.DevicesManager;
import com.yf.remotecontrolclient.minaclient.tcp.MinaMessageManager;
import com.yf.remotecontrolclient.minaclient.tcp.RemoteServerManager;
import com.yf.remotecontrolclient.minaclient.tcp.ServerDataDisposeCenter;

public class RemoteDevicesListActivity extends BaseActivity implements DevicesManager.DevicesUpdateListener {

    @BindView(R.id.back) ImageButton back;
    @BindView(R.id.add_remote_devices) ImageButton addRemoteDevices;
    @BindView(R.id.remote_devices_list) ListView remoteDevicesList;
    @BindView(R.id.switch_remote_server) Switch switchRemoteServer;

    private RemoteDevicesListAdapter devicesListAdapter;
    private List<DeviceInfo> deviceInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_devices_list);
        ButterKnife.bind(this);
//        initspinner();
        initView();
    }

    private void initView() {
        deviceInfos = DevicesManager.getInstance().getDeviceInfos();
        DevicesManager.getInstance().addDevicesUpdateListener(this);
        switchRemoteServer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("RemoteDevicesListActivi", "b:" + b);
                if (b) {
                    RemoteServerManager.getInstance().startRemoteServer();
                } else {
                    Log.d("RemoteDevicesListActivi", "音乐控制测试指令发送。。。");
                    MinaMessageManager.getInstance().sendControlCmd("我是音乐控制测试指令");
                }
            }
        });
        devicesListAdapter = new RemoteDevicesListAdapter();
        remoteDevicesList.setAdapter(devicesListAdapter);
        remoteDevicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DeviceInfo deviceInfo = devicesListAdapter.getItem(i);
                showSetRemoteDeviceDialog(deviceInfo);
            }
        });
        remoteDevicesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                DeviceInfo deviceInfo = devicesListAdapter.getItem(i);
                showRemoveRemoteDevicesDialog(deviceInfo);
                return true;
            }
        });
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        DevicesManager.getInstance().removeDevicesUpdateListener(this);
    }

    @OnClick({R.id.back, R.id.add_remote_devices})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.add_remote_devices:
                startActivity(new Intent(this, QRCodeScanActivity.class));
//                showAddRemoteDevicesDialog();
                break;
        }
    }


    private AlertDialog addDeviceDialog;

    private void showAddRemoteDevicesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_remote_devices, null, false);
        final EditText idInput = (EditText) view.findViewById(R.id.input_device_id);
        final EditText nameInput = (EditText) view.findViewById(R.id.input_device_name);
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (null != addDeviceDialog) {
                    addDeviceDialog.dismiss();
                }
            }
        });
        view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                DeviceInfo deviceInfo = new DeviceInfo(nameInput.getText().toString().trim(), idInput.getText().toString().trim());
                ServerDataDisposeCenter.addDevice(deviceInfo);
                if (null != addDeviceDialog) {
                    addDeviceDialog.dismiss();
                }
            }
        });
        builder.setView(view);
        addDeviceDialog = builder.create();
        addDeviceDialog.show();
    }

    private AlertDialog removeDeviceDialog;

    private void showRemoveRemoteDevicesDialog(final DeviceInfo deviceInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_remove_remote_device, null, false);
        TextView removeInfo = (TextView) view.findViewById(R.id.remove_info);
        removeInfo.setText("是否删除设备：设备名：" + deviceInfo.getName() + "设备Id：" + deviceInfo.getId());
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (null != removeDeviceDialog) {
                    removeDeviceDialog.dismiss();
                }
            }
        });
        view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                ServerDataDisposeCenter.removeDevice(deviceInfo);
                if (null != removeDeviceDialog) {
                    removeDeviceDialog.dismiss();
                }
            }
        });
        builder.setView(view);
        removeDeviceDialog = builder.create();
        removeDeviceDialog.show();
    }

    private AlertDialog setRemoteDeviceDialog;

    private EditText inputDeviceName;
    private EditText inputDeviceId;
    private DeviceInfo deviceInfo;
    private String modifyId;

    private void showSetRemoteDeviceDialog(DeviceInfo devInfo) {
        deviceInfo = devInfo;
        modifyId = devInfo.getId();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_set_remote_device, null, false);
        inputDeviceName = (EditText) view.findViewById(R.id.input_name);
        inputDeviceId = (EditText) view.findViewById(R.id.input_id);
        inputDeviceName.setText(deviceInfo.getName());
        inputDeviceId.setText(deviceInfo.getId());
        view.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                deviceInfo.setName(inputDeviceName.getText().toString().trim());
                deviceInfo.setId(inputDeviceId.getText().toString().trim());
                ServerDataDisposeCenter.renameDeviceName(deviceInfo, modifyId);
            }
        });
        view.findViewById(R.id.set_remote_control_device).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                ServerDataDisposeCenter.setRemoteReceiverId(deviceInfo.getId());
                if (null != setRemoteDeviceDialog) {
                    setRemoteDeviceDialog.dismiss();
                }
                devicesListAdapter.notifyDataSetChanged();
            }
        });
        builder.setView(view);
        setRemoteDeviceDialog = builder.create();
        setRemoteDeviceDialog.show();
    }

    @Override public void devicesUpdate() {
        deviceInfos = DevicesManager.getInstance().getDeviceInfos();
        devicesListAdapter.notifyDataSetChanged();
    }


    private class RemoteDevicesListAdapter extends BaseAdapter {

        @Override public int getCount() {
            return deviceInfos.size();
        }

        @Override public DeviceInfo getItem(int i) {
            return deviceInfos.get(i);
        }

        @Override public long getItemId(int i) {
            return i;
        }

        @Override public View getView(int i, View itemView, ViewGroup parent) {
            ViewHolder holder;
            if (itemView == null) {
                holder = new ViewHolder();
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.remote_device_item,
                        parent, false);
                holder.deviceName = (TextView) itemView.findViewById(R.id.device_name);
                holder.deviceId = (TextView) itemView.findViewById(R.id.device_id);
                holder.deviceCheck = (CheckBox) itemView.findViewById(R.id.device_check_state);
                itemView.setTag(holder);
            } else {
                holder = (ViewHolder) itemView.getTag();
            }
            DeviceInfo deviceInfo = getItem(i);
            holder.deviceName.setText(deviceInfo.getName());
            holder.deviceId.setText(deviceInfo.getId());
            String selectDeviceId = ServerDataDisposeCenter.getRemoteReceiverId();
            if (selectDeviceId.equals(deviceInfo.getId())) {
                holder.deviceCheck.setVisibility(View.VISIBLE);
                holder.deviceCheck.setChecked(true);
            } else {
                holder.deviceCheck.setVisibility(View.INVISIBLE);
                holder.deviceCheck.setChecked(false);
            }
            return itemView;
        }

        private class ViewHolder {
            TextView deviceName;
            TextView deviceId;
            CheckBox deviceCheck;
        }

    }

}
