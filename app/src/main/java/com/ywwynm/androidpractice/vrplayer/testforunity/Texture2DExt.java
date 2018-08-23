package com.ywwynm.androidpractice.vrplayer.testforunity;

import android.opengl.GLES11Ext;
import android.opengl.GLES31;
import android.util.Log;

import com.ywwynm.androidpractice.R;
import com.ywwynm.androidpractice.vrplayer.utils.ShaderUtils;

import static com.ywwynm.androidpractice.vrplayer.utils.GLUtilsKt.glCheckError;

/**
 * Created by ywwynm on 2018/8/15.
 */
public class Texture2DExt extends Texture2D {

  protected String TAG = "Texture2DExt";

  public Texture2DExt() {
    super();
  }

  @Override
  public void initTexture() {
    int[] idContainer = new int[1];
    GLES31.glGenTextures(1, idContainer, 0);
    textureId = idContainer[0];
    Log.i(TAG, "texture_external_oes generated: " + textureId);

    bindTexture();

    GLES31.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
        GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_NEAREST);
    GLES31.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
        GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_LINEAR);
    GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D,
        GLES31.GL_TEXTURE_WRAP_S, GLES31.GL_CLAMP_TO_EDGE);
    GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D,
        GLES31.GL_TEXTURE_WRAP_T, GLES31.GL_CLAMP_TO_EDGE);

    unbindTexture();
  }

  @Override
  protected void loadShadersSrc() {
    vertexShaderSrc = ShaderUtils.Companion.readRawTextFile(R.raw.video_vertex_shader);
    fragShaderSrc = ShaderUtils.Companion.readRawTextFile(R.raw.video_oes_fragment_shader);
  }

  @Override
  protected void bindTexture() {
    GLES31.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);
    glCheckError(TAG, "glBindTexture " + textureId);
  }

  @Override
  protected void unbindTexture() {
    GLES31.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
    glCheckError(TAG, "glBindTexture 0");
  }

//  @Override
//  public void draw() {
//    int fboStatus = GLES31.glCheckFramebufferStatus(GLES31.GL_FRAMEBUFFER);
//    Log.i(TAG, "check at draw() on Texture2DExt.java, fbo status: " + fboStatus);
//    if (fboStatus != GLES31.GL_FRAMEBUFFER_COMPLETE) {
//      throw new RuntimeException("framebuffer incomplete!");
//    }
//
//    GLES31.glClearColor(0.0f, 0.0f, 0.2f, 1.0f);
//    glCheckError(TAG, "glClearColor");
//    GLES31.glClear(GLES31.GL_DEPTH_BUFFER_BIT | GLES31.GL_COLOR_BUFFER_BIT);
//    glCheckError(TAG, "glClear");
//
//    GLES31.glUseProgram(programId);
//
//    GLES31.glUniformMatrix4fv(uMatHandle, 1, false, projectionMatrix, 0);
//    glCheckError(TAG, "glUniformMatrix4fv uMatrix");
//
//     // http://forum.unity3d.com/threads/mixing-unity-with-native-opengl-drawing-on-android.134621/
////    GLES31.glBindBuffer(GLES31.GL_ELEMENT_ARRAY_BUFFER, 0);
////    GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, 0);
//
//    vertexBuffer.position(0);
//    GLES31.glEnableVertexAttribArray(aPosHandle);
//    glCheckError(TAG, "glEnableVertexAttribArray aPosHandle");
//    GLES31.glVertexAttribPointer(
//        aPosHandle, 3, GLES31.GL_FLOAT, false, 12, vertexBuffer);
//    glCheckError(TAG, "glVertexAttribPointer aPosHandle");
//
//    textureVertexBuffer.position(0);
//    GLES31.glEnableVertexAttribArray(aTexCoordHandle);
//    glCheckError(TAG, "glEnableVertexAttribArray aTexCoordHandle");
//    GLES31.glVertexAttribPointer(
//        aTexCoordHandle, 2, GLES31.GL_FLOAT, false, 8, textureVertexBuffer);
//    glCheckError(TAG, "glVertexAttribPointer aTexCoordHandle");
//
//    bindTexture();
//
//    GLES31.glUniform1i(uTexHandle, 0);
//    glCheckError(TAG, "glUniform1i uTexHandle");
////    GLES31.glDrawElements(
////        GLES31.GL_TRIANGLES, drawOrders.length,
////        GLES31.GL_UNSIGNED_SHORT, mDrawListBuffer);
//    GLES31.glDrawArrays(GLES31.GL_TRIANGLE_STRIP, 0, 4);
//    glCheckError(TAG, "glDrawArrays");
//
//    unbindTexture();
//  }
}
