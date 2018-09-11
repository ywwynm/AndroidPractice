//
// Created by ywwynm on 2018/9/3.
//

#ifndef ANDROIDPRACTICE_TRIANGLE_H
#define ANDROIDPRACTICE_TRIANGLE_H

#include "../utils/log.h"
#include "GLES3/gl3.h"

class Triangle {
protected:
  const char* tag = "Triangle";

  const char* vertex_shader_src = {
      "attribute vec4 aPosition;\n"
      "void main() {\n"
      "  gl_Position = aPosition;\n"
      "}\n"
  };

  const char* fragment_shader_src = {
      "precision mediump float;\n"
      "void main() {\n"
      "  gl_FragColor = vec4(0.9, 0.0, 0.0, 1.0);\n"
      "}\n"
  };

  GLuint program_id;

  GLint aPos_handle;

  const float vertex_data[9] = {
       0.0f, 0.0f, 0.0f,
      -1.0f, 1.0f, 0.0f,
       1.0f, 1.0f, 0.0f
  };
  GLuint vertex_buffer;

public:

  void init();
  void create_program();
  void find_handles();
  void init_buffers();

  void draw();
};


#endif //ANDROIDPRACTICE_TRIANGLE_H
