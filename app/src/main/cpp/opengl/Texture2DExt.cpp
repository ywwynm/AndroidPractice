//
// Created by zhangqi.pateo on 2018/9/2.
//

#include "Texture2DExt.h"

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
