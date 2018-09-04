//
// Created by ywwynm on 2018/9/3.
//

#include "Triangle.h"
#include "../utils/log.h"
#include "../utils/glutils.h"

void Triangle::init() {
  create_program();
  find_handles();
  init_buffers();
}

void Triangle::create_program() {
  GLuint vertex_shader = glutils::compile_shader(tag, vertex_shader_src, GL_VERTEX_SHADER);
  GLuint fragment_shader = glutils::compile_shader(tag, fragment_shader_src, GL_FRAGMENT_SHADER);
  logi(tag, "vertex shader: %d, fragment shader: %d", vertex_shader, fragment_shader);

  program_id = glutils::create_program(tag, vertex_shader, fragment_shader);
  logi(tag, "program is created: %d", program_id);
}

void Triangle::find_handles() {
  aPos_handle = glGetAttribLocation(program_id, "aPosition");
  glutils::gl_check_error(tag, "glGetAttribLocation aPosition");
  logi(tag, "aPos_handle: %d", aPos_handle);
}

void Triangle::init_buffers() {
  glGenBuffers(1, &vertex_buffer);
  glutils::gl_check_error(tag, "glGenBuffers");
  glBindBuffer(GL_ARRAY_BUFFER, vertex_buffer);
  glutils::gl_check_error(tag, "glBindBuffer GL_ARRAY_BUFFER");
  glBufferData(GL_ARRAY_BUFFER, sizeof(vertex_data), vertex_data, GL_STATIC_DRAW);
  glutils::gl_check_error(tag, "glBufferData");
  logi(tag, "vertex_buffer generated: %d, vertex_data has been copied to it", vertex_buffer);
}

void Triangle::draw() {
  logi(tag, "Start drawing");

  glClearColor(0, 0, 0.5, 1.0);
  glutils::gl_check_error(tag, "glClearColor");
  glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
  glutils::gl_check_error(tag, "glClear");

  glUseProgram(program_id);

  glEnableVertexAttribArray((GLuint) aPos_handle);
  glutils::gl_check_error(tag, "glEnableVertexAttribArray aPos_handle");
  glVertexAttribPointer((GLuint) aPos_handle, 3, GL_FLOAT, GL_FALSE, 12, 0);
  glutils::gl_check_error(tag, "glVertexAttribPointer aPos_handle");

  glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
  glutils::gl_check_error(tag, "glDrawArrays");

}
