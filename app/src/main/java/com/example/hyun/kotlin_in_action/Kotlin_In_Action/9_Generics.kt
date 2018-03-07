package com.example.hyun.kotlin_in_action.Kotlin_In_Action


//  76> 제네릭 타입 파라미터
//  - 제너릭스를 사용하면 타입 파라미터를 받는 타입을 정의할 수 있다
//  -> 제너릭 타입의 인스턴스를 만드려면 타입 파라미터를 구체적인 타입 인자로 치환해야 한다
//  - ex>
val author1 = listOf<String>("Dimitry", "Svetlana")
val author2 = listOf("Dimitry", "Svetlana") // 코틀린은 타입인자도 추론 할 수 있으므로 생략 가능

//  - 빈 리스트를 만들어야 한다면 타입 인자를 추론할 근거가 없기 때문에 직접 타입 인자를 명시해야한다
//  - ex>
val readers1: MutableList<String> = mutableListOf()
val readers2 = mutableListOf<String>()


//  - 어떤 특정 타입을 저장하는 리스트뿐만 아니라 모든 리스트를 다룰 수 있는 함수를 원할 때 제네릭 함수를 사용한다
//  - 제네릭 함수를 호출할 때는 반드시 구체적 타입으로 타입 인자를 넘겨야 한다
//  - ex>
fun <T> List<T>.slice(indices: IntRange): List<T> {}

//  <T>->타입 파라미터 선언
//          <T>-> 타입 파라미터가 수신 객체와 반환 타입에 쓰인다
//                                            <T>-> 타입 파라미터가 수신 객체와 반환 타입에 쓰인다
//  - ex>
fun <T> List<T>.filter2(predicate: (T) -> Boolean): List<T>

fun main300() {
    readers2.filter2 { it !in author2 }
    // 람다 파라미터에 대해 자동으로 만들어진 변수 it의 타입은 T라는 제네릭 타입이다 ((T)->Boolean에서 온 타입)
}

//  - 제네릭 함수를 정의할 때와 마찬가지 방법으로 제네릭 확장 프로퍼티를 선언할 수 있다
//  - ex>
val <T> List<T>.penultimate: T // 모든 리스트 타입에 이 제네릭 확장 프로퍼티를 사용할 수 있다
    get() = this[size - 2]

fun main301() {
    listOf(1, 2, 3, 4).penultimate // -> 3
}

//  - 확장프로퍼티만 제네릭하게 만들 수 있다
//  -> 일반 (확장이 아닌)프로퍼티는 타입 파라미터를 가질 수 없다. 클래스 프로퍼티에 여러 타입의 값을 저장할 수는 없으므로
//     제네릭한 일반 프로퍼티는 말이 되지 않는다 일반 프로퍼티를 제네릭하게 정의하면 컴파일러가 다음과 같은 오류를 표시한다
//  - ex>
val <T> x: T = TODO() // -> ERROR: type parameter of a property mush be used in its receiver type

//  제너릭 클래스 선언
//  - 자바와 마찬가지로 타입 파라미터를 넣은 꺾쇠 기호를 클래스 이름 뒤에 붙이면 클래스를 제너릭하게 만들 수 있다
//  - ex>
interface List<T> { // List 인터페이스에 T라는 타입 파라미터를 정의한다
    operator fun get(index: Int): T  // 인터페이스 안에서 T를 일반 타입처럼 사용할 수 있다
}

//  - 제네릭 클래스를 확장 하는 클래스를 정의 하려면 기반 타입의 제네릭 파라미터에 대해 타입 인자를 지정해야 한다
//  - ex> 구체적인 타입 인자로 String 을 지정해 List를 구현한다
class StringList : List<String> {
    override fun get(index: Int): String {
        TODO("not implemented")
    }
    // -> String 타입의 원소만 포함한다. 따라서 String을 기반 타입의 타입 인자로 지정한다
    // -> 하위 클래스에서 상위 클래스에 정의된 함수를 오버라이드하거나 사용하려면 타입 인자 T를 구체적 타입 String으로 치환해야한다
}

//  - ex> ArrayList의 제네릭 타입 파라미터 T를 List의 타입 인자로 넘긴다
class ArrayList<T> : List<T> {
    override fun get(index: Int): T {
        TODO("not implemented")
    }
    // -> 자신만의 타입 파라미터 T를 정의 하면서 그 T를 기반 클래스의 타입 인자로 사용한다
    // -> 여기에서 ArrayList<T>의 T와 List<T>의 T는 같지 않다 (T대신 다른 이름을 사용해도 무방하다)
}

