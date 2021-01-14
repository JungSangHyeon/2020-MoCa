package com.onandon.moca;

public class Constant {
    /**
     *  Technical Constant
     */
    // Common
    public final static int NotDefined = -1;

    /**
     *  Domain Constant
     */
        // DB
        public static final String DefaultFileName = "MAlarms";

        /**
         *  Time
         */

        // Alarm
        public static final int AlarmRingMinute = 5;
        // Snooze
        public static int SnoozeInterval = 5; // min
        // Repeat
        public static class ReAlarm {
            public static final int[] interval = {1, 3, 5, 8, 10};
            public static final int[] count = {1, 2, 3, 4, 5};
        }
        /**
         *  Media
         */
        // Power
        public static int DefaultLevel = 75;

        // Vibration Manager
        public static final int defaultVibrationPattern = 0;
        public static final long[] NoEffectVibrationPattern = {500, 2500, 500, 2500};

        public enum EVibrationPattern{
            pattern_1(R.string.alarm_setting_vibration_pattern1_name, new long[]{300, 50,200,100,300, 50,200,100}, new int[]{255,150,255,100,255,150,255,100}),
            pattern_2(R.string.alarm_setting_vibration_pattern2_name, new long[]{100, 100, 100, 100, 100, 100, 100, 100}, new int[]{0, 255, 0, 255, 0, 255, 0, 255}),
            pattern_3(R.string.alarm_setting_vibration_pattern3_name, new long[]{100,50,100,500,100,50,100,500}, new int[]{255,50,255,50,255,50,255,50}),
            ;
            private int nameId;
            private long[] duration;
            private int[] amplitude;
            EVibrationPattern(int nameId, long[] duration, int[] amplitude) {
                this.nameId=nameId;
                this.duration=duration;
                this.amplitude=amplitude;
            }
            public int getNameId() { return nameId; }
            public long[] getDuration() { return duration; }
            public int[] getAmplitude() { return amplitude; }
        }

        public static int waitTimePerCount = 100;
        // Flash Manager
        public static int FlashSwitchCount = 1;
        // Screen Brightness
        public static int ScreenSwitchCount = 10;
}
