
package hzy.ubilabs.com.myapplication.BLE.fastble.conn;
import android.bluetooth.BluetoothGattCharacteristic;


public abstract class BleCharacterCallback extends BleCallback {
    public abstract void onSuccess(BluetoothGattCharacteristic characteristic);
}