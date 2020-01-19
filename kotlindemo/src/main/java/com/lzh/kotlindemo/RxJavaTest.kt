package com.lzh.kotlindemo

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables

/**
 *
 * @Author leizhiheng
 * @CreateDate 2019/10/28
 * @Description
 *
 */

object RxJavaTest {
    var disposable:CompositeDisposable = CompositeDisposable()

    @JvmStatic
    fun main(args: Array<String>) {
        println("helle RxJava")

    }
}
