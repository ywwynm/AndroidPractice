//
// Created by ywwynm on 2018/9/3.
//

#ifndef ANDROIDPRACTICE_CONTEXT_H
#define ANDROIDPRACTICE_CONTEXT_H

#include "opengl/Triangle.h"
#include "opengl/Texture2D.h"
#include "opengl/TextureTransfer.h"
#include "GLES3/gl3.h"
#include "opengl/Texture2DExt.h"
#include "unity/UnityGraphics.h"

class Context {
public:
  static const char* tag;

  Triangle triangle;
  Texture2D texture2D;
  Texture2DExt texture2DExt;
  TextureTransfer transfer;

  int width;
  int height;

  void init();

  void update_viewport();

  void update_viewport(int width, int height);

  static void on_render_event(int event_id);

  UnityRenderingEvent get_render_event_function();
};

#endif //ANDROIDPRACTICE_CONTEXT_H
