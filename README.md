# SlidableViewModifier
Animate ViewContainer with Slide Gesture. It can be with a modifier class

![image](https://user-images.githubusercontent.com/41609506/153758679-fbc16469-00c5-4e02-9c6d-360748b5f2b3.png)


Hwo to use?


```kotlin

  modifier = SlidableViewModifier().setView(myItem).setMaxHeight(1800).setMinHeight(1000).setHeadRatio(0.35).setOnSlideRatioChangeListener {       
               imageView.alpha = (1-it)
            }.activate()

```

