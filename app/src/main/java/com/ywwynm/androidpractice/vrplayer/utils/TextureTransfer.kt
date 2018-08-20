package com.ywwynm.androidpractice.vrplayer.utils

import android.opengl.GLES31
import android.util.Log

class TextureTransfer(private val hostName: String) {

  private var fboId = 0

  var tempTexture2DId = 0
    private set

  fun tryToCreateFBO() {
    if (fboId != 0) {
      return
    }
    val idContainer = IntArray(1)
    GLES31.glGenFramebuffers(1, idContainer, 0)
    fboId = idContainer[0]
    glCheckError(hostName, "glGenFrameBuffers")
    Log.i(hostName, "fbo generated: $fboId")
  }

  fun tryToCreateTempTexture2D(textureWidth: Int, textureHeight: Int) {
    if (tempTexture2DId != 0) {
      return
    }
    val idContainer = IntArray(1)
    GLES31.glGenTextures(1, idContainer, 0)
    tempTexture2DId = idContainer[0]
    glCheckError(hostName, "glGenTextures")
    Log.i(hostName, "temp texture2d generated: $tempTexture2DId")
    GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, tempTexture2DId) // ???
    glCheckError(hostName, "glBindTexture $tempTexture2DId")

    GLES31.glTexParameterf(GLES31.GL_TEXTURE_2D,
        GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_NEAREST.toFloat())
    GLES31.glTexParameterf(GLES31.GL_TEXTURE_2D,
        GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_LINEAR.toFloat())
    GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D,
        GLES31.GL_TEXTURE_WRAP_S, GLES31.GL_CLAMP_TO_EDGE)
    GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D,
        GLES31.GL_TEXTURE_WRAP_T, GLES31.GL_CLAMP_TO_EDGE)

    GLES31.glTexImage2D(GLES31.GL_TEXTURE_2D, 0, GLES31.GL_RGBA, textureWidth, textureHeight, 0,
        GLES31.GL_RGBA, GLES31.GL_UNSIGNED_BYTE, null) // empty texture
    glCheckError(hostName, "glTexImage2D")

    GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, 0) // ???
    glCheckError(hostName, "glBindTexture 0")

    fboStart()

    GLES31.glFramebufferTexture2D(GLES31.GL_FRAMEBUFFER, GLES31.GL_COLOR_ATTACHMENT0,
        GLES31.GL_TEXTURE_2D, tempTexture2DId, 0)
    glCheckError(hostName, "glFramebufferTexture2D")

    fboEnd()
  }

  fun fboStart() {
    GLES31.glBindFramebuffer(GLES31.GL_FRAMEBUFFER, fboId)
    glCheckError(hostName, "glBindFramebuffer $fboId")
  }

  fun fboEnd() {
    GLES31.glBindFramebuffer(GLES31.GL_FRAMEBUFFER, 0)
    glCheckError(hostName, "glFramebufferTexture2D 0")
  }

}