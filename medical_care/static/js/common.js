
// 导航相关事件
function menuFunc() {
	var Oparant = $('#header');
	var Omenu = $('#menu_box');
	Oparant.find('.menu').click(function() {
	  var ismenu = $(this).hasClass('menu_img');
	  $(this).addClass('hide');
	  $(this).siblings('img').removeClass('hide');
	  if(ismenu) {	//判断点击当前对象是否显示导航
	    $('body').css('overflow','hidden');
	    Omenu.removeClass('hide');
	  }else {
	    $('body').css('overflow','auto');
	    Omenu.addClass('hide');          
	  }
	});


} 


//判断是否为空
function isNull(str) {
	if(!str || str === "" || str === null || str.length === 0) {
		return true;
	}
	return false;
}


// tab切换事件
function changeTab(obj) {
	obj.click(function() {
		var hasCas = $(this).hasClass('active');

		if(!hasCas) {
			$(this).addClass('active');
			$(this).siblings().removeClass('active');
		}
	})
}


// 返回顶部事件
function backTop() {

  scrollTo(0,0);
}


// 监听滚动条距顶部高度
window.onscroll = function() {
  var topVal = document.documentElement.scrollTop || document.body.scrollTop;

  if(topVal > 200) {
    $('.backTop').removeClass('hide');
  }else {
    $('.backTop').addClass('hide');
  }
}