## BottomBar
 [ [ ![Download](https://api.bintray.com/packages/wenchieh/maven/bottombar/images/download.svg) ](https://bintray.com/wenchieh/maven/bottombar/_latestVersion)
 ![](https://img.shields.io/badge/build-passing-green.svg)
 ![](https://img.shields.io/badge/license-MIT-orange.svg)

A Convenient Bottom Bar for Android


BottomBar is a small library, written in Kotlin.

its Child View inherited from View. drawing directly with code instead of using a combined View. So there are only 2 layers of View.

## Download
Grab via Gradle:
```
implementation 'me.wenchieh:BottomBar:0.9.1'
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
### support attrs in BottomTab

Name | Type | Des
| --- | ---|---
tabIconNormal | reference | the normal icon
tabIconSelected | reference | the selected icon
tabText | string | text
tabTextSize | dimension | text's size
textColorNormal | color | normal text color
textColorSelected | color | selected text color
badgeBackgroundColor | color | the color of badge backgound
paddingTextWithIcon | dimension | the padding between text and pictures


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
    bottomBar.setupTab(dp2px(9f), Color.GRAY, Color.RED, *array)

    // when first selected
    bottomBar.setOnSelectedListener { pre, cur ->

    }

    // when reSelected
    bottomBar.setOnReSelectedListener {

    }

    // you can select by youself
    bottomBar.select(0)
```

### in xml
```
<?xml version="1.0" encoding="utf-8"?>
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
      >

    <wenchieh.lu.bottombar.BottomTab
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_weight="1"
        app:tabText="Home"
        app:tabTextSize="16sp"
        app:tabIconNormal="@mipmap/ic_home_unselected"
        app:tabIconSelected="@mipmap/ic_home"
        app:paddingTextWithIcon="10dp"
        app:badgeBackgroundColor="@android:color/holo_red_dark"
        app:textColorSelected="@color/color333333"
        app:textColorNormal="@color/colorDADADA"
        />

    <wenchieh.lu.bottombar.BottomTab
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_weight="1"
        app:tabText="Profile"
        app:tabTextSize="16sp"
        app:tabIconNormal="@mipmap/ic_home_unselected"
        app:tabIconSelected="@mipmap/ic_home"
        app:paddingTextWithIcon="10dp"
        app:badgeBackgroundColor="@android:color/holo_red_dark"
        app:textColorSelected="@color/color333333"
        app:textColorNormal="@color/colorDADADA"
        />

    <wenchieh.lu.bottombar.BottomTab
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_weight="1"
        app:tabText="Message"
        app:tabTextSize="16sp"
        app:tabIconNormal="@mipmap/ic_home_unselected"
        app:tabIconSelected="@mipmap/ic_home"
        app:paddingTextWithIcon="10dp"
        app:badgeBackgroundColor="@android:color/holo_red_dark"
        app:textColorSelected="@color/color333333"
        app:textColorNormal="@color/colorDADADA"
        />
  </wenchieh.lu.bottombar.BottomBar>

</android.support.constraint.ConstraintLayout>
```






## Sample


![](http://7xt4re.com1.z0.glb.clouddn.com/20180515152636067658667.jpg)


