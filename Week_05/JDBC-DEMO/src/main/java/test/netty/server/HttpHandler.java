package test.netty.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.util.ReferenceCountUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Random;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            String uri = fullRequest.uri();

            if (uri.contains("/api/test")) {
                handlerTest(fullRequest, ctx);
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void handlerTest(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        FullHttpResponse response = null;

        Connection connection = null;
        Statement stmt = null;
        try {
            StringBuilder builder = new StringBuilder();
            // 测试写法
            connection = getConnection();

            String sql = "SELECT id, name, balance FROM account";
//            ResultSet rs = queryByStatement(connection, sql);

            ResultSet rs = batchUpdateByPreparedStatement(connection);
            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                BigDecimal balance =  rs.getBigDecimal("balance");
//                System.out.println("id = " + id + ", name = " + name + ", balance = " + balance);
                builder.append("id = ");
                builder.append(id);
                builder.append(", name = ");
                builder.append(name);
                builder.append(", balance = ");
                builder.append(balance);
                builder.append("\r\n");
            }

            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(builder.toString().getBytes("UTF-8")));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", response.content().readableBytes());

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
        } finally {
            if (fullRequest != null) {
                if (!HttpHeaderUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    response.headers().set(CONNECTION, KEEP_ALIVE);
                    ctx.write(response);
                }
            }
            // 关闭资源
            try{
                if(stmt!=null) {
                    stmt.close();
                }
            }catch(SQLException se2){
                se2.printStackTrace();
            }
            try{
                if(connection!=null) {
                    connection.close();
                }
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }

    public ResultSet queryByStatement(Connection connection, String sql) throws SQLException {
        assert connection != null;
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(sql);
    }


    /**
     * 批量
     * @param connection
     * @return
     * @throws SQLException
     */
    public ResultSet batchUpdateByPreparedStatement(Connection connection) throws SQLException {
        assert connection != null;
        PreparedStatement ps = connection.prepareStatement("insert into account (id,name,balance) values(?,?,?)");
        connection.setAutoCommit(false);
        for (int i = 0; i < 2; i++) {
            ps.setInt(1, getUUID(5));
            ps.setString(2, "张三");
            ps.setBigDecimal(3, new BigDecimal(80.8));
            ps.addBatch();
        }
        ps.executeBatch();
        connection.commit();
        connection.setAutoCommit(true);
        ps = connection.prepareStatement("SELECT id, name, balance FROM account");
        return ps.executeQuery();
    }

    public int getUUID(int len) {
        Random random = new Random();
        return random.nextInt(999) % 900 + 100;
    }


    /**
     * mysql connection
     * @return
     */
    private Connection getConnection() {
        Connection connection = null;
        try {
            String mysqlDriver = "com.mysql.jdbc.Driver";
            String dburl = "jdbc:mysql://localhost:3306/kad?characterEncoding=utf8&useSSL=true";
            String dbuser = "root";
            String dbpwd = "Admin@123";
            Class.forName(mysqlDriver);
            connection = DriverManager.getConnection(dburl, dbuser, dbpwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
