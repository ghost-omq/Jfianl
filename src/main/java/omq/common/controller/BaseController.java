package omq.common.controller;

import com.jfinal.core.Controller;

import omq.common.model.Account;
import omq.login.LoginService;

public class BaseController extends Controller{

	private Account loginAccount = null;
	
	public Account getLoginAccount(){
		if(loginAccount == null){
			loginAccount = getAttr(LoginService.loginAccountCacheName);
			if(loginAccount != null && ! loginAccount.isStatusOk()){
				throw new IllegalStateException("当前用户状态不允许登录");
			}
		}
		return loginAccount;
	}
	
	public boolean isLogin(){
		return getLoginAccount() != null;
	}
	
	public boolean noLogin(){
		return !isLogin();
	}
	
	public int getLoginAccountId(){
		return getLoginAccount().getId();
	}
}

