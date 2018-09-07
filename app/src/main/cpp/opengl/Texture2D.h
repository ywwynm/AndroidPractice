//
// Created by zhangqi.pateo on 2018/9/2.
//

#ifndef REALSCENEVRPLAYER_UNITYANDROIDPLUGIN_TEXTURE2D_H
#define REALSCENEVRPLAYER_UNITYANDROIDPLUGIN_TEXTURE2D_H

#include "../utils/log.h"
#include "GLES3/gl3.h"

class Texture2D {

protected:
  const char* tag;
  const char* vertex_shader_src;
  const char* fragment_shader_src;

  GLuint texture_id;
  GLuint program_id;

  GLint aPos_handle;
  GLint uMVPMat_handle;
  GLint aTexCoord_handle;
  GLint uTex_handle;

  const float vertex_data[12] = {
       1.0f,  1.0f, 0.0f,
      -1.0f,  1.0f, 0.0f,
      -1.0f, -1.0f, 0.0f,
       1.0f, -1.0f, 0.0f
  };
  GLuint vertex_buffer;

  const float texture_data[8] = {
      1.0f, 0.0f,
      0.0f, 0.0f,
      1.0f, 1.0f,
      0.0f, 1.0f
  };
  GLuint texture_buffer;

public:
  Texture2D() {
    tag = "Texture2D";
    vertex_shader_src = {
        "attribute vec4 aPosition;\n"
        "uniform mat4 uMVPMatrix;\n"
        "attribute vec4 aTexCoord;\n"
        "varying vec2 vTexCoord;\n"
        "void main() {\n"
        "  gl_Position = aPosition;\n"
        "  vTexCoord = aTexCoord.xy;\n"
        "}\n"
    };
    fragment_shader_src = {
        "precision mediump float;\n"
        "varying vec2 vTexCoord;\n"
        "uniform sampler2D uTexture;\n"
        "void main() {\n"
        "  gl_FragColor = texture2D(uTexture, vTexCoord);\n"
        "}\n"
    };
  }

  void init();
  void create_program();
  void find_handles();
  void init_buffers();
  virtual void init_texture();

  virtual void bind_texture();
  virtual void unbind_texture();

  void draw();

  GLuint get_texture_id() { return texture_id; }
};


#endif //REALSCENEVRPLAYER_UNITYANDROIDPLUGIN_TEXTURE2D_H
