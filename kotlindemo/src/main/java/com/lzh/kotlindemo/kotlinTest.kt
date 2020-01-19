package com.lzh.kotlindemo

import com.lzh.kotlindemo.bean.*

/**
 * @Author leizhiheng
 * @CreateDate 2019/10/10
 * @Description
 */

fun Person.method(): String {
    return "$name and $preName"
}

fun printAllValues() {
    for (e in EnumDemo.values()) {
        print("$e ")
    }
}

object JavaTest {

    @JvmStatic
    fun main(args: Array<String>) {
        list()

        var person = Person("zhiheng", 20)
        println("my name is ${person.getInfo()}")
        var name = person.getMyName();

        person.name = "blue"
        println("name'length = " + person.nameLength)
        person.name = "leizhiheng"
        println("name'length = " + person.method())

        var data1 = Data("leizhiheng", 10)
        println(data1.toString())
        var data2 = Data("leizhiheng", 10)
        println(data2.toString())
        println(data1.equals(data2))

        var pair = Pair("A", 1)

        var toA:Array<Any> = arrayOf()
        copy(arrayOf(data1, data2), toA)

        var item1 = Items<Person> (Person("lzh", 10))

        var ii = object : IInterface, IInterface2 {


            override fun method1() {

            }
        }

        person.setListener(ii)


        println(EnumDemo.valueOf("RED"))
        var e: EnumDemo = EnumDemo.BLUE
        printAllValues()

        var person1 = Person("BB", 10)
        println("school = " + Person.school)
        Person.school = "JK"
        println("school = " + Person.school)

        println(Person.getSchoolName())

        var s = Student(person)
        s.eat()
        s.sleep()

        /**
         * 传入可变数量参数时需要按照以下形式： 参数名 = *arrayOf()。参数名需要与方法声明时设置的参数名一直， arrayOf前面需要加一个*号
         */
        person.setInfo(names = *arrayOf("lei1", "lei2", "lei3"))

        /**
         * 可变参数方法的调用页可以直接传入多个指定类型的参数值
         */
        person.setInfo("L", "E", "I", "Z", "H")

        person.fold("lei", "zhiheng") { ori: String, ori2: String ->
            var temp = "temp"
            var temp2 = "temp2"
            "$ori$ori2-post-$temp-$temp2"
        }
    }

    fun demo(c: Comparable<Number>) {

    }

    fun copy(from: Array<in String>, to: Array<Any>) {
        println(from.toString())
    }


    private fun list() {
//        listOf(1, 2, 3, 4).forEach foo@{
//            if (it == 2) return@foo
//            println("it = $it")
//        }
        println("finish loop")

        listOf(1, 2, 3, 4).forEach(fun(it: Int) {
            if (it == 2) return
            println("it = $it")
        })


    }

    private fun jump() {
        loop@for (i in 1..10) {
            for (j in 100..105) {
                if (j == 103) {
                    continue@loop
                }
                println("i = $i, j = $j")
            }
        }
    }

}


