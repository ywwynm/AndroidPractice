package com.ywwynm.androidpractice.vrplayer

import android.Manifest
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.opengl.*
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Surface
import com.ywwynm.androidpractice.R
import com.ywwynm.androidpractice.vrplayer.utils.ShaderUtils
import kotlinx.android.synthetic.main.activity_video_opengl.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10



class OpenGLVideoActivity : AppCompatActivity() {

  val TAG = "OpenGLVideoActivity"

  private lateinit var mRenderer: MyGLRenderer

  private val vertexShaderSrc = ShaderUtils.readRawTextFile(R.raw.image_vertex_shader)
  private val fragShaderSrc = ShaderUtils.readRawTextFile(R.raw.image_fragment_shader)

  private var programId = -1

  private var projectionMatrix = FloatArray(16)
  private var uMatHandle = -1
  private var aPosHandle = -1

  private var vertexData = floatArrayOf(
      0.0f,  0.0f, 0.0f,
      1.0f,  1.0f, 0.0f,
      -1.0f,  1.0f, 0.0f,
      -1.0f, -1.0f, 0.0f,
      1.0f, -1.0f, 0.0f
  )
  private lateinit var vertexBuffer: FloatBuffer

  private var vertexIndexData = shortArrayOf(
      0, 1, 2,
      0, 2, 3,
      0, 3, 4,
      0, 4, 1
  )
  private lateinit var vertexIndexBuffer: ShortBuffer

  private var textureVertexData = floatArrayOf(
      0.5f, 0.5f,
      1.0f, 0.0f,
      0.0f, 0.0f,
      0.0f, 1.0f,
      1.0f, 1.0f
  )
  private lateinit var textureVertexBuffer: FloatBuffer

  private var uTextureSamplerHandle = 0
  private var aTextureCoordHandle = 0

  private var textureId = 0

  private lateinit var surfaceTexture: SurfaceTexture
  private lateinit var mediaPlayer: MediaPlayer
  private var updateSurface = false

  private var stMatrix = FloatArray(16)
  private var uSTMatrixHandle = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_video_opengl)

    ActivityCompat.requestPermissions(this,
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)

    mediaPlayer = MediaPlayer()

    gsv.setEGLContextClientVersion(3)

    mRenderer = MyGLRenderer()
    gsv.setRenderer(mRenderer)
    gsv.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
  }

  inner class MyGLRenderer: GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
      updateSurface = true
    }

    override fun onDrawFrame(gl: GL10?) {
      synchronized(this) {
        if (updateSurface) {
          surfaceTexture.updateTexImage()
          surfaceTexture.getTransformMatrix(stMatrix)
          updateSurface = false
        }
      }

      GLES31.glClear(GLES31.GL_DEPTH_BUFFER_BIT or GLES31.GL_COLOR_BUFFER_BIT)
      GLES31.glUseProgram(programId)
      GLES31.glUniformMatrix4fv(uMatHandle, 1, false, projectionMatrix, 0);
      GLES31.glEnableVertexAttribArray(aPosHandle)
      GLES31.glVertexAttribPointer(aPosHandle, 3, GLES31.GL_FLOAT, false,
          12, vertexBuffer);

      GLES31.glEnableVertexAttribArray(aTextureCoordHandle)
      GLES31.glVertexAttribPointer(aTextureCoordHandle, 2, GLES31.GL_FLOAT, false,
          8, textureVertexBuffer)
      GLES31.glActiveTexture(GLES31.GL_TEXTURE0)
      GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureId)

      GLES31.glUniform1i(uTextureSamplerHandle, 0)

//      GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, 3);
      GLES31.glDrawElements(GLES31.GL_TRIANGLES, vertexIndexData.size, GLES31.GL_UNSIGNED_SHORT, vertexIndexBuffer)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
      val ratio = if (width > height) {
        width / height.toFloat()
      } else {
        height / width.toFloat()
      }
      if (width > height) {
        Matrix.orthoM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, -1f, 1f)
      } else {
        Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -ratio, ratio, -1f, 1f);
      }
//      GLES31.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
      GLES31.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)

      val vertexShader = ShaderUtils.compileShader(GLES31.GL_VERTEX_SHADER, vertexShaderSrc)
      val fragmentShader = ShaderUtils.compileShader(GLES31.GL_FRAGMENT_SHADER, fragShaderSrc)
      programId = ShaderUtils.createProgram(intArrayOf(vertexShader, fragmentShader))
      Log.i(TAG, "vertexShader: $vertexShader, fragmentShader: $fragmentShader, programId: $programId")

      uMatHandle = GLES31.glGetUniformLocation(programId, "uMatrix")
      aPosHandle = GLES31.glGetAttribLocation(programId, "aPosition")

      vertexBuffer = ByteBuffer.allocateDirect(vertexData.size * 4)
          .order(ByteOrder.nativeOrder())
          .asFloatBuffer()
          .put(vertexData)
      vertexBuffer.position(0)

      vertexIndexBuffer = ByteBuffer.allocateDirect(vertexIndexData.size * 2)
          .order(ByteOrder.nativeOrder())
          .asShortBuffer()
          .put(vertexIndexData)
      vertexIndexBuffer.position(0)

      textureVertexBuffer = ByteBuffer.allocateDirect(textureVertexData.size * 4)
          .order(ByteOrder.nativeOrder())
          .asFloatBuffer()
          .put(textureVertexData)
      textureVertexBuffer.position(0)

      uTextureSamplerHandle = GLES31.glGetUniformLocation(programId, "uTexture")
      aTextureCoordHandle = GLES31.glGetAttribLocation(programId, "aTexCoord")

//      val bitmapOptions = BitmapFactory.Options()
//      bitmapOptions.inScaled = false
//      val bitmap = BitmapFactory.decodeFile(
//          Environment.getExternalStorageDirectory().absolutePath + "/blue.jpg")
//      textureId = TextureUtils.loadTexture(bitmap)

      uSTMatrixHandle = GLES31.glGetUniformLocation(programId, "uSTMatrix")
      GLES31.glUniformMatrix4fv(uSTMatrixHandle, 1, false, stMatrix, 0)

      val textures = IntArray(1)
      GLES20.glGenTextures(1, textures, 0)

      textureId = textures[0]
      GLES31.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)

      GLES31.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES31.GL_TEXTURE_MIN_FILTER,
          GLES31.GL_NEAREST.toFloat())
      GLES31.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES31.GL_TEXTURE_MAG_FILTER,
          GLES31.GL_LINEAR.toFloat())

      surfaceTexture = SurfaceTexture(textureId)
      surfaceTexture.setOnFrameAvailableListener(this)
      val surface = Surface(surfaceTexture)
      mediaPlayer.setSurface(surface)
      surface.release()
    }

  }
}