package com.lzh.kotlindemo.bean

import com.lzh.kotlindemo.bean.ObjectDemo.age

/**
 *
 * @Author leizhiheng
 * @CreateDate 2019/10/10
 * @Description
 *
 */

class Student(val p: IHuman) : IHuman by p {

    override fun eat() {
        println("student eat")
    }

    override fun sleep() {
        println("student sleep")
    }
}