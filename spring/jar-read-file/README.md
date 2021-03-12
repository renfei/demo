# Spring Boot 在 Jar 包中使用 getInputStream 获取文件流的验证
我之前写了一篇《SpringBoot项目打包成jar后读取文件的大坑，使用ClassPathResource获取classpath下文件失败》，发表在了我的博客和 CSDN 上。

很久没登陆 CSDN 了，今天登陆进来发现两个网友的留言，都是用我的方式 亲测，获取不到。

那我就亲自再运行一遍，写个 Demo，亲自验证以下到底能不能使用 getInputStream 获取 Jar 包里的文件。

## 相关链接
- [https://www.renfei.net/posts/1003293](https://www.renfei.net/posts/1003293)
- [https://blog.csdn.net/qq_41337646/article/details/103495110](https://blog.csdn.net/qq_41337646/article/details/103495110)