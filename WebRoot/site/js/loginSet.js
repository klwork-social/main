var klwork_loginSet = (function() {

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
		//完善信息的提交 
		perfectSubmit : function() {
			var user_name = $("#email").val();
			if (!klwork_check.checkEmail($.trim($("#email").val()))) {
				$("#email").val('请填写正确的邮箱帐号！');
				return false;
			}
			var url = klwork_.path() + "/user/perfectSubmit";
			var checkedForm = $("#login_form");
			var param = checkedForm.serialize();
			// $("#login_form").submit();

			$.post(url, param, function(data) {
						if (data.success) {
							window.location.href= klwork_.path() + "/";
						} else {
							alert(data.errorMessage);
						}
					});

		},
		//绑定信息的提交
		bindSubmit : function() {
			var url = klwork_.path() + "/user/bindSubmit";
			var checkedForm = $("#bind_form");
			var param = checkedForm.serialize();
			$.post(url, param, function(data) {
						if (data.success) {
							window.location.href= klwork_.path() + "/";
						} else {
							alert(data.errorMessage);
						}
					});

		},

		refreshVerifyCode : function() {
			$("#verifycodeimage").attr(
					"src",
					klwork_.path() + "/user/verifycode?now="
							+ new Date().getTime());
		},

		init : function() {
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
		
	//切换层
	  var $tab_click = $(".login_tab span");
	  $tab_click.click(function(){
		  $(this).addClass("tab1").siblings().removeClass("tab1");
	      var index = $tab_click.index(this); //获取当前的索引
	      $(".login_all").eq(index).show().siblings(".login_all").hide();  
		  });
	});
