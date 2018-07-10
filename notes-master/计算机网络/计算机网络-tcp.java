----------------------------
tcp							|
----------------------------
	# 传输控制协议(Transmission Control Protocol)
	# TCP主要特点
		* tcp是面向连接的运输层协议,这就是说,应用程序在使用tcp协议之前,必须先建立tcp连接,在数据传输完毕之后,必须释放tcp连接
		  这就是说,应用进程之间的通信好像在打电话一样,通话之前,先要拨号建立tcp连接,通话结束后要挂机释放连接
		
		* 每一条tcp只能有两个端点,每一条tcp连接只能是点对点的(一对一)

		* tcp提供可靠的交付服务,也就是说,通过tcp连接传输的数据,无差错,不丢失,不重复,并且按序到达

		* tcp提供双工通信,tcp允许通信双方的应用进程在任何时候都能发送数据
		  tcp连接的两端都设有发送缓存和接收缓存,用来临时存放双向通信的数据
		  在发送时,应用程序把数据传送给tcp的缓存后,就可以做自己的事情,而tcp会在合适的时候把数据发送出去
		  在接收时,tcp把收到的数据放入缓存,上层引用进程在合适的时候读取缓存中的数据
		 
		* 面向字节流,tcp中的"流",指的是流入到进程或从进程流出的字节序列
		  "面向字节流"的含义是:虽然应用程序和tcp的交互是一次一个数据库块(大小不等),但tcp把应用程序交付下来的数据看成仅仅是一连串无结构的字节流
		  tcp并不知道所传送的字节流的含义,tcp不保证接收方的应用程序所收到的数据块和发送方程序所发出来的数据块具有对应大小的关系
		  (例如:发送方应用程序交给发送方tcp共10个数据块,但是接收方的tcp可能只用了4个数据块就把接收到字节流交付给了上层应用程序)
		  但接收方应用程序收到的字节流必须和发送方应用程序发出的字节流完全一样,当然,接收方的应用程序必须有能力识别收到的字节流,把它还原成有意义的应用层数据
		 
		* tcp根据对方给出的窗口值和当前网络拥塞的程度来决定一个报文应该包含多少个字节(udp发送的报文长度是由应用程序给出的)

		  
----------------------------
tcp-可靠传输的工作原理		|
----------------------------
	* tcp发送的报文是交给ip层传送的,但ip层只能提供尽最大努力服务
	  也就是手,tcp下面的网络所提供的是不可靠的传输,因此,tcp必须采用适当的措施才能使得两个运输层之间的通信变得可靠
	
	* 理想的传输条件有以下两个特点

		1,传输通道不产生差错
		2,不管发送方以多快的速度发送数据,接收方总是来得及处理收到的数据

		在这样理想的传输条件下,不需要采取任何措施就可以实现可靠传输
		然后实际的网络都不具备以上两个理想的条件,我们可以使用一些可靠的传输协议,当出现差错时让发送方重传出现差错的数据
		同时在接收方来不及处理收到的数据时,及时告知发送方适当降低发送数据的速度
	
	# 停止等待协议
		* 停止等待,就是没发送完一个分组,就停止发送,等待对方确认,在收到确认后再发送下一个分组

		1,如果没有出现差错
			A发送一个M1报文到B,B收到报文后告诉A收到了M1报文,A再发送M2报文...

		2,出现差错
			* B接收M1时出现了差错,直接就丢弃M1(并不通知A)
			* 可能是M1在传输过程中就丢失了,B这时啥也不知道
			* 以上两种情况,B都不会发送任何消息
			* 可靠的传输协议是这样设计的
				A只要超过一段时间,没有收到B的确认,就认为刚才发送的分组丢失了,因而重传前面发送过的分组,这个就是'超时重传'
				要实现超时重传,就要在每发送完了一个分组设置一个超时计时器,如果在计时器到期之前收到了确认,就撤销已经设置的超时器

			* 这里需要注意3个地方
				1,A在发送完一个分组后,必须暂时的保留已发送的分组副本(在超时重传时使用),只有在收到相应的确认后才能清除暂时保留的分组副本
				2,分组和确认分组都必须进行编号,这样才能明确是哪一个发送出去的分组收到了确认,而哪一个分组还没有收到确认
				3,超时器设置的重传时间应该比'数据在分组传输的平均往返时间更长一些'
			
			* 确认丢失和确认迟到
				B发送的M1确认丢失了,A没有收到M1的确认,超时后重传,此时B收到了重传的M2,直接丢弃,不向上交付
				A在收到'迟到的M1确认'后,什么也不会做
			

----------------------------
tcp-报头					|
----------------------------
	 -----------------------------------------------------------------------------------------------------------
	|										tcp首部											|选项(长度可变)|填充|						
	 -----------------------------------------------------------------------------------------------------------
	|源端口|目的端口|序号|确认号|数据偏移|保留|URG|ACK|PSH|RST|SYN|FIN|窗口|校验和|紧急指针 |
     ---------------------------------------------------------------------------------------
	|	2  |   2	|  4 |	4	|			 2						  | 2  |   2  |   2     |
     ---------------------------------------------------------------------------------------
	|						20 字节固定首部												    |
	 ---------------------------------------------------------------------------------------

	# 数据偏移
		* 占4位,指出tcp报文段的数据起始距离tcp报文段起始处有多远,这个字段实际指出tcp报文段首部长度
	
	# 保留
		* 占6位,保留今后使用,目前置为0
	
	# 紧急URG
	# 确认ACK
		* 当ack=1,时,确认号字段才有效,当ack=0时,确认号无效,tcp规定.在建立连接后所有传送的报文段,都必须把ack置1
	# 推送PSH
	# 复位RST
	# 同步SYN
	# 终止FIN
		* 用于释放连接,FIN=1,表示报文段发送方的数据已经发送完毕,并要求释放运输连接
	

----------------------------
tcp-连接的建立				|
----------------------------
	客户端A									服务器B
		|--------->	SYN=1,seq=x					|
		|SYN-SENT								|
		|	SYN=1,ACK=1,seq=y,ack=x+1 <---------|
		|								SYN=RCVD|
		|---------> ACK=1,seq=x+1,ack=y+1		|
		|ESTAB-LISHED <-数据传递->  ESTAB-LISHED|

		

	1,客户端进程先创建'传输控制模块TCB',然后向B发出连接请求报文段
	  此时,首部中的同步位:syn=1,同时选择一个初始序号seq=x,tcp规定,syn报文段(syn=1的报文段)不能携带数据,但是要消耗掉一个序号,此时tcp客户端进程进入SYN-SENT同步一发送状态
	 
	

