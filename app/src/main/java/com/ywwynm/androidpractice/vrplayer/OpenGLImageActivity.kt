package com.ywwynm.androidpractice.vrplayer

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.opengl.*
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.ywwynm.androidpractice.R
import com.ywwynm.androidpractice.vrplayer.testforunity.Texture2D
import com.ywwynm.androidpractice.vrplayer.testforunity.TextureTransfer
import com.ywwynm.androidpractice.vrplayer.utils.glCheckError
import kotlinx.android.synthetic.main.activity_triangle_opengl.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class OpenGLImageActivity : AppCompatActivity() {

  val TAG = "OpenGLImageActivity"

  private lateinit var mRenderer: MyGLRenderer

//  private val vertexShaderSrc = ShaderUtils.readRawTextFile(R.raw.image_vertex_shader)
//  private val fragShaderSrc = ShaderUtils.readRawTextFile(R.raw.image_fragment_shader)
//
//  private var programId = -1
//
//  private var projectionMatrix = FloatArray(16)
//  private var uMatHandle = -1
//  private var aPosHandle = -1
//
//  private var vertexData = floatArrayOf(
//       0.0f,  0.0f, 0.0f,
//       1.0f,  1.0f, 0.0f,
//      -1.0f,  1.0f, 0.0f,
//      -1.0f, -1.0f, 0.0f,
//       1.0f, -1.0f, 0.0f
//  )
//  private lateinit var vertexBuffer: FloatBuffer
//
//  private var vertexIndexData = shortArrayOf(
//      0, 1, 2,
//      0, 2, 3,
//      0, 3, 4,
//      0, 4, 1
//  )
//  private lateinit var vertexIndexBuffer: ShortBuffer
//
//  private var textureVertexData = floatArrayOf(
//      0.5f, 0.5f,
//      1.0f, 0.0f,
//      0.0f, 0.0f,
//      0.0f, 1.0f,
//      1.0f, 1.0f
//  )
//  private lateinit var textureVertexBuffer: FloatBuffer
//
//  private var uTextureSamplerHandle = 0
//  private var aTextureCoordHandle = 0
//
//  private var textureId = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_image_opengl)

    ActivityCompat.requestPermissions(this,
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)

    gsv.setEGLContextClientVersion(3)

    mRenderer = MyGLRenderer()
    gsv.setRenderer(mRenderer)
    gsv.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
  }

  inner class MyGLRenderer: GLSurfaceView.Renderer {

    private lateinit var texture: Texture2D // this is for drawing to another texture through FBO
    private lateinit var textureTransfer: TextureTransfer
    private lateinit var texture2: Texture2D // this is for real drawing

    private var textureWidth = 0
    private var textureHeight = 0

    private var logged = false

    override fun onDrawFrame(gl: GL10?) {
      textureTransfer.fboStart()
      texture.draw()

      if (!logged) {
        val pixelsBuffer = ByteBuffer.allocateDirect(textureWidth * textureHeight * 4)
            .order(ByteOrder.nativeOrder())
            .asIntBuffer()
        GLES30.glReadPixels(0, 0, textureWidth, textureHeight,
            GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, pixelsBuffer.clear())
        glCheckError(TAG, "glReadPixels")
        val bitmap2 = Bitmap.createBitmap(textureWidth, textureHeight, Bitmap.Config.ARGB_8888)
        bitmap2.copyPixelsFromBuffer(pixelsBuffer)
        logFrameBufferPixel(bitmap2, 0, 0)
        logFrameBufferPixel(bitmap2, textureWidth / 2, textureHeight / 2)
        logFrameBufferPixel(bitmap2, textureWidth / 3, textureHeight / 3)
        logFrameBufferPixel(bitmap2, textureWidth * 3 / 4, textureHeight * 3 / 4)
        logFrameBufferPixel(bitmap2, textureWidth - 1, textureHeight - 1)
        bitmap2.recycle()
        logged = true
      }

      textureTransfer.fboEnd()

      texture2.draw()
//      GLES31.glClear(GLES31.GL_DEPTH_BUFFER_BIT or GLES31.GL_COLOR_BUFFER_BIT)
//      GLES31.glUseProgram(programId)
//      GLES31.glUniformMatrix4fv(uMatHandle, 1, false, projectionMatrix, 0);
//      GLES31.glEnableVertexAttribArray(aPosHandle)
//      GLES31.glVertexAttribPointer(aPosHandle, 3, GLES31.GL_FLOAT, false,
//          12, vertexBuffer);
//
//      GLES31.glEnableVertexAttribArray(aTextureCoordHandle)
//      GLES31.glVertexAttribPointer(aTextureCoordHandle, 2, GLES31.GL_FLOAT, false,
//          8, textureVertexBuffer)
//      GLES31.glActiveTexture(GLES31.GL_TEXTURE0)
//      GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureId)
//
//      GLES31.glUniform1i(uTextureSamplerHandle, 0)
//
////      GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, 3);
//      GLES31.glDrawElements(GLES31.GL_TRIANGLES, vertexIndexData.size, GLES31.GL_UNSIGNED_SHORT, vertexIndexBuffer)
    }

    private fun logFrameBufferPixel(bitmap: Bitmap, x: Int, y: Int) {
      val pixel = bitmap.getPixel(x, y)
      val red = Color.red(pixel)
      val green = Color.green(pixel)
      val blue = Color.blue(pixel)
      val alpha = Color.alpha(pixel)
      Log.i(TAG, "pixel value at (" + x + ", " + y + "): " +
          "R(" + red + ") G(" + green + ") B(" + blue + ") A(" + alpha + ")")
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
      textureWidth = width
      textureHeight = height
      textureTransfer.tryToInitTempTexture2D(texture2, width, height)
//      val ratio = if (width > height) {
//        width / height.toFloat()
//      } else {
//        height / width.toFloat()
//      }
//      if (width > height) {
//        Matrix.orthoM(texture.projectionMatrix, 0, -ratio, ratio, -1f, 1f, -1f, 1f)
//      } else {
//        Matrix.orthoM(texture.projectionMatrix, 0, -1f, 1f, -ratio, ratio, -1f, 1f);
//      }
//      GLES31.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
      GLES31.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)

//      val vertexShader = ShaderUtils.compileShader(GLES31.GL_VERTEX_SHADER, vertexShaderSrc)
//      val fragmentShader = ShaderUtils.compileShader(GLES31.GL_FRAGMENT_SHADER, fragShaderSrc)
//      programId = ShaderUtils.createProgram(intArrayOf(vertexShader, fragmentShader))
//      Log.i(TAG, "vertexShader: $vertexShader, fragmentShader: $fragmentShader, programId: $programId")
//
//      uMatHandle = GLES31.glGetUniformLocation(programId, "uMatrix")
//      aPosHandle = GLES31.glGetAttribLocation(programId, "aPosition")
//
//      vertexBuffer = ByteBuffer.allocateDirect(vertexData.size * 4)
//          .order(ByteOrder.nativeOrder())
//          .asFloatBuffer()
//          .put(vertexData)
//      vertexBuffer.position(0)
//
//      vertexIndexBuffer = ByteBuffer.allocateDirect(vertexIndexData.size * 2)
//          .order(ByteOrder.nativeOrder())
//          .asShortBuffer()
//          .put(vertexIndexData)
//      vertexIndexBuffer.position(0)
//
//      textureVertexBuffer = ByteBuffer.allocateDirect(textureVertexData.size * 4)
//          .order(ByteOrder.nativeOrder())
//          .asFloatBuffer()
//          .put(textureVertexData)
//      textureVertexBuffer.position(0)
//
//      uTextureSamplerHandle = GLES31.glGetUniformLocation(programId, "uTexture")
//      aTextureCoordHandle = GLES31.glGetAttribLocation(programId, "aTexCoord")
//
//      val bitmapOptions = BitmapFactory.Options()
//      bitmapOptions.inScaled = false
//      val bitmap = BitmapFactory.decodeFile(
//          Environment.getExternalStorageDirectory().absolutePath + "/blue.jpg")
//      textureId = TextureUtils.loadTexture(bitmap)

      texture = Texture2D()
      texture.init(R.raw.image_vertex_shader, R.raw.image_fragment_shader)

      textureTransfer = TextureTransfer()
      textureTransfer.tryToCreateFBO()

      texture2 = Texture2D()
      texture2.init(R.raw.image_vertex_shader, R.raw.image_fragment_shader)

      texture.bindTexture()
//      val textureId = texture.textureID
      val bitmap = BitmapFactory.decodeFile(
          Environment.getExternalStorageDirectory().absolutePath + "/blue.jpg")
      GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_LINEAR_MIPMAP_LINEAR)
      GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_LINEAR)
      GLUtils.texImage2D(GLES31.GL_TEXTURE_2D, 0, bitmap, 0)
      GLES31.glGenerateMipmap(GLES31.GL_TEXTURE_2D)
      texture.unbindTexture()
    }

  }
}
