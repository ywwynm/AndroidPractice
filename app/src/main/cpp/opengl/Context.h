//
// Created by ywwynm on 2018/9/3.
//

#ifndef ANDROIDPRACTICE_CONTEXT_H
#define ANDROIDPRACTICE_CONTEXT_H

#include "Triangle.h"
#include "GLES3/gl3.h"

class Context {
public:
  Triangle triangle;

  int width;
  int height;

  void init() {
    triangle.init();
  }

  void update_viewport(int width, int height) {
    glViewport(0, 0, width, height);
  }
};

#endif //ANDROIDPRACTICE_CONTEXT_H
