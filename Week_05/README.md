学习笔记

### 第九课

#### 1. 使用 Java 中的动态代理，实现一个简单的 AOP

通过动态代理，实现对指定方法进行前后拦截，[代码地址](https://github.com/oliverschen/JAVA-000/tree/main/Week_05/homework/jdkproxy/src/main/java/com/github/oliverschen/proxy)

代理类	

```java
/**
 * @author ck
 * Java 动态代理 AOP
 */
public class LogAOP implements InvocationHandler {

    public static void main(String[] args) {
        IOrderService orderService = new OrderServiceImpl();
        IOrderService o = (IOrderService) Proxy.newProxyInstance(
                orderService.getClass().getClassLoader(),
                orderService.getClass().getInterfaces(),
                new LogAOP(orderService)
        );
        o.placeOrder();
    }

    private Object object;

    public LogAOP(Object o) {
        this.object = o;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long startTime = doBefore();
        Object result = method.invoke(this.object, args);
        doAfter(startTime);
        return result;
    }

    private void doAfter(long startTime) {
        System.out.println();
        System.out.println("AOP 结束调用，总共消耗：" + (System.currentTimeMillis() - startTime) + " ms");
    }

    private long doBefore() {
        long l = System.currentTimeMillis();
        System.out.println("AOP 开始调用时间：" + l);
        System.out.println();
        return l;
    }
}
```

接口

```java
public interface IOrderService {
    /**
     * 下单
     */
    boolean placeOrder();
}
```

实现类

```java
public class OrderServiceImpl implements IOrderService {

    @Override
    public boolean placeOrder() {
        System.out.println("下单中...");
        System.out.println("订单已创建");
        System.out.println("下单完成！");
        return true;
    }
}
```

总结：使用 AOP 能对代码进行无侵入的增强，也是 Spring 核心构成部分之一。

#### 2. 写代码实现 Spring bean 的装配，方式越多越好

总体使用了 XML 和 注解两种方式进行装配，[代码地址](https://github.com/oliverschen/JAVA-000/tree/main/Week_05/homework/spring-bean/src/main/java/com/github/oliverschen/springbean)

1. 使用 XML <beans> 标签，用 ClassPathXmlApplicationContext 进行加载 XML 解析装配获取bean
2. 使用 AnnotationConfigApplicationContext 注解扫描方式，加载 @Componet 注解装配 bean
3. 使用 AnnotationConfigApplicationContext 注解扫描，加载 @Configuration 



