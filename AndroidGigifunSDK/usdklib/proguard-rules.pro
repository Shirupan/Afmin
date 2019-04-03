# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/tim/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*

-keep class com.gigifun.guestinfo.** { *; }
-keep class com.gigifun.model.** { *; }
-keep class com.gigifun.gp.percent.** { *; }
-keep class com.gigifun.gp.service.** { *; }
#第三方包


-dontwarn com.alipay.**
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}


#-dontwarn org.junit.**
#-keep class  org.junit.** { *;}
#
#-dontwarn com.appsflyer.**
#-keep class  com.appsflyer.** { *;}

-dontwarn kr.co.namee.permissiongen.**
-keep class  kr.co.namee.permissiongen.** { *;}

-dontwarn com.android.vending.billing.**
-keep class  com.android.vending.billing.** { *;}
-keepattributes Signature

-dontwarn com.google.gson.**
-keep class  com.google.gson.** { *;}

-keep class  com.google.ads.conversiontracking.** { *;}

-dontwarn okhttp3.**
-keep class  okhttp3.** { *;}



-dontwarn com.zhy.http.okhttp.**
-keep class  com.zhy.http.okhttp.** { *;}

-dontwarn com.gigifun.gp.percent.**
-keep class  com.gigifun.gp.percent.** { *;}

-dontwarn okio.**
-keep class  okio.** { *;}

-dontwarn com.nostra13.universalimageloader.**
-keep class  com.nostra13.universalimageloader.** { *;}

-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**

-dontwarn com.google.ads.**
-keep class com.google.ads.**{ *; }

#-dontwarn android.support.**
#-keep class android.support.** { *; }
#
#-dontwarn bolts.**
#-keep class bolts.** { *; }
#
#-dontwarn java.nio.file.**
#-keep class java.nio.file.** { *; }

#反射
-keep class com.gigifun.gp.utils.** { *; }
-keep class com.gigifun.gp.listener.** { *; }


-keep class com.gigifun.gp.UgameSDK {*;}
-keep class com.gigifun.gp.ui.LoginDialog {*;}
-keep class com.gigifun.gp.MorePay { *; }
