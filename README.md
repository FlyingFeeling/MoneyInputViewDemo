MoneyKeyboardView 是一个用来输入金额的自定义控件,代码风格采用的kotlin。

### 演示GIF

![radio.gif](https://github.com/FlyingFeeling/MoneyInputViewDemo/blob/master/image/radio.gif)

### 项目集成
1.在你的项目的build.gradle中添加如下代码
```language
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
2.添加依赖
```language
dependencies {
    implementation 'com.github.FlyingFeeling:MoneyInputViewDemo:1.0.1'
}
```
### 属性介绍

|属性|格式|例如|
|-|-|-|
|textSize|dimension|20sp|
|textColor|color|#000000|
|keyBackground|reference|app:keyBackground="@drawable/keyboard_bg"|
|dividerColor|color|#BFBFBF|
|deleteDrawable|reference|app:deleteDrawable="@drawable/ic_delete"|
|confirmDrawable|reference|app:confirmDrawable="@drawable/ic_confirm"|

### 使用

```java
keyboardView?.setOnclickListener(object : MoneyKeyboardView.OnClickListener {
    override fun onClickKey(keyValue: Any) {
        if(keyValue is String){
            edAmount?.addText(keyValue)
        }
    }
    override fun onConfirm() {
        Toast.makeText(this@MainActivity,"amount: ${edAmount?.getAmount()}", Toast.LENGTH_SHORT).show()
    }
    override fun onDelete() {
        edAmount?.deleteText()
    }
})
```
