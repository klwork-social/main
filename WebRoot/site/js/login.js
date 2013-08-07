var klwork_login = (function() {

	var checkloginEmail = function(email) {
		if (email == "") {
			alert("请填写注册时的email");
			$("user_email").focus();
			return false;
		}
		return true;
	};

	var checkloginpass = function(password) {
		if (password == "") {
			alert("请填写密码");
			$("user_password").focus();
			return false;
		}
		return true;
	};

	var initForm = function() {
		// 邮箱
		var user_email = $("#user_email");
		user_email.blur(function() {
					if (!checkEmail($.trim($("#user_email").val()))) {
						$("#user_email").val('请填写正确的邮箱帐号！');
						return false;
					}
				});
		user_email.focus(function() {
					var email_val = $("#user_email").val();
					if (email_val == '请填写正确的邮箱帐号！' || email_val == '邮箱') {
						$("#user_email").val("");
					}
				});

		// 验证码
		var user_verifycode = $("#user_verifycode");
		user_verifycode.blur(function() {
					if ($.trim($("#user_verifycode").val()) == '') {
						$("#user_verifycode").val("请填写验证码");
					}
				});
		user_verifycode.focus(function() {
					$("#user_verifycode").val("");
				});
	}

	return {

		passBlur : function() {
			if ($.trim($("#user_password").val()) == '') {
				$("#user_password")
						.replaceWith("<input id='user_password' onblur='klwork_login.passBlur();' onfocus='klwork_login.passFocus();' class='login_text1' name='password' type='text'/>");
				$("#user_password").val("密码");
			}
		},
		passFocus : function() {
			if ($.trim($("#user_password").val()) == '密码'
					|| $.trim($("#user_password").val()) == '密码错误') {
				$("#user_password")
						.replaceWith("<input id='user_password' onblur='klwork_login.passBlur();' onfocus='klwork_login.passFocus();' class='login_text1' name='password' type='password'/>");
				$("#user_password").focus();
				$("#user_password").val("");
			}
		},
		// 
		loginChecked : function() {
			/*
			 * var user_name = $("#user_email").val();
			 * if(!checkEmail($.trim($("#user_email").val()))){
			 * $("#user_email").val('请填写正确的邮箱帐号！'); return false; }
			 * if($.trim($("#user_password").val()) == '' ||
			 * $.trim($("#user_password").val()) == '密码'){
			 * $("#user_password").focus(); return false; }
			 * if($.trim($("#user_verifycode").val()) == '' ||
			 * $.trim($("#user_verifycode").val()) == '验证码'){
			 * $("#user_verifycode").val("请填写验证码"); return false; }
			 */
			var url = klwork_.path() + "/user/login-submit";
			var checkedForm = $("#login_form");
			var param = checkedForm.serialize();
			$("#login_form").submit();
			/*
			 * $.post(url, param, function(data) { if(data == 0){
			 * $("#login_form").submit(); }else if(data == 1){
			 * $("#user_verifycode").val("请填写验证码"); }else if(data == 4){
			 * $("#user_verifycode").val("验证码错误"); }else if(data == 5){
			 * $("#user_email").val('email未注册！'); }else if(data == 6){
			 * $("#user_password").replaceWith("<input id='user_password'
			 * onblur='klwork_login.passBlur();'
			 * onfocus='klwork_login.passFocus();' class='login_text1'
			 * name='password' type='text'/>"); $("#user_password").val("密码错误");
			 * }else if(data == 7){ $("#user_email").val('您的帐号已被冻结！'); }else
			 * if(data == 8){ alert('您不是微博主,请选择正确的用户类型登录');
			 * //location.href=klwork_.path()+"/user/login";
			 * location.reload(true); //$("#user_email").val('');
			 * //$("#user_password").val(''); //$("#user_verifycode").val('');
			 * }else if(data == 9){ alert('您不是企业用户,请选择正确的用户类型登录');
			 * //location.href=klwork_.path()+"/user/login";
			 * location.reload(true); //$("#user_email").val('');
			 * //$("#user_password").val(''); //$("#user_verifycode").val(''); }
			 * klwork_login.refreshVerifyCode();
			 *  } );
			 */

		},

		refreshVerifyCode : function() {
			$("#verifycodeimage").attr(
					"src",
					klwork_.path() + "/user/verifycode?now="
							+ new Date().getTime());
		},

		init : function() {
			// 为输入框添加默认字
			$('.text1').bind({
						focus : function() {
							alert("sdfd");
							if (this.value == this.defaultValue) {
								this.value = "";
							}
						},
						blur : function() {
							alert(defaultValue);
							if (this.value == "") {
								this.value = this.defaultValue;
							}
						}
					});
		}
	}
})();
$(function(){
	//为输入框添加默认字
		$('.text1').bind({ 
		focus:function(){ 
		if (this.value == this.defaultValue){ 
		this.value=""; 
		} 
		}, blur:function(){ 
		if (this.value == ""){ 
		this.value = this.defaultValue; 
		} 
		} 
		});
	});
//klwork_login.init();