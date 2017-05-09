package idn.ting44.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idn.ting44.handler.HttpAppDataDecode;
import idn.ting44.handler.HttpAppDataEnCode;
import idn.ting44.handler.HttpServiceHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class HttpServer {
	private Logger logger = LoggerFactory.getLogger(HttpServer.class);
  private int port ;
  private void bind(int port){
	  EventLoopGroup worker = new NioEventLoopGroup();
	  EventLoopGroup boss = new NioEventLoopGroup();
	  ServerBootstrap bootstrap = new ServerBootstrap();
	  bootstrap.group(boss, worker);
	  bootstrap.channel(NioServerSocketChannel.class);
	  bootstrap.option(ChannelOption.SO_BACKLOG,1024);
	  //bootstrap.option(ChannelOption.TCP_NODELAY, true);
	  bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
	 // bootstrap.option(ChannelOption.SO_LINGER, 0);
	  //bootstrap.childOption(ChannelOption.SO_LINGER,0);
	  bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
	  bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
	  bootstrap.handler(new LoggingHandler(LogLevel.INFO));
	  bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

		@Override
		protected void initChannel(SocketChannel channel) throws Exception {
			ChannelPipeline pipeline = channel.pipeline();
			pipeline.addLast(new HttpResponseEncoder());//编码
			pipeline.addLast(new HttpAppDataEnCode());//系统编码，把map转换成json
			
			pipeline.addLast(new HttpRequestDecoder());//解码
			pipeline.addLast(new HttpObjectAggregator(1024*64));
			pipeline.addLast(new HttpAppDataDecode());//系统解码，把json转换层map
			pipeline.addLast(new HttpServiceHandler());
		}
	});
	  ChannelFuture future = bootstrap.bind(port);
	  try {
		ChannelFuture sync = future.sync();
		if(sync.isSuccess()){
			System.out.println("server ---------------------");
		}
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
  public static void main(String[] args) {
	HttpServer server = new HttpServer();
	server.bind(8080);
}
}
