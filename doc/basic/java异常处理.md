### 参考链接

- [Java中的异常和处理详解](https://www.cnblogs.com/lulipro/p/7504267.html)

- [Java的异常处理机制](https://blog.csdn.net/qq_15349687/article/details/82811581)
- [JAVA 异常分类与理解](https://www.cnblogs.com/itcui/p/6400499.html)

### Java异常处理机制

Java标准库内建了一些通用的异常 这些类以Throwable为顶层父类  Throwable又派生了Error和Exception两个子类

- error 表示应用程序本身无法克服和恢复的一种严重问题   大多数错误与代码编写者执行的操作无关  而表示代码运行时JVM出现的问题   例如java虚拟机运行错误   OutOfMemoryError  这些异常发生时，Java虚拟机（JVM）一般会选择线程终止。
- exception 表示程序能够克服和恢复的问题  分为运行时异常和非运行时异常
  - **运行时异常** : 都是RuntimeException类及其子类异常，如NullPointerException(空指针异常)、IndexOutOfBoundsException(下标越界异常)等，这些异常是不检查异常，程序中可以选择捕获处理，也可以不处理。这些异常一般是由程序逻辑错误引起的，程序应该从逻辑角度尽可能避免这类异常的发生。
  - **非运行时异常** : 是RuntimeException以外的异常，类型上都属于Exception类及其子类。从程序语法角度讲是必须进行处理的异常，如果不处理，程序就不能编译通过。如IOException、SQLException等以及用户自定义的Exception异常，一般情况下不自定义检查异常。

总体上将异常(error和exception)分为 非检查异常和检查异常

- 非检查异常 : Error 和 RuntimeException 以及他们的子类  在编译时  不会提示和发现这样的异常  不要求程序处理这些异常  如除0错误ArithmeticException，错误的强制类型转换错误ClassCastException，数组索引越界ArrayIndexOutOfBoundsException，空指针NullPointerException等。
- 检查异常 : 除了Error 和 RuntimeException的其它异常  javac强制要求程序员为这样的异常做预备处理工作（使用try...catch...finally或者throws）。在方法中要么用try-catch语句捕获它并处理，要么用throws子句声明抛出它，否则编译不会通过。如SQLException , IOException,ClassNotFoundException 等

![img](https://images2017.cnblogs.com/blog/858860/201709/858860-20170911125844719-1230755033.png)

### 异常的链化

异常链化 : 以一个异常对象为参数构造新的异常对象。新的异对象将包含先前异常的信息。

这项技术主要是异常类的一个带Throwable参数的函数来实现的。这个当做参数的异常，我们叫他根源异常（cause）。

```java
// Throwable部分源码
public class Throwable implements Serializable {
    private Throwable cause = this;
   
    public Throwable(String message, Throwable cause) {
        fillInStackTrace();
        detailMessage = message;
        this.cause = cause;
    }
     public Throwable(Throwable cause) {
        fillInStackTrace();
        detailMessage = (cause==null ? null : cause.toString());
        this.cause = cause;
    }
}
```

### 自定义异常

如果要自定义异常类，则扩展Exception类即可，因此这样的自定义异常都属于检查异常（checked exception）。如果要自定义非检查异常，则扩展自RuntimeException。

按照国际惯例，自定义的异常应该总是包含如下的构造函数：

- 一个无参构造函数
- 一个带有String参数的构造函数，并传递给父类的构造函数。
- 一个带有String参数和Throwable参数，并都传递给父类构造函数
- 一个带有Throwable 参数的构造函数，并传递给父类的构造函数。

参照 IOException的源码

### throws和throw的区别

- throw : 1) throw用于方法体内  表示抛出异常 由方法体内的语句处理;  2) throw 抛出的是一个异常实例
- throws : 1) throws用于方法声明后面  由方法的调用者处理异常 2) throws抛出的是异常的类型  3) throws表示出现异常的一种可能性  并不一定会发生这种异常

### finally块和return 

具体例子参考链接 [Java中的异常和处理详解](https://www.cnblogs.com/lulipro/p/7504267.html)

- finally中的return 会覆盖 try 或者catch中的返回值
- finally中的return会抑制（消灭）前面try或者catch块中的异常
- finally中的异常会覆盖（消灭）前面try或者catch中的异常

建议：

- 不要在fianlly中使用return。
- 不要在finally中抛出异常。
- 减轻finally的任务，不要在finally中做一些其它的事情，finally块仅仅用来释放资源是最合适的。
- 将尽量将所有的return写在函数的最后面，而不是try ... catch ... finally中。

### 小考: 写出5个左右你常见的异常类

- NullPointerException
- ClassCastException
- IndexOutOfBoundsException
- StringIndexOutOfBoundsException
- NoSuchMethodException
- NoClassDefFoundException
- IOException
- SQLException