## BottomBar
 [ ![Download](https://api.bintray.com/packages/wenchieh/maven/bottombar/images/download.svg) ](https://bintray.com/wenchieh/maven/bottombar/_latestVersion)
 ![](https://img.shields.io/badge/build-passing-green.svg)
 ![](https://img.shields.io/badge/license-MIT-orange.svg)

A Convenient Bottom Bar for Android


BottomBar is a small library, written in Kotlin.

its Child View inherited from View. drawing directly with code instead of using a combined View. So there are only 2 layers of View.

## Download
Grab via Gradle:
```
implementation 'me.wenchieh:bottombar:1.0.2'
```
or Maven:

```
<dependency>
  <groupId>me.wenchieh</groupId>
  <artifactId>BottomBar</artifactId>
  <version>0.9.1</version>
  <type>pom</type>
</dependency>
```

## How to use

### use in code

first declare the BottomBar in xml.

```
<android.support.constraint.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/container"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="wenchieh.lu.sample.SampleActivity"
    >

  <TextView
      android:id="@+id/message"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:layout_marginLeft="@dimen/activity_horizontal_margin"
      android:layout_marginStart="@dimen/activity_horizontal_margin"
      android:layout_marginTop="@dimen/activity_vertical_margin"
      android:text="@string/title_home"
      app:layout_constraintLeft_toLeftOf="parent"
      app:layout_constraintTop_toTopOf="parent"
      />

  <wenchieh.lu.bottombar.BottomBar
      android:id="@+id/bottomBar"
      android:layout_width="0dp"
      android:layout_height="50dp"
      android:layout_marginEnd="0dp"
      android:layout_marginStart="0dp"
      android:background="?android:attr/windowBackground"
      app:layout_constraintBottom_toBottomOf="parent"
      app:layout_constraintLeft_toLeftOf="parent"
      app:layout_constraintRight_toRightOf="parent"
      />

</android.support.constraint.ConstraintLayout>

```

```kotlin
 val tabs = Arrays.asList(
        BottomTab(this, text = "首页", iconNormal = mipmap.ic_home_unselected,
            iconSelected = R.mipmap.ic_home, mBadgeNumber = 8),
        BottomTab(this, text = "发现", iconNormal = R.mipmap.ic_home_unselected,
            iconSelected = R.mipmap.ic_home, mBadgeNumber = 22),
        BottomTab(this, iconNormal = R.mipmap.ic_home_unselected,
            iconSelected = R.mipmap.ic_home, mBadgeNumber = 888),
        BottomTab(this, text = "消息", iconNormal = R.mipmap.ic_home_unselected,
            iconSelected = R.mipmap.ic_home, isShowPoint = true),
        BottomTab(this, text = "我的", iconNormal = R.mipmap.ic_home_unselected,
            iconSelected = R.mipmap.ic_home))

    val array = tabs.toTypedArray()
    bottomBar.addTab(*array)

    // when first selected
    bottomBar.setOnSelectedListener { pre, cur ->

    }

    // when reSelected
    bottomBar.setOnReSelectedListener {

    }

    // you can select by youself
    bottomBar.selectItem(0)
```

## Sample


![](http://7xt4re.com1.z0.glb.clouddn.com/20180515152636067658667.jpg)

## changelog

* 1.0.2(2019/8/15): remove xml setting . bugsfixed. change some api
