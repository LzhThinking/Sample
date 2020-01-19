package com.lzh.kotlindemo

import com.lzh.kotlindemo.bean.Person

/**
 *
 * @Author leizhiheng
 * @CreateDate 2019/10/14
 * @Description
 *
 */

class Items<out T : Person> (p: T) {

    fun getInfo(): String {
        return toString()
    }
}