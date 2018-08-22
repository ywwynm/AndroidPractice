package com.ywwynm.androidpractice.vrplayer

import android.Manifest
import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.opengl.GLES31
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Surface
import com.ywwynm.androidpractice.R
import com.ywwynm.androidpractice.vrplayer.texture.OESTexture2D
import com.ywwynm.androidpractice.vrplayer.texture.Texture2D
import com.ywwynm.androidpractice.vrplayer.utils.TextureTransfer
import com.ywwynm.androidpractice.vrplayer.utils.glCheckError
import kotlinx.android.synthetic.main.activity_video_opengl.*
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10



class OpenGLVideoActivity : AppCompatActivity() {

  val TAG = "OpenGLVideoActivity"

  private lateinit var mRenderer: MyGLRenderer

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_video_opengl)

    ActivityCompat.requestPermissions(this,
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)

    gsv.setEGLContextClientVersion(3)

    mRenderer = MyGLRenderer(Environment.getExternalStorageDirectory().absolutePath + "/vr_video/VR_1.mp4")
    gsv.setRenderer(mRenderer)
    gsv.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
  }

  override fun onDestroy() {
    super.onDestroy()
    mRenderer.onDestroy()
  }

  override fun onPause() {
    super.onPause()
    gsv.onPause()
    mRenderer.onPause()
  }

  override fun onResume() {
    super.onResume()
    gsv.onResume()
    mRenderer.onResume()
  }

