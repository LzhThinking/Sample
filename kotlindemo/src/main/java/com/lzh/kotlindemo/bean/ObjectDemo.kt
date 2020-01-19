package com.lzh.kotlindemo.bean

/**
 *
 * @Author leizhiheng
 * @CreateDate 2019/10/14
 * @Description 对象声明。对象声明总是以object开头，后面接对象名称。对象声明不能有构造函数，也就是说声明的对象不能有多个实例。
 *              声明的对象就是一个单例模式
 *
 */
object ObjectDemo{
    var name: String = ""
    var age: Int = 0

    fun printName() {
        println("name is $name")
    }
}