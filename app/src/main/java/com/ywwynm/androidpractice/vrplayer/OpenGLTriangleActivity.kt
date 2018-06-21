package com.ywwynm.androidpractice.vrplayer

import android.opengl.GLES31
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.ywwynm.androidpractice.R
import com.ywwynm.androidpractice.vrplayer.utils.compileShader
import com.ywwynm.androidpractice.vrplayer.utils.createProgram
import com.ywwynm.androidpractice.vrplayer.utils.readRawTextFile
import kotlinx.android.synthetic.main.activity_triangle_opengl.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class OpenGLTriangleActivity : AppCompatActivity() {

  val TAG = "OpenGLTriangleActivity"

  private lateinit var mRenderer: MyGLRenderer

  private val vertexShaderSrc = readRawTextFile(R.raw.triangle_vertex_shader)
  private val fragShaderSrc = readRawTextFile(R.raw.triangle_fragment_shader)

  private var aPosHandle = -1
  private var programId = -1
  private lateinit var vertexBuffer: FloatBuffer

  private var vertexData = floatArrayOf(
      0.0f,  0.0f, 0.0f,
      1.0f, -1.0f, 0.0f,
      1.0f,  1.0f, 0.0f
  )

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_triangle_opengl)

    gsv.setEGLContextClientVersion(3)

    mRenderer = MyGLRenderer()
    gsv.setRenderer(mRenderer)
    gsv.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
  }

  inner class MyGLRenderer: GLSurfaceView.Renderer {
    override fun onDrawFrame(gl: GL10?) {
      GLES31.glClear(GLES31.GL_DEPTH_BUFFER_BIT or GLES31.GL_COLOR_BUFFER_BIT)
      GLES31.glUseProgram(programId)
      GLES31.glEnableVertexAttribArray(aPosHandle)
      GLES31.glVertexAttribPointer(aPosHandle, 3, GLES31.GL_FLOAT, false,
          12, vertexBuffer);
      GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, 3);
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
      GLES31.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
      GLES31.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

      val vertexShader = compileShader(GLES31.GL_VERTEX_SHADER, vertexShaderSrc)
      val fragmentShader = compileShader(GLES31.GL_FRAGMENT_SHADER, fragShaderSrc)
      programId = createProgram(intArrayOf(vertexShader, fragmentShader))
      Log.i(TAG, "vertexShader: $vertexShader, fragmentShader: $fragmentShader, programId: $programId")

      aPosHandle = GLES31.glGetAttribLocation(programId, "aPosition")
      vertexBuffer = ByteBuffer.allocateDirect(vertexData.size * 4)
          .order(ByteOrder.nativeOrder())
          .asFloatBuffer()
          .put(vertexData)
      vertexBuffer.position(0)
    }

  }
}
