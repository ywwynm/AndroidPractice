package com.ywwynm.androidpractice.vrplayer.texture

import android.opengl.GLES31
import android.util.Log
import com.ywwynm.androidpractice.R
import com.ywwynm.androidpractice.vrplayer.utils.ShaderUtils
import com.ywwynm.androidpractice.vrplayer.utils.glCheckError
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

open class Texture2D {

  protected open val TAG = "Texture2D"

  protected lateinit var vertexShaderSrc: String
  protected lateinit var fragShaderSrc: String

  protected var programId = -1

  var projectionMatrix = FloatArray(16)
  protected var uMatHandle = -1
  protected var aPosHandle = -1

  var stMatrix = FloatArray(16)
  protected var uSTMatrixHandle = -1

  protected var vertexData = floatArrayOf(
      1.0f,  1.0f, 0.0f,
      -1.0f,  1.0f, 0.0f,
      1.0f, -1.0f, 0.0f,
      -1.0f, -1.0f, 0.0f
  )
  protected lateinit var vertexBuffer: FloatBuffer

  protected var textureVertexData = floatArrayOf(
      1.0f, 0.0f,
      0.0f, 0.0f,
      1.0f, 1.0f,
      0.0f, 1.0f
  )
  protected lateinit var textureVertexBuffer: FloatBuffer

  protected var uTextureSamplerHandle = -1
  protected var aTextureCoordHandle = -1

  var textureId: Int = -1

  open fun init() {
    vertexBuffer = ByteBuffer.allocateDirect(vertexData.size * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()
        .put(vertexData)
    vertexBuffer.position(0)

    textureVertexBuffer = ByteBuffer.allocateDirect(textureVertexData.size * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()
        .put(textureVertexData)
    textureVertexBuffer.position(0)

    loadShadersSrc()
    createProgram()
    findHandles()
  }

  protected open fun loadShadersSrc() {
    vertexShaderSrc = ShaderUtils.readRawTextFile(R.raw.video_vertex_shader)
    fragShaderSrc = ShaderUtils.readRawTextFile(R.raw.video_fragment_shader)
  }

  protected open fun createProgram() {
    val vertexShader = ShaderUtils.compileShader(GLES31.GL_VERTEX_SHADER, vertexShaderSrc)
    val fragmentShader = ShaderUtils.compileShader(GLES31.GL_FRAGMENT_SHADER, fragShaderSrc)
    programId = ShaderUtils.createProgram(intArrayOf(vertexShader, fragmentShader))
    Log.i(TAG, "vertexShader: $vertexShader, fragmentShader: $fragmentShader, programId: $programId")
  }

  protected open fun findHandles() {
    uMatHandle = GLES31.glGetUniformLocation(programId, "uMatrix")
    Log.i(TAG, "uMatHandle: $uMatHandle")

    aPosHandle = GLES31.glGetAttribLocation(programId, "aPosition")
    Log.i(TAG, "aPosHandle: $aPosHandle")

    uTextureSamplerHandle = GLES31.glGetUniformLocation(programId, "uTexture")
    Log.i(TAG, "uTextureSamplerHandle: $uTextureSamplerHandle")

    aTextureCoordHandle = GLES31.glGetAttribLocation(programId, "aTexCoord")
    Log.i(TAG, "aTextureCoordHandle: $aTextureCoordHandle")

    uSTMatrixHandle = GLES31.glGetUniformLocation(programId, "uSTMatrix")
    Log.i(TAG, "uSTMatrixHandle: $uSTMatrixHandle")
  }

  protected open fun bindTexture() {
    GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureId)
    glCheckError(TAG, "glBindTexture $textureId")
  }

  protected open fun unbindTexture() {
    GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, 0)
    glCheckError(TAG, "glBindTexture 0")
  }

  open fun draw(width: Int, height: Int) {
    GLES31.glUseProgram(programId)
    glCheckError(TAG, "glUseProgram")

    GLES31.glUniformMatrix4fv(uMatHandle, 1, false, projectionMatrix, 0)
    glCheckError(TAG, "glUniformMatrix4fc uMatHandle")
    GLES31.glUniformMatrix4fv(uSTMatrixHandle, 1, false, stMatrix, 0)
    glCheckError(TAG, "glUniformMatrix4fc uSTMatrixHandle")

    vertexBuffer.position(0)
    GLES31.glEnableVertexAttribArray(aPosHandle)
    glCheckError(TAG, "glEnableVertexAttribArray aPosHandle")
    GLES31.glVertexAttribPointer(aPosHandle, 3, GLES31.GL_FLOAT, false,
        12, vertexBuffer)
    glCheckError(TAG, "glVertexAttribPointer aPosHandle")

    textureVertexBuffer.position(0)
    GLES31.glEnableVertexAttribArray(aTextureCoordHandle)
    glCheckError(TAG, "glEnableVertexAttribArray aTextureCoordHandle")
    GLES31.glVertexAttribPointer(aTextureCoordHandle, 2, GLES31.GL_FLOAT, false,
        8, textureVertexBuffer)
    glCheckError(TAG, "glVertexAttribPointer aTextureCoordHandle")

    bindTexture()

    GLES31.glUniform1i(uTextureSamplerHandle, 0)
    glCheckError(TAG, "glUniform1i uTextureSamplerHandle")
    GLES31.glViewport(0, 0, width, height)
    GLES31.glDrawArrays(GLES31.GL_TRIANGLE_STRIP, 0, 4)
    glCheckError(TAG, "glDrawArrays")

    unbindTexture()
  }

}