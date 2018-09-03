//
// Created by zhangqi.pateo on 2018/9/2.
//

#include "Texture2D.h"
#include "../utils/log.h"
#include "../utils/glutils.h"


const char *Texture2D::provide_tag() {
  return "Texture2D";
}

const char *Texture2D::provide_vertex_shader_src() {
  return {
      "attribute vec4 aPosition;\n"
      "attribute vec4 aTexCoord;\n"
      "varying vec2 vTexCoord;\n"
      "void main() {\n"
      "  gl_Position = aPosition;\n"
      "  vTexCoord = aTexCoord.xy;\n"
      "}\n"
  };
}

const char *Texture2D::provide_fragment_shader_src() {
  return {
      "precision mediump float;\n"
      "varying vec2 vTexCoord;\n"
      "uniform sampler2D uTexture;\n"
      "void main() {\n"
      "  gl_FragColor = texture2D(uTexture, vTexCoord);\n"
      "}\n"
  };
}

Texture2D::Texture2D() {

}

void Texture2D::init() {
  create_program();
  find_handles();
  init_texture();
  init_buffers();
}

void Texture2D::create_program() {
  GLuint vertex_shader = glutils::compile_shader(tag, vertex_shader_src, GL_VERTEX_SHADER);
  GLuint fragment_shader = glutils::compile_shader(tag, fragment_shader_src, GL_FRAGMENT_SHADER);
  logi(tag, "vertex shader: %d, fragment shader: %d", vertex_shader, fragment_shader);

  program_id = glutils::create_program(tag, vertex_shader, fragment_shader);
  logi(tag, "program is created: %d", program_id);
}

void Texture2D::find_handles() {
  aPos_handle = glGetAttribLocation(program_id, "aPosition");
  glutils::gl_check_error(tag, "glGetAttribLocation aPosition");
  logi(tag, "aPos_handle: %d", aPos_handle);

  aTexCoord_handle = glGetAttribLocation(program_id, "aTexCoord");
  glutils::gl_check_error(tag, "glGetAttribLocation aTexCoord");
  logi(tag, "aTexCoord_handle: %d", aTexCoord_handle);

  uTex_handle = glGetUniformLocation(program_id, "uTexture");
  glutils::gl_check_error(tag, "glGetUniformLocation uTexture");
  logi(tag, "uTex_handle: %d", uTex_handle);
}

void Texture2D::init_buffers() {
  glGenBuffers(1, &vertex_buffer);
  glBindBuffer(GL_ARRAY_BUFFER, vertex_buffer);
  glBufferData(vertex_buffer, sizeof(vertex_data), vertex_data, GL_STATIC_DRAW);
  logi(tag, "vertex_buffer generated: %d, vertex_data has been copied to it", vertex_buffer);

  glGenBuffers(1, &texture_buffer);
  glBindBuffer(GL_ARRAY_BUFFER, texture_buffer);
  glBufferData(texture_buffer, sizeof(texture_data), texture_data, GL_STATIC_DRAW);
  logi(tag, "texture_buffer generated: %d, texture_data has been copied to it", texture_buffer);
}

void Texture2D::init_texture() {
  glGenTextures(1, &texture_id);
  logi(tag, "texture_id generated: %d", texture_id);

  bind_texture();

  glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
  glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

  unbind_texture();
}

void Texture2D::bind_texture() {
  glBindTexture(GL_TEXTURE_2D, texture_id);
  glutils::gl_check_error(tag, "bind texture2D: " + texture_id);
}

void Texture2D::unbind_texture() {
  glBindTexture(GL_TEXTURE_2D, 0);
  glutils::gl_check_error(tag, "unbind texture2D");
}

void Texture2D::draw() {
  logi(tag, "start drawing");

  glClearColor(0, 0, 0.5, 1.0);
  glutils::gl_check_error(tag, "glClearColor");
  glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
  glutils::gl_check_error(tag, "glClear");

  glUseProgram(program_id);

  glEnableVertexAttribArray((GLuint) aPos_handle);
  glutils::gl_check_error(tag, "glEnableVertexAttribArray aPos_handle");
  glVertexAttribPointer((GLuint) aPos_handle, 3, GL_FLOAT, GL_FALSE, 12, 0);
  glutils::gl_check_error(tag, "glVertexAttribPointer aPos_handle");

  glEnableVertexAttribArray((GLuint) aTexCoord_handle);
  glutils::gl_check_error(tag, "glEnableVertexAttribArray aTexCoord_handle");
  glVertexAttribPointer((GLuint) aTexCoord_handle, 2, GL_FLOAT, GL_FALSE, 8, 0);
  glutils::gl_check_error(tag, "glVertexAttribPointer aTexCoord_handle");

  bind_texture();

  glUniform1i(uTex_handle, 0);
  glutils::gl_check_error(tag, "glUniform1i");

  glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
  glutils::gl_check_error(tag, "glDrawArrays");

  unbind_texture();

}
