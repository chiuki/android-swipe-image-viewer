Android Swipe Image Viewer
--------------------------

This image viewer shows one image at a time. Swiping moves the image back and forth.

The initial implementation uses `ImageSwitcher` with `GestureDetector`:
[ImageSwitcher tag] [1]

Here is my [blog post] [2] explaining how it was made.

It has been updated to use `ViewPager`, which slides the images as you swipe, giving better visual feedback.

  [1]: https://github.com/chiuki/android-swipe-image-viewer/tree/ImageSwitcher
  [2]: http://blog.sqisland.com/2012/07/android-swipe-image-viewer.html
