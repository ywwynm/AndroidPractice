#include <jni.h>
#include "utils/log.h"
#include "opengl/Context.h"

const char* tag = "native-lib.cpp";
Context context;

extern "C" JNIEXPORT void JNICALL
Java_com_ywwynm_androidpractice_vrplayer_OpenGLTriangleActivity_initNativeContext(
    JNIEnv *env, jobject /* this */) {
  logi(tag, "initNativeContext is called");
  context.init();
}

extern "C" JNIEXPORT void JNICALL
Java_com_ywwynm_androidpractice_vrplayer_OpenGLTriangleActivity_nativeDrawTriangle(
    JNIEnv *env, jobject /* this */) {
  logi(tag, "nativeDrawTriangle is called");
  context.transfer.start_fbo();
  context.triangle.draw();
  context.transfer.end_fbo();
  context.texture2D.draw();
}

extern "C" JNIEXPORT void JNICALL
Java_com_ywwynm_androidpractice_vrplayer_OpenGLTriangleActivity_nativeUpdateViewport(
    JNIEnv *env, jobject /* this */, jint width, jint height) {
  logi(tag, "nativeUpdateViewport is called, width: %d, height: %d", width, height);
  context.update_viewport(width, height);
}




extern "C" JNIEXPORT void JNICALL
Java_com_ywwynm_androidpractice_vrplayer_OpenGLVideoActivity_initNativeContext(
    JNIEnv *env, jobject /* this */) {
  logi(tag, "initNativeContext is called");
  context.init();
}

extern "C" JNIEXPORT jint JNICALL
Java_com_ywwynm_androidpractice_vrplayer_OpenGLVideoActivity_getNativeTextureExtId(
    JNIEnv *env, jobject /* this */) {
  logi(tag, "getNativeTextureExtId is called");
  return context.texture2DExt.get_texture_id();
}

extern "C" JNIEXPORT void JNICALL
Java_com_ywwynm_androidpractice_vrplayer_OpenGLVideoActivity_nativeDrawVideo(
    JNIEnv *env, jobject /* this */) {
  logi(tag, "nativeDrawTriangle is called");
//  context.update_viewport();
  context.transfer.start_fbo();
  context.texture2DExt.draw();
  context.transfer.end_fbo();
  context.texture2D.draw();
}

extern "C" JNIEXPORT void JNICALL
Java_com_ywwynm_androidpractice_vrplayer_OpenGLVideoActivity_nativeUpdateViewport(
    JNIEnv *env, jobject /* this */, jint width, jint height) {
  logi(tag, "nativeUpdateViewport is called, width: %d, height: %d", width, height);
  context.update_viewport(width, height);
}
