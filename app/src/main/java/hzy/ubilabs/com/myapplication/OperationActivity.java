package hzy.ubilabs.com.myapplication;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.util.Timer;
import java.util.TimerTask;

import hzy.ubilabs.com.myapplication.BLE.fastble.BluetoothService;
import hzy.ubilabs.com.myapplication.BLE.fastble.conn.BleCharacterCallback;
import hzy.ubilabs.com.myapplication.BLE.fastble.exception.BleException;
import hzy.ubilabs.com.myapplication.BLE.fastble.utils.HexUtil;

public class OperationActivity extends AppCompatActivity {

    private BluetoothService mBluetoothService;
    private BluetoothGatt gatt;
    private BluetoothGattService service;
    private String back = "";

    private ImageButton up;
    private ImageButton down;
    private ImageButton left;
    private ImageButton right;
    private Button btnShaChe;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                getService();
                BLE_start_listener();
            } else if (msg.what == 2) {
                BLE_start_writer();
            }
            super.handleMessage(msg);
        }
    };

    private static final String TAG = "OperationActivity";
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.i(TAG, "onTouch event ===========> " + event.getAction());
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (v.getId() == R.id.up) {
                        writer("1");
                    } else if (v.getId() == R.id.down) {
                        writer("2");
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    writer("5");
                    break;
            }
            return true;
        }
    };

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.left:
                    writer("3");
                    break;
                case R.id.right:
                    writer("4");
                    break;
                case R.id.btnShaChe:
                    writer("6");
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);
        up = findViewById(R.id.up);
        down = findViewById(R.id.down);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        btnShaChe = findViewById(R.id.btnShaChe);

        left.setOnClickListener(listener);
        right.setOnClickListener(listener);
        btnShaChe.setOnClickListener(listener);

        up.setOnTouchListener(touchListener);
        down.setOnTouchListener(touchListener);
        bindService();
    }

    public void writer(String msg) {
        final BluetoothGattCharacteristic characteristic = mBluetoothService.getCharacteristic();
        mBluetoothService.write(
                characteristic.getService().getUuid().toString(),
                characteristic.getUuid().toString(),
                String.valueOf(HexUtil.encodeHex(msg.getBytes())),
                new BleCharacterCallback() {

                    @Override
                    public void onSuccess(final BluetoothGattCharacteristic characteristic) {
                        //成功写入操作
                    }

                    @Override
                    public void onFailure(final BleException exception) {
                        StartBLEWriterAfter(150);
                    }

                    @Override
                    public void onInitiatedResult(boolean result) {

                    }

                });
    }  //写数据

    private void StartBLEListenerAfter(int time) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        };
        timer.schedule(task, time);
    }

    private void StartBLEWriterAfter(int time) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
            }
        };
        timer.schedule(task, time);
    }

    public void getService() {
        gatt = mBluetoothService.getGatt();
        mBluetoothService.setService(gatt.getServices().get(gatt.getServices().size() - 1));
    }

    private void BLE_start_writer() {
        if (service.getCharacteristics().size() < 2) return;
        service = mBluetoothService.getService();
        mBluetoothService.setCharacteristic((service.getCharacteristics().get(service.getCharacteristics().size() - 2)));
        mBluetoothService.setCharaProp(1);
    }

    private void BLE_start_listener() {
        service = mBluetoothService.getService();
        mBluetoothService.setCharacteristic((service.getCharacteristics().get(service.getCharacteristics().size() - 1)));
        mBluetoothService.setCharaProp(2);
        final BluetoothGattCharacteristic characteristic = mBluetoothService.getCharacteristic();
        mBluetoothService.notify(
                characteristic.getService().getUuid().toString(),
                characteristic.getUuid().toString(),
                new BleCharacterCallback() {

                    @Override
                    public void onSuccess(final BluetoothGattCharacteristic characteristic) {
                        OperationActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String hexEncode = (String.valueOf(HexUtil.encodeHex(characteristic.getValue())));//得到16进制字符串
                                byte[] bytes = HexUtil.hexStringToBytes(hexEncode);//转为byte类型
                                back = ("" + new String(bytes));//解出字符串
                            }
                        });
                    }

                    @Override
                    public void onFailure(final BleException exception) {
                        OperationActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                StartBLEListenerAfter(100);//重新开始监听
                            }
                        });
                    }

                    @Override
                    public void onInitiatedResult(boolean result) {

                    }
                });
    }//蓝牙接收返回数据

    private void bindService() {
        Intent bindIntent = new Intent(this, BluetoothService.class);
        this.bindService(bindIntent, mFhrSCon, Context.BIND_AUTO_CREATE);
        StartBLEListenerAfter(100);
        StartBLEWriterAfter(150);
    }

    private ServiceConnection mFhrSCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBluetoothService = ((BluetoothService.BluetoothBinder) service).getService();
            mBluetoothService.setConnectCallback(callback);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBluetoothService = null;
        }
    };

    private BluetoothService.Callback2 callback = new BluetoothService.Callback2() {
        @Override
        public void onDisConnected() {
            //蓝牙连接断开，开始退出该页面
            finish();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void unbindService() {
        this.unbindService(mFhrSCon);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothService != null)
            mBluetoothService.closeConnect();
        unbindService();
    }

}