//
// Created by zhangqi.pateo on 2018/9/2.
//

#ifndef REALSCENEVRPLAYER_UNITYANDROIDPLUGIN_TEXTURE2D_H
#define REALSCENEVRPLAYER_UNITYANDROIDPLUGIN_TEXTURE2D_H

#include "../utils/log.h"
#include "GLES3/gl3.h"

#define tag provide_tag()
#define vertex_shader_src provide_vertex_shader_src()
#define fragment_shader_src provide_fragment_shader_src()

class Texture2D {

protected:
  virtual const char* provide_tag();
  virtual const char* provide_vertex_shader_src();
  virtual const char* provide_fragment_shader_src();

  GLuint texture_id;
  GLuint program_id;

  GLint aPos_handle;
  GLint aTexCoord_handle;
  GLint uTex_handle;

  const float vertex_data[12] = {
      1.0f,  1.0f, 0.0f,
      -1.0f,  1.0f, 0.0f,
      1.0f, -1.0f, 0.0f,
      -1.0f, -1.0f, 0.0f
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
  Texture2D();

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
