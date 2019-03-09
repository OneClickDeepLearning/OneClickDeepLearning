 $(function() {
		        $('.d-firstNav').click(function() {
		            dropSwift($(this), '.d-firstDrop');
		        });
		        $('.d-secondNav').click(function() {
		            dropSwift($(this), '.d-secondDrop');
		        });
		        /**
		         * @param dom   点击当前的元素
		         * @param drop  处理下拉菜单
		         */
		        function dropSwift(dom, drop) {
		            dom.next().slideToggle();
		            dom.parent().siblings().find('.icon-chevron-up').removeClass('iconRotate');
		            dom.parent().siblings().find(drop).slideUp();
		            var iconChevron = dom.find('.icon-chevron-up');
		            if (iconChevron.hasClass('iconRotate')) {
		                iconChevron.removeClass('iconRotate');
		            } else {
		                iconChevron.addClass('iconRotate');
		            }
		        }
		    })
