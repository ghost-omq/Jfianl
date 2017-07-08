package omq.my.setting;

import java.awt.image.BufferedImage;
import java.io.File;

import com.jfinal.kit.HashKit;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.upload.UploadFile;

import omq.common.kit.ImageKit;
import omq.common.model.Account;
import omq.index.IndexService;
import omq.login.LoginService;

public class MySettingService {
	
	public static final MySettingService me = new MySettingService();
	public final Account dao = new Account().dao();
	
	public static final String extName = ".jpg";

	public int getAvatarMaxSize(){
		return 1024 * 1024;
	}
	
	public String getAvatarTempDir(){
		return "/avatar/temp/";
	}
	
	public Ret uploadAvatar(int accountId, UploadFile uf){
		if(uf == null){
			return Ret.fail("msg", "上传文件对象不能为空");
		}
		try{
			if(ImageKit.notImageExtName(uf.getFileName())){
				return Ret.fail("msg","文件类型不正确，只支持：gif、jpg、jpeg、png、bmp");
			}
			
			String avatarUrl = "/upload" + getAvatarTempDir() + accountId + "_" + System.currentTimeMillis() + extName;
			String saveFile = PathKit.getWebRootPath() + avatarUrl;
			ImageKit.zoom(500, uf.getFile(), saveFile);
			return Ret.ok("avatarUrl", avatarUrl);
		}
		catch (Exception e) {
			return Ret.fail("msg", e.getMessage());
		} finally {
			uf.getFile().delete();
		}
	}
	
	public Ret saveAvatar(Account loginAccount, String avatarUrl, int x, int y, int width, int height) {
		int accountId = loginAccount.getId();
		String webRootPath = PathKit.getWebRootPath() ;
		String avatarFileName = webRootPath + avatarUrl;

		try {
			String[] relativePathFileName = new String[1];
			String[] absolutePathFileName = new String[1];
			buildPathAndFileName(accountId, webRootPath, relativePathFileName, absolutePathFileName);

			BufferedImage bi = ImageKit.crop(avatarFileName, x, y, width, height);
			bi = ImageKit.resize(bi, 200, 200); 
			//deleteOldAvatarIfExists(absolutePathFileName[0]);
			ImageKit.save(bi, absolutePathFileName[0]);

			//AccountService.me.updateAccountAvatar(accountId, relativePathFileName[0]);
			LoginService.me.reloadLoginAccount(loginAccount);
			IndexService.me.clearCache();
			return Ret.ok("msg", "头像更新成功");
		} catch (Exception e) {
			return Ret.fail("msg", "头像更新失败：" + e.getMessage());
		} finally {
			new File(avatarFileName).delete();
		}
	}
	
	private void buildPathAndFileName(int accountId, String webRootPath, String[] relativePathFileName, String[] absolutePathFileName) {
		String relativePath = (accountId / 5000) + "/";
		String fileName = accountId + extName;
		relativePathFileName[0] = relativePath + fileName;

		String absolutePath = webRootPath + "/upload/avatar/" + relativePath;
		File temp = new File(absolutePath);
		if (!temp.exists()) {
			temp.mkdirs();
		}
		absolutePathFileName[0] = absolutePath + fileName;
	}
	
	public Ret updatePassword(int accountId, String oldPassword, String newPassword) {
		if (StrKit.isBlank(oldPassword)) {
			return Ret.fail("msg", "原密码不能为空");
		}
		if (StrKit.isBlank(newPassword)) {
			return Ret.fail("msg", "新密码不能为空");
		}
		if (newPassword.length() < 6) {
			return Ret.fail("msg", "新密码长度不能小于 6");
		}

		Account account = dao.findById(accountId);
		String salt = account.getSalt();
		String hashedPass = HashKit.sha256(salt + oldPassword);
		if ( ! hashedPass.equals(account.getPassword())) {
			return Ret.fail("msg", "原密码不正确，请重新输入");
		}

		salt = HashKit.generateSaltForSha256();
		newPassword = HashKit.sha256(salt + newPassword);
		int result = Db.update("update account set password=?, salt=? where id=? limit 1", newPassword, salt, accountId);
		if (result > 0) {
			return Ret.ok("msg", "密码更新成功");
		} else {
			return Ret.fail("msg", "未找到账号，请联系管理员");
		}
	}
}
