package hzy.ubilabs.com.myapplication.BLE.fastble.exception.hanlder;

import hzy.ubilabs.com.myapplication.BLE.fastble.exception.BlueToothNotEnableException;
import hzy.ubilabs.com.myapplication.BLE.fastble.exception.ConnectException;
import hzy.ubilabs.com.myapplication.BLE.fastble.exception.GattException;
import hzy.ubilabs.com.myapplication.BLE.fastble.exception.NotFoundDeviceException;
import hzy.ubilabs.com.myapplication.BLE.fastble.exception.OtherException;
import hzy.ubilabs.com.myapplication.BLE.fastble.exception.ScanFailedException;
import hzy.ubilabs.com.myapplication.BLE.fastble.exception.TimeoutException;
import hzy.ubilabs.com.myapplication.BLE.fastble.utils.BleLog;

public class DefaultBleExceptionHandler extends BleExceptionHandler {

    private static final String TAG = "BleExceptionHandler";

    public DefaultBleExceptionHandler() {

    }

    @Override
    protected void onConnectException(ConnectException e) {
        BleLog.e(TAG, e.getDescription());
    }

    @Override
    protected void onGattException(GattException e) {
        BleLog.e(TAG, e.getDescription());
    }

    @Override
    protected void onTimeoutException(TimeoutException e) {
        BleLog.e(TAG, e.getDescription());
    }

    @Override
    protected void onNotFoundDeviceException(NotFoundDeviceException e) {
        BleLog.e(TAG, e.getDescription());
    }

    @Override
    protected void onBlueToothNotEnableException(BlueToothNotEnableException e) {
        BleLog.e(TAG, e.getDescription());
    }

    @Override
    protected void onScanFailedException(ScanFailedException e) {
        BleLog.e(TAG, e.getDescription());
    }

    @Override
    protected void onOtherException(OtherException e) {
        BleLog.e(TAG, e.getDescription());
    }
}
