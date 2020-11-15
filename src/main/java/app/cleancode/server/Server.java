package app.cleancode.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LoggingHandler;

public class Server implements Runnable {
public static int port = 80;

@Override
public void run() {
	NioEventLoopGroup boss = new NioEventLoopGroup(1);
	NioEventLoopGroup workers = new NioEventLoopGroup();
	try {
		ServerBootstrap boot = new ServerBootstrap();
		boot.group(boss, workers)
		.channel(NioServerSocketChannel.class)
		.handler(new LoggingHandler())
		.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline()
				.addLast(new HttpRequestDecoder())
				.addLast(new HttpObjectAggregator(65536))
				.addLast(new HttpResponseEncoder())
				.addLast(new HttpHandler());
			}
		});
		ChannelFuture f = boot.bind(port).sync();
		f.channel().closeFuture().sync();
	}catch (Exception e) {
		e.printStackTrace();
	}finally {
		boss.shutdownGracefully();
		workers.shutdownGracefully();
	}
}

}
