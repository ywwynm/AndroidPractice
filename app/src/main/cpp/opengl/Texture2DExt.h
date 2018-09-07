//
// Created by zhangqi.pateo on 2018/9/2.
//

#ifndef REALSCENEVRPLAYER_UNITYANDROIDPLUGIN_TEXTURE2DEXT_H
#define REALSCENEVRPLAYER_UNITYANDROIDPLUGIN_TEXTURE2DEXT_H

#include "Texture2D.h"
#include "GLES3/gl3.h"
#include "../utils/glutils.h"

class Texture2DExt: public Texture2D {

public:
  Texture2DExt() {
    tag = "Texture2DExt";
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
        "#extension GL_OES_EGL_image_external: require\n"
        "precision mediump float;\n"
        "varying vec2 vTexCoord;\n"
        "uniform samplerExternalOES uTexture;\n"
        "void main() {\n"
        "  gl_FragColor = texture2D(uTexture, vTexCoord);\n"
        "}\n"
    };
  }

  void init_texture();

  void bind_texture();
  void unbind_texture();
};


#endif //REALSCENEVRPLAYER_UNITYANDROIDPLUGIN_TEXTURE2DEXT_H
