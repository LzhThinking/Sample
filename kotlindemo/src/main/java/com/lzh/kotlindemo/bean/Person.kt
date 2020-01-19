package com.lzh.kotlindemo.bean

/**
 * @Author leizhiheng
 * @CreateDate 2019/10/10
 * @Description
 */

const val HOME_NAME: String = "LEI"

object MyFamily: Person("leizhiheng", 16) {
    const val PI = 3.14

    fun printPI() {
        println("PI = $PI, name is $name, age is $age")
    }

    val fl = "0"
}

open class Person: IHuman{
    override fun eat() {
        println("eat")
    }

    override fun sleep() {
        println("sleep")
    }

    var name: String = ""
        set(value) {
            field = "$HOME_NAME.$value"
        }

        get() {
            return "$field.post"
        }

    lateinit var student: Student
    var age: Int = 0
    lateinit var sex: String

    var preName: String = ""

    val nameLength = 0

    init {

        println("person's name is $name")
    }

    constructor(name: String , age: Int){
        this.name = name
        this.age = age
    }

    constructor(name: String, age: Int, sex: String, student: Student) {
        this.name = name
        this.age = age
        this.sex = sex
        this.student = student
    }
    //var name = "Lei $n"

    init {
        println("name is : $name")
    }

    fun getMyName(): String {
        return name
    }

    open fun getInfo(): String {
        return "My name is $name, my age is $age"
    }

    fun Student.getAge(): Int {
        return age;
    }

    //变量类型可以从getter中推断出来，但是不能从setter中推断出来
    val Student.clazz
        get(): String {
            return "${name.length}"
        }

    fun addPre(value: String): Unit{
        print("pre$value")
    }

    lateinit var iInterface: IInterface

    fun setListener(i: IInterface) {
        iInterface = i
    }

    fun setStudentO(s: Student) {
        student = s
    }

    fun setInfo(name: String = "BLUE", age: Int = 10) {
        print("set info name is $name, age is $age")
    }

    fun setInfo(name: String = "BLUE") {
        print("set info name is $name, age is $age")
    }

    /**
     * 可变数量参数使用需要用vararg关键词修饰。并且参数名需要在方法调用时使用
     */
    fun setInfo(vararg names: String) {
        for (i in names.indices) {
            println("name $i is ${names[i]}")
        }
        for (s in names) {
            println("name: $s ")
        }
    }

    /**
     * 用户函数参数。函数参数传入的是一个函数体
     */
    fun fold(ori: String, ori2: String, method: (String, String) -> String) {
        var result:String = method(ori, ori2)
        println("result is $result")
    }


    /**
     * 返回一个函数，这个函数的参数时s，返回一个String类型的值，函数体就是大括号中的代码块。在函数体中通过it关键字使用函数。
     */


    companion object PsersonObject {
        var school: String = "yizhong"
        fun getSchoolName() = school
    }
}
