# 基于mvp框架的基础库
本框架仅适用于androidX

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
跳转，未封装拦截器（ARouter）  </br>

## 计划要实现：
回调  </br>
dialog  </br>
数据库（Room） </br>
加载失败界面  </br>
webview  </br>
内存分析  </br>
动画  </br>
键盘  </br>
Activity管理  </br>
Activity过场动画

## 使用
1、在项目build.gradle中添加

    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
   
2、添加依赖   
    
    implementation 'com.github.Shirupan:mvplib:1.0.3'
    annotationProcessor 'com.jakewharton:butterknife-compiler:10.1.0'

## FAQ
1、Static interface methods are only supported starting with Android N，JDK1.8以下不支持lambda，添加如下配置到build.gradle</br>

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }

2、提示add 'tools:replace="android:appComponentFactory"' to <application> element at AndroidManifest.xml，添加如下配置到AndroidManifest</br>

    <application
        tools:replace="android:appComponentFactory"
        android:appComponentFactory="android.support.v4.app.CoreComponentFactory">
    </application>
        
3、More than one file was found with OS independent path 'META-INF/...'，添加如下配置到build.gradle</br>

    packagingOptions {
         exclude 'META-INF/*'
    }
    
4、报错V4相关Program type already present: android.support.v4.**，修改gradle.properties，使用androidX</br>

    android.useAndroidX=true
    android.enableJetifier=true
