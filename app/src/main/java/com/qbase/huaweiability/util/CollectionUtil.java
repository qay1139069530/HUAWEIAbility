package com.qbase.huaweiability.util;

import java.util.Collection;

/**
 * Created by qay
 */
public class CollectionUtil {
    /**
     * 判断集合是否为空
     */
    public static boolean isEmpty(Collection<? extends Object> collection) {
        boolean isEmpty = true;
        if (collection != null && !collection.isEmpty()) {
            isEmpty = false;
        }
        return isEmpty;
    }
}
