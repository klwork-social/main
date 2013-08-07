
var klwork_ = (function() {
	// console.log("am_构造开始");

	// 缓存页面上的全局变量 params
	var winParams = window.params || {};

	// 配置
	var config = {
		userState : winParams.userState || null, // 用户激活状态
		userActJs : '/js/test.js' // 
	};

	return {
		// 日志
		log : function(msg) {
			if (typeof console != 'undefined') {
				console.log(msg);
			} else {
				
			}
		},
		
		//将字符串中的s1全部替换成s2
		replaceAll : function(old ,s1 ,s2) { 
		    return old.replace(new RegExp(s1,"gm"),s2); 
		},
		
		// 可以调用的方法
		path : function() {
			if (winParams.path) {
				return winParams.path;
			} else {
				return "";
			}
		},
		// 初始化全局变量 params
		initGlobalParam : function(data) {
			winParams.path = data.path;
		},
		// 得到参数对象
		getWinParams : function() {
			return winParams;
		},
		// 初始化
		init : function() {
		},
		
		/**
		 * @pageUrl 
		 * @func  请求成功后的回调函数
		 * @dataType ajax返回的类型,默认为json
		 * @async 同步还是异步，默认为异步
		 */
		ajaxSubmit : function(/* String */pageUrl,/* function */func, /* String */params, dataType,async) {
			var vParams = "";
			var vAsync = true;
			var vFunc = func;
			var vDataType = "json";
			
			if(arguments.length > 2){
				if(params){//传递的参数
					vParams = params;
				}
			}
			
			if(arguments.length > 3){
				if(dataType){
					vDataType = dataType;
				}
			}
			
			if(arguments.length > 4){//进行同步设置
				vAsync = async;
			}
			
			$.ajax({
					url : pageUrl,
					async : vAsync,// false为同步
					type : "post",
					data : vParams,
					dataType : vDataType,
					success : function(data) {
						vFunc(data);
					}
				});
		}
	}
})();