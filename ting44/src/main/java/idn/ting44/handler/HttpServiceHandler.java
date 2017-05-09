package idn.ting44.handler;

import java.util.HashMap;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

public class HttpServiceHandler extends SimpleChannelInboundHandler<FullHttpRequest>{

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
		System.out.println("-------(⊙o⊙)…(⊙o⊙)…(⊙o⊙)…(⊙o⊙)…excepitonCaught"+cause.getMessage());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("-----"+this.getClass().getName()+"----"+msg.getClass().getName());
		Map map = new HashMap();
		map.put("username", "挺44");
		map.put("welcome", "hello world");
		map.put("password", 23434);
		ctx.write(map);
		
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		ctx.flush();
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
            System.out.println("HttpServiceHandler.messageReceived() is running.....");
	}
 
}