//  inner class MyGLRenderer(videoPath: String): GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener, MediaPlayer.OnPreparedListener {
//
//    private lateinit var surfaceTexture: SurfaceTexture
//    var mediaPlayer: MediaPlayer = MediaPlayer()
//    private var playerPrepared = false
//
//    private val vertexShaderSrc = ShaderUtils.readRawTextFile(R.raw.video_vertex_shader)
//    private val fragShaderSrc = ShaderUtils.readRawTextFile(R.raw.video_oes_fragment_shader)
//
//    private var programId = -1
//
//    private var projectionMatrix = FloatArray(16)
//    private var uMatHandle = -1
//    private var aPosHandle = -1
//
//    private var vertexData = floatArrayOf(
//        1.0f,  1.0f, 0.0f,
//        -1.0f,  1.0f, 0.0f,
//        1.0f, -1.0f, 0.0f,
//        -1.0f, -1.0f, 0.0f
//    )
//    private var vertexBuffer: FloatBuffer
//
//    private var textureVertexData = floatArrayOf(
//        1.0f, 0.0f,
//        0.0f, 0.0f,
//        1.0f, 1.0f,
//        0.0f, 1.0f
//    )
//    private var textureVertexBuffer: FloatBuffer
//
//    private var uTextureSamplerHandle = 0
//    private var aTextureCoordHandle = 0
//
//    private var textureId = 0
//
//    private var updateSurface = false
//
//    private var stMatrix = FloatArray(16)
//    private var uSTMatrixHandle = 0
//
//    private var screenWidth = 0
//    private var screenHeight = 0
//
//    init {
//      Log.i(TAG, "video path: $videoPath")
//      mediaPlayer.setDataSource(videoPath)
//      mediaPlayer.setAudioAttributes(
//          AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
//      mediaPlayer.isLooping = true
//      mediaPlayer.setOnPreparedListener(this)
//      Log.i(TAG, "player setOnPreparedListener")
//
//      vertexBuffer = ByteBuffer.allocateDirect(vertexData.size * 4)
//          .order(ByteOrder.nativeOrder())
//          .asFloatBuffer()
//          .put(vertexData)
//      vertexBuffer.position(0)
//
//      textureVertexBuffer = ByteBuffer.allocateDirect(textureVertexData.size * 4)
//          .order(ByteOrder.nativeOrder())
//          .asFloatBuffer()
//          .put(textureVertexData)
//      textureVertexBuffer.position(0)
//    }
//
//    private var shouldResumeFromPause = false
//    fun onPause() {
//      if (playerPrepared) {
//        mediaPlayer.pause()
//        shouldResumeFromPause = true
//      }
//    }
//
//    fun onDestroy() {
//      mediaPlayer.release()
//    }
//
//    fun onResume() {
//      if (playerPrepared && shouldResumeFromPause) {
//        mediaPlayer.start()
//        shouldResumeFromPause = false
//      }
//    }
//
//    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
//      updateSurface = true
//    }
//
//    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
//      GLES31.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
//
//      val vertexShader = ShaderUtils.compileShader(GLES31.GL_VERTEX_SHADER, vertexShaderSrc)
//      val fragmentShader = ShaderUtils.compileShader(GLES31.GL_FRAGMENT_SHADER, fragShaderSrc)
//      programId = ShaderUtils.createProgram(intArrayOf(vertexShader, fragmentShader))
//      Log.i(TAG, "vertexShader: $vertexShader, fragmentShader: $fragmentShader, programId: $programId")
//
//      uMatHandle = GLES31.glGetUniformLocation(programId, "uMatrix")
//      aPosHandle = GLES31.glGetAttribLocation(programId, "aPosition")
//      uTextureSamplerHandle = GLES31.glGetUniformLocation(programId, "uTexture")
//      aTextureCoordHandle = GLES31.glGetAttribLocation(programId, "aTexCoord")
//      uSTMatrixHandle = GLES31.glGetUniformLocation(programId, "uSTMatrix")
//
//      val textures = IntArray(1)
//      GLES20.glGenTextures(1, textures, 0)
//
//      textureId = textures[0]
//      GLES31.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)
//      Log.i(TAG, "textureId bound: $textureId")
//
//      GLES31.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES31.GL_TEXTURE_MIN_FILTER,
//          GLES31.GL_NEAREST.toFloat())
//      GLES31.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES31.GL_TEXTURE_MAG_FILTER,
//          GLES31.GL_LINEAR.toFloat())
//
//      surfaceTexture = SurfaceTexture(textureId)
//      surfaceTexture.setOnFrameAvailableListener(this)
//      val surface = Surface(surfaceTexture)
//      mediaPlayer.setSurface(surface)
//      surface.release()
//
//      runOnUiThread {
//        if (!playerPrepared) {
//          try {
//            Log.i(TAG, "Prepared to start playing")
//            mediaPlayer.prepareAsync()
//          } catch (_: IOException) {
//            Log.e(TAG, "player prepare failed")
//          }
//        }
//      }
//    }
//
//    override fun onPrepared(mp: MediaPlayer?) {
//      playerPrepared = true
//      Log.i(TAG, "player prepared, now it is playing!")
//      mp?.start()
//    }
//
//    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
//      screenWidth = width
//      screenHeight = height
//      val ratio = if (width > height) {
//        width / height.toFloat()
//      } else {
//        height / width.toFloat()
//      }
//      if (width > height) {
//        Matrix.orthoM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, -1f, 1f)
//      } else {
//        Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -ratio, ratio, -1f, 1f);
//      }
////      GLES31.glViewport(0, 0, width, height)
//    }
//
//    override fun onDrawFrame(gl: GL10?) {
//      Log.i(TAG, "onDrawFrame")
//      GLES31.glClear(GLES31.GL_DEPTH_BUFFER_BIT or GLES31.GL_COLOR_BUFFER_BIT)
//
//      synchronized(this) {
//        if (updateSurface) {
//          Log.i(TAG, "updateSurface")
//          surfaceTexture.updateTexImage()
//          surfaceTexture.getTransformMatrix(stMatrix)
//          updateSurface = false
//        }
//      }
//
//      Log.i(TAG, "really drawing")
//      GLES31.glUseProgram(programId)
//      GLES31.glUniformMatrix4fv(uMatHandle, 1, false, projectionMatrix, 0);
//      GLES31.glUniformMatrix4fv(uSTMatrixHandle, 1, false, stMatrix, 0)
//
//      vertexBuffer.position(0)
//      GLES31.glEnableVertexAttribArray(aPosHandle)
//      GLES31.glVertexAttribPointer(aPosHandle, 3, GLES31.GL_FLOAT, false,
//          12, vertexBuffer);
//
//      textureVertexBuffer.position(0)
//      GLES31.glEnableVertexAttribArray(aTextureCoordHandle)
//      GLES31.glVertexAttribPointer(aTextureCoordHandle, 2, GLES31.GL_FLOAT, false,
//          8, textureVertexBuffer)
//
//      GLES31.glActiveTexture(GLES31.GL_TEXTURE0)
//      GLES31.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)
//
//      GLES31.glUniform1i(uTextureSamplerHandle, 0)
//      GLES31.glViewport(0, 0, screenWidth, screenHeight)
//      GLES31.glDrawArrays(GLES31.GL_TRIANGLE_STRIP, 0, 4)
//
////      GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, 3);
//    }
//
//  }

  inner class MyGLRenderer(videoPath: String): GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener, MediaPlayer.OnPreparedListener {

    private lateinit var surfaceTexture: SurfaceTexture
    var mediaPlayer: MediaPlayer = MediaPlayer()
    private var playerPrepared = false

    private var updateSurface = false

    private var screenWidth = 0
    private var screenHeight = 0

    private lateinit var oesTexture2D: OESTexture2D
    private lateinit var texture2D: Texture2D

    private var pixelsBuffer: IntBuffer? = null

    init {
      Log.i(TAG, "video path: $videoPath")
      mediaPlayer.setDataSource(videoPath)
      mediaPlayer.setAudioAttributes(
          AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
      mediaPlayer.isLooping = true
      mediaPlayer.setOnPreparedListener(this)
      Log.i(TAG, "player setOnPreparedListener")
    }

    private var shouldResumeFromPause = false
    fun onPause() {
      if (playerPrepared) {
        mediaPlayer.pause()
        shouldResumeFromPause = true
      }
    }

    fun onDestroy() {
      mediaPlayer.release()
    }

    fun onResume() {
      if (playerPrepared && shouldResumeFromPause) {
        mediaPlayer.start()
        shouldResumeFromPause = false
      }
    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
      updateSurface = true
    }

    private val textureTransfer = TextureTransfer(TAG) // test transferring OES texture to normal 2D texture

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
      GLES31.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)

      oesTexture2D = OESTexture2D()
      texture2D = Texture2D()

      texture2D.init()
      oesTexture2D.init()

      surfaceTexture = SurfaceTexture(oesTexture2D.textureId)
      surfaceTexture.setOnFrameAvailableListener(this)
      val surface = Surface(surfaceTexture)
      mediaPlayer.setSurface(surface)
      surface.release()

      textureTransfer.tryToCreateFBO()

      runOnUiThread {
        if (!playerPrepared) {
          try {
            Log.i(TAG, "Prepared to start playing")
            mediaPlayer.prepareAsync()
          } catch (_: IOException) {
            Log.e(TAG, "player prepare failed")
          }
        }
      }
    }

    override fun onPrepared(mp: MediaPlayer?) {
      playerPrepared = true
      Log.i(TAG, "player prepared, now it is playing!")
      mp?.start()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
      Log.i(TAG, "onSurfaceChanged called, width: $width, height: $height")
      textureTransfer.tryToCreateTempTexture2D(width, height)
      texture2D.textureId = textureTransfer.tempTexture2DId
      screenWidth = width
      screenHeight = height
      pixelsBuffer = ByteBuffer.allocateDirect(width * height * 4)
          .order(ByteOrder.nativeOrder())
          .asIntBuffer()

      val ratio = if (width > height) {
        width / height.toFloat()
      } else {
        height / width.toFloat()
      }
      if (width > height) {
        Matrix.orthoM(texture2D.projectionMatrix, 0, -ratio, ratio, -1f, 1f, -1f, 1f)
        Matrix.orthoM(oesTexture2D.projectionMatrix, 0, -ratio, ratio, -1f, 1f, -1f, 1f)
      } else {
        Matrix.orthoM(texture2D.projectionMatrix, 0, -1f, 1f, -ratio, ratio, -1f, 1f)
        Matrix.orthoM(oesTexture2D.projectionMatrix, 0, -1f, 1f, -ratio, ratio, -1f, 1f)
      }
//      GLES31.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
      GLES31.glClear(GLES31.GL_DEPTH_BUFFER_BIT or GLES31.GL_COLOR_BUFFER_BIT)

      synchronized(this) {
        if (updateSurface) {
          surfaceTexture.updateTexImage()
          surfaceTexture.getTransformMatrix(texture2D.stMatrix)
          surfaceTexture.getTransformMatrix(oesTexture2D.stMatrix)
          updateSurface = false
        }
      }

      textureTransfer.fboStart()
      Log.i(TAG, "oesTexture2D.draw")
      oesTexture2D.draw(screenWidth, screenHeight)
      GLES31.glReadPixels(0, 0, screenWidth, screenHeight,
          GLES31.GL_RGBA, GLES31.GL_UNSIGNED_BYTE, pixelsBuffer!!.clear())
      val bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888)
      bitmap.copyPixelsFromBuffer(pixelsBuffer)
      Log.i(TAG, "bitmap center: ${bitmap.getPixel(540, 960)}")
      glCheckError(TAG, "glReadPixels")
      textureTransfer.fboEnd()

      Log.i(TAG, "texture2D.draw")
      texture2D.draw(screenWidth, screenHeight)

