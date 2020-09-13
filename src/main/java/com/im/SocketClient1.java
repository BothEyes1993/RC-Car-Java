package com.im;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SocketClient1 {
    public static final int port = SocketServer.port;
    public static final String address = SocketServer.address;
    private static Logger log = Logger.getLogger("client");
    //为了方便使用直接定义为静态属性，，调用很方便。
    private static Socket client = null;
    //给客户端起个名字
    private static String clientName = "张三";
    private static Integer fromId = 10001;
    private static Integer toId = 1;

    private static MessageVo messageVo = new MessageVo(fromId, toId, clientName +"说：正在连接服务器");

    public static void main(String[] args) throws IOException, InterruptedException {
        log.log(Level.INFO, "客户端" + clientName + "开始链接，端口：" + port);
        client = new Socket(address, port);
        new SendThread().start();
        new ReceiveThread().start();
        sendSocketServer(JSONObject.toJSONString(messageVo), client);//发送消息
        while (!client.isClosed()) {
            Thread.sleep(1000);
        }
    }

    /**
     * 发送Socket的socket
     *
     * @param outStr 待发送信息
     * @param client 客户端Socket
     */
    private static void sendSocketServer(String outStr, Socket client) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
            oos.writeObject(outStr);
            oos.flush();//刷新，将流发出去
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送信息线程
     */
    static class SendThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!client.isClosed()) {//插入停止条件
                Scanner in = new Scanner(System.in);//接收输入
                String inputStr = in.next();
                if (inputStr != null) {
                    messageVo.setMessage(inputStr);
                    sendSocketServer(JSONObject.toJSONString(messageVo), client);//发送消息
                }
            }
        }
    }

    /**
     * 接受信息线程，，运行在后台，，等待输入信息
     */
    static class ReceiveThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                InputStream serverInputStream = client.getInputStream();
                while (!client.isClosed() && serverInputStream != null) {//插入停止条件
                    ObjectInputStream ois = new ObjectInputStream(serverInputStream);
                    String inputStr = (String) ois.readObject();
                    //获取输出流，经过测试发现，输入流貌似是阻塞，，也就是没有输入时，
                    // 他就停在这里了，，一直等着输入，，所以无需加入Thread.sleep().
                    System.out.println(inputStr);
                }
            } catch (SocketException e) {
                log.log(Level.INFO, "服务器断开连接！！！");
                try {
                    client.close();//服务器断开连接此时需要关闭客户端连接
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
