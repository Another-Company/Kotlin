package com.example.hyun.kotlin_in_action.Inflearn

/**
 * Created by hyun on 2018. 1. 24..
 */

class Userr(age: Int) {
    var age: Int = age
}

// * 람다식으로 함수 만들기
// 1> 첫번째 방법 (함수의 개요가 암시적으로 들어나는 방법)
val summ1 = { x: Int, y: Int -> x + y }

// 2> 두번째 방법 (함수의 개요가 명시적으로 들어나는 방법)
val summ2: (Int, Int) -> Int = { x, y -> x + y }

val abccc2: (Userr) -> Boolean = { a -> if (a.age > 10) true else false }


// * 함수를 인자로 받는 함수 만드는 방법
fun _filter(list: ArrayList<Userr>, predict: (a: Userr) -> Boolean): ArrayList<Userr> {
    var new_list = ArrayList<Userr>()
    list.forEach {
        if (predict(it) == true) {
            new_list.add(it)
        }
    }

    for (i in 0..list.size - 1) {
        print(list.get(i).age)
        if (predict(list.get(i)) == true) {
            new_list.add(list.get(i))
        }
    }

    return new_list
}


// * 함수를 함수를 인자로 받는 함수에 전달 하는 방법
fun main1122() {
    var test_list = ArrayList<Userr>()
    test_list.add(Userr(10))
    test_list.add(Userr(15))

    // 람다식으로 함수 만들기중 첫번째 방식으로 함수를 만들어 함수의 인자로 전달한다
    _filter(test_list, { a: Userr -> if (a.age > 10) true else false })
}


