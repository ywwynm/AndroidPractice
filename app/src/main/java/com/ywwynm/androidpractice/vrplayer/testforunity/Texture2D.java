package com.ywwynm.androidpractice.vrplayer.testforunity;

import android.opengl.GLES31;
import android.util.Log;

import com.ywwynm.androidpractice.R;
import com.ywwynm.androidpractice.vrplayer.utils.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static com.ywwynm.androidpractice.vrplayer.utils.GLUtilsKt.glCheckError;

/**
 * Created by ywwynm on 2018/8/15.
 */
public class Texture2D {

  protected String TAG = "Texture2D";

  protected int textureId;

  protected String vertexShaderSrc;
  protected String fragShaderSrc;

  protected int programId;

  protected int aPosHandle;
//  protected int uMatHandle;
  protected int aTexCoordHandle;
  protected int uTexHandle;
//  protected int uSTMatrixHandle;

  protected float[] projectionMatrix = new float[16];
//  protected float[] stMatrix = new float[16];

  protected float vertexData[] = {
//      -1f, 1f, 0.0f,
//      -1f, -1f, 0.0f,
//      1f, -1f, 0.0f,
//      1f, 1f, 0.0f
      1.0f,  1.0f, 0.0f,
      -1.0f,  1.0f, 0.0f,
      1.0f, -1.0f, 0.0f,
      -1.0f, -1.0f, 0.0f
  };
  protected FloatBuffer vertexBuffer;

  protected float[] textureVertexData = new float[] {
//      0.0f, 0.0f, // top left (V2)
//      0.0f, 1.0f, // bottom left (V1)
//      1.0f, 1.0f, // top right (V4)
//      1.0f, 0.0f // bottom right (V3)
      1.0f, 0.0f,
      0.0f, 0.0f,
      1.0f, 1.0f,
      0.0f, 1.0f
  };

  protected FloatBuffer textureVertexBuffer;

  public void init() {
    initBuffers();
    loadShadersSrc();
    createProgram();
    findHandles();
    initTexture();
  }

  protected void loadShadersSrc() {
    vertexShaderSrc = ShaderUtils.Companion.readRawTextFile(R.raw.video_vertex_shader);
    fragShaderSrc = ShaderUtils.Companion.readRawTextFile(R.raw.video_fragment_shader);
  }

  protected void createProgram() {
    int vertexShader = ShaderUtils.Companion.compileShader(GLES31.GL_VERTEX_SHADER, vertexShaderSrc);
    int fragmentShader = ShaderUtils.Companion.compileShader(GLES31.GL_FRAGMENT_SHADER, fragShaderSrc);
    programId = ShaderUtils.Companion.createProgram(new int[] {vertexShader, fragmentShader});
    Log.i(TAG, "programId: " + programId);
  }

  protected void findHandles() {
    aPosHandle = GLES31.glGetAttribLocation(programId, "aPosition");
    glCheckError(TAG, "glGetAttribLocation aPosition");
    Log.i(TAG, "aPosHandle: " + aPosHandle);

//    uMatHandle = GLES31.glGetUniformLocation(programId, "uMatrix");
//    glCheckError(TAG, "glGetUniformLocation uMatrix");
//    Log.i(TAG, "uMatHandle: " + uMatHandle);

    aTexCoordHandle = GLES31.glGetAttribLocation(programId, "aTexCoord");
    glCheckError(TAG, "glGetAttribLocation aTexCoord");
    Log.i(TAG, "aTexCoordHandle: " + aTexCoordHandle);

    uTexHandle = GLES31.glGetUniformLocation(programId, "uTexture");
    glCheckError(TAG, "glGetUniformLocation uTexture");
    Log.i(TAG, "uTexHandle: " + uTexHandle);

//    uSTMatrixHandle = GLES31.glGetUniformLocation(programId, "uSTMatrix");
//    glCheckError(TAG, "glGetUniformLocation uSTMatrixHandle");
//    Log.i(TAG, "uSTMatrixHandle: " + uSTMatrixHandle);
  }

  protected void initBuffers() {
    // initialize vertex byte buffer for shape coordinates
    vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()
        .put(vertexData);
    vertexBuffer.position(0);

    textureVertexBuffer = ByteBuffer.allocateDirect(textureVertexData.length * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()
        .put(textureVertexData);
    textureVertexBuffer.position(0);
  }

  public void initTexture() {
    int[] idContainer = new int[1];
    GLES31.glGenTextures(1, idContainer, 0);
    textureId = idContainer[0];
    Log.i(TAG, "texture2D generated: " + textureId);

    bindTexture();

    GLES31.glTexParameterf(GLES31.GL_TEXTURE_2D,
        GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_NEAREST);
    GLES31.glTexParameterf(GLES31.GL_TEXTURE_2D,
        GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_LINEAR);
    GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D,
        GLES31.GL_TEXTURE_WRAP_S, GLES31.GL_CLAMP_TO_EDGE);
    GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D,
        GLES31.GL_TEXTURE_WRAP_T, GLES31.GL_CLAMP_TO_EDGE);

    unbindTexture();
  }

  protected void bindTexture() {
    GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureId);
    glCheckError(TAG, "glBindTexture " + textureId);
  }

  protected void unbindTexture() {
    GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, 0);
    glCheckError(TAG, "glBindTexture 0");
  }

  public void draw() {
    GLES31.glClearColor(0.0f, 0.0f, 0.2f, 1.0f);
    glCheckError(TAG, "glClearColor");
    GLES31.glClear(GLES31.GL_DEPTH_BUFFER_BIT | GLES31.GL_COLOR_BUFFER_BIT);
    glCheckError(TAG, "glClear");

    GLES31.glUseProgram(programId);

//    GLES31.glUniformMatrix4fv(uMatHandle, 1, false, projectionMatrix, 0);
//    glCheckError(TAG, "glUniformMatrix4fv uMatHandle");

    vertexBuffer.position(0);
    GLES31.glEnableVertexAttribArray(aPosHandle);
    GLES31.glVertexAttribPointer(
        aPosHandle, 3, GLES31.GL_FLOAT, false, 12, vertexBuffer);

    textureVertexBuffer.position(0);
    GLES31.glEnableVertexAttribArray(aTexCoordHandle);
    GLES31.glVertexAttribPointer(
        aTexCoordHandle, 2, GLES31.GL_FLOAT, false, 8, textureVertexBuffer);

    bindTexture();

    GLES31.glUniform1i(uTexHandle, 0);
    glCheckError(TAG, "glUniform1i uTexHandle");
    GLES31.glDrawArrays(GLES31.GL_TRIANGLE_STRIP, 0, 4);
    glCheckError(TAG, "glDrawArrays");

    unbindTexture();
  }

  public int getTextureID() {
    return textureId;
  }

  public float[] getProjectionMatrix() {
    return projectionMatrix;
  }

//  public float[] getStMatrix() {
//    return stMatrix;
//  }
}
