//
// Created by zhangqi.pateo on 2018/9/2.
//

#ifndef REALSCENEVRPLAYER_UNITYANDROIDPLUGIN_GLUTILS_H
#define REALSCENEVRPLAYER_UNITYANDROIDPLUGIN_GLUTILS_H

#include "log.h"
#include "GLES3/gl3.h"

// this is ridiculous but is required because if we include GLES/glext.h to use this macro,
// there will be some errors when building. As a result, we define it by ourselves
#define GL_TEXTURE_EXTERNAL_OES 0x8D65

namespace glutils {

  void gl_check_error(const char *checker, const char *operation);

  GLuint compile_shader(const char *compiler, const char *shader_src, GLenum type);

  GLuint create_program(const char *creator, GLuint vertex_shader, GLuint fragment_shader);

} // end namespace glutils

#endif //REALSCENEVRPLAYER_UNITYANDROIDPLUGIN_GLUTILS_H
