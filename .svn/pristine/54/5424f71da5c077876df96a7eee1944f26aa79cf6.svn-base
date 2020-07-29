package io.nuite.modules.system.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import io.nuite.common.utils.R;
import io.nuite.modules.sr_base.entity.BaseUserEntity;
import io.nuite.modules.sr_base.service.BaseUserService;
import io.nuite.modules.system.model.LoginResponse;
import io.nuite.modules.system.service.SystemUserTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 扫描登录功能
 * @author Administrator
 *
 */
@RestController
@RequestMapping("scanQrCode")
@Api(tags="扫描登录接口")
public class ScanQrCodeController extends AbstractController{
	@Autowired
	private SystemUserTokenService systemUserTokenService;
	@Autowired
	private BaseUserService baseUserService;

	 private Map<String, LoginResponse> loginMap = new ConcurrentHashMap<>();
	
	 /**
	  * 获取二维码
	  * @return
	  * @throws Exception
	  */
	   @GetMapping("login/getQrCode")
	    public R getQrCode() throws Exception {
		   UUID uuid=UUID.randomUUID();
	        return R.ok().put("UUID",uuid).put("image", createQrCode(uuid.toString()));
	    }
	   
	   /**
	    *  app二维码登录地址
	    * @param UUID
	    * @param loginUser
	    * @return
	    */
	   @GetMapping("login/setUser")
	   @ApiOperation("app二维码登录地址")
	    public R  setUser(@ApiParam("UUID") @RequestParam(value = "UUID") String UUID,@ApiParam("用户seq") @RequestParam(value = "userSeq") Integer  userSeq) {
	        BaseUserEntity loginUser=baseUserService.selectById(userSeq);
		   if (loginMap.containsKey(UUID)) {
	            LoginResponse loginResponse = loginMap.get(UUID);

	            // 赋值登录用户
	            loginResponse.user = loginUser;

	            // 唤醒登录等待线程
	            loginResponse.latch.countDown();
	        }
	        return R.ok().put("UUID", UUID).put("user", loginUser);
	    }
	   
	   /**
	         * 等待二维码扫码结果的长连接
	    * @param UUID
	    * @param session
	    * @return
	    */
	   @GetMapping("login/getResponse")
	    public R  getResponse(@ApiParam("UUID") @RequestParam(value = "UUID") String UUID, HttpSession session) {
	        Map<String, Object> result = new HashMap<>();
	        result.put("loginId", UUID);
	        try {
	            LoginResponse loginResponse = null;
	            if (!loginMap.containsKey(UUID)) {
	                loginResponse = new LoginResponse();
	                loginMap.put(UUID, loginResponse);
	            } else {
	            	loginResponse = loginMap.get(UUID);
	            }
	                
	            // 第一次判断
	            // 判断是否登录,如果已登录则写入session
	            if (loginResponse.user != null) {
	            	R r = systemUserTokenService.createToken(loginResponse.user.getSeq());
	                return r;
	            }

	            if (loginResponse.latch == null) {
	                loginResponse.latch = new CountDownLatch(1);
	            }
	            try {
	                // 线程等待
	                loginResponse.latch.await(5, TimeUnit.MINUTES);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }

	            // 再次判断
	            // 判断是否登录,如果已登录则写入session
	            if (loginResponse.user != null) {
	            	R r = systemUserTokenService.createToken(loginResponse.user.getSeq());
	                return r;
	            }

	            return R.error();
	        } finally {
	            // 移除登录请求
	            if (loginMap.containsKey(UUID))
	                loginMap.remove(UUID);
	        }
	    }

	   /**
	    *  生成base64二维码
	    * @param content
	    * @return
	    * @throws Exception
	    */
	   private String createQrCode(String content) throws Exception {
	        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
	            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
	            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
	            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
	            hints.put(EncodeHintType.MARGIN, 1);
	            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 400, 400, hints);
	            int width = bitMatrix.getWidth();
	            int height = bitMatrix.getHeight();
	            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	            for (int x = 0; x < width; x++) {
	                for (int y = 0; y < height; y++) {
	                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
	                }
	            }
	            ImageIO.write(image, "JPG", out);
	            return Base64.encodeBase64String(out.toByteArray());
	        }
	    }
}
