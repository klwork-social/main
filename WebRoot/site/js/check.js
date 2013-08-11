var klwork_check = (function() {
	return {
		/**
		 * 检查输入的邮箱格式是否正确 输入:str 字符串 返回:true 或 flase; true表示格式正确
		 */
		checkEmail : function checkEmail(str) {
			if (str
					.match(/[A-Za-z0-9_-]+[@](\S*)(net|com|cn|org|cc|tv|[0-9]{1,3})(\S*)/g) == null) {
				return false;
			} else {
				return true;
			}
		}
	}
})();