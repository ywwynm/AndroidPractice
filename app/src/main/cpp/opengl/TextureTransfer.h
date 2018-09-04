//
// Created by ywwynm on 2018/9/3.
//

#ifndef REALSCENEVRPLAYER_UNITYANDROIDPLUGIN_TEXTURETRANSFER_H
#define REALSCENEVRPLAYER_UNITYANDROIDPLUGIN_TEXTURETRANSFER_H

#include "GLES3/gl3.h"
#include "../utils/log.h"
#include "../utils/glutils.h"

class TextureTransfer {

private:
  const char* tag = "TextureTransfer";

  GLuint fbo_id = 0;
  GLuint texture_id = 0;

public:
  void try_create_fbo() {
    if (fbo_id != 0) {
      return;
    }
    glGenFramebuffers(1, &fbo_id);
    glutils::gl_check_error(tag, "glGenFramebuffers");
    logi(tag, "framebuffer generated: %d", fbo_id);
  }

  void try_attach_texture_to_fbo(GLuint texture_id, int width, int height) {
    if (this->texture_id != 0) {
      return;
    }
    this->texture_id = texture_id;
    glBindTexture(GL_TEXTURE_2D, texture_id);
    glutils::gl_check_error(tag, "glBindTexture to init FBO, id: " + texture_id);
    logi(tag, "Bind texture2D to init FBO, id: %d", texture_id);

    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, NULL);
    glutils::gl_check_error(tag, "glTexImage2D");
    logi(tag, "glTexImage2D, width: %d, height: %d", width, height);

    start_fbo();

    glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture_id, 0);
    GLenum fbo_status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
    if (fbo_status != GL_FRAMEBUFFER_COMPLETE) {
      loge(tag, "Framebuffer incomplete after attach texture2D!");
    }

    end_fbo();
  }

  void start_fbo() {
    glBindFramebuffer(GL_FRAMEBUFFER, fbo_id);
    glutils::gl_check_error(tag, "glBindFramebuffer " + fbo_id);
  }

  void end_fbo() {
    glBindFramebuffer(GL_FRAMEBUFFER, 0);
    glutils::gl_check_error(tag, "glBindFramebuffer 0 (unbind)");
  }

};

#endif //REALSCENEVRPLAYER_UNITYANDROIDPLUGIN_TEXTURETRANSFER_H
