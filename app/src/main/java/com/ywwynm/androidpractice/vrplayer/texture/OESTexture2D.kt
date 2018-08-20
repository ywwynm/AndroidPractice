package com.ywwynm.androidpractice.vrplayer.texture

import android.opengl.GLES11Ext
import android.opengl.GLES31
import android.util.Log
import com.ywwynm.androidpractice.R
import com.ywwynm.androidpractice.vrplayer.utils.ShaderUtils
import com.ywwynm.androidpractice.vrplayer.utils.glCheckError

class OESTexture2D: Texture2D() {

  override val TAG = "OESTexture2D"

  override fun init() {
    super.init()
    initTexture()
  }

  private fun initTexture() {
    val idContainer = IntArray(1)
    GLES31.glGenTextures(1, idContainer, 0)
    textureId = idContainer[0]
    GLES31.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)
    glCheckError(TAG, "glBindTexture $textureId")
    Log.i(TAG, "texture_external_oes generated: $textureId")

    GLES31.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
        GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_NEAREST.toFloat())
    GLES31.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
        GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_LINEAR.toFloat())
    GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D,
        GLES31.GL_TEXTURE_WRAP_S, GLES31.GL_CLAMP_TO_EDGE)
    GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D,
        GLES31.GL_TEXTURE_WRAP_T, GLES31.GL_CLAMP_TO_EDGE)

    GLES31.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)
  }

  override fun loadShadersSrc() {
    vertexShaderSrc = ShaderUtils.readRawTextFile(R.raw.video_vertex_shader)
    fragShaderSrc = ShaderUtils.readRawTextFile(R.raw.video_oes_fragment_shader)
  }

  override fun bindTexture() {
    GLES31.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)
    glCheckError(TAG, "glBindTexture $textureId")
  }

  override fun unbindTexture() {
    GLES31.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)
    glCheckError(TAG, "glBindTexture $textureId")
  }

}