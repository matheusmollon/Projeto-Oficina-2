/*
* Layout
*/
(function(){
   //Get saved layout type from LocalStorage
   var layoutStatus = localStorage.getItem('ma-layout-status');
   if (layoutStatus == 1) {
       $('body').addClass('sw-toggled');
       $('#tw-switch').prop('checked', true);
   }
   
   $('body').on('change', '#toggle-width input:checkbox', function(){
       if ($(this).is(':checked')) {
	   $('body').addClass('toggled sw-toggled');
	   localStorage.setItem('ma-layout-status', 1);
       }
       else {
	   $('body').removeClass('toggled sw-toggled');
	   localStorage.setItem('ma-layout-status', 0);
       }
   });
})();


$(document).ready(function(){
    /*
     * Auto Hight Textarea
     */
    if ($('.auto-size')[0]) {
	   $('.auto-size').autosize();
    }
    
    /*
     * Custom Scrollbars
     */
    function scrollbar(className, color, cursorWidth) {
        $(className).niceScroll({
            cursorcolor: color,
            cursorborder: 0,
            cursorborderradius: 0,
            cursorwidth: cursorWidth,
            bouncescroll: true,
            mousescrollstep: 100
        });
    }

    //Scrollbar for HTML but not for login page
    if (!$('.login-content')[0]) {
        scrollbar('html', 'rgba(0,0,0,0.3)', '5px');
    }
    
    //Scrollbar Tables
    if ($('.table-responsive')[0]) {
        scrollbar('.table-responsive', 'rgba(0,0,0,0.5)', '5px');
    }
    
    //Scrill bar for Chosen
    if ($('.chosen-results')[0]) {
        scrollbar('.chosen-results', 'rgba(0,0,0,0.5)', '5px');
    }
    
    //Scroll bar for tabs
    if ($('.tab-nav')[0]) {
        scrollbar('.tab-nav', 'rgba(0,0,0,0.2)', '2px');
    }

    //Scroll bar for dropdowm-menu
    if ($('.dropdown-menu .c-overflow')[0]) {
        scrollbar('.dropdown-menu .c-overflow', 'rgba(0,0,0,0.5)', '0px');
    }

    //Scrollbar for rest
    if ($('.c-overflow')[0]) {
        scrollbar('.c-overflow', 'rgba(0,0,0,0.5)', '5px');
    }
    
    /*
    * Profile Menu
    */
    $('body').on('click', '.profile-menu > a', function(e){
        e.preventDefault();
        $(this).parent().toggleClass('toggled');
	    $(this).next().slideToggle(200);
    });

    
    
    /*
     * Input Mask
     */
    if ($('input-mask')[0]) {
	$('.input-mask').mask();
    }
    
    /*
     * Color Picker
     */
    if ($('.color-picker')[0]) {
	$('.color-picker').each(function(){
	    $('.color-picker').each(function(){
		var colorOutput = $(this).closest('.cp-container').find('.cp-value');
		$(this).farbtastic(colorOutput);
	    });
	});
    }
    
    /*
     * HTML Editor
     */
    if ($('.html-editor')[0]) {
	$('.html-editor').summernote({
	    height: 150
	});
    }
    
    if($('.html-editor-click')[0]) {
	//Edit
	$('body').on('click', '.hec-button', function(){
	    $('.html-editor-click').summernote({
		focus: true
	    });
	    $('.hec-save').show();
	})
	
	//Save
	$('body').on('click', '.hec-save', function(){
	    $('.html-editor-click').code();
	    $('.html-editor-click').destroy();
	    $('.hec-save').hide();
	    notify('Content Saved Successfully!', 'success');
	});
    }
    
    //Air Mode
    if($('.html-editor-airmod')[0]) {
	$('.html-editor-airmod').summernote({
	    airMode: true
	});
    }
    
    /*
     * Date Time Picker
     */
    
    //Date Time Picker
    if ($('.date-time-picker')[0]) {
	   $('.date-time-picker').datetimepicker();
    }
    
    //Time
    if ($('.time-picker')[0]) {
    	$('.time-picker').datetimepicker({
    	    format: 'LT'
    	});
    }
    
    //Date
    if ($('.date-picker')[0]) {
    	$('.date-picker').datetimepicker({
    	    format: 'DD/MM/YYYY'
    	});
    }

    
    /*
     * Lightbox
     */
    if ($('.lightbox')[0]) {
        $('.lightbox').lightGallery({
            enableTouch: true
        }); 
    }
    
    /*
     * Link prevent
     */
    $('body').on('click', '.a-prevent', function(e){
        e.preventDefault();
    });
    
    /*
     * Collaspe Fix
     */
    if ($('.collapse')[0]) {
        
        //Add active class for opened items
        $('.collapse').on('show.bs.collapse', function (e) {
            $(this).closest('.panel').find('.panel-heading').addClass('active');
        });
   
        $('.collapse').on('hide.bs.collapse', function (e) {
            $(this).closest('.panel').find('.panel-heading').removeClass('active');
        });
        
        //Add active class for pre opened items
        $('.collapse.in').each(function(){
            $(this).closest('.panel').find('.panel-heading').addClass('active');
        });
    }
    
   
});
