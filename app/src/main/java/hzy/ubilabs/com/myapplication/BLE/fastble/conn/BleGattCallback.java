
package hzy.ubilabs.com.myapplication.BLE.fastble.conn;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;

import hzy.ubilabs.com.myapplication.BLE.fastble.data.ScanResult;
import hzy.ubilabs.com.myapplication.BLE.fastble.exception.BleException;


public abstract class BleGattCallback extends BluetoothGattCallback {

    public void onFoundDevice(ScanResult scanResult) {
    }

    public void onConnecting(BluetoothGatt gatt, int status) {
    }

    public abstract void onConnectError(BleException exception);

    public abstract void onConnectSuccess(BluetoothGatt gatt, int status);

    public abstract void onDisConnected(BluetoothGatt gatt, int status, BleException exception);

}