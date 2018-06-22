package com.ywwynm.androidpractice.vrplayer.utils

import android.graphics.Bitmap
import android.opengl.GLES31
import android.opengl.GLUtils
import android.util.Log

class TextureUtils {
  companion object {

    const val TAG = "TextureUtils"

    fun loadTexture(bitmap: Bitmap): Int {
      val textureObjectIds = IntArray(1)
      GLES31.glGenTextures(1, textureObjectIds, 0)
      if (textureObjectIds[0] == 0) {
        Log.e(TAG, "failed to generate texture id")
        return 0
      }

      GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureObjectIds[0])

      GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_LINEAR_MIPMAP_LINEAR)
      GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_LINEAR)
      GLUtils.texImage2D(GLES31.GL_TEXTURE_2D, 0, bitmap, 0)

      GLES31.glGenerateMipmap(GLES31.GL_TEXTURE_2D)
      GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, 0)

      return textureObjectIds[0]
    }

  }
}