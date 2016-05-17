/*
 * Parent.h
 *
 *  Created on: 2016-2-24
 *      Author: mulewen
 */

#ifndef PARENT_H_
#define PARENT_H_

#include "Baseprocess.h"

class Parent: public Baseprocess {
public:
	Parent( JNIEnv* env, jobject jobj ,char* username);

	virtual bool create_child();

	virtual void do_work();

	virtual void catch_child_dead_signal();

	virtual void on_child_end();

	virtual ~Parent();

	bool create_channel();

	/**
	* 获取父进程的JNIEnv
	*/
	JNIEnv *get_jni_env() const;

	/**
	* 获取Java层的对象
	*/
	jobject get_jobj() const;

	private:

	JNIEnv *m_env;

	jobject m_jobj;
};

#endif /* PARENT_H_ */
