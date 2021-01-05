package com.onandon.moca.technical;

import android.content.Context;

import com.onandon.moca.Constant;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class DataAccessObject {

    private final Context applicationContext;
    public DataAccessObject(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Saves a serializable object.
     *
     * @param objectToSave The object to save.
     * @param <T> The type of the object.
     */
    public <T extends Serializable> void save(T objectToSave) {
        try {
            FileOutputStream fileOutputStream =
                    this.applicationContext.openFileOutput(Constant.Alarm.DefaulFileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(objectToSave);
            objectOutputStream.writeObject(null);

            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a serializable object.
     *
     * @param <T> The object type.
     *
     * @return the serializable object.
     */
    public <T extends Serializable> T read() {
        T objectToReturn = null;
        try {
            FileInputStream fileInputStream =
                    this.applicationContext.openFileInput(Constant.Alarm.DefaulFileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            objectToReturn = (T) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return objectToReturn;
    }

    public void close() {
    }
}
