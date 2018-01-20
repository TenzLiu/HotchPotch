package com.tenz.hotchpotch.util;


import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * User: Tenz Liu
 * Date: 2017-08-21
 * Time: 12-10
 * Description: Log日志打印工具类
 */

public class LogUtil {

    public static boolean debug = false;

    /**
     * 初始化
     * @param debug
     */
    public static void init(boolean debug){
        LogUtil.debug = debug;
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    /**
     * 打印日志(Verbose)
     *
     * @param msg 内容
     */
    public static void v(String msg) {
        if (debug) {
            Logger.v(msg);
        }
    }

    /**
     * 打印日志(Debug)
     *
     * @param msg 内容
     */
    public static void d(String msg) {
        if (debug) {
            Logger.d(msg);
        }
    }


    /**
     * 打印日志(Info)
     *
     * @param msg 内容
     */
    public static void i(String msg) {
        if (debug) {
            Logger.i(msg);
        }
    }

    /**
     * 打印日志(Warm)
     *
     * @param msg 内容
     */
    public static void w(String msg) {
        if (debug) {
            Logger.w(msg);
        }
    }

    /**
     * 打印日志(wtf)
     *
     * @param msg 内容
     */
    public static void wtf(String msg) {
        if (debug) {
            Logger.wtf(msg);
        }
    }


    /**
     * 打印日志(Error)
     *
     * @param msg 内容
     */
    public static void e(String msg) {
        if (debug) {
            Logger.e(msg);
        }
    }

    /**
     * 打印日志(Error)
     *
     * @param throwable
     */
    public static void e(Throwable throwable) {
        if (debug) {
            Logger.e(throwable, "");
        }
    }


    /**
     * 打印日志(Erro)
     *
     * @param msg       内容
     * @param throwable
     */
    public static void e(String msg, Throwable throwable) {
        if (debug) {
            Logger.e(throwable, msg);
        }
    }

    /**
     * 打印日志(json)
     *
     * @param msg 内容
     */
    public static void json(String msg) {
        if (debug) {
            Logger.json(msg);
        }
    }

    /**
     * 打印日志(Error)
     *
     * @param object 对象
     */
    public static void d(Object object) {
        if (debug) {
            //Logger.d(JsonUtil.object2Json(object));
        }
    }


}
