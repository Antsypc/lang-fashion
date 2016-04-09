lang-fashion
============
> 为了开发时阅读方便,就不翻译成英文了.

> github爬取接口官方文档 https://developer.github.com

项目主要是爬取 github 上用户及用户仓库信息.最终分析各编程语言的流行度,趋势,应用领域等问题.本地数据库的创建方法在[这里](database/)

项目用到的工具,框架主要有: `maven`,`mybatis`,`jackson`.

其中的 web 项目主要是获取 github application 用户验证的 access_token.

## 开发过程中遇到及解决的问题

jackson 转换 Json,并存入数据库,其中的 entity 类设计和数据库表设计的问题比较揪心.
