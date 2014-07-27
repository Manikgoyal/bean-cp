$(document).ready(function() {
	var app = Sammy('#page-main', function() {
	
	var $pageMain = $("#page-main");
	var $pageMenu = $("#page-menu");
	var $root = $("html,body");
	var $body = $("body");
	var $window = $(window);
	var $document = $(document);
	
	var currentPage = null;
	
	function _scrollToAnchor(address) {
		var anchor = pageList.getAnchorOnPage(address);
		
		if (anchor != null) {
			var targetOffset = $("#" + anchor).offset().top - 20;
			
			$root.animate({scrollTop: targetOffset}, "fast");
		}
	}
	
	function _setupTableOfContentsPosition() {
		$window.scroll(function() {
			if ($document.scrollTop() > 130) {
				$body.addClass("pin-table-of-contents");
			} else {
				$body.removeClass("pin-table-of-contents");
			}
		});
	}

	function _loadWithNoSecurityCheck(address) {
		var pagePath = pageList.getPagePath(address);
	
		if (currentPage != pagePath) {
			$pageMain.load("pages/" + pagePath + ".html #page-main-content", function() {
				_updateMenuStatus(pagePath);
				SyntaxHighlighter.highlight();
				_setupTableOfContentsPosition();
				
				_scrollToAnchor(address);
				currentPage = pagePath;
			});
		} else {
			_scrollToAnchor(address);
		}
	}
	
	function _updateMenuStatus(address) {
		$pageMenu.find("a.selected").each(function() {
			$(this).removeClass("selected");
		});
		
		$pageMenu.find("a[href=#" + pageList.getPagePath(address) + "]").addClass("selected");
	}

	function load(address) {
		if (pageList.isPageEnabled(address)) {
			_loadWithNoSecurityCheck(address);
		} else {
			_loadWithNoSecurityCheck("not-found");
		}
	}

	this.get('#:name', function() {
			load(this.params['name']);
		});
	});

	function loadModuleCssFile(pageName) {
		var cssLocation = "css/" + pageName + ".css";
	
		$.get(cssLocation, function(css) {
		   $('<style type="text/css"></style>')
			  .html(css)
			  .appendTo("head");
		});
	}

	function loadAllModuleCssFiles() {
		var pages = pageList.getPageList();
		
		for (i = 0; i < pages.length; i++) {
			loadModuleCssFile(pages[i]);
		}
	}
	
	loadAllModuleCssFiles();
	
	// start the application
	app.run('#home');
});
