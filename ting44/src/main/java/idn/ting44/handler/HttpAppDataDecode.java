package idn.ting44.handler;



import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;

public class HttpAppDataDecode extends SimpleChannelInboundHandler<FullHttpRequest> {

	private Logger log = LoggerFactory.getLogger(HttpAppDataDecode.class);
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("HttpAppDataDecode:"+cause.getMessage());
		cause.printStackTrace();
		ctx.close();
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("in "+this.getClass().getName()+" object is:"+msg.getClass().getName());
		FullHttpRequest request = (FullHttpRequest) msg;
		HttpMethod method = request.getMethod();
		String uri = request.getUri();
		System.out.println("method:"+method+" uri:"+uri);
		ByteBuf byteBuf = request.content();
		String json = byteBuf.toString(io.netty.util.CharsetUtil.UTF_8);
		Map map = (Map) JSON.parse(json);
		if(map == null){
			map = new HashMap();
			map.put("username", "挺55");
			map.put("password", "123456");
		}
		System.out.println("from http bs data:"+map);
		byteBuf.release();
		System.out.println("channelRead:"+ctx);
		ctx.fireChannelRead(map);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		ByteBuf content = request.content();
		System.out.println("messageReceived:"+content.getClass().getName());
	}

	
	

}
