package com.xcrj.netty.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    private static final String HOST="localhost";
    private static final int PORT=7000;

    public static void main(String[] args) {
        EventLoopGroup group=new NioEventLoopGroup();
        try {
            Bootstrap bootstrap=new Bootstrap();
            bootstrap
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                           ChannelPipeline channelPipeline = ch.pipeline();
                           channelPipeline.addLast(new NettyClientChannelHandler());
                        }
                    });
            System.out.println("客户端配置准备完成...");

            //Future-Listener异步模型
            //sync(): 当前线程等待异步线程connect()执行完成
            ChannelFuture channelFuture = bootstrap.connect(HOST,PORT).sync();
            //sync(): 当前线程等待异步线程closefuture()执行完成
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }
}
