package org.example;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final String FONT_FAMILY = "楷体";

    public static void graphics2DDrawTest(SmbFile srcImgPath, File waterImgPath, String outPath, String text) {
        FileOutputStream outputStream = null;
        try {
            BufferedImage targetImg = ImageIO.read(srcImgPath.getInputStream());
            int imgWidth = targetImg.getWidth();
            int imgHeight = targetImg.getHeight();

            BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight,
                    BufferedImage.TYPE_INT_BGR);
            // 繪製圖片
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(targetImg, 0, 0, imgWidth, imgHeight, null);
            g.setColor(Color.BLACK);

            // 圖片印的XY軸
            BufferedImage icon = ImageIO.read(waterImgPath);
            g.drawImage(icon, 1800, 200, icon.getWidth(), icon.getHeight(), null);

            // 文字印的XY
            Font userNameFont = new Font(FONT_FAMILY, Font.PLAIN, 60);
            g.setFont(userNameFont);
            g.setColor(Color.decode("#336699"));
            int[] userNameSize = getContentSize(userNameFont, text);
            int userNameTopMargin = 340 + userNameSize[1];
            g.drawString(text, 1860, userNameTopMargin);

            //輸出檔案
            FileOutputStream outImgStream = new FileOutputStream(outPath);
            ImageIO.write(bufferedImage, "jpg", outImgStream);
            g.dispose();
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    /*
    * smbFile       主要圖片路徑
    * waterImgPath  浮水印圖片路徑
    * outPath       輸出圖片位置
    * text          浮水印文字內容
    * */
    public static void graphics2DDrawTest2(String smbFile, File waterImgPath, String outPath, String text) {
        FileOutputStream outputStream = null;
        try {
            String ftpuser = "webadmin@firstins.com.tw";
            String ftppass = "W03359109b";
            SmbFile srcImgPath = new SmbFile(smbFile,
                    new NtlmPasswordAuthentication("", ftpuser, ftppass));
            // 輸入圖片
            BufferedImage targetImg = ImageIO.read(srcImgPath.getInputStream());
            // 取得圖片寬高
            int imgWidth = targetImg.getWidth();
            int imgHeight = targetImg.getHeight();
            // 產生一個空白圖片
            BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_BGR);
            Graphics2D g = bufferedImage.createGraphics();
            // 將輸入圖片放置模板上
            g.drawImage(targetImg, 0, 0, imgWidth, imgHeight, null);

            // 取得圖片2
            BufferedImage icon = ImageIO.read(waterImgPath);
            // 設定圖片2在模板中的XY軸
            g.drawImage(icon, 1800, 200, icon.getWidth(), icon.getHeight(), null);
            // 新增字體模式
            Font userNameFont = new Font(FONT_FAMILY, Font.PLAIN, 60);
            g.setFont(userNameFont);
            // 字顏色
            g.setColor(Color.decode("#336699"));
            int[] userNameSize = getContentSize(userNameFont, text);
            int userNameTopMargin = 340 + userNameSize[1];
            // 設定字體在模板中的XY軸
            g.drawString(text, 1860, userNameTopMargin);
            //輸出檔案
            FileOutputStream outImgStream = new FileOutputStream(outPath);
            ImageIO.write(bufferedImage, "jpg", outImgStream);
            g.dispose();
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }


    /**
     * 获取文本的长度，字体大小不同，长度也不同
     *
     * @param font
     * @param content
     * @return
     */
    public static int[] getContentSize(Font font, String content) {
        int[] contentSize = new int[2];
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        Rectangle rec = font.getStringBounds(content, frc).getBounds();
        contentSize[0] = (int) rec.getWidth();
        contentSize[1] = (int) rec.getHeight();
        return contentSize;
    }

    public static ArrayList<Map<String, String>> as400JdbcTemplateBean() throws SQLException, ClassNotFoundException {
        String url = "jdbc:mysql://192.168.10.171/tfiimg";
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(url, "sign", "1qaz2wsx");

        if (connection != null) {
            System.out.println("連接成功！");
        } else {
            System.out.println("連接失敗！");
        }
        String sqlStatement = "SELECT  " +
                "ins_img_info.ACCEPT_NO, ins_img_info.STATUS, ins_img_info.SCAN_DTM, ins_img_info.CASE_TYPE, " +
                "ins_img_info.INS_CODE_NO, ins_img_info.DEPT_NO, ins_img_info.dept_area, ins_img_info.FILE_PATH, " +
                "area_code.AREA_NO, area_code.AREA_NAME, area_code.INS_CODE, ins_img_info.POLICY_DATE, " +
                "case when area_code.AREA_NAME = '新北市區' then 'W'  " +
                "         when area_code.AREA_NAME = '台南區' then 'N'  " +
                "     when area_code.AREA_NAME = '台中區' then 'C'  " +
                "     when area_code.AREA_NAME = '高雄區' then 'K'  " +
                "     when area_code.AREA_NAME = '北市區' then 'V'  " +
                "     when area_code.AREA_NAME = '桃竹區' then 'A' else '' end as accno_area " +
                "FROM ins_img_info " +
                "LEFT JOIN area_code ON ins_img_info.INS_CODE_NO=area_code.INS_CODE AND substr(ins_img_info.ACCEPT_NO,1,4)=area_code.AREA_NO " +
                "WHERE ins_img_info.STATUS IN ('07','06')  " +
                "AND substr(ACCEPT_NO,7,2) in ('RC','RA','RE','RO','SA','SO','SS','ST','WA','WO','WT','WW','LO','YO') " +
                "AND SCAN_DTM >= '111/09/02' AND SCAN_DTM <= '111/09/30';";
//                "AND ACCEPT_NO='100110RCC00895C010';";

        PreparedStatement ps = connection.prepareStatement(sqlStatement);
        ResultSet rs = ps.executeQuery();
        ArrayList<Map<String, String>> list = new ArrayList<>();
        while (rs.next()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("ACCEPT_NO", rs.getString("ACCEPT_NO"));
            map.put("STATUS", rs.getString("STATUS"));
            map.put("SCAN_DTM", rs.getString("SCAN_DTM"));
            map.put("CASE_TYPE", rs.getString("CASE_TYPE"));
            map.put("INS_CODE_NO", rs.getString("INS_CODE_NO"));
            map.put("DEPT_NO", rs.getString("DEPT_NO"));
            map.put("dept_area", rs.getString("dept_area"));
            map.put("FILE_PATH", rs.getString("FILE_PATH"));
            map.put("AREA_NO", rs.getString("AREA_NO"));
            map.put("AREA_NAME", rs.getString("AREA_NAME"));
            map.put("INS_CODE", rs.getString("INS_CODE"));
            map.put("POLICY_DATE", rs.getString("POLICY_DATE"));
            map.put("accno_area", rs.getString("accno_area"));
            list.add(map);
        }
        connection.close();
        return list;
    }

    public static void returnImgWH(SmbFile smbFile, String outPath) throws IOException {
        //载入图片到输入流
        BufferedInputStream bis = new BufferedInputStream(smbFile.getInputStream());
        //实例化存储字节数组
        byte[] bytes = new byte[100];
        //设置写入路径以及图片名称
        OutputStream bos = new FileOutputStream(new File(outPath));
        int len;
        while ((len = bis.read(bytes)) > 0) {
            bos.write(bytes, 0, len);
        }
        bis.close();
        bos.flush();
        bos.close();
    }


    public static void main(String[] args) throws Exception {
        String projectPath = System.getProperty("user.dir");
        String waterImgPath = projectPath + "/src/main/images/area_sign/";
        String myoutPath = projectPath + "/src/main/images/";
        String smbPath = "smb://nas4/SignImage/sign/tfiimg/images/";
        String outPath = "\\\\nas4\\SignImage\\sign\\tfiimg\\images\\";

        int[] i = {1};
        ArrayList<Map<String, String>> list = as400JdbcTemplateBean();
        list.forEach(map -> {
            System.out.println("--------------"+ i[0]++);
            String SCAN_DTM = map.get("SCAN_DTM").toString().split(" ")[0].replace("/", "");
            SCAN_DTM = String.valueOf(Integer.parseInt(SCAN_DTM) + 19110000);
            String ACCEPT_NO = map.get("ACCEPT_NO").toString() + "0001.jpg";
            String FILE_PATH = map.get("FILE_PATH").toString().replace("/", "\\") + "\\";
//        myoutPath = myoutPath + ACCEPT_NO;
            String outP = outPath + FILE_PATH + ACCEPT_NO;

            // 浮水印日期
            String POLICY_DATE = map.get("POLICY_DATE").toString();
            System.out.println("浮水印日期 POLICY_DATE--" + POLICY_DATE);
            // 浮水印印章圖檔
            String waterImgP = waterImgPath + map.get("accno_area") + ".png";
            System.out.println("浮水印印章圖檔 area_sign--" + waterImgP);
            //建立遠端檔案物件
//        SmbFile smbFile = new SmbFile(smbPath + SCAN_DTM + "/" + ACCEPT_NO,
//                new NtlmPasswordAuthentication("", ftpuser, ftppass));
            String smbFile = smbPath + SCAN_DTM + "/" + ACCEPT_NO;
            // 遠端檔案位置
            System.out.println("遠端檔案位置 SmbFile--" + smbFile);
            // 輸出檔案位置
            System.out.println("輸出檔案位置 outPath--" + outP);
//        returnImgWH(smbFile, outPath);
            graphics2DDrawTest2(smbFile, new File(waterImgP), outP, POLICY_DATE);
        });


    }

}
