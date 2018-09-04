//
// Created by ywwynm on 2018/9/3.
//

#ifndef ANDROIDPRACTICE_CONTEXT_H
#define ANDROIDPRACTICE_CONTEXT_H

#include "Triangle.h"
#include "Texture2D.h"
#include "TextureTransfer.h"
#include "GLES3/gl3.h"

class Context {
public:
  Triangle triangle;
  Texture2D texture2D;
  TextureTransfer transfer;

  int width;
  int height;

  void init() {
    triangle.init();
    texture2D.init();
    transfer.try_create_fbo();
  }

  void update_viewport() {
    glViewport(0, 0, width, height);
  }

  void update_viewport(int width, int height) {
    this->width = width;
    this->height = height;
    glViewport(0, 0, width, height);
    transfer.try_attach_texture_to_fbo(texture2D.get_texture_id(), width, height);
  }
};

#endif //ANDROIDPRACTICE_CONTEXT_H
