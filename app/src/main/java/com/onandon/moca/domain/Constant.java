package com.onandon.moca.domain;

import com.onandon.moca.R;

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
        public enum EAlarmPower {
            eLevel1("Level 1", 20),
            eLevel2("Level 2", 60),
            eLevel3("Level 3", 100);

            private int power;
            private String levelName;
            EAlarmPower(String levelName, int power){ this.power=power; this.levelName=levelName;}
            public int getPower() { return power; }
            public String getLevelName() { return levelName; }
        }
        public enum EAlarmMode {
            eNoSound("무음", R.id.alarm_setting_mode_radio_nosound),
            eSound("표준", R.id.alarm_setting_mode_radio_sound),
            eCrazy("광란", R.id.alarm_setting_mode_radio_crazy),
            eUserDefined("사용자 설정", R.id.alarm_setting_mode_radio_userdefined);

            private String levelName;
            private int radioButtonId;
            EAlarmMode(String levelName, int radioButtonId){ this.levelName=levelName; this.radioButtonId=radioButtonId;}
            public String getModeName() { return levelName; }
            public int getRadioButtonId() { return radioButtonId; }
        }


        // Vibration Manager
        public static final int defaultVibrationPattern = 0;
        public static final long[] NoEffectVibrationPattern = {500, 2500, 500, 2500};

        public enum EVibrationPattern {
            pattern_1(R.string.alarm_setting_vibration_pattern1_name, new int[][] {{100,0}, {300,255}, {50,150}, {200,255}, {100,100}, {300,255}, {50,150}, {200,255}, {100,100},}),
            pattern_2(R.string.alarm_setting_vibration_pattern2_name, new int[][] {{100,0}, {100,255}, {100,0}, {100,255}, {100,0}, {100,255}, {100,0}, {100,255},}),
            ;
            private final int nameId;
            private final int[][] pattern;
            EVibrationPattern(int nameId, int[][] pattern) {
                this.nameId=nameId;
                this.pattern=pattern;
            }
            public int getNameId() { return nameId; }
            public int[][] getPattern() { return pattern; }
        }

        public static int waitTimePerCount = 2000;
        // Flash Manager
        public static int FlashSwitchCount = 1;
        // Screen Brightness
        public static int ScreenSwitchCount = 10;
}
