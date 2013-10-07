$(function(){
			
    //产品演示
	  var $fix2_click = $(".fix_nav2 ul li");
	  $fix2_click.click(function(){
		  $(this).children().addClass("fix_current2").parent().siblings().children().removeClass("fix_current2");
	      var index = $fix2_click.index(this); //获取当前的索引
	      $(".fix2_tab").eq(index).show().siblings(".fix2_tab").hide();  
		  });
		  
	//abour us
	  var $about_click = $(".a_nav ul li");
	  $about_click.click(function(){
		  $(this).children().addClass("a_current").parent().siblings().children().removeClass("a_current");
	      var index = $about_click.index(this); //获取当前的索引
	      $(".a_c").eq(index).show().siblings(".a_c").hide();  
		  });
    
	//底部页面链接
	$(".intr1").click(function(){
		$(".intr1_c").show().siblings(".a_c").hide();
		});
})


	