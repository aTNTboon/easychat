package com.example.demo.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

public class IdComponent {
    private static int workerId=0;
    private static int datacenterId=1;

    public static Long generateMeetingNo() {
        Snowflake snowflake = IdUtil.getSnowflake(workerId, datacenterId);
        long id = snowflake.nextId();  // 始终返回正数
        return id;
    }

}
