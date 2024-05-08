package com.buyi.spzx.utils;

import com.buyi.spzx.model.entity.system.SysUser;

public class AuthContextUtil {

    private static final ThreadLocal<SysUser> threadLocal = new ThreadLocal<>();

    /**
     * 给当前线程存放用户信息
     *
     * @param sysUser 用户信息
     */
    public static void set(SysUser sysUser) {
        threadLocal.set(sysUser);
    }

    /**
     * 定义获取数据的方法
     *
     * @return 用户信息
     */
    public static SysUser get() {
        return threadLocal.get();
    }

    /**
     * 删除数据的方法
     */
    public static void remove() {
        threadLocal.remove();
    }
}
