package com.onandon.moca.control;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.room.Room;

import com.onandon.moca.Constant;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.model.db.AppDatabase;
import com.onandon.moca.model.db.User;
import com.onandon.moca.model.db.UserDao;
import com.onandon.moca.receiver.RAlarm;
import com.onandon.moca.utility.ObjectAndByteArrayConverter;

import java.util.Calendar;
import java.util.Locale;
import java.util.Vector;

public class CAlarm {

    private Locale locale;
    private Vector<MAlarm> mAlarms;
    private MAlarm currentAlarm;
    private int currentPosition;
    private boolean bCreate;

    private final Context mainActivity;
//    private DataAccessObject dataAccessObject;
    private UserDao dao;
    private User user;

    public CAlarm(Context mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void onCreate(Locale locale) {
        this.locale = locale;
        this.open();
        this.load();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onDestroy() {
        this.close();
    }

    //////////////////////////////////////////////////////////////////
    // file read & write
    private void open() {
        Log.d("CAlarmManager::open","");
//        this.dataAccessObject = new DataAccessObject(this.mainActivity);
        AppDatabase db = Room.databaseBuilder(this.mainActivity.getApplicationContext(),
                AppDatabase.class, Constant.dbName).allowMainThreadQueries().build();
        this.dao = db.userDao();

        this.mAlarms = null;
        this.currentAlarm = null;
        this.currentPosition = Constant.NotDefined;
        this.bCreate = false;
    }
    private void load() {
        Log.d("CAlarmManager::load","");
//        this.mAlarms = this.dataAccessObject.read();
        this.user = (User) this.dao.findById(Constant.userId);
        if(this.user==null){
            this.user = new User(Constant.userId, null);
            this.dao.insertAll(this.user);
        }
        this.mAlarms = (Vector<MAlarm>) ObjectAndByteArrayConverter.byteArrayToObject(this.user.mAlarms);
        if(this.mAlarms==null){
            this.mAlarms = new Vector<>();
        }
        Log.d("TEST", ""+mAlarms.size());
//        if(this.mAlarms == null){
//            Log.println(Log.INFO,"load","null");
//            this.mAlarms = new Vector<>();
//        }
    }
    public void store() {
        Log.d("CAlarmManager::store","");
        this.dao.updateMAlarms(this.user.uid, ObjectAndByteArrayConverter.objectToByteArray(this.mAlarms));
//        this.dataAccessObject.save(this.mAlarms);
    }
    private void close() {
        Log.d("CAlarmManager::close","");
//        this.dataAccessObject.close();
//
//        this.dataAccessObject = null;
        this.mAlarms = null;

        this.currentAlarm = null;
        this.currentPosition = Constant.NotDefined;
        this.bCreate = false;
    }

    //////////////////////////////////////////////////////////////////
    public MAlarm findByKey(String key) {
        for (MAlarm mAlarm: this.mAlarms) {
            if (key.equals(mAlarm.getKey())) {
                return mAlarm;
            }
        }
        return null;
    }
    public int getAlarmSize() {
        Log.d("CAlarmManager::Size",Integer.toString(this.mAlarms.size()));
        return this.mAlarms.size();
    }
    public MAlarm getAlarm() {
        Log.d("CAlarmManager::getAlarm", Integer.toString(currentPosition));
        return this.currentAlarm;
    }
    public MAlarm getAlarm(int postion) {
        Log.d("CAlarmManager::getAlarm", Integer.toString(postion));
        return this.mAlarms.get(postion);
    }

    // create
    public void createAlarm() {
        Log.d("CAlarmManager::create",Integer.toString(currentPosition));
        this.currentPosition = this.mAlarms.size();
        this.currentAlarm = new MAlarm();
        this.bCreate = true;
    }   // edit
    public void editAlarm(int currentPosition) {
        Log.d("CAlarmManager::editAlar",Integer.toString(currentPosition));
        this.currentPosition = currentPosition;
        this.currentAlarm = this.mAlarms.get(this.currentPosition).clone();
        this.bCreate = false;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveAlarm() {
        if (this.bCreate) {
            Log.d(currentPosition+"CAlarmManager::addAlarm", Integer.toString(this.mAlarms.size()));
//            this.mAlarms.add(this.currentAlarm);
//            this.addWithTimeSort(this.currentAlarm);
        } else {
            Log.d("currentPosition", "CAlarmManager::setAlarm"+this.currentPosition);
//            this.mAlarms.set(this.currentPosition, this.currentAlarm);
            this.mAlarms.remove(this.currentPosition);
        }
        this.addWithTimeSort(this.currentAlarm);
    }

    private void addWithTimeSort(MAlarm currentAlarm) {
        if(this.mAlarms.size()==0){this.mAlarms.add(currentAlarm); return;}
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentAlarm.getTime().getTimeInMillis());
        int timeValue = calendar.get(Calendar.MINUTE) + calendar.get(Calendar.HOUR_OF_DAY)*60;
        for(int i=0; i<this.mAlarms.size(); i++){
            calendar.setTimeInMillis(this.mAlarms.get(i).getTime().getTimeInMillis());
            int tempTimeValue = calendar.get(Calendar.MINUTE) + calendar.get(Calendar.HOUR_OF_DAY)*60;
            if(timeValue < tempTimeValue){
                if(i==0){this.mAlarms.add(0, currentAlarm);}
                else{this.mAlarms.add(i-1, currentAlarm); }
                return;
            }
        }
        this.mAlarms.add(currentAlarm);
    }

    public void removeAlarm(int position) {
        Log.d("CAlarmManager::remove", "");
        this.mAlarms.remove(position);
        this.currentPosition = Constant.NotDefined;
        this.currentAlarm = null;
        this.store();
        // remove earlier scheduler
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void scheduleAlarm() {
        MAlarm nextCloneAlarm = null;
        long minTime = Long.MAX_VALUE;
        for(MAlarm mAlarm : this.mAlarms){
            MAlarm cloneAlarm = mAlarm.schedulerNextAlarm();
            if(cloneAlarm.getAlarmTime() < minTime){
                nextCloneAlarm = cloneAlarm;
                minTime = cloneAlarm.getAlarmTime();
            }
        }
        if (nextCloneAlarm != null && nextCloneAlarm.isChecked()){
            AlarmManager alarmManager = (AlarmManager) this.mainActivity.getSystemService(Context.ALARM_SERVICE);
            Bundle bundle = new Bundle();
            bundle.putSerializable(MAlarm.class.getSimpleName(), nextCloneAlarm);
            Intent intentStartAlarm = new Intent(this.mainActivity, RAlarm.class);
            intentStartAlarm.setAction("RAlarm.START");
            intentStartAlarm.putExtra("bundle", bundle);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.mainActivity, 0, intentStartAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextCloneAlarm.getAlarmTime(), pendingIntent);
//            this.toastMAlarm(nextCloneAlarm);
        }
    }

    private void toastMAlarm(MAlarm mAlarm) {
        String toastText = null;
        try {
            toastText = String.format(this.locale,
                    "Alarm %d scheduled for %s at %s",
//                    mAlarm.getId(),
                    1234,
                    mAlarm.getName(),
                    mAlarm.getTime().format("MM/dd (E), hh:mm"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(this.mainActivity, toastText, Toast.LENGTH_LONG).show();
    }

}