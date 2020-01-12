# Magic Color
Magic color is an Android app that turns black and white drawings into colorful gradients.


## The app


### App store
You can find the app on the Google Play store here: https://play.google.com/store/apps/details?id=com.algofashion.magiccolor


### Features
The app has three main features:
 - it will turn a picture into a colorful gradient looking like scratch paper
 - it provides a library of color palettes used for the gradients.
 - it allows you to create your own color palette for the gradients.

### Screenshots
![Image of Result](https://github.com/edeguine/magiccolor/tree/master/images/Result.png)
![Image of Palettes](https://github.com/edeguine/magiccolor/tree/master/images/Palettes.png)
![Image of Custom palette](https://github.com/edeguine/magiccolor/tree/master/images/CustomPalette.png)
![Image of Custom Result](https://github.com/edeguine/magiccolor/tree/master/images/CustomResult.png)
![Image of UI](https://github.com/edeguine/magiccolor/tree/master/images/MainUI.png)



## The code


### License

The software is free but copyrighted. It is copyrighted under the [JRL license](https://en.wikipedia.org/wiki/Java_Research_License), commercial or proprietary use is forbidden but research and academic use are allowed.


### Implementation details

This projects leverages CPP inside an Android java app.
The code that performs the core of the bitmap manipulation is in the cpp folder.
The code that provides UI, UX and color palette manipulation is in the magiccolor subfolder (Android Java app).

### CPP library

To use the CPP code and generate a test image test.png:
```
    cd cpp/test
    make test
    ./test mandala.png
    open test.png
```

You can look in test.cpp to see how the library is used.
You might need to install libpng.
