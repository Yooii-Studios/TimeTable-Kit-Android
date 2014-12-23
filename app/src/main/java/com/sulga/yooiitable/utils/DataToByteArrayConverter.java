package com.sulga.yooiitable.utils;

import java.io.*;

import com.sulga.yooiitable.data.*;

public class DataToByteArrayConverter {

	public static byte[] objectToByteArray(Serializable obj){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		byte[] result = null;
		try {
		  out = new ObjectOutputStream(bos);   
		  out.writeObject(obj);
		  result = bos.toByteArray();
		}catch(IOException e){
			e.printStackTrace();
		}finally {
		  try {
		    if (out != null) {
		      out.close();
		    }
		  } catch (IOException ex) {
		    // ignore close exception
		  }
		  try {
		    bos.close();
		  } catch (IOException ex) {
		    // ignore close exception
		  }
		}
		return result;
	}
	
	public static Timetable byteArrayToTimetableData(byte[] input){
		if(input == null){
			return null;
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		ObjectInput in = null;
		Timetable result = null;
		try {
		  in = new ObjectInputStream(bis);
		  result = (Timetable) in.readObject(); 
		} catch(IOException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
		  try {
		    bis.close();
		  } catch (IOException ex) {
		    // ignore close exception
		  }
		  try {
		    if (in != null) {
		      in.close();
		    }
		  } catch (IOException ex) {
		    // ignore close exception
		  }
		}
		return result;
	}
}
