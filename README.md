## BottomBar
 [ ![Download](https://api.bintray.com/packages/wenchieh/maven/BottomBar/images/download.svg) ](https://bintray.com/wenchieh/maven/BottomBar/_latestVersion)
 ![](https://img.shields.io/badge/build-passing-green.svg)
 ![](https://img.shields.io/badge/license-MIT-orange.svg)


## 如何使用

1. 添加依赖

```
implementation 'me.wenchieh:BottomBar:<lastest version>'
```

2. 在布局中添加 BottomBar 控件

```xml
 <wenchieh.lu.bottombar.BottomBar
      android:id="@+id/bottomBar"
      android:layout_width="match_parent"
      android:layout_height="50dp"
      android:layout_gravity="bottom"
      android:background="?android:attr/windowBackground"
      />
```

3. 在代码中给 BottomBar 初始化 BottomTab

```kotlin
    val home = BottomTab.Builder(this).text("home")
        // 默认图片资源id，和 bitmap 不能共存
        .iconNormal(mipmap.ic_home_unselected)
        // 选择后的图片资源id，和 bitmap 不能共存
        .iconSelected(mipmap.ic_home)
        // 默认图片的 bitmap，和资源 id 不能共存
        .iconNormalBt(normalBt)
        // 选中图片的 bitmap，和资源 id 不能共存
        .iconSelectedBt(selectedBt)
        // 图片和文字之间的间距，单位是 px
        .padding(dp2px(10f))
        // 默认字体的颜色
        .textColorNormal(color(android.R.color.black))
        // 选择后字体的颜色
        .textColorSelected(color(android.R.color.holo_red_dark))
        // 字体的大小
        .textSize(15f)
        // 角标文本的数量，文本和圆点只能 2 选 1
        .badgeNumber(8)
        // 是否显示圆点，文本和圆点只能 2 选 1
        .isShowPoint(true)
        // 角标的背景颜色
        .badgeBackgroundColor(Color.BLUE)
        // 角标字体的颜色
        .badgeTextColor(Color.RED)
        .build()
```


4. 设置监听

```
// 选择的监听，pre 是上一个 tab 的位置，cur 是当前选择后 tab 的位置
bottomBar.setOnSelectedListener { pre, cur ->

}

// 再次重复选择的监听
bottomBar.setOnReSelectedListener { cur ->

}
```

5. 初始化

```
bottomBar.setupTab(bottomTab)
```

6. 设置默认选择的 Tab

```
// 参数为 tab 在 bottomBar 的位置索引
bottomBar.select(0)
```

## 从网络中动态加载图片
支持直接设置 Bitmap。

如果底栏的图片是服务器动态配置的。可以使用 `bottomBar.update(0,home)` 更新某个位置的 tab。


```kotlin
 // 默认的 tab
 val home = BottomTab.Builder(this)
        .text("home)
        .iconNormal(mipmap.ic_home_unselected)
        .iconSelected(mipmap.ic_home)
        .build()


 // 从网络中请求图片信息后更新 tab
 val newHomeTab = home.newBuilder()
         .iconNormalBt(normalBt)
         .iconSelectedBt(selectedBt).build()
 bottomBar.update(0, newHomeTab)
```


## 直接在布局中设置（不推荐）
也支持直接在布局中配置 BottomTab，但是比较繁琐。建议使用代码动态配置。


![](http://7xt4re.com1.z0.glb.clouddn.com/20180515152636067658667.jpg)
