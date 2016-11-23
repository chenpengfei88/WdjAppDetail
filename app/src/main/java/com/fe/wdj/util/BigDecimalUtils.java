package com.fe.wdj.util;

import java.math.BigDecimal;

/**
 * Created by chenpengfei on 2016/11/23.
 */
public class BigDecimalUtils {

    public static float divide(int one, int two) {
        return BigDecimal.valueOf(one).divide(BigDecimal.valueOf(two), 2, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
