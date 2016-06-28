try {
	(function ($, autosize) {
		$(function () {
			$("#page-content").addClass("loaded");
			$(".slider").slick();
			$("textarea").each(function () { autosize(this); });
		});
	})(jQuery, autosize);
} catch (e) {
	document.getElementById("page-content").className = "loaded";
}