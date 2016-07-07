LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := Silence
LOCAL_MODULE_TAGS := optional
LOCAL_PACKAGE_NAME := Silence

silence_root  := $(LOCAL_PATH)
silence_dir   := .
silence_out   := $(PWD)/$(OUT_DIR)/target/common/obj/APPS/$(LOCAL_MODULE)_intermediates
silence_build := $(silence_root)/$(silence_dir)/build
silence_apk   := build/outputs/apk/$(silence_dir)-release-unsigned.apk

$(silence_root)/$(silence_dir)/$(silence_apk):
	rm -Rf $(silence_build)
	mkdir -p $(silence_out)
	ln -sf $(silence_out) $(silence_build)
	cd $(silence_root)/$(silence_dir) && git submodule update --init --recursive && ./gradlew assembleRelease

LOCAL_CERTIFICATE := platform
LOCAL_SRC_FILES := $(silence_dir)/$(silence_apk)
LOCAL_MODULE_CLASS := APPS
LOCAL_MODULE_SUFFIX := $(COMMON_ANDROID_PACKAGE_SUFFIX)
LOCAL_PRIVILEGED_MODULE := false

include $(BUILD_PREBUILT)
