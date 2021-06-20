package hzy.ubilabs.com.myapplication.BLE.fastble.conn;


public abstract class BleRssiCallback extends BleCallback {
    public abstract void onSuccess(int rssi);
}