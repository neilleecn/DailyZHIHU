# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\android_develop_tools\android-sdk-windows/tools/proguard/proguard-android.txt
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

# 系统混淆配置
-dontusemixedcaseclassnames          #混淆时不使用大小写混合类名
-dontskipnonpubliclibraryclasses     #不跳过library中的非public的类
-verbose                             #打印混淆的详细信息
-dontoptimize                        #不进行优化，建议使用此选项，
-dontpreverify                       #不进行预校验,Android不需要,可加快混淆速度。
-ignorewarnings                      #忽略警告
#-optimizationpasses 5               #指定代码的压缩级别

###------------------------基本配置------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

# support.v4/v7包不混淆
-keep class android.support.** { *; }
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.** { *; }
-keep public class * extends android.support.v7.**
-keep interface android.support.v7.app.** { *; }
-dontwarn android.support.**    # 忽略警告

# 保持注解继承类不混淆
-keep class * extends java.lang.annotation.Annotation {*;}
# 保持Serializable实现类不被混淆
-keepnames class * implements java.io.Serializable
# 保持Serializable不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
# 保持枚举enum类不被混淆
-keepclassmembers enum * {
  public static **[] values();
 public static ** valueOf(java.lang.String);
}
# 自定义组件不被混淆
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

# 不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

###------------------------第三方jar包library混淆配置------------------------
# Butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

# Retrofit
-dontwarn retrofit.**
-dontwarn retrofit2.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# universalimageloader图片加载框架不混淆
-keep class com.nostra13.universalimageloader.** { *; }
-dontwarn com.nostra13.universalimageloader.**

# Gson相关的不混淆配置
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**
-keep class sun.misc.Unsafe { *; }
-keep class cn.neillee.dailyzhijiu.model.bean.**{ *; }
-keep class cn.neillee.dailyzhijiu.model.bean.original.**{ *; }

## ShareSDK 混淆
#-dontwarn cn.sharesdk.**
#-keep class cn.sharesdk.** { *; }

##zxing
#-keep class com.google.zxing.** { *; }
#-keep class com.google.zxing.**

# Okhttp
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**

# prt-lib下拉刷新框架不混淆
-keep class in.srain.cube.views.ptr.** { *; }
-dontwarn in.srain.cube.views.ptr.**

# PullToRefreshLibrary下拉刷新框架不混淆
-keep class com.handmark.pulltorefresh.library.** { *; }
-dontwarn com.handmark.pulltorefresh.library.**

# GreenDAO 3
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties

# If you do not use SQLCipher:
-dontwarn org.greenrobot.greendao.database.**
# If you do not use RxJava:
-dontwarn rx.**

## https://github.com/krschultz/android-proguard-snippets/blob/master/libraries/proguard-square-dagger.pro
# Dagger ProGuard rules.
# https://github.com/square/dagger
-dontwarn dagger.internal.codegen.**
-keepclassmembers,allowobfuscation class * {
    @javax.inject.* *;
    @dagger.* *;
    <init>();
}
-keep class dagger.* { *; }
-keep class javax.inject.* { *; }
-keep class * extends dagger.internal.Binding
-keep class * extends dagger.internal.ModuleAdapter
-keep class * extends dagger.internal.StaticInjection

# Bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}

# http://stackoverflow.com/questions/33047806/proguard-duplicate-definition-of-library-class
-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**
#参考：
#  http://blog.csdn.net/zuiwuyuan/article/details/48552701
#  http://blog.csdn.net/fengyuzhengfan/article/details/43876197
#  http://blog.isming.me/2014/05/31/use-proguard/
#  http://hanhailong.com/2015/12/28/Android%E8%BF%9B%E9%98%B6%E4%B9%8BProGuard%E4%BB%A3%E7%A0%81%E6%B7%B7%E6%B7%86/
# 关于混淆的思考：
#  https://www.zhihu.com/question/37446729
# 扩展：资源文件混淆
#  http://blog.csdn.net/Fancy_xty/article/details/51202866
#代码混淆规则
# 指定代码的压缩级别
#-optimizationpasses 5
# 是否使用大小写混合
#-dontusemixedcaseclassnames
# 是否混淆第三方jar
#-dontskipnonpubliclibraryclasses
# 混淆时是否做预校验
#-dontpreverify
# 混淆时是否记录日志
#-verbose
# 混淆时所采用的算法
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#-keep public class * extends android.app.Activity                               # 保持哪些类不被混淆
#-keep public class * extends android.app.Application                            # 保持哪些类不被混淆
#-keep public class * extends android.app.Service                                # 保持哪些类不被混淆
#-keep public class * extends android.content.BroadcastReceiver                  # 保持哪些类不被混淆
#-keep public class * extends android.content.ContentProvider                    # 保持哪些类不被混淆
#-keep public class * extends android.app.backup.BackupAgentHelper               # 保持哪些类不被混淆
#-keep public class * extends android.preference.Preference                      # 保持哪些类不被混淆
#-keep public class com.android.vending.licensing.ILicensingService              # 保持哪些类不被混淆

#-keepclasseswithmembernames class * {                                           # 保持 native 方法不被混淆
#    native <methods>;
#}
#
#-keepclasseswithmembers class * {                                               # 保持自定义控件类不被混淆
#    public <init>(android.content.Context, android.util.AttributeSet);
#}
#
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet, int);     # 保持自定义控件类不被混淆
#}
#
#-keepclassmembers class * extends android.app.Activity {                        # 保持自定义控件类不被混淆
#   public void *(android.view.View);
#}
#
#-keepclassmembers enum * {                                                      # 保持枚举 enum 类不被混淆
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#
#-keep class * implements android.os.Parcelable {                                # 保持 Parcelable 不被混淆
#  public static final android.os.Parcelable$Creator *;
#}

#-keep class MyClass;                                                            # 保持自己定义的类不被混淆