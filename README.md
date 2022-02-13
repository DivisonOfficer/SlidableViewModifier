# SlidableViewModifier
Animate ViewContainer with Slide Gesture. It can be with a modifier class



Hwo to use?


"""


  modifier = SlidableViewModifier().setView(myItem).setMaxHeight(1800).setMinHeight(1000).setHeadRatio(0.35).setOnSlideRatioChangeListener {       
               imageView.alpha = (1-it)
               
            }.activate()

"""

