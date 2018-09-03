//
// Created by zhangqi.pateo on 2018/9/2.
//

#include "Texture2DExt.h"

const char* Texture2DExt::provide_tag() {
  return "Texture2DExt";
}

const char* Texture2DExt::provide_vertex_shader_src() {
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

const char* Texture2DExt::provide_fragment_shader_src() {
  return {
      "#extension GL_OES_EGL_image_external: require\n"
      "precision mediump float;\n"
      "varying vec2 vTexCoord;\n"
      "uniform samplerExternalOES uTexture;\n"
      "void main() {\n"
      "  gl_FragColor = texture2D(uTexture, vTexCoord);\n"
      "}\n"
  };
}

Texture2DExt::Texture2DExt() {

}

void Texture2DExt::init_texture() {
  glGenTextures(1, &texture_id);
  logi(tag, "oes texture_id generated: %d", texture_id);

  bind_texture();

  glTexParameterf(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
  glTexParameterf(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
  glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
  glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

  unbind_texture();
}

void Texture2DExt::bind_texture() {
  glBindTexture(GL_TEXTURE_EXTERNAL_OES, texture_id);
  glutils::gl_check_error(tag, "bind texture2DExt: " + texture_id);
}

void Texture2DExt::unbind_texture() {
  glBindTexture(GL_TEXTURE_EXTERNAL_OES, 0);
  glutils::gl_check_error(tag, "unbind texture2DExt");
}
