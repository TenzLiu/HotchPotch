package com.tenz.hotchpotch.rx;


/**
 * Author: TenzLiu
 * Date: 2018-01-20 23:21
 * Description: RxBus
 * Subject同时充当了Observer和Observable的角色，Subject是非线程安全的，
 * 要避免该问题，需要将 Subject转换为一个 SerializedSubject ，
 * 上述RxBus类中把线程非安全的PublishSubject包装成线程安全的Subject。
 * PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
 * ofType操作符只发射指定类型的数据，其内部就是filter+cast
 */

public class RxBus {



}
