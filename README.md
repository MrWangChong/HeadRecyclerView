### Head联动

[![](https://jitpack.io/v/MrWangChong/HeadRecyclerView.svg)](https://jitpack.io/#MrWangChong/HeadRecyclerView)

## [下载APK体验](https://raw.githubusercontent.com/MrWangChong/HeadRecyclerView/master/apk/app-debug.apk)

### 引入
Step 1. Add the JitPack repository to your build file Add it in your root build.gradle at the end of repositories:
```gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency
```gradle
	dependencies {
	      compile 'com.github.MrWangChong:HeadRecyclerView:1.0.0'
	}
```


### 效果图

![image](https://github.com/MrWangChong/HeadRecyclerView/blob/master/image/image.gif)


### 支持
思路是由大神掌阅黄老师分享出来的:  
 ``` "给大家分享一下我们是怎么实现的，ViewPager是整个屏幕大小，里面的RecyleView也是整个屏幕大小，每个RecyleView都有一个head大小的全透明headView，ViewPager的底部有个正真的headView。当RecyleView滑动的时候在ScrollChange中移动正真的headView。当点击事件点中RecyleView的透明head区域时，把该事件发送给底部正真的head。"  ```
![image](http://note.youdao.com/yws/public/resource/a7de1ce6aec05c6edc9a20ca607efe0e/xmlnote/89F4D1B3DC2A4BFE91DE946D32D5E9CC/3343)
然而我在做的过程中，发现不实现懒加载更简单点，于是没有做懒加载支持

### 使用方法

* ##### 单个RecyclerView联动

可以下载demo看，使用起来也不复杂，主要是布局
```xml
<?xml version="1.0" encoding="utf-8"?>
<com.wc.recyclerview.HeadFrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.wc.recyclerview.HeadLayout
        android:id="@+id/head_layout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <head1>这里放跟着Recycler滑动的View或ViewProup(最好固定高度)
            ...
        </head1>

        <head2>这里放固定顶部的View或ViewGroup(最好固定高度)
            ...
         </head1>
    </com.wc.recyclerview.HeadLayout>


    <com.wc.recyclerview.HeadRecyclerView
        android:id="@+id/recycler_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</com.wc.recyclerview.HeadFrameLayout>


```
HeadLayout是只能放两个View或ViewGroup的竖向布局管理器，也可以换成LinearLayout，但是只能放两个View或ViewGroup  
HeadFrameLayout不能换，因为一般的布局在锁屏之后开屏会执行onLayout，原本View offsetTopAndBottom的就会被还原了。

然后在Activity中把HeadLayout设置到HeadRecyclerView中就行了。
其余该干什么干什么，只需要在setAdapter之后调用 
```java
//设置HeadView
mRecyclerView.setHeadView(mHeadLayout);
```
HeadRecyclerView内封装了上拉加载更多,不需要就不设置，不影响使用
```java
//设置上拉加载更多监听
        mRecyclerView.setOnLoadMoreListener(new HeadRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore(HeadRecyclerView view) {
                for (int i = 500; i < 588; i++) {
                    infos.add("加载更多，这个是item\t" + i);
                }
                //如果是本地加载不能直接刷新，网络加载本身就有一点延迟
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        //重置上拉加载更多，不重置下回不会触发加载更多
                        mRecyclerView.resetLoadMore();
                    }
                }, 50);
            }
        });
```
* ##### 嵌套ViewPager联动
demo里面也是有的，主要是在布局文件上面做文章
```xml
<?xml version="1.0" encoding="utf-8"?>
<com.wc.recyclerview.HeadFrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <head1>这里放跟着Recycler滑动的View或ViewProup(最好固定高度)
            ...
    </head1>

    <!-- 标题 --> 这里放固定顶部的View或ViewGroup(最好固定高度),我使用的是标签导航栏PagerNavigationBar
    <com.wc.pagerbar.PagerNavigationBar
        android:id="@+id/pagerBar"
        android:layout_width="match_parent"
        android:layout_height="40dp"
        android:background="@android:color/white" />
    </com.wc.recyclerview.HeadLayout>

    <com.wc.recyclerview.HeadViewPager
        android:id="@+id/viewPager"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</com.wc.recyclerview.HeadFrameLayout>
```
使用方法和普通的ViewPager以及单个的RecyclerView联动一模一样的，[PagerNavigationBar](https://github.com/MrWangChong/ViewPagerBar)是我自己的一个开源库。
```java
 recyclerView.setHeadView(headLayout);
```
RecyclerView的设置和单个的一样
```java
 //目前必须设置缓存为所有
viewPager.setOffscreenPageLimit(views.size());
 //如果有需要滑动的HeadView需要设置这个，没有就不用
viewPager.setHeadView(headLayout);
```

目前暂时不支持ViewPager懒加载，需要设置setOffscreenPageLimit缓存全部  
如果有需要横向滑动的Head需要viewPager.setHeadView(headLayout);才能把横向滑动事件交给Head