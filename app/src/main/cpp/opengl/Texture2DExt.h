//
// Created by zhangqi.pateo on 2018/9/2.
//

#ifndef REALSCENEVRPLAYER_UNITYANDROIDPLUGIN_TEXTURE2DEXT_H
#define REALSCENEVRPLAYER_UNITYANDROIDPLUGIN_TEXTURE2DEXT_H

#include "Texture2D.h"
#include "GLES3/gl3.h"
#include "../utils/glutils.h"

class Texture2DExt: public Texture2D {

protected:
  const char* provide_tag();
  const char* provide_vertex_shader_src();
  const char* provide_fragment_shader_src();

public:
  Texture2DExt();

  void init_texture();

  void bind_texture();
  void unbind_texture();
};


#endif //REALSCENEVRPLAYER_UNITYANDROIDPLUGIN_TEXTURE2DEXT_H
