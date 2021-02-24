package com.onandon.moca.onAndOnAsset.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class UObjectAndByteArrayConverter {

	public static byte[] objectToByteArray(Object object){
		byte[] result = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(object);
			out.flush();
			result = bos.toByteArray();
			bos.close();
		} catch (IOException e) { e.printStackTrace(); }
		return result;
	}

	public static Object byteArrayToObject(byte[] bytes) {
		Object result = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInput in = new ObjectInputStream(bis);
			result = in.readObject();
			if (in != null) { in.close(); }
		} catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
		return result;
	}
}
