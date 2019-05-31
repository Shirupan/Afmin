# 基于mvp框架的基础库


## 已实现：
网络请求（retrofit） </br>
图片加载（glide） </br>
eventbus </br>
日志打印 </br>
文件读写 </br>
spUtil </br>
ButterKnife </br>
viewpager </br>
StatusBarUtil </br>
标题栏titlebar </br>
activity滑动返回（swipeback） </br>
混淆  </br>

## 计划要实现：
回调  </br>
dialog  </br>
跳转  </br>
数据库（Room） </br>
加载失败界面  </br>
webview  </br>
内存分析  </br>
动画  </br>
键盘  </br>
Activity管理  </br>
Activity过场动画

## FAQ
1、Static interface methods are only supported starting with Android N，JDK1.8以下不支持lambda，添加如下配置到build.gradle</br>
    compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
    }

2、提示add 'tools:replace="android:appComponentFactory"' to <application> element at AndroidManifest.xml，添加如下配置到AndroidManifest</br>
    tools:replace="android:appComponentFactory"
    android:appComponentFactory="android.support.v4.app.CoreComponentFactory"
        
3、More than one file was found with OS independent path 'META-INF/...'，添加如下配置到build.gradle</br>
    packagingOptions {
         exclude 'META-INF/*'
    }

