package com.example.iot;

import android.bluetooth.BluetoothSocket;

class SocketSingleton {

    private static BluetoothSocket socket;

    public static void setSocket(BluetoothSocket socketpass) {
        SocketSingleton.socket = socketpass;
    }

    public static BluetoothSocket getSocket() {
        return SocketSingleton.socket;
        //return socket;
    }
}