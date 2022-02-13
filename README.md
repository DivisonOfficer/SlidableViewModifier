# SlidableViewModifier
Animate ViewContainer with Slide Gesture. It can be with a modifier class

![20220213_234855](https://user-images.githubusercontent.com/41609506/153758739-d5c7893a-78a9-42df-bad9-71b6c7de0475.gif)



Hwo to use?


```kotlin

  modifier = SlidableViewModifier().setView(myItem).setMaxHeight(1800).setMinHeight(1000).setHeadRatio(0.35).setOnSlideRatioChangeListener {       
               imageView.alpha = (1-it)
            }.activate()

```

https://github.com/DivisonOfficer/SlidableViewModifier/tree/master/app/src/main/java/com/divisonofficer/slidableviewmodyfier
