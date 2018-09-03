//
// Created by zhangqi.pateo on 2018/9/2.
//

#ifndef REALSCENEVRPLAYER_UNITYANDROIDPLUGIN_LOG_H
#define REALSCENEVRPLAYER_UNITYANDROIDPLUGIN_LOG_H

#include "android/log.h"

#define logi(tag, ...) __android_log_print(ANDROID_LOG_INFO,  tag, __VA_ARGS__)
#define loge(tag, ...) __android_log_print(ANDROID_LOG_ERROR, tag, __VA_ARGS__)

#endif
