package com.onandon.moca.technical;

import android.content.Context;
import android.util.Log;

import com.onandon.moca.Constant;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class DataAccessObject implements Serializable {

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
    public <T extends Serializable> void save(String fileName, T objectToSave) {
        try {
            Log.d("Test10" ,"save "+applicationContext);
            FileOutputStream fileOutputStream =
                    this.applicationContext.openFileOutput(fileName, Context.MODE_PRIVATE);
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
    public <T extends Serializable> T read(String fileName) {
        T objectToReturn = null;
        try {
            FileInputStream fileInputStream =
                    this.applicationContext.openFileInput(fileName);
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
