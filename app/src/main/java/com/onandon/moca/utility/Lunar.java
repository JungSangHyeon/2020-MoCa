package com.onandon.moca.utility;

    import java.util.Calendar;
import java.util.TimeZone;

public class Lunar
{
    // 2011년에서 2030년까지 지원
    final static private int fromYear = 2011;
    final static private int toYear = 2030;



    // start day : 2011-02-03에서 1970-01-01을 뺀 일수 : Calendar.getTimeInMillis()
    // 양력 2011-02-03 = 음력 2011-01-01
    final static private int startDay = 15008;

    // 음력 달 기초 정보
    // 1 = 평달 작은 달(29일)
    // 2 = 평달 큰 달(30일)
    // 3 = 윤달있는 달(29일+29일)
    // 4 = 윤달있는 달(29일+30일)
    // 5 = 윤달있는 달(30일+29일)
    // 6 = 윤달있는 달(30일+30일)
    final static private byte[][] baseInfo =
            {
                    { 2, 1, 2, 2, 1, 2, 1, 1, 2, 1, 2, 1 }, // 2011
                    { 2, 1, 6, 2, 1, 2, 1, 1, 2, 1, 2, 1 },
                    { 2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2 },
                    { 1, 2, 1, 2, 1, 2, 1, 2, 5, 2, 1, 2 },
                    { 1, 2, 1, 1, 2, 1, 2, 2, 2, 1, 2, 1 },
                    { 2, 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 2 },
                    { 1, 2, 1, 2, 3, 2, 1, 2, 1, 2, 2, 2 },
                    { 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2 },
                    { 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2 },
                    { 2, 1, 2, 5, 2, 1, 1, 2, 1, 2, 1, 2 },
                    { 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1 }, // 2021
                    { 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 1, 2 },
                    { 1, 5, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2 },
                    { 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 1 },
                    { 2, 1, 2, 1, 1, 5, 2, 1, 2, 2, 2, 1 },
                    { 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2 },
                    { 1, 2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 2 },
                    { 1, 2, 2, 1, 5, 1, 2, 1, 1, 2, 2, 1 },
                    { 2, 2, 1, 2, 2, 1, 1, 2, 1, 1, 2, 2 },
                    { 1, 2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1 }
            };

    // [in] y, m, d : 양력 년,월,일
    // [반환값] 음력 변환 결과, [0]=년, [1]=월, [2]=일, [3]=평달이면 0 윤달이면 1
    // [반환값] null == 변환할 범위를 벗어남
    public static int[] get(int y, int m, int d)
    {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.set(y, m - 1, d);



        d = (int)(c.getTimeInMillis() / (24*60*60*1000)) - startDay;
        if (d < 0 || y > toYear) return null;



        y = m = 0;



        int i, j, n;
        int leap = 0; // 0=평달, 1=윤달



        while (true)
        {
            for (i = j = 0; i < 12; i++)
            {
                switch (baseInfo[y][i])
                {
                    case 1: j += 29; break;
                    case 2: j += 30; break;
                    case 3: j += 29 + 29; break;
                    case 4: // j += 29 + 30; break;
                    case 5: j += 30 + 29; break;
                    case 6: j += 30 + 30; break;
                }
            }

            if (d >= j)
            {
                d -= j;
                y++;
            }
            else break;
        }



        while (true)
        {
            if (baseInfo[y][m] <= 2)
            {
                n = baseInfo[y][m] + 28;
                if (d >= n)
                {
                    d -= n;
                    m++;
                }
                else break;
            }
            else
            {
                switch (baseInfo[y][m])
                {
                    case 3: i = 29; j = 29; break;
                    case 4: i = 29; j = 30; break;
                    case 5: i = 30; j = 29; break;
                    case 6: i = 30; j = 30; break;
                }

                if (d >= i)
                {
                    d -= i;
                    if (d >= j)
                    {
                        d -= j;
                        m++;
                    }
                    else
                    {
                        leap = 1;
                        break;
                    }
                }
                else break;
            }
        }

        int[] l = new int[4];

        l[0] = y + fromYear;
        l[1] = m + 1;
        l[2] = d + 1;
        l[3] = leap;



        return l;
    }
}

