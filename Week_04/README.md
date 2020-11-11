学习笔记

（必做）思考有多少种方式，在main函数启动一个新线程或线程池，异步运行一个方法，拿到这个方法的返回值后，退出主线程？

1. 使用 CompletableFuture

   ```java
   CompletableFuture<Integer> result = CompletableFuture.supplyAsync(Homework03::sum);
   ```

2. 最近太忙，今天没有补完，明天继续

