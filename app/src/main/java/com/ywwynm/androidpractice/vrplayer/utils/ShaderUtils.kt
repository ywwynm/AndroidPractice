package com.ywwynm.androidpractice.vrplayer.utils

import android.opengl.GLES31
import android.util.Log
import com.ywwynm.androidpractice.App
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class ShaderUtils {
  companion object {

    const val TAG = "ShaderUtils"

    fun readRawTextFile(resId: Int): String {
      val inputStream = App.app.resources.openRawResource(resId)
      try {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val sb = StringBuilder()
        var line: String?
        while (true) {
          line = reader.readLine()
          if (line != null) {
            sb.append(line).append("\n")
          } else break
        }
        reader.close()
        return sb.toString()
      } catch (e: IOException) {
        e.printStackTrace()
      }
      return ""
    }

    fun compileShader(shaderType: Int, src: String): Int {
      var shader = GLES31.glCreateShader(shaderType)
      if (shader != 0) {
        GLES31.glShaderSource(shader, src)
        GLES31.glCompileShader(shader)
        val compiled = IntArray(1)
        GLES31.glGetShaderiv(shader, GLES31.GL_COMPILE_STATUS, compiled, 0)
        if (compiled[0] == 0) {
          Log.e(TAG, "Could not compile shader $shaderType:");
          Log.e(TAG, GLES31.glGetShaderInfoLog(shader));
          GLES31.glDeleteShader(shader);
          shader = 0
        }
      }
      return shader
    }

    fun createProgram(shaders: IntArray): Int {
      var program = GLES31.glCreateProgram()
      if (program != 0) {
        for (shader in shaders) {
          GLES31.glAttachShader(program, shader)
        }
        GLES31.glLinkProgram(program)
        val linkStatus = IntArray(1)
        GLES31.glGetProgramiv(program, GLES31.GL_LINK_STATUS, linkStatus, 0)
        if (linkStatus[0] != GLES31.GL_TRUE) {
          Log.e(TAG, "Could not link program: ")
          Log.e(TAG, GLES31.glGetProgramInfoLog(program))
          GLES31.glDeleteProgram(program)
          program = 0
        }
      }
      return program
    }
  }
}