# DepSpider
This framework is built with Java and is designed to scan and analyze page-level component dependencies within React-based front-end projects.

这个框架由 Java 构建，旨在扫描和分析基于 React 的前端项目中的页面级组件依赖关系。
---

### 引入依赖：

```xml
<dependency>
    <groupId>io.github.1807149205</groupId>
    <artifactId>depspider</artifactId>
    <version>0.0.9</version>
</dependency>
```

### 用法
准备工作：
```java
import org.wzl.depspider.react.project.ReactProjectOperator;
import org.wzl.depspider.react.project.config.language.Language
import org.wzl.depspider.react.project.config.language.*

import java.util.Set;

//配置
ProjectConfiguration projectConfiguration = new ProjectConfiguration();
//配置项目的语言，有ts和js语言
Set<Language> languages = new HashSet<>();
languages.add(Language.JS);
languages.add(Language.TS);
projectConfiguration.setLanguages(languages);

//配置项目要扫描的路径
List<String> scanPath = new ArrayList<>();
scanPath.add("src");
scanPath.add("pages");
projectConfiguration.setScanPath(scanPath);

//操作类
IReactProjectOperator reactProjectOperator = new ReactProjectOperator(
        "D:\\gitlab\\wd-operation-front",   //前端项目根目录
        projectConfiguration                //配置
);

```
#### 1、获取项目依赖关系
```java
IReactProjectOperator reactProjectOperator = new ReactProjectOperator(
        "D:\\gitlab\\wd-operation-front",   //前端项目根目录
        projectConfiguration                //配置
);
List<ProjectFileRelation> relations = reactProjectOperator.jsxFileRelation();
```
返回示例：
```json
[
  {
    "targetFile": "src/pages/home/index.jsx",
    "relationFilePaths": [
      "src/pages/home/data.js",
      "src/component/List/index.jsx",
    ]
  },
  {
    ...
  }
]
```