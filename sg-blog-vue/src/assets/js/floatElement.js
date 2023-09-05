export default function floatElement (id) {
    //  代码内容
    $(window).scroll(function(e){ 
        var $el = $('.floatElement'); 
        var isPositionFixed = ($el.css('position') == 'fixed');
        if ($(this).scrollTop() > 200 && !isPositionFixed){ 
          $el.css({'position': 'fixed', 'top': '0px'}); 
        }
        if ($(this).scrollTop() < 200 && isPositionFixed){
          $el.css({'position': 'static', 'top': '0px'}); 
        } 
      });
      console.log("ccc");
}

