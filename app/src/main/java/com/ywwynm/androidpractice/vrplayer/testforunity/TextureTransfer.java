package com.ywwynm.androidpractice.vrplayer.testforunity;

import android.opengl.GLES31;
import android.util.Log;

import static com.ywwynm.androidpractice.vrplayer.utils.GLUtilsKt.glCheckError;

/**
 * Created by ywwynm on 2018/8/15.
 * Transfer Android specific texture_external_oes to universal texture_2d.
 */
public class TextureTransfer {

  private static final String TAG = "TextureTransfer";

  private int mFBOId = -1;
  private int mTexture2DId = -1;

  public void tryToCreateFBO() {
    if (mFBOId != -1) {
      return;
    }
    int[] idContainer = new int[1];
    GLES31.glGenFramebuffers(1, idContainer, 0);
    mFBOId = idContainer[0];
    glCheckError(TAG, "glGenFramebuffers");
    Log.i(TAG, "fbo generated: " + mFBOId);
  }

  public void tryToInitTempTexture2D(Texture2D texture2D, int textureWidth, int textureHeight) {
    if (mTexture2DId != -1) {
      return;
    }

    mTexture2DId = texture2D.getTextureID();
    GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, mTexture2DId);
    Log.i(TAG, "glBindTexture " + mTexture2DId + " to init for FBO");
    glCheckError(TAG, "glBindTexture " + mTexture2DId);

    // make 2D texture empty
    GLES31.glTexImage2D(GLES31.GL_TEXTURE_2D, 0, GLES31.GL_RGBA, textureWidth, textureHeight, 0,
        GLES31.GL_RGBA, GLES31.GL_UNSIGNED_BYTE, null);
    Log.i(TAG, "glTexImage2D, textureWidth: " + textureWidth + ", textureHeight: " + textureHeight);
    glCheckError(TAG, "glTexImage2D");

    GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, 0);
    glCheckError(TAG, "glBindTexture 0");

    fboStart();
    GLES31.glFramebufferTexture2D(GLES31.GL_FRAMEBUFFER, GLES31.GL_COLOR_ATTACHMENT0,
        GLES31.GL_TEXTURE_2D, mTexture2DId, 0);
    Log.i(TAG, "glFramebufferTexture2D");
    glCheckError(TAG, "glFramebufferTexture2D " + mTexture2DId);
    int fboStatus = GLES31.glCheckFramebufferStatus(GLES31.GL_FRAMEBUFFER);
    Log.i(TAG, "fbo status: " + fboStatus);
    if (fboStatus != GLES31.GL_FRAMEBUFFER_COMPLETE) {
      throw new RuntimeException("framebuffer " + mFBOId + " incomplete!");
    }
    fboEnd();
  }

  public void fboStart() {
    GLES31.glBindFramebuffer(GLES31.GL_FRAMEBUFFER, mFBOId);
    glCheckError(TAG, "glBindFramebuffer " + mFBOId);

//    GLES31.glBindBuffer(GLES31.GL_ELEMENT_ARRAY_BUFFER, 0);
//    GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, 0);
  }

  public void fboEnd() {
    GLES31.glBindFramebuffer(GLES31.GL_FRAMEBUFFER, 0);
    glCheckError(TAG, "glBindFramebuffer 0");
  }
}
