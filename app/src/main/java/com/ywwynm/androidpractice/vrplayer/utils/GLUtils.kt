package com.ywwynm.androidpractice.vrplayer.utils

import android.opengl.GLES31
import android.util.Log

fun glCheckError(checker: String, operation: String) {
  val error = GLES31.glGetError()
  if (error != GLES31.GL_NO_ERROR) {
    Log.e(checker, "$operation caused glError: $error")
    throw RuntimeException("$operation caused glError: $error")
  }
}