//      GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, 3);
    }

//    private fun doDraw(useOES: Boolean = true) {
//      GLES31.glUseProgram(programId)
//      GLES31.glUniformMatrix4fv(uMatHandle, 1, false, projectionMatrix, 0);
//      GLES31.glUniformMatrix4fv(uSTMatrixHandle, 1, false, stMatrix, 0)
//
//      vertexBuffer.position(0)
//      GLES31.glEnableVertexAttribArray(aPosHandle)
//      GLES31.glVertexAttribPointer(aPosHandle, 3, GLES31.GL_FLOAT, false,
//          12, vertexBuffer);
//
//      textureVertexBuffer.position(0)
//      GLES31.glEnableVertexAttribArray(aTextureCoordHandle)
//      GLES31.glVertexAttribPointer(aTextureCoordHandle, 2, GLES31.GL_FLOAT, false,
//          8, textureVertexBuffer)
//
//      if (useOES) {
//        GLES31.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, oesTextureId)
//      } else {
//        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureTransfer.tempTexture2DId)
//      }
//
//      GLES31.glUniform1i(uTextureSamplerHandle, 0)
//      GLES31.glViewport(0, 0, screenWidth, screenHeight)
//      GLES31.glDrawArrays(GLES31.GL_TRIANGLE_STRIP, 0, 4)
//
//      if (useOES) {
//        GLES31.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)
//      } else {
//        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, 0)
//      }
//    }

  }
}
