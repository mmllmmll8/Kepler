LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := com_example_kepler_tools_watch
LOCAL_SRC_FILES := com_example_kepler_tools_watch.cpp
LOCAL_SRC_FILES += Parent.cpp
LOCAL_SRC_FILES += Child.cpp
include $(BUILD_SHARED_LIBRARY)