# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.


file(GLOB helloext_src
        "../../../../external_folder/*.h"
        "../../../../external_folder/*.cpp"
        "../../../../external_folder/psychetouch/psychetouch/src/libcoloring/*.h"
        "../../../../external_folder/psychetouch/psychetouch/src/libcoloring/*.cpp"
        "../../../../external_folder/psychetouch/psychetouch/src/libanimation/*.h"
        "../../../../external_folder/psychetouch/psychetouch/src/libanimation/*.cpp"
        "../../../../external_folder/psychetouch/psychetouch/src/libdna/*.h"
        "../../../../external_folder/psychetouch/psychetouch/src/libdna/*.cpp"
        "../../../../external_folder/psychetouch/psychetouch/src/libserver/*.h"
        "../../../../external_folder/psychetouch/psychetouch/src/libserver/*.cpp"
        )

include_directories(
        "../../../../external_folder"
        "../../../../external_folder/psychetouch/psychetouch/src/libcoloring"
        "../../../../external_folder/psychetouch/psychetouch/src/libanimation"
        "../../../../external_folder/psychetouch/psychetouch/src/libdna"
        "../../../../external_folder/psychetouch/psychetouch/src/libserver"
)

add_library( # Sets the name of the library.
        native-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/cpp/native-lib.cpp
        ${helloext_src})
#${psychetouch_src})

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        native-lib

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib} )

#include("ndk-stl-config.cmake")