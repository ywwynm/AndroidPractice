# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/Context.h src/main/cpp/Context.cpp
             src/main/cpp/native-lib.cpp

             src/main/cpp/opengl/Triangle.h src/main/cpp/opengl/Triangle.cpp
             src/main/cpp/opengl/Texture2D.h src/main/cpp/opengl/Texture2D.cpp
             src/main/cpp/opengl/Texture2DExt.h src/main/cpp/opengl/Texture2DExt.cpp
             src/main/cpp/opengl/TextureTransfer.h

             src/main/cpp/utils/log.h
             src/main/cpp/utils/glutils.h src/main/cpp/utils/glutils.cpp

             src/main/cpp/unity/IUnityInterface.h
             src/main/cpp/unity/UnityGraphics.h
             )

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
find_library(gles-v3-lib GLESv3)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       native-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib}
                       ${gles-v3-lib}
                       )