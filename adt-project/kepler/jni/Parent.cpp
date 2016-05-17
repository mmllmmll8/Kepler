/*
 * Parent.cpp
 *
 *  Created on: 2016-2-24
 *      Author: mulewen
 */

#include "Parent.h"
#include "child.h"
extern Baseprocess *g_process;

extern JNIEnv* g_env;
char* user;
//子进程有权限访问父进程的私有目录,在此建立跨进程通信的套接字文件
static const char* PATH = "/data/data/com.example.kepler_example/my.sock";

//服务名称




int Baseprocess::write_to_channel( void* data, int len )
{
	return write( m_channel, data, len );
}

int Baseprocess::read_from_channel( void* data, int len )
{
	return            ( m_channel, data, len );
}

int Baseprocess::get_channel() const
{
	return m_channel;
}

void Baseprocess::set_channel( int channel_fd )
{
	m_channel = channel_fd;
}

Baseprocess::~Baseprocess()
{
	close(m_channel);
}

Parent::Parent(JNIEnv *env,jobject jobj,char* username)// : m_env(env)
{
	user = username;
	m_jobj = env->NewGlobalRef(jobj);
}

Parent::~Parent()
{

	g_process = NULL;
}

void Parent::do_work()
{
}

JNIEnv* Parent::get_jni_env() const
{
	return m_env;
}

jobject Parent::get_jobj() const
{
	return m_jobj;
}

/**
* 父进程创建通道,这里其实是创建一个客户端并尝试
* 连接服务器(子进程)
*/
bool Parent::create_channel()
{
	int sockfd;

	sockaddr_un addr;

	while( 1 )
	{
		sockfd = socket( AF_LOCAL, SOCK_STREAM, 0 );

		if( sockfd < 0 )
		{

			return false;
		}

		memset(&addr, 0, sizeof(addr));

		addr.sun_family = AF_LOCAL;

		strcpy( addr.sun_path, PATH );

		if( connect( sockfd, (sockaddr*)&addr, sizeof(addr)) < 0 )
		{
			close(sockfd);

			sleep(1);

			continue;
		}

		set_channel(sockfd);

		break;
	}

	return true;
}

/**
* 子进程死亡会发出SIGCHLD信号,通过捕捉此信号父进程可以
* 知道子进程已经死亡,此函数即为SIGCHLD信号的处理函数.
*/
static void sig_handler( int signo )
{
	pid_t pid;

	int status;

	//调用wait等待子进程死亡时发出的SIGCHLD
	//信号以给子进程收尸，防止它变成僵尸进程
	pid = wait(&status);

	if( g_process != NULL )
	{
		g_process->on_child_end();
	}
}

void Parent::catch_child_dead_signal()
{

	struct sigaction sa;

	sigemptyset(&sa.sa_mask);

	sa.sa_flags = 0;

	sa.sa_handler = sig_handler;

	sigaction( SIGCHLD, &sa, NULL );
}

void Parent::on_child_end()
{

	create_child();
}

bool Parent::create_child()
{
	pid_t pid;

	if( (pid = fork()) < 0 )
	{
		return false;
	}
	else if( pid == 0 ) //子进程
	{


		Child* child = new Child(user);

		Baseprocess& ref_child = *child;

		ref_child.do_work();
	}
	else if( pid > 0 )  //父进程
	{

	}

	return true;
}
