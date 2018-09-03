//
// Created by ywwynm on 2018/9/3.
//

#include "glutils.h"

namespace glutils {
  void gl_check_error(const char *checker, const char *operation) {
    GLenum check_result = glGetError();
    if (check_result != GL_NO_ERROR) {
      loge(checker, "gl_error %d happens, operation: %s", check_result, operation);
      // todo should we shut down the app here?
    }
  }

  GLuint compile_shader(const char *compiler, const char *shader_src, GLenum type) {
    logi(compiler, "Start to compile shader, type: %d", type);
    logi(compiler, "Shader src:\n%s", shader_src);
    GLuint shader = glCreateShader(type);
    glShaderSource(shader, 1, &shader_src, NULL);
    glCompileShader(shader);

    GLint compile_status;
    glGetShaderiv(shader, GL_COMPILE_STATUS, &compile_status);

    if (compile_status == GL_FALSE) {
      loge(compiler, "Compile status is %d", compile_status);

      GLint log_len = 0;
      glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &log_len);
      // The log_len includes the NULL character
      GLchar *msg = new char[log_len];
      glGetShaderInfoLog(shader, log_len, &log_len, msg);

      loge(compiler, "Error compiling shader, type: %d, msg: %s", type, msg);

      delete[] msg;
    }

    logi(compiler, "Shader compiled, id: %d", shader);
    return shader;
  };

  GLuint create_program(const char *creator, GLuint vertex_shader, GLuint fragment_shader) {
    GLuint program = glCreateProgram();
    gl_check_error(creator, "create program");

    glAttachShader(program, vertex_shader);
    gl_check_error(creator, "attach vertex shader");
    glAttachShader(program, fragment_shader);
    gl_check_error(creator, "attach fragment shader");

    glLinkProgram(program);

    GLint link_status;
    glGetProgramiv(program, GL_LINK_STATUS, &link_status);

    if (link_status == GL_FALSE) {
      loge(creator, "Link status is %d", link_status);

      GLint log_len = 0;
      glGetProgramiv(program, GL_INFO_LOG_LENGTH, &log_len);
      GLchar *msg = new char[log_len];
      glGetProgramInfoLog(program, log_len, &log_len, msg);

      loge(creator, "Error linking program, msg: %s", msg);

      delete[] msg;
    }

    return program;
  }
}

