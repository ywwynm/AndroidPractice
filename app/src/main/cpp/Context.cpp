//
// Created by zhangqi.pateo on 2018/9/5.
//

#include "Context.h"

const char* Context::tag = "NativeContext";

void Context::init() {
  triangle.init();
  texture2D.init();
  texture2DExt.init();
  transfer.try_create_fbo();
}

void Context::update_viewport() {
  glViewport(0, 0, width, height);
}

void Context::update_viewport(int width, int height) {
  this->width = width;
  this->height = height;
  glViewport(0, 0, width, height);
  transfer.try_attach_texture_to_fbo(texture2D.get_texture_id(), width, height);
}

void Context::on_render_event(int event_id) {
  // todo fill this method
  logi(tag, "on_render_event is called, event_id: %d", event_id);
}

UnityRenderingEvent Context::get_render_event_function() {
  UnityRenderingEvent ptr = on_render_event;
  logi(tag, "get_render_event_function is called, ptr: %ld", (long) ptr);
  return on_render_event;
}