//  타입 파라미터 제약 (type parameter constraint)
//  - 타입 파라미터 제약은 클래스나 함수에 사용할 수 있는 타입 인자를 제한하는 기능이다.
//  - 즉, List<Int>, List<Double>은 sum 함수를 적요 할 수 있지만 List<String>에는 불가능 하다 -> 제약이 필요함
//  - 어떤 타입을 제네릭 타입의 타입 파라미터에 대한 상한(upper bound)로 지정하면 그 제네릭 타입을 인스턴스화할 때
//    사용하는 타입 인자는 반드시 그 상한 타입이거나 그 상한 타입의 하위 타입이어야 한다
//  - 제약을 가하려면 ':'을 표시하고 그 뒤에 상한 타입을 적으면 된다
//  - ex>
fun <T : Number> List<T>.sum(): T // -> Number를 상속 받은 클래스들만으로 제약을 하겠다는 뜻

fun main302() {
    listOf(1, 2, 3).sum()
}

//  - 타입 파라미터 T에 대한 상한을 정하고 나면 T 타입의 값을 그 상한 타입의 값으로 취급 할 수 있다
//  - ex>
fun <T : Number> oneHalf(value: T): Double { // Number를 타입 파라미터 상한으로 정한다
    return value.toDouble() / 2.0  // Number 클래스에 정의된 메소드를 호출한다
}

fun main303() {
    oneHalf(3) // -> 1.5
}

//  - ex>
fun <T : Comparable<T>> max(first: T, second: T): T {
    return if (first > second) first else second
}

fun main304() {
    max("kotlin", "java") // -> kotlin
    max("kotlin" 42)// -> max를 비교할 수 없는 값 사이에 호출하면 컴파일 오류가 난다
}

//  - ex> 타입 파라미터에 여러 제약을 가하기
fun <T> ensureTrailingPeriod(seq: T)
        where T : CharSequence, T : Appendable { // 타입 파라미터 제약 목록
    if (!seq.endsWith('.')) {
        seq.append('.') // Appendable의 메소드를 호출 한다
    }
    // 이 예제는 타입 인자가 CharSequence 와 Appendable 인터페이스를 반드시 구현해야 한다
}

fun main305() {
    val helloWorld = StringBuilder("Hello World")
    ensureTrailingPeriod(helloWorld) // -> Hello World.
}

//  - 타입 파라미터를 null 이 될 수 없는 타입으로 한정
//  - 아무런 상한을 정하지 않은 타입 파라미터는 결과적으로 Any?를 상한으로 정한 파라미터와 같다
//  - ex>
class Processor<T> { // 디폴트 Any? 로 타입 파라미터가 설정되어 null 이 될 수 있다
    fun process(value: T) {
        value?.hashCode() // value 는 null이 될 수 있다 따라서 안전한 호출을 사용 해야 한다
    }
}

fun main306() {
    val nullableStringProcessor = Processor<String?>()
    nullableStringProcessor.process(null) // 이 코드는 잘 컴파일 되며 "null"이 "value"인자로 지정된다
}

//  - 항상 null이 될 수 없는 타입만 타입 인자로 받게 만들려면 타입 파라미터에 제약을 가해야 한다
//  - ex>
class Processor1<T : Any> { // Any로 제약을 걸어서 null이 될수 없는 타입 파라미터를 만든다
    fun process(value: T) {
        value.hashCode()
    }
}

fun main307() {
    val nullableStringProcessor1 = Processor1<String?>() // -> 컴파일 에러가 발생한다
}

//  77> 실행시 제네릭스의 동작 : 소거된 타입 파라미터와 실체화된 타입 파라미터
//  - JVM의 제네릭스는 보통 타입 소거(type erasure)를 사용해 구현된다
//  -> 실행 시점에 제네릭 클래스의 인스턴스에 타입 인자 정보가 들어있지 않다듣 뜻이다
//  - 코틀린에서는 함수를 inline으로 만들면 타입 인자가 지워지지 않게 할 수 있다 (코틀린에서는 이를 reify라고 한다)

//  - 자바와 마찬가지로 코틀린 제네릭 타입 인자 정보는 런타임에 지워진다
//  -> 이는 제네릭 클래스 인스턴스가 그 인스턴스를 생성할 때 쓰인 타입 인자에 대한 정보를 유지 하지 않는 다는 뜻이다
//  -> 즉, List<String> 객체를 만들고 그 안에 문자열을 여럿 넣더라도 실행 시점에서는 그 객체를 오직 List로만 볼 수 있다
//     그 List객체가 어떤 타입의 원소를 저장하는지 실행 시점에서는 알 수 없다

//  - 타입 인자를 따로 저장하지 않기 때문에 실행 시점에 타입 인자를 검사 할 수 없다 (타입소거로 인해 생기는 한계)
//  -
































