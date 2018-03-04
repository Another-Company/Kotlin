package com.example.hyun.kotlin_in_action.Kotlin_In_Action

//  74> 고차함수
//  - 다른 함수를 인자로 받거나 함수를 반환하는 함수이다
//  - 고차 함수를 정의 하기 위해서는 함수 타입(function type)을 알아야 한다

//  함수 타입 (function type)
//  - 람다를 로컬 변수에 대입 하는 방법
//  - ex> 함수를 만드는 방법 (타입 추론을 이용하여 변수 타입을 지정 하지 않는 방법)
val summ = { x: Int, y: Int -> x + y }
val actionn = { println(42) }

//  - ex> 함수를 만드는 방법 (변수에 타입선언을 지정한 방법)
val summ1: (Int, Int) -> Int = { x, y -> x + y } // -> Int 두개를 받아서 Int를 리턴 하는 함수
val actionn1: () -> Unit = { println(42) } // -> 아무 인자도 받지 않고 아무 값도 반환하지 않는 함수

var canRetrunNull: (Int, Int) -> Int? = { x, y -> null } // -> null을 반환 할수 있는 함수
var funOrNull: ((Int, Int) -> Int)? = null
// -> null을 반환 할수 있는 함수 (괄호? 필수) -> 괄호를 빼면 함수 자체가 null 이된다
//  -> 즉 (parametor type, parametor type,...) -> (return type)


//  - ex> 함수 파라미터에 이름을 지정할 수 있다
fun performRequest(url: String, callback: (code: Int, content: String) -> Unit) {}

fun main200() {
    val url = "http://kotl.in"
    performRequest(url) { code, content -> } // api에서주는 대로 사용
    performRequest(url) { code, page -> } // page로 바꿔서 사용
}

//  고차함수 정의하기
//  -ex> 함수를 인자로 받는 함수 만드는 방법(고차함수)
fun twoAndThree(operation: (Int, Int) -> Int) {
    val result = operation(2, 3)
    println("The result is $result")
}

fun main201() {
    twoAndThree { a, b -> a + b } // -> The result is 5
    twoAndThree { a, b -> a * b } // -> The result is 6
    // -> 함수를 인자로 받는 함수를 호출 하기 위해서는 {} 괄호를 사용하고 인자를 ,로 구분하여 넣어준다
}

fun String.filter(predicate: (Char) -> Boolean): String {
    val sb = StringBuilder()
    for (index in 0 until length) {
        val element = get(index)
        if (predicate(element)) sb.append(element)
    }
    return sb.toString()
}

fun main202() {
    "ab1c".filter { it in 'a'..'z' } // -> abc
}

//  디폴드 값을 지정한 함수 타입 파라미터나 null이 될 수 있는 함수 타입 파라미터
//  - 파라미터를 함수 타입으로 선언할 때도 디폴트 값을 정할 수 있다
//  - ex> 디폴트 미적용
fun <T> Collection<T>.joinToString1(
        separator: String = ",",
        prefix: String = "",
        postfix: String = ""
): String {
    val result = StringBuilder(prefix)
    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return readLine().toString()
}

//  - ex> 디폴트 적용
fun <T> Collection<T>.joinToString2(
        separator: String = ",",
        prefix: String = "",
        postfix: String = "",
        transform: (T) -> String = { it.toString() }// 함수 타입 파라미터 디폴트 함수 적
): String {
    val result = StringBuilder(prefix)
    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(transform(element))
    }
    result.append(postfix)
    return result.toString()
}

fun main203() {
    val letters = listOf("Alpha", "Beta")
    letters.joinToString2() // -> Alpha, Beta
    letters.joinToString2(transform = { it.toLowerCase() })  // -> alpha, beta
    letters.joinToString2 { it.toLowerCase() }  // -> alpha, beta
    letters.joinToString2(separator = "! ", postfix = "! ", transform = { it.toUpperCase() })
    // -> ALPHA!, BETA!
}

//  - ex> Null 이 될 수 있느 ㄴ함수 타입 파라미터 사용하기
fun <T> Collection<T>.joinToString3(
        separator: String = ",",
        prefix: String = "",
        postfix: String = "",
        transform: ((T) -> String)? = null // Null이 될 수 있는 타입으로 함수를 받으면 직접 호출 할 수 없다
): String {
    val result = StringBuilder(prefix)
    for ((index, elemnet) in this.withIndex()) {
        if (index > 0) result.append(separator)
        val str = transform?.invoke(elemnet) ?: elemnet.toString()
        // Null이 될수 있는 함수는 ?.invoke()로 호출 해야 한다
        result.append(str)
    }
    result.append(postfix)
    return result.toString()
}

//  함수를 함수에서 반환
//  - 함수가 함수를 인자로 받아야 할 필요가 있는 경우 보다 적지만 필요함
//  - 필요 예시 : 사용자가 선택한 배송 수단에 따라 배송비를 계산 하는 방법이 다를 경우
//  - ex>
enum class Delivery { STANDARD, EXPEDITED }

class Order(val itemCount: Int)

fun getShippingCostCalculator(
        delivery: Delivery): (Order) -> Double { // delivery를 받고 order를 Double 만드는 함수를 리턴하겠다는
    if (delivery == Delivery.EXPEDITED) {
        return { order -> 6 + 2.1 * order.itemCount } // 함수 반환은 {}사용
    }
    return { order -> 1.2 * order.itemCount }
}

fun main204() {
    val calulator = getShippingCostCalculator(Delivery.EXPEDITED)
    calulator(Order(3)) // -> 12.3
}

//  - 필요 예시 : GUI 연락처 관리 앱을 만드는데 UI의 상태에 따라 어떤 연락처 정보를 표시할지 결정해야 할 필요가 있다
//              사용자가 UI의 입력 창에 입력한 문자열과 매치되는 연락처만 화면에 표시하되 설정에 따라 전화번호 정보가
//              없는 연락처를 제외시킬 수도 있고 포함 시킬 수도 있어야 한다
//  - ex>
data class Person00(
        val firstName: String,
        val lastName: String,
        val phoneNumber: String?
)

class ContactListFilters {
    var prefix: String = ""
    var onlyWithPhoneNumber: Boolean = false

    fun getPredicate(): (Person00) -> Boolean {
        val startWithPrefix = { p: Person00 ->
            p.firstName.startsWith(prefix) || p.lastName.startsWith(prefix)
        }
        if (!onlyWithPhoneNumber) {
            return startWithPrefix
        }
        return { startWithPrefix(it) && it.phoneNumber != null }
    }
}

fun main205() {
    val contacts = listOf(Person00("Dmitry", "Jemerov", "123-456"),
            Person00("Svetlana", "Isakova", null))
    val contactListFilters = ContactListFilters()
    with(contactListFilters) {
        prefix = "Dm"
        onlyWithPhoneNumber = true
    }

    contacts.filter(contactListFilters.getPredicate())
    // -> Person(firstName = Dmitry, lastName = Jemerov, phoneNumber = 123-4567
}










































